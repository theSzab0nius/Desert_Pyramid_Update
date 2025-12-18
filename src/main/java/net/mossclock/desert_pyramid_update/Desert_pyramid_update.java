package net.mossclock.desert_pyramid_update;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.mossclock.desert_pyramid_update.block.ModBlocks;
import net.mossclock.desert_pyramid_update.block.entity.ModBlockEntities;
import net.mossclock.desert_pyramid_update.effect.ModEffects;
import net.mossclock.desert_pyramid_update.enchantment.ModEnchantmentEffects;
import net.mossclock.desert_pyramid_update.item.ModItems;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.argument.EntityArgumentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class Desert_pyramid_update implements ModInitializer {
    public static final String MOD_ID = "desert_pyramid_update";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Identifier BLESSINGS_ID = Identifier.of(MOD_ID, "blessings");
    public static AttachmentType<BlessingsData> PLAYER_BLESSINGS;

    @Override
    public void onInitialize() {
        ModEnchantmentEffects.bootstrap();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModBlockEntities.registerAll();
        ModEffects.registerEffects();

        Registry.register(
                Registries.SOUND_EVENT,
                Identifier.of(MOD_ID, "block.trapped_limestone.cycle"),
                ModSounds.TRAPPED_LIMESTONE_CYCLE
        );

        PLAYER_BLESSINGS = AttachmentRegistry.createPersistent(
                BLESSINGS_ID,
                BlessingsData.CODEC
        );

        // Tick loop: refresh beacon-style effects every 5 ticks
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.getTime() % 5 != 0) return;
            for (ServerPlayerEntity player : world.getPlayers()) {
                applyBeaconStyleEffects(player);
            }
        });

        // Commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(CommandManager.literal("blessing")
                    .then(CommandManager.literal("apply")
                            .then(CommandManager.argument("player", EntityArgumentType.player())
                                    .then(CommandManager.argument("blessing", StringArgumentType.word())
                                            .suggests((context, builder) -> getBlessingSuggestions(builder))
                                            .executes(context -> {
                                                ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
                                                String blessingName = StringArgumentType.getString(context, "blessing");

                                                try {
                                                    Blessing blessing = Blessing.valueOf(blessingName.toUpperCase());
                                                    grantBlessing(target, blessing);
                                                    context.getSource().sendFeedback(
                                                            () -> Text.of("Applied blessing " + blessingName + " to " + target.getName().getString()),
                                                            false
                                                    );
                                                } catch (IllegalArgumentException e) {
                                                    context.getSource().sendError(Text.of("Invalid blessing name: " + blessingName));
                                                }
                                                return 1;
                                            })
                                    )
                            )
                    )
                    .then(CommandManager.literal("revoke")
                            .then(CommandManager.argument("player", EntityArgumentType.player())
                                    .then(CommandManager.argument("blessing", StringArgumentType.word())
                                            .suggests((context, builder) -> getBlessingSuggestions(builder))
                                            .executes(context -> {
                                                ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
                                                revokeBlessing(target);
                                                context.getSource().sendFeedback(
                                                        () -> Text.of("Revoked blessings from " + target.getName().getString()),
                                                        false
                                                );
                                                return 1;
                                            })
                                    )
                            )
                    )
                    .then(CommandManager.literal("list")
                            .then(CommandManager.argument("player", EntityArgumentType.player())
                                    .executes(context -> {
                                        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
                                        BlessingsData data = target.getAttachedOrCreate(PLAYER_BLESSINGS, BlessingsData::new);

                                        if (data.activeBlessings().isEmpty()) {
                                            context.getSource().sendFeedback(() -> Text.of(target.getName().getString() + " has no active blessings."), false);
                                        } else {
                                            String blessings = String.join(", ", data.activeBlessings());
                                            context.getSource().sendFeedback(() -> Text.of(target.getName().getString() + " active blessings: " + blessings), false);
                                        }
                                        return 1;
                                    })
                            )
                    )
            );
        });
    }

    // --- ATTRIBUTE LOGIC ---
    public static void grantBlessing(ServerPlayerEntity player, Blessing blessing) {
        var current = player.getAttachedOrCreate(PLAYER_BLESSINGS, BlessingsData::new);
        player.setAttached(PLAYER_BLESSINGS, current.add(blessing.id));

        EntityAttributeInstance inst = player.getAttributeInstance(blessing.attribute);
        if (inst != null) {
            EntityAttributeModifier modifier = blessing.createModifier();
            Identifier modId = Identifier.of("desert_pyramid_update", blessing.id);
            if (!inst.hasModifier(modId)) {
                inst.addPersistentModifier(modifier);
            }
        }

        if (blessing.attribute == EntityAttributes.GENERIC_MAX_HEALTH) {
            player.heal((float) blessing.value);
        }
    }

    public static void revokeBlessing(ServerPlayerEntity player) {
        player.setAttached(PLAYER_BLESSINGS, new BlessingsData());

        for (Blessing b : Blessing.values()) {
            if (b.statusEffect != null) {
                player.removeStatusEffect(b.statusEffect);
            }

            var inst = player.getAttributeInstance(b.attribute);
            if (inst != null) {
                inst.removeModifier(b.createModifier());
            }
        }

        float max = player.getMaxHealth();
        if (player.getHealth() > max) {
            player.setHealth(max);
        }
    }

    // --- VISUAL EFFECTS ---
    private static void applyBeaconStyleEffects(ServerPlayerEntity player) {
        var data = player.getAttachedOrCreate(PLAYER_BLESSINGS, BlessingsData::new);
        for (String id : data.activeBlessings()) {
            try {
                Blessing b = Blessing.valueOf(id.toUpperCase());
                if (b.statusEffect != null) {
                    player.addStatusEffect(new StatusEffectInstance(
                            b.statusEffect,
                            200,   // 10s
                            0,
                            true,  // ambient (beacon-like)
                            false, // no particles
                            true   // show icon
                    ));
                }
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static CompletableFuture<Suggestions> getBlessingSuggestions(SuggestionsBuilder builder) {
        for (Blessing b : Blessing.values()) {
            builder.suggest(b.name().toLowerCase());
        }
        return builder.buildFuture();
    }
}

package net.mossclock.desert_pyramid_update;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.block.ModBlocks;
import net.mossclock.desert_pyramid_update.block.entity.ModBlockEntities;
import net.mossclock.desert_pyramid_update.enchantment.ModEnchantmentEffects;
import net.mossclock.desert_pyramid_update.item.ModItems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        Registry.register(
                Registries.SOUND_EVENT,
                Identifier.of(MOD_ID, "block.trapped_limestone.cycle"),
                ModSounds.TRAPPED_LIMESTONE_CYCLE
        );

        // Register persistent attachment (saves to player.dat, no sync needed)
        PLAYER_BLESSINGS = AttachmentRegistry.createPersistent(
                BLESSINGS_ID,
                BlessingsData.CODEC
        );

        // Apply blessings every world tick
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (ServerPlayerEntity player : world.getPlayers()) {
                applyBlessings(player);
            }
        });
    }

    // ONE clean method – applies all active blessings
    public static void applyBlessings(PlayerEntity player) {
        BlessingsData data = player.getAttachedOrCreate(PLAYER_BLESSINGS);

        for (String id : data.activeBlessings()) {
            try {
                Blessing blessing = Blessing.valueOf(id.toUpperCase());
                EntityAttributeInstance instance = player.getAttributeInstance(blessing.attribute);
                if (instance != null) {
                    // In 1.21, same Identifier = overwrites old modifier automatically
                    // No need to remove by UUID — just add!
                    instance.addPersistentModifier(blessing.createModifier());
                }

                // Instantly heal extra hearts from max health blessings
                if (blessing.attribute == EntityAttributes.GENERIC_MAX_HEALTH) {
                    player.heal((float) blessing.value);
                }
            } catch (IllegalArgumentException ignored) {
                // Invalid blessing ID — silently skip
            }
        }
    }

    public static void grantBlessing(PlayerEntity player, Blessing blessing) {
        BlessingsData current = player.getAttachedOrCreate(PLAYER_BLESSINGS);
        player.setAttached(PLAYER_BLESSINGS, current.add(blessing.id));
    }

    public static void revokeBlessing(PlayerEntity player, Blessing blessing) {
        BlessingsData current = player.getAttachedOrCreate(PLAYER_BLESSINGS);
        player.setAttached(PLAYER_BLESSINGS, current.remove(blessing.id));
    }
}
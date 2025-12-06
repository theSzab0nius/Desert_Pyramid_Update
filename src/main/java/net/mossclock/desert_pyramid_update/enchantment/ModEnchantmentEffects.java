package net.mossclock.desert_pyramid_update.enchantment;

import com.mojang.serialization.MapCodec;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;
import net.mossclock.desert_pyramid_update.enchantment.custom.CobraStrike;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantmentEffects {
    public static final MapCodec<? extends EnchantmentEntityEffect> desert_pyramid_update =
            registerEntityEffect("cobra_strike", CobraStrike.CODEC);


    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name,
                                                                                    MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(Desert_pyramid_update.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        Desert_pyramid_update.LOGGER.info("Registering Mod Enchantment Effects for " + Desert_pyramid_update.MOD_ID);
    }
}

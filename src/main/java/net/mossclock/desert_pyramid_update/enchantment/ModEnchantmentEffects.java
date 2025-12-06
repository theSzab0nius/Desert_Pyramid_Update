package net.mossclock.desert_pyramid_update.enchantment;

import com.mojang.serialization.MapCodec;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;
import net.mossclock.desert_pyramid_update.enchantment.custom.CobraStrike;
import net.mossclock.desert_pyramid_update.enchantment.custom.SandsOfTime;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.enchantment.custom.SunForged;

public class ModEnchantmentEffects {

    // One unique field per effect — different names!
    public static final MapCodec<? extends EnchantmentEntityEffect> COBRA_STRIKE;
    public static final MapCodec<? extends EnchantmentEntityEffect> SANDS_OF_TIME;
    public static final MapCodec<? extends EnchantmentEntityEffect> SUN_FORGED;

    // Add more here later if you want
    // public static final MapCodec<? extends EnchantmentEntityEffect> PHARAOHS_CURSE;

    static {
        COBRA_STRIKE = register("cobra_strike", CobraStrike.CODEC);
        SANDS_OF_TIME = register("sands_of_time", SandsOfTime.CODEC);
        SUN_FORGED = register("sun_forged", SunForged.CODEC);
        // PHARAOHS_CURSE = register("pharaohs_curse", PharaohsCurse.CODEC);
    }

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String name, MapCodec<T> codec) {
        return Registry.register(
                Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
                Identifier.of(Desert_pyramid_update.MOD_ID, name),
                codec
        );
    }

    // Optional: keep this if you like seeing the log message
    public static void bootstrap() {
        Desert_pyramid_update.LOGGER.info("Registered Mod Enchantment Effects for " + Desert_pyramid_update.MOD_ID);
    }
}
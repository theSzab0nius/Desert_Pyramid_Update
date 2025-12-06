package net.mossclock.desert_pyramid_update.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record SandsOfTime() implements EnchantmentEntityEffect {
    public static final MapCodec<SandsOfTime> CODEC = MapCodec.unit(SandsOfTime::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof LivingEntity victim) {
            int duration;
            int amplifier;

            switch (level) {
                case 1 -> {
                    duration = 40;
                    amplifier = 0;
                } // 2 sec, Poison I
                case 2 -> {
                    duration = 80;
                    amplifier = 1;
                } // 4 sec, Poison II
                case 3 -> {
                    duration = 120;
                    amplifier = 2;
                } // 6 sec, Poison III
                default -> {
                    duration = 40;
                    amplifier = 0;
                }
            }

            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, amplifier));
        }
    }


    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }

}

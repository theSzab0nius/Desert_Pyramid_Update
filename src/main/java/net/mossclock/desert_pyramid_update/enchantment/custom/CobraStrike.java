package net.mossclock.desert_pyramid_update.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record CobraStrike() implements EnchantmentEntityEffect {
    public static final MapCodec<CobraStrike> CODEC = MapCodec.unit(CobraStrike::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {

        if (user instanceof LivingEntity target) {
            if (level == 1) {
                double chance = 0.05;
                if (world.random.nextDouble() < chance) {
                    int duration = 40; // 2 seconds in ticks
                    int amplifier = 0; // Poison I

                    // Apply the poison effect to the target
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, duration, amplifier));
                }
            }
            if(level == 2) {
                double chance = 0.05;
                if (world.random.nextDouble() < chance) {
                    int duration = 80; // 2 seconds in ticks
                    int amplifier = 0; // Poison I

                    // Apply the poison effect to the target
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, duration, amplifier));
                }
            }
            if(level == 3) {
                double chance = 0.05;
                if (world.random.nextDouble() < chance) {
                    int duration = 80; // 2 seconds in ticks
                    int amplifier = 1; // Poison I

                    // Apply the poison effect to the target
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, duration, amplifier));
                }
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }

}

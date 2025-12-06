package net.mossclock.desert_pyramid_update.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;

public record SunForged() implements EnchantmentEntityEffect {
    public static final MapCodec<SunForged> CODEC = MapCodec.unit(SunForged::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof LivingEntity victim  && !victim.getType().isIn(EntityTypeTags.UNDEAD)) {
            // Only apply if nothing is above your head and it's daytime
            BlockPos attackerPos = user.getBlockPos();
            boolean inDirectDaylight = world.isSkyVisible(attackerPos) &&
                    world.getTimeOfDay() % 24000 < 12000; // daytime

            int amplifier = 0;
            if (inDirectDaylight) {
                switch(level) {
                    case 1 -> {
                        amplifier = 0;
                    } // 2 sec, Poison I
                    case 2 -> {
                        amplifier = 1;
                    }
                }
                    victim.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, amplifier, false, false));
            }
        }
    }



    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }

}

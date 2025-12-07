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
import net.minecraft.util.math.Vec3d;

public record PharaohsCurse() implements EnchantmentEntityEffect {
    public static final MapCodec<PharaohsCurse> CODEC = MapCodec.unit(PharaohsCurse::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof LivingEntity victim) {
            int duration = 400;
            int amplifier = 3;


            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, amplifier, false, false));
            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, duration, amplifier, false, false));
        }
    }



    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }

}

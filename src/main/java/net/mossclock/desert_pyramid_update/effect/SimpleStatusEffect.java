package net.mossclock.desert_pyramid_update.effect;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class SimpleStatusEffect extends StatusEffect {

    public SimpleStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }


}
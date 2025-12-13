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

    public SimpleStatusEffect addAttribute(
            EntityAttribute attribute,
            String modifierName,
            double amount,
            EntityAttributeModifier.Operation operation
    ) {
        RegistryEntry<EntityAttribute> entry = Registries.ATTRIBUTE.getEntry(attribute);
        Identifier modifierId = Identifier.of("mossclock", modifierName);
        this.addAttributeModifier(entry, modifierId, amount, operation);
        return this;
    }


}
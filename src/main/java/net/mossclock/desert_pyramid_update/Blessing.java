package net.mossclock.desert_pyramid_update;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.effect.ModEffects;

public enum Blessing {


    BLESSING_OF_KHERT(
            "blessing_of_khert",
            ModEffects.KHERT_BLESSING,
            EntityAttributes.GENERIC_ARMOR,
            2.0
    ),


    BLESSING_OF_MNEMON(
            "blessing_of_mnemon",
            ModEffects.MNEMON_INSIGHT,
            EntityAttributes.GENERIC_ATTACK_SPEED,
            0.15
    ),


    BLESSING_OF_BELLATOR(
            "blessing_of_bellator",
            ModEffects.BELLATOR_FURY,
            EntityAttributes.GENERIC_ATTACK_DAMAGE,
            1.0
    ),


    BLESSING_OF_IXKALI(
            "blessing_of_ixkali",
            ModEffects.IXKALI_VIGOR,
            EntityAttributes.GENERIC_JUMP_STRENGTH,
            0.08
            ),

    BLESSING_OF_AVASURA(
            "blessing_of_avasura",
            ModEffects.AVASURA_PEACE,
            EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
            0.2
    ),


    BLESSING_OF_ILLAPACHARI(
            "blessing_of_illapachari",
            ModEffects.ILLAPACHARI_SPEED,
            EntityAttributes.GENERIC_SAFE_FALL_DISTANCE,
            0.5
    ),


    BLESSING_OF_HOSHIKARI(
            "blessing_of_hoshikari",
            ModEffects.HOSHIKARI_FORTUNE,
            EntityAttributes.GENERIC_LUCK,
            1.0
    ),


    BLESSING_OF_MOR_EZEN(
            "blessing_of_mor_ezen",
            ModEffects.MOR_EZEN_GRACE,
            EntityAttributes.GENERIC_MOVEMENT_SPEED,
            0.05
    ),


    BLESSING_OF_NYASARI(
            "blessing_of_nyasari",
            ModEffects.NYASARI_VITALITY,
            EntityAttributes.GENERIC_MAX_HEALTH,
            2.0
    ),

    BLESSING_OF_UMBRATH(
    "blessing_of_umbrath",
    ModEffects.UMBRATH_SHADE,
    EntityAttributes.PLAYER_SNEAKING_SPEED,
    0.15
    ),

    BLESSING_OF_FORGATHOR(
            "blessing_of_forgathor",
            ModEffects.FORGATHOR_FORGE,
            EntityAttributes.PLAYER_MINING_EFFICIENCY,
            10.0
    );

    public final String id;

    public final RegistryEntry<EntityAttribute> attribute;
    public final RegistryEntry<StatusEffect> statusEffect;

    public final double value;

    Blessing(
            String id,
            RegistryEntry<StatusEffect> statusEffect,  // changed
            RegistryEntry<EntityAttribute> attribute,
            double value
    ) {
        this.id = id;
        this.statusEffect = statusEffect;
        this.attribute = attribute;
        this.value = value;
    }

    public boolean isAttributeBlessing() {
        return attribute != null;
    }

    public boolean isStatusEffectBlessing() {
        return statusEffect != null;
    }

    public EntityAttributeModifier createModifier() {
        return new EntityAttributeModifier(
                Identifier.of("desert_pyramid_update", id),
                value,
                EntityAttributeModifier.Operation.ADD_VALUE
        );
    }
}

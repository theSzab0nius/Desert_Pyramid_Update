package net.mossclock.desert_pyramid_update;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;


import java.util.UUID;

public enum Blessing {
    KHERT_BLESSING("khert_blessing", EntityAttributes.GENERIC_ARMOR_TOUGHNESS, "khert-toughness", 4.0, EntityAttributeModifier.Operation.ADD_VALUE),
    MNEMON_INSIGHT("mnemon_insight", EntityAttributes.GENERIC_ATTACK_SPEED, "mnemon-haste", 0.4, EntityAttributeModifier.Operation.ADD_VALUE),
    BELLATOR_FURY("bellator_fury", EntityAttributes.GENERIC_ATTACK_DAMAGE, "bellator-strength", 1.0, EntityAttributeModifier.Operation.ADD_VALUE),
    IXKALI_VIGOR("ixkali_vigor", EntityAttributes.GENERIC_MAX_HEALTH, "ixkali-health", 4.0, EntityAttributeModifier.Operation.ADD_VALUE),
    AVASURA_PEACE("avasura_peace", EntityAttributes.GENERIC_ARMOR, "avasura-armor", 4.0, EntityAttributeModifier.Operation.ADD_VALUE),
    ILLAPACHARI_SPEED("illapachari_speed", EntityAttributes.GENERIC_MOVEMENT_SPEED, "illapa-speed", 0.2, EntityAttributeModifier.Operation.ADD_VALUE),
    HOSHIKARI_FORTUNE("hoshikari_fortune", EntityAttributes.GENERIC_LUCK, "hoshikari-luck", 2.0, EntityAttributeModifier.Operation.ADD_VALUE),
    MOR_EZEN_GRACE("mor_ezen_grace", EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, "mor-slowfall", 100.0, EntityAttributeModifier.Operation.ADD_VALUE),
    NYASARI_VITALITY("nyasari_vitality", EntityAttributes.GENERIC_MAX_HEALTH, "nyasari-boost", 2.0, EntityAttributeModifier.Operation.ADD_VALUE);

    public final String id;
    public final RegistryEntry<EntityAttribute> attribute;  // Changed type!
    public final String modifierName;
    public final double value;
    public final EntityAttributeModifier.Operation operation;
    public final UUID modifierUUID;

    Blessing(String id, RegistryEntry<EntityAttribute> attribute, String modifierName, double value, EntityAttributeModifier.Operation operation) {
        this.id = id;
        this.attribute = attribute;
        this.modifierName = modifierName;
        this.value = value;
        this.operation = operation;
        this.modifierUUID = UUID.nameUUIDFromBytes((id + modifierName).getBytes());
    }

    public EntityAttributeModifier createModifier() {
        Identifier modifierId = Identifier.of("desert_pyramid_update", this.id + "_blessing");
        return new EntityAttributeModifier(modifierId, value, operation);
    }
}
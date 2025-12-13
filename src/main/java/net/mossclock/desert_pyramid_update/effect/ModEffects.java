package net.mossclock.desert_pyramid_update.effect;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {

    private static StatusEffect register(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, Identifier.of("desert_pyramid_update", name), effect);
    }

    public static final StatusEffect KHERT_BLESSING = register("khert_blessing",
            new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x8B4513)
                    .addAttribute(
                            EntityAttributes.GENERIC_ARMOR_TOUGHNESS.value(),
                            "khert-toughness",
                            4.0,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    public static final StatusEffect MNEMON_INSIGHT = register("mnemon_insight",
            new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFD700)
                    .addAttribute(
                            EntityAttributes.GENERIC_ATTACK_SPEED.value(),
                            "mnemon-haste",
                            0.4,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    public static final StatusEffect BELLATOR_FURY = register("bellator_fury",
            new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x8B0000)
                    .addAttribute(
                            EntityAttributes.GENERIC_ATTACK_DAMAGE.value(),
                            "bellator-strength",
                            1.0,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    // Mesoamerican – Health Boost + Regen feel
    public static final StatusEffect IXKALI_VIGOR = register("ixkali_vigor",
            new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFD700)
                    .addAttribute(
                            EntityAttributes.GENERIC_MAX_HEALTH.value(),
                            "ixkali-health",
                            4.0,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    // South/East Asian – Resistance (via Armor)
    public static final StatusEffect AVASURA_PEACE = register("avasura_peace",
            new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00CED1)
                    .addAttribute(
                            EntityAttributes.GENERIC_ARMOR.value(),
                            "avasura-armor",
                            4.0,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    // Incan – Speed
    public static final StatusEffect ILLAPACHARI_SPEED = register("illapachari_speed",
            new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x4B0082)
                    .addAttribute(
                            EntityAttributes.GENERIC_MOVEMENT_SPEED.value(),
                            "illapa-speed",
                            0.2,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    // East Asian – Luck
    public static final StatusEffect HOSHIKARI_FORTUNE = register("hoshikari_fortune",
            new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFFF00)
                    .addAttribute(
                            EntityAttributes.GENERIC_LUCK.value(),
                            "hoshikari-luck",
                            2.0,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    // Steppe – Slow Falling
    public static final StatusEffect MOR_EZEN_GRACE = register("mor_ezen_grace",
            new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x87CEEB)
                    .addAttribute(
                            EntityAttributes.GENERIC_SAFE_FALL_DISTANCE.value(),
                            "mor-slowfall",
                            100.0,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    // African – Health Boost
    public static final StatusEffect NYASARI_VITALITY = register("nyasari_vitality",
            new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00FF00)
                    .addAttribute(
                            EntityAttributes.GENERIC_MAX_HEALTH.value(),
                            "nyasari-boost",
                            2.0,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    public static void registerEffects() {
        try {
            Class.forName("net.mossclock.desert_pyramid_update.effect.ModEffects");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // force class initialization
        var ignore1 = KHERT_BLESSING;
        var ignore2 = MNEMON_INSIGHT;
        var ignore3 = BELLATOR_FURY;
        var ignore4 = IXKALI_VIGOR;
        var ignore5 = AVASURA_PEACE;
        var ignore6 = ILLAPACHARI_SPEED;
        var ignore7 = HOSHIKARI_FORTUNE;
        var ignore8 = MOR_EZEN_GRACE;
        var ignore9 = NYASARI_VITALITY;
    }
}
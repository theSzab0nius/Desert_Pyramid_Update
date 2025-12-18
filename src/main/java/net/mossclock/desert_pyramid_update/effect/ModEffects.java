package net.mossclock.desert_pyramid_update.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {

    // Register the raw StatusEffect instances
    private static StatusEffect register(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, Identifier.of("desert_pyramid_update", name), effect);
    }

    // Your custom effects (still needed for instantiation)
    public static final StatusEffect KHERT_BLESSING_RAW = new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x8B4513);
    public static final StatusEffect MNEMON_INSIGHT_RAW = new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFD700);
    public static final StatusEffect BELLATOR_FURY_RAW = new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x8B0000);
    public static final StatusEffect IXKALI_VIGOR_RAW = new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFD700);
    public static final StatusEffect AVASURA_PEACE_RAW = new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00CED1);
    public static final StatusEffect ILLAPACHARI_SPEED_RAW = new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x4B0082);
    public static final StatusEffect HOSHIKARI_FORTUNE_RAW = new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFFF00);
    public static final StatusEffect MOR_EZEN_GRACE_RAW = new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x87CEEB);
    public static final StatusEffect NYASARI_VITALITY_RAW = new SimpleStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00FF00);

    // These are the RegistryEntry versions you will use in the Blessing enum
    public static final RegistryEntry<StatusEffect> KHERT_BLESSING;
    public static final RegistryEntry<StatusEffect> MNEMON_INSIGHT;
    public static final RegistryEntry<StatusEffect> BELLATOR_FURY;
    public static final RegistryEntry<StatusEffect> IXKALI_VIGOR;
    public static final RegistryEntry<StatusEffect> AVASURA_PEACE;
    public static final RegistryEntry<StatusEffect> ILLAPACHARI_SPEED;
    public static final RegistryEntry<StatusEffect> HOSHIKARI_FORTUNE;
    public static final RegistryEntry<StatusEffect> MOR_EZEN_GRACE;
    public static final RegistryEntry<StatusEffect> NYASARI_VITALITY;

    static {
        // Actually register them and get the RegistryEntry references
        KHERT_BLESSING = Registry.registerReference(Registries.STATUS_EFFECT,
                Identifier.of("desert_pyramid_update", "khert_blessing"), KHERT_BLESSING_RAW);

        MNEMON_INSIGHT = Registry.registerReference(Registries.STATUS_EFFECT,
                Identifier.of("desert_pyramid_update", "mnemon_insight"), MNEMON_INSIGHT_RAW);

        BELLATOR_FURY = Registry.registerReference(Registries.STATUS_EFFECT,
                Identifier.of("desert_pyramid_update", "bellator_fury"), BELLATOR_FURY_RAW);

        IXKALI_VIGOR = Registry.registerReference(Registries.STATUS_EFFECT,
                Identifier.of("desert_pyramid_update", "ixkali_vigor"), IXKALI_VIGOR_RAW);

        AVASURA_PEACE = Registry.registerReference(Registries.STATUS_EFFECT,
                Identifier.of("desert_pyramid_update", "avasura_peace"), AVASURA_PEACE_RAW);

        ILLAPACHARI_SPEED = Registry.registerReference(Registries.STATUS_EFFECT,
                Identifier.of("desert_pyramid_update", "illapachari_speed"), ILLAPACHARI_SPEED_RAW);

        HOSHIKARI_FORTUNE = Registry.registerReference(Registries.STATUS_EFFECT,
                Identifier.of("desert_pyramid_update", "hoshikari_fortune"), HOSHIKARI_FORTUNE_RAW);

        MOR_EZEN_GRACE = Registry.registerReference(Registries.STATUS_EFFECT,
                Identifier.of("desert_pyramid_update", "mor_ezen_grace"), MOR_EZEN_GRACE_RAW);

        NYASARI_VITALITY = Registry.registerReference(Registries.STATUS_EFFECT,
                Identifier.of("desert_pyramid_update", "nyasari_vitality"), NYASARI_VITALITY_RAW);
    }

    public static void registerEffects() {
        // The static block above already registers everything when the class is loaded.
        // This method can stay empty or just be a no-op.
        // It's safe to keep it for compatibility with your current call in onInitialize().
    }
}
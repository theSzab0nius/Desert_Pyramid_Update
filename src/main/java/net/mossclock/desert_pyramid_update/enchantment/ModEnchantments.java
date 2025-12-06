package net.mossclock.desert_pyramid_update.enchantment;

import net.mossclock.desert_pyramid_update.Desert_pyramid_update;
import net.mossclock.desert_pyramid_update.enchantment.custom.CobraStrike;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.enchantment.custom.SandsOfTime;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> COBRA_STRIKE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Desert_pyramid_update.MOD_ID, "cobra_strike"));
    public static final RegistryKey<Enchantment> SANDS_OF_TIME =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Desert_pyramid_update.MOD_ID, "sands_of_time"));
    public static final RegistryKey<Enchantment> SUN_FORGED =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Desert_pyramid_update.MOD_ID, "sun_forged"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, COBRA_STRIKE, Enchantment.builder(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                0,
                3,
                Enchantment.leveledCost(5, 7),
                Enchantment.leveledCost(25, 9),
                8,
                AttributeModifierSlot.MAINHAND))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new CobraStrike()));

        register(registerable, SANDS_OF_TIME, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        0,
                        3,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(25, 9),
                        8,
                        AttributeModifierSlot.MAINHAND))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new SandsOfTime()));

        register(registerable, SUN_FORGED, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        0,
                        3,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(25, 9),
                        8,
                        AttributeModifierSlot.MAINHAND))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new SandsOfTime()));
    }


    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}

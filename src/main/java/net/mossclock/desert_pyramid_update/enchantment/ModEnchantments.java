package net.mossclock.desert_pyramid_update.enchantment;

import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;
import net.mossclock.desert_pyramid_update.enchantment.custom.CobraStrike;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.enchantment.custom.SandsOfTime;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> COBRA_STRIKE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Desert_pyramid_update.MOD_ID, "cobra_strike"));
    public static final RegistryKey<Enchantment> SANDS_OF_TIME =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Desert_pyramid_update.MOD_ID, "sands_of_time"));
    public static final RegistryKey<Enchantment> SUN_FORGED =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Desert_pyramid_update.MOD_ID, "sun_forged"));
    public static final RegistryKey<Enchantment> PHARAOHS_CURSE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Desert_pyramid_update.MOD_ID, "pharaohs_curse"));
    public static final RegistryKey<Enchantment> INVISIBLE_ENCHANTMENT =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Desert_pyramid_update.MOD_ID, "invisible_enchantment"));

    public static final TagKey<Item> TOOLS = TagKey.of(RegistryKeys.ITEM, Identifier.of("desert_pyramid_update", "tools"));
    public static final TagKey<Item> KHOPS = TagKey.of(RegistryKeys.ITEM, Identifier.of("desert_pyramid_update", "khopeshes"));

    public static RegistryEntry<Enchantment> INVISIBLE_ENTRY;

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, INVISIBLE_ENCHANTMENT, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(KHOPS),           // primary (universal)
                        items.getOrThrow(KHOPS),
                        0,
                        3,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(25, 9),
                        8,
                        AttributeModifierSlot.MAINHAND))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.ATTACKER,
                        new CobraStrike()));

        INVISIBLE_ENTRY = enchantments.getOrThrow(INVISIBLE_ENCHANTMENT);

        register(registerable, PHARAOHS_CURSE, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(TOOLS),           // primary (universal)
                        items.getOrThrow(TOOLS),
                        0,
                        3,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(25, 9),
                        8,
                        AttributeModifierSlot.MAINHAND))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.ATTACKER,
                        new CobraStrike()));

        register(registerable, COBRA_STRIKE, Enchantment.builder(Enchantment.definition(
                items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of("desert_pyramid_update", "custom_enchants"))),
                        items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of("desert_pyramid_update", "custom_enchants"))),
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
                        items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of("desert_pyramid_update", "custom_enchants"))),  // supported_items
                        items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of("desert_pyramid_update", "custom_enchants"))),  // primary_items
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
                        items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of("desert_pyramid_update", "custom_enchants"))),  // supported_items
                        items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of("desert_pyramid_update", "custom_enchants"))),  // primary_items
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

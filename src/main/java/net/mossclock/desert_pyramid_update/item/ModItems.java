package net.mossclock.desert_pyramid_update.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;
import net.mossclock.desert_pyramid_update.block.ModBlocks;

public class ModItems {

    public static final Item ANCIENT_SCROLL = registerItem(
            "ancient_scroll",
            new ScrollItem(
                    new Item.Settings()
                            .maxCount(1)
            )
    );

    public static final Item TATTERED_ANCIENT_SCROLL = registerItem(
            "tattered_ancient_scroll",
            new ScrollItem(
                    new Item.Settings()
                            .maxCount(1)
            )
    );

    public static final Item RUSTED_KHOPESH = registerItem(
            "rusted_khopesh",
            new RustedKhopeshItem(
                    RustedToolMaterial.INSTANCE,
                    new Item.Settings()
                            .maxCount(1)
                            .attributeModifiers(
                                    SwordItem.createAttributeModifiers(
                                            RustedToolMaterial.INSTANCE,
                                            0,      // attack damage bonus
                                            -2.4f   // attack speed
                                    )
                            )
            )
    );

    public static final Item COPPER_ALLOY_KHOPESH = registerItem(
            "copper_alloy_khopesh",
            new CopperAlloyKhopeshItem(
                    copper_alloy_khopesh_mat.INSTANCE,
                    new Item.Settings()
                            .maxCount(1)
                            .attributeModifiers(
                                    SwordItem.createAttributeModifiers(
                                            copper_alloy_khopesh_mat.INSTANCE,
                                            0,      // attack damage bonus
                                            -2.5f   // attack speed
                                    )
                            )
            )
    );

    public static final Item IRON_ALLOY_KHOPESH = registerItem(
            "iron_alloy_khopesh",
            new IronAlloyKhopeshItem(
                    iron_alloy_khopesh_mat.INSTANCE,
                    new Item.Settings()
                            .maxCount(1)
                            .attributeModifiers(
                                    SwordItem.createAttributeModifiers(
                                            iron_alloy_khopesh_mat.INSTANCE,
                                            0,      // attack damage bonus
                                            -2.4f   // attack speed
                                    )
                            )
            )
    );

    public static final Item GOLD_ALLOY_KHOPESH = registerItem(
            "gold_alloy_khopesh",
            new GoldAlloyKhopeshItem(
                    gold_alloy_khopesh_mat.INSTANCE,
                    new Item.Settings()
                            .maxCount(1)
                            .attributeModifiers(
                                    SwordItem.createAttributeModifiers(
                                            gold_alloy_khopesh_mat.INSTANCE,
                                            0,      // attack damage bonus
                                            -2.2f   // attack speed
                                    )
                            )
            )
    );

    public static final Item DIAMOND_ALLOY_KHOPESH = registerItem(
            "diamond_alloy_khopesh",
            new DiamondAlloyKhopeshItem(
                    diamond_alloy_khopesh_mat.INSTANCE,
                    new Item.Settings()
                            .maxCount(1)
                            .attributeModifiers(
                                    SwordItem.createAttributeModifiers(
                                            diamond_alloy_khopesh_mat.INSTANCE,
                                            0,      // attack damage bonus
                                            -2.3f   // attack speed
                                    )
                            )
            )
    );

    public static final Item NETHERITE_ALLOY_KHOPESH = registerItem(
            "netherite_alloy_khopesh",
            new NetheriteAlloyKhopeshItem(
                    netherite_alloy_khopesh_mat.INSTANCE,
                    new Item.Settings()
                            .maxCount(1)
                            .attributeModifiers(
                                    SwordItem.createAttributeModifiers(
                                            netherite_alloy_khopesh_mat.INSTANCE,
                                            0,      // attack damage bonus
                                            -2.2f   // attack speed
                                    )
                            )
            )
    );

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Desert_pyramid_update.MOD_ID, name), item);
    }

    public static void registerModItems () {
        Desert_pyramid_update.LOGGER.info("Registering mod items for " + Desert_pyramid_update.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(ANCIENT_SCROLL);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(TATTERED_ANCIENT_SCROLL);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(RUSTED_KHOPESH);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(COPPER_ALLOY_KHOPESH);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(IRON_ALLOY_KHOPESH);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(GOLD_ALLOY_KHOPESH);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(DIAMOND_ALLOY_KHOPESH);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(NETHERITE_ALLOY_KHOPESH);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.LAYERED_SAND_ITEM);
        });
    }
}

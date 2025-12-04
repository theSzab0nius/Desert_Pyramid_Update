package net.mossclock.desert_pyramid_update.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;

public class ModItems {

    public static final Item RUSTED_KHOPESH = registerItem(
            "rusted_khopesh",
            new SwordItem(
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

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Desert_pyramid_update.MOD_ID, name), item);
    }

    public static void registerModItems () {
        Desert_pyramid_update.LOGGER.info("Registering mod items for " + Desert_pyramid_update.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(RUSTED_KHOPESH);
        });
    }
}

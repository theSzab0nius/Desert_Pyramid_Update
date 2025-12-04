package net.mossclock.desert_pyramid_update.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;

public class ModItems {

    private static Item registerItem(String name, Item item) {

        return Registry.register(Registries.ITEM, Identifier.of(Desert_pyramid_update.MOD_ID, name), item);
    }

    public static void registerModItems () {
        Desert_pyramid_update.LOGGER.info("Registering modItems for " + Desert_pyramid_update.MOD_ID);
    }
}

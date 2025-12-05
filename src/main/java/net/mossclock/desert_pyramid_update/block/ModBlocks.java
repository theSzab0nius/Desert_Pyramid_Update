package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;

public class ModBlocks {

    public static final Block LAYERED_SAND = registerBlock("layered_sand",
            new layered_sand(Block.Settings.copy(Blocks.SAND)));

    // This creates the item version (for inventory, creative tab, etc.)
    public static final Item LAYERED_SAND_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "layered_sand"),
            new BlockItem(LAYERED_SAND, new Item.Settings())  // ← No FabricItemSettings needed!
    );

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(Desert_pyramid_update.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        Desert_pyramid_update.LOGGER.info("Registering ModBlocks for " + Desert_pyramid_update.MOD_ID);
    }
}
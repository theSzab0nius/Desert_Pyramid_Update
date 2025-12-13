package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;

public class ModBlocks {

    public static final Block LAYERED_SAND = registerBlock("layered_sand",
            new layered_sand(Block.Settings.copy(Blocks.SAND)));

    public static final Block BOOBY_TRAP_DOOR = registerBlock("booby_trap_door",
            new booby_trap_door(AbstractBlock.Settings.create().strength(0f).sounds(BlockSoundGroup.SCAFFOLDING).nonOpaque()));

    public static final Block BURIAL_URN = registerBlock("burial_urn",
            new burial_urn(AbstractBlock.Settings.create().strength(0f).sounds(BlockSoundGroup.DECORATED_POT).nonOpaque()));

    public static final Block STRING_HOOK = registerBlock("string_hook",
            new string_hook(AbstractBlock.Settings.create().strength(0f).sounds(BlockSoundGroup.WOOD).nonOpaque()));

    static final Block TRAPPED_LIMESTONE = registerBlock("trapped_limestone",
            new trapped_limestone(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.STONE).nonOpaque()));


    public static final BlockItem TRAPPED_LIMESTONE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "trapped_limestone"),
            new BlockItem(TRAPPED_LIMESTONE, new Item.Settings())
    );

    public static final BlockItem BOOBY_TRAP_DOOR_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "booby_trap_door"),
            new BlockItem(BOOBY_TRAP_DOOR, new Item.Settings())
    );

    public static final BlockItem STRING_HOOK_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "string_hook"),
            new BlockItem(STRING_HOOK, new Item.Settings())
    );

    // This creates the item version (for inventory, creative tab, etc.)
    public static final Item LAYERED_SAND_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "layered_sand"),
            new BlockItem(LAYERED_SAND, new Item.Settings())
    );

    public static final Item BURIAL_URN_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "burial_urn"),
            new BlockItem(BURIAL_URN, new Item.Settings())
    );


    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(Desert_pyramid_update.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        Desert_pyramid_update.LOGGER.info("Registering ModBlocks for " + Desert_pyramid_update.MOD_ID);
    }
}
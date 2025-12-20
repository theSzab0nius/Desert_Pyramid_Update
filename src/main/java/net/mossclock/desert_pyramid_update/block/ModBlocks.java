package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;

public class ModBlocks {

    public static final Block LAYERED_SAND = registerBlock("layered_sand",
            new layered_sand(Block.Settings.copy(Blocks.SAND)
                    .strength(0.5f)                          // same as vanilla sand
                    .sounds(BlockSoundGroup.SAND)
                    .nonOpaque()                             // keeps your existing visuals
                    .mapColor(MapColor.DIRT_BROWN)     )            // base color for tinting (yellowish)
    );

    public static final Block RED_LAYERED_SAND = registerBlock("red_layered_sand",
            new red_layered_sand(Block.Settings.copy(Blocks.RED_SAND)
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.SAND)            // red sand uses same sound as regular
                    .nonOpaque()
                    .mapColor(MapColor.TERRACOTTA_RED)   )          // reddish base for wet/dry tint
    );

    public static final Block BOOBY_TRAP_DOOR = registerBlock("booby_trap_door",
            new booby_trap_door(AbstractBlock.Settings.create().strength(0f).sounds(BlockSoundGroup.SCAFFOLDING).nonOpaque()));

    public static final Block BURIAL_URN = registerBlock("burial_urn",
            new burial_urn(AbstractBlock.Settings.create().strength(0f).sounds(BlockSoundGroup.DECORATED_POT).nonOpaque()));

    static final Block TRAPPED_LIMESTONE = registerBlock("trapped_limestone",
            new trapped_limestone(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    static final Block KHERT_STATUE = registerBlock("khert_statue",
            new KhertStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem KHERT_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "khert_statue"),
            new BlockItem(KHERT_STATUE, new Item.Settings())
    );

    static final Block MNEMON_STATUE = registerBlock("mnemon_statue",
            new MnemonStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem MNEMON_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "mnemon_statue"),
            new BlockItem(MNEMON_STATUE, new Item.Settings())
    );

    static final Block BELLATOR_STATUE = registerBlock("bellator_statue",
            new BellatorStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem BELLATOR_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "bellator_statue"),
            new BlockItem(BELLATOR_STATUE, new Item.Settings())
    );

    static final Block IX_KALI_STATUE = registerBlock("ix_kali_statue",
            new IxKaliStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem IX_KALI_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "ix_kali_statue"),
            new BlockItem(IX_KALI_STATUE, new Item.Settings())
    );

    static final Block AVASURA_STATUE = registerBlock("avasura_statue",
            new AvasuraStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem AVASURA_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "avasura_statue"),
            new BlockItem(AVASURA_STATUE, new Item.Settings())
    );

    static final Block ILLAPACHARI_STATUE = registerBlock("illapachari_statue",
            new IllapachariStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem ILLAPACHARI_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "illapachari_statue"),
            new BlockItem(ILLAPACHARI_STATUE, new Item.Settings())
    );

    static final Block HOSHIKARI_STATUE = registerBlock("hoshikari_statue",
            new HoshikariStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem HOSHIKARI_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "hoshikari_statue"),
            new BlockItem(HOSHIKARI_STATUE, new Item.Settings())
    );

    static final Block MOR_EZEN_STATUE = registerBlock("mor_ezen_statue",
            new MorEzenStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem MOR_EZEN_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "mor_ezen_statue"),
            new BlockItem(MOR_EZEN_STATUE, new Item.Settings())
    );

    static final Block NYASARI_STATUE = registerBlock("nyasari_statue",
            new NyasariStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem NYASARI_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "nyasari_statue"),
            new BlockItem(NYASARI_STATUE, new Item.Settings())
    );

    static final Block UMBRATH_STATUE = registerBlock("umbrath_statue",
            new UmbrathStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem UMBRATH_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "umbrath_statue"),
            new BlockItem(UMBRATH_STATUE, new Item.Settings())
    );

    static final Block FORGATHOR_STATUE = registerBlock("forgathor_statue",
            new ForgathorStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem FORGATHOR_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "forgathor_statue"),
            new BlockItem(FORGATHOR_STATUE, new Item.Settings())
    );

    static final Block NAMELESS_STATUE = registerBlock("nameless_statue",
            new NamelessStatueBlock(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)   // hardness 1.5f, resistance 6.0f — exact vanilla stone values
                    .requiresTool()         // crucial: massive slowdown without correct tool + no drops if you want that
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    public static final BlockItem NAMELESS_STATUTE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "nameless_statue"),
            new BlockItem(NAMELESS_STATUE, new Item.Settings())
    );

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

    // This creates the item version (for inventory, creative tab, etc.)
    public static final Item LAYERED_SAND_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "layered_sand"),
            new BlockItem(LAYERED_SAND, new Item.Settings())
    );

    public static final Item RED_LAYERED_SAND_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of(Desert_pyramid_update.MOD_ID, "red_layered_sand"),
            new BlockItem(RED_LAYERED_SAND, new Item.Settings())
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
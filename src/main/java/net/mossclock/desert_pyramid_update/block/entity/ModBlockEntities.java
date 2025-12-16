package net.mossclock.desert_pyramid_update.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;
import net.mossclock.desert_pyramid_update.block.ModBlocks;

public class ModBlockEntities {
    public static BlockEntityType<burial_urn_block_entity> BURIAL_URN_ENTITY_TYPE;

    public static void registerAll() {
        BURIAL_URN_ENTITY_TYPE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of("desert_pyramid_update", "burial_urn"),
                BlockEntityType.Builder.create(burial_urn_block_entity::new, ModBlocks.BURIAL_URN).build(null)
        );
    }

    public static final BlockEntityType<layered_sand_block_entity> LAYERED_SAND_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(Desert_pyramid_update.MOD_ID, "layered_sand"),
                    BlockEntityType.Builder.create(layered_sand_block_entity::new,
                            ModBlocks.LAYERED_SAND, ModBlocks.RED_LAYERED_SAND).build(null));
}

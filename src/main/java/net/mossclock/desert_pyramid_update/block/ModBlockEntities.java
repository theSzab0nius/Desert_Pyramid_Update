package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.block.entity.burial_urn_block_entity;

public class ModBlockEntities {
    public static BlockEntityType<burial_urn_block_entity> BURIAL_URN_ENTITY_TYPE;

    public static void registerAll() {
        BURIAL_URN_ENTITY_TYPE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of("desert_pyramid_update", "burial_urn"),
                BlockEntityType.Builder.create(burial_urn_block_entity::new, ModBlocks.BURIAL_URN).build(null)
        );
    }
}

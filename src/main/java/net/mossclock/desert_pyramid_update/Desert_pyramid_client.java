package net.mossclock.desert_pyramid_update;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.state.property.Properties;
import net.mossclock.desert_pyramid_update.block.ModBlocks;

public class Desert_pyramid_client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            return state.get(Properties.WATERLOGGED)
                    ? 0x4A6741   // wet sand
                    : 0xFFFFFF; // dry
        }, ModBlocks.LAYERED_SAND, ModBlocks.RED_LAYERED_SAND);

    }
}

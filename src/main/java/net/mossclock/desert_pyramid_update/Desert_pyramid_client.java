package net.mossclock.desert_pyramid_update;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.mossclock.desert_pyramid_update.block.ModBlocks;
import net.mossclock.desert_pyramid_update.block.entity.layered_sand_block_entity;

public class Desert_pyramid_client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {


            ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                if (world == null || pos == null) return 0xFFFFFF;

                var be = world.getBlockEntity(pos);
                if (be instanceof layered_sand_block_entity sandBE) {
                    return sandBE.getTintColor();
                }
                return 0xFFFFFF; // fallback dry
            }, ModBlocks.LAYERED_SAND, ModBlocks.RED_LAYERED_SAND);

            // Optional: also register item color if you want the item in inventory to show wet/dry
            // (but usually items are dry, so skip unless you want fancy inventory rendering)

    }
}

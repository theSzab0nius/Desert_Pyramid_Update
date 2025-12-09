package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class string_hook extends Block {
    public string_hook(Settings settings) {
        super(settings);
    }

    // Outline shape — small hook-ish cube (like tripwire hook but block-sized)
    private static final VoxelShape SHAPE = Block.createCuboidShape(2, 2, 2, 14, 14, 14);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        // Must attach to at least one solid face
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.offset(dir);
            if (world.getBlockState(neighbor).isSideSolidFullSquare(world, neighbor, dir.getOpposite())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!canPlaceAt(state, world, pos)) {
            world.breakBlock(pos, true); // drop itself if no support
        }
    }
}

package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class burial_urn extends Block {

    public burial_urn(AbstractBlock.Settings settings) {
        super(settings);
    }

    protected static final VoxelShape URN_SHAPE = Block.createCuboidShape(3,0,3,13,16,13);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return URN_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return URN_SHAPE;
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModBlocks.BURIAL_URN_ITEM);
    }


    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient && !state.isOf(newState.getBlock())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof DecoratedPotBlockEntity decoratedPot) {
                // Drop the stored item, if any
                ItemStack stored = decoratedPot.getStack();
                if (!stored.isEmpty()) {
                    dropStack(world, pos, stored);
                }
                // Drop the block itself
                dropStack(world, pos, new ItemStack(ModBlocks.BURIAL_URN));
            } else {
                // fallback, drop block normally
                dropStack(world, pos, new ItemStack(ModBlocks.BURIAL_URN));
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }


}

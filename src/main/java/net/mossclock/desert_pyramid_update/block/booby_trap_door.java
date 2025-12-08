package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.util.math.Direction;


public class booby_trap_door extends Block {

    public booby_trap_door(Settings settings) {
        super(settings);
    }

    // Thin 2-unit-high slab at the top of the block
    protected static final VoxelShape TOP_TRAPDOOR_SHAPE =
            Block.createCuboidShape(0, 14, 0, 16, 16, 16);


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return TOP_TRAPDOOR_SHAPE;
    }

    private void breakConnected(World world, BlockPos pos, int depth) {
        if (depth > 12) return; // Limit chain reaction

        for (Direction dir : Direction.values()) { // all 6 directions
            BlockPos neighborPos = pos.offset(dir);
            BlockState neighborState = world.getBlockState(neighborPos);

            if (neighborState.getBlock() instanceof booby_trap_door) {
                world.breakBlock(neighborPos, true); // drops loot
                breakConnected(world, neighborPos, depth + 1); // recursive
            }
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);

        // Only care about players
        if (entity instanceof PlayerEntity player) {
            // Check if player is NOT crouching
            if (!player.isSneaking()) {
                // Trigger the trap: break the block
                world.breakBlock(pos, true); // true = drops loot
                breakConnected(world, pos, 1);
                // Optional: play sound when triggered
                world.playSound(null, pos, SoundEvents.BLOCK_SCAFFOLDING_BREAK,
                        SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
    // When the block is broken
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient && !state.isOf(newState.getBlock())) {
            // Drop 4 sticks
            dropStack(world, pos, new ItemStack(Items.STICK, 4));
            // Drop 2 strings
            dropStack(world, pos, new ItemStack(Items.STRING, 2));
            breakConnected(world, pos, 1);
            // TODO: Trigger your trap here
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);

        if (!world.isClient) {
            // Check if the block is receiving redstone power
            boolean powered = world.isReceivingRedstonePower(pos);
            if (powered) {
                // Break the block immediately (drops loot)
                world.breakBlock(pos, true);
                // Optional sound
                world.playSound(null, pos, SoundEvents.BLOCK_SCAFFOLDING_BREAK,
                        SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

}

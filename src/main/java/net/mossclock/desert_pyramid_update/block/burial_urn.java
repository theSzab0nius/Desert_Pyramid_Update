
package net.mossclock.desert_pyramid_update.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.mossclock.desert_pyramid_update.block.entity.burial_urn_block_entity;
import org.jetbrains.annotations.Nullable;
import net.minecraft.util.ActionResult;

public class burial_urn extends BlockWithEntity implements BlockEntityProvider {


    public static final MapCodec<burial_urn> CODEC = burial_urn.createCodec(burial_urn::new);

    public burial_urn(Settings settings) {
        super(settings);
    }

    protected static final VoxelShape URN_SHAPE = Block.createCuboidShape(3, 0, 3, 13, 16, 13);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return URN_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return URN_SHAPE;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new burial_urn_block_entity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModBlocks.BURIAL_URN_ITEM);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos below = pos.down();
        BlockState belowState = world.getBlockState(below);

        // Only survives on full solid blocks (no slabs, stairs, glass, etc.)
        return Block.isFaceFullSquare(belowState.getSidesShape(world, below), Direction.UP);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        checkDrop(world, pos, state);
    }

    private void checkDrop(World world, BlockPos pos, BlockState state) {
        BlockState stateBelow = world.getBlockState(pos.down());
        if (!stateBelow.isSideSolidFullSquare(world, pos.down(), Direction.UP) && !stateBelow.isOf(this)) {
            world.breakBlock(pos, false); // break without dropping anything
        }
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isClient && entity instanceof LivingEntity living && fallDistance > 0.5f) {
            // Anvils and heavy shit destroy it instantly
            world.breakBlock(pos, true);  // true = drop items (urn + stored item)
            living.playSound(SoundEvents.BLOCK_ANVIL_HIT, 1.0f, 1.0f);
        }
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (world.getBlockEntity(pos) instanceof burial_urn_block_entity urn) {
                // THIS DROPS EVERY SINGLE ITEM IN ALL 16 SLOTS
                for (int i = 0; i < urn.size(); i++) {
                    ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, urn.getStack(i));
                }
            }
            // This drops the urn block item itself (silk touch or normal break)
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack playerStack, BlockState state, World world,
                                             BlockPos pos, PlayerEntity player, Hand hand,
                                             BlockHitResult hit) {
        if (world.isClient) return ItemActionResult.SUCCESS;

        if (!(world.getBlockEntity(pos) instanceof burial_urn_block_entity urn)) {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // INSERT ITEM — find first empty slot
        if (!playerStack.isEmpty()) {
            for (int i = 0; i < 16; i++) {
                if (urn.getStack(i).isEmpty()) {
                    ItemStack toInsert = playerStack.split(1);
                    urn.setStack(i, toInsert);
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.6f, 1.8f);
                    urn.markDirty();
                    return ItemActionResult.SUCCESS;
                }
            }
            // No space left → bonk sound
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS, 1.0f, 0.8f);
            return ItemActionResult.SUCCESS;
        }


        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}

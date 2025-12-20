package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.mossclock.desert_pyramid_update.Blessing;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;
import net.mossclock.desert_pyramid_update.ModSounds;
import net.mossclock.desert_pyramid_update.StatueCooldowns;
import net.mossclock.desert_pyramid_update.util.AdvancementUtils;

public class BellatorStatueBlock extends Block implements Waterloggable {

    public BellatorStatueBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                        .with(Properties.WATERLOGGED, false)
        );
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // Convert from JSON units (0-16) to Minecraft VoxelShape units (0.0-1.0)
        return VoxelShapes.union(
                VoxelShapes.cuboid(2/16.0, 0/16.0, 2/16.0, 14/16.0, 14/16.0, 14/16.0)
        );
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, Properties.WATERLOGGED);
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.get(Properties.WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            if (!world.isClient()) {
                world.setBlockState(
                        pos,
                        state.with(Properties.WATERLOGGED, true),
                        Block.NOTIFY_ALL
                );
                world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            return true;
        }
        return false;
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        boolean waterlogged = ctx.getWorld()
                .getFluidState(ctx.getBlockPos())
                .getFluid() == Fluids.WATER;

        return this.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(Properties.WATERLOGGED, waterlogged);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED)
                ? Fluids.WATER.getStill(false)
                : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos below = pos.down();
        BlockState belowState = world.getBlockState(below);

        // Only survives on full solid blocks (no slabs, stairs, glass, etc.)
        return Block.isFaceFullSquare(belowState.getSidesShape(world, below), Direction.UP);
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (state.get(Properties.WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(
                state, direction, neighborState, world, pos, neighborPos
        );
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient()) return ActionResult.SUCCESS;

        if (!(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.PASS;
        if (!StatueCooldowns.canUse(serverPlayer)) {
            world.playSound(
                    null,
                    pos,
                    SoundEvents.BLOCK_DECORATED_POT_INSERT_FAIL,
                    SoundCategory.BLOCKS,
                    0.6f,  // volume
                    0.7f   // pitch (lower = heavier)
            );
            return ActionResult.CONSUME;
        }

        ItemStack mainHand = player.getMainHandStack();
        ItemStack offHand = player.getOffHandStack();

        // Check if the player is holding any valid offering
        if (mainHand.isOf(Items.WHEAT) || mainHand.isOf(Items.BREAD) || mainHand.isOf(Items.COOKED_BEEF) ||
                offHand.isOf(Items.WHEAT) || offHand.isOf(Items.BREAD) || offHand.isOf(Items.COOKED_BEEF)) {

            if (!world.isClient) {
                // Decrement the held item if not in creative
                if (!player.getAbilities().creativeMode) {
                    if (mainHand.isOf(Items.WHEAT) || mainHand.isOf(Items.BREAD) || mainHand.isOf(Items.COOKED_BEEF)) {
                        mainHand.decrement(1);
                    } else {
                        offHand.decrement(1);
                    }
                }

                StatueCooldowns.markUsed(serverPlayer);
                // Apply or revoke the blessing
                if (Desert_pyramid_update.hasBlessing(serverPlayer, Blessing.BLESSING_OF_BELLATOR)) {
                    Desert_pyramid_update.revokeBlessing(serverPlayer);
                } else {
                    Desert_pyramid_update.revokeBlessing(serverPlayer);
                    Desert_pyramid_update.grantBlessing(serverPlayer, Blessing.BLESSING_OF_BELLATOR);

                    AdvancementUtils.grantIfFirstTime(serverPlayer, "blessing_of_bellator");

                    world.playSound(
                            null,
                            pos,
                            ModSounds.STATUE_BLESSING,
                            SoundCategory.BLOCKS,
                            1f,
                            0.8f + world.random.nextFloat() * 0.2f
                    );

                    ((ServerWorld) world).spawnParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6, 0.3, 0.3, 0.3, 0.05);
                }
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.CONSUME;
    }

}

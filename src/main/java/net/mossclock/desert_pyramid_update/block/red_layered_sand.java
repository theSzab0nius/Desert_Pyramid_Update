package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class red_layered_sand extends Block implements Waterloggable {
    public static final IntProperty LAYERS = IntProperty.of("layers", 1, 8);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected static final VoxelShape[] SHAPES = new VoxelShape[]{
            VoxelShapes.empty(),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
    };

    public red_layered_sand(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(LAYERS, 1)
                        .with(WATERLOGGED, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LAYERS, WATERLOGGED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(LAYERS)];
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState stateAtPos = ctx.getWorld().getBlockState(ctx.getBlockPos());
        BlockState placedState;

        if (stateAtPos.isOf(this)) {
            int layers = stateAtPos.get(LAYERS);
            placedState = layers < 8 ? stateAtPos.with(LAYERS, layers + 1) : this.getDefaultState();
        } else {
            placedState = this.getDefaultState().with(LAYERS, 1);
        }

        FluidState fluid = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean shouldWaterlog = fluid.getFluid() == Fluids.WATER;

        // Handle stacking in flowing water cases
        if (!shouldWaterlog && stateAtPos.isOf(this)) {
            FluidState belowFluid = ctx.getWorld().getFluidState(ctx.getBlockPos().down());
            shouldWaterlog = belowFluid.getFluid() == Fluids.WATER;
        }

        return placedState.with(WATERLOGGED, shouldWaterlog);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED)
                ? Fluids.WATER.getStill(false)
                : super.getFluidState(state);
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
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (!context.shouldCancelInteraction()) {
            ItemStack stack = context.getStack();
            if (stack.isOf(ModBlocks.RED_LAYERED_SAND_ITEM)) {
                return state.get(LAYERS) < 8;
            }
        }
        return state.get(LAYERS) == 1;
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModBlocks.RED_LAYERED_SAND_ITEM);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        checkDrop(world, pos, state);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        checkDrop(world, pos, state);
        if (state.get(WATERLOGGED)) {
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }
    }

    private void checkDrop(World world, BlockPos pos, BlockState state) {
        BlockState below = world.getBlockState(pos.down());
        if (!below.isSideSolidFullSquare(world, pos.down(), Direction.UP) && !below.isOf(this)) {
            world.breakBlock(pos, false);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.down());
        return below.isSideSolidFullSquare(world, pos.down(), Direction.UP)
                || below.isOf(this);
    }
}

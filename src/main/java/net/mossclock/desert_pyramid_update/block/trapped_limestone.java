package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.mossclock.desert_pyramid_update.ModSounds;

import java.util.List;

public class trapped_limestone extends Block {

    public trapped_limestone(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.UP)
                .with(MODE, TrappedLimestoneMode.FIRE)
                .with(WAXED, false)
        );
    }

    public enum TrappedLimestoneMode implements StringIdentifiable {
        FIRE, ICE, CHAIN, DIRECTIONAL, SINGLE, SAFE;

        @Override
        public String asString() {
            return name().toLowerCase();
        }
    }

    public static final BooleanProperty WAXED = BooleanProperty.of("waxed");
    public static final DirectionProperty FACING = Properties.FACING;
    public static final DirectionProperty ROTATION = DirectionProperty.of("rotation", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    public static final EnumProperty<TrappedLimestoneMode> MODE = EnumProperty.of("mode", TrappedLimestoneMode.class);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ROTATION, MODE, WAXED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction triggerDir = ctx.getSide(); // This controls where effects go
        Direction playerHorizontal = ctx.getHorizontalPlayerFacing();

        return this.getDefaultState()
                .with(FACING, triggerDir)
                .with(ROTATION, playerHorizontal)
                .with(MODE, TrappedLimestoneMode.FIRE)
                .with(WAXED, false);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!state.get(WAXED) && (player.getMainHandStack().isOf(Items.HONEYCOMB) || player.getOffHandStack().isOf(Items.HONEYCOMB))) {
            if (!world.isClient) {
                ItemStack mainHand = player.getMainHandStack();
                ItemStack offHand = player.getOffHandStack();
                world.setBlockState(pos, state.with(WAXED, true), Block.NOTIFY_ALL);
                if (!player.getAbilities().creativeMode) {
                    if (mainHand.isOf(Items.HONEYCOMB)) mainHand.decrement(1);
                    else offHand.decrement(1);
                }
                world.playSound(null, pos, SoundEvents.ITEM_HONEYCOMB_WAX_ON, SoundCategory.BLOCKS, 1f, 1f);
                ((ServerWorld) world).spawnParticles(ParticleTypes.WAX_ON, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6, 0.3, 0.3, 0.3, 0.05);
            }
            return ActionResult.success(world.isClient);
        }

        if (!state.get(WAXED) && player.isSneaking()) {
            if (!world.isClient) {
                TrappedLimestoneMode[] values = TrappedLimestoneMode.values();
                TrappedLimestoneMode next = values[(state.get(MODE).ordinal() + 1) % values.length];
                world.setBlockState(pos, state.with(MODE, next), Block.NOTIFY_ALL);
                world.playSound(null, pos, ModSounds.TRAPPED_LIMESTONE_CYCLE, SoundCategory.BLOCKS, 1f, 0.8f + world.random.nextFloat() * 0.2f);
                player.swingHand(player.getActiveHand());
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient || !state.get(WAXED)) return;
        if (world.getReceivedRedstonePower(pos) > 0) tryTrigger(state, (ServerWorld) world, pos);
        else if (sourcePos != null) {
            Block source = world.getBlockState(sourcePos).getBlock();
            if (source instanceof RepeaterBlock && world.getBlockState(sourcePos).get(RepeaterBlock.POWERED))
                tryTrigger(state, (ServerWorld) world, pos);
            else if (source instanceof TargetBlock) {
                tryTrigger(state, (ServerWorld) world, pos);
            }
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        checkEntityTrigger(world, pos, state, entity);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, state, pos, entity, fallDistance);
        checkEntityTrigger(world, pos, state, entity);
    }

    private void checkEntityTrigger(World world, BlockPos pos, BlockState state, Entity entity) {
        if (world.isClient || !state.get(WAXED)) return;
        if ((entity instanceof LivingEntity || entity instanceof ItemEntity) && world.getBlockState(pos.down()).isAir()) {
            tryTrigger(state, (ServerWorld) world, pos);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) { return true; }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return switch (state.get(MODE)) {
            case FIRE -> 2;
            case ICE -> 3;
            case CHAIN -> 4;
            case DIRECTIONAL -> 5;
            case SINGLE -> 6;
            case SAFE -> 7;
        };
    }

    private void tryTrigger(BlockState state, ServerWorld world, BlockPos pos) {
        if (!state.get(WAXED)) return;

        switch (state.get(MODE)) {
            case FIRE -> fireTriggered(world, pos, state.get(FACING));
            case ICE -> iceTriggered(world, pos, state.get(FACING));
            case SINGLE -> singleTriggered(world, pos);
            case CHAIN -> chainTriggered(world, pos);
            case DIRECTIONAL -> directionalTriggered(world, pos);
            case SAFE -> safeTriggered(world, pos);
        }
    }

    private void fireTriggered(ServerWorld world, BlockPos pos, Direction facing) {
        Vec3d center = Vec3d.ofCenter(pos);
        Box fireBox = new Box(pos).stretch(facing.getVector().getX() * 1.5, facing.getVector().getY() * 1.5, facing.getVector().getZ() * 1.5).expand(0.4);

        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, fireBox, e -> true)) {
            entity.setOnFireFor(8);
            entity.damage(entity.getDamageSources().onFire(), 4f);
        }

        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.5f, 0.8f + world.random.nextFloat() * 0.4f);
        Vec3d facingVec = Vec3d.of(facing.getVector());
        Vec3d plumeStart = center.add(facingVec.multiply(0.51));
        spawnPlumeParticles(world, plumeStart, facingVec, ParticleTypes.FLAME, ParticleTypes.LARGE_SMOKE);
    }

    private void iceTriggered(ServerWorld world, BlockPos pos, Direction facing) {
        Box freezeBox = new Box(pos).stretch(facing.getVector().getX() * 1.5, facing.getVector().getY() * 1.5, facing.getVector().getZ() * 1.5).expand(0.4);

        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, freezeBox, e -> true)) {
            entity.setFrozenTicks(140);
            entity.damage(entity.getDamageSources().freeze(), 2f);
        }

        world.playSound(null, pos, SoundEvents.BLOCK_SNOW_BREAK, SoundCategory.BLOCKS, 1.2f, 0.9f + world.random.nextFloat() * 0.2f);
        Vec3d facingVec = Vec3d.of(facing.getVector());
        Vec3d plumeStart = Vec3d.ofCenter(pos).add(facingVec.multiply(0.51));
        spawnPlumeParticles(world, plumeStart, facingVec, ParticleTypes.ITEM_SNOWBALL, ParticleTypes.SNOWFLAKE);
    }

    private void spawnPlumeParticles(ServerWorld world, Vec3d start, Vec3d direction, SimpleParticleType main, SimpleParticleType secondary)
    {
        double radius = 0.2, plumeLength = 2.0, tinySpread = 0.015;
        int mainCount = 30, smokeCount = 15;

        for (int i = 0; i < mainCount; i++) {
            double t = world.random.nextDouble() * plumeLength;
            Vec3d pos = start.add(direction.multiply(t));
            double offsetX = direction.x == 0 ? (world.random.nextDouble() - 0.5) * radius : 0;
            double offsetY = direction.y == 0 ? (world.random.nextDouble() - 0.5) * radius : 0;
            double offsetZ = direction.z == 0 ? (world.random.nextDouble() - 0.5) * radius : 0;
            world.spawnParticles(main, pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ, 1, direction.x * 0.3, direction.y * 0.3, direction.z * 0.3, 0.0);
        }

        for (int i = 0; i < smokeCount; i++) {
            double t = world.random.nextDouble() * plumeLength;
            Vec3d pos = start.add(direction.multiply(t));
            double offsetX = direction.x == 0 ? (world.random.nextDouble() - 0.5) * radius * 1.2 : 0;
            double offsetY = direction.y == 0 ? (world.random.nextDouble() - 0.5) * radius * 1.2 : 0;
            double offsetZ = direction.z == 0 ? (world.random.nextDouble() - 0.5) * radius * 1.2 : 0;
            world.spawnParticles(secondary, pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ, 1, direction.x * 0.3, direction.y * 0.3, direction.z * 0.3, 0.0);
        }
    }

    private void singleTriggered(ServerWorld world, BlockPos pos) { triggerBlockBreak(world, pos, false); }
    private void chainTriggered(ServerWorld world, BlockPos pos) { triggerBlockBreak(world, pos, true); }

    private void triggerBlockBreak(ServerWorld world, BlockPos pos, boolean propagate) {
        boolean crouching = world.getOtherEntities(null,
                        new Box(pos.getX(), pos.getY() + 0.5, pos.getZ(), pos.getX() + 1, pos.getY() + 1.5, pos.getZ() + 1))
                .stream().anyMatch(e -> e instanceof ServerPlayerEntity player && player.isSneaking());

        int fakeId = pos.hashCode();

        if (!crouching) {
            world.breakBlock(pos, false);
            if (propagate) breakConnected(world, pos, 0);
            return;
        }

        // Show crack animation
        for (int stage = 0; stage <= 9; stage++) {
            final int currentStage = stage;
            world.getServer().execute(() -> sendBreakProgress(world, pos, fakeId, currentStage));
        }

        // Schedule self-break
        world.scheduleBlockTick(pos, this, 7);
    }


    private void breakConnected(ServerWorld world, BlockPos pos, int depth) {
        if (depth > 12) return;

        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.offset(dir);
            BlockState state = world.getBlockState(neighbor);
            if (state.getBlock() instanceof trapped_limestone && state.get(WAXED) && world.getBlockState(neighbor.down()).isAir())
                world.scheduleBlockTick(neighbor, this, 2);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(WAXED)) return;

        // Always break the block for chain mode, ignoring mode of neighbor
        if (state.get(MODE) == TrappedLimestoneMode.SINGLE) {
            finalizeSingle(world, pos);
        } else if (state.get(MODE) == TrappedLimestoneMode.DIRECTIONAL){
            finalizeDirectional(world, pos);
        }
        else {
            finalizeChain(world, pos);
        }
    }


    private void finalizeDirectional(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        sendBreakProgress(world, pos, pos.hashCode(), -1);
        world.breakBlock(pos, false);

        Direction propagation = state.get(ROTATION);
        Direction facing = state.get(FACING);

        if (facing == Direction.DOWN) {
            propagation = propagation.getOpposite();
        }
        // Schedule next block propagation
        breakDirectionalConnected(world, pos, propagation, 1);
    }

    private void finalizeSingle(ServerWorld world, BlockPos pos) {
        sendBreakProgress(world, pos, pos.hashCode(), -1);
        world.breakBlock(pos, false);
    }

    private void finalizeChain(ServerWorld world, BlockPos pos) {
        sendBreakProgress(world, pos, pos.hashCode(), -1);
        world.breakBlock(pos, false);
        breakConnected(world, pos, 1);
    }

    private void breakDirectionalConnected(ServerWorld world, BlockPos pos, Direction propagation, int depth) {
        if (depth > 12) return;

        BlockPos nextPos = pos.offset(propagation);
        BlockState nextState = world.getBlockState(nextPos);

        if (nextState.getBlock() instanceof trapped_limestone
                && nextState.get(WAXED)
                && world.getBlockState(nextPos.down()).isAir()) {

            if (nextState.get(MODE) == TrappedLimestoneMode.DIRECTIONAL) {
                world.scheduleBlockTick(nextPos, this, 2);
            } else {
                // Non-directional block: just break itself
                tryTrigger(nextState, world, nextPos);
            }
        }
    }

    private void finalizeBreak(ServerWorld world, BlockPos pos) {
        sendBreakProgress(world, pos, pos.hashCode(), -1);
        world.breakBlock(pos, false);
        breakConnected(world, pos, 1);
    }

    private void sendBreakProgress(ServerWorld world, BlockPos pos, int id, int stage) {
        BlockBreakingProgressS2CPacket packet = new BlockBreakingProgressS2CPacket(id, pos, stage);
        world.getPlayers().forEach(p -> p.networkHandler.sendPacket(packet));
    }

    private void directionalTriggered(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        directionalBreak(world, pos, state.get(FACING));
    }


    private void directionalBreak(ServerWorld world, BlockPos pos, Direction facing) {
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof trapped_limestone)) return;

        // Only trigger if facing UP or DOWN
        if (facing != Direction.UP && facing != Direction.DOWN) return;

        // Use ROTATION for propagation
        Direction propagation = state.get(ROTATION);
        if (facing == Direction.DOWN && (propagation == Direction.NORTH || propagation == Direction.SOUTH)) {
            propagation = propagation.getOpposite();
        }

        boolean crouching = world.getOtherEntities(null,
                        new Box(pos.getX(), pos.getY() + 0.5, pos.getZ(),
                                pos.getX() + 1, pos.getY() + 1.5, pos.getZ() + 1))
                .stream().anyMatch(e -> e instanceof ServerPlayerEntity player && player.isSneaking());

        int fakeId = pos.hashCode();

        if (!crouching) {
            sendBreakProgress(world, pos, fakeId, -1);
            world.breakBlock(pos, false);

            // Propagate along rotation direction
            BlockPos nextPos = pos.offset(propagation);
            BlockState nextState = world.getBlockState(nextPos);
            if (nextState.getBlock() instanceof trapped_limestone
                    && nextState.get(WAXED)
                    && world.getBlockState(nextPos.down()).isAir()) {
                world.scheduleBlockTick(nextPos, this, 2);
            }
            return;
        }

        // If crouching, show crack animation
        for (int stage = 0; stage <= 9; stage++) {
            final int currentStage = stage;
            world.getServer().execute(() -> sendBreakProgress(world, pos, fakeId, currentStage));
        }

        // Schedule breaking later
        world.scheduleBlockTick(pos, this, 7);

    }

    private void safeTriggered(World world, BlockPos pos) {}
}

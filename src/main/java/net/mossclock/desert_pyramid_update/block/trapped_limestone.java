package net.mossclock.desert_pyramid_update.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.particle.ParticleTypes;
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
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.mossclock.desert_pyramid_update.ModSounds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.List;


public class trapped_limestone extends Block {

    public trapped_limestone(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(FACING, Direction.UP)
                        .with(MODE, TrappedLimestoneMode.FIRE)
                        .with(WAXED, false)
        );
    }


    public enum TrappedLimestoneMode implements StringIdentifiable {
        FIRE,
        ICE,
        CHAIN,
        DIRECTIONAL,
        SINGLE,
        SAFE;

        @Override
        public String asString() {
            return name().toLowerCase();
        }
    }

    public static final BooleanProperty WAXED = BooleanProperty.of("waxed");
    public static final DirectionProperty FACING = Properties.FACING;
    public static final EnumProperty<TrappedLimestoneMode> MODE =
            EnumProperty.of("mode", TrappedLimestoneMode.class);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE, WAXED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getSide());
    }

    @Override
    protected ActionResult onUse(
            BlockState state,
            World world,
            BlockPos pos,
            PlayerEntity player,
            BlockHitResult hit
    ) {
        if (!state.get(WAXED)) {
            ItemStack mainHand = player.getMainHandStack();
            ItemStack offHand = player.getOffHandStack();

            if (mainHand.isOf(Items.HONEYCOMB) || offHand.isOf(Items.HONEYCOMB)) {
                if (!world.isClient) {
                    // Set to waxed
                    world.setBlockState(pos, state.with(WAXED, true), Block.NOTIFY_ALL);

                    // Consume one honeycomb (like vanilla copper)
                    if (!player.getAbilities().creativeMode) {
                        if (mainHand.isOf(Items.HONEYCOMB)) {
                            mainHand.decrement(1);
                        } else {
                            offHand.decrement(1);
                        }
                    }

                    // Play honeycomb place sound
                    world.playSound(null, pos, SoundEvents.ITEM_HONEYCOMB_WAX_ON, SoundCategory.BLOCKS, 1.0f, 1.0f);

                    // Replace your particle line with this:
                    ((ServerWorld)world).spawnParticles(ParticleTypes.WAX_ON,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            6,  // Number of particles
                            0.3, 0.3, 0.3,  // Spread (x,y,z)
                            0.05  // Speed
                    );
                }
                return ActionResult.success(world.isClient);
            }
        }

        if (!state.get(WAXED) && player.isSneaking()) {
            if (!world.isClient) {
                TrappedLimestoneMode current = state.get(MODE);
                TrappedLimestoneMode[] values = TrappedLimestoneMode.values();
                TrappedLimestoneMode next = values[(current.ordinal() + 1) % values.length];

                world.setBlockState(
                        pos,
                        state.with(MODE, next),
                        Block.NOTIFY_ALL
                );

                world.playSound(
                        null,
                        pos,
                        ModSounds.TRAPPED_LIMESTONE_CYCLE,
                        SoundCategory.BLOCKS,
                        1.0f,
                        0.8f + world.random.nextFloat() * 0.2f
                );
            }
        }
        return ActionResult.SUCCESS;
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// REDSTONE TRIGGER — call trap when powered
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) return;
        if (!state.get(WAXED)) return;

        // Existing direct power check (redstone dust, levers, etc.)
        if (world.getReceivedRedstonePower(pos) > 0) {
            handleWaxedTrigger(state, (ServerWorld) world, pos);
            return;
        }

        // NEW: Check if the update came from a repeater or target block
        // These don't give strong power, but they notify neighbors when powered
        if (sourcePos != null) {
            Block source = world.getBlockState(sourcePos).getBlock();

            if (source instanceof RepeaterBlock || source instanceof TargetBlock) {
                // Optional: Only trigger if the repeater/target is actually powered
                // Repeater: check if it's powered
                if (source instanceof RepeaterBlock) {
                    Boolean powered = world.getBlockState(sourcePos).get(RepeaterBlock.POWERED);
                    if (powered) {
                        handleWaxedTrigger(state, (ServerWorld) world, pos);
                    }
                }
                // Target block: triggers when hit by redstone signal
                else if (source instanceof TargetBlock) {
                    // Target blocks emit a short pulse when hit — neighborUpdate fires
                    // So just trigger on any update from it (vanilla-like behavior)
                    handleWaxedTrigger(state, (ServerWorld) world, pos);
                }
            }
        }
    }
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// ENTITY STEPPING / LANDING — ONLY if unsupported!
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
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
        if (world.isClient) return;
        if (!state.get(WAXED)) return;

        if (shouldTriggerFromEntity(entity) && isUnsupported(world, pos)) {
            handleWaxedTrigger(state, (ServerWorld) world, pos);
        }
    }

    private boolean shouldTriggerFromEntity(Entity entity) {
        return entity instanceof LivingEntity || entity instanceof ItemEntity;
    }

    private boolean isUnsupported(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).isAir();
    }

    // 1. Does this block emit redstone power? (Only when waxed)
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true; // Always has output (even if 0)
    }

    // 2. What strength does the comparator read?
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

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// MAIN TRAP EFFECT — called from redstone OR entity step
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private void handleWaxedTrigger(BlockState state, World world, BlockPos pos) {
        TrappedLimestoneMode mode = state.get(MODE);

        switch (mode) {
            case FIRE -> fireTriggered(state, world, pos);
            case ICE -> iceTriggered(state, world, pos);
            case CHAIN -> chainTriggered(state, world, pos);
            case DIRECTIONAL -> directionalTriggered(state, world, pos);
            case SINGLE -> singleTriggered(state, world, pos);
            case SAFE -> safeTriggered(state, world, pos);
        }
    }

    // Empty methods ready for your trap logic
    private void fireTriggered(BlockState state, World world, BlockPos pos) {
        if (world.isClient) return;
        ServerWorld serverWorld = (ServerWorld) world;

        // 1. Ignite entities in the plume direction
        Direction facing = state.get(FACING);

// Center of the block
        Vec3d center = Vec3d.ofCenter(pos);

// Create a box that extends 1 block in the facing direction (and a bit wide/tall)
        Vec3d min = center.subtract(0.5, 0.5, 0.5); // Base box around block center
        Vec3d max = center.add(0.5, 0.5, 0.5);

// Extend the box 1.5 blocks in the facing direction
        Vec3d extension = Vec3d.of(facing.getVector()).multiply(1.5);


// Or shorter: start from block box and stretch in facing
        Box fireBox = new Box(pos).stretch(facing.getVector().getX() * 1.5, facing.getVector().getY() * 1.5, facing.getVector().getZ() * 1.5).expand(0.4); // widen a bit

        for (LivingEntity entity : serverWorld.getEntitiesByClass(LivingEntity.class, fireBox, e -> true)) {
            entity.setOnFireFor(8);
            entity.damage(entity.getDamageSources().onFire(), 4.0f);
        }

        // 2. Fire sound
        serverWorld.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.5f, 0.8f + serverWorld.random.nextFloat() * 0.4f);

        // 3. DIRECTIONAL PLUME — tight cylinder shooting in FACING
        // Plume parameters
        Vec3d facingVec = Vec3d.of(facing.getVector());
        Vec3d plumeStart = Vec3d.ofCenter(pos).add(facingVec.multiply(0.51)); // slightly outside
        Vec3d velocity = facingVec.multiply(0.3); // particle speed
        double radius = 0.2;        // thicker plume
        double plumeLength = 2.0;   // extend 2 blocks
        int flameParticles = 30;    // total flames along the plume
        double tinySpread = 0.015;

// Spawn flame particles along the plume length
        for (int i = 0; i < flameParticles; i++) {
            double t = world.random.nextDouble() * plumeLength; // random distance along the plume
            Vec3d particlePos = plumeStart.add(facingVec.multiply(t));

            double offsetX = (facing.getOffsetX() == 0 ? (world.random.nextDouble() - 0.5) * radius : 0);
            double offsetY = (facing.getOffsetY() == 0 ? (world.random.nextDouble() - 0.5) * radius : 0);
            double offsetZ = (facing.getOffsetZ() == 0 ? (world.random.nextDouble() - 0.5) * radius : 0);

            serverWorld.spawnParticles(ParticleTypes.FLAME,
                    particlePos.x + offsetX,
                    particlePos.y + offsetY,
                    particlePos.z + offsetZ,
                    1,
                    velocity.x + (world.random.nextDouble() - 0.5) * tinySpread,
                    velocity.y + (world.random.nextDouble() - 0.5) * tinySpread,
                    velocity.z + (world.random.nextDouble() - 0.5) * tinySpread,
                    0.0  // keep the spread param 0 so velocity defines direction
            );
        }

// Smoke following the plume
        int smokeParticles = 15;
        for (int i = 0; i < smokeParticles; i++) {
            double t = world.random.nextDouble() * plumeLength;
            Vec3d particlePos = plumeStart.add(facingVec.multiply(t));

            double offsetX = (facing.getOffsetX() == 0 ? (world.random.nextDouble() - 0.5) * radius * 1.2 : 0);
            double offsetY = (facing.getOffsetY() == 0 ? (world.random.nextDouble() - 0.5) * radius * 1.2 : 0);
            double offsetZ = (facing.getOffsetZ() == 0 ? (world.random.nextDouble() - 0.5) * radius * 1.2 : 0);

            serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                    particlePos.x + offsetX,
                    particlePos.y + offsetY,
                    particlePos.z + offsetZ,
                    1,
                    velocity.x * 0.3 + (world.random.nextDouble() - 0.5) * tinySpread,
                    velocity.y * 0.3 + (world.random.nextDouble() - 0.5) * tinySpread,
                    velocity.z * 0.3 + (world.random.nextDouble() - 0.5) * tinySpread,
                    0.0
            );
        }
    }

    private void iceTriggered(BlockState state, World world, BlockPos pos) {
        if (world.isClient) return;
        ServerWorld serverWorld = (ServerWorld) world;

        // 1. FREEZE entities in the plume direction (powder snow effect)
        Direction facing = state.get(FACING);
        Box freezeBox = new Box(pos)
                .stretch(facing.getVector().getX() * 1.5, facing.getVector().getY() * 1.5, facing.getVector().getZ() * 1.5)
                .expand(0.4);

        for (LivingEntity entity : serverWorld.getEntitiesByClass(LivingEntity.class, freezeBox, e -> true)) {
            // Exact powder snow freeze effect
            entity.setFrozenTicks(140);  // Max freeze — full overlay + damage starts

            // Optional extra kick (immediate damage + stronger slow)
            entity.damage(entity.getDamageSources().freeze(), 2.0f);
        }
        // 2. Fire sound
        serverWorld.playSound(null, pos,
                SoundEvents.BLOCK_SNOW_BREAK,  // icy/frosty sound
                SoundCategory.BLOCKS,
                1.2f,                          // volume
                0.9f + serverWorld.random.nextFloat() * 0.2f // slight pitch variation
        );
        // 3. DIRECTIONAL PLUME — tight cylinder shooting in FACING
        // Plume parameters
        Vec3d facingVec = Vec3d.of(facing.getVector());
        Vec3d plumeStart = Vec3d.ofCenter(pos).add(facingVec.multiply(0.51)); // slightly outside
        Vec3d velocity = facingVec.multiply(0.3); // particle speed
        double radius = 0.2;        // thicker plume
        double plumeLength = 2.0;   // extend 2 blocks
        int flameParticles = 30;    // total flames along the plume
        double tinySpread = 0.015;

// Spawn flame particles along the plume length
        for (int i = 0; i < flameParticles; i++) {
            double t = world.random.nextDouble() * plumeLength; // random distance along the plume
            Vec3d particlePos = plumeStart.add(facingVec.multiply(t));

            double offsetX = (facing.getOffsetX() == 0 ? (world.random.nextDouble() - 0.5) * radius : 0);
            double offsetY = (facing.getOffsetY() == 0 ? (world.random.nextDouble() - 0.5) * radius : 0);
            double offsetZ = (facing.getOffsetZ() == 0 ? (world.random.nextDouble() - 0.5) * radius : 0);

            serverWorld.spawnParticles(ParticleTypes.ITEM_SNOWBALL,
                    particlePos.x + offsetX,
                    particlePos.y + offsetY,
                    particlePos.z + offsetZ,
                    1,
                    velocity.x + (world.random.nextDouble() - 0.5) * tinySpread,
                    velocity.y + (world.random.nextDouble() - 0.5) * tinySpread,
                    velocity.z + (world.random.nextDouble() - 0.5) * tinySpread,
                    0.0  // keep the spread param 0 so velocity defines direction
            );
        }

// Smoke following the plume
        int smokeParticles = 15;
        for (int i = 0; i < smokeParticles; i++) {
            double t = world.random.nextDouble() * plumeLength;
            Vec3d particlePos = plumeStart.add(facingVec.multiply(t));

            double offsetX = (facing.getOffsetX() == 0 ? (world.random.nextDouble() - 0.5) * radius * 1.2 : 0);
            double offsetY = (facing.getOffsetY() == 0 ? (world.random.nextDouble() - 0.5) * radius * 1.2 : 0);
            double offsetZ = (facing.getOffsetZ() == 0 ? (world.random.nextDouble() - 0.5) * radius * 1.2 : 0);

            serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE,
                    particlePos.x + offsetX,
                    particlePos.y + offsetY,
                    particlePos.z + offsetZ,
                    1,
                    velocity.x * 0.3 + (world.random.nextDouble() - 0.5) * tinySpread,
                    velocity.y * 0.3 + (world.random.nextDouble() - 0.5) * tinySpread,
                    velocity.z * 0.3 + (world.random.nextDouble() - 0.5) * tinySpread,
                    0.0
            );
        }
    }

    private void chainTriggered(BlockState state, World world, BlockPos pos) {
        if (world.isClient) return;
        ServerWorld serverWorld = (ServerWorld) world;

        // 1. First, do the normal SINGLE collapse on THIS block (cracks if crouching)
        singleTriggered(state, serverWorld, pos);

        // 2. Schedule neighbor check AFTER this block breaks (so it becomes unsupported)
        serverWorld.scheduleBlockTick(pos, this, 1); // 1 tick delay
    }

    private void spreadChainReaction(ServerWorld world, BlockPos brokenPos) {
        // Check all 6 directions
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = brokenPos.offset(direction);
            BlockState neighborState = world.getBlockState(neighborPos);

            // Must be: our trapped_limestone + waxed + unsupported (air below)
            if (neighborState.getBlock() == this &&
                    neighborState.get(WAXED) &&
                    world.getBlockState(neighborPos.down()).isAir()) {

                // Trigger its SINGLE collapse logic (cracks if someone crouching on it)
                singleTriggered(neighborState, world, neighborPos);

                // Small delay so the wave spreads naturally (not instant flood)
                java.util.Random random = null;
                world.scheduleBlockTick(neighborPos, this, 2 + random.nextInt(3)); // 2-4 tick stagger
            }
        }
    }

    private void directionalTriggered(BlockState state, World world, BlockPos pos) {

    }

    

    private void singleTriggered(BlockState state, World world, BlockPos pos) {
        if (world.isClient) return;

        ServerWorld serverWorld = (ServerWorld) world;

        // Get entities on top
        List<Entity> entities = serverWorld.getOtherEntities(null,
                new Box(pos.getX(), pos.getY() + 0.5, pos.getZ(), pos.getX() + 1, pos.getY() + 1.5, pos.getZ() + 1));

        boolean hasCrouchingPlayer = false;

        for (Entity entity : entities) {
            if (entity instanceof ServerPlayerEntity player && player.isSneaking()) {
                hasCrouchingPlayer = true;
                break;
            }
        }

        if (!hasCrouchingPlayer) {
            // Instant break — no mercy
            serverWorld.breakBlock(pos, false);
            return;
        }

        // Crouching player → dramatic delayed break with cracks
        final int delayTicks = 10; // ~0.5 seconds
        final int fakeEntityId = pos.hashCode(); // Unique per block

        // Show progressive cracks
        for (int stage = 0; stage <= 9; stage++) {
            final int currentStage = stage;
            serverWorld.getServer().execute(() -> {
                sendBreakProgress(serverWorld, pos, fakeEntityId, currentStage);
            });
            // Optional: add small delay between stages if you want slower cracks
        }

        // Final break after delay
        serverWorld.scheduleBlockTick(pos, this, delayTicks);
    }

    // Called when the scheduled tick runs
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        final int fakeEntityId = pos.hashCode();
        sendBreakProgress(world, pos, fakeEntityId, -1); // Remove cracks
        world.breakBlock(pos, false);
    }

    private void sendBreakProgress(ServerWorld world, BlockPos pos, int entityId, int stage) {
        BlockBreakingProgressS2CPacket packet = new BlockBreakingProgressS2CPacket(entityId, pos, stage);
        world.getPlayers().forEach(player -> player.networkHandler.sendPacket(packet));
    }

    // Helper to send break progress to all nearby players
    private void sendBreakProgressToNearby(ServerWorld world, BlockPos pos, int entityId, int stage) {
        List<ServerPlayerEntity> nearbyPlayers = world.getPlayers(player -> player.getPos().squaredDistanceTo(Vec3d.ofCenter(pos)) < 64 * 64);

        BlockBreakingProgressS2CPacket packet = new BlockBreakingProgressS2CPacket(entityId, pos, stage);

        for (ServerPlayerEntity player : nearbyPlayers) {
            player.networkHandler.sendPacket(packet);
        }
    }

    private void safeTriggered(BlockState state, World world, BlockPos pos) {
        // Safe (no effect) trap logic here
    }

}

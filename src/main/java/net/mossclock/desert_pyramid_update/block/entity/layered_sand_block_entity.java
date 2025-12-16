package net.mossclock.desert_pyramid_update.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class layered_sand_block_entity extends BlockEntity {
    public static final int DEFAULT_WET_TINT = 0x4A6741;  // darker wet sand color

    private int wetColor = 0xFFFFFF;  // white = dry (private + getter/setter is cleaner)

    public layered_sand_block_entity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setWet(boolean wet) {
        this.wetColor = wet ? DEFAULT_WET_TINT : 0xFFFFFF;
        markDirty();
    }

    public int getTintColor() {
        return wetColor;
    }

    // === FIXED: New 1.21+ NBT methods with RegistryWrapper ===
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("wet_color", wetColor);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        wetColor = nbt.getInt("wet_color");
    }

    // Optional: fallback for old worlds (if you care about compatibility)
    @Override
    public void readNbt(NbtCompound nbt) {  // deprecated, but keeps old worlds working
        super.readNbt(nbt);
        wetColor = nbt.getInt("wet_color");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("wet_color", wetColor);
    }
}
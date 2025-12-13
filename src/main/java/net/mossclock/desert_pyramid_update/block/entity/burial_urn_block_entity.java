
package net.mossclock.desert_pyramid_update.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class burial_urn_block_entity extends BlockEntity implements ImplementedInventory, SidedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(16, ItemStack.EMPTY);

    public burial_urn_block_entity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BURIAL_URN_ENTITY_TYPE, pos, state);
    }

    private int getValueFromRarity(ItemStack stack) {
        Rarity rarity = stack.getRarity();

        return switch (rarity) {
            case COMMON    -> 1;
            case UNCOMMON  -> 5;
            case RARE      -> 10;
            case EPIC      -> 15;
        };
    }
    public int getRarityPower() {
        for (int i = 0; i < this.size(); i++) {
            ItemStack stack = this.getStack(i);
            if (!stack.isEmpty()) return Math.min(15, getValueFromRarity(stack));
        }
        return 0; // empty urn
    }

    public int getFilledSlots() {
        int filled = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.getStack(i).isEmpty()) filled++;
        }
        return filled; // 0–16 (MC will clamp to 15 anyway)
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            inventory.set(slot, ItemStack.EMPTY);
            markDirty();
            return;
        }

        int total = getTotalItems() - inventory.get(slot).getCount(); // current total excluding this slot
        if (total >= 16) {
            inventory.set(slot, ItemStack.EMPTY); // no room left
        } else {
            ItemStack copy = stack.copy();
            copy.setCount(1); // per-slot clamp
            inventory.set(slot, copy);
        }

        markDirty();
    }
    @Override
    public int[] getAvailableSlots(Direction side) {
        // Allow all 16 slots from any side
        int[] slots = new int[16];
        for (int i = 0; i < 16; i++) slots[i] = i;
        return slots;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return getTotalItems() < 16 && !stack.isEmpty();
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true; // allow taking items out
    }

    @Override
    public int getMaxCountPerStack() {
        return 1; // Critical: prevents hoppers from inserting more than 1 per slot
    }
    public int getTotalItems() {
        int total = 0;
        for (ItemStack stack : inventory) {
            total += stack.getCount();
        }
        return total;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.world != null) {
            world.updateComparators(pos, this.getCachedState().getBlock());
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}

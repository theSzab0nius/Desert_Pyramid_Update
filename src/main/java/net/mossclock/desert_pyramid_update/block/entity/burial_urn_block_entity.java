
package net.mossclock.desert_pyramid_update.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class burial_urn_block_entity extends BlockEntity implements ImplementedInventory {
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
        int total = 0;

        for (int i = 0; i < this.size(); i++) {
            ItemStack stack = this.getStack(i);
            if (!stack.isEmpty()) total += getValueFromRarity(stack);
        }

        return Math.min(15, total); // always clamp to 15 so it's vanilla-legal
    }

    public int getFilledSlots() {
        int filled = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.getStack(i).isEmpty()) filled++;
        }
        return filled; // 0–16 (MC will clamp to 15 anyway)
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

package net.mossclock.desert_pyramid_update.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.mossclock.desert_pyramid_update.block.ModBlockEntities;
import net.minecraft.registry.RegistryWrapper;

public class burial_urn_block_entity extends BlockEntity {

    private ItemStack storedItem = ItemStack.EMPTY; // stores 1 item (can be any)

    public burial_urn_block_entity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BURIAL_URN_ENTITY_TYPE, pos, state);
    }

    public ItemStack getStack() {
        return storedItem;
    }

    public void setStack(ItemStack stack) {
        this.storedItem = stack;
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        if (!storedItem.isEmpty()) {
            nbt.put("StoredItem", storedItem.encode(registries));  // <--- Use encode() here
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);

        if (nbt.contains("StoredItem")) {
            storedItem = ItemStack.fromNbt(registries, nbt.getCompound("StoredItem"))
                    .orElse(ItemStack.EMPTY);  // <--- this fixes it
        } else {
            storedItem = ItemStack.EMPTY;
        }
    }
}

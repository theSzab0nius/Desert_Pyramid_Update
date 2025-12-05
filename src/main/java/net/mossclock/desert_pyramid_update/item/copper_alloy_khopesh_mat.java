package net.mossclock.desert_pyramid_update.item;

import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class copper_alloy_khopesh_mat implements ToolMaterial {
    public static final copper_alloy_khopesh_mat INSTANCE = new copper_alloy_khopesh_mat();

    @Override
    public int getDurability() { return 200; }

    @Override
    public float getMiningSpeedMultiplier() { return 1.0f; }

    @Override
    public float getAttackDamage() { return 5.0f; }

    @Override
    public int getEnchantability() { return 15; }

    @Override
    public Ingredient getRepairIngredient() { return Ingredient.ofItems(Items.COPPER_INGOT); }

    // this is required by some mappings — match the exact signature in your ToolMaterial
    // If your ToolMaterial declares this method, make sure the return type matches what your IDE shows.
    public TagKey<Block> getInverseTag() {
        return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
    }
}

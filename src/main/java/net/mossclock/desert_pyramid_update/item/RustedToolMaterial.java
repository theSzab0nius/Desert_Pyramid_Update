package net.mossclock.desert_pyramid_update.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.BlockTags;

public class RustedToolMaterial implements ToolMaterial {
    public static final RustedToolMaterial INSTANCE = new RustedToolMaterial();

    @Override
    public int getDurability() { return 10; }

    @Override
    public float getMiningSpeedMultiplier() { return 1.0f; }

    @Override
    public float getAttackDamage() { return 1.0f; }

    @Override
    public int getEnchantability() { return 5; }

    @Override
    public Ingredient getRepairIngredient() { return Ingredient.EMPTY; }

    // this is required by some mappings — match the exact signature in your ToolMaterial
    // If your ToolMaterial declares this method, make sure the return type matches what your IDE shows.
    public TagKey<Block> getInverseTag() {
        return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
    }
}

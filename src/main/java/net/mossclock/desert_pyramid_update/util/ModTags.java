package net.mossclock.desert_pyramid_update.util;

import net.minecraft.enchantment.Enchantment;
import net.mossclock.desert_pyramid_update.Desert_pyramid_update;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Enchantments {
        public static final TagKey<Enchantment> CUSTOM_DAMAGE = createTag("custom_damage");

        private static TagKey<Enchantment> createTag(String name) {
            return TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Desert_pyramid_update.MOD_ID, name));
        }
    }
}

package net.mossclock.desert_pyramid_update;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.mossclock.desert_pyramid_update.block.ModBlocks;
import net.mossclock.desert_pyramid_update.block.entity.ModBlockEntities;
import net.mossclock.desert_pyramid_update.enchantment.ModEnchantmentEffects;
import net.mossclock.desert_pyramid_update.item.IronAlloyKhopeshItem;
import net.mossclock.desert_pyramid_update.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Desert_pyramid_update implements ModInitializer {
	public static final String MOD_ID = "desert_pyramid_update";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        ModEnchantmentEffects.bootstrap();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModBlockEntities.registerAll();


	}



}
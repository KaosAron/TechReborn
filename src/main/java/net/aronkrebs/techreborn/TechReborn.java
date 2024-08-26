package net.aronkrebs.techreborn;

import net.aronkrebs.techreborn.block.ModBlocks;
import net.aronkrebs.techreborn.block.entity.ModBlockEntities;
import net.aronkrebs.techreborn.datagen.ModWorldGenerator;
import net.aronkrebs.techreborn.item.ModItemGroups;
import net.aronkrebs.techreborn.item.ModItems;
import net.aronkrebs.techreborn.recipe.ModRecipes;
import net.aronkrebs.techreborn.screen.ModScreenHandlers;
import net.aronkrebs.techreborn.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechReborn implements ModInitializer {
	public static final String MOD_ID = "techreborn";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();

		ModRecipes.registerRecipes();

		ModWorldGeneration.generateModWorldGen();
	}
}
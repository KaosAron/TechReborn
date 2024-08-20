package net.aronkrebs.techreborn.datagen;

import net.aronkrebs.techreborn.block.ModBlocks;
import net.aronkrebs.techreborn.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LITHIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_LITHIUM_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LITHIUM_ORE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.RAW_LITHIUM, Models.GENERATED);
        itemModelGenerator.register(ModItems.DIAMOND_DUST, Models.GENERATED);
        itemModelGenerator.register(ModItems.BATTERY, Models.GENERATED);
        itemModelGenerator.register(ModItems.LITHIUM_INGOT, Models.GENERATED);
    }
}

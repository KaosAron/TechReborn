package net.aronkrebs.techreborn.datagen;

import net.aronkrebs.techreborn.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)       // Needs Pickaxe to get mined
                .add(ModBlocks.LITHIUM_BLOCK)
                .add(ModBlocks.DEEPSLATE_LITHIUM_ORE)
                .add(ModBlocks.LITHIUM_ORE)

                .add(ModBlocks.PULVERIZER_BLOCK)
                .add(ModBlocks.SOLAR_GENERATOR_MK1);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)       // Needs Stone Tool Level to get mined
                .add(ModBlocks.PULVERIZER_BLOCK)
                .add(ModBlocks.SOLAR_GENERATOR_MK1);

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)        // Needs Iron Tool Level to get mined
                .add(ModBlocks.LITHIUM_ORE)
                .add(ModBlocks.LITHIUM_BLOCK)
                .add(ModBlocks.DEEPSLATE_LITHIUM_ORE);

        //getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)   // Needs Diamond Tool Level to get mined
        //        .add(ModBlocks.)

        //getOrCreateTagBuilder(TagKey.of(RegistryKeys.BLOCK, new Identifier("fabric", "needs_tool_level_4")))      // Needs Netherite Tool Level to get mined
        //        .add(ModBlocks.)
    }
}

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

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.LITHIUM_BLOCK)
                .add(ModBlocks.PULVERIZER_BLOCK)
                .add(ModBlocks.DEEPSLATE_LITHIUM_ORE)
                .add(ModBlocks.LITHIUM_ORE)
                .add(ModBlocks.COAL_GENERATOR);

        //getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
        //        .add(ModBlocks.)

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.LITHIUM_ORE)
                .add(ModBlocks.LITHIUM_BLOCK)
                .add(ModBlocks.DEEPSLATE_LITHIUM_ORE);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.PULVERIZER_BLOCK)
                .add(ModBlocks.COAL_GENERATOR);
        //getOrCreateTagBuilder(TagKey.of(RegistryKeys.BLOCK, new Identifier("fabric", "needs_tool_level_4")))
        //        .add(ModBlocks.)
    }
}

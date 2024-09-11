package net.aronkrebs.techreborn.datagen;

import net.aronkrebs.techreborn.block.ModBlocks;
import net.aronkrebs.techreborn.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    private static final List<ItemConvertible> LITHIUM_SMELTABLES = List.of(ModBlocks.LITHIUM_ORE,
            ModBlocks.DEEPSLATE_LITHIUM_ORE, ModItems.RAW_LITHIUM);

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerSmelting(exporter, LITHIUM_SMELTABLES, RecipeCategory.MISC, ModItems.LITHIUM_INGOT,
                0.7f, 200, "lithium");
        offerBlasting(exporter, LITHIUM_SMELTABLES, RecipeCategory.MISC, ModItems.LITHIUM_INGOT,
                0.7f, 200, "lithium");

        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.LITHIUM_INGOT,
                RecipeCategory.DECORATIONS, ModBlocks.LITHIUM_BLOCK);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BATTERY, 1)
                .pattern(" L ")
                .pattern("LIL")
                .pattern("LIL")
                .input('L', ModItems.LITHIUM_INGOT)
                .input('I', Items.IRON_INGOT)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(ModItems.LITHIUM_INGOT), conditionsFromItem(ModItems.LITHIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BATTERY)));
    }
}

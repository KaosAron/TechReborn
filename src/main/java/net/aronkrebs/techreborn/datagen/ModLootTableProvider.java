package net.aronkrebs.techreborn.datagen;

import net.aronkrebs.techreborn.block.ModBlocks;
import net.aronkrebs.techreborn.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.PULVERIZER_BLOCK);
        addDrop(ModBlocks.SOLAR_GENERATOR_MK1);
        addDrop(ModBlocks.COAL_GENERATOR_MK1);

        addDrop(ModBlocks.LITHIUM_BLOCK);

        addDrop(ModBlocks.LITHIUM_ORE, oreDrops(ModBlocks.LITHIUM_ORE, ModItems.RAW_LITHIUM));
        addDrop(ModBlocks.DEEPSLATE_LITHIUM_ORE, oreDrops(ModBlocks.DEEPSLATE_LITHIUM_ORE, ModItems.RAW_LITHIUM));
    }




    public LootTable.Builder copperLikeOreDrops(Block drop, Item item) {
        return BlockLootTableGenerator.dropsWithSilkTouch(drop, (LootPoolEntry.Builder)this.applyExplosionDecay(drop,
                ((LeafEntry.Builder)
                        ItemEntry.builder(item)
                                .apply(SetCountLootFunction
                                        .builder(UniformLootNumberProvider
                                                .create(1.0f, 3.0f)))) //How much CopperLike Ore Drops
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))));
    }

}

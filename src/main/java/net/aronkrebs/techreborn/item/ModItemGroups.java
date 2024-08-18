package net.aronkrebs.techreborn.item;

import net.aronkrebs.techreborn.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.aronkrebs.techreborn.TechReborn;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup TECHREBORN_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(TechReborn.MOD_ID, "TechReborn"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.techreborn"))
                    .icon(() -> new ItemStack(ModItems.DIAMOND_DUST)).entries((displayContext, entries) -> {
                        entries.add(Items.DIAMOND);

                        entries.add(ModItems.DIAMOND_DUST);
                        entries.add(ModItems.BATTERY);
                        entries.add(ModItems.INGOTLITHIUM);
                        entries.add(ModItems.RAWLITHIUM);

                        entries.add(ModBlocks.PULVERIZER_BLOCK);
                        entries.add(ModBlocks.ORELITHIUM);
                        entries.add(ModBlocks.DEEPSLATEORELITHIUM);
                        entries.add(ModBlocks.LITHIUM_BLOCK);
                    }).build());


    public static void registerItemGroups() {
        TechReborn.LOGGER.info("Registering Item Groups for " + TechReborn.MOD_ID);
    }
}
package net.aronkrebs.techreborn.item;

import net.aronkrebs.techreborn.TechReborn;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item DIAMOND_DUST = registerItem("diamonddust", new Item(new FabricItemSettings()));
    public static final Item BATTERY = registerItem("battery", new Item(new FabricItemSettings()));
    public static final Item INGOTLITHIUM = registerItem("ingotlithium", new Item(new FabricItemSettings()));
    public static final Item RAWLITHIUM = registerItem("rawlithium", new Item(new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(DIAMOND_DUST);
        entries.add(BATTERY);
        entries.add(INGOTLITHIUM);
        entries.add(RAWLITHIUM);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(TechReborn.MOD_ID, name), item);
    }


    public static void registerModItems(){
        TechReborn.LOGGER.info("Registering Mod Items for " + TechReborn.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}

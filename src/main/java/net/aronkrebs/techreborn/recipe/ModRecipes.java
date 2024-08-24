package net.aronkrebs.techreborn.recipe;

import net.aronkrebs.techreborn.TechReborn;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(TechReborn.MOD_ID, PulverizerMK1Recipe.Serializer.ID),
                PulverizerMK1Recipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(TechReborn.MOD_ID, PulverizerMK1Recipe.Type.ID),
                PulverizerMK1Recipe.Type.INSTANCE);
    }
}

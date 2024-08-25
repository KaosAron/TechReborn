package net.aronkrebs.techreborn.compat;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.aronkrebs.techreborn.block.ModBlocks;
import net.aronkrebs.techreborn.recipe.PulverizerMK1Recipe;
import net.aronkrebs.techreborn.screen.PulverizerMK1Screen;

import java.awt.*;

public class TechRebornModREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new PulverizerMK1Category());

        registry.addWorkstations(PulverizerMK1Category.PULVERIZERMK1, EntryStacks.of(ModBlocks.PULVERIZER_BLOCK));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(PulverizerMK1Recipe.class, PulverizerMK1Recipe.Type.INSTANCE,
                PulverizerMK1Display::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(75, 30, 20, 30), PulverizerMK1Screen.class,
                PulverizerMK1Category.PULVERIZERMK1);
    }
}

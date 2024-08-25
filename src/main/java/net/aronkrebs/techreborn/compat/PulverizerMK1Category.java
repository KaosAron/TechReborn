package net.aronkrebs.techreborn.compat;

import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import net.minecraft.text.Text;

public class PulverizerMK1Category implements DisplayCategory<BasicDisplay> {
    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return null;
    }

    @Override
    public Text getTitle() {
        return null;
    }

    @Override
    public Renderer getIcon() {
        return null;
    }
}

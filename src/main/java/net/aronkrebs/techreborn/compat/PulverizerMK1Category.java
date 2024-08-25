package net.aronkrebs.techreborn.compat;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.aronkrebs.techreborn.TechReborn;
import net.aronkrebs.techreborn.block.ModBlocks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class PulverizerMK1Category implements DisplayCategory<BasicDisplay> {
    public static final Identifier TEXTURE =
            new Identifier(TechReborn.MOD_ID, "textures/gui/pulverizer_block_gui.png");

    public static final CategoryIdentifier<PulverizerMK1Display> PULVERIZERMK1 =
            CategoryIdentifier.of(TechReborn.MOD_ID, "pulverizermk1");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return PULVERIZERMK1;
    }

    @Override
    public Text getTitle() {
        return Text.literal("Pulverizer MK1");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.PULVERIZER_BLOCK.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();
        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, 175, 82)));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 44, startPoint.y + 34))
                .entries(display.getInputEntries().get(0)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 119, startPoint.y + 25))
                .markOutput().entries(display.getOutputEntries().get(0)));

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 90;
    }

}

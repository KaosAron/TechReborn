package net.aronkrebs.techreborn.compat;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.aronkrebs.techreborn.recipe.PulverizerMK1Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A display for Pulverizer MK1 recipes in the REI interface.
 */
public class PulverizerMK1Display extends BasicDisplay {
    /**
     * Constructs a new Pulverizer MK1 display with the given input and output ingredients.
     *
     * @param inputs  the input ingredients
     * @param outputs the output ingredients
     */
    public PulverizerMK1Display(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    /**
     * Constructs a new Pulverizer MK1 display from the given recipe.
     *
     * @param recipe the recipe to display
     */
    public PulverizerMK1Display(PulverizerMK1Recipe recipe) {
        super(getInputList(recipe), List.of(EntryIngredient.of(EntryStacks.of(recipe.output))));
    }

    /**
     * Returns a list of input ingredients for the given recipe.
     *
     * @param recipe the recipe
     * @return the input ingredients
     */
    private static List<EntryIngredient> getInputList(PulverizerMK1Recipe recipe) {
        if (recipe == null || recipe.getIngredients().isEmpty()) {
            return Collections.emptyList();
        }
        List<EntryIngredient> list = new ArrayList<>();
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(0)));
        return list;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PulverizerMK1Category.PULVERIZERMK1;
    }

    @Override
    public String toString() {
        return "PulverizerMK1Display{" +
                "inputs=" + this.inputs +
                ", outputs=" + this.outputs +
                '}';
    }
}
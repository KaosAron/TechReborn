package net.aronkrebs.techreborn.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraft.recipe.Ingredient;

import java.util.List;

public class CoalGeneratorMK1Recipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final List<Ingredient> recipeItems;
    private final Integer energy;

    public CoalGeneratorMK1Recipe(Identifier id, List<Ingredient> ingredients, Integer outputEnergy) {
        this.id = id;
        this.recipeItems = ingredients;
        this.energy = outputEnergy;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if(world.isClient()) {
            return false;
        }


        return recipeItems.get(0).test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return null;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.ofSize(this.recipeItems.size());
        list.addAll(recipeItems);
        return list;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CoalGeneratorMK1Recipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "coalgeneratormk1";
    }

    public static class Serializer implements RecipeSerializer<CoalGeneratorMK1Recipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "coalgeneratormk1";
        // this is the name given in the json file

        @Override
        public CoalGeneratorMK1Recipe read(Identifier id, JsonObject json) {
            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.EMPTY);

            Integer outputEnergy = JsonHelper.getInt(json, "outputEnergy");

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new CoalGeneratorMK1Recipe(id, inputs, outputEnergy);
        }

        @Override
        public CoalGeneratorMK1Recipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            Integer outputEnergy = buf.readInt();

            return new CoalGeneratorMK1Recipe(id, inputs, outputEnergy);
        }

        @Override
        public void write(PacketByteBuf buf, CoalGeneratorMK1Recipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeInt(recipe.energy);
        }
    }


}

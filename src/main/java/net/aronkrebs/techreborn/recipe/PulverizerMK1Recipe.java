package net.aronkrebs.techreborn.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.List;

public class PulverizerMK1Recipe implements Recipe<SimpleInventory> {
    private final ItemStack output;
    private final List<Ingredient> recipeItems;

    public PulverizerMK1Recipe(List<Ingredient> ingredients, ItemStack itemStack) {
        this.output = itemStack;
        this.recipeItems = ingredients;
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
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.ofSize(this.recipeItems.size());
        list.addAll(recipeItems);
        return list;
    }

    @Override
    public Identifier getId() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<PulverizerMK1Recipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "pulverizermk1";
    }

    public static class Serializer implements RecipeSerializer<PulverizerMK1Recipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "pulverizermk1";

        public static final Codec<PulverizerMK1Recipe> CODEC = RecordCodecBuilder.create(in -> in.group(
                validateAmount(Ingredient., 9).fieldOf("ingredients").forGetter(PulverizerMK1Recipe::getIngredients),
                RecipeCodecs.CRAFTING_RESULT.fieldOf("output").forGetter(r -> r.output)
        ).apply(in, PulverizerMK1Recipe::new));

        private static Codec<List<Ingredient>> validateAmount(Codec<Ingredient> delegate, int max) {
            return Codecs.validate(Codecs.validate(
                    delegate.listOf(), list -> list.size() > max ? DataResult.error(() -> "Recipe has too many ingredients!") : DataResult.success(list)
            ), list -> list.isEmpty() ? DataResult.error(() -> "Recipe has no ingredients!") : DataResult.success(list));
        }


        @Override
        public Codec<PulverizerMK1Recipe> codec() {
            return CODEC;
        }

        @Override
        public PulverizerMK1Recipe read(Identifier id, JsonObject json) {
            return null;
        }

        @Override
        public PulverizerMK1Recipe read(Identifier id, PacketByteBuf buf) {
            return null;
        }

        @Override
        public void write(PacketByteBuf buf, PulverizerMK1Recipe recipe) {

        }
    }

}

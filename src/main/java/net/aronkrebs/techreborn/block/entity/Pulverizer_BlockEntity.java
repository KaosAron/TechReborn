package net.aronkrebs.techreborn.block.entity;

import net.aronkrebs.techreborn.item.ModItems;
import net.aronkrebs.techreborn.recipe.PulverizerMK1Recipe;
import net.aronkrebs.techreborn.screen.PulverizerMK1ScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Pulverizer_BlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT1 = 1;
    private static final int OUTPUT_SLOT2 = 2;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxprogress = 72;


    public Pulverizer_BlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PULVERIZER_BLOCK_ENTITY ,pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index)  {
                    case 0 -> Pulverizer_BlockEntity.this.progress;
                    case 1 -> Pulverizer_BlockEntity.this.maxprogress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> Pulverizer_BlockEntity.this.progress = value;
                    case 1 -> Pulverizer_BlockEntity.this.maxprogress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Pulverizer");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("pulverizermk1.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("pulverizermk1.progress");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new PulverizerMK1ScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            return;
        }

        if(isOutputSlot1EmptyOrReceivable()) {
            if(this.hasRecipe()) {
                this.increaseCraftProgress();
                markDirty(world, pos, state);

                if(hasCraftingFinished()) {
                    this.craftItem();
                    this.resetProgress();
                }
            } else {
                this.resetProgress();
            }
        } else {
            this.resetProgress();
            markDirty(world, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem() {
        Optional<PulverizerMK1Recipe> recipe = getCurrentRecipe();

        this.removeStack(INPUT_SLOT, 1);

        this.setStack(OUTPUT_SLOT1, new ItemStack(recipe.get().getOutput(null).getItem(), getStack(OUTPUT_SLOT1).getCount() + recipe.get().getOutput(null).getCount()));
    }

    private boolean hasCraftingFinished() {
        return progress >= maxprogress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    private boolean hasRecipe() {

        Optional<PulverizerMK1Recipe> recipe = getCurrentRecipe();

        return recipe.isPresent() && canInsertAmountIntoOutputSlot1(recipe.get().getOutput(null)) && canInsertItemIntoOutputSlot1(recipe.get().getOutput(null).getItem());
    }

    private Optional<PulverizerMK1Recipe> getCurrentRecipe() {
        SimpleInventory inv = new SimpleInventory(this.size());
        for(int i = 0; i < this.size(); i++) {
            inv.setStack(i, this.getStack(i));
        }

        return getWorld().getRecipeManager().getFirstMatch(PulverizerMK1Recipe.Type.INSTANCE, inv, getWorld());
    }

    private boolean canInsertItemIntoOutputSlot1(Item item) {
        return this.getStack(OUTPUT_SLOT1).getItem() == item || this.getStack(OUTPUT_SLOT1).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot1(ItemStack result) {
        return this.getStack(OUTPUT_SLOT1).getCount() + result.getCount() <= getStack(OUTPUT_SLOT1).getMaxCount();
    }

    private boolean isOutputSlot1EmptyOrReceivable() {
        return this.getStack(OUTPUT_SLOT1).isEmpty() || this.getStack(OUTPUT_SLOT1).getCount() < this.getStack(OUTPUT_SLOT1).getMaxCount();
    }
}

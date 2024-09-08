package net.aronkrebs.techreborn.block.entity;

import net.aronkrebs.techreborn.networking.ModMessages;
import net.aronkrebs.techreborn.recipe.PulverizerMK1Recipe;
import net.aronkrebs.techreborn.screen.PulverizerMK1ScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;

public class Pulverizer_BlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(30000, 32 ,16) {
        @Override
        protected void onFinalCommit() {
            markDirty();
            if (!world.isClient()) {
                PacketByteBuf data = PacketByteBufs.create();
                data.writeLong(amount);
                data.writeBlockPos(getPos());

                for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                    ServerPlayNetworking.send(player, ModMessages.ENERGY_SYNC, data);
                }

            }

        }
    };

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT1 = 1;
    private static final int OUTPUT_SLOT2 = 2;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    private int storedEnergy = 0;
    private int maxStoredEnergy = 30000;


    public Pulverizer_BlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PULVERIZER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index)  {
                    case 0 -> Pulverizer_BlockEntity.this.progress;
                    case 1 -> Pulverizer_BlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> Pulverizer_BlockEntity.this.progress = value;
                    case 1 -> Pulverizer_BlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    public void setEnergyLevel(long energyLevel) {
        this.energyStorage.amount = energyLevel;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeLong(this.energyStorage.amount);
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
        nbt.putLong("pulverizermk1.energy", energyStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("pulverizermk1.progress");
        energyStorage.amount = nbt.getLong("pulverizermk1.energy");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new PulverizerMK1ScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!world.isClient() && hasEnoughEnergy()) {
            if (isOutputSlot1EmptyOrReceivable()) {
                if (this.hasRecipe()) {
                    this.increaseCraftProgress();
                    this.extractEnergy();
                    markDirty(world, pos, state);

                    if (hasCraftingFinished()) {
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
    }

    private void extractEnergy() {
        try(Transaction transaction = Transaction.openOuter()) {
            energyStorage.extract(32, transaction);
            transaction.commit();
        }
    }

    private boolean hasEnoughEnergy() {
        return energyStorage.amount >= 32;
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
        return progress >= maxProgress;
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

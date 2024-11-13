package net.aronkrebs.techreborn.block.entity;

import net.aronkrebs.techreborn.block.custom.CoalGeneratorMK1;
import net.aronkrebs.techreborn.networking.ModMessages;
import net.aronkrebs.techreborn.recipe.CoalGeneratorMK1Recipe;
import net.aronkrebs.techreborn.screen.CoalGeneratorMK1ScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
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

import static net.aronkrebs.techreborn.block.custom.CoalGeneratorMK1.WORKING;

public class CoalGeneratorMK1_BlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory{
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(30000, 0 ,16) {
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

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    public CoalGeneratorMK1_BlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COAL_GENERATOR_MK1_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index)  {
                    case 0 -> CoalGeneratorMK1_BlockEntity.this.progress;
                    case 1 -> CoalGeneratorMK1_BlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CoalGeneratorMK1_BlockEntity.this.progress = value;
                    case 1 -> CoalGeneratorMK1_BlockEntity.this.maxProgress = value;
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
        return Text.translatable("coal_generator_mk1_translate","Coal Generator");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("coal_generator_mk1.progress", progress);
        nbt.putLong("coal_generator_mk1.energy", energyStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("coal_generator_mk1.progress");
        energyStorage.amount = nbt.getLong("coal_generator_mk1.energy");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CoalGeneratorMK1ScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!world.isClient() && !isInputSlotEmpty()) {
            if (this.hasRecipe() || 1 == 1) {
                CoalGeneratorMK1.startWorking(world, pos, state);

                energyStorage.amount = Math.min(energyStorage.amount + 16, energyStorage.capacity);
                syncEnergy();

                this.increaseBurnProgress();
                markDirty(world, pos, state);
                if (hasBurningFinished()) {
                    deleteItem();

                    this.resetProgress();
                }
            } else {
                this.resetProgress();
                CoalGeneratorMK1.stopWorking(world, pos, state);
            }
        } else {
            this.resetProgress();
            CoalGeneratorMK1.stopWorking(world, pos, state);
            markDirty(world, pos, state);
        }
    }

    private boolean hasBurningFinished() {
        return progress >= maxProgress;
    }

    private void deleteItem() {
        this.removeStack(INPUT_SLOT, 1);
    }

    private void increaseBurnProgress() {
        progress++;
    }

    private boolean hasRecipe() {

        Optional<CoalGeneratorMK1Recipe> recipe = getCurrentRecipe();

        return recipe.isPresent();
    }

    private Optional<CoalGeneratorMK1Recipe> getCurrentRecipe() {
        SimpleInventory inv = new SimpleInventory(this.size());

        for(int i = 0; i < this.size(); i++) {
            inv.setStack(i, this.getStack(i));
        }

        return getWorld().getRecipeManager().getFirstMatch(CoalGeneratorMK1Recipe.Type.INSTANCE, inv, getWorld());
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void syncEnergy() {
        if (!world.isClient()) {
            PacketByteBuf data = PacketByteBufs.create();
            data.writeLong(energyStorage.amount);
            data.writeBlockPos(getPos());

            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                ServerPlayNetworking.send(player, ModMessages.ENERGY_SYNC, data);
            }
        }
    }

    public boolean isInputSlotEmpty() {
        return this.getStack(INPUT_SLOT).isEmpty();
    }
}

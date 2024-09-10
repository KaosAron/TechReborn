package net.aronkrebs.techreborn.block.entity;

import net.aronkrebs.techreborn.networking.ModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class SolarGeneratorMK1_BlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory  {

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(8000, 16 ,32) {
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


    public SolarGeneratorMK1_BlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOLAR_GENERATOR_MK1_BLOCK_ENTITY, pos, state);
    }

    public void setEnergyLevel(long energyLevel) {
        this.energyStorage.amount = energyLevel;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeLong(this.energyStorage.amount);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Solar Generator MK1");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putLong("solar_generator_mk1.energy", energyStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        energyStorage.amount = nbt.getLong("solar_generator_mk1.energy");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return null;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!world.isClient() && hasEnoughEnergy()) {
            if(world.isDay()) {

                long energyToExtract = energyStorage.maxExtract;

                try (Transaction transaction = Transaction.openOuter()) {
                    // Energie abziehen (mit der neuen Methode in API 3.0.0)
                    long extractedEnergy = energyStorage.extract(energyToExtract, transaction);

                    // Wenn Energie erfolgreich abgezogen wurde, an benachbarte BlockEntities verteilen
                    if (extractedEnergy > 0) {
                        distributeEnergy(world, pos, extractedEnergy, transaction);
                        transaction.commit(); // Commit der Transaktion, wenn alles erfolgreich ist
                    }
                }
            }
        }
    }

    private boolean hasEnoughEnergy() {
        return energyStorage.amount >= 64;
    }

    private void distributeEnergy(World world, BlockPos pos, long energy, Transaction transaction) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            EnergyStorage neighborStorage = EnergyStorage.SIDED.find(world, neighborPos, direction.getOpposite());

            if (neighborStorage != null) {
                // Übertragen der Energie an den Nachbarblock mit der neuen insert-Methode und einer Transaktion
                long acceptedEnergy = neighborStorage.insert(energy, transaction);

                // Energie abziehen, die erfolgreich übertragen wurde
                energyStorage.extract(acceptedEnergy, transaction);
            }
        }
    }
}

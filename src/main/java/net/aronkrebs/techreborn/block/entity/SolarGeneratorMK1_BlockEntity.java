package net.aronkrebs.techreborn.block.entity;

import net.aronkrebs.techreborn.networking.ModMessages;
import net.aronkrebs.techreborn.screen.SolarGeneratorMK1ScreenHandler;
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

public class SolarGeneratorMK1_BlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(8000, 0, 16) {
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

    protected final PropertyDelegate propertyDelegate;


    public SolarGeneratorMK1_BlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOLAR_GENERATOR_MK1_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return 0;
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int size() {
                return 0;
            }
        };
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
        return new SolarGeneratorMK1ScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }


    public void tick(World world, BlockPos pos, BlockState state) {
        if (!world.isClient()) {
            if (world.isDay()) {
                energyStorage.amount = Math.min(energyStorage.amount + 16, energyStorage.capacity);
                markDirty();  // Sicherstellen, dass der Block als geändert markiert wird
            }

            // Äußere Transaktion eröffnen
            try (Transaction transaction = Transaction.openOuter()) {
                if (hasEnoughEnergy() && hasValidProvider(transaction)) {  // Übergib die Transaktion
                    long energyToExtract = energyStorage.maxExtract;

                    long extractedEnergy = energyStorage.extract(energyToExtract, transaction);
                    if (extractedEnergy > 0) {
                        distributeEnergy(world, pos, extractedEnergy, transaction);  // Übergib die Transaktion
                        transaction.commit();  // Committe die Transaktion
                    }
                }
            }
        }
    }

    private boolean hasValidProvider(@Nullable Transaction outerTransaction) {
        boolean isOuterTransactionOwner = false;

        // Wenn keine äußere Transaktion vorhanden ist, eröffne eine neue
        if (outerTransaction == null) {
            outerTransaction = Transaction.openOuter();  // Eröffne eine äußere Transaktion
            isOuterTransactionOwner = true;  // Kennzeichne, dass diese Methode die Transaktion kontrolliert
        }

        try {
            long energyToExtract = energyStorage.maxExtract;

            // Prüfen, ob es einen Empfänger gibt
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.offset(direction);
                EnergyStorage neighborStorage = EnergyStorage.SIDED.find(world, neighborPos, direction.getOpposite());

                if (neighborStorage != null && neighborStorage.insert(energyToExtract, outerTransaction) > 0) {
                    // Wenn eine erfolgreiche Einfügung der Energie möglich ist, committe und gib true zurück
                    if (isOuterTransactionOwner) {
                        outerTransaction.commit();
                    }
                    return true;
                }
            }

            // Falls keine gültigen Empfänger gefunden wurden, gib false zurück
            return false;
        } catch (Exception e) {
            System.err.println("Fehler beim Überprüfen von Energiequellen: " + e.getMessage());
            if (isOuterTransactionOwner) {
                outerTransaction.abort();  // Abbrechen bei Fehler
            }
            return false;
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
                try (Transaction nestedTransaction = Transaction.openNested(transaction)) {
                    long acceptedEnergy = neighborStorage.insert(energy, nestedTransaction);
                    energyStorage.extract(acceptedEnergy, nestedTransaction);
                    nestedTransaction.commit();
                } catch (Exception e) {
                    // Fange unerwartete Fehler ab
                    System.err.println("Fehler beim Übertragen von Energie: " + e.getMessage());
                }
            }
        }
    }
}

package net.aronkrebs.techreborn.screen;

import net.aronkrebs.techreborn.block.entity.SolarGeneratorMK1_BlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import team.reborn.energy.api.base.SimpleEnergyStorage;


public class SolarGeneratorMK1ScreenHandler extends ScreenHandler {
    public final SolarGeneratorMK1_BlockEntity blockEntity;

    protected SolarGeneratorMK1ScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()));

        long energy = buf.readLong();           //Get and Set the Energy Level on opening GUI
        this.blockEntity.setEnergyLevel(energy);
    }

    public SolarGeneratorMK1ScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.SOLARGENERATORMK1_SCREEN_HANDLER, syncId);
        this.blockEntity = (SolarGeneratorMK1_BlockEntity) blockEntity;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}

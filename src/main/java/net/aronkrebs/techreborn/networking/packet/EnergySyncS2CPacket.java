package net.aronkrebs.techreborn.networking.packet;

import net.aronkrebs.techreborn.block.entity.SolarGeneratorMK1_BlockEntity;
import net.aronkrebs.techreborn.screen.SolarGeneratorMK1ScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.aronkrebs.techreborn.block.entity.Pulverizer_BlockEntity;
import net.aronkrebs.techreborn.screen.PulverizerMK1ScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class EnergySyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        long energy = buf.readLong();
        BlockPos position = buf.readBlockPos();

        if (client.world.getBlockEntity(position) instanceof Pulverizer_BlockEntity blockEntity) {
            blockEntity.setEnergyLevel(energy);

            if (client.player.currentScreenHandler instanceof PulverizerMK1ScreenHandler screenHandler &&
                    screenHandler.blockEntity.getPos().equals(position)) {
                blockEntity.setEnergyLevel(energy);
            }
        }

        if (client.world.getBlockEntity(position) instanceof SolarGeneratorMK1_BlockEntity blockEntity) {
            blockEntity.setEnergyLevel(energy);

            if (client.player.currentScreenHandler instanceof SolarGeneratorMK1ScreenHandler screenHandler &&
                    screenHandler.blockEntity.getPos().equals(position)) {
                blockEntity.setEnergyLevel(energy);
            }
        }
    }
}
package net.aronkrebs.techreborn.networking.packet;

import net.aronkrebs.techreborn.block.entity.CoalGeneratorMK1_BlockEntity;
import net.aronkrebs.techreborn.block.entity.SolarGeneratorMK1_BlockEntity;
import net.aronkrebs.techreborn.networking.ModMessages;
import net.aronkrebs.techreborn.screen.CoalGeneratorMK1ScreenHandler;
import net.aronkrebs.techreborn.screen.SolarGeneratorMK1ScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.aronkrebs.techreborn.block.entity.Pulverizer_BlockEntity;
import net.aronkrebs.techreborn.screen.PulverizerMK1ScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.base.SimpleEnergyStorage;

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

        if (client.world.getBlockEntity(position) instanceof CoalGeneratorMK1_BlockEntity blockEntity) {
            blockEntity.setEnergyLevel(energy);

            if (client.player.currentScreenHandler instanceof CoalGeneratorMK1ScreenHandler screenHandler &&
                    screenHandler.blockEntity.getPos().equals(position)) {
                blockEntity.setEnergyLevel(energy);
            }
        }
    }
}
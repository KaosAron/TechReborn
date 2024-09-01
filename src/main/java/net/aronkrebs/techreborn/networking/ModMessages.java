package net.aronkrebs.techreborn.networking;

import net.aronkrebs.techreborn.TechReborn;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.aronkrebs.techreborn.networking.packet.EnergySyncS2CPacket;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier ENERGY_SYNC = new Identifier(TechReborn.MOD_ID, "energy_sync");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ENERGY_SYNC, EnergySyncS2CPacket::receive);
    }
}

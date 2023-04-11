package net.splurpy.paranoiamod.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.splurpy.paranoiamod.ParanoiaMod;
import net.splurpy.paranoiamod.networking.packet.SanityDataSyncS2CPacket;
import net.splurpy.paranoiamod.networking.packet.SwallowPillC2SPacket;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ParanoiaMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;
        // Swallowing a sanity pill
        net.messageBuilder(SwallowPillC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SwallowPillC2SPacket::new)
                .encoder(SwallowPillC2SPacket::toBytes)
                .consumerMainThread(SwallowPillC2SPacket::handle)
                .add();
        // Sync Sanity data between server and client
        net.messageBuilder(SanityDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SanityDataSyncS2CPacket::new)
                .encoder(SanityDataSyncS2CPacket::toBytes)
                .consumerMainThread(SanityDataSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}

package net.splurpy.paranoiamod.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.splurpy.paranoiamod.client.ClientSanityData;
import net.splurpy.paranoiamod.sanity.PlayerSanityProvider;
import net.splurpy.paranoiamod.sound.ModSounds;

import java.util.function.Supplier;

public class SanityDataSyncS2CPacket {
    private final int sanity;
    public SanityDataSyncS2CPacket(int sanity) {
        this.sanity = sanity;
    }

    public SanityDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.sanity = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(sanity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // THIS IS ON THE CLIENT
            ClientSanityData.set(sanity);
        });
        return true;
    }
}

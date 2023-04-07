package net.splurpy.paranoiamod.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.splurpy.paranoiamod.sanity.PlayerSanityProvider;
import net.splurpy.paranoiamod.sound.ModSounds;

import java.util.function.Supplier;

public class SwallowPillC2SPacket {
    public SwallowPillC2SPacket() {

    }

    public SwallowPillC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            // THIS IS ON THE SERVER
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            InteractionHand hand = player.getUsedItemHand();
            ItemStack itemStack = player.getItemInHand(hand);

            // Play the swallowing SoundEvent
            level.playSound(null, player.getOnPos(), ModSounds.SWALLOW_PILL.get(), SoundSource.PLAYERS,
                    1.0f, level.random.nextFloat() * 0.1f + 0.9f);

            // Consume the pill from the itemStack
            itemStack.setCount(itemStack.getCount() - 1);
            player.setItemInHand(hand, itemStack);

            // Alter Sanity Accordingly
            player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                sanity.incSanity(10);
                // For testing, output current Sanity
                player.sendSystemMessage(Component.literal("Current Sanity: " + sanity.getSanity()));
            });
        });
        return true;
    }
}

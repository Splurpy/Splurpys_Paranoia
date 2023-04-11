package net.splurpy.paranoiamod.item.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.splurpy.paranoiamod.networking.ModMessages;
import net.splurpy.paranoiamod.networking.packet.SwallowPillC2SPacket;
import net.splurpy.paranoiamod.sanity.SanityEvents;
import net.splurpy.paranoiamod.sound.ModSounds;

public class SanityPill extends Item {
    public SanityPill(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            // For testing purposes
            ModMessages.sendToServer(new SwallowPillC2SPacket());
        }
        return InteractionResultHolder.success(itemStack);
    }
}

package net.splurpy.paranoiamod.event;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.splurpy.paranoiamod.ParanoiaMod;
import net.splurpy.paranoiamod.sanity.PlayerSanity;
import net.splurpy.paranoiamod.sanity.PlayerSanityProvider;

@Mod.EventBusSubscriber(modid = ParanoiaMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerSanityProvider.PLAYER_SANITY).isPresent()) {
                event.addCapability(new ResourceLocation(ParanoiaMod.MOD_ID, "properties"), new PlayerSanityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(newStore -> {
                    newStore.from(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSanity.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if ((event.player.level.getBrightness(LightLayer.BLOCK, event.player.blockPosition())) <= 7) {

            // System.out.println("[DEBUG] Light level is 7 or below.");
            if(event.side == LogicalSide.SERVER) {
                event.player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {

                    if(sanity.getSanity() > 0 && event.player.getRandom().nextFloat() < 0.005f) { // Once Every 10 Seconds on Avg
                        sanity.decSanity(1);
                    }

                });
            }
        }

        // System.out.println("[DEBUG] TICK");
    }
}

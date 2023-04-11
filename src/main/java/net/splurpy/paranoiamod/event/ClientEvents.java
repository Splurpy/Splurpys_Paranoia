package net.splurpy.paranoiamod.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.splurpy.paranoiamod.ParanoiaMod;
import net.splurpy.paranoiamod.client.InsaneScreenOverlay;
import net.splurpy.paranoiamod.client.SanityHudOverlay;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = ParanoiaMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerBelow(new ResourceLocation("sleep_fade"), "sanity", SanityHudOverlay.HUD_SANITY);
            event.registerBelow(new ResourceLocation("hotbar"), "insane_effect_overlay", InsaneScreenOverlay.INSANE_OVERLAY);
        }
    }
}

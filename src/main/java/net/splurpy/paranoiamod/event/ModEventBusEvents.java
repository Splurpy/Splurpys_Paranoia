package net.splurpy.paranoiamod.event;

import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.splurpy.paranoiamod.ParanoiaMod;
import net.splurpy.paranoiamod.particle.ModParticles;
import net.splurpy.paranoiamod.particle.custom.LithiumFlameParticles;

@Mod.EventBusSubscriber(modid = ParanoiaMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.register(ModParticles.LITHIUM_FLAME_PARTICLES.get(),
                LithiumFlameParticles.Provider::new);
    }
}

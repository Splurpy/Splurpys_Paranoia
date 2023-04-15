package net.splurpy.paranoiamod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.splurpy.paranoiamod.block.ModBlocks;
import net.splurpy.paranoiamod.block.entity.ModBlockEntities;
import net.splurpy.paranoiamod.effect.ModEffects;
import net.splurpy.paranoiamod.item.ModItems;
import net.splurpy.paranoiamod.networking.ModMessages;
import net.splurpy.paranoiamod.particle.ModParticles;
import net.splurpy.paranoiamod.recipe.ModRecipes;
import net.splurpy.paranoiamod.screen.ModMenuTypes;
import net.splurpy.paranoiamod.screen.MortarAndPestleScreen;
import net.splurpy.paranoiamod.sound.ModSounds;
import net.splurpy.paranoiamod.world.feature.ModConfiguredFeatures;
import net.splurpy.paranoiamod.world.feature.ModPlacedFeatures;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ParanoiaMod.MOD_ID)
public class ParanoiaMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "paranoiamod";
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace


    public ParanoiaMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);

        ModConfiguredFeatures.register(modEventBus);
        ModPlacedFeatures.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);

        ModSounds.register(modEventBus);

        ModParticles.register(modEventBus);

        ModEffects.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
           ModMessages.register();
        });

        ModMessages.register();
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.MORTAR_AND_PESTLE_MENU.get(), MortarAndPestleScreen::new);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LITHIUM_TORCH.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LITHIUM_WALL_TORCH.get(), RenderType.cutout());
        }
    }
}

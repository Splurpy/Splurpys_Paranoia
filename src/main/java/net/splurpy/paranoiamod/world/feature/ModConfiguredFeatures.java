package net.splurpy.paranoiamod.world.feature;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.splurpy.paranoiamod.ParanoiaMod;
import net.splurpy.paranoiamod.block.ModBlocks;

import java.util.List;
import java.util.function.Supplier;

public class ModConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, ParanoiaMod.MOD_ID);

    // Worldgen for Deepslate lithium ore
    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_LITHIUM_ORES =
            Suppliers.memoize(() -> List.of(
                    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                            ModBlocks.DEEPSLATE_LITHIUM_ORE.get().defaultBlockState())
            ));

    public static final RegistryObject<ConfiguredFeature<?, ?>> DEEPSLATE_LITHIUM_ORE = CONFIGURED_FEATURES.register("deepslate_lithium_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_LITHIUM_ORES.get(), 7)));

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
}

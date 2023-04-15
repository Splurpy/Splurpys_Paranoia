package net.splurpy.paranoiamod.block;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.splurpy.paranoiamod.ParanoiaMod;
import net.splurpy.paranoiamod.block.custom.MortarAndPestleBlock;
import net.splurpy.paranoiamod.block.torch.LithiumTorchBlock;
import net.splurpy.paranoiamod.block.torch.LithiumWallTorchBlock;
import net.splurpy.paranoiamod.item.ModCreativeModeTab;
import net.splurpy.paranoiamod.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ParanoiaMod.MOD_ID);

    public static final RegistryObject<Block> DEEPSLATE_LITHIUM_ORE = registerBlock("deepslate_lithium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE)
                    .strength(4.5f)
                    .explosionResistance(6),
                    UniformInt.of(0,5)), ModCreativeModeTab.TAB_PARANOIA);

    public static final RegistryObject<Block> MORTAR_AND_PESTLE =
            registerBlock("mortar_and_pestle",
                    () -> new MortarAndPestleBlock(BlockBehaviour.Properties.of(Material.DECORATION)
                            .strength(3f)
                            .sound(SoundType.WOOD).noOcclusion()), ModCreativeModeTab.TAB_PARANOIA);

    public static final RegistryObject<LithiumTorchBlock> LITHIUM_TORCH =
            registerBlockNoItem("lithium_torch",
                    () -> new LithiumTorchBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((light) -> {
                        return 15;
                    }).sound(SoundType.BONE_BLOCK), ParticleTypes.FLAME));

    public static final RegistryObject<LithiumWallTorchBlock> LITHIUM_WALL_TORCH =
            registerBlockNoItem("lithium_wall_torch",
                    () -> new LithiumWallTorchBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((light) -> {
                        return 15;
                    }).sound(SoundType.BONE_BLOCK).dropsLike(LITHIUM_TORCH.get()), ParticleTypes.FLAME));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> result = BLOCKS.register(name, block);
        registerBlockItem(name, result, tab);

        return result;
    }

    private static <T extends Block> RegistryObject<T> registerBlockNoItem(String name, Supplier<T> block) {
        RegistryObject<T> result = BLOCKS.register(name, block);

        return result;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

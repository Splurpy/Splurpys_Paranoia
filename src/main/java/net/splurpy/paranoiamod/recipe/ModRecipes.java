package net.splurpy.paranoiamod.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.splurpy.paranoiamod.ParanoiaMod;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ParanoiaMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<MortarAndPestleRecipe>> MORTAR_AND_PESTLE_SERIALIZER =
            SERIALIZERS.register("mortar_and_pestle", () -> MortarAndPestleRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}

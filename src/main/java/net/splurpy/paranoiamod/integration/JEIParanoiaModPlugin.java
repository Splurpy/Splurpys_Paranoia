package net.splurpy.paranoiamod.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.splurpy.paranoiamod.ParanoiaMod;
import net.splurpy.paranoiamod.recipe.MortarAndPestleRecipe;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIParanoiaModPlugin implements IModPlugin {
    public static RecipeType<MortarAndPestleRecipe> MAP_TYPE =
            new RecipeType<>(MortarAndPestleRecipeCategory.UID, MortarAndPestleRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ParanoiaMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                MortarAndPestleRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<MortarAndPestleRecipe> recipesInfusing = rm.getAllRecipesFor(MortarAndPestleRecipe.Type.INSTANCE);
        registration.addRecipes(MAP_TYPE, recipesInfusing);
    }
}

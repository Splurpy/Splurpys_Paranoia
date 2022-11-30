package net.splurpy.paranoiamod.integration;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.splurpy.paranoiamod.ParanoiaMod;
import net.splurpy.paranoiamod.block.ModBlocks;
import net.splurpy.paranoiamod.recipe.MortarAndPestleRecipe;

public class MortarAndPestleRecipeCategory implements IRecipeCategory<MortarAndPestleRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(ParanoiaMod.MOD_ID, "mortar_and_pestle");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(ParanoiaMod.MOD_ID, "textures/gui/mortar_and_pestle_gui.png");

    private final IDrawable background;
    private final IDrawable icon;


    public MortarAndPestleRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MORTAR_AND_PESTLE.get()));
    }


    @Override
    public RecipeType<MortarAndPestleRecipe> getRecipeType() {
        return JEIParanoiaModPlugin.MAP_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Mortar and Pestle");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MortarAndPestleRecipe recipe, IFocusGroup focuses) {
        // INPUT SLOT
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 33).addIngredients(recipe.getIngredients().get(0));

        // OUTPUT SLOT
        builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 33).addItemStack(recipe.getResultItem());
    }
}

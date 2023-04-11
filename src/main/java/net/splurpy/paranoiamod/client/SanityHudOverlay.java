package net.splurpy.paranoiamod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.splurpy.paranoiamod.ParanoiaMod;

public class SanityHudOverlay {
    private static final ResourceLocation SANE = new ResourceLocation(ParanoiaMod.MOD_ID,
            "textures/sanity/status/status_sane.png");
    private static final ResourceLocation UNSETTLED = new ResourceLocation(ParanoiaMod.MOD_ID,
            "textures/sanity/status/status_unsettled.png");
    private static final ResourceLocation ANXIOUS = new ResourceLocation(ParanoiaMod.MOD_ID,
            "textures/sanity/status/status_anxious.png");
    private static final ResourceLocation PARANOID = new ResourceLocation(ParanoiaMod.MOD_ID,
            "textures/sanity/status/status_paranoid.png");
    private static final ResourceLocation INSANE = new ResourceLocation(ParanoiaMod.MOD_ID,
            "textures/sanity/status/status_insane.png");

    public static final IGuiOverlay HUD_SANITY = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        int x = (screenWidth / 2) - 8;
        int y = screenHeight - 50;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        if (!gui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()) {
            if (ClientSanityData.getPlayerSanity() < 20) {
                renderSanityHud(poseStack, x, y, INSANE);
            }
            else if (ClientSanityData.getPlayerSanity() < 40) {
                renderSanityHud(poseStack, x, y, PARANOID);
            }
            else if (ClientSanityData.getPlayerSanity() < 60) {
                renderSanityHud(poseStack, x, y, ANXIOUS);
            }
            else if (ClientSanityData.getPlayerSanity() < 80) {
                renderSanityHud(poseStack, x, y, UNSETTLED);
            }
            else {
                renderSanityHud(poseStack, x, y, SANE);
            }
        }


    });

    private static void renderSanityHud(PoseStack poseStack, int x, int y, ResourceLocation resourceLocation) {
        // System.out.println("[DEBUG] Accessed renderSanityHud");
        RenderSystem.setShaderTexture(0, resourceLocation);
        GuiComponent.blit(poseStack, x, y,
                0, 0, 16, 16, 16, 16);
        // System.out.println("[DEBUG] Accessed GuiComponent.blit");
    }
}

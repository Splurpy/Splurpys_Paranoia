package net.splurpy.paranoiamod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.splurpy.paranoiamod.ParanoiaMod;

public class InsaneScreenOverlay {
    private static final ResourceLocation INTENSE_OVERLAY = new ResourceLocation(ParanoiaMod.MOD_ID,
            "textures/sanity/overlay/insane_overlay_intense.png");

    private static final ResourceLocation INTENSER_OVERLAY = new ResourceLocation(ParanoiaMod.MOD_ID,
            "textures/sanity/overlay/insane_overlay_intenser.png");

    private static final ResourceLocation BLACKOUT = new ResourceLocation(ParanoiaMod.MOD_ID,
            "textures/sanity/overlay/insane_overlay_blackout.png");

    static int tickCount = 0;

    public static final IGuiOverlay INSANE_OVERLAY = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth;
        int y = screenHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        // RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        if (!gui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()) {
            if (ClientSanityData.getPlayerSanity() < 20) {
                ResourceLocation overlayResource = INTENSER_OVERLAY;
                if (tickCount >= 0 && tickCount < 21) {
                    // System.out.println("[DEBUG] In If");
                    overlayResource = INTENSE_OVERLAY;
                    getAlpha(tickCount);
                }
                render(poseStack, x, y, overlayResource);
                // System.out.println("[DEBUG] Insane tick count: " + tickCount);
                tickCount = tickCount + 1;
            }
            else tickCount = 0;
            if (ClientSanityData.getPlayerSanity() < 40) {
                setAlpha(0.6f);
                render(poseStack, x, y, INTENSE_OVERLAY);
            }
            else if (ClientSanityData.getPlayerSanity() < 60) {
                setAlpha(0.3f);
                render(poseStack, x, y, INTENSE_OVERLAY);
            }
        }


    });

    private static void render(PoseStack poseStack, int x, int y, ResourceLocation resourceLocation) {
        // System.out.println("[DEBUG] Accessed render");
        RenderSystem.setShaderTexture(0, resourceLocation);
        GuiComponent.blit(poseStack, 0, 0,
                0, 0, 3840, 2160, x, y);
        // System.out.println("[DEBUG] Accessed GuiComponent.blit");
    }

    private static void setAlpha(float alpha)
    {
        // System.out.println("[DEBUG] setAlpha called");
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
    }

    private static void getAlpha(int tickCount)
    {
        // System.out.println("[DEBUG] getAlpha called");
        switch (tickCount)
        {
            case 0:
                // setAlpha(0.0f);
                break;
            case 1:
                // setAlpha(0.05f);
                break;
            case 2:
                // setAlpha(0.1f);
                break;
            case 3:
                // setAlpha(0.15f);
                break;
            case 4:
                // setAlpha(0.2f);
                break;
            case 5:
                // setAlpha(0.25f);
                break;
            case 6:
                // setAlpha(0.3f);
                break;
            case 7:
                // setAlpha(0.35f);
                break;
            case 8:
                // setAlpha(0.4f);
                break;
            case 9:
                // setAlpha(0.45f);
                break;
            case 10:
                setAlpha(0.5f);
                break;
            case 11:
                setAlpha(0.55f);
                break;
            case 12:
                setAlpha(0.6f);
                break;
            case 13:
                setAlpha(0.65f);
                break;
            case 14:
                setAlpha(0.7f);
                break;
            case 15:
                setAlpha(0.75f);
                break;
            case 16:
                setAlpha(0.8f);
                break;
            case 17:
                setAlpha(0.85f);
                break;
            case 18:
                setAlpha(0.9f);
                break;
            case 19:
                setAlpha(0.95f);
                break;
            case 20:
                setAlpha(1.0f);
        }
    }
}

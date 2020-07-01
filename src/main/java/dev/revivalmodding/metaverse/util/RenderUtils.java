package dev.revivalmodding.metaverse.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderUtils {

    public static void renderColor(int x1, int y1, int x2, int y2, float r, float g, float b, float a) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        resetColorState();
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        RenderSystem.defaultBlendFunc();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x1, y2, 0).color(r, g, b, a).endVertex();
        builder.pos(x2, y2, 0).color(r, g, b, a).endVertex();
        builder.pos(x2, y1, 0).color(r, g, b, a).endVertex();
        builder.pos(x1, y1, 0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture();
    }

    public static void renderColoredTexture(int x1, int y1, int x2, int y2, float r, float g, float b, float a, ResourceLocation location) {
        renderColoredTexture(x1, y1, x2, y2, 0.0F, 0.0F, 1.0F, 1.0F, r, g, b, a, location);
    }

    public static void renderColoredTexture(int x1, int y1, int x2, int y2, float texStartU, float texStartV, float texEndU, float texEndV, float r, float g, float b, float a, ResourceLocation location) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        resetColorState();
        GlStateManager.enableBlend();
        RenderSystem.defaultBlendFunc();
        Minecraft.getInstance().getTextureManager().bindTexture(location);
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
        builder.pos(x1, y2, 0).color(r, g, b, a).tex(texStartU, texEndV).endVertex();
        builder.pos(x2, y2, 0).color(r, g, b, a).tex(texEndU, texEndV).endVertex();
        builder.pos(x2, y1, 0).color(r, g, b, a).tex(texEndU, texStartV).endVertex();
        builder.pos(x1, y1, 0).color(r, g, b, a).tex(texStartU, texStartV).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
    }

    public static void renderTexture(int x1, int y1, int x2, int y2, ResourceLocation texture) {
        renderTexture(x1, y1, x2, y2, 0.0F, 0.0F, 1.0F, 1.0F, texture);
    }

    public static void renderTexture(int x1, int y1, int x2, int y2, float texStartU, float texStartV, float texEndU, float texEndV, ResourceLocation texture) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        resetColorState();
        GlStateManager.enableBlend();
        RenderSystem.defaultBlendFunc();
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x1, y2, 0).tex(texStartU, texEndV).endVertex();
        builder.pos(x2, y2, 0).tex(texEndU, texEndV).endVertex();
        builder.pos(x2, y1, 0).tex(texEndU, texStartV).endVertex();
        builder.pos(x1, y1, 0).tex(texStartU, texStartV).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
    }

    public static void resetColorState() {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}

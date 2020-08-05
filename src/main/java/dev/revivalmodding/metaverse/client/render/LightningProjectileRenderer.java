package dev.revivalmodding.metaverse.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.revivalmodding.metaverse.common.entity.LightningProjectile;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class LightningProjectileRenderer extends EntityRenderer<LightningProjectile> {

    public LightningProjectileRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getEntityTexture(LightningProjectile entity) {
        return null;
    }

    @Override
    public void render(LightningProjectile entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        List<Vec3d> points = entityIn.getTickedPositions();
        Vec3d connectedTo = null;
        IVertexBuilder builder = bufferIn.getBuffer(RenderType.LINES);
        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        GlStateManager.disableTexture();
        GlStateManager.lineWidth(10);
        GlStateManager.enableBlend();
        RenderSystem.defaultBlendFunc();
        for(int i = 0; i < points.size(); i++) {
            Vec3d current = points.get(i);
            if(connectedTo != null) {
                float relativeX1 = interpolate(current.x - entityIn.getPosX(), current.x - entityIn.lastTickPosX, partialTicks);
                float relativeY1 = interpolate(current.y - entityIn.getPosY(), current.y - entityIn.lastTickPosY, partialTicks);
                float relativeZ1 = interpolate(current.z - entityIn.getPosZ(), current.z - entityIn.lastTickPosZ, partialTicks);
                float relativeX2 = interpolate(connectedTo.x - entityIn.getPosX(), connectedTo.x - entityIn.lastTickPosX, partialTicks);
                float relativeY2 = interpolate(connectedTo.y - entityIn.getPosY(), connectedTo.y - entityIn.lastTickPosY, partialTicks);
                float relativeZ2 = interpolate(connectedTo.z - entityIn.getPosZ(), connectedTo.z - entityIn.lastTickPosZ, partialTicks);
                bufferBuilder.pos(matrix4f, relativeX2, relativeY2, relativeZ2).color(0.8F, 1.0F, 1.0F, 0.6F).endVertex();
                bufferBuilder.pos(matrix4f, relativeX1, relativeY1, relativeZ1).color(0.8F, 1.0F, 1.0F, 0.6F).endVertex();
            }
            connectedTo = current;
        }
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture();
    }

    private static float interpolate(double at, double prev, float pct) {
        return (float) (prev + (at - prev) * pct);
    }
}

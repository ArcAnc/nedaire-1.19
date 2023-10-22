/**
 * @author ArcAnc
 * Created at: 2022-09-20
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.extensions.common.IClientItemExtensions.FontContext;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class RenderHelper 
{
    private static final RenderType TRANSLUCENT = RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS);

    public static Minecraft mc()
	{
		return Minecraft.getInstance();
	}
	
	public static ItemRenderer renderItem()
	{
		return mc().getItemRenderer();
	}
	
	public static void renderItemStack(GuiGraphics guiGraphics, ItemStack stack, int x, int y, boolean overlay)
	{
		renderItemStack(guiGraphics, stack, x, y, overlay, null);
	}

	public static void renderItemStack(GuiGraphics guiGraphics, ItemStack stack, int x, int y, boolean overlay, String count)
    {
        if(!stack.isEmpty())
		{
			PoseStack poseStack = guiGraphics.pose();
            // Include the matrix transformation
			PoseStack modelViewStack = RenderSystem.getModelViewStack();
			modelViewStack.pushPose();
			modelViewStack.mulPoseMatrix(poseStack.last().pose());
			RenderSystem.applyModelViewMatrix();

			// Counteract the zlevel increase, because multiplied with the matrix, it goes out of view
			guiGraphics.renderItem(stack, x, y, -50);

			if(overlay)
			{
				// Use the Item's font renderer, if available
				Font font = IClientItemExtensions.of(stack.getItem()).getFont(stack, FontContext.ITEM_COUNT);
				font = font!=null? font: mc().font;
				guiGraphics.renderItemDecorations(font, stack, x, y, count);
			}
			modelViewStack.popPose();
			RenderSystem.applyModelViewMatrix();
		}
	}
	

    public static void renderFakeItemTransparent(ItemStack stack, int x, int y, float alpha)
    {
        renderFakeItemColored(stack, x, y, 1F, 1F, 1F, alpha);
    }

	public static void renderFakeItemColored(ItemStack stack, int x, int y, float red, float green, float blue, float alpha)
    {
        if (stack.isEmpty()) 
        	return; 

        Minecraft mc = mc();
		BakedModel model = renderItem().getModel(stack, null, mc.player, 0);
        renderItemModel(stack, x, y, red, green, blue, alpha, model);
    }

    /**
     * {@link ItemRenderer::renderGuiItem} but with color
     */
    public static void renderItemModel(ItemStack stack, int x, int y, float red, float green, float blue, float alpha, BakedModel model)
    {
        mc().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);

        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.translate(x, y, 100.0F);
        modelViewStack.translate(8.0D, 8.0D, 0.0D);
        modelViewStack.scale(1.0F, -1.0F, 1.0F);
        modelViewStack.scale(16.0F, 16.0F, 16.0F);
        RenderSystem.applyModelViewMatrix();

        boolean flatLight = !model.usesBlockLight();
        if (flatLight)
        {
            Lighting.setupForFlatItems();
        }

        MultiBufferSource.BufferSource buffer = mc().renderBuffers().bufferSource();
        renderItem().render(
                stack,
                ItemDisplayContext.GUI,
                false,
                new PoseStack(),
                wrapBuffer(buffer, red, green, blue, alpha, alpha < 1F),
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                model
        );
        buffer.endBatch();

        RenderSystem.enableDepthTest();

        if (flatLight)
        {
            Lighting.setupFor3DItems();
        }

        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private static MultiBufferSource wrapBuffer(MultiBufferSource buffer, float red, float green, float blue, float alpha, boolean forceTranslucent)
    {
        return renderType -> new TintedVertexConsumer(buffer.getBuffer(forceTranslucent ? TRANSLUCENT : renderType), red, green, blue, alpha);
    }	
    
    public static final class TintedVertexConsumer implements VertexConsumer
    {
        private final VertexConsumer wrapped;
        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;

        public TintedVertexConsumer(VertexConsumer wrapped, float red, float green, float blue, float alpha)
        {
            this.wrapped = wrapped;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public TintedVertexConsumer(VertexConsumer wrapped, int red, int green, int blue, int alpha)
        {
            this(wrapped, red / 255F, green / 255F, blue / 255F, alpha / 255F);
        }

        @Override
        public @NotNull VertexConsumer vertex(double x, double y, double z)
        { 
        	return wrapped.vertex(x, y, z); 
        }

        @Override
        public @NotNull VertexConsumer color(int red, int green, int blue, int alpha)
        {
            return wrapped.color(
                    (int) (red * this.red),
                    (int) (green * this.green),
                    (int) (blue * this.blue),
                    (int) (alpha * this.alpha)
            );
        }

        @Override
        public @NotNull VertexConsumer uv(float u, float v)
        { 
        	return wrapped.uv(u, v); 
        }

        @Override
        public @NotNull VertexConsumer overlayCoords(int u, int v)
        { 
        	return wrapped.overlayCoords(u, v); 
        }

        @Override
        public @NotNull VertexConsumer uv2(int u, int v)
        { 
        	return wrapped.uv2(u, v); 
        }

        @Override
        public @NotNull VertexConsumer normal(float x, float y, float z)
        { 
        	return wrapped.normal(x, y, z); 
        }

        @Override
        public void endVertex() 
        { 
        	wrapped.endVertex(); 
        }

        @Override
        public void defaultColor(int r, int g, int b, int a) 
        { 
        	wrapped.defaultColor(r, g, b, a); 
        }

        @Override
        public void unsetDefaultColor() 
        { 
        	wrapped.unsetDefaultColor(); 
        }
    }
    
    public static int[] splitRGBA(int color)
    {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8)  & 0xFF;
        int b =  color        & 0xFF;
        int a = (color >> 24) & 0xFF;

        return new int[] { r, g, b, a };
    }

    public static void blit(GuiGraphics guiGraphics, ResourceLocation location, float posX, float posY, float posZ, float sizeX, float sizeY, int uStart, int uSize, int vStart, int vSize, int textureSizeX, int textureSizeY)
    {
        blit(guiGraphics, location, posX, posY, posZ, sizeX, sizeY, uStart, uSize, vStart, vSize, textureSizeX, textureSizeY, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void blit(@NotNull GuiGraphics guiGraphics, ResourceLocation location, float posX, float posY, float posZ, float sizeX, float sizeY, int uStart, int uSize, int vStart, int vSize, int textureSizeX, int textureSizeY, float red, float green, float blue, float alpha)
    {
        RenderSystem.setShaderTexture(0, location);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.enableBlend();

        float posXFinish = posX + sizeX;
        float posYFinish = posY + sizeY;

        float uScaledStart = uStart / (float)textureSizeX;
        float uScaledFinish = (uStart + uSize) / (float)textureSizeX;
        float vScaledStart = vStart / (float)textureSizeY;
        float vScaledFinish = (vStart + vSize) / (float)textureSizeY;

        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.vertex(matrix4f, posX,       posY,       posZ).color(red, green, blue, alpha).uv(uScaledStart,  vScaledStart).endVertex();
        bufferbuilder.vertex(matrix4f, posX,       posYFinish, posZ).color(red, green, blue, alpha).uv(uScaledStart,  vScaledFinish).endVertex();
        bufferbuilder.vertex(matrix4f, posXFinish, posYFinish, posZ).color(red, green, blue, alpha).uv(uScaledFinish, vScaledFinish).endVertex();
        bufferbuilder.vertex(matrix4f, posXFinish, posY,       posZ).color(red, green, blue, alpha).uv(uScaledFinish, vScaledStart).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }

}

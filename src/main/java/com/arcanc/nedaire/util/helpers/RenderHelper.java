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
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.extensions.common.IClientItemExtensions.FontContext;

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
	
	public static void renderItemStack(PoseStack transform, ItemStack stack, int x, int y, boolean overlay)
	{
		renderItemStack(transform, stack, x, y, overlay, null);
	}

	public static void renderItemStack(PoseStack transform, ItemStack stack, int x, int y, boolean overlay, String count)
	{
		if(!stack.isEmpty())
		{
			// Include the matrix transformation
			PoseStack modelViewStack = RenderSystem.getModelViewStack();
			modelViewStack.pushPose();
			modelViewStack.mulPoseMatrix(transform.last().pose());
			RenderSystem.applyModelViewMatrix();

			// Counteract the zlevel increase, because multiplied with the matrix, it goes out of view
			ItemRenderer itemRenderer = renderItem();
			transform.translate(0, 0, -50); 
			itemRenderer.renderAndDecorateItem(transform, stack, x, y);
			transform.translate(0, 0, 50);

			if(overlay)
			{
				// Use the Item's font renderer, if available
				Font font = IClientItemExtensions.of(stack.getItem()).getFont(stack, FontContext.ITEM_COUNT);
				font = font!=null? font: mc().font;
				itemRenderer.renderGuiItemDecorations(transform, font, stack, x, y, count);
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
        public VertexConsumer vertex(double x, double y, double z) 
        { 
        	return wrapped.vertex(x, y, z); 
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha)
        {
            return wrapped.color(
                    (int) (red * this.red),
                    (int) (green * this.green),
                    (int) (blue * this.blue),
                    (int) (alpha * this.alpha)
            );
        }

        @Override
        public VertexConsumer uv(float u, float v) 
        { 
        	return wrapped.uv(u, v); 
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) 
        { 
        	return wrapped.overlayCoords(u, v); 
        }

        @Override
        public VertexConsumer uv2(int u, int v) 
        { 
        	return wrapped.uv2(u, v); 
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) 
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
    
}

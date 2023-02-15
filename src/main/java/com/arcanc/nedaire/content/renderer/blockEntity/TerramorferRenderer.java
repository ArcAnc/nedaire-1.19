/**
 * @author ArcAnc	
 * Created at: 2023-02-05
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.arcanc.nedaire.content.block.entities.NBETerramorfer;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class TerramorferRenderer implements BlockEntityRenderer<NBETerramorfer>   
{
	public static final ResourceLocation VORTEX_TEXURE = StringHelper.getLocFStr("misc/vortex");
	
	public TerramorferRenderer(BlockEntityRendererProvider.Context ctx) 
	{
	}
	
	@Override
	public void render(NBETerramorfer blockEntity, float partialTicks, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) 
	{
		if (blockEntity != null)
		{
			Minecraft mc = RenderHelper.mc();
			Camera cam = mc.getBlockEntityRenderDispatcher().camera;

			TextureAtlasSprite tex = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(VORTEX_TEXURE);
			
			mStack.pushPose();
			
		    RenderSystem.enableBlend();
		    RenderSystem.defaultBlendFunc();
		    RenderSystem.disableDepthTest();
				
		    mStack.translate(0.5f, 0.65f, 0.5f);
			
		    mStack.mulPose(cam.rotation());
			mStack.mulPose(Axis.ZP.rotationDegrees((float)blockEntity.getLevel().getGameTime() % 360));
			
			VertexConsumer builder = buffer.getBuffer(Sheets.translucentCullBlockSheet());
			
			Matrix4f matrix = mStack.last().pose();
			Matrix3f normal = mStack.last().normal();
			
			builder.vertex(matrix, 0.3f - 0.5f, 0.7f - 0.5f, 0).color(255, 255, 255, 255).uv(tex.getU0(), tex.getV0()).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0, 0, -1).endVertex();
			builder.vertex(matrix, 0.7f - 0.5f, 0.7f - 0.5f, 0).color(255, 255, 255, 255).uv(tex.getU1(), tex.getV0()).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0, 0, -1).endVertex();
			builder.vertex(matrix, 0.7f - 0.5f, 0.3f - 0.5f, 0).color(255, 255, 255, 255).uv(tex.getU1(), tex.getV1()).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0, 0, -1).endVertex();
			builder.vertex(matrix, 0.3f - 0.5f, 0.3f - 0.5f, 0).color(255, 255, 255, 255).uv(tex.getU0(), tex.getV1()).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0, 0, -1).endVertex();
			RenderSystem.enableDepthTest();
			RenderSystem.disableBlend();
			
			mStack.popPose();
		}
	}

}

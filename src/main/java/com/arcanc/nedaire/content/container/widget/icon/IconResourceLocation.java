/**
 * @author ArcAnc
 * Created at: 2023-01-25
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget.icon;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class IconResourceLocation implements Icon<ResourceLocation> 
{
	private final ResourceLocation loc;
	private int blitOffset = 0, texX = 0, texY = 0, texW = 1, texH = 1;
	private int textureSizeX = 256, textureSizeY = 256;
	
	
	public IconResourceLocation(ResourceLocation texture) 
	{
		this(texture, 0, 0, 0, 1, 1, 256, 256);
	}
	
	public IconResourceLocation(ResourceLocation texture, int blitOffset, int texX, int texY, int texW, int texH, int textureSizeX, int textureSizeY) 
	{
		this.loc = texture;
		this.blitOffset = blitOffset;
		this.texX = texX;
		this.texY = texY;
		this.texW = texW;
		this.texH = texH;
		this.textureSizeX = textureSizeX;
		this.textureSizeY = textureSizeY;
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int x, int y, int width, int height)
	{

		PoseStack poseStack = guiGraphics.pose();

	    poseStack.pushPose();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
	    RenderSystem.setShaderTexture(0, loc);
	    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1f);
	    RenderSystem.enableBlend();
	    RenderSystem.defaultBlendFunc();
	    RenderSystem.enableDepthTest();	    
	    
		blit(poseStack.last().pose(), x, x + width, y, y + height, blitOffset, texX / (float)textureSizeX, (texX + texW) / (float)textureSizeX, texY / (float)textureSizeY, (texY + texH) / (float)textureSizeY );
	    
		RenderSystem.disableBlend();
	    poseStack.popPose();
	}
	
	private void blit(Matrix4f matrix, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1)
	{
	      RenderSystem.setShader(GameRenderer::getPositionTexShader);
	      BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
	      bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
	      bufferbuilder.vertex(matrix, x0, y1, z).uv(u0, v1).endVertex();
	      bufferbuilder.vertex(matrix, x1, y1, z).uv(u1, v1).endVertex();
	      bufferbuilder.vertex(matrix, x1, y0, z).uv(u1, v0).endVertex();
	      bufferbuilder.vertex(matrix, x0, y0, z).uv(u0, v0).endVertex();
	      BufferUploader.drawWithShader(bufferbuilder.end());
	}

}

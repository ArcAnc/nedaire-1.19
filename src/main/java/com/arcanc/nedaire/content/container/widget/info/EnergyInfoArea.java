/**
 * @author ArcAnc
 * Created at: 2023-01-18
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget.info;

import java.util.function.Supplier;

import org.joml.Matrix4f;

import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnergyInfoArea extends InfoArea
{

	public static final ResourceLocation TEXTURE = NDatabase.GUI.getTexturePath(NDatabase.GUI.Elements.Vim.PATH);
	
	protected IVim energy;
	protected Supplier<Tooltip> tooltip;
	public EnergyInfoArea(Rect2i area, IVim energy) 
	{
		super(area);
		this.energy = energy;
		this.tooltip = () -> Tooltip.create(Component.translatable(NDatabase.Capabilities.Vim.Lang.DESCRIPTION_MAIN, 
				Component.literal(Integer.toString(energy.getEnergyStored())).withStyle(ChatFormatting.BLUE),
				Component.literal(Integer.toString(energy.getMaxEnergyStored())).withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float p_93660_) 
	{
		if (visible)
		{
	         this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
	         this.renderButton(stack, mouseX, mouseY, p_93660_);
	         this.renderTooltip();
		}
	}
	
	@Override
	public void renderButton(PoseStack stack, int mouseX, int mouseY, float p_93679_) 
	{
		stack.pushPose();
		
		Minecraft mc = RenderHelper.mc();
		Screen screen = mc.screen;
		if (screen != null)
		{
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

			RenderSystem.setShaderTexture(0, TEXTURE);

			blit(stack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 0, 14, 42, 32, 64);
			
			float energyPercent = (float)energy.getEnergyStored() / energy.getMaxEnergyStored();
			
			float f = this.getY() + (this.getHeight() * (1 - energyPercent));
			float f1 = 42 * (1 - energyPercent);
			
			localBlit(stack.last().pose(), this.getX(), this.getX() + this.getWidth(), f, this.getY() + this.getHeight(), screen.getBlitOffset(), 17f / 32f, (17f + 14f) / 32f, f1/64f, 42f/64f);
		}
		
		stack.popPose();
	}
	
	private void localBlit(Matrix4f matrix, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1)
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

	protected void renderTooltip() 
	{
		if (isHoveredOrFocused())
		{
			Minecraft mc = RenderHelper.mc();
			Screen screen = mc.screen;
	        if (screen != null) 
	        {
	        	screen.setTooltipForNextRenderPass(tooltip.get(), this.createTooltipPositioner(), this.isFocused());
		    }
	    }
	}

}

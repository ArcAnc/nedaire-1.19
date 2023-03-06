/**
 * @author ArcAnc
 * Created at: 2023-03-06
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget.progressBar;

import java.util.function.Supplier;

import org.joml.Matrix4f;

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

public class ArrowBrogressBar extends ProgressBar 
{
	public static final ResourceLocation TEXTURE = NDatabase.GUI.getTexturePath(NDatabase.GUI.Elements.ProgressBar.ARROW_RIGHT);
	
	protected Supplier<Double> current;
	protected Supplier<Double> max;
	protected Supplier<Tooltip> tooltip;
	
	/**
	 * @param percent - 0 -- 1
	 */
	public ArrowBrogressBar(Rect2i area, Supplier<Double> current, Supplier<Double> max) 
	{
		super(area);

		this.current = current;
		this.max = max;
		this.tooltip = () -> Screen.hasShiftDown() ? 
				Tooltip.create(Component.translatable(NDatabase.GUI.Elements.ProgressBar.Description.PERCENT_FULL, 
				Component.literal(Double.toString(current.get())).withStyle(ChatFormatting.BLUE),
				Component.literal(Double.toString(max.get())).withStyle(ChatFormatting.BLUE))
				.withStyle(ChatFormatting.GRAY))
				: 
				Tooltip.create(Component.translatable(NDatabase.GUI.Elements.ProgressBar.Description.PERCENT, 
				Component.literal(Integer.toString((int)((current.get()/max.get()) * 100))).withStyle(ChatFormatting.BLUE))
				.withStyle(ChatFormatting.GRAY).append(Component.literal("%")));
	}
	
	@Override
	public void render(PoseStack mStack, int mouseX, int mouseY, float partialTicks) 
	{
		if (visible)
		{
	         this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
	         this.renderButton(mStack, mouseX, mouseY, partialTicks);
	         this.renderTooltip();
		}
	}

	@Override
	public void renderButton(PoseStack mStack, int mouseX, int mouseY, float partialTicks) 
	{
		mStack.pushPose();
		
		Minecraft mc = RenderHelper.mc();
		Screen screen = mc.screen;
		
		if (screen != null)
		{
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

			RenderSystem.setShaderTexture(0, TEXTURE);

			blit(mStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0, 0, 22, 15, 32, 32);
			
			if (current.get() > 0 && max.get() > 0)
			{
				float percent = (float)(current.get() / max.get());
				
				float recalc = 22 * percent;
				
				localBlit(mStack.last().pose(), this.getX(), this.getX() + recalc, this.getY(), this.getY() + this.getHeight(), screen.getBlitOffset(), 0f / 32f, (0f + recalc) / 32f, 16/32f, 32f/32f);
			}
		}
		
		mStack.popPose();
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

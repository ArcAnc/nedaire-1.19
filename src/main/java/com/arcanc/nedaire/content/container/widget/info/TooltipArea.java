/**
 * @author ArcAnc
 * Created at: 2023-01-15
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget.info;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

public class TooltipArea extends InfoArea 
{

	@Nullable
	protected Supplier<Tooltip> tooltip;
	
	public TooltipArea(Rect2i area, @NotNull Tooltip text)
	{
		this (area, () -> text);
	}

	public TooltipArea(Rect2i area, @NotNull  Supplier<Tooltip> text)
	{
		super(area);
		this.tooltip = text;
	}

	
	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		if (visible)
		{
	         this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
	         this.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
	         this.renderTooltip();
		}
	}
	
	@Override
	public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
	}
	
	protected void renderTooltip() 
	{
		if (this.tooltip != null) 
	    {
			if (isHovered())
			{
				Minecraft mc = RenderHelper.mc();
				Screen screen = mc.screen;
	         	if (screen != null) 
	         	{
	         		screen.setTooltipForNextRenderPass(this.tooltip.get(), this.createTooltipPositioner(), this.isFocused());
		        }
			}
	    }
	}
}

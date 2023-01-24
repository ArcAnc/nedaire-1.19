/**
 * @author ArcAnc
 * Created at: 2023-01-23
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget;

import java.util.function.Supplier;

import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class Label extends AbstractWidget 
{
	private Supplier<Component> text;

	public Label(int x, int y, int width, int height, Supplier<Component> text) 
	{
		super(x, y, width, height, Component.empty());
		this.text = text;
	}

	@Override
	public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
		stack.pushPose();
		
		Minecraft mc = RenderHelper.mc();
		Font font = mc.font;

		drawCenteredString(stack, font, text.get(), this.getX(), this.getY(), 16777215);
		
		stack.popPose();
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
		if (this.visible) 
		{
			this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
			this.renderButton(stack, mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	protected void updateWidgetNarration(NarrationElementOutput newText) 
	{

	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int buttonId) 
	{
		return false;
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int buttonId) 
	{
		return false;
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int buttonId, double p_93648_, double p_93649_) 
	{
		return false;
	}

}

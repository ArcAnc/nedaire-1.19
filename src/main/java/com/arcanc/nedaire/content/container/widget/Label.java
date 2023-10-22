/**
 * @author ArcAnc
 * Created at: 2023-01-23
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget;

import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class Label extends AbstractWidget 
{
	private final Supplier<Component> text;

	public Label(int x, int y, int width, int height, Supplier<Component> text) 
	{
		super(x, y, width, height, Component.empty());
		this.text = text;
	}

	@Override
	public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		PoseStack poseStack = guiGraphics.pose();

		poseStack.pushPose();
		
		Minecraft mc = RenderHelper.mc();
		Font font = mc.font;

		guiGraphics.drawString(font, text.get(), this.getX() - font.width(text.get()) / 2, this.getY(), 16777215, false);
		
		poseStack.popPose();
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		if (this.visible) 
		{
			this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
			this.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput newText)
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

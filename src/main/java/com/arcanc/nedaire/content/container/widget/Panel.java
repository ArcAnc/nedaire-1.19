/**
 * @author ArcAnc
 * Created at: 2023-01-12
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget;

import java.util.List;

import net.minecraft.client.gui.GuiGraphics;
import org.apache.commons.compress.utils.Lists;

import com.arcanc.nedaire.content.container.screen.NContainerScreen;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class Panel extends AbstractButton  
{

	protected List<AbstractWidget> objects = Lists.newArrayList();
	public int index = 0;
	
	public Panel(int index, int x, int y, int width, int height) 
	{
		this(index, x, y, width, height, Component.empty(), Lists.newArrayList());
	}
	
	public Panel(int index, int x, int y, int width, int height, @NotNull Component label)
	{
		this(index, x, y, width, height, label, Lists.newArrayList());
	}
	
	public Panel(int index, int x, int y, int width, int height, @NotNull Component label, @NotNull List<AbstractWidget> widgets)
	{
		super(x, y, width, height, label);
		Preconditions.checkNotNull(widgets);
		this.objects = widgets;
		this.index = index;
	}

	public @NotNull Panel addWidget(@NotNull AbstractWidget widget)
	{
		objects.add(widget);
		return this;
	}
	
	public @NotNull List<AbstractWidget> getObjects()
	{
		return ImmutableList.copyOf(objects);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int buttonId) 
	{
		return false;
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		if (visible)
			for(AbstractWidget w : this.objects)
				w.visible = true;
		else
			for(AbstractWidget w : this.objects)
				w.visible = false;
	}
	
	@Override
	public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		PoseStack poseStack = guiGraphics.pose();

		poseStack.pushPose();
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

		guiGraphics.blit(NContainerScreen.LEFT_TOP, getX(), getY(), 0, 0, 0, 8, 8, 8, 8);

		guiGraphics.blit(NContainerScreen.MIDDLE_TOP, getX() + 8, getY(), 0, 0, 0, getWidth() - 16, 8, 8, 8);
		
		guiGraphics.blit(NContainerScreen.RIGHT_TOP, getX() + getWidth() - 8, getY(), 0, 0, 0, 8, 8, 8, 8);
		
		guiGraphics.blit(NContainerScreen.MIDDLE_LEFT, getX(), getY() + 8, 0, 0, 0, 8, getHeight() - 16, 8, 8);
		
		guiGraphics.blit(NContainerScreen.LEFT_BOT, getX(), getY() + getHeight() - 8, 0, 0, 0, 8, 8, 8, 8);

		guiGraphics.blit(NContainerScreen.MIDDLE_BOT, getX() + 8, getY() + getHeight() - 8, 0, 0, 0, getWidth() - 16, 8, 8, 8);

		guiGraphics.blit(NContainerScreen.RIGHT_BOT, getX() + getWidth() - 8, getY() + getHeight() - 8, 0, 0, 0, 8, 8, 8, 8);

		guiGraphics.blit(NContainerScreen.MIDDLE_RIGHT, getX() + getWidth() - 8, getY() + 8, 0, 0, 0, 8, getHeight() - 16, 8, 8);

		guiGraphics.blit(NContainerScreen.MIDDLE, getX() + 8, getY() + 8, 0, 0, 0, getWidth() - 16, getHeight() - 16, 8, 8);

		poseStack.popPose();
	}
	
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput p_259858_)
	{
		
	}

	@Override
	public void onPress() 
	{
		
	}

}

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

import org.apache.commons.compress.utils.Lists;

import com.arcanc.nedaire.content.container.screen.NContainerScreen;
import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class Panel extends AbstractButton  
{

	protected List<AbstractWidget> objects = Lists.newArrayList();
	public int index = 0;
	
	public Panel(int index, int x, int y, int width, int height) 
	{
		this(index, x, y, width, height, Component.empty(), Lists.newArrayList());
	}
	
	public Panel(int index, int x, int y, int width, int height, Component label) 
	{
		this(index, x, y, width, height, label, Lists.newArrayList());
	}
	
	public Panel(int index, int x, int y, int width, int height, Component label, List<AbstractWidget> widgets) 
	{
		super(x, y, width, height, label);
		Preconditions.checkNotNull(widgets);
		this.objects = widgets;
		this.index = index;
	}

	public Panel addWidget(AbstractWidget widget)
	{
		Preconditions.checkNotNull(widget);
		objects.add(widget);
		return this;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int buttonId) 
	{
		return false;
	}

	
	@Override
	public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
		stack.pushPose();
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		RenderSystem.setShaderTexture(0, NContainerScreen.LEFT_TOP);
		blit(stack, getX(), getY(), this.getBlitOffset(), 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_TOP);
		blit(stack, getX() + 8, getY(), this.getBlitOffset(), 0, 0, getWidth() - 8, 8, 8, 8);
		
		RenderSystem.setShaderTexture(0, NContainerScreen.RIGHT_TOP);
		blit(stack, getX() + getWidth(),  getY(), this.getBlitOffset(), 0, 0, 8, 8, 8, 8);
		
		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_LEFT);
		blit(stack, getX(), getY() + 8, this.getBlitOffset(), 0, 0, 8, getHeight() - 8, 8, 8);
		
		RenderSystem.setShaderTexture(0, NContainerScreen.LEFT_BOT);
		blit(stack, getX(), getY() + getHeight(), this.getBlitOffset(), 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_BOT);
		blit(stack, getX() + 8, getY() + getHeight(), this.getBlitOffset(), 0, 0, getWidth() - 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.RIGHT_BOT);
		blit(stack, getX() + getWidth(), getY() + getHeight(), this.getBlitOffset(), 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_RIGHT);
		blit(stack, getX() + getWidth(), getY() + 8, this.getBlitOffset(), 0, 0, 8, getHeight() - 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE);
		blit(stack, getX() + 8, getY() + 8, this.getBlitOffset(), 0, 0, getWidth() - 8, getHeight() - 8, 8, 8);

		stack.popPose();
	}
	
	@Override
	protected void updateWidgetNarration(NarrationElementOutput p_259858_) 
	{
		
	}

	@Override
	public void onPress() 
	{
		
	}

}

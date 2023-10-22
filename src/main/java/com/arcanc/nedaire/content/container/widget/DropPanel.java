/**
 * @author ArcAnc
 * Created at: 2023-01-18
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget;

import com.arcanc.nedaire.content.container.screen.NContainerScreen;
import com.arcanc.nedaire.content.container.widget.icon.Icon;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class DropPanel extends AbstractWidget 
{

	/**
	 * FIXME: finish drop panel code. Add here button press, coloring and description in closed state
	 */
	private final Vec2 changeSize;
	
	private final Vec2 openSize;
	private final Vec2 closedSize = new Vec2(24, 24);
	private State state;
	private final Side side;
	private final Color color;
	
	private final List<AbstractWidget> widgets = Lists.newArrayList();
	private final Supplier<Icon<?>> icon;
	private final Supplier<Tooltip> tooltip;
	
	public DropPanel(int width, int height, Side side, boolean isOpen, Color color, Supplier<Icon<?>> icon, Supplier<Tooltip> closeTooltip)
	{
		super(0, 0, isOpen ? width : 24, isOpen ? height: 24, Component.empty());
		this.openSize = new Vec2(width, height);
		state = isOpen ? State.OPEN : State.CLOSED;
		this.side = side;
		Vec2 temp = openSize.add(closedSize.negated()).scale(1f/8f);
		
		changeSize = new Vec2(temp.x < 1 ? 1 : temp.x, temp.y < 1 ? 1 : temp.y);
		
		this.color = color;
		this.icon = icon;
		this.tooltip = closeTooltip;
	}

	public DropPanel addWidget (AbstractWidget widget)
	{
		Preconditions.checkNotNull(widget);
		widgets.add(widget);
		return this;
	}
	
	public List<AbstractWidget> getWidgets() 
	{
		return ImmutableList.copyOf(widgets);
	}
	
	@Override
	public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		if (state == State.OPENING)
		{
			this.width = (int) (width + changeSize.x);
			this.height = (int) (height + changeSize.y);
			if (this.width > openSize.x)
			{
				this.width = (int)openSize.x;
			}
			if (this.height > openSize.y)
			{
				this.height = (int)openSize.y;
			}
			if (width == openSize.x && height == openSize.y)
			{
				state = State.OPEN;
			}
		}
		else if (state == State.CLOSING)
		{
			this.width = (int)(width - changeSize.x);
			this.height = (int)(height - changeSize.y);
			if (this.width < closedSize.x)
			{
				this.width = (int)closedSize.x;
			}
			if (this.height < closedSize.y)
			{
				this.height = (int)closedSize.y;
			}
			if (width == closedSize.x && height == closedSize.y)
			{
				state = State.CLOSED;
			}		
		} 
		
		if (state == State.OPEN)
		{
			for (AbstractWidget widget : widgets)
				widget.visible = true;
		}
		else
		{
			for (AbstractWidget widget : widgets)
				widget.visible = false;
		}

		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		
//		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
//		RenderSystem.setShaderColor(this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f);

		RenderHelper.blit(guiGraphics, NContainerScreen.LEFT_TOP, getX(), getY(), -10, 8 ,8, 0, 8, 0,8, 8, 8,this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f );

		RenderHelper.blit(guiGraphics, NContainerScreen.MIDDLE_TOP, getX() + 8, getY(), -10, getWidth() - 16, 8, 0, 8, 0, 8, 8, 8, this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f );

		RenderHelper.blit(guiGraphics, NContainerScreen.RIGHT_TOP, getX() + getWidth() - 8, getY(), -10, 8, 8, 0, 8 , 0, 8, 8, 8, this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f );

		RenderHelper.blit(guiGraphics, NContainerScreen.MIDDLE_LEFT, getX(), getY() + 8, -10, 8, getHeight() - 16, 0, 8, 0, 8, 8, 8, this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f );

		RenderHelper.blit(guiGraphics, NContainerScreen.LEFT_BOT, getX(), getY() + getHeight() - 8, -10, 8, 8, 0, 8, 0, 8, 8, 8, this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f );

		RenderHelper.blit(guiGraphics, NContainerScreen.MIDDLE_BOT, getX() + 8, getY() + getHeight() - 8, -10, getWidth() - 16, 8, 0, 8, 0, 8, 8, 8, this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f );

		RenderHelper.blit(guiGraphics, NContainerScreen.RIGHT_BOT, getX() + getWidth() - 8, getY() + getHeight() - 8, -10, 8,8, 0, 8, 0, 8, 8, 8, this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f );

		RenderHelper.blit(guiGraphics, NContainerScreen.MIDDLE_RIGHT, getX() + getWidth() - 8, getY() + 8, -10, 8, getHeight() - 16, 0, 8, 0, 8, 8,8,this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f );

		RenderHelper.blit(guiGraphics, NContainerScreen.MIDDLE, getX() + 8, getY() + 8, -10, getWidth() - 16, getHeight() - 16, 0, 8, 0, 8, 8,8,this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f );

		if (state == State.CLOSED)
		{
			icon.get().render(guiGraphics, this.getX() + (this.getWidth() / 2) - 8, this.getY() + (this.getHeight() / 2) - 8, 16, 16);
			renderTooltip();
		}
		
		poseStack.popPose();

	}
	
	private void renderTooltip()
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

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) 
	{
		boolean canClick = true;
		for (AbstractWidget widget : this.widgets)
		{
			if (widget.isMouseOver(mouseX, mouseY))
			{
				canClick = false;
			}
		}
		
		return canClick && super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void onClick(double mouseX, double mouseY) 
	{
		if (state.getId() % 2 != 0)
			return;
		else if (state == State.OPEN)
		{
			state = State.CLOSING;
		}
		else if (state == State.CLOSED) 
		{
			state = State.OPENING;
		}
	}
	
	public Side getSide() 
	{
		return side;
	}
	
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput p_259858_)
	{
	}
	
	public enum State
	{
		OPEN(0), 
		OPENING(1),
		CLOSED(2),
		CLOSING(3);
		
		private final int id;
		
		State (int id)
		{
			this.id = id;
		}
		
		public int getId() 
		{
			return id;
		}
	}
	public enum Side
	{
		LEFT, 
		RIGHT
	}

}

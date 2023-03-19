/**
 * @author ArcAnc
 * Created at: 2023-01-24
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget;

import java.util.function.Supplier;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ChangeSizeButton extends Button 
{

	private final int maxValue;
	private final int minValue;
	private Label label;
	private Button decrease;
	private Button increase;
	private int currentValue;
	private OnPress pressAction;
	
	public ChangeSizeButton(int x, int y, int minValue, int maxValue, int currentValue, OnPress pressAction, ButtonCtx leftButton, ButtonCtx rightButton) 
	{
		super(x, y, 65, 55, Component.empty(), but -> {}, Button.DEFAULT_NARRATION);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.currentValue = currentValue;
		this.pressAction = pressAction;
		
		//shift +16
		//ctrl + 8
		//alt + 4
		
		decrease = Button.builder(Component.translatable(leftButton.name().get()), but -> 
		{
			if (Screen.hasShiftDown())
				this.currentValue -= 16;
			else if (Screen.hasControlDown())
				this.currentValue -= 8;
			else if (Screen.hasAltDown())
				this.currentValue -= 4;
			else
				this.currentValue--;

			increase.active = true;
			checkButtonsBounds();
		}).pos(x + 10,  y + 10).
		   size(20, 20).
		   tooltip(Tooltip.create(Component.translatable(leftButton.tooltip().get()))).
		   build();
		increase = Button.builder(Component.translatable(rightButton.name().get()), but -> 
		{
			if (Screen.hasShiftDown())
				this.currentValue += 16;
			else if (Screen.hasControlDown())
				this.currentValue += 8;
			else if (Screen.hasAltDown())
				this.currentValue += 4;
			else
				this.currentValue++;

			decrease.active = true;
			checkButtonsBounds();
		}).pos(x + 35,  y + 10).
		   size(20, 20).
		   tooltip(Tooltip.create(Component.translatable(rightButton.tooltip().get()))).
		   build();
		
		label = new Label(x + 30, y , 5, 5, () -> Component.literal(Integer.toString(this.currentValue)).withStyle(ChatFormatting.DARK_GRAY));
		
		checkButtonsBounds();
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
		if (visible)
		{
			renderWidget(stack, mouseX, mouseY, partialTicks);
			
			this.decrease.visible = true;
			this.increase.visible = true;
			this.label.visible = true;
		}
		else
		{
			this.decrease.visible = false;
			this.increase.visible = false;
			this.label.visible = false;
		}
		
		decrease.render(stack, mouseX, mouseY, partialTicks);
		increase.render(stack, mouseX, mouseY, partialTicks);
		label.render(stack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderWidget(PoseStack stack, int mouseY, int mouseX, float partialTicks) 
	{
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int buttonId) 
	{
		if (this.active && this.visible) 
		{
			if (decrease.mouseClicked(mouseX, mouseY, buttonId) || increase.mouseClicked(mouseX, mouseY, buttonId))
			{
				onClick(mouseX, mouseY);
				return true;	
			}
		}
		return false;
	}
	
	@Override
	public void onClick(double mouseX, double mouseY) 
	{
		this.pressAction.onPress(this);
	}
	
	public int getCurrentValue() 
	{
		return currentValue;
	}
	
	private void checkButtonsBounds()
	{
		if (this.currentValue >= maxValue)
		{
			increase.active = false;
			this.currentValue = maxValue;
		}
		if (this.currentValue <= minValue)
		{
			this.currentValue = minValue;
			decrease.active = false;
		}

	}
	
	@OnlyIn(Dist.CLIENT)
	public record ButtonCtx(Supplier<String> name, Supplier<String> tooltip) {}
	
	@OnlyIn(Dist.CLIENT)
	public interface OnPress 
	{
		void onPress(ChangeSizeButton button);
	}
}

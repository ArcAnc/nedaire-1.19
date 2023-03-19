/**
 * @author ArcAnc
 * Created at: 2023-01-25
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget.info;

import java.util.function.Supplier;

import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IconCheckbox extends Checkbox 
{
	private ResourceLocation texture;
	private OnPress pressAction;
	private Supplier<Tooltip> tooltip;
	
	public IconCheckbox(int x, int y, int width, int height, boolean selected, ResourceLocation texture, OnPress actions, Supplier<Tooltip> tooltip) 
	{
		super(x, y, width, height, Component.empty(), selected, false);
		this.texture = texture;
		this.pressAction = actions;
		this.tooltip = tooltip;
	}
	
	@Override
	public void onClick(double mouseX, double mouseY) 
	{
		onPress();
		this.pressAction.onPress(this);
	}
	
	@Override
	public void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
		stack.pushPose();
		
		RenderSystem.setShaderTexture(0, texture);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		blit(stack, this.getX(), this.getY(), this.width, this.height, selected() ? 0.0f : 20.0F, this.isHovered() ? 20.0F : 0.0F, 20, 20, 64, 64);
		
		RenderSystem.disableBlend();
		
		stack.popPose();
	}
	

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
		if (this.visible) 
		{
			this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
			this.renderWidget(stack, mouseX, mouseY, partialTicks);
			renderTooltip();
		}
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

	@OnlyIn(Dist.CLIENT)
	public interface OnPress 
	{
		void onPress(IconCheckbox button);
	}

}

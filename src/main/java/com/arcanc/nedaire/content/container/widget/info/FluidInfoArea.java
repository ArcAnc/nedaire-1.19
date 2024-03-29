/**
 * @author ArcAnc
 * Created at: 2023-01-19
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget.info;

import java.awt.Color;
import java.util.function.Supplier;

import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidInfoArea extends InfoArea 
{
	public static final ResourceLocation TEXTURE = NDatabase.GUI.getTexturePath(NDatabase.GUI.Elements.FluidHandler.PATH);

	protected IFluidHandler fluid;
	protected Supplier<Tooltip> tooltip;
	protected int tank;

	public FluidInfoArea(Rect2i area, IFluidHandler handler)
	{
		this(area, handler, 0);
	}
	
	public FluidInfoArea(Rect2i area, IFluidHandler handler, int tank) 
	{
		super(area);
		
		this.fluid = handler;
		this.tank = tank;
		
		IClientFluidTypeExtensions renderProps = IClientFluidTypeExtensions.of(fluid.getFluidInTank(tank).getFluid());
		Style style = Style.EMPTY.withColor(renderProps.getTintColor());
		
		this.tooltip = () -> Tooltip.create(Component.translatable(NDatabase.Capabilities.FluidHandler.Lang.DESCRIPTION_MAIN,
				Component.literal(Integer.toString(fluid.getFluidInTank(tank).getAmount())).withStyle(style),
				Component.literal(Integer.toString(fluid.getTankCapacity(tank))).withStyle(style),
				Component.translatable(fluid.getFluidInTank(tank).getTranslationKey()).withStyle(style)).withStyle(ChatFormatting.GRAY));
	}
	
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
		if (visible)
		{
	         this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
	         this.renderWidget(stack, mouseX, mouseY, partialTicks);
	         this.renderTooltip();
		}
	}
	
	@Override
	public void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
		stack.pushPose();
		
		Minecraft mc = RenderHelper.mc();
		Screen screen = mc.screen;
		if (screen != null)
		{
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

			RenderSystem.setShaderTexture(0, TEXTURE);

			Screen.blit(stack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0, 0, 18, 42, 64, 64);
			Screen.blit(stack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 46, 0, 18, 42, 64, 64);
			
			float fluidPercent = (float)fluid.getFluidInTank(tank).getAmount() / fluid.getTankCapacity(tank);
			
			float f = this.getY() + (this.getHeight() * (1 - fluidPercent));
			float f1 = this.getHeight() * fluidPercent;
			
			if (!FluidHelper.isEmpty(fluid))
			{
				if (!fluid.getFluidInTank(tank).isEmpty())
				{
					IClientFluidTypeExtensions renderProps = IClientFluidTypeExtensions.of(fluid.getFluidInTank(tank).getFluid());
					Color color = new Color(renderProps.getTintColor());
					ResourceLocation texture = renderProps.getStillTexture();
					TextureAtlasSprite still = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
			
					RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
					RenderSystem.setShaderTexture(0, still.atlasLocation());
					blit(stack, this.getX(), (int)f, 0/*FIXME: find method to get blitOffset*/, this.getWidth(), (int)f1, still);

				}
			}
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
			RenderSystem.setShaderTexture(0, TEXTURE);

			Screen.blit(stack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 46, 0, 18, 42, 64, 64);
		}
		
		stack.popPose();
	}
	
	protected void renderTooltip() 
	{
		if (isHovered())
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

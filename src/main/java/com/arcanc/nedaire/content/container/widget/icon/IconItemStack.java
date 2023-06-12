/**
 * @author ArcAnc
 * Created at: 2023-01-25
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget.icon;

import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class IconItemStack implements Icon<ItemStack> 
{
	private final boolean overlay;
	private final ItemStack stack;
	
	public IconItemStack(ItemStack stack, boolean requiredOverlay)
	{
		this.stack = stack;
		this.overlay = requiredOverlay;
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int x, int y, int width, int height)
	{
		PoseStack poseStack = guiGraphics.pose();

		poseStack.pushPose();
		
		float sizeX = width/16f;
		float sizeY = height/16f;
		
		poseStack.scale(sizeX, sizeY, 1f);
		
		RenderHelper.renderItemStack(guiGraphics, this.stack, x, y, overlay);
	
		poseStack.scale(-sizeX, -sizeY, 1f);
		
		poseStack.popPose();
	}

}

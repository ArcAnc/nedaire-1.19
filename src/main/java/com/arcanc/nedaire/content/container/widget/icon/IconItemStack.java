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

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class IconItemStack implements Icon<ItemStack> 
{
	private boolean overlay;
	private ItemStack stack;
	
	public IconItemStack(ItemStack stack, boolean requiredOverlay)
	{
		this.stack = stack;
		this.overlay = requiredOverlay;
	}
	
	public IconItemStack(ItemLike stack)
	{
		this(new ItemStack(stack), false);
	}
	
	@Override
	public void render(PoseStack stack, int x, int y, int width, int height) 
	{
		stack.pushPose();
		
		float sizeX = width/16f;
		float sizeY = height/16f;
		
		stack.scale(sizeX, sizeY, 1f);
		
		RenderHelper.renderItemStack(stack, this.stack, x, y, overlay);
	
		stack.scale(-sizeX, -sizeY, 1f);
		
		stack.popPose();
	}

}

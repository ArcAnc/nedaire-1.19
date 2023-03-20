/**
 * @author ArcAnc
 * Created at: 2023-01-25
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget.icon;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface Icon<T> 
{
	void render(PoseStack stack, int x, int y, int width, int height);

	static IconResourceLocation of(ResourceLocation loc, int blitOffset, int texX, int texY, int texW, int texH, int textureSizeX, int textureSizeY)
	{
		return new IconResourceLocation(loc, blitOffset, texX, texY, texW, texH, textureSizeX, textureSizeY);
	}
	
	static IconItemStack of (ItemStack stack, boolean overlay)
	{
		return new IconItemStack(stack, overlay);
	}
	
	static IconItemStack of (ItemLike stack)
	{
		return new IconItemStack(stack);
	}
}

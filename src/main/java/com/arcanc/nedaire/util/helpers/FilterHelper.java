/**
 * @author ArcAnc
 * Created at: 2023-01-13
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import com.arcanc.nedaire.content.capabilities.filter.CapabilityFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter;
import com.arcanc.nedaire.content.capabilities.vim.IVim;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

public class FilterHelper 
{
	public static Capability<IFilter<IItemHandler, ItemStack>> itemFilter = CapabilityFilter.FILTER_ITEM;
	public static Capability<IFilter<IItemHandler, FluidStack>> fluidFilter = CapabilityFilter.FILTER_FLUID;
	public static Capability<IFilter<IVim, Integer>> energyFilter = CapabilityFilter.FILTER_VIM; 

	public static boolean isItemHandler(BlockEntity tile, Direction dir)
	{
		return tile != null && tile.getCapability(itemFilter, dir).isPresent();
	}
	
	public static LazyOptional<IFilter<IItemHandler, ItemStack>> getItemFilter (BlockEntity tile, Direction dir)
	{
		if (isItemHandler(tile, dir))
		{
			return tile.getCapability(itemFilter, dir);
		}
		return LazyOptional.empty();
	}
	
	public static LazyOptional<IFilter<IItemHandler, ItemStack>> getItemFilter (BlockEntity tile)
	{
		return getItemFilter(tile, null);
	}

}

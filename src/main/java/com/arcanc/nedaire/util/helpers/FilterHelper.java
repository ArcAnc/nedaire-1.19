/**
 * @author ArcAnc
 * Created at: 2023-01-13
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import org.jetbrains.annotations.NotNull;

import com.arcanc.nedaire.content.capabilities.filter.CapabilityFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IFluidFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IItemFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IVimFilter;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class FilterHelper 
{
	public static Capability<IItemFilter> itemFilter = CapabilityFilter.FILTER_ITEM;
	public static Capability<IFluidFilter> fluidFilter = CapabilityFilter.FILTER_FLUID;
	public static Capability<IVimFilter> energyFilter = CapabilityFilter.FILTER_VIM; 

	public static boolean isItemHandler(BlockEntity tile, Direction dir)
	{
		return tile != null && tile.getCapability(itemFilter, dir).isPresent();
	}
	
	public static boolean isFluidHandler(BlockEntity tile, Direction dir)
	{
		return tile != null && tile.getCapability(fluidFilter, dir).isPresent();
	}
	
	public static boolean isVimHandler(BlockEntity tile, Direction dir)
	{
		return tile != null && tile.getCapability(energyFilter, dir).isPresent();
	}
	
	public static @NotNull LazyOptional<IItemFilter> getItemFilter (BlockEntity tile, Direction dir)
	{
		if (isItemHandler(tile, dir))
		{
			return tile.getCapability(itemFilter, dir);
		}
		return LazyOptional.empty();
	}
	
	public static @NotNull LazyOptional<IItemFilter> getItemFilter (BlockEntity tile)
	{
		return getItemFilter(tile, null);
	}
	
	public static @NotNull LazyOptional<IFluidFilter> getFluidFilter (BlockEntity tile, Direction dir)
	{
		if (isFluidHandler(tile, dir))
		{
			return tile.getCapability(fluidFilter, dir);
		}
		return LazyOptional.empty();
	}
	
	public static @NotNull LazyOptional<IFluidFilter> getFluidFilter (BlockEntity tile)
	{
		return getFluidFilter(tile, null);
	}

	public static @NotNull LazyOptional<IVimFilter> getVimFilter (BlockEntity tile, Direction dir)
	{
		if (isVimHandler(tile, dir))
		{
			return tile.getCapability(energyFilter, dir);
		}
		return LazyOptional.empty();
	}
	
	public static @NotNull LazyOptional<IVimFilter> getVimFilter (BlockEntity tile)
	{
		return getVimFilter(tile, null);
	}
	
	public static @NotNull LazyOptional<IFilter<?, ?, ?>> getFilterFromString(BlockEntity tile, String str)
	{
		return switch (str)
		{
			case "item" -> getItemFilter(tile).cast();
			case "fluid" -> getFluidFilter(tile).cast();
			case "vim" -> getVimFilter(tile).cast();
			default -> LazyOptional.empty();
		};
	}
}

/**
 * @author ArcAnc
 * Created at: 2022-04-25
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import com.arcanc.nedaire.content.capabilities.vim.CapabilityVim;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class VimHelper 
{
	public static Capability<IVim> vimHandler = CapabilityVim.VIM;
	
	public static boolean isVimHandler(Level world, BlockPos pos, Direction dir)
	{
		if (world != null && pos != null)
		{
			return BlockHelper.getTileEntity(world, pos).map(tile -> 
			{
				return isVimHandler(tile, dir);
			}).orElse(false);
		}
		return false;
	}
	
	public static boolean isVimHandler(BlockEntity tile)
	{
		return isVimHandler(tile, null);
	}

	public static boolean isVimHandler(BlockEntity tile, Direction dir)
	{
		return tile != null && tile.getCapability(vimHandler, dir).isPresent();
	}
	
	public static boolean isVimHandler(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getCapability(vimHandler).isPresent();
	}

	public static LazyOptional<IVim> getNearbyVimHandler (BlockEntity tile, Direction dir)
	{
		if (tile != null)
		{
			Level world = tile.getLevel();
			BlockPos pos = tile.getBlockPos();

			return BlockHelper.getTileEntity(world, pos.relative(dir)).map(t ->
			{
				return getVimHandler(t, dir.getOpposite());
			}).orElse(LazyOptional.empty());
		}
		return LazyOptional.empty();
	}

	public static LazyOptional<IVim> getVimHandler (BlockEntity tile, Direction dir)
	{
		if (isVimHandler(tile, dir))
		{
			return tile.getCapability(vimHandler, dir);
		}
		return LazyOptional.empty();
	}
	
	public static LazyOptional<IVim> getVimHandler (BlockEntity tile)
	{
		return getVimHandler(tile, null);
	}
	
	public static LazyOptional<IVim> getVimHandler (ItemStack stack)
	{
		if (isVimHandler(stack))
		{
			return stack.getCapability(vimHandler);
		}
		return LazyOptional.empty();
	}
	
	public static boolean hasEmptySpace(BlockEntity tile)
	{
		return hasEmptySpace(tile, null);
	}
	
	public static boolean hasEmptySpace(BlockEntity tile, Direction dir) 
	{
		LazyOptional<IVim> handler = getVimHandler(tile, dir);
		if (handler.isPresent())
		{
			return hasEmptySpace(handler);
		}
		return false;
	}
	
	public static boolean hasEmptySpace (LazyOptional<IVim> in)
	{
		return in.map(handler -> {
			return handler.getMaxEnergyStored() - handler.getEnergyStored() > 0;
		}).orElse(false);
	}
	
	public static boolean hasEmptySpace (IVim in)
	{
		return in.getMaxEnergyStored() - in.getEnergyStored() > 0;
	}

	public static int getEmptySpace (LazyOptional<IVim> in)
	{
		if (hasEmptySpace(in))
		{
			return in.map(handler -> 
			{
				return handler.getMaxEnergyStored() - handler.getEnergyStored();
			}).orElse(0);
		}
		return 0;
	}
	
	public static int getEmptySpace (IVim in)
	{
		if (hasEmptySpace(in))
		{
			return in.getMaxEnergyStored() - in.getEnergyStored();
		}
		return 0;
	}

	public static boolean isEmpty(BlockEntity tile) 
	{
		LazyOptional<IVim> hand = getVimHandler(tile);
		if (hand != null)
		{
			return isEmpty(hand);
		}
		return false;
	}
	
	public static boolean isEmpty(LazyOptional<IVim> in) 
	{
		return in.map(handler -> 
		{
			return handler.getEnergyStored() == 0;
		}).orElse(true);
	}
}

/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidHelper 
{
	public static Capability<IFluidHandler> fluidHandler = ForgeCapabilities.FLUID_HANDLER;
	public static Capability<IFluidHandlerItem> fluidHandlerItem = ForgeCapabilities.FLUID_HANDLER_ITEM;
	
	public static FluidStack copyFluidStackWithAmount(FluidStack stack, int amount)
	{
		if(stack==null || stack == FluidStack.EMPTY)
			return null;
		return new FluidStack(stack, amount);
	}
	
	public static boolean hasEmptySpace(BlockEntity tile) 
	{
		return hasEmptySpace(tile, null);
	}
	
	public static boolean isFluidHandler(Level world, BlockPos pos, Direction dir)
	{
		if (world != null)
		{
			BlockEntity tile = BlockHelper.getTileEntity(world, pos);
			return isFluidHandler(tile, dir);
		}
		return false;
	}
	
	public static boolean isFluidHandler(BlockEntity tile)
	{
		return isFluidHandler(tile, null);
	}

	public static boolean isFluidHandler(BlockEntity tile, Direction dir)
	{
		return tile != null && tile.getCapability(fluidHandler, dir).isPresent();
	}
	
	public static boolean isFluidHandler(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getCapability(fluidHandlerItem).isPresent();
	}
	
	public static LazyOptional<IFluidHandler> getFluidHandler (BlockEntity tile, Direction dir)
	{
		if (isFluidHandler(tile, dir))
		{
			return tile.getCapability(fluidHandler, dir);
		}
		return LazyOptional.empty();
	}
	
	public static LazyOptional<IFluidHandler> getNearbyFluidHandler (BlockEntity tile, Direction dir)
	{
		if (tile != null)
		{
			Level world = tile.getLevel();
			BlockPos pos = tile.getBlockPos();
			
			BlockEntity t = BlockHelper.getTileEntity(world, pos.relative(dir));
			if (t != null)
			{
				 return getFluidHandler(t, dir.getOpposite());
			}
		}
		return LazyOptional.empty();
	}
	
	public static LazyOptional<IFluidHandler> getFluidHandler (BlockEntity tile)
	{
		return getFluidHandler(tile, null);
	}
	
	public static LazyOptional<IFluidHandler> getFluidHandler (ItemStack stack)
	{
		if (isFluidHandler(stack))
		{
			return stack.getCapability(fluidHandler);
		}
		return LazyOptional.empty();
	}
	
	public static boolean hasEmptySpace(BlockEntity tile, Direction dir) 
	{
		LazyOptional<IFluidHandler> handler = getFluidHandler(tile, dir);
		if (handler.isPresent())
		{
			return hasEmptySpace(handler);
		}
		return false;
	}

	public static boolean hasEmptySpace(LazyOptional<IFluidHandler> in)
	{
		return in.map(handler -> 
		{
			for (int q = 0; q < handler.getTanks(); q++)
			{
				FluidStack stack = handler.getFluidInTank(q);
				if (stack.isEmpty() || stack.getAmount() < handler.getTankCapacity(q))
				{
					return true;
				}
			}
			return false;
		}).orElse(false);
	}

	public static int getEmptySpace(LazyOptional<IFluidHandler> in) 
	{
		if (hasEmptySpace(in))
		{
			return in.map(handler -> 
			{
				int space = 0;
				for (int q = 0; q < handler.getTanks(); q++)
				{
					FluidStack stack = handler.getFluidInTank(q);
					if (stack.isEmpty())
					{
						space += handler.getTankCapacity(q);
					}
					else if (stack.getAmount() < handler.getTankCapacity(q))
					{
						space += handler.getTankCapacity(q) - stack.getAmount();
					}
				}
				return space;
			}).orElse(0);
		}
		return 0;
	}

	public static boolean isEmpty(BlockEntity tile) 
	{
		LazyOptional<IFluidHandler> hand = getFluidHandler(tile);
		if (hand != null)
		{
			return isEmpty(hand);
		}
		return false;
	}
	
	public static boolean isEmpty(LazyOptional<IFluidHandler> in) 
	{
		return in.map(handler -> 
		{
			for (int q = 0; q < handler.getTanks(); q++)
			{
				if (!handler.getFluidInTank(q).isEmpty())
					return false;
			}
			return true;
		}).orElse(true);
	}
	
	public static ResourceLocation getRegistryName (Item item) 
	{
		return ForgeRegistries.ITEMS.getKey(item);
	}

	
	public static JsonElement jsonSerializeFluidStack(FluidStack fluidStack)
	{
		if(fluidStack==null)
			return JsonNull.INSTANCE;
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid()).toString());
		jsonObject.addProperty("amount", fluidStack.getAmount());
		if(fluidStack.hasTag())
			jsonObject.addProperty("tag", fluidStack.getTag().toString());
		return jsonObject;
	}
	
}

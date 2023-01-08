/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemHelper 
{
	public static Capability<IItemHandler> itemHandler = ForgeCapabilities.ITEM_HANDLER;
	
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	
	public static CompoundTag parseNbtFromJson(JsonElement jsonElement) throws CommandSyntaxException
	{
		if(jsonElement.isJsonObject())
			return TagParser.parseTag(GSON.toJson(jsonElement));
		else
			return TagParser.parseTag(jsonElement.getAsString());
	}

	public static ItemStack copyStackWithAmount(ItemStack stack, int count)
	{
		ItemStack ret = stack.copy();
		if (count > ret.getMaxStackSize())
			ret.setCount(ret.getMaxStackSize());
		else
			ret.setCount(count);
		return ret;
	}

	public static boolean hasEmptySpace(BlockEntity tile) 
	{
		return hasEmptySpace(tile, null);
	}
	
	public static boolean isItemHandler(Level world, BlockPos pos, Direction dir)
	{
		if (world != null)
		{
			BlockEntity tile = BlockHelper.getTileEntity(world, pos);
			return isItemHandler(tile, dir);
		}
		return false;
	}
	
	public static boolean isItemHandler(BlockEntity tile)
	{
		return isItemHandler(tile, null);
	}

	public static boolean isItemHandler(BlockEntity tile, Direction dir)
	{
		return tile != null && tile.getCapability(itemHandler, dir).isPresent();
	}
	
	public static boolean isItemHandler(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getCapability(itemHandler).isPresent();
	}
	
	public static LazyOptional<IItemHandler> getItemHandler (BlockEntity tile, Direction dir)
	{
		if (isItemHandler(tile, dir))
		{
			return tile.getCapability(itemHandler, dir);
		}
		return LazyOptional.empty();
	}
	
	public static LazyOptional<IItemHandler> getNearbyItemHandler (BlockEntity tile, Direction dir)
	{
		if (tile != null)
		{
			Level world = tile.getLevel();
			BlockPos pos = tile.getBlockPos();
			
			BlockEntity t = BlockHelper.getTileEntity(world, pos.relative(dir));
			if (t != null)
			{
				 return getItemHandler(t, dir.getOpposite());
			}
		}
		return LazyOptional.empty();
	}
	
	public static LazyOptional<IItemHandler> getItemHandler (BlockEntity tile)
	{
		return getItemHandler(tile, null);
	}
	
	public static LazyOptional<IItemHandler> getItemHandler (ItemStack stack)
	{
		if (isItemHandler(stack))
		{
			return stack.getCapability(itemHandler);
		}
		return LazyOptional.empty();
	}
	
	public static boolean hasEmptySpace(BlockEntity tile, Direction dir) 
	{
		LazyOptional<IItemHandler> handler = getItemHandler(tile, dir);
		if (handler.isPresent())
		{
			return hasEmptySpace(handler);
		}
		return false;
	}

	public static boolean hasEmptySpace(LazyOptional<IItemHandler> in)
	{
		return in.map(handler -> 
		{
			for (int q = 0; q < handler.getSlots(); q++)
			{
				ItemStack stack = handler.getStackInSlot(q);
				if (stack.isEmpty() || stack.getCount() < stack.getMaxStackSize())
				{
					return true;
				}
			}
			return false;
		}).orElse(false);
	}

	public static int getEmptySpace(LazyOptional<IItemHandler> in) 
	{
		if (hasEmptySpace(in))
		{
			return in.map(handler -> 
			{
				int space = 0;
				for (int q = 0; q < handler.getSlots(); q++)
				{
					ItemStack stack = handler.getStackInSlot(q);
					if (stack.isEmpty())
					{
						space += handler.getSlotLimit(q);
					}
					else if (stack.getCount() < stack.getMaxStackSize())
					{
						space += stack.getMaxStackSize() - stack.getCount();
					}
				}
				return space;
			}).orElse(0);
		}
		return 0;
	}

	public static boolean isEmpty(BlockEntity tile) 
	{
		LazyOptional<IItemHandler> hand = ItemHelper.getItemHandler(tile);
		if (hand != null)
		{
			return isEmpty(hand);
		}
		return false;
	}
	
	public static boolean isEmpty(LazyOptional<IItemHandler> in) 
	{
		return in.map(handler -> 
		{
			for (int q = 0; q < handler.getSlots(); q++)
			{
				if (!handler.getStackInSlot(q).isEmpty())
					return false;
			}
			return true;
		}).orElse(true);
	}
	
	public static JsonObject toJson(ItemStack stack)
	{
		Preconditions.checkNotNull(stack, "Can't write to json null stack");
		Preconditions.checkArgument(!stack.isEmpty(), "Can't write to json empty stack");
		
		JsonObject json = new JsonObject();
		
		json.addProperty("item", getRegistryName(stack.getItem()).toString());
		
		if (stack.getCount() > 1)
		{
			json.addProperty("count", stack.getCount());
		}
		
		if (stack.getTag() != null)
		{
			CompoundTag nbt = stack.getTag();
			
			json.addProperty("nbt", nbt.getAsString());
		}
		
		return json;
	}
	
	public static ResourceLocation getRegistryName (Item item) 
	{
		return ForgeRegistries.ITEMS.getKey(item);
	}
	
	public static void dropContents (Level level, BlockPos pos, LazyOptional<IItemHandler> inventory)
	{
		if (level != null && pos != null)
		{
			inventory.ifPresent(handler -> 
			{
				for (int q = 0; q < handler.getSlots() ; q++)
				{
					Containers.dropItemStack(level, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, handler.getStackInSlot(q));
				}
			});
		}
	}
}

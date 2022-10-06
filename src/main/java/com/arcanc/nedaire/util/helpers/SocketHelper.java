/**
 * @author ArcAnc
 * Created at: 2022-06-24
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import com.arcanc.nedaire.content.module.jewelry.capability.CapabilitySocket;
import com.arcanc.nedaire.content.module.jewelry.capability.ISocket;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class SocketHelper 
{
	public static Capability<ISocket> socketHandler = CapabilitySocket.SOCKET;
	
	public static boolean isSocketHandler(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getCapability(socketHandler).isPresent();
	}
	
	public static LazyOptional<ISocket> getSocketHandler (ItemStack stack)
	{
		if (isSocketHandler(stack))
		{
			return stack.getCapability(socketHandler);
		}
		return LazyOptional.empty();
	}
	
	public static boolean hasEmptySpace(ItemStack stack) 
	{
		LazyOptional<ISocket> handler = getSocketHandler(stack);
		if (handler.isPresent())
		{
			return hasEmptySpace(handler);
		}
		return false;
	}
	
	public static boolean hasEmptySpace (LazyOptional<ISocket> in)
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

	public static int getEmptySpace (LazyOptional<ISocket> in)
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
						space += 1;
					}
				}
				return space;
			}).orElse(0);
		}
		return 0;

	}

	public static boolean isEmpty(ItemStack stack) 
	{
		LazyOptional<ISocket> hand = getSocketHandler(stack);
		if (hand != null)
		{
			return isEmpty(hand);
		}
		return false;
	}
	
	public static boolean isEmpty(LazyOptional<ISocket> in) 
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

}

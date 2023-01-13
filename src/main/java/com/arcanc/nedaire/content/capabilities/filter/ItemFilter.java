/**
 * @author ArcAnc
 * Created at: 2023-01-11
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.filter;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;
import com.google.common.base.Preconditions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemFilter implements IFilter<IItemHandler, ItemStack> 
{
	
	public NSimpleItemStorage content;
	public boolean whitelist = false;
	
	public ItemFilter(boolean whitelist, NSimpleItemStorage content) 
	{
		Preconditions.checkNotNull(content);
		this.content = content;
		this.whitelist = whitelist;
	}
	
	public ItemFilter(boolean whitelist, IInventoryCallback tile, int size)
	{
		this(whitelist, new NSimpleItemStorage(tile, size));
	}
	
	@Override
	public boolean filter(ItemStack obj) 
	{
		if (obj == null || obj.isEmpty())
			return false;
		if (whitelist)
		{
			for (int q = 0; q < content.getSlots(); q++)
			{
				if (ItemStack.isSame(obj, content.getStackInSlot(q)))
				{
					return true;
				}
			}
		}
		else
		{
			for (int q = 0; q < content.getSlots(); q++)
			{
				if (ItemStack.isSame(obj, content.getStackInSlot(q)))
				{
					return false;
				}
			}
			return true;
		}
		
		return false;
	}

	@Override
	public boolean isWhitelist() 
	{
		return whitelist;
	}
	
	@Override
	public void setWhitelist(boolean value) 
	{
		this.whitelist = value;
	}
	
	@Override
	public NSimpleItemStorage getContent() 
	{
		return content;
	}

	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		
		nbt.put(NDatabase.Capabilities.Filter.CONTENT, content.serializeNBT());
		nbt.putBoolean(NDatabase.Capabilities.Filter.WHITELIST, whitelist);
		
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		content.deserializeNBT(nbt.getCompound(NDatabase.Capabilities.Filter.CONTENT));
		whitelist = nbt.getBoolean(NDatabase.Capabilities.Filter.WHITELIST);
	}
}

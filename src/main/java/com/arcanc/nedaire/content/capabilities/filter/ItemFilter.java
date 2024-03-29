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
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IItemFilter;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;
import com.google.common.base.Preconditions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemFilter implements IItemFilter 
{
	
	public NSimpleItemStorage content;
	public boolean whitelist = true;
	public boolean modOwner = false;
	public boolean checkTag = false;
	public int extraction = 0;
	public int maxInInventory = 0;
	
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
		return filterWhiteList(obj) && filterModOwner(obj) && filterCheckTag(obj);
	}
	
	@Override
	public boolean filterWhiteList(ItemStack obj) 
	{
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
	public boolean filterModOwner(ItemStack obj) 
	{
		if (modOwner)
		{
			String locObj = ForgeRegistries.ITEMS.getKey(obj.getItem()).getNamespace();
			for (int q = 0; q < content.getSlots(); q++)
			{
				ItemStack stack = content.getStackInSlot(q);
				String loc = ForgeRegistries.ITEMS.getKey(stack.getItem()).getNamespace();
				if (locObj.equals(loc))
				{
					return true;
				}
			}
			return false;
		}
		else
		return true;
	}

	@Override
	public boolean filterCheckTag(ItemStack obj) 
	{
		if (checkTag)
		{
			for (int q = 0; q < content.getSlots(); q++)
			{
				ItemStack stack = content.getStackInSlot(q);
				if (ItemStack.tagMatches(stack, obj))
				{
					return true;
				}
			}
			return false;
		}
		else
		return true;
	}
	
	@Override
	public boolean filterExtraction(IItemHandler tileInv, ItemStack obj) 
	{
		int emptySpace = ItemHelper.getEmptySpace(tileInv);
		return Math.min(emptySpace, extraction) > 0;
	}

	@Override
	public boolean filterMaxInInventory(IItemHandler tileInv, ItemStack obj) 
	{
		int amountInInv = 0;
	
		for (int q = 0; q < tileInv.getSlots(); q++)
		{
			ItemStack stack = tileInv.getStackInSlot(q);
			if (ItemStack.isSameItemSameTags(stack, obj))
			{
				amountInInv += stack.getCount();
			}
		}
		
		return maxInInventory > amountInInv;
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
		nbt.putBoolean(NDatabase.Capabilities.Filter.MOD_OWNER, modOwner);
		nbt.putBoolean(NDatabase.Capabilities.Filter.CHECK_TAG, checkTag);
		nbt.putInt(NDatabase.Capabilities.Filter.MAX_EXTRACTING_STACK, extraction);
		nbt.putInt(NDatabase.Capabilities.Filter.MAX_AMOUNT_IN, maxInInventory);
		
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		content.deserializeNBT(nbt.getCompound(NDatabase.Capabilities.Filter.CONTENT));
		whitelist = nbt.getBoolean(NDatabase.Capabilities.Filter.WHITELIST);
		modOwner = nbt.getBoolean(NDatabase.Capabilities.Filter.MOD_OWNER);
		checkTag = nbt.getBoolean(NDatabase.Capabilities.Filter.CHECK_TAG);
		extraction = nbt.getInt(NDatabase.Capabilities.Filter.MAX_EXTRACTING_STACK);
		maxInInventory = nbt.getInt(NDatabase.Capabilities.Filter.MAX_AMOUNT_IN);
	}

	@Override
	public boolean isModOwner() 
	{
		return modOwner;
	}

	@Override
	public void setModOwner(boolean value) 
	{
		this.modOwner = value;
	}

	@Override
	public boolean isCheckTag() 
	{
		return checkTag;
	}

	@Override
	public void setCheckTag(boolean value) 
	{
		this.checkTag = value;
	}

	@Override
	public int getExtraction() 
	{
		return this.extraction;
	}

	@Override
	public void setExtracion(int value) 
	{
		this.extraction = value;
	}

	@Override
	public int getMaxInInventory() 
	{
		return this.maxInInventory;
	}

	@Override
	public void setMaxInInventory(int value) 
	{
		this.maxInInventory = value;
	}
}

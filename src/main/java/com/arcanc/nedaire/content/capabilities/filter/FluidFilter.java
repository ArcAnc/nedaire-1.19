/**
 * @author ArcAnc
 * Created at: 2023-02-15
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.filter;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IFluidFilter;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;
import com.google.common.base.Preconditions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;

public class FluidFilter implements IFluidFilter 
{

	public NSimpleItemStorage content;
	public boolean whitelist = true;
	public boolean modOwner = false;
	public boolean checkTag = false;
	public int extraction = 0;
	public int maxInInventory = 0;
	
	public FluidFilter(boolean whitelist, NSimpleItemStorage content) 
	{
		Preconditions.checkNotNull(content);
		this.content = content;
		this.whitelist = whitelist;
	}
	
	public FluidFilter(boolean whitelist, IInventoryCallback tile, int size)
	{
		this(whitelist, new NSimpleItemStorage(tile, size));
	}
	
	@Override
	public boolean filter(FluidStack obj) 
	{
		if (obj == null || obj.isEmpty())
			return false;
		return filterWhiteList(obj) && filterModOwner(obj) && filterCheckTag(obj);
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
	public boolean filterWhiteList(FluidStack obj) 
	{
		if (whitelist)
		{
			for (int q = 0; q < content.getSlots(); q++)
			{
				ItemStack stack = content.getStackInSlot(q);
				
				if (FluidHelper.getFluidHandler(stack).isPresent())
				{
					if (FluidHelper.itemContains(FluidHelper.getFluidHandler(stack), obj))
					{
						return true;
					}
				}
			}
		}
		else
		{
			for (int q = 0; q < content.getSlots(); q++)
			{
				ItemStack stack = content.getStackInSlot(q);
				
				if (FluidHelper.getFluidHandler(stack).isPresent())
				{
					if (FluidHelper.itemContains(FluidHelper.getFluidHandler(stack), obj))
					{
						return false;
					}
				}
			}
			return true;
		}

		return false;
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
	public boolean filterModOwner(FluidStack obj) 
	{
		if (modOwner)
		{
			String locObj = FluidHelper.getRegistryName(obj.getFluid()).getNamespace();
			for (int q = 0; q < content.getSlots(); q++)
			{
				ItemStack stack = content.getStackInSlot(q);
				LazyOptional<IFluidHandlerItem> h = FluidHelper.getFluidHandler(stack);
				if (h.isPresent())
				{
					boolean b = h.map(handler -> 
					{
						for (int f = 0; f < handler.getTanks(); f++)
						{
							FluidStack fluid = handler.getFluidInTank(f);
							String loc = FluidHelper.getRegistryName(fluid.getFluid()).getNamespace();
							if (locObj.equals(loc))
							{
								 return true;
							}
						}
						return false;
					}).orElse(false);
					if (b)
						return true;
				}
			}
			return false;
		}
		else
		return true;
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
	public boolean filterCheckTag(FluidStack obj) 
	{
		if (checkTag)
		{
			for (int q = 0; q < content.getSlots(); q++)
			{
				ItemStack stack = content.getStackInSlot(q);
				LazyOptional<IFluidHandlerItem> h = FluidHelper.getFluidHandler(stack);
				
				if (h.isPresent())
				{
					boolean b = h.map(handler -> 
					{
						for (int f = 0; f < handler.getTanks(); f++)
						{
							FluidStack fluid = handler.getFluidInTank(f);
							if (FluidStack.areFluidStackTagsEqual(fluid, obj))
							{
								return true;
							}
						}
						return false;
					}).orElse(false);
					if (b)
						return true;
				}
			}
			return false;
		}
		else
		return true;
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
	public boolean filterExtraction(IFluidHandler tileInv, FluidStack obj) 
	{
		int emptySpace = FluidHelper.getEmptySpace(tileInv);
		return Math.min(emptySpace, extraction) > 0;
	}

	@Override
	public int getMaxInInventory() 
	{
		return maxInInventory;
	}

	@Override
	public void setMaxInInventory(int value) 
	{
		this.maxInInventory = value;
	}

	@Override
	public boolean filterMaxInInventory(IFluidHandler tileInv, FluidStack obj) 
	{
		int out = 0;
		for (int f = 0; f < tileInv.getTanks(); f++)
		{
			FluidStack fluid = tileInv.getFluidInTank(f);
			if (fluid.isFluidEqual(obj))
			{
				out += fluid.getAmount();
			}
		}
		return maxInInventory > out;
	}

	@Override
	public IItemHandler getContent() 
	{
		return this.content;
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
}

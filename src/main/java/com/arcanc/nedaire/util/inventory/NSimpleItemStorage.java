/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.util.database.NDatabase;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class NSimpleItemStorage implements IItemHandler, INBTSerializable<CompoundTag> 
{
	@Nullable
	protected IInventoryCallback tileInv;
	protected List<IItemStackAccess> items;
	
	public NSimpleItemStorage() 
	{
		this(null, 0);
	}
	
	public NSimpleItemStorage(@Nullable IInventoryCallback tileInv)
	{
		this.tileInv = tileInv;
		items = new ArrayList<>();
	}
	
	public NSimpleItemStorage(@Nullable IInventoryCallback tileInv, int size)
	{
		this.tileInv = tileInv;
		items = Stream.generate(ItemStackHolder :: new).limit(size).collect(Collectors.toList());
	}
	
	public NSimpleItemStorage(@Nullable IInventoryCallback tileInv, @Nonnull List<IItemStackAccess> stacks)
	{
		this.tileInv = tileInv;
		items = stacks;
	}
	
    public NSimpleItemStorage addSlot(int capacity, Predicate<ItemStack> validator)
    {
    	return addSlot(new ItemStackHolder(capacity, validator));
    }
    
    public NSimpleItemStorage addSlot (@Nonnull ItemStackHolder slot)
    {
    	this.items.add(slot);
    	return this;
    }
    
    public NSimpleItemStorage addSlots(@Nonnull List<IItemStackAccess> slots)
    {
    	this.items.addAll(slots);
    	return this;
    }
	
	@Override
	public int getSlots() 
	{
		return items.size();
	}
	
	public List<IItemStackAccess> getItems() 
	{
		return items;
	}
	
	public IItemStackAccess getSlot (int slot)
	{
		validateSlotIndex(slot);
		return items.get(slot);
	}
	
	@Override
	public @NotNull ItemStack getStackInSlot(int slot)
	{
		validateSlotIndex(slot);
		return items.get(slot).getItemStack();
	}
	
	public void setStackInSlot (@Nonnull ItemStack stack, int slot)
	{
		validateSlotIndex(slot);
		items.get(slot).setItemStack(stack);
		onInventoryChange(slot);
	}

	@Override
	public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		if (!isItemValid(slot, stack))
			return stack;
		validateSlotIndex(slot);
		
		ItemStack retStack = items.get(slot).insert(stack, simulate);
		
		if (!simulate)
		{
			onInventoryChange(slot);
		}
		
		return retStack;
	}

	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount == 0)
			return ItemStack.EMPTY;
		validateSlotIndex(slot);
		
		ItemStack retStack = items.get(slot).extract(amount, simulate);
		
		if (!simulate)
		{
			onInventoryChange(slot);
		}
		return retStack;
	}

	@Override
	public int getSlotLimit(int slot) 
	{
		validateSlotIndex(slot);
		return items.get(slot).getSizeLimit();
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack)
	{
		validateSlotIndex(slot);
		return items.get(slot).isValid(stack);
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		
		if (!hasSlots())
		{
			return tag;
		}
		
		ListTag list = new ListTag();
		for (int q = 0; q < getSlots(); q++)
		{
			list.add(items.get(q).save());
		}
		
		tag.put(NDatabase.Capabilities.ItemHandler.SLOTS, list);
		
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
        for (IItemStackAccess slot : items) 
        {
            slot.clear();
        }
        ListTag list = nbt.getList(NDatabase.Capabilities.ItemHandler.SLOTS, Tag.TAG_COMPOUND);
        for (int q = 0; q < list.size(); ++q) 
        {
        	items.get(q).load(list.getCompound(q));
        }
	}
	
	public void onInventoryChange(int slot)
	{
		if(tileInv == null)
			return;
		validateSlotIndex(slot);
		tileInv.onInventoryChange(slot);
		items.get(slot).onContentsChanged();
	}
	
	public void clear()
	{
		for (IItemStackAccess slot : items)
		{
			slot.clear();
		}
	}
	
	public boolean hasSlots()
	{
		return getSlots() > 0 ;
	}
	
    protected void validateSlotIndex(int slot)
    {
        if (slot < 0 || slot >= items.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + items.size() + "]");
    }

}

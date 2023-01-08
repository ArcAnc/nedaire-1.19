/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.inventory;

import javax.annotation.Nonnull;

import com.arcanc.nedaire.util.database.NDatabase;
import com.google.common.base.Predicate;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemStackHolder implements IItemStackAcess 
{

	private ItemStack stack = ItemStack.EMPTY;
	/**
	 * -1 - unlimited amount, 0 - empty slot
	 */
	private int stackLimit = 64;
	private Predicate<ItemStack> validator = stack -> true;
	
	public ItemStackHolder(@Nonnull ItemStack stack, int limit, @Nonnull Predicate<ItemStack> validator) 
	{
		this.stack = stack;
		this.stackLimit = limit;
		this.validator = validator;
	}
	
	public ItemStackHolder(@Nonnull ItemStack stack, @Nonnull Predicate<ItemStack> validator) 
	{
		this(stack, 64, validator);
	}
	
	public ItemStackHolder(int stackLimit, @Nonnull Predicate<ItemStack> validator) 
	{
		this(ItemStack.EMPTY, stackLimit, validator);
	}
	
	public ItemStackHolder(@Nonnull ItemStack stack) 
	{
		this(stack, item -> true);
	}
	
	public ItemStackHolder(int stackLimit) 
	{
		this (stackLimit, item -> true);
	}
	
	public ItemStackHolder(@Nonnull Predicate<ItemStack> validator) 
	{
		this (ItemStack.EMPTY, validator);
	}
	
	public ItemStackHolder() 
	{
		this(ItemStack.EMPTY);
	}
	
	@Override
	public ItemStack getItemStack() 
	{
		return stack;
	}

	@Override
	public void setItemStack(ItemStack stack) 
	{
		if (stack != null)
		{
			this.stack = stack;
			onContentsChanged();
		}
	}

	@Override
	public int getCount() 
	{
		return stack.getCount();
	}

	@Override
	public void setCount(int newCount) 
	{
		int limit = stackLimit < stack.getMaxStackSize() ? stackLimit : stack.getMaxStackSize();
		if (newCount <= 0)
			stack = ItemStack.EMPTY;
		else if (newCount > limit)
			stack.setCount(limit);
		else
			stack.setCount(newCount);
		onContentsChanged();
	}

	@Override
	public int modify(int amount) 
	{
		int newCount = stack.getCount() + amount;

		if (newCount <= 0)
			stack = ItemStack.EMPTY;
		else if (newCount > stack.getMaxStackSize())
			stack.setCount(stack.getMaxStackSize());
		else
			stack.setCount(newCount);
		onContentsChanged();
		
		return newCount;
	}

	@Override
	public int getSizeLimit() 
	{
		return Math.min(stackLimit, stack.getMaxStackSize());
	}

	@Override
	public boolean isEmpty() 
	{
		return stack.isEmpty();
	}

	@Override
	public ItemStack insert(ItemStack stack, boolean simulate) 
	{
        if (stack.isEmpty())
            return ItemStack.EMPTY;
            
        if (!isValid(stack))
            return stack;

        int limit =  Math.min(stackLimit, stack.getMaxStackSize());

        if (!isEmpty())
        {
        	if (!ItemHandlerHelper.canItemStacksStack(stack, getItemStack()))
                return stack;

            limit -= getItemStack().getCount();
        }
        
        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() >= limit;
        
        
        if (!simulate)
        {
           if (getItemStack().isEmpty())
            {
            	setItemStack(reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            }
            else
            {
            	modify(reachedLimit ? limit : stack.getCount());
            }
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack extract(int amount, boolean simulate) 
	{
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack existing = this.stack;

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract)
        {
            if (!simulate)
            {
            	this.clear();
                return existing;
            }
            else
            {
                return existing.copy();
            }
        }
        else
        {
            if (!simulate)
            {
                setItemStack(ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
	}

	@Override
	public void clear() 
	{
		stack = ItemStack.EMPTY;
		onContentsChanged();
	}

	@Override
	public IItemStackAcess load(CompoundTag nbt) 
	{
		this.stackLimit = nbt.getInt(NDatabase.Capabilities.ItemHandler.ItemHolder.SLOT_LIMIT);
		this.stack = ItemStack.of(nbt);
		
		return this;
	}

	@Override
	public CompoundTag save() 
	{
		CompoundTag nbt = new CompoundTag();
		
		stack.save(nbt);
		nbt.putInt(NDatabase.Capabilities.ItemHandler.ItemHolder.SLOT_LIMIT, stackLimit);
		
		return nbt;
	}

	@Override
	public IItemStackAcess setValidator(Predicate<ItemStack> validator) 
	{
		if (validator != null)
		{
			this.validator = validator;
		}
		
		return this;
	}

	@Override
	public boolean isValid(ItemStack stack) 
	{
		if (stack != null)
		{
			return validator.test(stack);
		}
		return false;	
	}
}

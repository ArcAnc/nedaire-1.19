/**
 * @author ArcAnc
 * Created at: 2023-02-16
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.fluid;

import javax.annotation.Nonnull;

import com.arcanc.nedaire.util.database.NDatabase;
import com.google.common.base.Predicate;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class FluidStackHolder implements IFluidStackAccess 
{

	private FluidStack stack = FluidStack.EMPTY;
	/**
	 * -1 - unlimited amount, 0 - empty slot
	 */
	private int stackLimit = Integer.MAX_VALUE;
	private Predicate<FluidStack> validator = stack -> true;
	
	public FluidStackHolder(@Nonnull FluidStack stack, int limit, @Nonnull Predicate<FluidStack> validator) 
	{
		this.stack = stack;
		this.stackLimit = limit;
		this.validator = validator;
	}
	
	public FluidStackHolder(@Nonnull FluidStack stack, @Nonnull Predicate<FluidStack> validator) 
	{
		this(stack, Integer.MAX_VALUE, validator);
	}
	
	public FluidStackHolder(int stackLimit, @Nonnull Predicate<FluidStack> validator) 
	{
		this(FluidStack.EMPTY, stackLimit, validator);
	}
	
	public FluidStackHolder(@Nonnull FluidStack stack) 
	{
		this(stack, fluid -> true);
	}
	
	public FluidStackHolder(int stackLimit) 
	{
		this (stackLimit, fluid -> true);
	}
	
	public FluidStackHolder(@Nonnull Predicate<FluidStack> validator) 
	{
		this (FluidStack.EMPTY, validator);
	}
	
	public FluidStackHolder() 
	{
		this(FluidStack.EMPTY);
	}
	
	@Override
	public FluidStack getFluidStack() 
	{
		return stack;
	}

	@Override
	public void setFluidStack(FluidStack stack) 
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
		return stack.getAmount();
	}

	@Override
	public void setCount(int newCount) 
	{
		int limit = stackLimit;
		if (newCount <= 0)
			stack = FluidStack.EMPTY;
		else if (newCount > limit)
			stack.setAmount(limit);
		else
			stack.setAmount(newCount);
		onContentsChanged();
	}

	@Override
	public int modify(int amount) 
	{
		int newCount = stack.getAmount() + amount;

		if (newCount <= 0)
			stack = FluidStack.EMPTY;
		else if (newCount > stackLimit)
			stack.setAmount(stackLimit);
		else
			stack.setAmount(newCount);
		onContentsChanged();
		
		return newCount;
	}

	@Override
	public int getSizeLimit() 
	{
		return this.stackLimit;
	}

	@Override
	public boolean isEmpty() 
	{
		return stack.isEmpty();
	}

	@Override
	public int insert(FluidStack resource, FluidAction simulate) 
	{
        if (resource.isEmpty() || !isValid(resource))
        {
            return 0;
        }
        if (simulate.simulate())
        {
            if (stack.isEmpty())
            {
                return Math.min(stackLimit, resource.getAmount());
            }
            if (!stack.isFluidEqual(resource))
            {
                return 0;
            }
            return Math.min(stackLimit - stack.getAmount(), resource.getAmount());
        }
        if (stack.isEmpty())
        {
            stack = new FluidStack(resource, Math.min(stackLimit, resource.getAmount()));
            onContentsChanged();
            return stack.getAmount();
        }
        if (!stack.isFluidEqual(resource))
        {
            return 0;
        }
        int filled = stackLimit - stack.getAmount();

        if (resource.getAmount() < filled)
        {
            stack.grow(resource.getAmount());
            filled = resource.getAmount();
        }
        else
        {
            stack.setAmount(stackLimit);
        }
        if (filled > 0)
            onContentsChanged();
        return filled;
	}

	@Override
	public FluidStack extract(FluidStack resource, FluidAction simulate) 
	{
        if (resource.isEmpty() || !resource.isFluidEqual(stack))
        {
            return FluidStack.EMPTY;
        }
        return extract(resource.getAmount(), simulate);	
	}
	
	@Override
	public FluidStack extract(int amount, FluidAction simulate) 
	{
        int drained = amount;
        if (stack.getAmount() < drained)
        {
            drained = stack.getAmount();
        }
        FluidStack stack = new FluidStack(this.stack, drained);
        if (simulate.execute() && drained > 0)
        {
            this.stack.shrink(drained);
            onContentsChanged();
        }
        return stack;
	}

	@Override
	public void clear() 
	{
		stack = FluidStack.EMPTY;
		onContentsChanged();
	}

	@Override
	public IFluidStackAccess load(CompoundTag nbt) 
	{
		this.stackLimit = nbt.getInt(NDatabase.Capabilities.FluidHandler.FluidHolder.SLOT_LIMIT);
		this.stack = FluidStack.loadFluidStackFromNBT(nbt);
		
		return this;
	}

	@Override
	public CompoundTag save() 
	{
		CompoundTag nbt = new CompoundTag();
		
		stack.writeToNBT(nbt);
		nbt.putInt(NDatabase.Capabilities.FluidHandler.FluidHolder.SLOT_LIMIT, stackLimit);
		
		return nbt;
	}

	@Override
	public IFluidStackAccess setValidator(Predicate<FluidStack> validator) 
	{
		if (validator != null)
		{
			this.validator = validator;
		}
		
		return this;
	}

	@Override
	public boolean isValid(FluidStack stack) 
	{
		if (stack != null)
		{
			return validator.test(stack);
		}
		return false;	
	}
}

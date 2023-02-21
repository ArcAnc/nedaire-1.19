/**
 * @author ArcAnc
 * Created at: 2023-02-16
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.google.common.base.Predicate;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class NSimpleFluidStorage implements IFluidHandler, INBTSerializable<CompoundTag> 
{

	@Nullable
	protected IInventoryCallback tileInv;
	protected List<IFluidStackAccess> fluids;
	
	public NSimpleFluidStorage() 
	{
		this(null, 1);
	}
	
	public NSimpleFluidStorage(@Nullable IInventoryCallback tileInv)
	{
		this.tileInv = tileInv;
		fluids = new ArrayList<IFluidStackAccess>();
	}
	
	public NSimpleFluidStorage(@Nullable IInventoryCallback tileInv, int size)
	{
		this.tileInv = tileInv;
		fluids = Stream.generate(FluidStackHolder :: new).limit(size).collect(Collectors.toList());
	}
	
	public NSimpleFluidStorage(@Nullable IInventoryCallback tileInv, @Nonnull List<IFluidStackAccess> stacks)
	{
		this.tileInv = tileInv;
		fluids = stacks;
	}
	
    public NSimpleFluidStorage addTank(int capacity, Predicate<FluidStack> validator)
    {
    	return addTank(new FluidStackHolder(capacity, validator));
    }
    
    public NSimpleFluidStorage addTank (@Nonnull FluidStackHolder tank)
    {
    	this.fluids.add(tank);
    	return this;
    }
    
    public NSimpleFluidStorage addTank(@Nonnull List<IFluidStackAccess> tanks)
    {
    	this.fluids.addAll(tanks);
    	return this;
    }
	
	@Override
	public int getTanks() 
	{
		return fluids.size();
	}
	
	public List<IFluidStackAccess> getFluids() 
	{
		return fluids;
	}
	
	public IFluidStackAccess getTank (int tank)
	{
		validateTankIndex(tank);
		return fluids.get(tank);
	}
	
	@Override
	public FluidStack getFluidInTank(int tank) 
	{
		validateTankIndex(tank);
		return fluids.get(tank).getFluidStack();
	}
	
	public void setFluidInTank (@Nonnull FluidStack stack, int tank)
	{
		validateTankIndex(tank);
		fluids.get(tank).setFluidStack(stack);
		onTankChange(tank);
	}

	public int fill (int tank, FluidStack stack, FluidAction action) 
	{
		validateTankIndex(tank);
		int ret = fluids.get(tank).insert(stack, action); 
		
		if (action.execute())
			onTankChange(tank);
		
		return ret; 
	}
	
	@Override
	public int fill(FluidStack stack, FluidAction action) 
	{
		int ret = 0;
		FluidStack copy = stack.copy();
		
		for (int q = 0; q < fluids.size(); q++)
		{
			ret += fill(q, copy, action);
			copy = FluidHelper.copyFluidStackWithAmount(stack, stack.getAmount() - ret);
			if (copy.getAmount() <= 0)
				break;
		}
		
		return ret;
	}

	public FluidStack drain(int tank, FluidStack resource, FluidAction action)
	{
		validateTankIndex(tank);
		FluidStack ret = fluids.get(tank).extract(resource, action);
		if (action.execute())
			onTankChange(tank);
		return ret;
	}
	
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) 
	{
		if (resource.isEmpty())
			return FluidStack.EMPTY;
		FluidStack retStack = FluidStack.EMPTY;
		for (int q = 0; q < fluids.size(); q++)
		{
			int amount = resource.getAmount() - retStack.getAmount();
			if (amount <= 0)
				break;
			FluidStack tempStack = drain(q, FluidHelper.copyFluidStackWithAmount(resource, amount), FluidAction.SIMULATE);
			if (FluidHelper.canFluidStacksStack(tempStack, resource))
			{
				retStack = FluidHelper.copyFluidStackWithAmount(tempStack, retStack.getAmount() + tempStack.getAmount());
				if (action.execute())
				{
					drain(q, tempStack, action);
					onTankChange(q);
				}
			}
		}
		return retStack;
	}
	
	public FluidStack drain (int tank, int amount, FluidAction simulate)
	{
		validateTankIndex(tank);
		FluidStack ret = fluids.get(tank).extract(amount, simulate);
		if (simulate.execute())
			onTankChange(tank);
		return ret;
	}
	
	@Override
	public FluidStack drain(int amount, FluidAction simulate) 
	{
		return FluidStack.EMPTY;
	}

	@Override
	public int getTankCapacity(int tank) 
	{
		validateTankIndex(tank);
		return fluids.get(tank).getSizeLimit();
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) 
	{
		validateTankIndex(tank);
		return fluids.get(tank).isValid(stack);
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		
		if (!hasTanks())
		{
			return tag;
		}
		
		ListTag list = new ListTag();
		for (int q = 0; q < getTanks(); q++)
		{
			list.add(fluids.get(q).save());
		}
		
		tag.put(NDatabase.Capabilities.FluidHandler.TANKS, list);
		
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
        for (IFluidStackAccess slot : fluids) 
        {
            slot.clear();
        }
        ListTag list = nbt.getList(NDatabase.Capabilities.FluidHandler.TANKS, Tag.TAG_COMPOUND);
        for (int q = 0; q < list.size(); ++q) 
        {
        	fluids.get(q).load(list.getCompound(q));
        }
	}
	
	public void onTankChange(int tank)
	{
		if(tileInv == null)
			return;
		validateTankIndex(tank);
		tileInv.onTankChange(tank);
		fluids.get(tank).onContentsChanged();
	}
	
	public boolean hasTanks()
	{
		return getTanks() > 0 ;
	}
	
    protected void validateTankIndex(int tank)
    {
        if (tank < 0 || tank >= fluids.size())
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + fluids.size() + "]");
    }
}

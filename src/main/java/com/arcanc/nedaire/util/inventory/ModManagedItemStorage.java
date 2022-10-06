/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.inventory;

import java.util.List;

import javax.annotation.Nonnull;

import com.arcanc.nedaire.util.AccessType;
import com.google.common.base.Predicate;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class ModManagedItemStorage extends ModSimpleItemStorage 
{
	protected List<IItemStackAcess> inputSlots;
	protected List<IItemStackAcess> outputSlots;

	protected ModSimpleItemStorage inputHandler = new ModSimpleItemStorage();
	protected ModSimpleItemStorage outputHandler = new ModSimpleItemStorage();
	protected ModSimpleItemStorage fullHandler = new ModSimpleItemStorage();
	
	protected final LazyOptional<IItemHandler> input = LazyOptional.of(() -> inputHandler);
	protected final LazyOptional<IItemHandler> output = LazyOptional.of(() -> outputHandler);
	protected final LazyOptional<IItemHandler> full = LazyOptional.of(() -> fullHandler);

	public ModManagedItemStorage(@Nonnull List<IItemStackAcess> inputSlots, @Nonnull List<IItemStackAcess> outputSlots) 
	{
		super();
		this.inputSlots = inputSlots;
		this.outputSlots = outputSlots;
		
		this.items.addAll(inputSlots);
		this.items.addAll(outputSlots);
	}
	
	public ModManagedItemStorage addInputSlot(@Nonnull IItemStackAcess slot)
	{
		this.inputSlots.add(slot);
		
		this.items.add(inputSlots.size() - 1, slot);
		
		return this;
	}
	
	public ModManagedItemStorage addInputSlot(int capacity, Predicate<ItemStack> validator)
	{
		return addInputSlot(new ItemStackHolder(capacity, validator));
	}

	public ModManagedItemStorage addOutputSlot(@Nonnull IItemStackAcess slot)
	{
		this.outputSlots.add(slot);
		
		this.items.add(slot);
		
		return this;
	}
	
	public ModManagedItemStorage addOutputSlot(int capacity)
	{
		return addOutputSlot(new ItemStackHolder(capacity, val -> false));
	}
	
	public ModManagedItemStorage build()
	{
		inputHandler.addSlots(inputSlots);
		outputHandler.addSlots(outputSlots);
		fullHandler.addSlots(items);

		return this;
	}
	
	public void invalidate()
	{
		input.invalidate();
		output.invalidate();
		full.invalidate();
	}
	
	public ModSimpleItemStorage getFullHandler() 
	{
		return fullHandler;
	}
	
	public ModSimpleItemStorage getInputHandler() 
	{
		return inputHandler;
	}
	
	public ModSimpleItemStorage getOutputHandler() 
	{
		return outputHandler;
	}

	public LazyOptional<IItemHandler> getHandler(AccessType type)
	{
		switch (type)
		{
			case NONE:
				return LazyOptional.empty();
			case INPUT:
				return input;
			case OUTPUT:
				return output;
			case FULL:
				return full;
			default:
				return full;
		}
	}
	

}

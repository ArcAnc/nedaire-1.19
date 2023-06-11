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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class NManagedItemStorage extends NSimpleItemStorage 
{
	protected List<IItemStackAccess> inputSlots;
	protected List<IItemStackAccess> outputSlots;

	protected NSimpleItemStorage inputHandler = new NSimpleItemStorage();
	protected NSimpleItemStorage outputHandler = new NSimpleItemStorage();
	protected NSimpleItemStorage fullHandler = new NSimpleItemStorage();
	
	protected final LazyOptional<IItemHandler> input = LazyOptional.of(() -> inputHandler);
	protected final LazyOptional<IItemHandler> output = LazyOptional.of(() -> outputHandler);
	protected final LazyOptional<IItemHandler> full = LazyOptional.of(() -> fullHandler);

	public NManagedItemStorage(@Nullable IInventoryCallback tile)
	{
		super(tile);
		this.items = new ArrayList<>();
		this.inputSlots = new ArrayList<>();
		this.outputSlots = new ArrayList<>();
		
		this.inputHandler = new NSimpleItemStorage(tile);
		this.outputHandler = new NSimpleItemStorage(tile);
		this.fullHandler = new NSimpleItemStorage(tile);
	}
	
	public NManagedItemStorage(@Nonnull List<IItemStackAccess> inputSlots, @Nonnull List<IItemStackAccess> outputSlots) 
	{
		super();
		this.inputSlots = inputSlots;
		this.outputSlots = outputSlots;
		
		this.items.addAll(inputSlots);
		this.items.addAll(outputSlots);
	}

	public NManagedItemStorage addInputSlot(@Nonnull IItemStackAccess slot)
	{
		this.inputSlots.add(slot);
		
		this.items.add(inputSlots.size() - 1, slot);
		
		return this;
	}
	
	public NManagedItemStorage addInputSlot(int capacity, Predicate<ItemStack> validator)
	{
		return addInputSlot(new ItemStackHolder(capacity, validator));
	}

	public NManagedItemStorage addOutputSlot(int capacity, Predicate<ItemStack> validator)
	{
		return addOutputSlot(new ItemStackHolder(capacity, validator));
	}
	
	public NManagedItemStorage addOutputSlot(@Nonnull IItemStackAccess slot)
	{
		this.outputSlots.add(slot);
		
		this.items.add(slot);
		
		return this;
	}
	
	public NManagedItemStorage addOutputSlot(int capacity)
	{
		return addOutputSlot(capacity, val -> false);
	}
	
	public NManagedItemStorage build()
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
	
	public NSimpleItemStorage getFullHandler() 
	{
		return fullHandler;
	}
	
	public NSimpleItemStorage getInputHandler() 
	{
		return inputHandler;
	}
	
	public NSimpleItemStorage getOutputHandler() 
	{
		return outputHandler;
	}

	public LazyOptional<IItemHandler> getHandler(AccessType type)
	{
		return switch (type)
		{
			case NONE -> LazyOptional.empty();
			case INPUT -> input;
			case OUTPUT -> output;
			case FULL -> full;
			default -> full;
		};
	}
	
	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag tag = super.serializeNBT();
		
		tag.putInt(NDatabase.Capabilities.ItemHandler.ManagedInventory.DIVIDER_INDEX, inputHandler.getSlots());
		
		return tag;	
	}
	
	
	@Override
	public void deserializeNBT(CompoundTag tag)
	{
		
		this.inputHandler = new NSimpleItemStorage(tileInv);
		this.outputHandler = new NSimpleItemStorage(tileInv);
		this.fullHandler = new NSimpleItemStorage(tileInv);
		this.inputSlots.clear();
		this.outputSlots.clear();
		
		super.deserializeNBT(tag);
		
		int div = tag.getInt(NDatabase.Capabilities.ItemHandler.ManagedInventory.DIVIDER_INDEX);
		
		for (int q = 0 ; q < this.items.size(); q++)
		{
			if(q < div)
				this.inputSlots.add(items.get(q));
			else
				this.outputSlots.add(items.get(q));
		}
		
		build();
	}
}

/**
 * @author ArcAnc
 * Created at: 2023-01-11
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.filter;

import com.arcanc.nedaire.content.capabilities.vim.IVim;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public interface IFilter<Inv, FFilt, T> extends INBTSerializable<CompoundTag>
{
	/**
	 * Return true if obj is eligible for next manipulations. If contains in whitelist or missing is blacklist
	 */
	boolean filter (T obj);
	
	boolean isWhitelist();
	
	void setWhitelist(boolean value);
	
	boolean filterWhiteList(T obj);
	
	boolean isModOwner();
	
	void setModOwner(boolean value);
	
	boolean filterModOwner(T obj);
	
	boolean isCheckTag();
	
	void setCheckTag(boolean value);

	boolean filterCheckTag(T obj);
	
	int getExtraction();
	
	void setExtracion(int value);
	
	boolean filterExtraction(FFilt tileInv, T obj);
	
	int getMaxInInventory();
	
	void setMaxInInventory(int value);
	
	boolean filterMaxInInventory(FFilt tileInv, T obj);
	
	Inv getContent();
	
	interface IItemFilter extends IFilter<IItemHandler, IItemHandler, ItemStack>{}
	interface IFluidFilter extends IFilter<IItemHandler, IFluidHandler, FluidStack>{}
	interface IVimFilter extends IFilter<IVim, IVim, Integer>{}
	
}

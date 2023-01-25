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
import net.minecraftforge.items.IItemHandler;

public interface IFilter<Inv, T> extends INBTSerializable<CompoundTag>
{
	/**
	 * Return true if obj is eligible for next manipulations. If contains in whitelist or missing is blacklist
	 */
	boolean filter (T obj);
	
	boolean isWhitelist();
	
	void setWhitelist(boolean value);
	
	boolean isModOwner();
	
	void setModOwner(boolean value);
	
	boolean isCheckTag();
	
	void setCheckTag(boolean value);
	
	int getExtraction();
	
	void setExtracion(int value);
	
	int getMaxInInventory();
	
	void setMaxInInventory(int value);
	
	Inv getContent();
	
	interface IItemFilter extends IFilter<IItemHandler, ItemStack>{}
	interface IFluidFilter extends IFilter<IItemHandler, FluidStack>{}
	interface IVimFilter extends IFilter<IVim, Integer>{}
	
}

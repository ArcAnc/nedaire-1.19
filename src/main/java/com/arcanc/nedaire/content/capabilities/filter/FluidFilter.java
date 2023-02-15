/**
 * @author ArcAnc
 * Created at: 2023-02-15
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.filter;

import com.arcanc.nedaire.content.capabilities.filter.IFilter.IFluidFilter;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

public class FluidFilter implements IFluidFilter 
{

	/**
	 * FIXME: add custom fluid storage code
	 */
	//	public NSimpleItemStorage content;
	public boolean whitelist = true;
	public boolean modOwner = false;
	public boolean checkTag = false;
	public int extraction = 0;
	public int maxInInventory = 0;
	
	@Override
	public boolean filter(FluidStack obj) 
	{
		return false;
	}

	@Override
	public boolean isWhitelist() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWhitelist(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean filterWhiteList(FluidStack obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isModOwner() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setModOwner(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean filterModOwner(FluidStack obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCheckTag() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCheckTag(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean filterCheckTag(FluidStack obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getExtraction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setExtracion(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int filterExtraction(IItemHandler tileInv, FluidStack obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxInInventory(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int filterMaxInInventory(IItemHandler tileInv, FluidStack obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IItemHandler getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompoundTag serializeNBT() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		// TODO Auto-generated method stub

	}

}

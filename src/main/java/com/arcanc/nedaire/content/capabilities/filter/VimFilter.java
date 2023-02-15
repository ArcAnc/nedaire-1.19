/**
 * @author ArcAnc
 * Created at: 2023-02-15
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.filter;

import com.arcanc.nedaire.content.capabilities.filter.IFilter.IVimFilter;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.VimHelper;

import net.minecraft.nbt.CompoundTag;

public class VimFilter implements IVimFilter 
{
	public boolean whitelist = true;
	public boolean modOwner = false;
	public boolean checkTag = false;
	public int extraction = 0;
	public int maxInInventory = 0;
	
	public VimFilter(int extraction, int maxInInventory) 
	{
		this.extraction = extraction;
		this.maxInInventory = maxInInventory;
	}
	
	@Override
	public boolean filter(Integer obj) 
	{
		return obj > 0;
	}

	@Override
	public boolean isWhitelist() 
	{
		return this.whitelist;
	}

	@Override
	public void setWhitelist(boolean value) 
	{
		this.whitelist = value;
	}

	@Override
	public boolean filterWhiteList(Integer obj) 
	{
		return true;
	}

	@Override
	public boolean isModOwner() 
	{
		return this.modOwner;
	}

	@Override
	public void setModOwner(boolean value) 
	{
		this.modOwner = value;
	}

	@Override
	public boolean filterModOwner(Integer obj) 
	{
		return true;
	}

	@Override
	public boolean isCheckTag() 
	{
		return this.checkTag;
	}

	@Override
	public void setCheckTag(boolean value) 
	{
		this.checkTag = value;
	}

	@Override
	public boolean filterCheckTag(Integer obj) 
	{
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
	public int filterExtraction(IVim tileInv, Integer obj) 
	{
		int emptySpace = VimHelper.getEmptySpace(tileInv);
		return Math.min(emptySpace, extraction);
	}

	@Override
	public int getMaxInInventory() 
	{
		return this.maxInInventory;
	}

	@Override
	public void setMaxInInventory(int value) 
	{
		this.maxInInventory = value;
	}

	@Override
	public int filterMaxInInventory(IVim tileInv, Integer obj) 
	{
		int ret = tileInv.getEnergyStored() - maxInInventory;
		return ret > 0 ? ret : 0;
	}

	@Override
	public IVim getContent() 
	{
		return null;
	}

	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		
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
		whitelist = nbt.getBoolean(NDatabase.Capabilities.Filter.WHITELIST);
		modOwner = nbt.getBoolean(NDatabase.Capabilities.Filter.MOD_OWNER);
		checkTag = nbt.getBoolean(NDatabase.Capabilities.Filter.CHECK_TAG);
		extraction = nbt.getInt(NDatabase.Capabilities.Filter.MAX_EXTRACTING_STACK);
		maxInInventory = nbt.getInt(NDatabase.Capabilities.Filter.MAX_AMOUNT_IN);
	}

}

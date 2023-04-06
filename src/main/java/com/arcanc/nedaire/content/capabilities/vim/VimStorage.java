/**
 * @author ArcAnc
 * Created at: 2022-04-23
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.vim;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.util.database.NDatabase;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;

public class VimStorage implements IVim
{

	protected int energy;
	protected int maxEnergy;
	protected int extracting;
	protected int input;
	protected IInventoryCallback tile;
	
	private VimStorage (VimStorage.Config config)
	{
		this.energy = config.energy;
		this.maxEnergy = config.maxEnergy;
		this.extracting = config.extracting;
		this.input = config.input;
		this.tile = config.tile;
	}
	
	@Override
	public int add(int count, boolean simulate) 
	{
		int returned = 0;
		
		if (energy < maxEnergy)
		{
			if (isAllowedReceiving())
			{
				returned = Math.min(maxEnergy - energy, Math.min(input, count));
				if (!simulate)
				{
					energy += returned;
					onVimChange();
				}
			}
		}
		return returned;
	}

	@Override
	public int extract(int count, boolean simulate) 
	{
		int returned = 0;
		
		if (energy > 0)
		{
			if (isAllowedExtraction())
			{
				returned = Math.min(energy, Math.min(extracting, count));
				if (!simulate)
				{
					energy -= returned;
					onVimChange();
				}
			}
		}
		return returned;
	}

	@Override
	public int getEnergyStored() 
	{
		return energy;
	}

	@Override
	public int getMaxEnergyStored() 
	{
		return maxEnergy;
	}

	@Override
	public void setEnergyStored(int count) 
	{
		if (count > 0 && count <= maxEnergy)
			this.energy = count;
	}

	@Override
	public void setMaxEnergyStored(int count) 
	{
		if (count > 0)
			this.maxEnergy = count;
	}
	
	@Override
	public boolean isAllowedExtraction() 
	{
		return extracting > 0;
	}

	@Override
	public boolean isAllowedReceiving() 
	{
		return input > 0;
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt(NDatabase.Capabilities.Vim.ENERGY, getEnergyStored());
		tag.putInt(NDatabase.Capabilities.Vim.MAX_ENERGY, getMaxEnergyStored());
		tag.putInt(NDatabase.Capabilities.Vim.EXTRACT, extracting);
		tag.putInt(NDatabase.Capabilities.Vim.INPUT, input);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) 
	{
		this.energy = 		tag.getInt(NDatabase.Capabilities.Vim.ENERGY);
		this.maxEnergy = 	tag.getInt(NDatabase.Capabilities.Vim.MAX_ENERGY);
		this.extracting = 	tag.getInt(NDatabase.Capabilities.Vim.EXTRACT);
		this.input = 		tag.getInt(NDatabase.Capabilities.Vim.INPUT);
		onVimChange();
	}
	
	public void onVimChange()
	{
		if (tile == null)
			return;
		tile.onVimChange();
	}
	
	public static VimStorage.Config newConfig (@Nullable IInventoryCallback tile)
	{
		return new Config(tile);
	}
	
	public static class Config 
	{
		private int energy;
		private int maxEnergy;
		private int extracting = Integer.MAX_VALUE;
		private int input = Integer.MAX_VALUE;
		private IInventoryCallback tile;
		
		private Config(IInventoryCallback tile) 
		{
			this.tile = tile;
		}
		
		/**
		 * @param energy the energy to set
		 */
		public Config setEnergy(int energy) 
		{
			this.energy = energy;
			return this;
		}
		
		/**
		 * @param input the input to set
		 */
		public Config setInput(int input) 
		{
			this.input = input;
			return this;
		}

		/**
		 * @param maxEnergy the maxEnergy to set
		 */
		public Config setMaxEnergy(int maxEnergy)
		{
			this.maxEnergy = maxEnergy;
			return this;
		}
		
		/**
		 * @param extracting the extracting to set
		 */
		public Config setExtracting(int extracting) 
		{
			this.extracting = extracting;
			return this;
		}
		
		public VimStorage build ()
		{
			return new VimStorage(this);
		}
		
	}
}

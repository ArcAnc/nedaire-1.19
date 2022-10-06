/**
 * @author ArcAnc
 * Created at: 2022-04-23
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.vim;

import com.arcanc.nedaire.util.database.ModDatabase;

import net.minecraft.nbt.CompoundTag;

public class VimStorage implements IVim
{

	protected int energy;
	protected int maxEnergy;
	protected int extracting;
	protected int input;
	
	private VimStorage (VimStorage.Config config)
	{
		this.energy = config.energy;
		this.maxEnergy = config.maxEnergy;
		this.extracting = config.extracting;
		this.input = config.input;
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
		tag.putInt(ModDatabase.Capabilities.Vim.ENERGY, getEnergyStored());
		tag.putInt(ModDatabase.Capabilities.Vim.MAX_ENERGY, getMaxEnergyStored());
		tag.putInt(ModDatabase.Capabilities.Vim.EXTRACT, extracting);
		tag.putInt(ModDatabase.Capabilities.Vim.INPUT, input);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) 
	{
		this.energy = 		tag.getInt(ModDatabase.Capabilities.Vim.ENERGY);
		this.maxEnergy = 	tag.getInt(ModDatabase.Capabilities.Vim.MAX_ENERGY);
		this.extracting = 	tag.getInt(ModDatabase.Capabilities.Vim.EXTRACT);
		this.input = 		tag.getInt(ModDatabase.Capabilities.Vim.INPUT);
	}
	
	public static VimStorage.Config newConfig ()
	{
		return new Config();
	}
	
	public static class Config 
	{
		private int energy;
		private int maxEnergy;
		private int extracting;
		private int input;
		
		
		private Config() 
		{
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

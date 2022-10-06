/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.util.database.ModDatabase;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ModBERedstoneSensitive extends ModBaseBlockEntity 
{

	private int currentRedstoneMod = 2;
	
	public ModBERedstoneSensitive(BlockEntityType<?> type, BlockPos pos, BlockState state) 
	{
		super(type, pos, state);
	}

	public boolean isPowered()
	{
		if (currentRedstoneMod == 0 && getLevel().hasNeighborSignal(getBlockPos()))
			return true;
		else if (currentRedstoneMod == 1 && !getLevel().hasNeighborSignal(getBlockPos()))
			return true;
		else if (currentRedstoneMod == 2)
			return true;
		return false;
	}
	
	/**
	 * 0 - required redstone
	 * 1 - required disabled redstone
	 * 2 - ignore all redstone
	 */
	public void setCurrentRedstoneMod(int currentRedstoneMod) 
	{
		this.currentRedstoneMod = currentRedstoneMod;
	}
	
	/**
	 * 0 - required redstone
	 * 1 - required disabled redstone
	 * 2 - ignore all redstone
	 */
	public int getCurrentRedstoneMod() 
	{
		return currentRedstoneMod;
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		
		if (tag.contains(ModDatabase.Blocks.BlockEntities.TagAddress.Machines.RedstoneSensitive.REDSTONE_MOD))
		{
			this.currentRedstoneMod = tag.getInt(ModDatabase.Blocks.BlockEntities.TagAddress.Machines.RedstoneSensitive.REDSTONE_MOD);
		}
		
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		tag.putInt(ModDatabase.Blocks.BlockEntities.TagAddress.Machines.RedstoneSensitive.REDSTONE_MOD, this.currentRedstoneMod);
	}

}

/**
 * @author ArcAnc
 * Created at: 2023-03-08
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class NBEJewelryTable extends NBaseBlockEntity 
{

	public NBEJewelryTable(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_JEWELRY_TABLE.get(), pos, state);
	}

	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{

	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{

	}

}

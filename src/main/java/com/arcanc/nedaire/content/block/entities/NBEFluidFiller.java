/**
 * @author ArcAnc
 * Created at: 2023-03-10
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.AccessType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class NBEFluidFiller extends NBERedstoneSensitive 
{

	public NBEFluidFiller(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_FLUID_FILLER.get(), pos, state);

		ports.put(Direction.UP, AccessType.FULL);
		ports.put(Direction.DOWN, AccessType.FULL);
	}

}

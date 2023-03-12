/**
 * @author ArcAnc
 * Created at: 2023-03-12
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NBEDiffuser extends NBERedstoneSensitive 
{

	public NBEDiffuser(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_DIFFUSER.get(), pos, state);
	}

}

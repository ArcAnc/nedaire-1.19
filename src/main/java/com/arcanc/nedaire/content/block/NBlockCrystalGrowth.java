/**
 * @author ArcAnc
 * Created at: 2023-03-07
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBECrystalGrowth;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;

public class NBlockCrystalGrowth extends NTileProviderBlock<NBECrystalGrowth> 
{

	public NBlockCrystalGrowth(Properties properties) 
	{
		super(properties, NRegistration.RegisterBlockEntities.BE_CRYSTAL_GROWTH);
	}
	
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) 
	{
		builder.add(BlockHelper.BlockProperties.WATERLOGGED);
	}
}

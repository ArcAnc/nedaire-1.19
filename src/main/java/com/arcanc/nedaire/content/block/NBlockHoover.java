/**
 * @author ArcAnc
 * Created at: 2022-11-02
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEHoover;
import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NBlockHoover extends NTileProviderBlock<NBEHoover>
{

	private static final VoxelShape SHAPE = Shapes.or(
			box(0, 0, 0, 16, 7, 16),
			box(3, 7, 0, 13, 16, 3),
			box(3, 7, 13, 13, 16, 16),
			box(0, 7, 0, 3, 16, 16),
			box(13, 7, 0, 16, 16, 16),
			box(3, 7, 10, 13, 11, 13),
			box(3, 7, 3, 13, 11, 6),
			box(10, 7, 6, 13, 11, 10),
			box(3, 7, 6, 6, 11, 10));
	
	public NBlockHoover(Properties props) 
	{
		super(props, NRegistration.RegisterBlockEntities.BE_HOOVER);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) 
	{
		return SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) 
	{
		return SHAPE;
	}
	
	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) 
	{
		return SHAPE;
	}
}

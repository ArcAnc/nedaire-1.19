/**
 * @author ArcAnc
 * Created at: 2023-03-25
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBETerramorfer;
import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NBlockCore extends NTileProviderBlock<NBETerramorfer> 
{

	private static final VoxelShape SHAPE = Shapes.block();
	
	public NBlockCore(Properties properties) 
	{
		super(properties, NRegistration.RegisterBlockEntities.BE_CORE);
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
	
	@Override
	public RenderShape getRenderShape(BlockState state) 
	{
		return RenderShape.INVISIBLE;
	}
}

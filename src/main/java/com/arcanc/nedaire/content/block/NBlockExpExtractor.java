/**
 * @author ArcAnc
 * Created at: 2023-03-24
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEExpExtractor;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NBlockExpExtractor extends NTileProviderBlock<NBEExpExtractor> 
{
	private static final VoxelShape SHAPE = Shapes.or(
			box(0, 0, 1, 16, 1, 15),
			box(0, 1, 6, 1, 16, 10),
			box(15, 1, 6, 16, 16, 10));
	
	private static final VoxelShape ROTATED_SHAPE = Shapes.or(
			box(1, 0, 0, 15, 1, 16),
			box(6, 1, 15, 10, 16, 16),
			box(6, 1, 0, 10, 16, 1));
	   
	public NBlockExpExtractor(Properties properties) 
	{
		super(properties, NRegistration.RegisterBlockEntities.BE_EXP_EXTRACTOR);
	}
	
	@Override
	protected BlockState getInitDefaultState() 
	{
		BlockState state = super.getInitDefaultState();
		if (state.hasProperty(BlockHelper.BlockProperties.HORIZONTAL_FACING))
		{
			state = state.setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(BlockHelper.BlockProperties.LIT, false);
		}
		return state;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) 
	{
		return super.getStateForPlacement(context).setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot) 
	{
		return state.setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, rot.rotate(state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING)));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) 
	{
		return state.rotate(mirror.getRotation(state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING)));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) 
	{
		builder.add(BlockHelper.BlockProperties.HORIZONTAL_FACING, BlockHelper.BlockProperties.WATERLOGGED, BlockHelper.BlockProperties.LIT);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) 
	{
		return state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING).getAxis()  == Direction.Axis.Z ? SHAPE : ROTATED_SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) 
	{
		return state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING).getAxis()  == Direction.Axis.Z ? SHAPE : ROTATED_SHAPE;
	}
	
	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) 
	{
		return state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING).getAxis()  == Direction.Axis.Z ? SHAPE : ROTATED_SHAPE;
	}
}

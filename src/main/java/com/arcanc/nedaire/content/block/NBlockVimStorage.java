/**
 * @author ArcAnc
 * Created at: 2022-10-29
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEVimStorage;
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
import org.jetbrains.annotations.NotNull;

public class NBlockVimStorage extends NTileProviderBlock<NBEVimStorage> 
{

	private static final VoxelShape SHAPE = Shapes.or(
			box(0, 0, 2, 4, 16, 14), 
			box(12, 0, 2, 16, 16, 14),
			box(6, 0, 2, 10, 16, 14),
			box(4, 10, 5, 6, 15, 11),
			box(4, 1, 5, 6, 6, 11),
			box(10, 10, 5, 12, 15, 11),
			box(10, 1, 5, 12, 6, 11));
	
	private static final VoxelShape ROTATED_SHAPE = Shapes.or(
			box(2, 0, 12, 14, 16, 16), 
			box(2, 0, 0, 14, 16, 4),
			box(2, 0, 6, 14, 16, 10),
			box(5, 10, 10, 11, 15, 12),
			box(5, 1, 10, 11, 6, 12),
			box(5, 10, 4, 11, 15, 4),
			box(5, 1, 4, 11, 6, 6)); 
	
	public NBlockVimStorage(Properties props) 
	{
		super (props, NRegistration.RegisterBlockEntities.BE_VIM_STORAGE);
	}
	
	@Override
	protected BlockState getInitDefaultState() 
	{
		BlockState state = super.getInitDefaultState();
		if (state.hasProperty(BlockHelper.BlockProperties.HORIZONTAL_FACING))
		{
			state = state.setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, Direction.SOUTH);
		}
		return state;
	}

	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context)
	{
		return super.getStateForPlacement(context).setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull BlockState rotate(BlockState state, Rotation rot)
	{
		return state.setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, rot.rotate(state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING)));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public @NotNull BlockState mirror(BlockState state, Mirror mirror)
	{
		return state.rotate(mirror.getRotation(state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING)));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) 
	{
		builder.add(BlockHelper.BlockProperties.HORIZONTAL_FACING, BlockHelper.BlockProperties.WATERLOGGED);
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context)
	{
		return state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING).getAxis()  == Direction.Axis.Z ? SHAPE : ROTATED_SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context)
	{
		return state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING).getAxis()  == Direction.Axis.Z ? SHAPE : ROTATED_SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getInteractionShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos)
	{
		return state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING).getAxis()  == Direction.Axis.Z ? SHAPE : ROTATED_SHAPE;
	}
}

/**
 * @author ArcAnc
 * Created at: 2023-03-03
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEGeneratorMob;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NBlockGeneratorMob extends NTileProviderBlock<NBEGeneratorMob> 
{
	private static final VoxelShape SHAPE = Shapes.block();
	
	public NBlockGeneratorMob(Properties properties) 
	{
		super(properties, NRegistration.RegisterBlockEntities.BE_GENERATOR_MOB);
	}

	@Override
	protected BlockState getInitDefaultState() 
	{
		BlockState state = super.getInitDefaultState();
		if (state.hasProperty(BlockHelper.BlockProperties.FACING))
		{
			state = state.setValue(BlockHelper.BlockProperties.FACING, Direction.SOUTH).setValue(BlockHelper.BlockProperties.LIT, false);
		}
		return state;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) 
	{
		return super.getStateForPlacement(context).setValue(BlockHelper.BlockProperties.FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot) 
	{
		return state.setValue(BlockHelper.BlockProperties.FACING, rot.rotate(state.getValue(BlockHelper.BlockProperties.FACING)));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) 
	{
		return state.rotate(mirror.getRotation(state.getValue(BlockHelper.BlockProperties.FACING)));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) 
	{
		return RenderShape.MODEL;
	}
	
	@Override
	public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean update) 
	{
		if (!level.isClientSide() && !oldState.is(newState.getBlock())) 
		{
			BlockHelper.castTileEntity(level, pos, NBEGeneratorMob.class).ifPresent(ent -> 	
			{
				ItemHelper.dropContents(level, pos, ItemHelper.getItemHandler(ent)); 
			});
		super.onRemove(oldState, level, pos, newState, update);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) 
	{
		builder.add(BlockHelper.BlockProperties.FACING, BlockHelper.BlockProperties.LIT);
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

/**
 * @author ArcAnc
 * Created at: 2023-03-10
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEFluidFiller;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NBlockFluidFiller extends NTileProviderBlock<NBEFluidFiller> 
{

	private static final VoxelShape SHAPE = Shapes.block();
	
	public NBlockFluidFiller(Properties properties) 
	{
		super(properties, NRegistration.RegisterBlockEntities.BE_FLUID_FILLER);
	}

	@Override
	protected BlockState getInitDefaultState() 
	{
		BlockState state = super.getInitDefaultState();
		if (state.hasProperty(BlockHelper.BlockProperties.ENABLED))
		{
			state = state.setValue(BlockHelper.BlockProperties.ENABLED, false);
		}
		return state;
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
			BlockHelper.castTileEntity(level, pos, NBEFluidFiller.class).ifPresent(ent -> 	
			{
				ItemHelper.dropContents(level, pos, ItemHelper.getItemHandler(ent)); 
			});
		super.onRemove(oldState, level, pos, newState, update);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) 
	{
		builder.add(BlockHelper.BlockProperties.ENABLED);
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

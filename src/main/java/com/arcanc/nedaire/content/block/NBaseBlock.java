/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.util.helpers.BlockHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.ticks.ScheduledTick;

public class NBaseBlock extends Block implements SimpleWaterloggedBlock
{
	public NBaseBlock(Properties properties) 
	{
		super(properties);
		
		this.registerDefaultState(getInitDefaultState());
	}
	
	protected BlockState getInitDefaultState ()
	{
		BlockState state = this.stateDefinition.any();
		if (state.hasProperty(BlockHelper.BlockProperties.WATERLOGGED))
		{
			state = state.setValue(BlockHelper.BlockProperties.WATERLOGGED, Boolean.FALSE);
		}
		return state;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventId, int eventParam) 
	{
		if (world.isClientSide() && eventId == 255)
		{
			world.sendBlockUpdated(pos, state, state, 3);
			return true;
		}
		return super.triggerEvent(state, world, pos, eventId, eventParam);
	}
	
	@Override
	public String getDescriptionId() 
	{
		return BlockHelper.getRegistryName(this).toString().replace(':', '.').replace(':', '.').replace('/', '.');
	}
	
	/**
	 * WATERLOGGING
	 */
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) 
	{
		BlockState state = this.defaultBlockState();
		if (state.hasProperty(BlockHelper.BlockProperties.WATERLOGGED))
		{
			return state.setValue(BlockHelper.BlockProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
		}
		return state;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) 
	{
		if (state.hasProperty(BlockHelper.BlockProperties.WATERLOGGED) && state.getValue(BlockHelper.BlockProperties.WATERLOGGED))
		{
			world.getFluidTicks().schedule(new ScheduledTick<>(Fluids.WATER, currentPos, Fluids.WATER.getTickDelay(world), 0));
		}
		return super.updateShape(state, dir, facingState, world, currentPos, facingPos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state) 
	{
		if (state.hasProperty(BlockHelper.BlockProperties.WATERLOGGED) && state.getValue(BlockHelper.BlockProperties.WATERLOGGED))
		{
			return Fluids.WATER.getSource(false);
		}
		return super.getFluidState(state);
	}
	
	@Override
	public boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid liquid) 
	{
		return state.hasProperty(BlockHelper.BlockProperties.WATERLOGGED) && SimpleWaterloggedBlock.super.canPlaceLiquid(world, pos, state, liquid);
	}
	
	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState liquid) 
	{
		return state.hasProperty(BlockHelper.BlockProperties.WATERLOGGED) && SimpleWaterloggedBlock.super.placeLiquid(world, pos, state, liquid);
	}

	@Override
	public ItemStack pickupBlock(LevelAccessor world, BlockPos pos, BlockState state) 
	{
		if (state.hasProperty(BlockHelper.BlockProperties.WATERLOGGED))
		{
			return SimpleWaterloggedBlock.super.pickupBlock(world, pos, state);
		}
		return ItemStack.EMPTY;
	}
}

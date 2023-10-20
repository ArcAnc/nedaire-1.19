/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.util.helpers.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.ticks.ScheduledTick;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NBlockOre extends DropExperienceBlock implements SimpleWaterloggedBlock
{
	
	public NBlockOre(Properties properties) 
	{
		this(properties, UniformInt.of(0, 0));
	}
	
	public NBlockOre(Properties properties, UniformInt range) 
	{
		super(properties, range);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean triggerEvent(@NotNull BlockState state, Level world, @NotNull BlockPos pos, int eventId, int eventParam)
	{
		if (world.isClientSide() && eventId == 255)
		{
			world.sendBlockUpdated(pos, state, state, 3);
			return true;
		}
		return super.triggerEvent(state, world, pos, eventId, eventParam);
	}
	
	@Override
	public @NotNull String getDescriptionId()
	{
		return BlockHelper.getRegistryName(this).toString().replace(':', '.').replace(':', '.').replace('/', '.');
	}
	
	/**
	 * WATERLOGGING
	 */
	
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context)
	{
		BlockState state = this.defaultBlockState();
		if (state.hasProperty(BlockStateProperties.WATERLOGGED))
		{
			return state.setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
		}
		return state;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public @NotNull BlockState updateShape(BlockState state, @NotNull Direction dir, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos)
	{
		if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))
		{
			world.getFluidTicks().schedule(new ScheduledTick<>(Fluids.WATER, currentPos, Fluids.WATER.getTickDelay(world), 0));
		}
		return super.updateShape(state, dir, facingState, world, currentPos, facingPos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public @NotNull FluidState getFluidState(BlockState state)
	{
		if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))
		{
			return Fluids.WATER.getSource(false);
		}
		return super.getFluidState(state);
	}
	
	@Override
	public boolean canPlaceLiquid(@Nullable Player player, @NotNull BlockGetter world, @NotNull BlockPos pos, BlockState state, @NotNull Fluid liquid)
	{
		return state.hasProperty(BlockStateProperties.WATERLOGGED) && SimpleWaterloggedBlock.super.canPlaceLiquid(player, world, pos, state, liquid);
	}
	
	@Override
	public boolean placeLiquid(@NotNull LevelAccessor world, @NotNull BlockPos pos, BlockState state, @NotNull FluidState liquid)
	{
		return state.hasProperty(BlockStateProperties.WATERLOGGED) && SimpleWaterloggedBlock.super.placeLiquid(world, pos, state, liquid);
	}

	@Override
	public @NotNull ItemStack pickupBlock(@Nullable Player player, @NotNull LevelAccessor world, @NotNull BlockPos pos, BlockState state)
	{
		if (state.hasProperty(BlockStateProperties.WATERLOGGED))
		{
			return SimpleWaterloggedBlock.super.pickupBlock(player, world, pos, state);
		}
		return ItemStack.EMPTY;
	}
}

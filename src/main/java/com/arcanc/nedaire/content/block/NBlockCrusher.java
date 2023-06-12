/**
 * @author ArcAnc
 * Created at: 2023-03-06
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBECrusher;
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
import org.jetbrains.annotations.NotNull;

public class NBlockCrusher extends NTileProviderBlock<NBECrusher> 
{

	private static final VoxelShape SHAPE = Shapes.block();
	
	public NBlockCrusher(Properties properties) 
	{
		super(properties, NRegistration.RegisterBlockEntities.BE_CRUSHER);
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

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull RenderShape getRenderShape(@NotNull BlockState state)
	{
		return RenderShape.MODEL;
	}
	
	@Override
	public void onRemove(@NotNull BlockState oldState, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean update)
	{
		if (!level.isClientSide() && !oldState.is(newState.getBlock())) 
		{
			BlockHelper.castTileEntity(level, pos, NBECrusher.class).ifPresent(ent -> 	
			{
				ItemHelper.dropContents(level, pos, ItemHelper.getItemHandler(ent)); 
			});
		super.onRemove(oldState, level, pos, newState, update);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) 
	{
		builder.add(BlockHelper.BlockProperties.HORIZONTAL_FACING, BlockHelper.BlockProperties.LIT);
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context)
	{
		return SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context)
	{
		return SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos)
	{
		return SHAPE;
	}

}

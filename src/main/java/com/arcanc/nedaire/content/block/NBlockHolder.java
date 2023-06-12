/**
 * @author ArcAnc
 * Created at: 2022-04-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import java.util.function.BiFunction;

import com.arcanc.nedaire.content.block.entities.NBEHolder;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class NBlockHolder extends NTileProviderBlock<NBEHolder> 
{
	private final VoxelShape SHAPE = Shapes.or(
			box(0, 10, 0, 16, 16, 16),
			box(4, 9, 4, 12, 10, 12),
			box(5, 8, 12, 11, 10, 13),
			box(5, 8, 3, 11, 10, 4),
			box(12, 8, 5, 13, 10, 11),
			box(3, 8, 5, 4, 10, 11),
			box(11, 8, 4, 12, 10, 5),
			box(11, 8, 11, 12, 10, 12),
			box(4, 8, 11, 5, 10, 12),
			box(4, 8, 4, 5, 10, 5));
	
	public NBlockHolder(Properties properties, BiFunction<BlockPos, BlockState, NBEHolder> tile) 
	{
		super(properties, tile);
	}

	public NBlockHolder(Properties props)
	{
		super(props, NRegistration.RegisterBlockEntities.BE_HOLDER);
	}
	
	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult rayTrace)
	{
		if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND)
		{
			ItemStack stack = player.getItemInHand(hand);
			BlockHelper.castTileEntity(world, pos, NBEHolder.class).ifPresent( tile ->
			{
				tile.useHolder(player, stack);
			});
			return InteractionResult.CONSUME;
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public void onRemove(@NotNull BlockState oldState, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean update)
	{
		if (!level.isClientSide && !oldState.is(newState.getBlock())) 
		{
			BlockHelper.getTileEntity(level, pos).ifPresent(ent -> 
			{
				ItemHelper.dropContents(level, pos, ItemHelper.getItemHandler(ent));			
			});
		super.onRemove(oldState, level, pos, newState, update);
		}
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

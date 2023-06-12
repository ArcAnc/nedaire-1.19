/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import java.util.function.BiFunction;

import com.arcanc.nedaire.content.block.entities.NBEPedestal;
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

public class NBlockPedestal extends NTileProviderBlock<NBEPedestal> 
{
	private static final VoxelShape SHAPE = Shapes.or(
			box(0, 0, 0, 16, 3, 16), 
			box(2, 11, 2, 14, 14, 14), 
			box(4, 3, 4, 12, 11, 12),
			box(4, 14, 4, 12, 15, 12),
			box(5, 14, 3, 11, 16, 4),
			box(5, 14, 12, 11, 16, 13),
			box(12, 14, 5, 13, 16, 11),
			box(3, 14, 5, 4, 16, 11),
			box(4, 14, 4, 5, 16, 5),
			box(4, 14, 11, 5, 16, 12),
			box(11, 14, 11, 12, 16, 12),
			box(11, 14, 4, 12, 16, 5));

	public NBlockPedestal(Properties properties, BiFunction<BlockPos, BlockState, NBEPedestal> tile) 
	{
		super(properties, tile);
	}

	public NBlockPedestal(Properties props)
	{
		super (props, NRegistration.RegisterBlockEntities.BE_PEDESTAL);
	}
	
	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult rayTrace)
	{
		if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND)
		{
			ItemStack stack = player.getItemInHand(hand);
			BlockHelper.castTileEntity(world, pos, NBEPedestal.class).ifPresent( tile -> 
			{
				tile.usePedestal(player, stack);
			});
			return InteractionResult.CONSUME;
		}
		return super.use(state, world, pos, player, hand, rayTrace);
	}
	
	@Override
	public void onRemove(@NotNull BlockState oldState, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean update)
	{
		if (!level.isClientSide && !oldState.is(newState.getBlock())) 
		{
			BlockHelper.castTileEntity(level, pos, NBEPedestal.class).ifPresent(ent -> 
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

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

import com.arcanc.nedaire.content.block.entities.ModBEHolder;
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

public class ModBlockHolder extends ModTileProviderBlock<ModBEHolder> 
{
	private final VoxelShape shape = Shapes.or(
			box(0, 10, 0, 16, 16, 16),
			box(1.5f, 3f, 1.5f, 14.5f, 10f, 14.5f));
	
	public ModBlockHolder(Properties properties, BiFunction<BlockPos, BlockState, ModBEHolder> tile) 
	{
		super(properties, tile);
	}

	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTrace) 
	{
		if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND)
		{
			ItemStack stack = player.getItemInHand(hand);
			BlockHelper.castTileEntity(world, pos, ModBEHolder.class).ifPresent( tile ->
			{
				tile.useHolder(player, stack);
			});
			return InteractionResult.CONSUME;
		}
		return InteractionResult.SUCCESS;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean update) 
	{
		if (!level.isClientSide && !oldState.is(newState.getBlock())) 
		{
			BlockHelper.castTileEntity(BlockHelper.getTileEntity(level, pos), ModBEHolder.class).ifPresent(ent -> 
			{
				ItemHelper.dropContents(level, pos, ItemHelper.getItemHandler(ent));			
			});
		super.onRemove(oldState, level, pos, newState, update);
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) 
	{
		return shape;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) 
	{
		return shape;
	}
	
	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) 
	{
		return shape;
	}

}

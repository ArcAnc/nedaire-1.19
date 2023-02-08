/**
 * @author ArcAnc
 * Created at: 2023-02-05
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import java.util.function.BiFunction;

import com.arcanc.nedaire.content.block.entities.NBETerramorfer;
import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NBlockTerramorfer extends NTileProviderBlock<NBETerramorfer> 
{

	private static final VoxelShape SHAPE = Shapes.or(
			box(4, 0, 4, 5, 4, 5), 
			box(4, 0, 11, 5, 4, 12),
			box(11, 0, 4, 12, 4, 5),
			box(11, 0, 11, 12, 4, 12),
			box(3, 4, 3, 13, 6, 13),
			box(5, 6, 5, 11, 13, 11)); 
	
	public NBlockTerramorfer(Properties properties, BiFunction<BlockPos, BlockState, NBETerramorfer> tile) 
	{
		super(properties, tile);
	}

	public NBlockTerramorfer(Properties props)
	{
		super (props, NRegistration.RegisterBlockEntities.BE_TERRAMORFER);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) 
	{
		//FIXME: add code to click behavior
		return super.use(state, level, pos, player, hand, hitResult);
	}
	
	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) 
	{
		return SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) 
	{
		return SHAPE;
	}
	
	@Override
	public VoxelShape getInteractionShape(BlockState p_60547_, BlockGetter p_60548_, BlockPos p_60549_) 
	{
		return SHAPE;
	}
}

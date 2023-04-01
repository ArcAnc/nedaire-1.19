/**
 * @author ArcAnc
 * Created at: 2023-02-05
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

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

	private static final VoxelShape SHAPE = Shapes.block(); 
	
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

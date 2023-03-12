/**
 * @author ArcAnc
 * Created at: 2023-03-12
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEDiffuser;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FluidHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NBlockDiffuser extends NTileProviderBlock<NBEDiffuser> 
{

	private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	private static final VoxelShape SHAPE = Shapes.join(
			Shapes.block(), 
				Shapes.or(
					box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), 
					box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), 
					box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), 
					INSIDE), 
				BooleanOp.ONLY_FIRST);

	
	public NBlockDiffuser(Properties properties) 
	{
		super(properties, NRegistration.RegisterBlockEntities.BE_DIFFUSER);
	}
	
	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) 
	{
		BlockHelper.castTileEntity(level, pos, BlockEntity.class).ifPresent(tile -> 
		{
			FluidHelper.getFluidHandler(tile).ifPresent(handler -> 
			{
				if (handler.getFluidInTank(0).getFluid().is(FluidTags.LAVA))
				{
					if (isEntityInsideContent(state, pos, handler.getFluidInTank(0).getAmount() / (float)handler.getTankCapacity(0), entity))
					{
						entity.lavaHurt();
					}
				}
			});
		});
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) 
	{
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) 
	{
		return BlockHelper.getTileEntity(level, pos).map(tile -> 
		{
			return FluidHelper.getFluidHandler(tile).map(handler -> 
			{
				return (int)((handler.getFluidInTank(0).getAmount() / (float)handler.getTankCapacity(0)) * 15);
			}).orElse(0);
		}).orElse(0);
	}
	
	private boolean isEntityInsideContent(BlockState blockState, BlockPos pos, float fluidHeight, Entity entity) 
	{
		return entity.getY() < (double)pos.getY() + fluidHeight && entity.getBoundingBox().maxY > (double)pos.getY() + 0.25D;
	}
	
	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType pCompType) 
	{
		return false;
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
		return INSIDE;
	}
}

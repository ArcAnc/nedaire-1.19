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
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

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
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult)
	{
		if (!level.isClientSide())
		{
			BlockHelper.castTileEntity(level, pos, NBEDiffuser.class).ifPresent(tile -> 
			{
				ItemStack stack = player.getItemInHand(hand);
				if (FluidHelper.isFluidHandler(stack))
				{
					FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection());
				}
				else
				{
					if (stack.isEmpty())
					{
						ItemStack t = ItemHelper.getItemHandler(tile).
								map(handler -> handler.extractItem(0, 1, false)).
								orElse(ItemStack.EMPTY);
						
						ItemHandlerHelper.giveItemToPlayer(player, t);
					}
					else
					{
						ItemHelper.getItemHandler(tile).ifPresent(handler -> 
						{
							player.setItemInHand(hand, handler.insertItem(0, stack, false));
						});
					}
				}
			});
			
		}
		return super.use(state, level, pos, player, hand, hitResult);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity)
	{
		BlockHelper.getTileEntity(level, pos).ifPresent(tile -> 
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

	@SuppressWarnings("deprecation")
	@Override
	public boolean hasAnalogOutputSignal(@NotNull BlockState state)
	{
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos)
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

	@SuppressWarnings("deprecation")
	@Override
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType pCompType)
	{
		return false;
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
		return INSIDE;
	}
}

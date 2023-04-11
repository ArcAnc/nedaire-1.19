/**
 * @author ArcAnc
 * Created at: 2022-10-10
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEManualCrusher;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.FakePlayer;

public class NBlockManualCrusher extends NTileProviderBlock<NBEManualCrusher> 
{

	private static final VoxelShape SHAPE = Shapes.or(
			box(2, 0, 2, 14, 6, 14), 
			box(2, 14, 2, 14, 16, 14),
			box(3, 6, 3, 13, 14, 13));

	public NBlockManualCrusher(Properties props) 
	{
		super (props, NRegistration.RegisterBlockEntities.BE_MANUAL_CRUSHER);
	}

	@Override
	protected BlockState getInitDefaultState() 
	{
		BlockState state = super.getInitDefaultState();
		if (state.hasProperty(BlockHelper.BlockProperties.HORIZONTAL_FACING))
		{
			state = state.setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, Direction.SOUTH);
		}
		return state;
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) 
	{
		if (player instanceof FakePlayer || player == null)
		{
			dropBlock(level, pos);
			return InteractionResult.sidedSuccess(level.isClientSide());
		}
		return BlockHelper.castTileEntity(level, pos, NBEManualCrusher.class).map(tile -> 
		{
			if (hitResult.getDirection() == Direction.UP)
			{
				tile.power();				
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
			return super.use(state, level, pos, player, hand, hitResult);
		}).
		orElse(super.use(state, level, pos, player, hand, hitResult));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos newPos, Block block, BlockPos oldPos, boolean bool) 
	{
		BlockHelper.castTileEntity(level, newPos, NBEManualCrusher.class).ifPresent( tile -> 
		{
			BlockState downState = level.getBlockState(newPos.below());
			BlockState upState = level.getBlockState(newPos.above());
			if (!downState.isFaceSturdy(level, newPos.below(), Direction.UP) || !upState.isAir())
			{
				dropBlock(level, newPos);
			}
		});
		
		super.neighborChanged(state, level, newPos, block, oldPos, bool);
	}
	
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity player, ItemStack stack) 
	{
		super.setPlacedBy(level, pos, state, player, stack);
		
		BlockHelper.castTileEntity(level, pos, NBEManualCrusher.class).ifPresent(tile -> 
		{
			BlockState downState = level.getBlockState(pos.below());
			BlockState upState = level.getBlockState(pos.above());
			if (!downState.isFaceSturdy(level, pos.below(), Direction.UP) || !upState.isAir())
			{
				dropBlock(level, pos);
			}
		});
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) 
	{
		return super.getStateForPlacement(context).setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot) 
	{
		return state.setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, rot.rotate(state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING)));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) 
	{
		return state.rotate(mirror.getRotation(state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING)));
	}
	
	private void dropBlock(Level level, BlockPos pos)
	{
		level.destroyBlock(pos, true);
		level.sendBlockUpdated(pos, defaultBlockState(), level.getBlockState(pos), UPDATE_ALL);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) 
	{
		return RenderShape.MODEL;
	}
	
	@Override
	public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean update) 
	{
		if (!level.isClientSide() && !oldState.is(newState.getBlock())) 
		{
			BlockHelper.castTileEntity(level, pos, NBEManualCrusher.class).ifPresent(ent -> 	
			{
				ItemHelper.dropContents(level, pos, ItemHelper.getItemHandler(ent)); 
			});
		super.onRemove(oldState, level, pos, newState, update);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) 
	{
		builder.add(BlockHelper.BlockProperties.HORIZONTAL_FACING, BlockHelper.BlockProperties.WATERLOGGED);
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

/**
 * @author ArcAnc
 * Created at: 2022-10-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import java.util.function.BiFunction;

import com.arcanc.nedaire.content.block.entities.NBEDeliveryStation;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NBlockDeliveryStation extends NTileProviderBlock<NBEDeliveryStation> 
{
	
	private static final VoxelShape SHAPE = Shapes.or(
			box(1, 0, 1, 15, 6, 15),
			box(7, 6, 7, 9, 8, 9),
			box(7.5f, 8, 7.5f, 8.5f, 13, 8.5f),
			box(7.75f, 13, 7.75f, 8.25f, 16, 8.25f),
			box(11, 6, 11, 14, 9, 14),
			box(11, 9, 11, 16, 10, 16),
			box(11, 6, 2, 14, 9, 5),
			box(11, 9, 0, 16, 10, 5),
			box(2, 6, 11, 5, 9, 14),
			box(0, 9, 11, 5, 10, 16),
			box(2, 6, 2, 5, 9, 5),
			box(0, 9, 0, 5, 10, 5));
	
	private final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap<>();
	private final VoxelShape[] shapeByIndex = makeShapes();
	
	public NBlockDeliveryStation(Properties properties, BiFunction<BlockPos, BlockState, NBEDeliveryStation> tile) 
	{
		super(properties, tile);
	}
	
	public NBlockDeliveryStation(Properties props) 
	{
		super (props, NRegistration.RegisterBlockEntities.BE_DELIVERY_STATION);
	}
	
	@Override
	protected BlockState getInitDefaultState() 
	{
		BlockState state =  super.getInitDefaultState();
		if (state.hasProperty(BlockHelper.BlockProperties.Pipe.NORTH))
		{
			state = state.setValue(BlockHelper.BlockProperties.Pipe.NORTH, Boolean.FALSE);			
		}
		if (state.hasProperty(BlockHelper.BlockProperties.Pipe.SOUTH))
		{
			state = state.setValue(BlockHelper.BlockProperties.Pipe.SOUTH, Boolean.FALSE);			
		}
		if (state.hasProperty(BlockHelper.BlockProperties.Pipe.EAST))
		{
			state = state.setValue(BlockHelper.BlockProperties.Pipe.EAST, Boolean.FALSE);			
		}
		if (state.hasProperty(BlockHelper.BlockProperties.Pipe.WEST))
		{
			state = state.setValue(BlockHelper.BlockProperties.Pipe.WEST, Boolean.FALSE);
		}
		return state;
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) 
	{
		if(!level.isClientSide())
		{
//			NetworkHooks.openScreen(null, null);
		}
		return super.use(state, level, pos, player, hand, hitResult);
	}
	

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) 
	{
		// north - 4
		// east - 8
		// west - 2
		// south - 1
		
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		
		return super.getStateForPlacement(context).
				setValue(BlockHelper.BlockProperties.Pipe.NORTH, connectsTo(level, pos, Direction.NORTH)).
				setValue(BlockHelper.BlockProperties.Pipe.SOUTH, connectsTo(level, pos, Direction.SOUTH)).
				setValue(BlockHelper.BlockProperties.Pipe.EAST, connectsTo(level, pos, Direction.EAST)).
				setValue(BlockHelper.BlockProperties.Pipe.WEST, connectsTo(level, pos, Direction.WEST));
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState facingState, LevelAccessor level,	BlockPos currentPos, BlockPos facingPos) 
	{
		return dir.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? state.setValue(BlockHelper.BlockProperties.Pipe.PROPERTY_BY_DIRECTION.get(dir), connectsTo(level, currentPos, dir)) : super.updateShape(state, dir, facingState, level, currentPos, facingPos);
	}
	
	public boolean connectsTo(LevelAccessor level, BlockPos pos, Direction direction) 
	{
		BlockEntity tile = level.getBlockEntity(pos.relative(direction));
		
		if (tile == null)
			return false;
		
		boolean isItemHandler = ItemHelper.isItemHandler(tile, direction);
		boolean isFluidHandler = FluidHelper.isFluidHandler(tile, direction);
		boolean isVimHandler = VimHelper.isVimHandler(tile, direction);
		
		return isItemHandler || isFluidHandler || isVimHandler;
	}
	
	private VoxelShape[] makeShapes()
	{
		
		//3
		VoxelShape connector_w = Shapes.or(
				box(1, 6, 7, 3, 8, 9),
				box(0, 6, 6, 1, 10, 10));
		
		//2
		VoxelShape connector_s = Shapes.or(
				box(6, 6, 15, 10, 10, 16),
				box(7, 6, 13, 9, 8, 15));
		//4
		VoxelShape connector_e = Shapes.or(
				box(13, 6, 7, 15, 8, 9),
				box(15, 6, 6, 16, 10, 10));
		//1
		VoxelShape connector_n = Shapes.or(
				box(6, 6, 0, 10, 10, 1),
				box(7, 6, 1, 9, 8, 3));
		
		//5
		VoxelShape connector_e_n = Shapes.or(
				connector_s,
				connector_n);
		//6
		VoxelShape connector_s_w = Shapes.or(
				connector_s, 
				connector_w);
		
		
		VoxelShape[] voxelShapes = new VoxelShape[] 
				{
						Shapes.empty(),
						connector_s,
						connector_w, 
						connector_s_w,
						connector_n,
						Shapes.or(connector_n, connector_s),
						Shapes.or(connector_w, connector_n),
						Shapes.or(connector_s_w, connector_n),
						connector_e,
						Shapes.or(connector_s, connector_e),
						Shapes.or(connector_w, connector_e),
						Shapes.or(connector_s_w, connector_e),
						connector_e_n,
						Shapes.or(connector_s, connector_e_n),
						Shapes.or(connector_w, connector_e_n),
						Shapes.or(connector_s_w, connector_e_n)
				};
		
		for (int q = 0; q < 16; q++)
		{
			voxelShapes[q] = Shapes.or(SHAPE, voxelShapes[q]);
		}
		
		return voxelShapes;
	}
	
	private static int indexFor(Direction direction) 
	{
		return 1 << direction.get2DDataValue();
	}
	
	@SuppressWarnings("deprecation")
	protected int getAABBIndex(BlockState state) 
	{
		return this.stateToIndex.computeIntIfAbsent(state, (st) -> 
			{
				int i = 0;
		        if (st.getValue(BlockHelper.BlockProperties.Pipe.NORTH)) 
		        {
		        	i |= indexFor(Direction.NORTH);
		        }
		        if (st.getValue(BlockHelper.BlockProperties.Pipe.EAST)) 
		        {
		        	i |= indexFor(Direction.EAST);
		        }
		        if (st.getValue(BlockHelper.BlockProperties.Pipe.SOUTH)) 
		        {
		           i |= indexFor(Direction.SOUTH);
		        }
		        if (st.getValue(BlockHelper.BlockProperties.Pipe.WEST)) 
		        {
		        	i |= indexFor(Direction.WEST);
		        }
		        return i;
		     });
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) 
	{
		return shapeByIndex[getAABBIndex(state)];
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) 
	{
		return shapeByIndex[getAABBIndex(state)];
	}
	
	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) 
	{
		return shapeByIndex[getAABBIndex(state)];
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) 
	{
		builder.add(BlockHelper.BlockProperties.Pipe.NORTH, BlockHelper.BlockProperties.Pipe.SOUTH, BlockHelper.BlockProperties.Pipe.EAST, BlockHelper.BlockProperties.Pipe.WEST);
	}
}

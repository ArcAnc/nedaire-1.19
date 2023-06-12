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
import org.jetbrains.annotations.NotNull;

public class NBlockDeliveryStation extends NTileProviderBlock<NBEDeliveryStation> 
{
	
	private static final VoxelShape SHAPE = box(5, 5, 5, 11, 11, 11);
	
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
		if (state.hasProperty(BlockHelper.BlockProperties.Pipe.UP))
		{
			state = state.setValue(BlockHelper.BlockProperties.Pipe.UP, Boolean.FALSE);
		}
		if (state.hasProperty(BlockHelper.BlockProperties.Pipe.DOWN))
		{
			state = state.setValue(BlockHelper.BlockProperties.Pipe.DOWN, Boolean.FALSE);
		}
		return state;
	}
	
	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult)
	{
		return super.use(state, level, pos, player, hand, hitResult);
	}
	

	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context)
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
				setValue(BlockHelper.BlockProperties.Pipe.WEST, connectsTo(level, pos, Direction.WEST)).
				setValue(BlockHelper.BlockProperties.Pipe.UP, connectsTo(level, pos, Direction.UP)).
				setValue(BlockHelper.BlockProperties.Pipe.DOWN, connectsTo(level, pos, Direction.DOWN));
	}
	
	@Override
	public @NotNull BlockState updateShape(BlockState state, @NotNull Direction dir, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos)
	{
		return state.setValue(BlockHelper.BlockProperties.Pipe.PROPERTY_BY_DIRECTION.get(dir), connectsTo(level, currentPos, dir));
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
		
		//new
		//16
		//old
		//3
		VoxelShape connector_w = Shapes.or(
				box(11, 7.5f, 7.5f, 15, 8.5f, 8.5f),
				box(15, 6, 6, 16, 10, 10));
		
		//new
		//8
		//old
		//2
		VoxelShape connector_s = Shapes.or(
				box(7.5f, 7.5f, 11, 8.5f, 8.5f, 16),
				box(6, 6, 15, 10, 10, 16));
		
		//new
		//32
		//old
		//4
		VoxelShape connector_e = Shapes.or(
				box(1, 7.5f, 7.5f, 5, 8.5f, 8.5f),
				box(0, 6, 6, 1, 10, 10));
		//new
		//4
		//old
		//1
		VoxelShape connector_n = Shapes.or(
				box(7.5f, 7.5f, 1, 8.5f, 8.5f, 5),
				box(6, 6, 0, 10, 10, 1));
		//2
		VoxelShape connector_up = Shapes.or(
				box(7.5f, 11, 7.5f, 8.5f, 15, 8.5f),
				box(6, 15, 6, 10, 16, 10));
		//1
		VoxelShape connector_down = Shapes.or(
				box(7.5f, 1, 7.5f, 8.5f, 5, 8.5f),
				box(6, 0, 6, 10, 1, 10));
		
		//3
		VoxelShape connector_up_down = Shapes.or(connector_down, connector_up);
		
		//12
		VoxelShape connector_s_n = Shapes.or(connector_s, connector_n);
		
		//20
		VoxelShape connector_e_n = Shapes.or(connector_e, connector_n);
		
		//24
		VoxelShape connector_e_s = Shapes.or(connector_e, connector_s);
		
		//28
		VoxelShape connector_e_s_n = Shapes.or(connector_e, connector_s, connector_n);
		
/*		//5
		VoxelShape connector_e_n = Shapes.or(
				connector_s,
				connector_n);
		//6
		VoxelShape connector_s_w = Shapes.or(
				connector_s, 
				connector_w);
*/		
		
		VoxelShape[] voxelShapes = new VoxelShape[] 
				{
		/*0*/			Shapes.empty(),
		/*1*/			connector_down,
		/*2*/			connector_up,
		/*3*/			connector_up_down,
		/*4*/			connector_n,
		/*5*/			Shapes.or(connector_n, connector_down),
		/*6*/			Shapes.or(connector_n, connector_up),
		/*7*/			Shapes.or(connector_n, connector_up_down),
		/*8*/			connector_s,
		/*9*/			Shapes.or(connector_s, connector_down),
		/*10*/			Shapes.or(connector_s, connector_up),
		/*11*/			Shapes.or(connector_s, connector_up_down),
		/*12*/			connector_s_n,
		/*13*/			Shapes.or(connector_s_n, connector_down),
		/*14*/			Shapes.or(connector_s_n, connector_up),
		/*15*/			Shapes.or(connector_s_n, connector_up_down),
		/*16*/			connector_e,
		/*17*/			Shapes.or(connector_e, connector_down),
		/*18*/			Shapes.or(connector_e, connector_up),
		/*19*/			Shapes.or(connector_e, connector_up_down),
		/*20*/			connector_e_n,
		/*21*/			Shapes.or(connector_e_n, connector_down),
		/*22*/			Shapes.or(connector_e_n, connector_up),
		/*23*/			Shapes.or(connector_e_n, connector_up_down),
		/*24*/			connector_e_s,
		/*25*/			Shapes.or(connector_e_s, connector_down),
		/*26*/			Shapes.or(connector_e_s, connector_up),
		/*27*/			Shapes.or(connector_e_s, connector_up_down),
		/*28*/			connector_e_s_n,
		/*29*/			Shapes.or(connector_e_s_n, connector_down),
		/*30*/			Shapes.or(connector_e_s_n, connector_up),
		/*31*/			Shapes.or(connector_e_s_n, connector_up_down),
		/*32*/			connector_w,
		/*33*/			Shapes.or(connector_w, connector_down),
		/*34*/			Shapes.or(connector_w, connector_up),
		/*35*/			Shapes.or(connector_w, connector_up_down),
		/*36*/			Shapes.or(connector_w, connector_n),
		/*37*/			Shapes.or(connector_w, connector_n, connector_down),
		/*38*/			Shapes.or(connector_w, connector_n, connector_up),
		/*39*/			Shapes.or(connector_w, connector_n, connector_up_down),
		/*40*/			Shapes.or(connector_w, connector_s),
		/*41*/			Shapes.or(connector_w, connector_s, connector_down),
		/*42*/			Shapes.or(connector_w, connector_s, connector_up),
		/*43*/			Shapes.or(connector_w, connector_s, connector_up_down),
		/*44*/			Shapes.or(connector_w, connector_s_n),
		/*45*/			Shapes.or(connector_w, connector_s_n, connector_down),
		/*46*/			Shapes.or(connector_w, connector_s_n, connector_up),
		/*47*/			Shapes.or(connector_w, connector_s_n, connector_up_down),
		/*48*/			Shapes.or(connector_w, connector_w),
		/*49*/			Shapes.or(connector_w, connector_w, connector_down),
		/*50*/			Shapes.or(connector_w, connector_w, connector_up),
		/*51*/			Shapes.or(connector_w, connector_w, connector_up_down),
		/*52*/			Shapes.or(connector_w, connector_e_n),
		/*53*/			Shapes.or(connector_w, connector_e_n, connector_down),
		/*54*/			Shapes.or(connector_w, connector_e_n, connector_up),
		/*55*/			Shapes.or(connector_w, connector_e_n, connector_up_down),
		/*56*/			Shapes.or(connector_w, connector_e_s),
		/*57*/			Shapes.or(connector_w, connector_e_s, connector_down),
		/*58*/			Shapes.or(connector_w, connector_e_s, connector_up),
		/*59*/			Shapes.or(connector_w, connector_e_s, connector_up_down),
		/*60*/			Shapes.or(connector_w, connector_e_s_n),
		/*61*/			Shapes.or(connector_w, connector_e_s_n, connector_down),
		/*62*/			Shapes.or(connector_w, connector_e_s_n, connector_up),
		/*63*/			Shapes.or(connector_w, connector_e_s_n, connector_up_down),
		
						
						
						
/*						connector_s,
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
*/				};
		
		for (int q = 0; q < 64; q++)
		{
			voxelShapes[q] = Shapes.or(SHAPE, voxelShapes[q]);
		}
		
		return voxelShapes;
	}
	
	private static int indexFor(Direction direction) 
	{
		return 1 << direction.get3DDataValue();
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
		        if(st.getValue(BlockHelper.BlockProperties.Pipe.UP))
		        {
		        	i |= indexFor(Direction.UP);
		        }
		        if(st.getValue(BlockHelper.BlockProperties.Pipe.DOWN))
		        {
		        	i |= indexFor(Direction.DOWN);
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
		builder.add(BlockHelper.BlockProperties.Pipe.NORTH, BlockHelper.BlockProperties.Pipe.SOUTH, BlockHelper.BlockProperties.Pipe.EAST, BlockHelper.BlockProperties.Pipe.WEST, BlockHelper.BlockProperties.Pipe.UP, BlockHelper.BlockProperties.Pipe.DOWN, BlockHelper.BlockProperties.WATERLOGGED);
	}
}

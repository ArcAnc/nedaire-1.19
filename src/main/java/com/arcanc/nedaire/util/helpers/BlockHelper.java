/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Optional;

public class BlockHelper 
{
	public static class BlockProperties
	{
		public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
		public static final DirectionProperty FACING = BlockStateProperties.FACING;
		public static final DirectionProperty VERTICAL_ATTACHMENT = DirectionProperty.create("vertical_attachment", Direction.UP, Direction.DOWN);
		public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
		public static final BooleanProperty ENABLED =  BlockStateProperties.ENABLED;
		public static final BooleanProperty LIT = BlockStateProperties.LIT;

		public static final BooleanProperty MULTIBLOCK_SLAVE = BooleanProperty.create("multiblock_slave");

		public static class Pipe
		{
			public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
			public static final BooleanProperty EAST = BlockStateProperties.EAST;
			public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
			public static final BooleanProperty WEST = BlockStateProperties.WEST;
			public static final BooleanProperty UP = BlockStateProperties.UP;
			public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
			public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction.class), (map) ->
			{
				map.put(Direction.NORTH, NORTH);
				map.put(Direction.EAST, EAST);
				map.put(Direction.SOUTH, SOUTH);
				map.put(Direction.WEST, WEST);
				map.put(Direction.UP, UP);
				map.put(Direction.DOWN, DOWN);
			}));
		}
	}


	public static Optional<BlockEntity> getTileEntity(LevelReader world, Vec3 pos)
	{
		if (pos != null)
		{
			return getTileEntity(world, BlockPos.containing(pos));
		}
		return Optional.empty();
	}

	@SuppressWarnings("deprecation")
	public static Optional<BlockEntity> getTileEntity(LevelReader world, BlockPos pos)
	{
		if (world != null && world.hasChunkAt(pos))
		{
			return Optional.ofNullable(world.getBlockEntity(pos));
		}
		return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	public static <T> Optional<T> castTileEntity(BlockEntity tile, Class<T> to)
	{
		if (tile != null && to.isAssignableFrom(tile.getClass()))
		{
			T entity = (T) tile;
			return Optional.of(entity);
		}
		return Optional.empty();
	}

	public static <T> Optional<T> castTileEntity(LevelReader world, BlockPos pos, Class<T> to)
	{
		if (world != null && pos != null)
		{
			return getTileEntity(world, pos).flatMap(tile -> castTileEntity(tile, to));
		}
		return Optional.empty();
	}

	public static <T> Optional<T> castTileEntity(LevelReader world, Vec3 pos, Class<T> to)
	{
		if (world != null && pos != null)
		{
			return castTileEntity(world, BlockPos.containing(pos.x(), pos.y(), pos.z()), to);
		}
		return Optional.empty();
	}

	public static ResourceLocation getRegistryName (Block block)
	{
		return ForgeRegistries.BLOCKS.getKey(block);
	}
}

/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;


import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockHelper 
{
	public static class BlockProperties
	{
		public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
		public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
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

	
	@SuppressWarnings("deprecation")
	public static final BlockEntity getTileEntity(Level world, BlockPos pos)
	{
		if (world != null && world.hasChunkAt(pos))
		{
			return world.getBlockEntity(pos);
		}
		return null;
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
	
	public static <T> Optional<T> castTileEntity(Level world, BlockPos pos, Class<T> to)
	{
		if (world != null && pos != null)
		{
			BlockEntity tile = getTileEntity(world, pos);
			return castTileEntity(tile, to);
		}
		return Optional.empty();
	}
	
	public static ResourceLocation getRegistryName (Block block) 
	{
		return ForgeRegistries.BLOCKS.getKey(block);
	}
}

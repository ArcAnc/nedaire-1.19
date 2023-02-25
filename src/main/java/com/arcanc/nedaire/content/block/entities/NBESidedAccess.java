/**
 * @author ArcAnc
 * Created at: 2023-02-25
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.EnumMap;
import java.util.Map;

import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NBESidedAccess extends NBaseBlockEntity 
{
	protected final Map<Direction, AccessType> ports = new EnumMap<>(Direction.class);
	
	public NBESidedAccess(BlockEntityType<?> type, BlockPos pos, BlockState state) 
	{
		super(type, pos, state);
		for (Direction dir : Direction.values())
		{
			ports.put(dir, AccessType.NONE);
		}
	}
	
	public AccessType getAccessType(Direction dir)
	{
		return ports.get(dir);
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		ListTag sides = tag.getList(NDatabase.Capabilities.SIDED_ACCESS, Tag.TAG_STRING);
		ports.clear();
		for (Direction dir : Direction.values())
		{
			ports.put(dir, AccessType.byName(sides.getString(dir.get3DDataValue())));
		}
	}
	
	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		ListTag sides = new ListTag();
		for (Direction dir : Direction.values())
		{
			StringTag side = StringTag.valueOf(ports.get(dir).getSerializedName());
			sides.add(dir.get3DDataValue(), side);
		}
		tag.put(NDatabase.Capabilities.SIDED_ACCESS, sides);
	}

}

/**
 * @author ArcAnc
 * Created at: 2023-03-07
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;

public class NBECrystalGrowth extends NBaseBlockEntity implements IInventoryCallback, ModServerTickerBlockEntity
{

	private static final int PERIOD = 100;

	private static final int MINX = -2;
	private static final int MINY = -2;
	private static final int MINZ = -2;

	private static final int MAXX = 3;
	private static final int MAXY = 3;
	private static final int MAXZ = 3;
	
	public NBECrystalGrowth(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_CRYSTAL_GROWTH.get(), pos, state);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tickServer() 
	{
		if (getLevel().getGameTime() % PERIOD == 0)
		{
			BlockPos pos = getBlockPos();
			for (int x = pos.getX() + MINX; x < pos.getX() + MAXX; x++)
			{
				for (int y = pos.getY() + MINY; y < pos.getY() + MAXY; y++)
				{
					for(int z = pos.getZ() + MINZ; z < pos.getZ() + MAXZ; z++)
					{
						BlockPos curPos = new BlockPos(x, y, z);
						BlockState state = getLevel().getBlockState(curPos);
						Block block = state.getBlock();
						ServerLevel lvl = (ServerLevel)level;
						if (block instanceof IPlantable)
						{
							RandomSource random = lvl.random;
							block.randomTick(state, lvl, curPos, random);

							level.levelEvent(1505, curPos, 0);
						}
					}
				}
			}
		}
	}

	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{

	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{

	}
}

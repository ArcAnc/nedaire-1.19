/**
 * @author ArcAnc
 * Created at: 2022-10-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.ArrayList;
import java.util.List;

import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.content.entities.DeliveryDroneEntity;
import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class NBEDeliveryStation extends NBaseBlockEntity implements ModServerTickerBlockEntity
{

	/*FIXME: change to FixedSizeList from EclipseCollections or write custom implementation*/
	private final List<DeliveryDroneEntity> drones = new ArrayList<>(4);
	
	private final StationSettings settings = StationSettings.newSettings();
	
	public NBEDeliveryStation(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_DELIVERY_STATION.get(), pos, state);
	}

	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		
	}

	@Override
	public void tickServer() 
	{
		
	}
	
	private static class StationSettings
	{
		
		
		private static StationSettings newSettings()
		{
			return new StationSettings();
		}
	}

}

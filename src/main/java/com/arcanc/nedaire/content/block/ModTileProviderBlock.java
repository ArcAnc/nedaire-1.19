/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import java.util.function.BiFunction;

import javax.annotation.Nullable;

import com.arcanc.nedaire.content.block.entities.ticker.ModClientTickerBlockEntity;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class ModTileProviderBlock<T extends BlockEntity> extends ModBaseBlock implements EntityBlock 
{
	private final BiFunction<BlockPos, BlockState, T> tile;
	private BlockEntityClassData classData;
	
	public ModTileProviderBlock(Properties properties, BiFunction<BlockPos, BlockState, T> tile) 
	{
		super(properties);
		this.tile = tile;
	}
	
	public ModTileProviderBlock(Properties props, RegistryObject<BlockEntityType<T>> type)
	{
		this(props, (bp, state) -> type.get().create(bp, state));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) 
	{
		return tile.apply(pos, state);
	}
	
	@Override
	public <T2 extends BlockEntity> BlockEntityTicker<T2> getTicker(Level level, BlockState state, BlockEntityType<T2> type) 
	{
		BlockEntityTicker<T2> baseTicker = getClassData().makeBaseTicker(level.isClientSide);
		
		return baseTicker;
	}
	
	@Override
	protected BlockState getInitDefaultState() 
	{
		/*
		 * TODO: добавить сюда состояния для поворотов, если буду их дописывать
		 */
		
		return super.getInitDefaultState();
	}
	
	private BlockEntityClassData getClassData()
	{
		if (this.classData == null)
		{
			T tempBE = tile.apply(BlockPos.ZERO, getInitDefaultState());
			this.classData = new BlockEntityClassData 
					(
					tempBE instanceof ModServerTickerBlockEntity,
					tempBE instanceof ModClientTickerBlockEntity
					);
					
		}
		return this.classData;
	}
	
	private record BlockEntityClassData (boolean serverTicking, boolean clientTicking)
	{
		@Nullable
		public <T extends BlockEntity> BlockEntityTicker<T> makeBaseTicker(boolean isClient)
		{
			if(serverTicking && !isClient)
				return ModServerTickerBlockEntity.makeTicker();
			else if(clientTicking && isClient)
				return ModClientTickerBlockEntity.makeTicker();
			else
				return null;
		}
	}

}

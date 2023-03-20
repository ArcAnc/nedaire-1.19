/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities.ticker;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public interface NServerTickerBlockEntity	extends NTickerBase 
{
	void tickServer();
	
	static <T extends BlockEntity>BlockEntityTicker<T> makeTicker()
	{
		return (level, pos, state, blockEntity) -> 
		{
			NServerTickerBlockEntity tickable = (NServerTickerBlockEntity) blockEntity;
			if (tickable.canTickAny())
			{
				tickable.tickServer();
			}
		};
	}
}

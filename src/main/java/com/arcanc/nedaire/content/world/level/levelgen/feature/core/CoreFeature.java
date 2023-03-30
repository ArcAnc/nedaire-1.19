/**
 * @author ArcAnc
 * Created at: 2023-03-30
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.world.level.levelgen.feature.core;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CoreFeature extends Feature<CoreConfiguration> 
{

	public CoreFeature(Codec<CoreConfiguration> codec) 
	{
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<CoreConfiguration> ctx) 
	{
		
		CoreConfiguration config = ctx.config();
		RandomSource random = ctx.random();
		BlockPos pos = ctx.origin();
		WorldGenLevel level = ctx.level();
		int offsetX = config.offset.sample(random);
		int offsetY = config.offset.sample(random);
		int offsetZ = config.offset.sample(random);
		
		BlockPos newPos = pos.offset(offsetX, offsetY, offsetZ);
        BlockState state = level.getBlockState(newPos);
		
        if (state.is(config.cannotReplace) || state.is(config.invalidBlocks))
        	return false;
        
        this.safeSetBlock(level, newPos, config.core.getState(random, newPos), p -> true);
        for(Direction dir : Direction.values())
        {
        	BlockPos tempPos = newPos.relative(dir);
        	this.safeSetBlock(level, tempPos, config.metall.getState(random, tempPos), p -> true);
        }
        
        for (Direction dir : Direction.values())
        {
        	if (dir.getAxis().isHorizontal())
        	{
            	BlockPos tempPos = newPos.relative(dir, 2);
            	if (tempPos.getY() >= 5)
            	{
                	this.safeSetBlock(level, tempPos, config.ore.getState(random, tempPos), p -> true);
            	}
            	else
            	{
                	this.safeSetBlock(level, tempPos, config.deepslateOre.getState(random, tempPos), p -> true);
            	}
        	}
        }
        
		return true;
	}

}

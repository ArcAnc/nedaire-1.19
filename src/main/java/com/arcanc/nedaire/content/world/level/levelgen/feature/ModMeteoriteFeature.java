/**
 * @author ArcAnc
 * Created at: 2022-04-03
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.world.level.levelgen.feature;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ModMeteoriteFeature extends Feature<NoneFeatureConfiguration> 
{

	public ModMeteoriteFeature(Codec<NoneFeatureConfiguration> codec) 
	{
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) 
	{
		WorldGenLevel level = ctx.level();
	    BlockPos blockpos = ctx.origin();
	    RandomSource random = ctx.random();
		
	    //добавить код проверки возможности спавна метеорита. –азнесу все сюда, по разным функци€м, потому что ебал € в рот
	    
		return false;
	}

}

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
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record CoreConfiguration(BlockStateProvider core,
								BlockStateProvider metal,
								BlockStateProvider ore,
								BlockStateProvider deepslateOre,
								IntProvider offset,
								TagKey<Block> cannotReplace,
								TagKey<Block> invalidBlocks) implements FeatureConfiguration
{
	public static final Codec<CoreConfiguration> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(BlockStateProvider.CODEC.fieldOf("core").forGetter(provider -> provider.core),
							BlockStateProvider.CODEC.fieldOf("metal").forGetter(provider -> provider.metal),
							BlockStateProvider.CODEC.fieldOf("ore").forGetter(provider -> provider.ore),
							BlockStateProvider.CODEC.fieldOf("deepslate_ore").forGetter(provider -> provider.deepslateOre),
							IntProvider.codec(1, 20).fieldOf("offset").orElse(UniformInt.of(4, 5)).forGetter(provider -> provider.offset),
							TagKey.hashedCodec(Registries.BLOCK).fieldOf("cannot_replace").forGetter(provider -> provider.cannotReplace),
							TagKey.hashedCodec(Registries.BLOCK).fieldOf("invalid_blocks").forGetter(provider -> provider.invalidBlocks)).
					apply(instance, CoreConfiguration::new));

}

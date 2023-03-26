/**
 * @author ArcAnc
 * Created at: 2023-03-26
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.world.level.levelgen.feature;

import java.util.List;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class NConfiguredFeatures 
{
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> CORE_KEY = registerKey(NDatabase.Blocks.BlockEntities.Names.CORE);
	
	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) 
	{
		/**
		 * FIXME: fix geode generation settings
		 */
		register(context, CORE_KEY, Feature.GEODE, new GeodeConfiguration(
				new GeodeBlockSettings(
					BlockStateProvider.simple(Blocks.AIR), 
					BlockStateProvider.simple(Blocks.AMETHYST_BLOCK), 
					BlockStateProvider.simple(NRegistration.RegisterBlocks.CORE.getDefaultBlockState()), 
					BlockStateProvider.simple(Blocks.CALCITE),
					BlockStateProvider.simple(Blocks.SMOOTH_BASALT),
					List.of(Blocks.SMALL_AMETHYST_BUD.defaultBlockState(), Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState(), Blocks.LARGE_AMETHYST_BUD.defaultBlockState(), Blocks.AMETHYST_CLUSTER.defaultBlockState()), 
					BlockTags.FEATURES_CANNOT_REPLACE, 
					BlockTags.GEODE_INVALID_BLOCKS), 
				new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D), 
				new GeodeCrackSettings(0.95D, 2.0D, 2), 
				0.35D, 
				0.083D, 
				true, 
				UniformInt.of(4, 6), 
				UniformInt.of(3, 4), 
				UniformInt.of(1, 2), 
				-16, 
				16, 
				0.05D,
				1));
	}
	
	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name)
	{
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, StringHelper.getLocFStr(name));
	}
	
	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register (BootstapContext<ConfiguredFeature<?, ?>> context,
																							ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration)
	{
		context.register(key, new ConfiguredFeature<>(feature, configuration));
	}
}

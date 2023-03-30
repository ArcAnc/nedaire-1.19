/**
 * @author ArcAnc
 * Created at: 2023-03-26
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.world.level.levelgen.feature;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.world.level.levelgen.feature.core.CoreConfiguration;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class NConfiguredFeatures 
{
	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) 
	{
		register(context, NDatabase.WorldGen.Features.Configured.CORE_CONFIGURED_KEY, NRegistration.RegisterFeatures.CORE.get(), 
				new CoreConfiguration(
						BlockStateProvider.simple(NRegistration.RegisterBlocks.CORE.getDefaultBlockState()), 
						BlockStateProvider.simple(NRegistration.RegisterMaterials.CORIUM.getRawStorageBlock().getDefaultBlockState()), 
						BlockStateProvider.simple(NRegistration.RegisterMaterials.CORIUM.getOreBlock().getDefaultBlockState()), 
						BlockStateProvider.simple(NRegistration.RegisterMaterials.CORIUM.getDeepSlateOre().getDefaultBlockState()),
						UniformInt.of(3,4),
						BlockTags.FEATURES_CANNOT_REPLACE, 
						BlockTags.GEODE_INVALID_BLOCKS));
	}
	
	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register (BootstapContext<ConfiguredFeature<?, ?>> context,
																							ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration)
	{
		context.register(key, new ConfiguredFeature<>(feature, configuration));
	}
}

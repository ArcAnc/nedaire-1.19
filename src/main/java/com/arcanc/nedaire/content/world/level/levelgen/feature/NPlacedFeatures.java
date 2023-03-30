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

import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class NPlacedFeatures 
{
	public static void bootstrap(BootstapContext<PlacedFeature> ctx)
	{
		HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = ctx.lookup(Registries.CONFIGURED_FEATURE);
		
		register(ctx, NDatabase.WorldGen.Features.Placed.CORE_PLACED_KEY, 
				configuredFeatures.getOrThrow(NDatabase.WorldGen.Features.Configured.CORE_CONFIGURED_KEY), 
				RarityFilter.onAverageOnceEvery(24), 
				InSquarePlacement.spread(), 
				HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30)), 
				BiomeFilter.biome());
	}
	
	private static void register(BootstapContext<PlacedFeature> ctx, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers)
	{
		ctx.register(key, new PlacedFeature(feature, List.copyOf(modifiers)));
	}
	
	private static void register(BootstapContext<PlacedFeature> ctx, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers)
	{
		register(ctx, key, feature, List.of(modifiers));
	}
}

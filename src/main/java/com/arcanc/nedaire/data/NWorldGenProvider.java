/**
 * @author ArcAnc
 * Created at: 2023-03-26
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.arcanc.nedaire.content.world.level.levelgen.feature.NConfiguredFeatures;
import com.arcanc.nedaire.content.world.level.levelgen.feature.NPlacedFeatures;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

public class NWorldGenProvider extends DatapackBuiltinEntriesProvider 
{

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().
			add(Registries.CONFIGURED_FEATURE, NConfiguredFeatures::bootstrap).
			add(Registries.PLACED_FEATURE, NPlacedFeatures :: bootstrap);
	
	public NWorldGenProvider(PackOutput output, CompletableFuture<Provider> registries) 
	{
		super(output, registries, BUILDER, Set.of(NDatabase.MOD_ID));
	}

	@Override
	public @NotNull String getName()
	{
		return "Nedaire World Gen";
	}
}

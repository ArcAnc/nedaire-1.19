/**
 * @author ArcAnc
 * Created at: 2023-01-27
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.worldgen;

import java.util.concurrent.CompletableFuture;

import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class NBiomeTags extends TagsProvider<Biome> 
{

	public NBiomeTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) 
	{
		super(packOutput, Registries.BIOME, lookupProvider, NDatabase.MOD_ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider)
	{
	}
	
	@Override
	public @NotNull String getName()
	{
		return "Nedaire Biome Tags";
	}

}

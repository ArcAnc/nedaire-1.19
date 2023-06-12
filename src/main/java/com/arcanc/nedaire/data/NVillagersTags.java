/**
 * @author ArcAnc
 * Created at: 2023-01-27
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data;

import java.util.concurrent.CompletableFuture;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class NVillagersTags extends TagsProvider<PoiType> 
{

	public NVillagersTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) 
	{
		super(packOutput, Registries.POINT_OF_INTEREST_TYPE, lookupProvider, NDatabase.MOD_ID, helper);
	}

	@Override
	protected void addTags(@NotNull Provider provider)
	{
		tag(createTag("acquirable_job_site")).add(createKey(NRegistration.RegisterVillage.UNDERGROUNDER_POI.getId().toString()));
	}
	
	@Override
	public @NotNull String getName()
	{
		return "Nedaire Villager PoI Tags";
	}

	private static ResourceKey<PoiType> createKey(String str) 
	{
		return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(str));
	}
	
	private static TagKey<PoiType> createTag(String str)
	{
		return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(str));
	}
}

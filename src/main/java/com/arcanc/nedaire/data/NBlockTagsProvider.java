/**
 * @author ArcAnc
 * Created at: 2022-04-01
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data;

import java.util.concurrent.CompletableFuture;

import com.arcanc.nedaire.content.material.NMaterial;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.tags.NTags;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class NBlockTagsProvider extends BlockTagsProvider
{
	public NBlockTagsProvider(PackOutput out, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper ext) 
	{
		super(out, lookupProvider, NDatabase.MOD_ID, ext);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider) 
	{
		NMaterial mat = NRegistration.RegisterMaterials.CORIUM;
		
		tag(Tags.Blocks.STORAGE_BLOCKS).addTag(NTags.Blocks.MATERIALS.get(mat.getName()).getStorageBlock());
		tag(NTags.Blocks.MATERIALS.get(mat.getName()).getStorageBlock()).add(mat.getStorageBlock().get()).add(mat.getRawStorageBlock().get());
		tag(BlockTags.NEEDS_IRON_TOOL).addTag(NTags.Blocks.MATERIALS.get(mat.getName()).getStorageBlock());
		
		if (mat.requiredOre())
		{
			tag(Tags.Blocks.ORES).addTag(NTags.Blocks.MATERIALS.get(mat.getName()).getOre());
			tag(NTags.Blocks.MATERIALS.get(mat.getName()).getOre()).add(mat.getOreBlock().get()).add(mat.getDeepSlateOre().get());
			tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(NTags.Blocks.MATERIALS.get(mat.getName()).getOre()).addTag(NTags.Blocks.MATERIALS.get(mat.getName()).getStorageBlock());
			tag(BlockTags.NEEDS_DIAMOND_TOOL).addTag(NTags.Blocks.MATERIALS.get(mat.getName()).getOre());
		}
		
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(NRegistration.RegisterBlocks.SKYSTONE.get()).add(NRegistration.RegisterBlocks.PEDESTAL.get()).add(NRegistration.RegisterBlocks.HOLDER.get());
		tag(BlockTags.NEEDS_STONE_TOOL).add(NRegistration.RegisterBlocks.SKYSTONE.get()).add(NRegistration.RegisterBlocks.PEDESTAL.get()).add(NRegistration.RegisterBlocks.HOLDER.get());
	
		tag(BlockTags.WALLS).add(NRegistration.RegisterBlocks.SKYSTONE_WALL.get());
	}
	
	@Override
	public String getName() 
	{
		return "Nedaire Block Tags";
	}

}

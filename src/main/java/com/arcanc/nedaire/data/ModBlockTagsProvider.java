/**
 * @author ArcAnc
 * Created at: 2022-04-01
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data;

import com.arcanc.nedaire.content.material.ModMaterial;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.tags.ModTags;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider
{
	public ModBlockTagsProvider(DataGenerator gen, ExistingFileHelper ext) 
	{
		super(gen, NDatabase.MOD_ID, ext);
	}
	
	@Override
	protected void addTags() 
	{
		ModMaterial mat = NRegistration.RegisterMaterials.CORIUM;
		
		tag(Tags.Blocks.STORAGE_BLOCKS).addTag(ModTags.Blocks.MATERIALS.get(mat.getName()).getStorageBlock());
		tag(ModTags.Blocks.MATERIALS.get(mat.getName()).getStorageBlock()).add(mat.getStorageBlock().get()).add(mat.getRawStorageBlock().get());
		tag(BlockTags.NEEDS_IRON_TOOL).addTag(ModTags.Blocks.MATERIALS.get(mat.getName()).getStorageBlock());

		if (mat.requiredOre())
		{
			tag(Tags.Blocks.ORES).addTag(ModTags.Blocks.MATERIALS.get(mat.getName()).getOre());
			tag(ModTags.Blocks.MATERIALS.get(mat.getName()).getOre()).add(mat.getOreBlock().get()).add(mat.getDeepSlateOre().get());
			tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(ModTags.Blocks.MATERIALS.get(mat.getName()).getOre()).addTag(ModTags.Blocks.MATERIALS.get(mat.getName()).getStorageBlock());
			tag(BlockTags.NEEDS_DIAMOND_TOOL).addTag(ModTags.Blocks.MATERIALS.get(mat.getName()).getOre());
		}
		
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(NRegistration.RegisterBlocks.SKYSTONE.get()).add(NRegistration.RegisterBlocks.PEDESTAL.get()).add(NRegistration.RegisterBlocks.HOLDER.get());
		tag(BlockTags.NEEDS_STONE_TOOL).add(NRegistration.RegisterBlocks.SKYSTONE.get()).add(NRegistration.RegisterBlocks.PEDESTAL.get()).add(NRegistration.RegisterBlocks.HOLDER.get());
	}
	
	@Override
	public String getName() 
	{
		return "Nedaire Block Tags";
	}

}

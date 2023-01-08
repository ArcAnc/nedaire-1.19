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

import com.arcanc.nedaire.content.material.ModMaterial;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.tags.ModTags;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class NItemTagsProvider extends ItemTagsProvider 
{
	public NItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTagsProvider, ExistingFileHelper ext) 
	{
		super(output, lookupProvider, blockTagsProvider, NDatabase.MOD_ID, ext);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider) 
	{
	
		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		copy(Tags.Blocks.ORES, Tags.Items.ORES);

		ModMaterial mat = NRegistration.RegisterMaterials.CORIUM;

		copy(ModTags.Blocks.MATERIALS.get(mat.getName()).getStorageBlock(), ModTags.Items.MATERIALS.get(mat.getName()).getStorageBlock());
			
		if (mat.requiredOre())
		{
			copy(ModTags.Blocks.MATERIALS.get(mat.getName()).getOre(), ModTags.Items.MATERIALS.get(mat.getName()).getOre());
		}
		
			
		tag(Tags.Items.INGOTS).addTag(ModTags.Items.MATERIALS.get(mat.getName()).getIngot());
		tag(ModTags.Items.MATERIALS.get(mat.getName()).getIngot()).add(mat.getIngot().get());
		
		tag(Tags.Items.NUGGETS).addTag(ModTags.Items.MATERIALS.get(mat.getName()).getNugget());
		tag(ModTags.Items.MATERIALS.get(mat.getName()).getNugget()).add(mat.getNugget().get());
		
		tag(Tags.Items.DUSTS).addTag(ModTags.Items.MATERIALS.get(mat.getName()).getDust());
		tag(ModTags.Items.MATERIALS.get(mat.getName()).getDust()).add(mat.getDust().get());
		
		tag(Tags.Items.RAW_MATERIALS).addTag(ModTags.Items.MATERIALS.get(mat.getName()).getRaw());
		tag(ModTags.Items.MATERIALS.get(mat.getName()).getRaw()).add(mat.getRaw().get());
	
		tag(ModTags.Items.WRENCH).add(NRegistration.RegisterItems.HAMMER.get());
		
		
	}
	
	@Override
	public String getName() 
	{
		return "Nedaire Item Tags";
	}
}

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
import com.arcanc.nedaire.content.registration.ModRegistration;
import com.arcanc.nedaire.data.tags.ModTags;
import com.arcanc.nedaire.util.database.ModDatabase;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider 
{
	public ModItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagsProvider, ExistingFileHelper ext) 
	{
		super(gen, blockTagsProvider, ModDatabase.MOD_ID, ext);
	}
	
	@Override
	protected void addTags() 
	{
	
		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		copy(Tags.Blocks.ORES, Tags.Items.ORES);

		ModMaterial mat = ModRegistration.RegisterMaterials.CORIUM;

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
	
		tag(ModTags.Items.WRENCH).add(ModRegistration.RegisterItems.HAMMER.get());
		
		
	}
	
	@Override
	public String getName() 
	{
		return "Nedaire Item Tags";
	}
}

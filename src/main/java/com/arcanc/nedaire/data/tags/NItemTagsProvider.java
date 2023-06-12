/**
 * @author ArcAnc
 * Created at: 2022-04-01
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.tags;

import java.util.concurrent.CompletableFuture;

import com.arcanc.nedaire.content.material.NMaterial;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.tags.base.NTags;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class NItemTagsProvider extends ItemTagsProvider 
{
	public NItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTagsProvider, ExistingFileHelper ext) 
	{
		super(output, lookupProvider, blockTagsProvider.contentsGetter(), NDatabase.MOD_ID, ext);
	}
	
	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider)
	{
	
		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		copy(Tags.Blocks.ORES, Tags.Items.ORES);
		copy(BlockTags.WALLS, ItemTags.WALLS);
		
		NMaterial mat = NRegistration.RegisterMaterials.CORIUM;

		copy(NTags.Blocks.MATERIALS.get(mat.getName()).getStorageBlock(), NTags.Items.MATERIALS.get(mat.getName()).getStorageBlock());
			
		if (mat.requiredOre())
		{
			copy(NTags.Blocks.MATERIALS.get(mat.getName()).getOre(), NTags.Items.MATERIALS.get(mat.getName()).getOre());
		}
		
			
		tag(Tags.Items.INGOTS).addTag(NTags.Items.MATERIALS.get(mat.getName()).getIngot());
		tag(NTags.Items.MATERIALS.get(mat.getName()).getIngot()).add(mat.getIngot().get());
		
		tag(Tags.Items.NUGGETS).addTag(NTags.Items.MATERIALS.get(mat.getName()).getNugget());
		tag(NTags.Items.MATERIALS.get(mat.getName()).getNugget()).add(mat.getNugget().get());
		
		tag(Tags.Items.DUSTS).addTag(NTags.Items.MATERIALS.get(mat.getName()).getDust());
		tag(NTags.Items.MATERIALS.get(mat.getName()).getDust()).add(mat.getDust().get());
		
		tag(Tags.Items.RAW_MATERIALS).addTag(NTags.Items.MATERIALS.get(mat.getName()).getRaw());
		tag(NTags.Items.MATERIALS.get(mat.getName()).getRaw()).add(mat.getRaw().get());
	
		tag(NTags.Items.WRENCH).add(NRegistration.RegisterItems.HAMMER.get());
		
		
	}
	
	@Override
	public @NotNull String getName()
	{
		return "Nedaire Item Tags";
	}
}

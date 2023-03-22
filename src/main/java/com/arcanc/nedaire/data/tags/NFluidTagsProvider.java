/**
 * @author ArcAnc
 * Created at: 2023-03-22
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.tags;

import java.util.concurrent.CompletableFuture;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.tags.base.NTags;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class NFluidTagsProvider extends FluidTagsProvider 
{

	public NFluidTagsProvider(PackOutput out, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper ext) 
	{
		super(out, lookupProvider, NDatabase.MOD_ID, ext);
	}
	
	@Override
	protected void addTags(Provider provider) 
	{
		tag(NTags.Fluids.EXPERIENCE).add(NRegistration.RegisterFluids.EXPERIENCE.still().get()).add(NRegistration.RegisterFluids.EXPERIENCE.flowing().get());
	}
	
	@Override
	public String getName() 
	{
		return "Nedaire Fluid Tags";
	}

}

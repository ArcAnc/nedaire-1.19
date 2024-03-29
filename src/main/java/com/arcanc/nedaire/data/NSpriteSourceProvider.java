/**
 * @author ArcAnc
 * Created at: 2023-01-03
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data;

import java.util.Optional;

import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.content.material.NMaterial;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

public class NSpriteSourceProvider extends SpriteSourceProvider 
{

	public NSpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) 
	{
		super(output, fileHelper, NDatabase.MOD_ID);
	}

	@Override
	protected void addSources() 
	{
		NMaterial mat = NRegistration.RegisterMaterials.CORIUM;
		
		//										SHIELD
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(mat.getToolMat().getShieldBase().texture(), Optional.empty()));
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(mat.getToolMat().getShieldNoPattern().texture(), Optional.empty()));
		
		//										SLOTS
		//		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new DirectoryLister(StringHelper.getStrLocFStr(NDatabase.GUI.Slots.PATH.substring(0, NDatabase.GUI.Slots.PATH.length()-1)),
		//																			   StringHelper.getStrLocFStr(NDatabase.GUI.Slots.PATH)));
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(NSlot.BACKGROUND_STANDART, Optional.empty()));
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(NSlot.BACKGROUND_INPUT, Optional.empty()));
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(NSlot.BACKGROUND_OUPUT, Optional.empty()));
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(NSlot.BACKGROUND_BOTH, Optional.empty()));
		//										MANUAL CRUSHER
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(StringHelper.getLocFStr("block/manual_crusher/manual_crusher"), Optional.empty()));
		
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(StringHelper.getLocFStr("misc/vortex"), Optional.empty()));
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(StringHelper.getLocFStr("misc/essence"), Optional.empty()));
//		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new DirectoryLister(StringHelper.getStrLocFStr("misc"), StringHelper.getStrLocFStr("misc/")));

		//----------------------------------------------------------------------------
		//										PARTICLES
		//----------------------------------------------------------------------------
		atlas(SpriteSourceProvider.PARTICLES_ATLAS).addSource(new SingleFile(StringHelper.getLocFStr("misc/lightning"), Optional.empty()));
		atlas(SpriteSourceProvider.PARTICLES_ATLAS).addSource(new SingleFile(StringHelper.getLocFStr("misc/sphere"), Optional.empty()));
		
	}

}

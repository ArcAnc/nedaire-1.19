/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.itemGroup;

import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class ModItemGroup extends CreativeModeTab
{
	public ModItemGroup (String label)
	{
		super(label);
		
		hideTitle();
		
		setBackgroundImage(StringHelper.getLocFStr(NDatabase.ItemGroups.BACKGROUND_IMAGE_PATH + label + ".png"));
	}
	
	@Override
	public boolean hasSearchBar() 
	{
		return true;
	}
	
	@Override
	public Component getDisplayName() 
	{
		return Component.translatable(NDatabase.MOD_ID + ".itemGroup." + getRecipeFolderName());
	}
	
	@Override
	public ItemStack makeIcon() 
	{
		return new ItemStack(Blocks.BEACON.asItem());
	}
}

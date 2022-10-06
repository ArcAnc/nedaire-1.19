/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item;

import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class ModBlockItemBase extends BlockItem 
{

	public ModBlockItemBase(Block block, Properties props) 
	{
		super(block, props);
	}

	@Override
	public String getDescriptionId() 
	{
		return ItemHelper.getRegistryName(this).toString().replace(':', '.').replace('/', '.');
	}
	
}

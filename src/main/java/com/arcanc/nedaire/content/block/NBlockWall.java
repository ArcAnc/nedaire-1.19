/**
 * @author ArcAnc
 * Created at: 2023-03-20
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.util.helpers.BlockHelper;

import net.minecraft.world.level.block.WallBlock;

public class NBlockWall extends WallBlock 
{

	public NBlockWall(Properties props) 
	{
		super(props);
	}

	@Override
	public String getDescriptionId() 
	{
		return BlockHelper.getRegistryName(this).toString().replace(':', '.').replace(':', '.').replace('/', '.');
	}
}

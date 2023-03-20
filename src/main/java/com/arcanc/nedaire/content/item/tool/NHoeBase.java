/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.tool;

import com.arcanc.nedaire.content.material.tool.NAbstractToolMaterial;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;

public class NHoeBase extends HoeItem 
{
	public NHoeBase(NAbstractToolMaterial toolMat) 
	{
		super(toolMat, (int) toolMat.getAttackDamageBonus(), toolMat.getAttackSpeed(), new Item.Properties());
	
	}

	@Override
	public String getDescriptionId() 
	{
		return ItemHelper.getRegistryName(this).toString().replace(':', '.').replace('/', '.');
	}

}

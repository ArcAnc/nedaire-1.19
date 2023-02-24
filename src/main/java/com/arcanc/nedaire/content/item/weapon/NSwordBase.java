/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.weapon;

import com.arcanc.nedaire.content.material.tool.ModAbstractToolMaterial;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

public class NSwordBase extends SwordItem 
{
	public NSwordBase(ModAbstractToolMaterial toolMat) 
	{
		super(toolMat, (int)toolMat.getAttackDamageBonus(), toolMat.getAttackSpeed(), new Item.Properties().defaultDurability(toolMat.getUses()));
	}

	@Override
	public String getDescriptionId() 
	{
		return ItemHelper.getRegistryName(this).toString().replace(':', '.').replace('/', '.');
	}

}

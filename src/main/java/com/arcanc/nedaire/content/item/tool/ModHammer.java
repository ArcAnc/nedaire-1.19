/**
 * @author ArcAnc
 * Created at: 2022-04-08
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.tool;

import com.arcanc.nedaire.content.item.ModBaseItem;

import net.minecraft.world.item.ItemStack;

public class ModHammer extends ModBaseItem
{

	public ModHammer(Properties props) 
	{
		super(props);
	}
	
	@Override
	public boolean isEnchantable(ItemStack p_41456_) 
	{
		return false;
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) 
	{
		return false;
	}
	
	@Override
	public int getEnchantmentValue() 
	{
		return 0;
	}

}

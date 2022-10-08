/**
 * @author ArcAnc
 * Created at: 2022-10-07
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class FakeIconItem extends ModBaseItem 
{

	public FakeIconItem() 
	{
		super(new Properties().stacksTo(1));
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) 
	{
		
	}
	
	@Override
	protected boolean allowedIn(CreativeModeTab tab) 
	{
		return false;
	}

}

/**
 * @author ArcAnc
 * Created at: 2022-06-14
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.gem;

import com.arcanc.nedaire.content.item.ItemInterfaces.IGemItem;
import com.arcanc.nedaire.content.item.ModBaseItem;
import com.arcanc.nedaire.content.registration.ModRegistration;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class GemItem extends ModBaseItem implements IGemItem
{
	
	public GemItem(Properties props) 
	{
		super(props);
	}
	
	@Override
	public ItemStack getDefaultInstance() 
	{
		ItemStack stack = new ItemStack(this);
		GemUtils.makeGem(stack, ModRegistration.RegisterGemEffects.HEALTH.get().getColor());
		return stack;
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) 
	{
		if (this.allowedIn(tab)) 
		{
			for (RegistryObject<GemEffect<?>> c : ModRegistration.RegisterGemEffects.EFFECTS.getEntries())
			{
				list.add(GemUtils.makeGem(new ItemStack(this), c.get().getColor()));
			}
	    }
	}
}

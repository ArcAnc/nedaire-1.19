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
import com.arcanc.nedaire.content.item.NBaseItem;
import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GemItem extends NBaseItem implements IGemItem
{
	
	public GemItem(Properties props) 
	{
		super(props);
	}
	
	@Override
	public @NotNull ItemStack getDefaultInstance()
	{
		ItemStack stack = new ItemStack(this);
		GemUtils.makeGem(stack, NRegistration.RegisterGemEffects.HEALTH.get().getColor());
		return stack;
	}
	
/*	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) 
	{
		PotionItem
		if (this.allowedIn(tab)) 
		{
			for (RegistryObject<GemEffect<?>> c : NRegistration.RegisterGemEffects.EFFECTS.getEntries())
			{
				list.add(GemUtils.makeGem(new ItemStack(this), c.get().getColor()));
			}
	    }
	}
*/}

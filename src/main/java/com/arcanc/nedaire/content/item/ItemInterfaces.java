/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item;

import com.arcanc.nedaire.content.item.gem.GemEffect;
import com.arcanc.nedaire.content.item.gem.GemEffectInstance;
import com.arcanc.nedaire.content.item.gem.GemUtils;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.world.item.ItemStack;

public class ItemInterfaces 
{
	public interface ICustomModelProperties
	{
		void registerModelProperties();
	}
	
	public interface IGemItem
	{
		default <T extends GemEffect<T>> GemEffectInstance<T> getEffect(ItemStack stack)
		{
			return (GemEffectInstance<T>) GemUtils.effectMap.get(stack.getTag().getCompound(NDatabase.Capabilities.Socket.TAG_LOCATION).getUUID(NDatabase.Capabilities.Socket.ID));
		}
	}
	
}

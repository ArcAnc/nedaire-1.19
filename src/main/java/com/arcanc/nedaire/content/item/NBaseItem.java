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

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class NBaseItem extends Item
{

	public NBaseItem(Properties props) 
	{
		super(props);
	}
	
	@Override
	public @NotNull String getDescriptionId()
	{
		return ItemHelper.getRegistryName(this).toString().replace(':', '.').replace('/', '.');
	}

}

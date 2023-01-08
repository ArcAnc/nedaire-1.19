/**
 * @author ArcAnc
 * Created at: 2023-01-03
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.screen;

import com.arcanc.nedaire.content.container.menu.NHooverMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NHooverScreen extends NContainerScreen<NHooverMenu> 
{

	public NHooverScreen(NHooverMenu slots, Inventory player, Component title) 
	{
		super(slots, player, title);

		this.imageHeight = 169;
		this.imageWidth =  178;
	}

	@Override
	protected void init() 
	{
		super.init();
	}
}

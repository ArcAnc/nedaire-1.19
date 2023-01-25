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
import com.arcanc.nedaire.content.container.widget.Panel;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NHooverScreen extends NContainerScreen<NHooverMenu> 
{

	public NHooverScreen(NHooverMenu slots, Inventory player, Component title) 
	{
		super(slots, player, title);

		this.imageHeight = 175;
		this.imageWidth =  185;
	}

	@Override
	protected void init() 
	{
		super.init();
		
		addRedstoneSensitiveDropPanel(menu.be);
		
		addPanel(new Panel(0, this.leftPos, this.topPos, this.imageWidth, this.imageHeight - (this.imageHeight / 2) - 10));
		
		addItemFilterPanel(menu.be);
		
		addPanelSwitcherDropPanel(menu.be);
	}
	
	@Override
	protected void containerTick() 
	{
		super.containerTick();
	}
}

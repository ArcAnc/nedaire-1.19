/**
 * @author ArcAnc
 * Created at: 2023-01-03
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.screen;

import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.container.menu.NHooverMenu;
import com.arcanc.nedaire.content.container.widget.Panel;
import com.arcanc.nedaire.content.container.widget.info.EnergyInfoArea;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NHooverScreen extends NContainerScreen<NHooverMenu> 
{

	private final IVim energy = VimStorage.newConfig(null).setEnergy(10000).setMaxEnergy(10000).build();
	
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

		addPanel(new Panel(0, this.leftPos, this.topPos, this.imageWidth, this.imageHeight - (this.imageHeight / 2) - 10).
				addWidget(new EnergyInfoArea(new Rect2i(this.leftPos + 10, this.topPos + 10, 38, 68), energy)));
	}
	
	@Override
	protected void containerTick() 
	{
		energy.extract(20, false);
		super.containerTick();
	}
}

/**
 * @author ArcAnc
 * Created at: 2023-03-07
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.screen;

import com.arcanc.nedaire.content.block.entities.NBEFurnace;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.container.menu.NFurnaceMenu;
import com.arcanc.nedaire.content.container.widget.Panel;
import com.arcanc.nedaire.content.container.widget.info.EnergyInfoArea;
import com.arcanc.nedaire.content.container.widget.progressBar.ArrowBrogressBar;
import com.arcanc.nedaire.util.helpers.VimHelper;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NFurnaceScreen extends NContainerScreen<NFurnaceMenu> 
{

	public NFurnaceScreen(NFurnaceMenu slots, Inventory player, Component title) 
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
		
		addPanel(new Panel(0, this.getGuiLeft(), this.getGuiTop(), this.getXSize(), this.getYSize() - (this.getYSize() / 2) - 10 ).
				addWidget(new EnergyInfoArea(new Rect2i(this.getGuiLeft() + 15, this.getGuiTop() + 15, 14, 42), VimHelper.getVimHandler(menu.be).
				orElseGet(() -> VimStorage.newConfig(null).setMaxEnergy(5000).build()))).
				addWidget(new ArrowBrogressBar(new Rect2i(this.getGuiLeft() + 100, this.getGuiTop() + 30, 22, 15), ()-> (double)menu.be.usedEnergy, () -> (double)NBEFurnace.RECIPE_MAX_ENERGY)));
	}
	
}

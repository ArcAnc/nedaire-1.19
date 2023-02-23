/**
 * @author ArcAnc
 * Created at: 2023-02-23
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.screen;

import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.container.menu.NGeneratorFoodMenu;
import com.arcanc.nedaire.content.container.widget.info.EnergyInfoArea;
import com.arcanc.nedaire.util.helpers.VimHelper;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NGeneratorFoodScreen extends NContainerScreen<NGeneratorFoodMenu> 
{

	public NGeneratorFoodScreen(NGeneratorFoodMenu slots, Inventory player, Component title) 
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

		addRenderableWidget(new EnergyInfoArea(new Rect2i(this.getGuiLeft() + 15, this.getGuiTop() + 15, 14, 42), VimHelper.getVimHandler(menu.be).
				orElseGet(() -> VimStorage.newConfig(null).setMaxEnergy(10000).build()))); 
	}

}

/**
 * @author ArcAnc
 * Created at: 2023-03-25
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.screen;

import com.arcanc.nedaire.content.container.menu.NExpExtractorMenu;
import com.arcanc.nedaire.content.container.widget.info.FluidInfoArea;
import com.arcanc.nedaire.util.helpers.FluidHelper;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class NExpExtractorScreen extends NContainerScreen<NExpExtractorMenu> 
{

	public NExpExtractorScreen(NExpExtractorMenu slots, Inventory player, Component title) 
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
		
		addRenderableWidget(new FluidInfoArea(new Rect2i(this.getGuiLeft() + 15, this.getGuiTop() + 20, 14, 42), FluidHelper.getFluidHandler(menu.be).
				orElseGet(() -> new FluidTank(1000)))); 

	}
	
}

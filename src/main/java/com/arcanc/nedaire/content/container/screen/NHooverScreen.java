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

/*		addDropPanel(new DropPanel(66, 36, false, new Color (150, 40, 40),new ItemStack(Items.REDSTONE),() -> Tooltip.create(Component.literal("Redstone Control"))));
		addDropPanel(new DropPanel(34, 45, false, Color.BLUE, new ItemStack(Items.ENDER_PEARL), () -> Tooltip.create(Component.literal("Test Drop Panel"))).addWidget(Button.builder(Component.empty(), (but) -> 
		{
			but.setMessage(Component.literal(RenderHelper.mc().level.random.nextInt(20) + ""));
		}).pos(5, 10).size(20, 20).build()));

		addPanel(new Panel(0, this.leftPos, this.topPos, this.imageWidth, this.imageHeight - (this.imageHeight / 2) - 10));
*/	}
	
	@Override
	protected void containerTick() 
	{
		super.containerTick();
	}
}

/**
 * @author ArcAnc
 * Created at: 2023-01-18
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class DropPanel extends AbstractWidget 
{

	/**
	 * FIXME: закончить код выпадающей панели. Добавить в нее обработку кнопок, окраску, описание в закрытом состоянии и остального
	 */
	
	public DropPanel(int x, int y, int width, int height, Component text) 
	{
		super(x, y, width, height, text);
	}

	@Override
	public void onClick(double mouseX, double mouseY) 
	{
		super.onClick(mouseX, mouseY);
	}
	
	@Override
	protected void updateWidgetNarration(NarrationElementOutput p_259858_) 
	{
	}
	
	public static enum State
	{
		OPEN(0), 
		OPENING(1),
		CLOSED(2),
		CLOSING(3);
		
		private int id;
		
		private State (int id)
		{
			this.id = id;
		}
		
		public int getId() 
		{
			return id;
		}
		
		
	}

}

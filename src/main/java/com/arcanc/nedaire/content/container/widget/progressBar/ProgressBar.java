/**
 * @author ArcAnc
 * Created at: 2023-03-06
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget.progressBar;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class ProgressBar extends AbstractWidget 
{

	public ProgressBar(Rect2i area) 
	{
		super(area.getX(), area.getY(), area.getWidth(), area.getHeight(), Component.empty());
	}

	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput element)
	{

	}

}

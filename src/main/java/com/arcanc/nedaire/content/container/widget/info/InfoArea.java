/**
 * @author ArcAnc
 * Created at: 2023-01-15
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget.info;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class InfoArea extends AbstractWidget 
{
	protected InfoArea(@NotNull Rect2i area)
	{
		super(area.getX(), area.getY(), area.getWidth(), area.getHeight(), Component.empty());
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) 
	{
		return false;
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int partialTicks) 
	{
		return false;
	}
	
	@Override
	public boolean mouseDragged(double p_93645_, double p_93646_, int p_93647_, double p_93648_, double p_93649_) 
	{
		return false;
	}

	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput p_259858_)
	{
		
	}
}

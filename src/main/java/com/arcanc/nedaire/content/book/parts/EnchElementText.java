/**
 * @author ArcAnc
 * Created at: 2022-09-23
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class EnchElementText extends EnchElementAbstract 
{
	private Component text;

	public EnchElementText(EnchiridionInstance instance, Component text, int x, int y, int width) 
	{
		super(instance, x, y, width, 0);
		this.text = text;
		
		setBaseShiftX(17);
		
		Minecraft mc = RenderHelper.mc();		
		this.height = mc.font.wordWrapHeight(text, this.width);
	}

	@Override
	public void onDraw(PoseStack pos, int mouseX, int mouseY, float f) 
	{
		Minecraft mc = RenderHelper.mc();		
		mc.font.drawWordWrap(pos, text, this.x, this.y, this.width, 8821358);
	}

	@Override
	public void onPress(double mouseX, double mouseY) 
	{
		
	}
}

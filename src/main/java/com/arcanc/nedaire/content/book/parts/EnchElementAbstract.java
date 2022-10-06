/**
 * @author ArcAnc
 * Created at: 2022-09-19
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;

public abstract class EnchElementAbstract 
{
	protected EnchiridionInstance ench;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	private boolean isHovered = false;
	
	protected int shiftX;
	protected int shiftY;
	
	protected int baseShiftX = 0;
	protected int baseShiftY = 0;
	
	public EnchElementAbstract(EnchiridionInstance instance, int x, int y, int width, int height)
	{
		this.ench = instance;
		this.shiftX = x;
		this.shiftY = y;
		this.width = width;
		this.height = height;
		this.x = ench.getScreen().guiLeft + shiftX + baseShiftX;
		this.y = ench.getScreen().guiTop + shiftY + baseShiftY;
	}
	
	public void render(PoseStack pos, int mouseX, int mouseY, float f)
	{
		onDraw(pos, mouseX, mouseY, f);
		isHovered = hovered(mouseX, mouseY);
		
	}
	
	public abstract void onDraw(PoseStack pos, int mouseX, int mouseY, float f);
	
	public void playDownSound(SoundManager manager) 
	{
		manager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	public void press (double mouseX, double mouseY)
	{
		if (clicked(mouseX, mouseY))
		{
			onPress(mouseX, mouseY);
			playDownSound(RenderHelper.mc().getSoundManager());
		}
	}
	
	public abstract void onPress(double mouseX, double mouseY);
	
	public boolean isHovered() 
	{
		return isHovered;
	}
	
	protected boolean clicked (double mouseX, double mouseY)
	{
		return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;	
	}
	
	protected boolean hovered (int mouseX, int mouseY)
	{
		return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;	
	}
	
	public int getBaseShiftX()
	{
		return baseShiftX;
	} 
	
	public void setBaseShiftX(int baseShiftX) 
	{
		this.baseShiftX = baseShiftX;
		this.x = ench.getScreen().guiLeft + shiftX + baseShiftX;
	}
	
	public int getBaseShiftY()
	{
		return baseShiftY;
	} 
	
	public void setBaseShiftY(int baseShiftY) 
	{
		this.baseShiftY = baseShiftY;
		this.y = ench.getScreen().guiTop + shiftY + baseShiftY;
	}
	
	public int getX() 
	{
		return x;
	}
	
	public void setX(int x) 
	{
		this.x = x;
	}
	
	public int getY() 
	{
		return y;
	}

	public void setY(int y) 
	{
		this.y = y;
	}
	
	public void setShiftX(int shiftX) 
	{
		this.shiftX = shiftX;
		this.x = ench.getScreen().guiLeft + shiftX + baseShiftX;
	}
	
	public void setShiftY(int shiftY) 
	{
		this.shiftY = shiftY;
		this.y = ench.getScreen().guiTop + shiftY + baseShiftY;
	}
	
	public int getHeight() 
	{
		return height;
	}
	
	public int getWidth() 
	{
		return width;
	}
}

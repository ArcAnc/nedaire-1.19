/**
 * @author ArcAnc
 * Created at: 2022-09-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.content.book.gui.EnchiridionScreen;
import com.arcanc.nedaire.content.container.widget.icon.Icon;
import com.arcanc.nedaire.util.database.NDatabase;
import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Chapter extends Button 
{
	private final ResourceLocation id;
	private final EnchiridionInstance instance;
	private final Icon<?> icon;
	private boolean isActive = false;
	private final boolean isNative;
	
	private EnchElementList list;
	public EnchElementPage page;
	
	public Chapter(int x, int y, ResourceLocation id, EnchiridionInstance instance, Icon<?> icon, OnPress press, Tooltip tooltip)
	{
		super(x, y, 20, 26, CommonComponents.EMPTY, press, DEFAULT_NARRATION);
		this.id  = id;
		this.instance = instance;
		this.icon = icon;
		this.isNative = id.getNamespace().equals(NDatabase.MOD_ID);
		this.setTooltip(tooltip);
	}

	@Override
	public void renderWidget(@NotNull GuiGraphics guiGraphics, int x, int y, float f)
	{
	    guiGraphics.pose().pushPose();
	    RenderSystem.setShader(GameRenderer::getPositionTexShader);
	    if (isActive)
	    	RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
	    else
	    	RenderSystem.setShaderColor(1.0f, 0.75f, 0.75f, this.alpha);
	    RenderSystem.enableBlend();
	    RenderSystem.defaultBlendFunc();
	    RenderSystem.enableDepthTest();
	    if (isNative)
	    	guiGraphics.blit(EnchiridionScreen.TEXT, this.getX(), this.getY(), 236, 0, this.width, this.height);
	    else
	    {
    		guiGraphics.blit(EnchiridionScreen.TEXT, this.getX(), this.getY(), 236, 26, this.width, this.height);
	    }
	    
	    //FIXME: fix render problem, when icon is ItemStack
	    guiGraphics.pose().scale(1.15f, 1.15f, 1f);
	    icon.render(guiGraphics, (int)(this.getX() /1.15f) + 1, (int)(this.getY() / 1.15f) + 1, 16, 16);
	    //RenderHelper.renderItemStack(pose, icon, (int)(this.getX() /1.15f) + 1, (int)(this.getY() / 1.15f), true);
	    guiGraphics.pose().popPose();
	    RenderSystem.disableBlend();
	    RenderSystem.disableDepthTest();
	    
	    if (isActive && list != null)
	    {
	    	renderContent(guiGraphics, x, y, f);
	    }
	}
	
	public void renderContent (GuiGraphics guiGraphics, int x, int y, float f)
	{
		if (page != null)
		{
			page.render(guiGraphics, x, y, f);
		}
		else
		{
			list.render(guiGraphics, x, y, f);
		}
	}
	
	public Chapter addData(EnchElementList element)
	{
		Preconditions.checkNotNull(element);
		list = element;
		list.chapter = this;
		return this;
	}
	
/*	public Chapter addData(List<EnchElementAbstract> element)
	{
		Preconditions.checkNotNull(element);
		element.forEach(el -> Preconditions.checkNotNull(el));
		data.addAll(element);
		return this;
	}
*/	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) 
	{
		boolean b = super.mouseClicked(mouseX, mouseY, button); 
		
		if (isActive)
		{
			if (page != null)
			{
				page.press(mouseX, mouseY);
			}
			else
			{
				list.press(mouseX, mouseY);
			}
		}
		
		return b;
	}
	
	@Override
	public void onPress() 
	{
		super.onPress();
		
		for (GuiEventListener but : instance.getScreen().children())
		{
			if (but instanceof Chapter chapt && chapt != this)
			{
				chapt.isActive = false;
			}
		}
		isActive = true;
		instance.getScreen().lastActiveChapter = this;
	}
}

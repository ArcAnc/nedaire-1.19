/**
 * @author ArcAnc
 * Created at: 2022-09-08
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.gui;

import java.util.List;
import java.util.function.Supplier;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.content.book.parts.Chapter;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnchiridionScreen extends Screen 
{
	public static final ResourceLocation TEXT = StringHelper.getLocFStr("textures/gui/enchiridion/book.png");
	
	private float scaleFactor = 1;
	private int xSize = 292;
	private int ySize = 180;
	public int guiLeft;
	public int guiTop;
	
	private final List<Chapter> chapters = Lists.newLinkedList();
	private int activeBookmark = 0;
	
	public Chapter lastActiveChapter = null;
	
	public EnchiridionScreen() 
	{
		super(GameNarrator.NO_TITLE);
	}

	@Override
	protected void init() 
	{
		Window res = minecraft.getWindow();
		double oldGuiScale = res.calculateScale(minecraft.options.guiScale().get(), minecraft.isEnforceUnicode());

		int guiScaleInt = getMinecraft().getWindow().calculateScale(0, true);
		double newGuiScale = res.calculateScale(guiScaleInt, true);

		if(guiScaleInt > 0 && newGuiScale != oldGuiScale)
		{
			scaleFactor = (float)newGuiScale/(float)res.getGuiScale();
			res.setGuiScale(newGuiScale);
			width = res.getGuiScaledWidth();
			height = res.getGuiScaledHeight();
			res.setGuiScale(oldGuiScale);
		}
		else
			scaleFactor = 1;
		
		guiLeft = (this.width - this.xSize) / 2;
		guiTop = (this.height - this.ySize) / 2;
		
		EnchiridionInstance.INSTANCE.setScreen(this);
		
		for (Supplier<Chapter> chapter : EnchiridionInstance.getContent().values())
		{
			addRenderableWidget(chapter.get());
		}
	}
	
	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float f) 
	{
		renderBackground(stack);	
		
		super.render(stack, mouseX, mouseY, f);
	}
	
	@Override
	public void renderBackground(PoseStack stack) 
	{
		super.renderBackground(stack);
		
		stack.pushPose();
	    RenderSystem.setShader(GameRenderer::getPositionTexShader);
	    RenderSystem.setShaderTexture(0, EnchiridionScreen.TEXT);
	    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
	    
	    this.blit(stack, guiLeft + xSize / 2, guiTop, 20, 1, 146, 180);
	    
	    stack.scale(-1f, -1f, 1f);
	    
	    this.blit(stack, -guiLeft - xSize / 2, - guiTop - ySize, 20, 1, 146, 180);

	    stack.scale(-1f, -1f, 1f);
	    stack.popPose();
	}
}

/**
 * @author ArcAnc
 * Created at: 2022-09-08
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.gui;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.content.book.parts.Chapter;
import com.arcanc.nedaire.util.database.NDatabase;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class EnchiridionScreen extends Screen 
{
	public static final ResourceLocation TEXT = NDatabase.GUI.getTexturePath("gui/enchiridion/book");
	
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
		EnchiridionInstance.INSTANCE.getScreen().lastActiveChapter = EnchiridionInstance.getContent().get(NDatabase.GUI.Enchiridion.Section.ResourceLocations.BASIC).get();
	}
	
	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		guiGraphics.pose().pushPose();
	    RenderSystem.setShader(GameRenderer::getPositionTexShader);
	    RenderSystem.setShaderTexture(0, EnchiridionScreen.TEXT);
	    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
	    
	    guiGraphics.blit(EnchiridionScreen.TEXT, guiLeft + xSize / 2, guiTop, 20, 1, 146, 180);
	    
	    guiGraphics.pose().scale(-1f, -1f, 1f);
	    
	    guiGraphics.blit(EnchiridionScreen.TEXT, -guiLeft - xSize / 2, - guiTop - ySize, 20, 1, 146, 180);

	    guiGraphics.pose().scale(-1f, -1f, 1f);
	    guiGraphics.pose().popPose();
	}
}

/**
 * @author ArcAnc
 * Created at: 2022-10-01
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import java.util.Map;
import java.util.Map.Entry;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.content.book.gui.EnchiridionScreen;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class EnchElementList extends EnchElementAbstract 
{
	private static final ResourceLocation EMPTY = new ResourceLocation("");
	
	private final Map<ResourceLocation, ResourceLocation> locations;
	/*z = height in this situation*/
	protected final Multimap<Integer, Entry<Vec3i, ResourceLocation>> positions = ArrayListMultimap.create();
	protected Vec3i arrowLeft, arrowRight;
	protected int currentPage = 0, maxPages = 0;
	protected Chapter chapter;
	
	
	public EnchElementList(EnchiridionInstance instance, Map<ResourceLocation, ResourceLocation> locs) 
	{
		super(instance, 0, 0, 110, 150);
		Preconditions.checkNotNull(locs);
		this.locations = locs;
		
		int side = 0;
		int shiftH = 0;
		Minecraft mc = RenderHelper.mc();
		
		for (Entry<ResourceLocation, ResourceLocation> entry : this.locations.entrySet().stream().
				sorted((entry1, entry2) -> entry1.getKey().compareTo(entry2.getKey())).toList())
		{
			int tempShiftH = mc.font.wordWrapHeight(Component.literal("\u23f5 ").append(Component.translatable(entry.getKey().toLanguageKey())), this.width);
			if (shiftH + tempShiftH > this.height)
			{
				side += 1;
				shiftH = 0;
			}
			this.positions.put(side, Map.entry(new Vec3i(this.x + 20 + (150 * (side % 2)) , this.y + 15 + shiftH, tempShiftH), entry.getKey()));
			shiftH += tempShiftH;
		}
		maxPages = side % 2 == 0 ? side : side - 1;
		arrowLeft = new Vec3i(this.x + 15 + 98, this.y + 161, 10);
		arrowRight = new Vec3i(this.x + 15 + 146, this.y + 161, 10);
	}

	@Override
	public void onDraw(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float f)
	{
		Minecraft mc = RenderHelper.mc();
		PoseStack poseStack = guiGraphics.pose();

		poseStack.pushPose();

		this.positions.entries().stream().filter(entry -> entry.getKey() == currentPage || entry.getKey() == currentPage + 1).forEachOrdered(entry -> 
		{
			Entry<Vec3i, ResourceLocation> e = entry.getValue();
			int tempX = e.getKey().getX();
			int tempY = e.getKey().getY();
			int tempH = e.getKey().getZ();
			if (mouseX >= tempX && mouseY >= tempY && mouseX <= tempX + this.width && mouseY <= tempY + tempH )
			{
				guiGraphics.drawWordWrap(mc.font, Component.literal("\u23f5 ").append(Component.translatable(e.getValue().toLanguageKey())), tempX, tempY, this.width, 11908981);
			}
			else
			{
				guiGraphics.drawWordWrap(mc.font, Component.literal("\u23f5 ").append(Component.translatable(e.getValue().toLanguageKey())), tempX, tempY, this.width, 8821358);
			}
		});
		poseStack.popPose();
		
		poseStack.pushPose();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		
		if (isArrowActive(arrowLeft))
		{
			if (isAboveArrow(mouseX, mouseY, arrowLeft))
			{
				guiGraphics.blit(EnchiridionScreen.TEXT, arrowLeft.getX(), arrowLeft.getY(), 26, 207, 18, arrowLeft.getZ());
				guiGraphics.renderTooltip(mc.font, Component.translatable(NDatabase.GUI.Enchiridion.Arrows.ARROW_LEFT), mouseX, mouseY);
			}
			else
			{
				guiGraphics.blit(EnchiridionScreen.TEXT, arrowLeft.getX(), arrowLeft.getY(), 3, 207, 18, arrowLeft.getZ());
			}
		}
		
		if (isArrowActive(arrowRight))
		{
			if (isAboveArrow(mouseX, mouseY, arrowRight))
			{
				guiGraphics.blit(EnchiridionScreen.TEXT, arrowRight.getX(), arrowRight.getY(), 26, 194, 18, arrowRight.getZ());
				guiGraphics.renderTooltip(mc.font, Component.translatable(NDatabase.GUI.Enchiridion.Arrows.ARROW_RIGHT), mouseX, mouseY);
			}
			else
			{
				guiGraphics.blit(EnchiridionScreen.TEXT, arrowRight.getX(), arrowRight.getY(), 3, 194, 18, arrowRight.getZ());
			}
		}
		poseStack.popPose();
	}
	
	protected boolean isAboveArrow(double mouseX, double mouseY, Vec3i arrow)
	{
		return mouseX >= arrow.getX() && mouseY >= arrow.getY() && mouseX <= arrow.getX() + 18 && mouseY <= arrow.getY() + 10;
	}
	
	@Override
	protected boolean clicked(double mouseX, double mouseY) 
	{
		return this.positions.entries().stream().
				filter(entry -> entry.getKey() == currentPage || entry.getKey() == currentPage + 1).
				map(Entry :: getValue).
				map(Entry :: getKey).
				anyMatch(vec ->  
				{
					return mouseX >= vec.getX() && mouseY >= vec.getY() && mouseX <= vec.getX() + this.width && mouseY <= vec.getY() + vec.getZ();
				}) || anyArrowClicked(mouseX, mouseY);
	}
	
	private boolean anyArrowClicked (double mouseX, double mouseY)
	{
		return isArrowClicked(mouseX, mouseY, arrowLeft) || isArrowClicked(mouseX, mouseY, arrowRight);
	}
	
	private boolean isArrowClicked(double mouseX, double mouseY, Vec3i arrow)
	{
		return isArrowActive(arrow) && isAboveArrow(mouseX, mouseY, arrow);
	}

	protected boolean isArrowActive(Vec3i arrow)
	{
		if (positions.containsKey(2))
		{
			if (arrow.equals(arrowRight))
			{
				return currentPage != maxPages;
			}
			else
			{
				return currentPage != 0;
			}
		}
		return false;
	}

	@Override
	public void onPress(double mouseX, double mouseY) 
	{
		if (isArrowClicked(mouseX, mouseY, arrowLeft))
		{
			this.currentPage -= 2;
		} 
		else if (isArrowClicked(mouseX, mouseY, arrowRight))
		{
			this.currentPage +=2;
		}
		else 
		{
			ResourceLocation loc = this.positions.entries().stream().
				filter(entry -> entry.getKey() == currentPage || entry.getKey() == currentPage + 1).
				map(Entry :: getValue).
				filter(entry -> 
				{
					Vec3i vec = entry.getKey();
					return mouseX >= vec.getX() && mouseY >= vec.getY() && mouseX <= vec.getX() + this.width && mouseY <= vec.getY() + vec.getZ();
				}).
				findFirst().
				map(l -> locations.get(l.getValue())).orElse(EMPTY);
			if (!loc.equals(EMPTY))
			{
				chapter.page = new EnchElementPage(ench, loc);
			}
		}
	}
}

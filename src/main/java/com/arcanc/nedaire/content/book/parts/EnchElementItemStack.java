/**
 * @author ArcAnc
 * Created at: 2022-09-20
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class EnchElementItemStack extends EnchElementAbstract 
{

	protected ItemStack stack;
	protected float scale;
	
	public EnchElementItemStack(EnchiridionInstance instance,  ItemStack stack, int x, int y, int width, int height) 
	{
		super(instance, x, y, width, height);
		this.stack = stack;
		this.scale = 1.0f;
		setBaseShiftX(55);
	}

	public static String encode(Item item) 
	{
		return String.format(" </item;%s/> ", ForgeRegistries.ITEMS.getKey(item).toString());
	}

	public static String encode(Block block)
	{
		return String.format(" </block;%s/> ", ForgeRegistries.BLOCKS.getKey(block).toString());
	}
	
	public EnchElementItemStack decode(ResourceLocation loc) 
	{
		return null;
	}

	@Override
	public void onDraw(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float f)
	{
		guiGraphics.pose().pushPose();
		guiGraphics.pose().scale(scale, scale, scale);
		RenderHelper.renderItemStack(guiGraphics, stack, x, y, true);
		guiGraphics.pose().popPose();
		
		if (isHovered())
		{
			RenderSystem.enableBlend();
			Minecraft mc = RenderHelper.mc();
			guiGraphics.renderTooltip(mc.font, Screen.getTooltipFromItem(mc, stack), stack.getTooltipImage(), mouseX, mouseY);
		}
	}
	
	@Override
	protected boolean hovered(int mouseX, int mouseY) 
	{
		return mouseX >= this.x * scale && mouseY >= this.y * scale && mouseX < (this.x + 16) * scale  && mouseY < (this.y + 16) * scale;
	}

	@Override
	public void onPress(double mouseX, double mouseY) 
	{
		
	}

}

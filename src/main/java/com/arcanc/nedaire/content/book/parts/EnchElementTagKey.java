/**
 * @author ArcAnc
 * Created at: 2022-09-20
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchElementTagKey extends EnchElementAbstract 
{
	
	protected TagKey<?> tag;
	protected float scale;
	
	public EnchElementTagKey(EnchiridionInstance instance, TagKey<?> tag, int x, int y, int width, int height) 
	{
		super(instance, x, y, width, height);
		
		setBaseShiftX(55);
		
		this.tag = tag;
		this.scale = 1.0f;
	}

	public static String encode(TagKey<?> tag)
	{
		return String.format(" </tag;%s;%s/> ", tag.registry().location().getPath(), tag.location());
	}

	@Override
	public void onDraw(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float f)
	{
		PoseStack poseStack = guiGraphics.pose();
		Minecraft mc = RenderHelper.mc();

		poseStack.pushPose();

		List<ItemStack> items = getStackFromTag(tag);
		
		if (items.isEmpty())
			return;
		
		ItemStack stack = getStackAtCurrentTime(items);
		poseStack.scale(scale, scale, scale);
		RenderHelper.renderItemStack(guiGraphics, stack, x, y, true);
		poseStack.popPose();
		
		RenderSystem.enableBlend();
		
		if (isHovered())
		{
			guiGraphics.renderTooltip(mc.font, Screen.getTooltipFromItem(mc, stack), stack.getTooltipImage(), mouseX, mouseY);
		}
	}

	@Override
	protected boolean hovered(int mouseX, int mouseY) 
	{
		return mouseX >= this.x * scale && mouseY >= this.y * scale && mouseX < (this.x + 16) * scale  && mouseY < (this.y + 16) * scale;
	}
	
	@SuppressWarnings("unchecked")
	private List<ItemStack> getStackFromTag (TagKey<?> tag)
	{
		String type = tag.registry().location().getPath();

		return switch (type)
		{
			case "item" ->
					ForgeRegistries.ITEMS.tags().getTag((@NotNull TagKey<Item>) tag).stream().map(ItemStack::new).toList();
			case "block" ->
					ForgeRegistries.BLOCKS.tags().getTag((@NotNull TagKey<Block>) tag).stream().map(ItemStack::new).toList();
			case "fluid" ->
					ForgeRegistries.FLUIDS.tags().getTag((@NotNull TagKey<Fluid>) tag).stream().map(f -> new FluidStack(f, 1)).map(FluidUtil::getFilledBucket).toList();
			default -> Lists.newArrayList();
		};
	}

	public ItemStack getStackAtCurrentTime(List<ItemStack> items)
	{
		if(items.isEmpty())
			return ItemStack.EMPTY;

		int perm = (int)(System.currentTimeMillis()/1000%items.size());
		return items.get(perm);
	}

	@Override
	public void onPress(double mouseX, double mouseY) 
	{
	}
}

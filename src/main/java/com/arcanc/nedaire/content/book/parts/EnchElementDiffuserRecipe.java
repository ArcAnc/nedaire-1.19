/**
 * @author ArcAnc
 * Created at: 2023-03-18
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.apache.commons.compress.utils.Lists;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.content.book.gui.EnchiridionScreen;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.recipe.NDiffuserRecipe;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.ChatFormatting;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class EnchElementDiffuserRecipe extends EnchElementAbstractRecipe 
{
	private final List<Vec2> positions = Lists.newArrayList();
	private Vec2 arrowDown = Vec2.ZERO;
	private Vec2 arrowDiag = Vec2.ZERO;
	private Vec2 arrowRight = Vec2.ZERO;

	public EnchElementDiffuserRecipe(EnchiridionInstance instance, ResourceLocation location, int x, int y, int width, int height) 
	{
		super(instance, location, x, y, width, height);
		
		setBaseShiftX(19);
		
		recipe.ifPresent(rec -> 
		{
			if (rec.getType().equals(NRegistration.RegisterRecipes.Types.DIFFUSER.get()))
			{
				//Input
				positions.add(new Vec2(this.x, this.y));
				
				//Fluid
				positions.add(new Vec2(this.x + 25, this.y));

				//Result
				positions.add(new Vec2(this.x + 80, this.y + 35));

				//Diffuser
				positions.add(new Vec2(this.x + 7, this.y + 35));
				
				//Arrows
				arrowDown = new Vec2(this.x + 1, this.y + 18);
				arrowDiag = new Vec2(this.x + 2 + 16, this.y + 18);
				arrowRight = new Vec2(this.x + 40, this.y + 35);
			}
		});
	}

	public EnchElementDiffuserRecipe(EnchiridionInstance instance, ResourceLocation loc, int x, int y) 
	{
		this(instance, loc, x, y, 90, 55);
	}
	
	@Override
	public void onDraw(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float f)
	{
		recipe.ifPresent(r -> 
		{
			Minecraft mc = RenderHelper.mc();
			NDiffuserRecipe rec = (NDiffuserRecipe)r;

			Ingredient in = rec.input;
			ItemStack stack = getStackAtCurrentTime(in);
			ItemStack highlighted = ItemStack.EMPTY;
			RenderHelper.renderItemStack(guiGraphics, stack, (int)positions.get(0).x, (int)positions.get(0).y, true);
			if (mouseX >= (int)positions.get(0).x && mouseY >= (int)positions.get(0).y && mouseX <= (int)positions.get(0).x + 16 && mouseY <= positions.get(0).y + 16)
			{
				highlighted = stack;
			}

			RenderHelper.renderItemStack(guiGraphics, rec.output.get(), (int)positions.get(2).x, (int)positions.get(2).y, true);
			if (mouseX >= (int)positions.get(2).x && mouseY >= (int)positions.get(2).y && mouseX <= (int)positions.get(2).x + 16 && mouseY <= positions.get(2).y + 16)
			{
				highlighted = rec.output.get();
			}
			
			RenderHelper.renderItemStack(guiGraphics, new ItemStack(NRegistration.RegisterBlocks.DIFFUSER), (int)positions.get(3).x, (int)positions.get(3).y, true);
			if (mouseX >= (int)positions.get(3).x && mouseY >= (int)positions.get(3).y && mouseX <= (int)positions.get(3).x + 16 && mouseY <= positions.get(3).y + 16)
			{
				highlighted = new ItemStack(NRegistration.RegisterBlocks.DIFFUSER);
			}
			
			guiGraphics.pose().pushPose();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();

			FluidStack fluid = rec.fluid;
			
			IClientFluidTypeExtensions renderProps = IClientFluidTypeExtensions.of(fluid.getFluid());
			Color color = new Color(renderProps.getTintColor());
			ResourceLocation texture = renderProps.getStillTexture();
			TextureAtlasSprite still = RenderHelper.mc().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
	
			RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
			RenderSystem.setShaderTexture(0, still.atlasLocation());
			guiGraphics.blit((int)positions.get(1).x, (int)positions.get(1).y, 0, 16, 16, still);
			
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
			
			guiGraphics.blit(EnchiridionScreen.TEXT, (int)arrowDown.x, (int)arrowDown.y, 241, 98, 15, 14);
			guiGraphics.blit(EnchiridionScreen.TEXT, (int)arrowDiag.x, (int)arrowDiag.y, 209, 97, 17, 15);
			guiGraphics.blit(EnchiridionScreen.TEXT, (int)arrowRight.x, (int)arrowRight.y, 234, 53, animateArrow(rec.getTotalProcessTime()), 15);
			
			if (!highlighted.isEmpty())
			{
				RenderSystem.enableBlend();
				guiGraphics.renderTooltip(mc.font, Screen.getTooltipFromItem(mc, highlighted), highlighted.getTooltipImage(), mouseX, mouseY);
				RenderSystem.disableBlend();
			}
			else if (mouseX >= (int)positions.get(1).x && mouseY >= (int)positions.get(1).y && mouseX <= (int)positions.get(1).x + 16 && mouseY <= positions.get(1).y + 16)
			{
				List<Component> list = new ArrayList<>();
				
				Style style = Style.EMPTY.withColor(renderProps.getTintColor());
				
				list.add(Component.translatable(fluid.getTranslationKey()).withStyle(style));
				list.add(Component.translatable(NDatabase.GUI.Elements.EnchElementDiffuserRecipe.AMOUNT, Integer.toString(fluid.getAmount())).withStyle(style));
				list.add(Component.empty());
				list.add(Component.literal(FluidHelper.getRegistryName(fluid.getFluid()).toString()).withStyle(ChatFormatting.DARK_GRAY));
				
				RenderSystem.enableBlend();
				guiGraphics.renderTooltip(mc.font, list, Optional.empty(), mouseX, mouseY);
				RenderSystem.disableBlend();
			}
			else if (mouseX >= arrowRight.x && mouseY >= arrowRight.y && mouseX <= arrowRight.x + 22 && mouseY <= arrowRight.y + 14)
			{
				RenderSystem.enableBlend();
				
				guiGraphics.renderTooltip(mc.font, List.of(Component.translatable(NDatabase.GUI.Enchiridion.Recipes.Translatable.TICKS, rec.getTotalProcessTime())), Optional.empty(), mouseX, mouseY);
			}
			guiGraphics.pose().popPose();
		});
	}

	@Override
	public void onPress(double mouseX, double mouseY) 
	{

	}

}

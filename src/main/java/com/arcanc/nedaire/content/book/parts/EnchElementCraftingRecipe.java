/**
 * @author ArcAnc
 * Created at: 2022-09-21
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.Lists;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.content.book.gui.EnchiridionScreen;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.phys.Vec2;

public class EnchElementCraftingRecipe extends EnchElementAbstractRecipe
{
	private final List<Vec2> positions = Lists.newArrayList();
	private Vec2 imagePos = Vec2.ZERO;
	private int partW = width/5;
	private int partH = height/3;
	
	public EnchElementCraftingRecipe(EnchiridionInstance instance, ResourceLocation loc, int x, int y, int width, int height) 
	{
		super(instance, loc, x, y, width, height);

		setBaseShiftX(19);

		recipe.ifPresent(rec -> 
		{
			if (rec.getType().equals(RecipeType.CRAFTING))
			{
				for (int yy = 0; yy < 3; yy++)
				{
					for (int xx = 0; xx < 3; xx++)
					{
						positions.add(xx+ (yy * 3), new Vec2(this.x + (xx *partW), this.y + (yy * partH)));
					}
				}
				
				imagePos = new Vec2(this.x + 4 * partW - 10, this.y + partH);
				positions.add(new Vec2 (this.x + 5 * partW, this.y + partH));
			}
		});
	}
	
	public EnchElementCraftingRecipe(EnchiridionInstance instance, ResourceLocation loc, int x, int y) 
	{
		this(instance, loc, x, y, 90, 55);
	}

	@Override
	public void onDraw(PoseStack pos, int mouseX, int mouseY, float f) 
	{
		recipe.ifPresent(rec -> 
		{
			if (rec.getType().equals(RecipeType.CRAFTING))
			{
				ItemStack highlighted = ItemStack.EMPTY; 
				ItemStack stack = ItemStack.EMPTY;
				
				NonNullList<Ingredient> withOut = NonNullList.withSize(10, Ingredient.of(ItemStack.EMPTY));
				NonNullList<Ingredient> ingr = rec.getIngredients();
				for (int q = 0; q < ingr.size(); q++)
				{
					withOut.set(q, ingr.get(q));
				}
				
				Minecraft mc = RenderHelper.mc();
				withOut.set(9 , Ingredient.of(rec.getResultItem(mc.level.registryAccess())));
				for (int q = 0 ; q < 10; q++ )
				{
					Ingredient in = withOut.get(q);
					stack = getStackAtCurrentTime(in);
				
					pos.pushPose();
					RenderSystem.setShaderTexture(0, EnchiridionScreen.TEXT);
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
					RenderSystem.enableBlend();
					RenderSystem.defaultBlendFunc();
					RenderSystem.enableDepthTest();
					GuiComponent.blit(pos, (int)positions.get(q).x - 1, (int)positions.get(q).y - 1, 217, 0, 18, 18);
					RenderHelper.renderItemStack(pos, stack, (int)positions.get(q).x, (int)positions.get(q).y, true);
					pos.popPose();
					if (mouseX >= (int)positions.get(q).x && mouseY >= (int)positions.get(q).y && mouseX <= (int)positions.get(q).x + 16 && mouseY <= positions.get(q).y + 16)
					{
						highlighted = stack;
					}
				}
			
				pos.pushPose();
				RenderSystem.setShaderTexture(0, EnchiridionScreen.TEXT);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.enableDepthTest();

				if (rec.getSerializer().equals(RecipeSerializer.SHAPELESS_RECIPE))
				{
					GuiComponent.blit(pos, (int)imagePos.x, (int)imagePos.y - 18, 234, 69, 22, 15);
				}
				
				GuiComponent.blit(pos, (int)imagePos.x, (int)imagePos.y, 234, 53, 22, 15);
				RenderSystem.disableBlend();
				RenderSystem.disableDepthTest();
				pos.popPose();
			
				if (!highlighted.isEmpty())
				{
					RenderSystem.enableBlend();
					ench.getScreen().renderTooltip(pos, ench.getScreen().getTooltipFromItem(highlighted), highlighted.getTooltipImage(), mouseX, mouseY);
					RenderSystem.disableBlend();
				}
				else if (mouseX >= imagePos.x && mouseY >= imagePos.y && mouseX <= imagePos.x + 22 && mouseY <= imagePos.y + 15)
				{
					RenderSystem.enableBlend();
					ench.getScreen().renderTooltip(pos, Arrays.asList(Component.translatable(NDatabase.GUI.Enchiridion.Recipes.Translatable.SHAPED)), Optional.empty(), mouseX, mouseY);
					RenderSystem.disableBlend();
				}
				else if ((mouseX >= imagePos.x && mouseY >= imagePos.y - 18 && mouseX <= imagePos.x + 22 && mouseY <= imagePos.y - 18 + 15)
						&& rec.getSerializer().equals(RecipeSerializer.SHAPELESS_RECIPE))
				{
					RenderSystem.enableBlend();
					ench.getScreen().renderTooltip(pos, Arrays.asList(Component.translatable(NDatabase.GUI.Enchiridion.Recipes.Translatable.SHAPELESS)), Optional.empty(), mouseX, mouseY);
					RenderSystem.disableBlend();
				}
			}
		});
	}

	@Override
	public void onPress(double mouseX, double mouseY) 
	{
		
	}

}

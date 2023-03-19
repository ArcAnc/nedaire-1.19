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
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.phys.Vec2;

public class EnchElementRecipeBurning extends EnchElementAbstractRecipe 
{
	private final List<Vec2> positions = Lists.newArrayList();
	private Vec2 arrowPos = Vec2.ZERO;
	private Vec2 expPos = Vec2.ZERO;
	
	public EnchElementRecipeBurning(EnchiridionInstance instance, ResourceLocation location, int x, int y, int width, int height) 
	{
		super(instance, location, x, y, 48, 17);
		
		setBaseShiftX(35);
		
		recipe.ifPresent(rec -> 
		{
			if (checkRecipe())
			{
				positions.add(new Vec2(this.x, this.y));
			
//				positions.add(new Vec2(x, y + 18 + 4));
			
				positions.add(new Vec2(this.x + 22 + 4 + 18, this.y));
			
				arrowPos = new Vec2(this.x + 18 + 2, this.y );
				expPos = new Vec2(this.x + 18 + 4 + 22 + 4, this.y + 2);
			}
		});
	}

	public EnchElementRecipeBurning(EnchiridionInstance instance, ResourceLocation location, int x, int y) 
	{
		this (instance, location, x, y, 59, 25);
	}
	
	@Override
	public void onDraw(PoseStack pos, int mouseX, int mouseY, float f) 
	{
		recipe.ifPresent(rec -> 
		{
			if (checkRecipe())
			{
				ItemStack highlighted = ItemStack.EMPTY; 
				ItemStack stack = ItemStack.EMPTY;
				
				AbstractCookingRecipe r = (AbstractCookingRecipe)rec;
				NonNullList<Ingredient> ingr = NonNullList.create();
				ingr.add(r.getIngredients().get(0));
				Minecraft mc = RenderHelper.mc();
				ingr.add(Ingredient.of(r.getResultItem(mc.level.registryAccess())));
				int time = r.getCookingTime();
				float exp = r.getExperience();
			
				
				for (int q = 0; q < 2; q++)
				{
					pos.pushPose();
					RenderSystem.setShaderTexture(0, EnchiridionScreen.TEXT);
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
					RenderSystem.enableBlend();
					RenderSystem.defaultBlendFunc();
					RenderSystem.enableDepthTest();
					stack = getStackAtCurrentTime(ingr.get(q));
					
					GuiComponent.blit(pos, (int)positions.get(q).x, (int)positions.get(q).y, 217, 0, 18, 18);
					RenderHelper.renderItemStack(pos, stack, (int)positions.get(q).x, (int)positions.get(q).y, true);
					if (mouseX >= (int)positions.get(q).x && mouseY >= (int)positions.get(q).y && mouseX <= (int)positions.get(q).x + 16 && mouseY <= positions.get(q).y + 16)
					{
						highlighted = stack;
					}
					pos.popPose();
				}
				pos.pushPose();
				RenderSystem.setShaderTexture(0, EnchiridionScreen.TEXT);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.enableDepthTest();
				//exp
				GuiComponent.blit(pos, (int)expPos.x + 18, (int)expPos.y + 2, 234, 86, 11, 11);
				
				RenderSystem.disableBlend();
				RenderSystem.disableDepthTest();
				pos.popPose();

				pos.pushPose();
				RenderSystem.setShaderTexture(0, EnchiridionScreen.TEXT);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.enableDepthTest();
				//palochka
				GuiComponent.blit(pos, (int)arrowPos.x, (int)arrowPos.y, 234, 53, animateArrow(time), 15);
				RenderSystem.disableBlend();
				RenderSystem.disableDepthTest();
				pos.popPose();
				
				
				if (!highlighted.isEmpty())
				{
					RenderSystem.enableBlend();
					ench.getScreen().renderTooltip(pos, ench.getScreen().getTooltipFromItem(highlighted), highlighted.getTooltipImage(), mouseX, mouseY);
				}
				else if (mouseX >= arrowPos.x && mouseY >= arrowPos.y && mouseX <= arrowPos.x + 14 && mouseY <= arrowPos.y + 14)
				{
					RenderSystem.enableBlend();
					
					ench.getScreen().renderTooltip(pos, Arrays.asList(Component.translatable(NDatabase.GUI.Enchiridion.Recipes.Translatable.TICKS, time)), Optional.empty(), mouseX, mouseY);
				}
				else if (mouseX >= expPos.x + 18 && mouseY >= expPos.y + 2 && mouseX <= expPos.x + 18 + 11 && mouseY <= expPos.y + 2 +11)
				{
					RenderSystem.enableBlend();
					
					ench.getScreen().renderTooltip(pos, Arrays.asList(Component.translatable(NDatabase.GUI.Enchiridion.Recipes.Translatable.EXPERIENCE, exp)), Optional.empty(), mouseX, mouseY);
				}
			}
		});
	}

	private boolean checkRecipe ()
	{
		return recipe.map(rec -> 
		{
			RecipeType<?> type = rec.getType();
			RecipeSerializer<? extends Recipe<?>> ser = rec.getSerializer();
			return (type.equals(RecipeType.SMELTING) || type.equals(RecipeType.BLASTING) || type.equals(RecipeType.SMOKING) || type.equals(RecipeType.CAMPFIRE_COOKING)) && 
				(ser.equals(RecipeSerializer.SMELTING_RECIPE) || ser.equals(RecipeSerializer.BLASTING_RECIPE) || ser.equals(RecipeSerializer.SMOKING_RECIPE) || ser.equals(RecipeSerializer.CAMPFIRE_COOKING_RECIPE));

		}).orElse(false);
	}

	@Override
	public void onPress(double mouseX, double mouseY) 
	{
		
	}
}

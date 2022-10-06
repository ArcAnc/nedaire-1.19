/**
 * @author ArcAnc
 * Created at: 2022-10-02
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.Lists;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.content.book.gui.EnchiridionScreen;
import com.arcanc.nedaire.util.database.ModDatabase;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchElementPage extends EnchElementAbstract 
{
	
	/*FIXME: пофиксить сдвиг по Х*/
	
	private Vec3i toContent, arrowLeft, arrowRight;
	private List<Pair<Integer, EnchElementAbstract>> elements = Lists.newArrayList();
	
	private final Chapter chap;
	private int currentPage = 0;
	private int maxPages = 0;
	
	public EnchElementPage(EnchiridionInstance instance, ResourceLocation loc) 
	{
		super(instance, 0, 0, 110, 150);
		chap = instance.getScreen().lastActiveChapter;
		
		toContent = new Vec3i(this.x + 15 + 122, this.y + 161, 9);
		arrowLeft = new Vec3i(this.x + 15 + 98, this.y + 166, 10);
		arrowRight = new Vec3i(this.x + 15 + 146, this.y + 166, 10);
	
		this.elements = parseComponents(Component.translatable(loc.toLanguageKey()));
	}

	@Override
	public void onDraw(PoseStack pos, int mouseX, int mouseY, float f) 
	{
		
		elements.stream().
		filter(entry -> entry.getFirst() == currentPage || entry.getFirst() == currentPage + 1).
		forEachOrdered(entry -> 
		{
			entry.getSecond().render(pos, mouseX, mouseY, f);
		});
		pos.pushPose();
		RenderSystem.setShaderTexture(0, EnchiridionScreen.TEXT);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		if (isArrowActive(arrowLeft))
		{
			if (isAboveArrow(mouseX, mouseY, arrowLeft))
			{
				ench.getScreen().blit(pos, arrowLeft.getX(), arrowLeft.getY(), 26, 207, 18, arrowLeft.getZ());
				ench.getScreen().renderTooltip(pos, Component.translatable(ModDatabase.GUI.Enchiridion.Arrows.ARROW_LEFT), mouseX, mouseY);
			}
			else
			{
				ench.getScreen().blit(pos, arrowLeft.getX(), arrowLeft.getY(), 3, 207, 18, arrowLeft.getZ());
			}
		}
		pos.popPose();
		pos.pushPose();
		RenderSystem.setShaderTexture(0, EnchiridionScreen.TEXT);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		if (isArrowActive(arrowRight))
		{
			if (isAboveArrow(mouseX, mouseY, arrowRight))
			{
				ench.getScreen().blit(pos, arrowRight.getX(), arrowRight.getY(), 26, 194, 18, arrowRight.getZ());
				ench.getScreen().renderTooltip(pos, Component.translatable(ModDatabase.GUI.Enchiridion.Arrows.ARROW_RIGHT), mouseX, mouseY);
			}
			else
			{
				ench.getScreen().blit(pos, arrowRight.getX(), arrowRight.getY(), 3, 194, 18, arrowRight.getZ());
			}
		}
		pos.popPose();
			
		pos.pushPose();
		RenderSystem.setShaderTexture(0, EnchiridionScreen.TEXT);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		if (isAboveArrow(mouseX, mouseY, toContent))
		{
			ench.getScreen().blit(pos, toContent.getX(), toContent.getY(), 49, 207, 17, toContent.getZ());
			ench.getScreen().renderTooltip(pos, Component.translatable(ModDatabase.GUI.Enchiridion.Arrows.ARROW_TO_START), mouseX, mouseY);
		}
		else
		{
			ench.getScreen().blit(pos, toContent.getX(), toContent.getY(), 49, 194, 17, toContent.getZ());
		}
		pos.popPose();
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
		else if (isArrowClicked(mouseX, mouseY, toContent)) 
		{
			this.chap.page = null;
		}
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) 
	{
		return anyArrowClicked(mouseX, mouseY);
	}
	
	protected boolean isAboveArrow(double mouseX, double mouseY, Vec3i arrow)
	{
		return mouseX >= arrow.getX() && mouseY >= arrow.getY() && mouseX <= arrow.getX() + (arrow.equals(toContent) ? 17 : 18) && mouseY <= arrow.getY() + (arrow.equals(toContent) ? 9 : 10);
	}
	
	private boolean anyArrowClicked (double mouseX, double mouseY)
	{
		return isArrowClicked(mouseX, mouseY, arrowLeft) || isArrowClicked(mouseX, mouseY, arrowRight) || isArrowClicked(mouseX, mouseY, toContent);
	}
	
	private boolean isArrowClicked(double mouseX, double mouseY, Vec3i arrow)
	{
		return isArrowActive(arrow) && isAboveArrow(mouseX, mouseY, arrow);
	}

	protected boolean isArrowActive(Vec3i arrow)
	{
		if (arrow.equals(toContent))
			return true;
		if (this.elements.stream().map(Pair :: getFirst).toList().contains(2))
		{
			if (arrow.equals(arrowRight))
			{
				if (currentPage != maxPages)
				{
					return true;
				}
			}
			else
			{
				if (currentPage != 0)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public List<Pair<Integer, EnchElementAbstract>> parseComponents(Component comp) 
	{
		String s = comp.getString();
		
		List<Pair<Integer, EnchElementAbstract>> list = Lists.newArrayList();
		
		String[] splitted = s.split("(</)|(/>)");
		
/*		List<String> list = Arrays.stream(s.split("(?=(item|block|entity|tag|recipe|multiblock);([^;>]+))|</|/>")).
			map(str -> str.trim()).
			filter(str -> str.length() > 0).
			collect(Collectors.toList());
*/		
		int side = 0;
		int shiftH = 0;
		int tempShiftH = 20;
		
		for (int q = 0; q < splitted.length; q++)
		{
			if (q > 0)
			{
				Pair<Integer, EnchElementAbstract> el = list.get(q - 1);
				tempShiftH += el.getSecond().getHeight() + shiftH;
				shiftH = 0;
			}
			
			EnchElementAbstract elem = createComponent(splitted[q].trim(), 150 * (side % 2), tempShiftH += shiftH);
			
			if (elem.getHeight() + tempShiftH >= this.y + this.height)
			{
				side += 1;
				tempShiftH = 0;
				shiftH = 20;
				elem.setShiftY(shiftH);
				elem.setShiftX(150 * (side % 2));
			}
			list.add(q, Pair.of(side, elem));
		}
		maxPages = side % 2 == 0 ? side : side - 1;
		currentPage = 0;
		return list;
	}

	private EnchElementAbstract createComponent(String str, int x, int y) 
	{
		Nedaire.getLogger().warn(str);
		String[] strings = str.split(";");
		if (str.startsWith("item;"))
		{
			ResourceLocation loc = new ResourceLocation(strings[1]);
			
			Item item = ForgeRegistries.ITEMS.getValue(loc);
			ItemStack stack = ItemStack.EMPTY;
			if (item != null)
			{
				stack = new ItemStack(item);
			}
			return new EnchElementItemStack(ench, stack, x, y, 16, 16); 
		} 
		else if (str.startsWith("block;"))
		{
			ResourceLocation loc = new ResourceLocation(strings[1]);
			
			Block block = ForgeRegistries.BLOCKS.getValue(loc);
			ItemStack stack = ItemStack.EMPTY;
			if (block != null)
			{
				stack = new ItemStack(block);
			}
			return new EnchElementItemStack(ench, stack, x, y, 16, 16); 
		}
		else if (str.startsWith("tag;"))
		{
			String type = strings[1]; 
			ResourceLocation loc = new ResourceLocation(strings[2]);
			
			TagKey<?> tag = null;
			
			if (type.equals("item"))
			{
				tag = ForgeRegistries.ITEMS.tags().getTagNames().filter(key -> key.location().equals(loc)).findFirst().get();
			}
			else if (type.equals("block"))
			{
				tag = ForgeRegistries.BLOCKS.tags().getTagNames().filter(key -> key.location().equals(loc)).findFirst().get();
			}
			else if (type.equals("fluid"))
			{
				tag = ForgeRegistries.BLOCKS.tags().getTagNames().filter(key -> key.location().equals(loc)).findFirst().get();
			}
			
			if (tag != null)
			{
				return new EnchElementTagKey(ench, tag, x, y, 16, 16); 
			}
		} 
		else if (str.startsWith("recipe;"))
		{
			ResourceLocation loc = new ResourceLocation(strings[1]);

			Minecraft mc = RenderHelper.mc();
			Optional<? extends Recipe<?>> recipe = mc.level.getRecipeManager().byKey(loc);
	
			return recipe.map(rec -> 
			{
				RecipeType<?> type = rec.getType();
				RecipeSerializer<?> ser = rec.getSerializer();
				
				if (type.equals(RecipeType.CRAFTING))
				{
					if (ser.equals(RecipeSerializer.SHAPED_RECIPE) || ser.equals(RecipeSerializer.SHAPELESS_RECIPE))
					{
						return new EnchElementCraftingRecipe(ench, loc, x, y);
					}
				}
				else if (type.equals(RecipeType.SMELTING) || type.equals(RecipeType.BLASTING) || type.equals(RecipeType.SMOKING) || type.equals(RecipeType.CAMPFIRE_COOKING))
				{
					return new EnchElementRecipeBurning(ench, loc, x, y);
				}
				return null;
			}).orElse(null);
		}
		else if (str.startsWith("entity;"))
		{
			ResourceLocation loc = new ResourceLocation(strings[1]);

			EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(loc);
		
			return new EnchElementEntity(ench, type, x, y);
		}
		
			return new EnchElementText(ench, Component.literal(str), x, y, 105);
	}

	
}

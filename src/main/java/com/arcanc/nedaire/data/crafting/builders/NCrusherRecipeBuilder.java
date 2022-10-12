/**
 * @author ArcAnc
 * Created at: 2022-10-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.builders;

import com.arcanc.nedaire.data.crafting.IngredientWithSize;
import com.arcanc.nedaire.data.crafting.recipe.NCrusherRecipe;
import com.arcanc.nedaire.util.database.NDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class NCrusherRecipeBuilder extends NFinishedRecipe<NCrusherRecipeBuilder> 
{

	JsonArray secondaryArray = new JsonArray();
	boolean isManual = false;

	private NCrusherRecipeBuilder()
	{
		super(NCrusherRecipe.SERIALIZER.get().get());
		addWriter(jsonObject -> jsonObject.add(NDatabase.Recipes.Types.Crusher.SECONDARIES, secondaryArray));
		addWriter(jsonObject -> jsonObject.addProperty(NDatabase.Recipes.Types.Crusher.IS_MANUAL, isManual));
	}

	public static NCrusherRecipeBuilder builder(Item result)
	{
		return new NCrusherRecipeBuilder().addResult(result);
	}

	public static NCrusherRecipeBuilder builder(ItemStack result)
	{
		return new NCrusherRecipeBuilder().addResult(result);
	}

	public static NCrusherRecipeBuilder builder(TagKey<Item> result, int count)
	{
		return new NCrusherRecipeBuilder().addResult(new IngredientWithSize(result, count));
	}

	public static NCrusherRecipeBuilder builder(IngredientWithSize result)
	{
		return new NCrusherRecipeBuilder().addResult(result);
	}

	public NCrusherRecipeBuilder addSecondary(ItemLike itemProvider, float chance)
	{
		return this.addSecondary(new ItemStack(itemProvider), chance);
	}

	public NCrusherRecipeBuilder addSecondary(ItemStack itemStack, float chance)
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(NDatabase.Recipes.StackWithChanceNBT.CHANCE, chance);
		jsonObject.add("output", serializeItemStack(itemStack));
		secondaryArray.add(jsonObject);
		return this;
	}

	public NCrusherRecipeBuilder addSecondary(TagKey<Item> tag, float chance)
	{
		return addSecondary(new IngredientWithSize(tag), chance);
	}

	public NCrusherRecipeBuilder addSecondary(IngredientWithSize ingredient, float chance, ICondition... conditions)
	{
		secondaryArray.add(serializeStackWithChance(ingredient, chance, conditions));
		return this;
	}
	
	public NCrusherRecipeBuilder setManual(boolean isManual)
	{
		this.isManual = isManual;
		return this;
	}
	
}

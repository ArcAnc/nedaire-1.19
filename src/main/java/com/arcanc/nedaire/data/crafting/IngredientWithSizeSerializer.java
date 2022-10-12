/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting;

import javax.annotation.Nonnull;

import com.arcanc.nedaire.util.database.NDatabase;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;

public class IngredientWithSizeSerializer implements IIngredientWithSizeSerializer 
{

	public static final IngredientWithSizeSerializer INSTANCE = new IngredientWithSizeSerializer();

	@Nonnull
	@Override
	public IngredientWithSize parse(@Nonnull FriendlyByteBuf buffer)
	{
		final int count = buffer.readInt();
		final Ingredient base = Ingredient.fromNetwork(buffer);
		return new IngredientWithSize(base, count);
	}

	@Override
	public void write(@Nonnull FriendlyByteBuf buffer, @Nonnull IngredientWithSize ingredient)
	{
		buffer.writeInt(ingredient.getCount());
		CraftingHelper.write(buffer, ingredient.getBaseIngredient());
	}

	@Nonnull
	@Override
	public IngredientWithSize parse(@Nonnull JsonElement json)
	{
		if(json.isJsonObject()&&json.getAsJsonObject().has(NDatabase.Recipes.IngredientWithSizeNBT.BASE_KEY))
		{
			final int count = GsonHelper.getAsInt(json.getAsJsonObject(), NDatabase.Recipes.IngredientWithSizeNBT.COUNT_KEY, 1);
			final JsonElement baseJson = json.getAsJsonObject().get(NDatabase.Recipes.IngredientWithSizeNBT.BASE_KEY);
			final Ingredient base = Ingredient.fromJson(baseJson);
			return new IngredientWithSize(base, count);
		}
		else //fallback for normal ingredients
		{
			final Ingredient base = Ingredient.fromJson(json);
			return new IngredientWithSize(base, 1);
		}
	}

	@Override
	public JsonElement write(@Nonnull IngredientWithSize ingredient)
	{
		if(ingredient.getCount()==1)
			return ingredient.getBaseIngredient().toJson();
		JsonObject json = new JsonObject();
		json.addProperty(NDatabase.Recipes.IngredientWithSizeNBT.COUNT_KEY, ingredient.getCount());
		json.add(NDatabase.Recipes.IngredientWithSizeNBT.BASE_KEY, ingredient.getBaseIngredient().toJson());
		return json;
	}
}

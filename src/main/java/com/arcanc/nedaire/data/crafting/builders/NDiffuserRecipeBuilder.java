/**
 * @author ArcAnc
 * Created at: 2023-03-12
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.builders;

import com.arcanc.nedaire.data.crafting.IngredientWithSize;
import com.arcanc.nedaire.data.crafting.recipe.NDiffuserRecipe;
import com.arcanc.nedaire.util.database.NDatabase;
import com.google.gson.JsonArray;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NDiffuserRecipeBuilder extends NFinishedRecipe<NDiffuserRecipeBuilder> 
{
	boolean isManual = false;

	protected NDiffuserRecipeBuilder() 
	{
		super(NDiffuserRecipe.SERIALIZER.get().get());
		addWriter(jsonObject -> jsonObject.addProperty(NDatabase.Recipes.Types.Crusher.IS_MANUAL, isManual));
	}

	public static NDiffuserRecipeBuilder builder(Item result)
	{
		return new NDiffuserRecipeBuilder().addResult(result);
	}

	public static NDiffuserRecipeBuilder builder(ItemStack result)
	{
		return new NDiffuserRecipeBuilder().addResult(result);
	}

	public static NDiffuserRecipeBuilder builder(TagKey<Item> result, int count)
	{
		return new NDiffuserRecipeBuilder().addResult(new IngredientWithSize(result, count));
	}

	public static NDiffuserRecipeBuilder builder(IngredientWithSize result)
	{
		return new NDiffuserRecipeBuilder().addResult(result);
	}
	
	public NDiffuserRecipeBuilder setManual(boolean isManual)
	{
		this.isManual = isManual;
		return this;
	}

	
}

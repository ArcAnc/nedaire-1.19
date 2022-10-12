/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.serializers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.arcanc.nedaire.data.crafting.IngredientWithSize;
import com.arcanc.nedaire.data.crafting.StackWithChance;
import com.arcanc.nedaire.util.database.NDatabase;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition.IContext;
import net.minecraftforge.common.util.Lazy;

public abstract class NRecipeSerializer <T extends Recipe<?>> implements RecipeSerializer<T> 
{
	public abstract ItemStack getIcon();
	
	@Override
	public T fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject object) 
	{
		return null;
	}
	
	@Override
	public T fromJson (ResourceLocation recipeId, JsonObject object, IContext context)
	{
		if (CraftingHelper.processConditions(object, "conditions", context))
			return readFromJson(recipeId, object, context);
		return null;
	}
	
	public abstract T readFromJson(ResourceLocation recipeId, JsonObject object, IContext context);
	
	protected static Lazy<ItemStack> readOutput(JsonElement outputObject)
	{
		if(outputObject.isJsonObject() && outputObject.getAsJsonObject().has("item"))
			return Lazy.of(() -> ShapedRecipe.itemStackFromJson(outputObject.getAsJsonObject()));
		IngredientWithSize outgredient = IngredientWithSize.deserialize(outputObject);
		/* FIXME: заменить на выбор нужного стака относительно мода*/
		return Lazy.of(() -> outgredient.getMatchingStacks()[0]);
	}
	
	@Nullable
	protected static StackWithChance readConditionalStackWithChance(JsonElement element, IContext context)
	{
		JsonObject object = element.getAsJsonObject();
		if(CraftingHelper.processConditions(object, "conditions", context))
		{
			float chance = GsonHelper.getAsFloat(object, NDatabase.Recipes.StackWithChanceNBT.CHANCE);
			Lazy<ItemStack> stack = readOutput(object.get("output"));
			return new StackWithChance(stack, chance);
		}
		return null;
	}
	
	protected static Lazy<ItemStack> readLazyStack(FriendlyByteBuf buf)
	{
		ItemStack stack = buf.readItem();
		return Lazy.of(() -> stack);
	}

	protected static void writeLazyStack(FriendlyByteBuf buf, Lazy<ItemStack> stack)
	{
		buf.writeItem(stack.get());
	}
}

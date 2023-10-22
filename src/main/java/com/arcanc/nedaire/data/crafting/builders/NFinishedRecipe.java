/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.builders;

import com.arcanc.nedaire.data.crafting.FluidTagInput;
import com.arcanc.nedaire.data.crafting.IngredientWithSize;
import com.arcanc.nedaire.data.crafting.serializers.NRecipeSerializer;
import com.arcanc.nedaire.util.database.NDatabase;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NFinishedRecipe<T extends NFinishedRecipe<T>> implements FinishedRecipe 
{
	/**
	 * TODO: completely move all generation code to codecs. FUCKING CODECS!
	 */

	private final NRecipeSerializer<?> serializer;
	private final List<Consumer<JsonObject>> writerFunctions;
	private ResourceLocation id;

	protected JsonArray inputArray = null;
	protected int inputCount = 0;
	protected int maxInputCount = 1;

	protected JsonArray resultArray = null;
	protected int resultCount = 0;
	protected int maxResultCount = 1;

	protected JsonArray conditions = null;
	
	protected NFinishedRecipe(NRecipeSerializer<?> serializer)
	{
		this.serializer = serializer;
		this.writerFunctions = new ArrayList<>();
	}

	protected boolean isComplete()
	{
		return true;
	}

	public void build(RecipeOutput out, ResourceLocation id)
	{
		Preconditions.checkArgument(isComplete(), "This recipe is incomplete");
		this.id = id;
		out.accept(this);
	}

	@SuppressWarnings("unchecked")
	public T addWriter(Consumer<JsonObject> writer)
	{
		Preconditions.checkArgument(id==null, "This recipe has already been finalized");
		this.writerFunctions.add(writer);
		return (T)this;
	}

/*	@SuppressWarnings("unchecked")
	public T addCondition(ICondition condition)
	{
		if(this.conditions==null)
		{
			this.conditions = new JsonArray();
			addWriter(jsonObject -> jsonObject.add("conditions", conditions));
		}
		this.conditions.add(CraftingHelper.serialize(condition));
		return (T)this;
	}
*/
	/* =============== Common Objects =============== */

	public T setTime(int time)
	{
		return addWriter(jsonObject -> jsonObject.addProperty(NDatabase.Recipes.TIME, time));
	}

	public T setEnergy(int energy)
	{
		return addWriter(jsonObject -> jsonObject.addProperty(NDatabase.Recipes.ENERGY, energy));
	}

	/* =============== Result Handling =============== */

	public T setMultipleResults(int maxResultCount)
	{
		this.resultArray = new JsonArray();
		this.maxResultCount = maxResultCount;
		return addWriter(jsonObject -> jsonObject.add(NDatabase.Recipes.RESULT, resultArray));
	}

	@SuppressWarnings("unchecked")
	public T addMultiResult(JsonElement obj)
	{
		Preconditions.checkArgument(maxResultCount > 1, "This recipe does not support multiple results");
		Preconditions.checkArgument(resultCount < maxResultCount, "Recipe can only have "+maxResultCount+" results");
		resultArray.add(obj);
		resultCount++;
		return (T)this;
	}

	public T addResult(ItemLike itemProvider)
	{
		return addResult(new ItemStack(itemProvider));
	}

	public T addResult(ItemStack itemStack)
	{
		if(resultArray!=null)
			return addMultiResult(serializeItemStack(itemStack));
		else
			return addItem(NDatabase.Recipes.RESULT, itemStack);
	}

	public T addResult(Ingredient ingredient)
	{
		if(resultArray!=null)
			return addMultiResult(ingredient.toJson(false));
		else
			return addWriter(jsonObject -> jsonObject.add(NDatabase.Recipes.RESULT, ingredient.toJson(false)));
	}

	public T addResult(IngredientWithSize ingredientWithSize)
	{
		if(resultArray!=null)
			return addMultiResult(ingredientWithSize.toJson());
		else
			return addWriter(jsonObject -> jsonObject.add(NDatabase.Recipes.RESULT, ingredientWithSize.toJson()));
	}

	/* =============== Input Handling =============== */

	public T setUseInputArray(int maxInputCount, String key)
	{
		this.inputArray = new JsonArray();
		this.maxInputCount = maxInputCount;
		return addWriter(jsonObject -> jsonObject.add(key, inputArray));
	}

	public T setUseInputArray(int maxInputCount)
	{
		return setUseInputArray(maxInputCount, NDatabase.Recipes.INPUT);
	}

	@SuppressWarnings("unchecked")
	public T addMultiInput(JsonElement obj)
	{
		Preconditions.checkArgument(maxInputCount > 1, "This recipe does not support multiple inputs");
		Preconditions.checkArgument(inputCount < maxInputCount, "Recipe can only have "+maxInputCount+" inputs");
		inputArray.add(obj);
		inputCount++;
		return (T)this;
	}

	public T addMultiInput(Ingredient ingredient)
	{
		return addMultiInput(ingredient.toJson(false));
	}

	public T addMultiInput(IngredientWithSize ingredient)
	{
		return addMultiInput(ingredient.toJson());
	}

	protected String generateSafeInputKey()
	{
		Preconditions.checkArgument(inputCount < maxInputCount, "Recipe can only have "+maxInputCount+" inputs");
		String key = maxInputCount==1?"input": "input"+inputCount;
		inputCount++;
		return key;
	}

	public T addInput(ItemLike... itemProviders)
	{
		if(inputArray!=null)
			return addMultiInput(Ingredient.of(itemProviders));
		else
			return addIngredient(generateSafeInputKey(), itemProviders);
	}

	public T addInput(ItemStack... itemStacks)
	{
		if(inputArray!=null)
			return addMultiInput(Ingredient.of(itemStacks));
		else
			return addIngredient(generateSafeInputKey(), itemStacks);
	}

	public T addInput(TagKey<Item> tag)
	{
		if(inputArray!=null)
			return addMultiInput(Ingredient.of(tag));
		else
			return addIngredient(generateSafeInputKey(), tag);
	}

	public T addInput(Ingredient input)
	{
		if(inputArray!=null)
			return addMultiInput(input);
		else
			return addIngredient(generateSafeInputKey(), input);
	}

	public T addInput(IngredientWithSize input)
	{
		if(inputArray!=null)
			return addMultiInput(input);
		else
			return addIngredient(generateSafeInputKey(), input);
	}

	/* =============== ItemStacks =============== */

	@SuppressWarnings("deprecation")
	public JsonElement serializeItemStack(ItemStack stack)
	{
		return Util.getOrThrow(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack), IllegalStateException :: new);
	}

	@SuppressWarnings("deprecation")
	protected T addSimpleItem(String key, ItemLike item)
	{
		return addWriter(json -> json.addProperty(key, BuiltInRegistries.ITEM.getKey(item.asItem()).toString()));
	}

	public T addItem(String key, ItemLike item)
	{
		return addItem(key, new ItemStack(item));
	}

	public T addItem(String key, ItemStack stack)
	{
		Preconditions.checkArgument(!stack.isEmpty(), "May not add empty ItemStack to recipe");
		return addWriter(jsonObject -> jsonObject.add(key, serializeItemStack(stack)));
	}

	/* =============== Ingredients =============== */

	public T addIngredient(String key, ItemLike... itemProviders)
	{
		return addIngredient(key, Ingredient.of(itemProviders));
	}

	public T addIngredient(String key, ItemStack... itemStacks)
	{
		return addIngredient(key, Ingredient.of(itemStacks));
	}

	public T addIngredient(String key, TagKey<Item> tag)
	{
		return addIngredient(key, Ingredient.of(tag));
	}

	public T addIngredient(String key, Ingredient ingredient)
	{
		return addWriter(jsonObject -> jsonObject.add(key, ingredient.toJson(false)));
	}

	public T addIngredient(String key, IngredientWithSize ingredient)
	{
		return addWriter(jsonObject -> jsonObject.add(key, ingredient.toJson()));
	}

	/* =============== Fluids =============== */

	public T addFluid(String key, FluidStack fluidStack)
	{
		return addWriter(jsonObject -> jsonObject.add(key, Util.getOrThrow(FluidStack.CODEC.encodeStart(JsonOps.INSTANCE, fluidStack), IllegalStateException::new)));
	}

	public T addFluid(FluidStack fluidStack)
	{
		return addFluid(NDatabase.Recipes.FLUID, fluidStack);
	}

	public T addFluid(Fluid fluid, int amount)
	{
		return addFluid(NDatabase.Recipes.FLUID, new FluidStack(fluid, amount));
	}

	public T addFluidTag(String key, FluidTagInput fluidTag)
	{
		return addWriter(jsonObject -> jsonObject.add(key, fluidTag.serialize()));
	}

	public T addFluidTag(String key, TagKey<Fluid> fluidTag, int amount)
	{
		return addFluidTag(key, new FluidTagInput(fluidTag, amount, null));
	}

	public T addFluidTag(TagKey<Fluid> fluidTag, int amount)
	{
		return addFluidTag(NDatabase.Recipes.FLUID, new FluidTagInput(fluidTag, amount, null));
	}

	/* =============== IFinishedRecipe =============== */

	@Override
	public void serializeRecipeData(@NotNull JsonObject jsonObject)
	{
		for(Consumer<JsonObject> writer : this.writerFunctions)
			writer.accept(jsonObject);
	}

	@Override
	public @NotNull ResourceLocation id()
	{
		return id;
	}

	@Override
	public @NotNull RecipeSerializer<?> type()
	{
		return serializer;
	}

	@Nullable
	@Override
	public AdvancementHolder advancement()
	{
		return null;
	}

	@Nullable
	@Override
	public AdvancementData advancementData()
	{
		return null;
	}

	protected static JsonObject serializeStackWithChance(IngredientWithSize ingredient, float chance, ICondition... conditions)
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("chance", chance);
		jsonObject.add("output", ingredient.toJson());
		if(conditions.length > 0)
		{
			JsonArray conditionArray = new JsonArray();
//			for(ICondition condition : conditions)
//				conditionArray.add(CraftingHelper.serialize(condition));
			jsonObject.add("conditions", conditionArray);
		}
		return jsonObject;
	}
}

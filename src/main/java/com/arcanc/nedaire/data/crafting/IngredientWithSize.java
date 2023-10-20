/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class IngredientWithSize extends Ingredient implements Predicate<ItemStack>
{

	protected final Ingredient ingredient;
	protected final int amount;

	public IngredientWithSize(Stream<? extends Value> values, int amount)
	{
		super(values);
		this.ingredient = fromValues(values);
		this.amount = amount;
	}

	public IngredientWithSize(Stream<? extends  Value> values)
	{
		this(values, 1);
	}

	public IngredientWithSize(Ingredient ingredient)
	{
		this(Arrays.stream(ingredient.getItems()).map(ItemValue::new));
	}

	public IngredientWithSize(Ingredient ingredient, int amount)
	{
		this(Arrays.stream(ingredient.getItems()).map(ItemValue::new), amount);
	}
	public IngredientWithSize(TagKey<Item> ingredient, int amount)
	{
		this(Ingredient.of(ingredient), amount);
	}

	public IngredientWithSize(TagKey<Item> ingredient)
	{
		this(ingredient, 1);
	}

	@Override
	public boolean test(@Nullable ItemStack itemStack)
	{
		if(itemStack==null)
			return false;
		return ingredient.test(itemStack)&&itemStack.getCount() >= this.amount;
	}

	@Nonnull
	public ItemStack[] getMatchingStacks()
	{
		ItemStack[] baseStacks = ingredient.getItems();
		ItemStack[] ret = new ItemStack[baseStacks.length];
		for(int i = 0; i < baseStacks.length; ++i)
			ret[i] = ItemHandlerHelper.copyStackWithSize(baseStacks[i], this.amount);
		return ret;
	}

	@Override
	public IIngredientSerializer<IngredientWithSize> serializer()
	{
		return NRegistration.RegisterIngredients.INGREDIENT_WITH_SIZE.get();
	}

	public boolean hasNoMatchingItems()
	{
		return ingredient.isEmpty();
	}

	public int getAmount()
	{
		return amount;
	}

	public Ingredient getIngredient()
	{
		return ingredient;
	}
	public static IngredientWithSize of(ItemStack stack)
	{
		return new IngredientWithSize(Ingredient.of(stack), stack.getCount());
	}

	public JsonElement toJson()
	{
		return Util.getOrThrow(IngredientWithSizeSerializer.CODEC.encodeStart(JsonOps.INSTANCE, this), IllegalStateException :: new);
	}

	public ItemStack getRandomizedExampleStack(int rand)
	{
		ItemStack[] all = getMatchingStacks();
		if (all.length == 0)
			return ItemStack.EMPTY;
		else
			return all[rand%all.length];
	}

	public boolean testIgnoringSize(ItemStack itemstack)
	{
		return ingredient.test(itemstack);
	}
}

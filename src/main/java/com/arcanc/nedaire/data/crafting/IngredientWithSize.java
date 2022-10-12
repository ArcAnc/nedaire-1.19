/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonElement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.ItemHandlerHelper;

public class IngredientWithSize implements Predicate<ItemStack> 
{
	public static final IIngredientWithSizeSerializer SERIALIZER = IngredientWithSizeSerializer.INSTANCE;
	protected final Ingredient basePredicate;
	protected final int count;

	public IngredientWithSize(Ingredient basePredicate, int count)
	{
		this.basePredicate = basePredicate;
		this.count = count;
	}

	public IngredientWithSize(Ingredient basePredicate)
	{
		this(basePredicate, 1);
	}

	public IngredientWithSize(TagKey<Item> basePredicate, int count)
	{
		this(Ingredient.of(basePredicate), count);
	}

	public IngredientWithSize(TagKey<Item> basePredicate)
	{
		this(basePredicate, 1);
	}

	public static IngredientWithSize deserialize(JsonElement input)
	{
		return SERIALIZER.parse(input);
	}

	public static IngredientWithSize read(FriendlyByteBuf input)
	{
		return SERIALIZER.parse(input);
	}

	@Override
	public boolean test(@Nullable ItemStack itemStack)
	{
		if(itemStack==null)
			return false;
		return basePredicate.test(itemStack)&&itemStack.getCount() >= this.count;
	}

	@Nonnull
	public ItemStack[] getMatchingStacks()
	{
		ItemStack[] baseStacks = basePredicate.getItems();
		ItemStack[] ret = new ItemStack[baseStacks.length];
		for(int i = 0; i < baseStacks.length; ++i)
			ret[i] = ItemHandlerHelper.copyStackWithSize(baseStacks[i], this.count);
		return ret;
	}

	@Nonnull
	public List<ItemStack> getMatchingStackList()
	{
		return Arrays.asList(getMatchingStacks());
	}

	@Nonnull
	public JsonElement serialize()
	{
		return SERIALIZER.write(this);
	}

	public boolean hasNoMatchingItems()
	{
		return basePredicate.isEmpty();
	}

	public int getCount()
	{
		return count;
	}

	public Ingredient getBaseIngredient()
	{
		return basePredicate;
	}

	public IngredientWithSize withSize(int size)
	{
		return new IngredientWithSize(this.basePredicate, size);
	}

	public static IngredientWithSize of(ItemStack stack)
	{
		return new IngredientWithSize(Ingredient.of(stack), stack.getCount());
	}

	public ItemStack getRandomizedExampleStack(int rand)
	{
		ItemStack[] all = getMatchingStacks();
		if (all.length == 0)
			return ItemStack.EMPTY;
		else
			return all[(rand/20)%all.length];
	}

	public boolean testIgnoringSize(ItemStack itemstack)
	{
		return basePredicate.test(itemstack);
	}

	public void write(FriendlyByteBuf out)
	{
		SERIALIZER.write(out, this);
	}
}

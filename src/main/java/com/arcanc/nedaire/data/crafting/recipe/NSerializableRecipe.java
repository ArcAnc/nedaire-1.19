/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.recipe;

import com.arcanc.nedaire.data.crafting.serializers.NRecipeSerializer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;

public abstract class NSerializableRecipe implements Recipe<Container> 
{
	public static final Lazy<ItemStack> LAZY_EMPTY = of(ItemStack.EMPTY);

	protected final Lazy<ItemStack> outputDummy;
	protected final RecipeType<?> type;
	protected final ResourceLocation id;

	protected <T extends Recipe<?>>	NSerializableRecipe(Lazy<ItemStack> outputDummy, RecipeType<T> type, ResourceLocation id)
	{
		this.outputDummy = outputDummy;
		this.type = type;
		this.id = id;
	}

	@Override
	public boolean isSpecial()
	{
		return true;
	}

	@Override
	public ItemStack getToastSymbol()
	{
		return getNSerializer().getIcon();
	}

	@Override
	public boolean matches(Container inv, Level worldIn)
	{
		return false;
	}

	@Override
	public ItemStack assemble(Container inv)
	{
		return this.outputDummy.get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return false;
	}

	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return getNSerializer();
	}

	protected abstract NRecipeSerializer<?> getNSerializer();

	@Override
	public RecipeType<?> getType()
	{
		return this.type;
	}

	public static Lazy<ItemStack> of(ItemStack value) 
	{
		return Lazy.of(() -> value);
	}
}

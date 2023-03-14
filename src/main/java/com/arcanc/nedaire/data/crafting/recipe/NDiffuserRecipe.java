/**
 * @author ArcAnc
 * Created at: 2023-03-12
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.recipe;

import java.util.Arrays;
import java.util.Optional;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.CachedRecipeList;
import com.arcanc.nedaire.data.crafting.serializers.NDiffuserRecipeSerializer;
import com.arcanc.nedaire.data.crafting.serializers.NRecipeSerializer;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

public class NDiffuserRecipe extends NRecipe
{
	public static Lazy<RegistryObject<NDiffuserRecipeSerializer>> SERIALIZER = Lazy.of(() -> NRegistration.RegisterRecipes.DIFFUSER_SERIALIZER);
	public static final CachedRecipeList<NDiffuserRecipe> RECIPES = new CachedRecipeList<>(NRegistration.RegisterRecipes.Types.DIFFUSER);

	public final Ingredient input;
	public final Lazy<ItemStack> output;
	public final FluidStack fluid;
	public final boolean isManual;
	
	public NDiffuserRecipe(ResourceLocation id, Lazy<ItemStack> output, Ingredient input, int time, FluidStack fluid, boolean isManual) 
	{
		super(output, NRegistration.RegisterRecipes.Types.DIFFUSER.get(), id);
		this.output = output;
		this.input = input;
		setTimeAndEnergy(time, 0);
		this.fluid = fluid;
		this.isManual = isManual;
		
		setInputList(Arrays.asList(input));
		this.outputList = Lazy.of(() -> NonNullList.of(ItemStack.EMPTY, this.output.get()));
	}

	@Override
	public int getMultipleProcessTicks() 
	{
		return 4;
	}

	@Override
	protected NRecipeSerializer<?> getNSerializer() 
	{
		return SERIALIZER.get().get();
	}
	
	public static Optional<NDiffuserRecipe> findRecipe (Level level, ItemStack stack, FluidStack fluidS)
	{
		return RECIPES.getRecipes(level).stream().
				filter(recipe -> recipe.input.test(stack) && recipe.fluid.isFluidEqual(fluidS)).
				findFirst();
	}
	
}

/**
 * @author ArcAnc
 * Created at: 2023-03-12
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.recipe;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.CachedRecipeList;
import com.arcanc.nedaire.data.crafting.serializers.NDiffuserRecipeSerializer;
import com.arcanc.nedaire.data.crafting.serializers.NRecipeSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.Optional;

public class NDiffuserRecipe extends NRecipe
{
	public static Lazy<RegistryObject<NDiffuserRecipeSerializer>> SERIALIZER = Lazy.of(() -> NRegistration.RegisterRecipes.DIFFUSER_SERIALIZER);
	public static final CachedRecipeList<NDiffuserRecipe> RECIPES = new CachedRecipeList<>(NRegistration.RegisterRecipes.Types.DIFFUSER);

	public final FluidStack fluid;

	public NDiffuserRecipe(ItemStack output, Ingredient input, int time, FluidStack fluid)
	{
		super(Lazy.of(() -> output), NRegistration.RegisterRecipes.Types.DIFFUSER.get());
		setTimeAndEnergy(time, 0);
		this.fluid = fluid;

		setInputList(Collections.singletonList(input));
		this.outputList = Lazy.of(() -> NonNullList.of(ItemStack.EMPTY, output));
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

	public ItemStack getOutput()
	{
		return outputList.get().get(0);
	}

	public Ingredient getInput()
	{
		return inputList.get(0);
	}

	public FluidStack getFluid()
	{
		return fluid.copy();
	}
	
	public static Optional<NDiffuserRecipe> findRecipe (Level level, ItemStack stack, FluidStack fluidS)
	{
		return RECIPES.getRecipes(level).stream().
				filter(recipe -> recipe.getInput().test(stack) && recipe.getFluid().isFluidEqual(fluidS)).
				findFirst();
	}
	
}

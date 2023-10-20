/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.recipe;

import com.arcanc.nedaire.data.crafting.FluidTagInput;
import com.arcanc.nedaire.data.crafting.IngredientWithSize;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.stream.Collectors;

public abstract class NRecipe extends NSerializableRecipe implements INMachineRecipe
{

	protected List<IngredientWithSize> inputList = new ArrayList<>(0);
	protected Lazy<NonNullList<ItemStack>> outputList = Lazy.of(NonNullList::create);

	protected <T extends Recipe<?>> NRecipe(Lazy<ItemStack> outputDummy, RecipeType<T> type)
	{
		super(outputDummy, type);
	}

	@Override
	public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registry)
	{
		NonNullList<ItemStack> outputs = getItemOutputs();
		if(outputs!=null&&outputs.size() > 0)
			return outputs.get(0);
		return ItemStack.EMPTY;
	}

	@Override
	public List<IngredientWithSize> getItemInputs()
	{
		return inputList;
	}

	protected void setInputListWithSizes(List<IngredientWithSize> inputList)
	{
		this.inputList = new ArrayList<>(inputList);
	}

	protected void setInputList(List<Ingredient> inputList)
	{
		this.inputList = inputList.stream()
				.map(IngredientWithSize::new)
				.collect(Collectors.toList());
	}

	@Override
	public NonNullList<ItemStack> getItemOutputs()
	{
		return outputList.get();
	}

	protected List<FluidTagInput> fluidInputList;

	@Override
	public List<FluidTagInput> getFluidInputs()
	{
		return fluidInputList;
	}

	protected List<FluidStack> fluidOutputList;

	@Override
	public List<FluidStack> getFluidOutputs()
	{
		return fluidOutputList;
	}

	Lazy<Integer> totalProcessTime;

	@Override
	public int getTotalProcessTime()
	{
		return this.totalProcessTime.get();
	}

	Lazy<Integer> totalProcessEnergy;

	@Override
	public int getTotalProcessEnergy()
	{
		return this.totalProcessEnergy.get();
	}

	void setTimeAndEnergy(int time, int energy)
	{
		totalProcessEnergy = Lazy.of(() -> energy);
		totalProcessTime = Lazy.of(() -> time);
	}

	public void modifyTimeAndEnergy(DoubleSupplier timeModifier, DoubleSupplier energyModifier)
	{
		final Lazy<Integer> oldTime = totalProcessTime;
		final Lazy<Integer> oldEnergy = totalProcessEnergy;
		this.totalProcessTime = Lazy.of(() -> (int)(Math.max(1, oldTime.get()*timeModifier.getAsDouble())));
		this.totalProcessEnergy = Lazy.of(() -> (int)(Math.max(1, oldEnergy.get()*energyModifier.getAsDouble())));
	}
}

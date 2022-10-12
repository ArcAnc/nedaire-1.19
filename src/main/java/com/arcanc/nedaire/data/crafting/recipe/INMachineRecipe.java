/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.recipe;

import java.util.List;

import com.arcanc.nedaire.data.FluidTagInput;
import com.arcanc.nedaire.data.crafting.IngredientWithSize;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

public interface INMachineRecipe 
{
	List<IngredientWithSize> getItemInputs();

	default boolean shouldCheckItemAvailability()
	{
		return true;
	}

	List<FluidTagInput> getFluidInputs();

	NonNullList<ItemStack> getItemOutputs();

	default NonNullList<ItemStack> getActualItemOutputs(BlockEntity tile)
	{
		return getItemOutputs();
	}

	List<FluidStack> getFluidOutputs();

	default ItemStack getDisplayStack(ItemStack input)
	{
		for(IngredientWithSize ingr : getItemInputs())
			if(ingr.test(input))
			{
				if(ingr.hasNoMatchingItems())
					return input;
				else
					return ingr.getMatchingStacks()[0];
			}
		return ItemStack.EMPTY;
	}

	default List<FluidStack> getActualFluidOutputs(BlockEntity tile)
	{
		return getFluidOutputs();
	}

	int getTotalProcessTime();

	int getTotalProcessEnergy();

	int getMultipleProcessTicks();
}

/**
 * @author ArcAnc
 * Created at: 2022-10-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.recipe;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.CachedRecipeList;
import com.arcanc.nedaire.data.crafting.IngredientWithSize;
import com.arcanc.nedaire.data.crafting.StackWithChance;
import com.arcanc.nedaire.data.crafting.serializers.NCrusherRecipeSerializer;
import com.arcanc.nedaire.data.crafting.serializers.NRecipeSerializer;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class NCrusherRecipe extends NRecipe 
{
	public static Lazy<RegistryObject<NCrusherRecipeSerializer>> SERIALIZER = Lazy.of(() -> NRegistration.RegisterRecipes.CRUSHER_SERIALIZER);
	public static final CachedRecipeList<NCrusherRecipe> RECIPES = new CachedRecipeList<>(NRegistration.RegisterRecipes.Types.MANUAL_CRUSHER);

	public final List<StackWithChance> secondaryOutputs = Lists.newArrayList();

	public NCrusherRecipe(ItemStack output, Ingredient input, int energy)
	{
		super(Lazy.of(() -> output), NRegistration.RegisterRecipes.Types.MANUAL_CRUSHER.get());
		setTimeAndEnergy(50, energy);

		
		setInputList(Collections.singletonList(input));
		this.outputList = Lazy.of(() -> NonNullList.of(ItemStack.EMPTY, output));
	}

	public NCrusherRecipe(IngredientWithSize output, Ingredient input, int energy)
	{
		this(output.getRandomizedExampleStack(0),input, energy);
	}

	@Override
	protected NRecipeSerializer<?> getNSerializer() 
	{
		return SERIALIZER.get().get();
	}
	
	@Override
	public NonNullList<ItemStack> getActualItemOutputs(BlockEntity tile) 
	{
		NonNullList<ItemStack> list = NonNullList.create();
		
		list.add(getOutput());
		
		for(StackWithChance out : secondaryOutputs)
		{
			ItemStack stack = out.stack().get();
			if (!stack.isEmpty() && tile.getLevel().random.nextFloat() < out.chance())
			{
				list.add(stack);
			}
		}
		
		return list;
	}
	
	public NCrusherRecipe addSecondaryOutput (StackWithChance out)	
	{
		Preconditions.checkNotNull(out);
		secondaryOutputs.add(out);
		
		return this;
	}
	
	public static @NotNull Optional<NCrusherRecipe> findRecipe(Level level, ItemStack input)
	{
		return RECIPES.getRecipes(level).stream().
		filter(recipe -> recipe.getInput().test(input)).
		findFirst();
	}

	@Override
	public int getMultipleProcessTicks() 
	{
		return 4;
	}

	public ItemStack getOutput()
	{
		return outputList.get().get(0);
	}

	public Ingredient getInput()
	{
		return inputList.get(0);
	}
}

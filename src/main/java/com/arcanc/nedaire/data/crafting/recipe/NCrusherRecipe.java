/**
 * @author ArcAnc
 * Created at: 2022-10-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.recipe;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.Lists;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.CachedRecipeList;
import com.arcanc.nedaire.data.crafting.StackWithChance;
import com.arcanc.nedaire.data.crafting.serializers.NCrusherRecipeSerializer;
import com.arcanc.nedaire.data.crafting.serializers.NRecipeSerializer;
import com.google.common.base.Preconditions;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.RegistryObject;

public class NCrusherRecipe extends NRecipe 
{
	public static Lazy<RegistryObject<NCrusherRecipeSerializer>> SERIALIZER = Lazy.of(() -> NRegistration.RegisterRecipes.CRUSHER_SERIALIZER);
	public static final CachedRecipeList<NCrusherRecipe> RECIPES = new CachedRecipeList<>(NRegistration.RegisterRecipes.Types.MANUAL_CRUSHER);
	
	public final Ingredient input;
	public final Lazy<ItemStack> output;
	public final List<StackWithChance> secondaryOutputs = Lists.newArrayList();
	public final boolean isManual;
	
	public NCrusherRecipe(ResourceLocation id, Lazy<ItemStack> output, Ingredient input, int energy, boolean isManual) 
	{
		super(output, NRegistration.RegisterRecipes.Types.MANUAL_CRUSHER.get(), id);
		this.output = output;
		this.input = input;
		setTimeAndEnergy(50, energy);
		this.isManual = isManual;
		
		
		setInputList(Arrays.asList(input));
		this.outputList = Lazy.of(() -> NonNullList.of(ItemStack.EMPTY, this.output.get()));
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
		
		list.add(output.get());
		
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
	
	public static Optional<NCrusherRecipe> findRecipe(Level level, ItemStack input)
	{
		
/*		for (NCrusherRecipe recipe : RECIPES.getRecipes(level))
			if (recipe.input.test(input))
				return recipe;
*/		return RECIPES.getRecipes(level).stream().
		filter(recipe -> recipe.input.test(input)).
		findFirst();

	}
	
	@Override
	public int getMultipleProcessTicks() 
	{
		return 4;
	}
}

/**
 * @author ArcAnc
 * Created at: 2023-03-14
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.serializers;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.recipe.NDiffuserRecipe;
import com.arcanc.nedaire.util.database.NDatabase;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ICondition.IContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;

public class NDiffuserRecipeSerializer extends NRecipeSerializer<NDiffuserRecipe> 
{

	@Override
	public ItemStack getIcon() 
	{
		return new ItemStack(NRegistration.RegisterBlocks.DIFFUSER.get());
	}
	
	@Override
	public NDiffuserRecipe readFromJson(ResourceLocation recipeId, JsonObject json, IContext context)
	{
		Lazy<ItemStack> output = readOutput(json.get(NDatabase.Recipes.Types.Diffuser.RESULT));
		Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, NDatabase.Recipes.Types.Diffuser.INPUT));
		int time = GsonHelper.getAsInt(json, NDatabase.Recipes.Types.Diffuser.TIME);
		boolean isManual = GsonHelper.getAsBoolean(json, NDatabase.Recipes.Types.Crusher.IS_MANUAL);
		FluidStack fluid = readFluidStack(json.getAsJsonObject(NDatabase.Recipes.Types.Diffuser.FLUID));
		NDiffuserRecipe recipe = /*FIXME: добавить прогрузку конфига сюда:IEServerConfig.MACHINES.crusherConfig.apply(*/new NDiffuserRecipe(recipeId, output, input, time, fluid, isManual);
		return recipe;
	}
	
	@Nullable
	@Override
	public NDiffuserRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer)
	{
		ItemStack output = buffer.readItem();
		Ingredient input = Ingredient.fromNetwork(buffer);
		int time = buffer.readInt();
		boolean isManual = buffer.readBoolean();
		FluidStack fluid = buffer.readFluidStack();
		NDiffuserRecipe recipe = new NDiffuserRecipe(recipeId, Lazy.of(() -> output), input, time, fluid, isManual);
		return recipe;
	}	
	
	@Override
	public void toNetwork(FriendlyByteBuf buffer, NDiffuserRecipe recipe)
	{
		buffer.writeItem(recipe.output.get());
		recipe.input.toNetwork(buffer);
		buffer.writeInt(recipe.getTotalProcessTime());
		buffer.writeBoolean(recipe.isManual);
		buffer.writeFluidStack(recipe.fluid);
	}

}

/**
 * @author ArcAnc
 * Created at: 2022-10-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.serializers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.StackWithChance;
import com.arcanc.nedaire.data.crafting.recipe.NCrusherRecipe;
import com.arcanc.nedaire.util.database.NDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ICondition.IContext;
import net.minecraftforge.common.util.Lazy;

public class NCrusherRecipeSerializer extends NRecipeSerializer<NCrusherRecipe> 
{

	@Override
	public ItemStack getIcon() 
	{
		return new ItemStack(NRegistration.RegisterBlocks.MANUAL_CRUSHER);
	}

	@Override
	public NCrusherRecipe readFromJson(ResourceLocation recipeId, JsonObject json, IContext context)
	{
		Lazy<ItemStack> output = readOutput(json.get(NDatabase.Recipes.Types.Crusher.RESULT));
		Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, NDatabase.Recipes.Types.Crusher.INPUT));
		JsonArray array = json.getAsJsonArray(NDatabase.Recipes.Types.Crusher.SECONDARIES);
		int energy = GsonHelper.getAsInt(json, NDatabase.Recipes.Types.Crusher.ENERGY);
		boolean isManual = GsonHelper.getAsBoolean(json, NDatabase.Recipes.Types.Crusher.IS_MANUAL);
		NCrusherRecipe recipe = /*FIXME: add config loading here:IEServerConfig.MACHINES.crusherConfig.apply(*/new NCrusherRecipe(recipeId, output, input, energy, isManual);
		for(int i = 0; i < array.size(); i++)
		{
			StackWithChance secondary = readConditionalStackWithChance(array.get(i), context);
			if(secondary!=null)
				recipe.addSecondaryOutput(secondary);
		}
		return recipe;
	}

	@Nullable
	@Override
	public NCrusherRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer)
	{
		ItemStack output = buffer.readItem();
		Ingredient input = Ingredient.fromNetwork(buffer);
		int energy = buffer.readInt();
		boolean isManual = buffer.readBoolean();
		int secondaryCount = buffer.readInt();
		NCrusherRecipe recipe = new NCrusherRecipe(recipeId, Lazy.of(() -> output), input, energy, isManual);
		for(int i = 0; i < secondaryCount; i++)
			recipe.addSecondaryOutput(StackWithChance.read(buffer));
		return recipe;
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, NCrusherRecipe recipe)
	{
		buffer.writeItem(recipe.output.get());
		recipe.input.toNetwork(buffer);
		buffer.writeInt(recipe.getTotalProcessEnergy());
		buffer.writeBoolean(recipe.isManual);
		buffer.writeInt(recipe.secondaryOutputs.size());
		for(StackWithChance secondaryOutput : recipe.secondaryOutputs)
			secondaryOutput.write(buffer);
	}

}

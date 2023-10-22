/**
 * @author ArcAnc
 * Created at: 2023-03-14
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.serializers;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.recipe.NDiffuserRecipe;
import com.arcanc.nedaire.util.database.NDatabase;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class NDiffuserRecipeSerializer extends NRecipeSerializer<NDiffuserRecipe> 
{

	public static final Codec<NDiffuserRecipe> CODEC = RecordCodecBuilder.create(instance ->
			instance.group
			(
				ItemStack.CODEC.fieldOf(NDatabase.Recipes.RESULT).forGetter(NDiffuserRecipe :: getOutput),
				Ingredient.CODEC_NONEMPTY.fieldOf(NDatabase.Recipes.INPUT).forGetter(NDiffuserRecipe::getInput),
				Codec.INT.fieldOf(NDatabase.Recipes.TIME).forGetter(NDiffuserRecipe ::getTotalProcessTime),
				FluidStack.CODEC.fieldOf(NDatabase.Recipes.FLUID).forGetter(NDiffuserRecipe::getFluid)
			).apply(instance, NDiffuserRecipe :: new));

	@Override
	public ItemStack getIcon() 
	{
		return new ItemStack(NRegistration.RegisterBlocks.DIFFUSER.get());
	}

	@Override
	public Codec<NDiffuserRecipe> codec()
	{
		return CODEC;
	}

	/*	@Override
        public NDiffuserRecipe readFromJson(ResourceLocation recipeId, JsonObject json, IContext context)
        {
            Lazy<ItemStack> output = readOutput(json.get(NDatabase.Recipes.Types.Diffuser.RESULT));
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, NDatabase.Recipes.Types.Diffuser.INPUT));
            int time = GsonHelper.getAsInt(json, NDatabase.Recipes.Types.Diffuser.TIME);
            boolean isManual = GsonHelper.getAsBoolean(json, NDatabase.Recipes.Types.Crusher.IS_MANUAL);
            FluidStack fluid = readFluidStack(json.getAsJsonObject(NDatabase.Recipes.Types.Diffuser.FLUID));
            NDiffuserRecipe recipe = /*FIXME: add config loading here:IEServerConfig.MACHINES.crusherConfig.apply(new NDiffuserRecipe(recipeId, output, input, time, fluid, isManual);
            return recipe;
        }
    */
	@Nullable
	@Override
	public NDiffuserRecipe fromNetwork(FriendlyByteBuf buffer)
	{
		ItemStack output = buffer.readItem();
		Ingredient input = Ingredient.fromNetwork(buffer);
		int time = buffer.readInt();
	//	boolean isManual = buffer.readBoolean();
		FluidStack fluid = buffer.readFluidStack();
		NDiffuserRecipe recipe = new NDiffuserRecipe(output, input, time, fluid);
		return recipe;
	}	
	
	@Override
	public void toNetwork(FriendlyByteBuf buffer, NDiffuserRecipe recipe)
	{
		buffer.writeItem(recipe.getOutput());
		recipe.getInput().toNetwork(buffer);
		buffer.writeInt(recipe.getTotalProcessTime());
	//	buffer.writeBoolean(recipe.isManual);
		buffer.writeFluidStack(recipe.fluid);
	}

}

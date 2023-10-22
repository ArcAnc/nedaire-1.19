/**
 * @author ArcAnc
 * Created at: 2022-10-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.serializers;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.IngredientWithSize;
import com.arcanc.nedaire.data.crafting.IngredientWithSizeSerializer;
import com.arcanc.nedaire.data.crafting.StackWithChance;
import com.arcanc.nedaire.data.crafting.recipe.NCrusherRecipe;
import com.arcanc.nedaire.util.database.NDatabase;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class NCrusherRecipeSerializer extends NRecipeSerializer<NCrusherRecipe> 
{

	private static final Codec<NCrusherRecipe> CODEC = RecordCodecBuilder.create(instance ->
			instance.group
			(
					ExtraCodecs.xor(ItemStack.CODEC, IngredientWithSizeSerializer.CODEC).
							xmap(either ->
							{
								return either.map(stack ->
								{
									return stack;
								}, ingredient ->
								{
									return ingredient;
								});
							}, value ->
							{
								if (value instanceof ItemStack stack)
									return Either.left(stack);
								else if (value instanceof IngredientWithSize ingredient)
									return Either.right(ingredient);
								else
									throw new UnsupportedOperationException("This is neither an item value nor a ingredient value.");
							}).
							fieldOf(NDatabase.Recipes.RESULT).forGetter(NCrusherRecipe :: getOutput),
					Ingredient.CODEC_NONEMPTY.fieldOf(NDatabase.Recipes.INPUT).forGetter(NCrusherRecipe :: getInput),
					Codec.INT.fieldOf(NDatabase.Recipes.ENERGY).forGetter(NCrusherRecipe :: getTotalProcessEnergy)
			).apply(instance, (output, input, energy) -> new NCrusherRecipe(output instanceof ItemStack outStack ? outStack : ((IngredientWithSize)output).getRandomizedExampleStack(0) , input, energy)));

	@Override
	public ItemStack getIcon() 
	{
		return new ItemStack(NRegistration.RegisterBlocks.MANUAL_CRUSHER);
	}

	@Override
	public Codec<NCrusherRecipe> codec()
	{
		return CODEC;
	}

	/*	@Override
        public NCrusherRecipe readFromJson(ResourceLocation recipeId, JsonObject json, IContext context)
        {
            Lazy<ItemStack> output = readOutput(json.get(NDatabase.Recipes.Types.Crusher.RESULT));
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, NDatabase.Recipes.Types.Crusher.INPUT));
            JsonArray array = json.getAsJsonArray(NDatabase.Recipes.Types.Crusher.SECONDARIES);
            int energy = GsonHelper.getAsInt(json, NDatabase.Recipes.Types.Crusher.ENERGY);
            boolean isManual = GsonHelper.getAsBoolean(json, NDatabase.Recipes.Types.Crusher.IS_MANUAL);
            NCrusherRecipe recipe = /*FIXME: add config loading here:IEServerConfig.MACHINES.crusherConfig.apply(new NCrusherRecipe(recipeId, output, input, energy, isManual);

            for(int i = 0; i < array.size(); i++)
            {
                StackWithChance secondary = readConditionalStackWithChance(array.get(i), context);
                if(secondary!=null)
                    recipe.addSecondaryOutput(secondary);
            }
            return recipe;
        }
    */
	@Nullable
	@Override
	public NCrusherRecipe fromNetwork(@NotNull FriendlyByteBuf buffer)
	{
		ItemStack output = buffer.readItem();
		Ingredient input = Ingredient.fromNetwork(buffer);
		int energy = buffer.readInt();
		// boolean isManual = buffer.readBoolean();
		int secondaryCount = buffer.readInt();
		NCrusherRecipe recipe = new NCrusherRecipe(output, input, energy);
		for(int i = 0; i < secondaryCount; i++)
			recipe.addSecondaryOutput(StackWithChance.read(buffer));
		return recipe;
	}

	@Override
	public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull NCrusherRecipe recipe)
	{
		buffer.writeItem(recipe.getOutput());
		recipe.getInput().toNetwork(buffer);
		buffer.writeInt(recipe.getTotalProcessEnergy());
//		buffer.writeBoolean(recipe.isManual);
		buffer.writeInt(recipe.secondaryOutputs.size());
		for(StackWithChance secondaryOutput : recipe.secondaryOutputs)
			secondaryOutput.write(buffer);
	}

}

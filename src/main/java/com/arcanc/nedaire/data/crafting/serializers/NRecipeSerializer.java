/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.serializers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

public abstract class NRecipeSerializer <T extends Recipe<?>> implements RecipeSerializer<T> 
{
	public abstract ItemStack getIcon();

	/*
	@Override
	public T fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject object) 
	{
		return null;
	}
	
	@Override
	public T fromJson (ResourceLocation recipeId, JsonObject object, IContext context)
	{
		if (CraftingHelper.processConditions(object, "conditions", context))
			return readFromJson(recipeId, object, context);
		return null;
	}
	
	public abstract T readFromJson(ResourceLocation recipeId, JsonObject object, IContext context);
	
	protected static Lazy<ItemStack> readOutput(JsonElement outputObject)
	{
		if(outputObject.isJsonObject() && outputObject.getAsJsonObject().has("item"))
			return Lazy.of(() -> ShapedRecipe.itemStackFromJson(outputObject.getAsJsonObject()));
		IngredientWithSize outIngredient = IngredientWithSize.deserialize(outputObject);
		// FIXME: change to choosing required stack depending on mc mod
		return Lazy.of(() -> outIngredient.getMatchingStacks()[0]);
	}
	
	@Nullable
	protected static StackWithChance readConditionalStackWithChance(JsonElement element, IContext context)
	{
		JsonObject object = element.getAsJsonObject();
		if(CraftingHelper.processConditions(object, "conditions", context))
		{
			float chance = GsonHelper.getAsFloat(object, NDatabase.Recipes.StackWithChanceNBT.CHANCE);
			Lazy<ItemStack> stack = readOutput(object.get("output"));
			return new StackWithChance(stack, chance);
		}
		return null;
	}
*/
	protected static @NotNull Lazy<ItemStack> readLazyStack(@NotNull FriendlyByteBuf buf)
	{
		ItemStack stack = buf.readItem();
		return Lazy.of(() -> stack);
	}

	protected static void writeLazyStack(@NotNull FriendlyByteBuf buf, @NotNull Lazy<ItemStack> stack)
	{
		buf.writeItem(stack.get());
	}
	
/*
	protected static FluidStack readFluidStack(JsonObject jsonObject)
	{
		Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(jsonObject, "fluid")));
		int amount = GsonHelper.getAsInt(jsonObject, "amount");
		FluidStack fluidStack = new FluidStack(fluid, amount);
		if(GsonHelper.isValidNode(jsonObject, "tag"))
			fluidStack.setTag(JsonUtils.readNBT(jsonObject, "tag"));
		return fluidStack;
	}
	*/
}

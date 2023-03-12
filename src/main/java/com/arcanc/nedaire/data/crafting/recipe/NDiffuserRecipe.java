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
import com.arcanc.nedaire.data.crafting.serializers.NRecipeSerializer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

public class NDiffuserRecipe extends NRecipe
{
//	public static Lazy<RegistryObject<NCrusherRecipeSerializer>> SERIALIZER = Lazy.of(() -> NRegistration.RegisterRecipes.CRUSHER_SERIALIZER);
	public static final CachedRecipeList<NDiffuserRecipe> RECIPES = new CachedRecipeList<>(NRegistration.RegisterRecipes.Types.DIFFUSER);

	protected NDiffuserRecipe(Lazy<ItemStack> outputDummy, ResourceLocation id) 
	{
		super(outputDummy, NRegistration.RegisterRecipes.Types.DIFFUSER.get(), id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getMultipleProcessTicks() 
	{
		return 4;
	}

	@Override
	protected NRecipeSerializer<?> getNSerializer() 
	{
		return null;
	}
	
}

/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;

import net.minecraft.network.FriendlyByteBuf;

public interface IIngredientWithSizeSerializer 
{
	IngredientWithSize parse(@Nonnull FriendlyByteBuf buffer);
	void write(@Nonnull FriendlyByteBuf buffer, @Nonnull IngredientWithSize ingredient);
	IngredientWithSize parse(@Nonnull JsonElement json);
	JsonElement write(@Nonnull IngredientWithSize ingredient);
}

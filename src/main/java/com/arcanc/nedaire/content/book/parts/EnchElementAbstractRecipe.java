/**
 * @author ArcAnc
 * Created at: 2022-09-21
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Optional;

public abstract class EnchElementAbstractRecipe extends EnchElementAbstract 
{

	protected ResourceLocation location;
	protected Optional<? extends Recipe<?>> recipe;
	
	public EnchElementAbstractRecipe(EnchiridionInstance instance, ResourceLocation location, int x, int y, int width, int height) 
	{
		super(instance, x, y, width, height);
		
		this.location = location;
		
		Minecraft mc = instance.getScreen().getMinecraft();
		recipe = Optional.ofNullable(mc.level.getRecipeManager().byKey(location).get().value());
	}
	
	public ItemStack getStackAtCurrentTime(Ingredient ingr)
	{
		if(ingr.getItems().length == 0)
			return ItemStack.EMPTY;

		int perm = (int)(System.currentTimeMillis()/1000%ingr.getItems().length);
		return ingr.getItems()[perm];
	}
	
	public int animateArrow (int ticks)
	{
		return (int) (System.currentTimeMillis()/ticks%22);
	}

}

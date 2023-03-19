/**
 * @author ArcAnc
 * Created at: 2022-04-01
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting.recipe;

import com.arcanc.nedaire.content.item.weapon.NShieldBase;
import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import net.minecraft.world.level.Level;

public class NShieldRecipes extends ShieldDecorationRecipe
{
	   
	public NShieldRecipes(ResourceLocation location, CraftingBookCategory category)
	{
		super(location, category);
	}
	
	public NShieldRecipes(ResourceLocation location) 
	{
		this(location, CraftingBookCategory.EQUIPMENT);
	}

	   
	@Override
	public boolean matches(CraftingContainer inventory, Level world) 
	{
		ItemStack itemstack = ItemStack.EMPTY;
		ItemStack itemstack1 = ItemStack.EMPTY;

		for(int i = 0; i < inventory.getContainerSize(); ++i) 
		{
			ItemStack itemstack2 = inventory.getItem(i);
		    if (!itemstack2.isEmpty()) 
		    {
		    	if (itemstack2.getItem() instanceof BannerItem) 
		    	{
		    		if (!itemstack1.isEmpty()) 
		    		{
		    			return false;
		            }

		    		itemstack1 = itemstack2;
		        } 
		    	else 
		        {
		    		if (!(itemstack2.getItem() instanceof NShieldBase)) 
		        	{
		    			return false;
		            }

		            if (!itemstack.isEmpty()) 
		            {
		            	return false;
		            }

		            if (itemstack2.getTagElement("BlockEntityTag") != null) 
		            {
		            	return false;
		            }

		            itemstack = itemstack2;
		         }
		    }
		}

		return !itemstack.isEmpty() && !itemstack1.isEmpty();
	}

	@Override
	public ItemStack assemble(CraftingContainer inventory, RegistryAccess registry) 
	{
		ItemStack itemstack = ItemStack.EMPTY;
		ItemStack itemstack1 = ItemStack.EMPTY;

		for(int i = 0; i < inventory.getContainerSize(); ++i) 
		{
			ItemStack itemstack2 = inventory.getItem(i);
			if (!itemstack2.isEmpty()) 
			{
				if (itemstack2.getItem() instanceof BannerItem) 
				{
					itemstack = itemstack2;
				} 
				else if (itemstack2.getItem() instanceof NShieldBase) 
				{
					itemstack1 = itemstack2.copy();
				}
			}
		}

		if (itemstack1.isEmpty()) 
		{
			return itemstack1;
		} 
		else 
		{
			CompoundTag compoundnbt = itemstack.getTagElement("BlockEntityTag");
			CompoundTag compoundnbt1 = compoundnbt == null ? new CompoundTag() : compoundnbt.copy();
			compoundnbt1.putInt("Base", ((BannerItem)itemstack.getItem()).getColor().getId());
			itemstack1.addTagElement("BlockEntityTag", compoundnbt1);
			return itemstack1;
		}
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) 
	{
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() 
	{
		return NRegistration.RegisterRecipes.SHIELD_SERIALIZER.get();
	}

}

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
import org.jetbrains.annotations.NotNull;

public class NShieldRecipes extends ShieldDecorationRecipe
{
	   
	public NShieldRecipes(CraftingBookCategory category)
	{
		super(category);
	}
	
	public NShieldRecipes()
	{
		this(CraftingBookCategory.EQUIPMENT);
	}

	   
	@Override
	public boolean matches(CraftingContainer inventory, @NotNull Level world)
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
	public @NotNull ItemStack assemble(CraftingContainer inventory, @NotNull RegistryAccess registry)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		ItemStack itemstack1 = ItemStack.EMPTY;

		for(int i = 0; i < inventory.getContainerSize(); ++i) 
		{
			ItemStack itemStack2 = inventory.getItem(i);
			if (!itemStack2.isEmpty())
			{
				if (itemStack2.getItem() instanceof BannerItem)
				{
					itemstack = itemStack2;
				} 
				else if (itemStack2.getItem() instanceof NShieldBase)
				{
					itemstack1 = itemStack2.copy();
				}
			}
		}

		if (!itemstack1.isEmpty())
		{
			CompoundTag compoundTag = itemstack.getTagElement("BlockEntityTag");
			CompoundTag compoundTag1 = compoundTag == null ? new CompoundTag() : compoundTag.copy();
			compoundTag1.putInt("Base", ((BannerItem) itemstack.getItem()).getColor().getId());
			itemstack1.addTagElement("BlockEntityTag", compoundTag1);
		}
		return itemstack1;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) 
	{
		return width * height >= 2;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer()
	{
		return NRegistration.RegisterRecipes.SHIELD_SERIALIZER.get();
	}

}

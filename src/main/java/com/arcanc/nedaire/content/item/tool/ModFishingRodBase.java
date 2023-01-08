/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.tool;

import com.arcanc.nedaire.content.item.ItemInterfaces.ICustomModelProperties;
import com.arcanc.nedaire.content.material.tool.ModAbstractToolMaterial;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;

public class ModFishingRodBase extends FishingRodItem implements ICustomModelProperties
{
	public ModFishingRodBase(ModAbstractToolMaterial toolMat) 
	{
		super(new Item.Properties().defaultDurability(toolMat.getUses()));
	}
	
	@Override
	public String getDescriptionId() 
	{
		return ItemHelper.getRegistryName(this).toString().replace(':', '.').replace('/', '.');
	}

	@Override
	public void registerModelProperties() 
	{
		ItemProperties.register(this, 
				   new ResourceLocation("cast"), (stack, world, living, integer) -> 
		   {
	           if (living == null) 
	           {
	               return 0.0f;
	           }
	           boolean flag1 = living.getMainHandItem() == stack;
	           
	           boolean flag2 = living.getOffhandItem() == stack;
	           
	           if (living.getMainHandItem().getItem() instanceof FishingRodItem) 
	           {
	               flag2 = false;
	           }
	           return ((flag1 || flag2) && 
	        		  living instanceof Player && 
	        		  ((Player)living).fishing != null) ? 1.0f : 0.0f;
	        });
	}

}

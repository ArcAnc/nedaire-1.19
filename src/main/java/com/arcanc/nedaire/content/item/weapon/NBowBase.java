/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.weapon;

import com.arcanc.nedaire.content.item.ItemInterfaces.ICustomModelProperties;
import com.arcanc.nedaire.content.material.tool.ModAbstractToolMaterial;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;

public class NBowBase extends BowItem implements ICustomModelProperties
{
	protected final ModAbstractToolMaterial material;
	
	public NBowBase(ModAbstractToolMaterial material) 
	{
		super(new Item.Properties().defaultDurability(material.getUses()));
	
		this.material = material;
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
				new ResourceLocation("pulling"), (stack, world, living, integer) -> 
		{
			return  living != null && 
					living.isUsingItem() && 
					living.getUseItem() == stack ? 1.0F : 0.0F;
		});
		ItemProperties.register(this, 
				new ResourceLocation("pull"), (stack, world, living, integer) ->
		{
			return living != null && 
				   living.isUsingItem() && 
				   living.getUseItem() == stack ? (living.getTicksUsingItem()) / 20.0F: 0.0f;
		});
		
	}
}

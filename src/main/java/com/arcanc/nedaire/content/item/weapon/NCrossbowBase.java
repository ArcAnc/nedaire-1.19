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
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class NCrossbowBase extends CrossbowItem implements ICustomModelProperties
{
	protected final ModAbstractToolMaterial material;
	
	public NCrossbowBase(ModAbstractToolMaterial toolMat) 
	{
		super(new Item.Properties().defaultDurability(toolMat.getUses()));
		
		this.material = toolMat;
	}
	
	@Override
	public boolean useOnRelease(ItemStack stack) 
	{
		return stack.getItem() instanceof CrossbowItem;
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
				   new ResourceLocation("pull"), (stack, world, living, integer) -> 
		   {
			   return living != null && 
					  !CrossbowItem.isCharged(stack) ? (living.getTicksUsingItem()) / (float)CrossbowItem.getChargeDuration(stack) : 0.0f;
		   });
			ItemProperties.register(this, 
				   new ResourceLocation("pulling"), (stack, world, living, integer) -> 
		   {
			   return living != null && 
					  living.isUsingItem() && 
					  living.getUseItem() == stack && 
					  !CrossbowItem.isCharged(stack) ? 1.0f : 0.0f;
		   });

			ItemProperties.register(this, 
				   new ResourceLocation("charged"), (stack, world, living, integer) ->
		   {
			   return living != null && 
					  CrossbowItem.isCharged(stack) ? 1.0f : 0.0f;
		   });
		   
			ItemProperties.register(this, 
				   new ResourceLocation("firework"), (stack, world, living, integer) -> 
		   {
			   return living != null && 
					  CrossbowItem.isCharged(stack) && 
					  CrossbowItem.containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0f : 0.0f ;
		   });
		
	}
}

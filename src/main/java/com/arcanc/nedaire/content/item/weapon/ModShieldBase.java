/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.weapon;

import java.util.function.Consumer;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.item.ItemInterfaces.ICustomModelProperties;
import com.arcanc.nedaire.content.material.tool.ModAbstractToolMaterial;
import com.arcanc.nedaire.content.renderer.item.shieldRenderer.ShieldTileEntityRenderer;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ModShieldBase extends ShieldItem implements ICustomModelProperties
{
	public static ShieldTileEntityRenderer shieldRenderer;
	
	protected final ModAbstractToolMaterial material;

	public ModShieldBase(ModAbstractToolMaterial toolMat) 
	{
		super(new Item.Properties().tab(Nedaire.getInstance().TAB).defaultDurability(toolMat.getUses()));
	
		this.material = toolMat;
	}
	
	/**
	 * @return the material
	 */
	public ModAbstractToolMaterial getMaterial() 
	{
		return material;
	}
	
	@Override
	public boolean isRepairable(ItemStack stack) 
	{
		return material.getRepairIngredient().test(stack) || super.isRepairable(stack);
	}
	
/*	@Override
	public boolean isShield(ItemStack stack, LivingEntity entity) 
	{
		return stack.getItem() instanceof ShieldItem;
	}
*/	
	@Override
	public String getDescriptionId() 
	{
		return ItemHelper.getRegistryName(this).toString().replace(':', '.').replace('/', '.');
	}
	
	@Override
	public String getDescriptionId(ItemStack stack) 
	{
	      return stack.getTagElement("BlockEntityTag") != null ? getDescriptionId() + "." + getColor(stack).getName() : getDescriptionId();
	}

	@Override
	public void registerModelProperties() 
	{
		ItemProperties.register(this, 
				   new ResourceLocation("blocking"), (stack, world, living, integer) -> 
		   {
			   return living != null && 
					  living.isUsingItem() && 
					  living.getUseItem() == stack ? 1.0f : 0.0f;  
		   });

	}	
	
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) 
	{
		consumer.accept(new IClientItemExtensions()
				{
					
					@Override
					public BlockEntityWithoutLevelRenderer getCustomRenderer() 
					{
						return shieldRenderer;
					}
				});
	}
	
	public static void registerReloadListener(final RegisterClientReloadListenersEvent event)
	{
		event.registerReloadListener(ModShieldBase.shieldRenderer = new ShieldTileEntityRenderer(
				Minecraft.getInstance().getBlockEntityRenderDispatcher(), 
				Minecraft.getInstance().getEntityModels()));
	}

}

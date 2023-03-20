/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.armor;

import com.arcanc.nedaire.content.material.armor.player.NAbstractPlayerArmorMaterial;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NArmorBase extends ArmorItem 
{
	public NArmorBase(NAbstractPlayerArmorMaterial armorMat, ArmorItem.Type slot) 
	{
		super(armorMat, slot, new Item.Properties().stacksTo(1));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) 
	{
		if (slot == EquipmentSlot.LEGS)
			return getArmorMat().getTexturePathSecondary();
		return getArmorMat().getTexturePathMain();
	}
	
	/**
	 * @return the armorMat
	 */
	public NAbstractPlayerArmorMaterial getArmorMat() 
	{
		return (NAbstractPlayerArmorMaterial)getMaterial();
	}
	
	@Override
	public String getDescriptionId() 
	{
		return ItemHelper.getRegistryName(this).toString().replace(':', '.').replace('/', '.');
	}
}

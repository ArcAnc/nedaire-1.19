/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.armor;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.material.armor.player.ModAbstractPlayerArmorMaterial;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModArmorBase extends ArmorItem 
{
	public ModArmorBase(ModAbstractPlayerArmorMaterial armorMat, EquipmentSlot slot) 
	{
		super(armorMat, slot, new Item.Properties().tab(Nedaire.getInstance().TAB).stacksTo(1));
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
	public ModAbstractPlayerArmorMaterial getArmorMat() 
	{
		return (ModAbstractPlayerArmorMaterial)getMaterial();
	}
	
	@Override
	public String getDescriptionId() 
	{
		return ItemHelper.getRegistryName(this).toString().replace(':', '.').replace('/', '.');
	}
}

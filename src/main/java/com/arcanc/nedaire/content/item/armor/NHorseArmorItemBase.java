/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.armor;

import java.util.List;

import com.arcanc.nedaire.content.material.armor.horse.NHorseArmorMaterial;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class NHorseArmorItemBase extends HorseArmorItem
{
	public NHorseArmorItemBase(NHorseArmorMaterial material) 
	{
		super(material.getDefense(), material.getTexturePath(), new Item.Properties().stacksTo(1));
	}

	@Override
	public String getDescriptionId() 
	{
		return ItemHelper.getRegistryName(this).toString().replace(':', '.').replace('/', '.');
	}
	
	@Override
	public void appendHoverText(ItemStack p_41421_, Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) 
	{
		p_41423_.add(Component.empty());
		p_41423_.add(Component.translatable(NDatabase.MOD_ID + ".item.modifiers.horse").withStyle(ChatFormatting.GRAY));
		p_41423_.add(Component.translatable("attribute.modifier.plus.0", this.getProtection(), Component.translatable("attribute.name.generic.armor")).withStyle(ChatFormatting.BLUE));
	}

}

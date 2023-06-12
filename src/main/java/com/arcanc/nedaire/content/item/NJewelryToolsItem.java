/**
 * @author ArcAnc
 * Created at: 2023-03-10
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item;

import com.arcanc.nedaire.util.helpers.WorldHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

public class NJewelryToolsItem extends NBaseItem 
{

	public NJewelryToolsItem(Properties props) 
	{
		super(props.defaultDurability(100));
	}

	@Override
	public int getMaxDamage(ItemStack stack) 
	{
		return 250;
	}
	
	@Override
	public ItemStack getCraftingRemainingItem(ItemStack itemStack) 
	{
		ItemStack stack = itemStack.copy();
		if (stack.hurt(1, WorldHelper.RANDOM_SOURCE, null))
			return ItemStack.EMPTY;
		else
			return stack;
	}
	
	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack) 
	{
		return true;
	}
	
	@Override
	public boolean canBeDepleted() 
	{
		return true;
	}
	
	@Override
	public boolean isEnchantable(@NotNull ItemStack stack)
	{
		return true;
	}
	
	@Override
	public int getEnchantmentValue() 
	{
		return 14;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) 
	{
		return enchantment == Enchantments.BLOCK_EFFICIENCY || enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.MENDING;
	}
	
	@Override
	public boolean isValidRepairItem(@NotNull ItemStack stack, ItemStack repairCandidate)
	{
		return repairCandidate.is(Tags.Items.INGOTS_IRON);
	}
	
}

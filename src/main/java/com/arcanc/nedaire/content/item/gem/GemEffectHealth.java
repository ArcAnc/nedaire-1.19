/**
 * @author ArcAnc
 * Created at: 2022-06-14
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.gem;

import java.awt.Color;

import com.arcanc.nedaire.util.database.ModDatabase;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GemEffectHealth <T extends GemEffect<T>> extends GemEffect<T> 
{

	private int healthAmount;
	
	public GemEffectHealth(Color color, int healthAmount) 
	{
		super(color, ModDatabase.Capabilities.Socket.Health.NAME);
		
		this.healthAmount = healthAmount;
	}
	
	public GemEffectHealth(Color color)
	{
		this(color, 3);
	}

	@Override
	protected boolean isTicker() 
	{
		return false;
	}

	@Override
	protected void tick(ItemStack stack, Level level, LivingEntity entity) 
	{
		
	}

	@Override
	protected void applyInstateousEffect(ItemStack stack, Level level, LivingEntity entity) 
	{
		if (entity != null)
		{
			AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
			if (instance != null)
			{
				AttributeModifier modifier = new AttributeModifier(getId(), getDescriptionId(), healthAmount, AttributeModifier.Operation.ADDITION);
				instance.addPermanentModifier(modifier);
			}
		}
	}

	@Override
	protected void removeInstanteousEffect(ItemStack stack, Level level, LivingEntity entity) 
	{
		if (entity != null)
		{
			AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
			if (instance != null)
			{
				instance.removePermanentModifier(getId());
			}
		}
	}

	@Override
	protected GemEffectHealth<T> loadEffect(CompoundTag tag) 
	{
		super.loadEffect(tag);
		if (tag.contains(ModDatabase.Capabilities.Socket.Health.AMOUNT))
		{
			this.healthAmount = tag.getInt(ModDatabase.Capabilities.Socket.Health.AMOUNT);
		}
		
		return this;
	}
	
	@Override
	protected CompoundTag saveEffect() 
	{
		CompoundTag tag = super.saveEffect();
		
		tag.putInt(ModDatabase.Capabilities.Socket.Health.AMOUNT, healthAmount);
		
		return tag;
	}
}

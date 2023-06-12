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

import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GemEffectRegen <T extends GemEffect<T>> extends GemEffect <T> 
{
	
	/**
	 * In ticks
	 */
	private final int period;
	private int healPower;
	
	public GemEffectRegen(Color color, int period, int power) 
	{
		super(color, NDatabase.Capabilities.Socket.Regeneration.NAME);
		
		this.period = period;
		this.healPower = power;
	}
	
	public GemEffectRegen(Color color) 
	{
		this(color, 200, 1);
	}

	@Override
	protected boolean isTicker() 
	{
		return true;
	}

	@Override
	protected void tick(ItemStack stack, Level level, LivingEntity entity) 
	{
		if (!stack.isEmpty() && level != null && entity != null && entity.isAlive())
		{
			CompoundTag tag = stack.getOrCreateTag();
			long lastTick = tag.getLong(NDatabase.Capabilities.Socket.Regeneration.LAST_TICK);
			long curTime = level.getGameTime();
			if (lastTick + period <= curTime)
			{
				if (entity.getHealth() < entity.getMaxHealth())
				{
					entity.heal(healPower);
				}
			
			tag.putLong(NDatabase.Capabilities.Socket.Regeneration.LAST_TICK, curTime);
			}
		}
	}

	@Override
	protected void applyInstantaneousEffect(ItemStack stack, Level level, LivingEntity entity)
	{
	}

	@Override
	protected void removeInstantaneousEffect(ItemStack stack, Level level, LivingEntity entity)
	{
		
	}

	@Override
	protected GemEffectRegen<T> loadEffect(CompoundTag tag) 
	{
		
		super.loadEffect(tag);
		
		if (!tag.contains(NDatabase.Capabilities.Socket.Regeneration.LAST_TICK))
		{
			tag.putLong(NDatabase.Capabilities.Socket.Regeneration.LAST_TICK, 0);
		}
		if (tag.contains(NDatabase.Capabilities.Socket.Regeneration.HEAL_POWER))
		{
			this.healPower = tag.getInt(NDatabase.Capabilities.Socket.Regeneration.HEAL_POWER);
		}
		if (tag.contains(NDatabase.Capabilities.Socket.Regeneration.PERIOD))
		{
			this.healPower = tag.getInt(NDatabase.Capabilities.Socket.Regeneration.PERIOD);
		}
		return this;
	}
	
	@Override
	protected CompoundTag saveEffect() 
	{
		CompoundTag tag = super.saveEffect(); 
		
		tag.putInt(NDatabase.Capabilities.Socket.Regeneration.HEAL_POWER, healPower);
		tag.putInt(NDatabase.Capabilities.Socket.Regeneration.PERIOD, period);
		
		return tag;
	}

}

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
import java.util.UUID;

import javax.annotation.Nonnull;

import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class GemEffect <T extends GemEffect<T>>
{
	private UUID id;
	protected String descriptionId;
	private Color color;
	private String name;
	
	public GemEffect(UUID id, @Nonnull Color color, String name) 
	{
		this.color = color;
		this.name = name;
		this.id = id;
	}
	
	public GemEffect(Color color, String name)
	{
		this(UUID.randomUUID(), color, name);
	}

	public UUID getId() 
	{
		return id;
	}
	
	public Color getColor() 
	{
		return color;
	}
	
	public String getName() 
	{
		return name;
	}
	
	protected abstract boolean isTicker();
	protected abstract void tick (ItemStack stack, Level level, LivingEntity entity);
	protected abstract void applyInstantaneousEffect(ItemStack stack, Level level, LivingEntity entity);
	protected abstract void removeInstantaneousEffect(ItemStack stack, Level level, LivingEntity entity);
	
	protected CompoundTag saveEffect ()
	{
		CompoundTag tag = new CompoundTag();
		
		tag.putUUID(NDatabase.Capabilities.Socket.ID, id);
		tag.putInt(NDatabase.Capabilities.Socket.COLOR, color.getRGB());
		tag.putString(NDatabase.Capabilities.Socket.NAME, name);
		
		return tag;
	}
	
	protected GemEffect<T> loadEffect (CompoundTag tag)
	{
		if (tag.contains(NDatabase.Capabilities.Socket.ID))
		{
			this.id = tag.getUUID(NDatabase.Capabilities.Socket.ID);
			this.color = new Color(tag.getInt(NDatabase.Capabilities.Socket.COLOR));
			this.name = tag.getString(NDatabase.Capabilities.Socket.NAME);
		}
		
		return this;
	}
	
	protected String getOrCreateDescriptionId() 
	{
		if (this.descriptionId == null) 
		{
			this.descriptionId = StringHelper.getStrLocFStr("gem_effect." + name).replace(':', '.');
		}
		return this.descriptionId;
	}

	public String getDescriptionId() 
	{
		return this.getOrCreateDescriptionId();
	}
	
	public Component getDisplayDescription() 
	{
		return Component.translatable(this.getDescriptionId());
	}
}

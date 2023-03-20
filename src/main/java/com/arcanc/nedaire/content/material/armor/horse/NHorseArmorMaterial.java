/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.material.armor.horse;

import net.minecraft.resources.ResourceLocation;

public class NHorseArmorMaterial implements IHorseArmorMaterial 
{

	private final int durability;
	private final int defense;
	private final ResourceLocation texturePath;
	
	private NHorseArmorMaterial(Builder builder) 
	{
		this.durability = builder.durability;
		this.defense = builder.defense;
		this.texturePath = builder.texturePath;
	}
	
	@Override
	public int getDurability() 
	{
		return durability;
	}
	
	@Override
	public int getDefense() 
	{
		return defense;
	}
	
	@Override
	public ResourceLocation getTexturePath() 
	{
		return texturePath;
	}
	
	public static class Builder
	{
		private int durability;
		private int defense;
		private ResourceLocation texturePath;
	
		public Builder setDefense(int defense) 
		{
			this.defense = defense;
			return this;
		}
		
		public Builder setDurability(int durability) 
		{
			this.durability = durability;
			return this;
		}
		
		public Builder setTexturePath(ResourceLocation texturePath) 
		{
			this.texturePath = texturePath;
			return this;
		}
		
		public NHorseArmorMaterial build()
		{
			return new NHorseArmorMaterial(this);
		}
	}
}

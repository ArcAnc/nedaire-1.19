/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.material.armor.player;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class NAbstractPlayerArmorMaterial implements ArmorMaterial	 
{
	private final String name;
	private final int[] durability;
	private final int[] defense;
	private final float toughness;
	private final int enchantmentValue;
	private final SoundEvent equipSound;
	private final Supplier<Ingredient> repairIngredient;
	private final float knockbackResistance;
	private final String texturePathMain;
	private final String texturePathSecondary;
	
	protected NAbstractPlayerArmorMaterial(NAbstractPlayerArmorMaterial.PlayerArmorAbstractBuilder<?> builder) 
	{
		this.name = builder.name;
		this.durability = builder.durability;
		this.defense = builder.defense;
		this.toughness = builder.toughness;
		this.enchantmentValue = builder.enchantmentValue;
		this.equipSound = builder.equipSound;
		this.repairIngredient = builder.repairIngredient;
		this.knockbackResistance = builder.knockbackResistance;
		this.texturePathMain = builder.texturePathMain;
		this.texturePathSecondary = builder.texturePathSecondary;
	}
	
	@Override
	public int getDurabilityForType(ArmorItem.Type slot) 
	{
		return durability[slot.getSlot().getIndex()];
	}

	@Override
	public int getDefenseForType(ArmorItem.Type slot) 
	{
		return defense[slot.getSlot().getIndex()];
	}

	@Override
	public int getEnchantmentValue() 
	{
		return enchantmentValue;
	}

	@Override
	public SoundEvent getEquipSound() 
	{
		return equipSound;
	}

	@Override
	public Ingredient getRepairIngredient() 
	{
		return repairIngredient.get();
	}
	
	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public float getToughness() 
	{
		return toughness;
	}
	
	@Override
	public float getKnockbackResistance() 
	{
		return knockbackResistance;
	}
	
	/**
	 * @return the texturePathMain
	 */
	public String getTexturePathMain() 
	{
		return texturePathMain;
	}
	
	/**
	 * @return the texturePathSecondary
	 */
	public String getTexturePathSecondary() 
	{
		return texturePathSecondary;
	}

	public abstract static class PlayerArmorAbstractBuilder<T extends PlayerArmorAbstractBuilder<T>>
	{
		private String name;
		private int[] durability;
		private int[] defense;
		private float toughness;
		private int enchantmentValue;
		private SoundEvent equipSound;
		private Supplier<Ingredient> repairIngredient;
		private float knockbackResistance;
		private String texturePathMain;
		private String texturePathSecondary;
		
		public PlayerArmorAbstractBuilder() {}
		
		public T setName(String name) 
		{
			this.name = name;
			return getSelf();
		}
		
		public T setDurabilityForSlot(int[] durability) 
		{
			this.durability = durability;
			return getSelf();
		}
		
		public T setDurability(int durability) 
		{
			this.durability = new int[]{durability, durability, durability, durability};
			return getSelf();
		}
		
		public T setDefenseForSlot(int[] defense) 
		{
			this.defense = defense;
			return getSelf();
		}
		
		public T setToughness(float toughness) 
		{
			this.toughness = toughness;
			return getSelf();
		}
		
		public T setEnchantmentValue(int enchantmentValue) 
		{
			this.enchantmentValue = enchantmentValue;
			return getSelf();
		}
		
		public T setEquipSound(SoundEvent equipSound) 
		{
			this.equipSound = equipSound;
			return getSelf();
		}
		
		public T setRepairIngredient(Supplier<Ingredient> repairIngredient) 
		{
			this.repairIngredient = Suppliers.memoize(repairIngredient);
			return getSelf();
		}
		
		public T setKnockbackResistance(float knockbackResistance) 
		{
			this.knockbackResistance = knockbackResistance;
			return getSelf();
		}
		
		/**
		 * @param texturePathMain the texturePathMain to set
		 */
		public T setTexturePathMain(String texturePathMain) 
		{
			this.texturePathMain = texturePathMain;
			return getSelf();
		}
		
		/**
		 * @param texturePathSecondary the texturePathSecondary to set
		 */
		public T setTexturePathSecondary(String texturePathSecondary) 
		{
			this.texturePathSecondary = texturePathSecondary;
			return getSelf();
		}
		
		protected abstract T getSelf();
		
		public abstract NAbstractPlayerArmorMaterial build ();
	}

}

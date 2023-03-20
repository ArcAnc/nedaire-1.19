/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.material.tool;

import com.arcanc.nedaire.util.database.NDatabase.Items;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import net.minecraft.client.resources.model.Material;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public abstract class NAbstractToolMaterial implements Tier 
{
	private final int durability;
	private final float speed;
	private final float attackDamageBonus;
	private final float attackSpeed;
	private final int level;
	private final int enchantmentValue;
	private final Supplier<Ingredient> repairIngredient;
	
	private final Tier betterThan;
	private final Tier worseThan;
	
	private final TagKey<Block> tag;
	
	private final Material shieldBase;
	private final Material shieldNoPattern;
	
	public NAbstractToolMaterial(NAbstractToolMaterial.ToolAbstractBuilder<?> builder) 
	{
		this.durability = builder.durability;
		this.speed = builder.speed;
		this.attackDamageBonus = builder.attackDamageBonus;
		this.attackSpeed = builder.attackSpeed;
		this.level = builder.level;
		this.enchantmentValue = builder.enchantmentValue;
		this.repairIngredient = builder.repairIngredient;

		this.betterThan = builder.betterThan;
		this.worseThan = builder.worseThan;

		this.tag = builder.tag;
		
		this.shieldBase = builder.shieldBase;
		this.shieldNoPattern = builder.shieldNoPattern;
	}
	
	@Override
	public int getUses() 
	{
		return durability;
	}

	@Override
	public float getSpeed() 
	{
		return speed;
	}

	@Override
	public float getAttackDamageBonus() 
	{
		return attackDamageBonus;
	}
	
	public float getAttackSpeed() 
	{
		return attackSpeed;
	}

	@Override
	public int getLevel() 
	{
		return level;
	}

	@Override
	public int getEnchantmentValue() 
	{
		return enchantmentValue;
	}

	@Override
	public Ingredient getRepairIngredient() 
	{
		return repairIngredient.get();
	}
	
	@Override
	public TagKey<Block> getTag() 
	{
		return tag;
	}
	
	public Tier getBetterThan() 
	{
		return betterThan;
	}
	
	public Tier getWorseThan() 
	{
		return worseThan;
	}
	
	public Material getShieldBase() 
	{
		return shieldBase;
	}
	
	public Material getShieldNoPattern() 
	{
		return shieldNoPattern;
	}
	
	public abstract static class ToolAbstractBuilder<T extends ToolAbstractBuilder<T>>
	{
		private int durability;
		private float speed;
		private float attackDamageBonus;
		private float attackSpeed;
		private int level;
		private int enchantmentValue;
		private Supplier<Ingredient> repairIngredient;
		private TagKey<Block> tag;
		private Tier betterThan;
		private Tier worseThan;
		private Material shieldBase;
		private Material shieldNoPattern;
		
		public ToolAbstractBuilder() {}
		
		public T setDurability(int durability) 
		{
			this.durability = durability;
			return getSelf();
		}
		
		public T setSpeed(float speed) 
		{
			this.speed = speed;
			return getSelf();
		}
		
		public T setAttackDamageBonus(float attackDamageBonus) 
		{
			this.attackDamageBonus = attackDamageBonus;
			return getSelf();
		}
		
		public T setAttackSpeed(float attackSpeed) 
		{
			this.attackSpeed = attackSpeed;
			return getSelf();
		}
		
		public T setEnchantmentValue(int enchantmentValue) 
		{
			this.enchantmentValue = enchantmentValue;
			return getSelf();
		}
		
		public T setLevel(int level) 
		{
			this.level = level;
			return getSelf();
		}
		
		public T setRepairIngredient(Supplier<Ingredient> repairIngredient) 
		{
			this.repairIngredient = Suppliers.memoize(repairIngredient);
			return getSelf();
		}
		
		public T setTag(TagKey<Block> tag) 
		{
			this.tag = tag;
			return getSelf();
		}
		
		public T setBetterThan (Tier tier)
		{
			this.betterThan = tier;
			return getSelf();
		}
		
		public T setWorseThan (Tier tier)
		{
			this.worseThan = tier;
			return getSelf();
		}
		
		public T setShieldBaseRenderMaterial(String name)
		{
			this.shieldBase = new Material(InventoryMenu.BLOCK_ATLAS, StringHelper.getLocFStr(StringHelper.slashPlacer(name, "item", Items.Names.WEAPON, Items.Names.Weapon.SHIELD)));
			return getSelf();
		}
		
		public T setShieldNoPatternRenderMaterial(String name)
		{
			this.shieldNoPattern = new Material(InventoryMenu.BLOCK_ATLAS, StringHelper.getLocFStr(StringHelper.slashPlacer(name + "_no_pattern", "item", Items.Names.WEAPON, Items.Names.Weapon.SHIELD)));
			return getSelf();
		}
		
		protected abstract T getSelf();

		public abstract NAbstractToolMaterial build(); 
	}
}

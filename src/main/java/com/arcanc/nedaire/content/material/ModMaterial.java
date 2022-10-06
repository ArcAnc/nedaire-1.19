/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.material;

import java.util.List;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.block.ModBaseBlock;
import com.arcanc.nedaire.content.block.ModOreBlock;
import com.arcanc.nedaire.content.item.ModBaseItem;
import com.arcanc.nedaire.content.item.ModBlockItemBase;
import com.arcanc.nedaire.content.item.armor.ModArmorBase;
import com.arcanc.nedaire.content.item.armor.ModHorseArmorItemBase;
import com.arcanc.nedaire.content.item.tool.ModAxeBase;
import com.arcanc.nedaire.content.item.tool.ModFishingRodBase;
import com.arcanc.nedaire.content.item.tool.ModHoeBase;
import com.arcanc.nedaire.content.item.tool.ModPickaxeBase;
import com.arcanc.nedaire.content.item.tool.ModShearsBase;
import com.arcanc.nedaire.content.item.tool.ModShovelBase;
import com.arcanc.nedaire.content.item.weapon.ModBowBase;
import com.arcanc.nedaire.content.item.weapon.ModCrossbowBase;
import com.arcanc.nedaire.content.item.weapon.ModShieldBase;
import com.arcanc.nedaire.content.item.weapon.ModSwordBase;
import com.arcanc.nedaire.content.material.armor.horse.ModHorseArmorMaterial;
import com.arcanc.nedaire.content.material.armor.player.ModAbstractPlayerArmorMaterial;
import com.arcanc.nedaire.content.material.armor.player.ModPlayerArmorMaterial;
import com.arcanc.nedaire.content.material.tool.ModAbstractToolMaterial;
import com.arcanc.nedaire.content.material.tool.ModToolMaterial;
import com.arcanc.nedaire.content.registration.ModRegistration;
import com.arcanc.nedaire.util.database.ModDatabase;
import com.arcanc.nedaire.util.database.ModDatabase.Items;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.base.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.registries.RegistryObject;

public class ModMaterial 
{

	private final String name;
	private final ModAbstractToolMaterial toolMat;
	private final ModAbstractPlayerArmorMaterial armorMat;
	private final ModHorseArmorMaterial horseArmorMat;
	
	private static Supplier<Item.Properties> baseProps = () -> new Item.Properties().tab(Nedaire.getInstance().TAB);
	
	private final RegistryObject<Item> ingot, nugget, raw, dust,
	   pickaxe, axe, shovel, hoe, shears, fishingRod,
	   sword, shield, bow, crossbow,
	   armorHorse, playerArmorHead, playerArmorChest, playerArmorLegs, playerArmorFeet;
	
	private final boolean isRequiredOre;
	private final RegistryObject <? extends Block> storageBlock, rawStorageBlock, oreBlock, deepSlateOre;
	
	private ModMaterial(ModMaterialProperties props) 
	{
		this.name = props.name;
		
		this.ingot = register(StringHelper.slashPlacer(this.name, Items.Names.INGOT), () -> new ModBaseItem(baseProps.get()));
		
		this.toolMat = new ModToolMaterial.Builder().
				setDurability(props.toolDurability).
				setSpeed(props.toolSpeed).
				setAttackDamageBonus(props.toolAttackDamageBonus).
				setAttackSpeed(props.toolAttackSpeed).
				setLevel(props.toolLevel).
				setEnchantmentValue(props.toolEnchantmentValue).
				setRepairIngredient(props.toolRepairIngredient != null ? props.toolRepairIngredient : () -> Ingredient.of(ingot.get())).
				setBetterThan(props.betterThan).
				setWorseThan(props.worseThan).
				setTag(props.toolTag).
				setShieldBaseRenderMaterial(name).
				setShieldNoPatternRenderMaterial(name).
				build(); 
		
		TierSortingRegistry.registerTier(toolMat, StringHelper.getLocFStr(this.name), 
				toolMat.getBetterThan() != null ? List.of(TierSortingRegistry.byName(TierSortingRegistry.getName(toolMat.getBetterThan()))) : List.of(),
				toolMat.getWorseThan() != null ? List.of(TierSortingRegistry.byName(TierSortingRegistry.getName(toolMat.getWorseThan()))): List.of());
		
		this.armorMat =	new ModPlayerArmorMaterial.Builder().
				setName(this.name).
				setDurabilityForSlot(props.playerArmorDurability).
				setDefenseForSlot(props.playerArmorDefense).
				setToughness(props.playerArmorToughness).
				setEnchantmentValue(props.playerArmorEnchantmentValue).
				setEquipSound(props.playerArmorEquipSound).
				setRepairIngredient(props.playerArmorRepairIngredient != null ? props.playerArmorRepairIngredient : () -> Ingredient.of(ingot.get())).
				setKnockbackResistance(0.0f).
				setTexturePathMain(props.playerArmorTexturePathMain != null ? props.playerArmorTexturePathMain : StringHelper.getStrLocFStr("textures/entity/" + ModDatabase.Items.Names.ARMOR + "/" + ModDatabase.Items.Names.PLAYER_ARMOR + "/") + name + "_"  + "1" + ".png").
				setTexturePathSecondary(props.playerArmorTexturePathSecondary != null ? props.playerArmorTexturePathSecondary : StringHelper.getStrLocFStr("textures/entity/" + ModDatabase.Items.Names.ARMOR + "/" + ModDatabase.Items.Names.PLAYER_ARMOR + "/") + name + "_"  + "2" + ".png").
				build();
		
		this.horseArmorMat = new ModHorseArmorMaterial.Builder().
				setDurability(props.horseArmorDurability).
				setDefense(props.horseArmorDefense).
				setTexturePath(props.horseArmorTexturePath != null ? props.horseArmorTexturePath : StringHelper.getLocFStr("textures/entity/armor/horse/" + this.name + ".png")).
				build();

		
		this.nugget = register(StringHelper.slashPlacer(this.name, Items.Names.NUGGET), () -> new ModBaseItem(baseProps.get()));
		this.dust = register(StringHelper.slashPlacer(this.name, Items.Names.DUST), () -> new ModBaseItem(baseProps.get()));
		
		this.pickaxe = register(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.PICKAXE), () -> new ModPickaxeBase(toolMat));
		this.axe = register(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.AXE), () -> new ModAxeBase(toolMat, toolMat.getAttackDamageBonus() + 5 - 1, -3.2f));
		this.shovel = register(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.SHOVEL), () -> new ModShovelBase(toolMat));
		this.hoe = register(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.HOE), () -> new ModHoeBase(toolMat));
		this.shears = register(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.SHEARS), () -> new ModShearsBase(toolMat));
		this.fishingRod = register(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.FISHING_ROD), () -> new ModFishingRodBase(toolMat));
	
		this.sword = register(StringHelper.slashPlacer(this.name, Items.Names.WEAPON, Items.Names.Weapon.SWORD), () -> new ModSwordBase(toolMat));
		this.shield = register(StringHelper.slashPlacer(this.name, Items.Names.WEAPON, Items.Names.Weapon.SHIELD), () -> new ModShieldBase(toolMat));
		this.bow = register(StringHelper.slashPlacer(this.name, Items.Names.WEAPON, Items.Names.Weapon.BOW), () -> new ModBowBase(toolMat));
		this.crossbow = register(StringHelper.slashPlacer(this.name, Items.Names.WEAPON, Items.Names.Weapon.CROSSBOW), () -> new ModCrossbowBase(toolMat));
	
		this.armorHorse = register(StringHelper.slashPlacer(this.name, Items.Names.ARMOR, Items.Names.Armor.ARMOR_HORSE), () -> new ModHorseArmorItemBase(horseArmorMat));
		this.playerArmorHead = register(StringHelper.slashPlacer(this.name, Items.Names.ARMOR, Items.Names.PLAYER_ARMOR, Items.Names.Armor.ARMOR_HEAD), () -> new ModArmorBase(armorMat, EquipmentSlot.HEAD));
		this.playerArmorChest = register(StringHelper.slashPlacer(this.name, Items.Names.ARMOR, Items.Names.PLAYER_ARMOR, Items.Names.Armor.ARMOR_CHEST), () -> new ModArmorBase(armorMat, EquipmentSlot.CHEST));
		this.playerArmorLegs = register(StringHelper.slashPlacer(this.name, Items.Names.ARMOR, Items.Names.PLAYER_ARMOR, Items.Names.Armor.ARMOR_LEGS), () -> new ModArmorBase(armorMat, EquipmentSlot.LEGS));
		this.playerArmorFeet = register(StringHelper.slashPlacer(this.name, Items.Names.ARMOR, Items.Names.PLAYER_ARMOR, Items.Names.Armor.ARMOR_FEET), () -> new ModArmorBase(armorMat, EquipmentSlot.FEET));
	
		this.storageBlock = registerBlock(StringHelper.slashPlacer(this.name, ModDatabase.Blocks.Names.STORAGE_BLOCK), () -> new ModBaseBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5.0f, 6.0f).sound(SoundType.METAL)));
	
		this.isRequiredOre = props.isRequiredOre;
		
		if (this.isRequiredOre)
		{
			this.raw = register(StringHelper.slashPlacer(this.name, Items.Names.RAW), () -> new ModBaseItem(baseProps.get()));

			this.oreBlock = registerBlock(StringHelper.slashPlacer(this.name, ModDatabase.Blocks.Names.ORE), () -> new ModOreBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0f, 3.0f)));
			
			this.rawStorageBlock = registerBlock(StringHelper.slashPlacer(this.name, ModDatabase.Blocks.Names.STORAGE_BLOCK, ModDatabase.Items.Names.RAW), () -> new ModBaseBlock(Block.Properties.of(Material.STONE /* create material Color for it*/).requiresCorrectToolForDrops().strength(5.0f, 6.0f)));
			
			this.deepSlateOre = registerBlock(StringHelper.slashPlacer(this.name, ModDatabase.Blocks.Names.DEEPSLATE), () -> new ModOreBlock(Block.Properties.copy(this.oreBlock.get()).color(MaterialColor.DEEPSLATE).strength(4.5f, 3.0f).sound(SoundType.DEEPSLATE)));
		}
		else
		{
			this.raw = null;
			this.oreBlock = null;
			this.rawStorageBlock = null;
			this.deepSlateOre = null;
		}
	}	
	
	private static <T extends Item> RegistryObject<T> register (String name, Supplier<? extends T> sup)
	{
		return ModRegistration.RegisterItems.ITEMS.register(name, sup);
	}
	
	private static <T extends Block> RegistryObject<T> registerBlock (String name, Supplier<? extends T> sup)
	{
		RegistryObject<T> obj = ModRegistration.RegisterBlocks.BLOCKS.register(name, sup);
		
		register (obj.getId().getPath(), () -> new ModBlockItemBase(obj.get(), baseProps.get()));
		
		return obj;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public ModAbstractToolMaterial getToolMat() 
	{
		return toolMat;
	}
	
	public ModAbstractPlayerArmorMaterial getArmorMat() 
	{
		return armorMat;
	}
	
	public ModHorseArmorMaterial getHorseArmorMat() 
	{
		return horseArmorMat;
	}
	
	public RegistryObject<Item> getIngot() 
	{
		return ingot;
	}
	
	public RegistryObject<Item> getRaw() 
	{
		return raw;
	}
	
	public RegistryObject<Item> getNugget() 
	{
		return nugget;
	}
	
	public RegistryObject<Item> getDust() 
	{
		return dust;
	}
	
	public RegistryObject<? extends Block> getStorageBlock() 
	{
		return storageBlock;
	}
	
	public RegistryObject<? extends Block> getOreBlock() 
	{
		return oreBlock;
	}
	
	public RegistryObject<? extends Block> getRawStorageBlock() 
	{
		return rawStorageBlock;
	}
	
	public RegistryObject<? extends Block> getDeepSlateOre() 
	{
		return deepSlateOre;
	}
	
	public RegistryObject<Item> getPickaxe() 
	{
		return pickaxe;
	}
	
	public RegistryObject<Item> getAxe() 
	{
		return axe;
	}
	
	public RegistryObject<Item> getShovel() 
	{
		return shovel;
	}
	
	public RegistryObject<Item> getHoe() 
	{
		return hoe;
	}
	
	public RegistryObject<Item> getShears() 
	{
		return shears;
	}
	
	public RegistryObject<Item> getFishingRod() 
	{
		return fishingRod;
	}
	
	public RegistryObject<Item> getSword() 
	{
		return sword;
	}
	
	public RegistryObject<Item> getShield() 
	{
		return shield;
	}
	
	public RegistryObject<Item> getBow() 
	{
		return bow;
	}
	
	public RegistryObject<Item> getCrossbow() 
	{
		return crossbow;
	}
	
	public RegistryObject<Item> getArmorHorse() 
	{
		return armorHorse;
	}
	
	public RegistryObject<Item> getPlayerArmorHead() 
	{
		return playerArmorHead;
	}
	
	public RegistryObject<Item> getPlayerArmorChest() 
	{
		return playerArmorChest;
	}
	
	public RegistryObject<Item> getPlayerArmorLegs() 
	{
		return playerArmorLegs;
	}
	
	public RegistryObject<Item> getPlayerArmorFeet() 
	{
		return playerArmorFeet;
	}
	
	public boolean requiredOre()
	{
		return this.isRequiredOre;
	}
	
	public static class ModMaterialProperties
	{
		private String name;

		private int toolDurability;
		private float toolSpeed;
		private float toolAttackDamageBonus;
		private float toolAttackSpeed;
		private int toolLevel;
		private TagKey<Block> toolTag;
		private Tier betterThan, worseThan;
		private int toolEnchantmentValue;
		private Supplier<Ingredient> toolRepairIngredient = null;

		private int[] playerArmorDurability;
		private int[] playerArmorDefense;
		private float playerArmorToughness;
		private int playerArmorEnchantmentValue;
		private SoundEvent playerArmorEquipSound;
		private Supplier<Ingredient> playerArmorRepairIngredient = null;
		private String playerArmorTexturePathMain = null;
		private String playerArmorTexturePathSecondary = null;

		private int horseArmorDurability;
		private int horseArmorDefense;
		private ResourceLocation horseArmorTexturePath = null;
		
		private boolean isRequiredOre = true;
		
		public ModMaterialProperties(String name) 
		{
			this.name = name;
		}
		
		public ModMaterialProperties setToolDurability(int toolDurability) 
		{
			this.toolDurability = toolDurability;
			return this;
		}
		
		public ModMaterialProperties setToolSpeed(float toolSpeed) 
		{
			this.toolSpeed = toolSpeed;
			return this;
		}

		public ModMaterialProperties setToolEnchantmentValue(int toolEnchantmentValue) 
		{
			this.toolEnchantmentValue = toolEnchantmentValue;
			return this;
		}
		
		public ModMaterialProperties setToolAttackDamageBonus(float toolAttackDamageBonus) 
		{
			this.toolAttackDamageBonus = toolAttackDamageBonus;
			return this;
		}
		
		public ModMaterialProperties setToolAttackSpeed(float toolAttackSpeed) 
		{
			this.toolAttackSpeed = toolAttackSpeed;
			return this;
		}
		
		public ModMaterialProperties setToolLevel(int toolLevel) 
		{
			this.toolLevel = toolLevel;
			return this;
		}
		
		public ModMaterialProperties setToolTag(TagKey<Block> toolTag) 
		{
			this.toolTag = toolTag;
			return this;
		}
		
		public ModMaterialProperties setBetterThan (Tier tier)
		{
			this.betterThan = tier;
			return this;
		}
		
		public ModMaterialProperties setWorseThan (Tier tier)
		{
			this.worseThan = tier;
			return this;
		}
		
		public ModMaterialProperties setToolRepairIngredient(Supplier<Ingredient> toolRepairIngredient) 
		{
			this.toolRepairIngredient = toolRepairIngredient;
			return this;
		}
		
		public ModMaterialProperties setPlayerArmorDurability(int[] playerArmorDurability) 
		{
			this.playerArmorDurability = playerArmorDurability;
			return this;
		}
		
		public ModMaterialProperties setPlayerArmorDurability(int playerArmorDurability) 
		{
			this.playerArmorDurability = new int[] {playerArmorDurability, playerArmorDurability, playerArmorDurability, playerArmorDurability};
			return this;
		}
		
		public ModMaterialProperties setPlayerArmorDefense(int[] playerArmorDefense) 
		{
			this.playerArmorDefense = playerArmorDefense;
			return this;
		}
		
		public ModMaterialProperties setPlayerArmorEnchantmentValue(int playerArmorEnchantmentValue) 
		{
			this.playerArmorEnchantmentValue = playerArmorEnchantmentValue;
			return this;
		}
		
		public ModMaterialProperties setPlayerArmorToughness(float playerArmorToughness) 
		{
			this.playerArmorToughness = playerArmorToughness;
			return this;
		}
		
		public ModMaterialProperties setPlayerArmorEquipSound(SoundEvent playerArmorEquipSound) 
		{
			this.playerArmorEquipSound = playerArmorEquipSound;
			return this;
		}
		
		public ModMaterialProperties setPlayerArmorRepairIngredient(Supplier<Ingredient> playerArmorRepairIngredient) 
		{
			this.playerArmorRepairIngredient = playerArmorRepairIngredient;
			return this;
		}
		
		/**
		 * @param playerArmorTexturePathMain the playerArmorTexturePathMain to set
		 */
		public ModMaterialProperties setPlayerArmorTexturePathMain(String playerArmorTexturePathMain) 
		{
			this.playerArmorTexturePathMain = playerArmorTexturePathMain;
			return this;
		}
		
		/**
		 * @param playerArmorTexturePathSecondary the playerArmorTexturePathSecondary to set
		 */
		public ModMaterialProperties setPlayerArmorTexturePathSecondary(String playerArmorTexturePathSecondary) 
		{
			this.playerArmorTexturePathSecondary = playerArmorTexturePathSecondary;
			return this;
		}
		
		public ModMaterialProperties setHorseArmorDefense(int horseArmorDefense) 
		{
			this.horseArmorDefense = horseArmorDefense;
			return this;
		}
		
		public ModMaterialProperties setHorseArmorDurability(int horseArmorDurability) 
		{
			this.horseArmorDurability = horseArmorDurability;
			return this;
		}
		
		public ModMaterialProperties setHorseArmorTexturePath(ResourceLocation horseArmorTexturePath) 
		{
			this.horseArmorTexturePath = horseArmorTexturePath;
			return this;
		}

		public ModMaterialProperties setDurability(int durability)
		{
			setHorseArmorDurability(durability);
			setPlayerArmorDurability(durability);
			setToolDurability(durability);
			return this;
		}
		
		public ModMaterialProperties setEnchantmentValue (int enchantmentValue)
		{
			setPlayerArmorEnchantmentValue(enchantmentValue);
			setToolEnchantmentValue(enchantmentValue);
			return this;
		}
		
		public ModMaterialProperties requiredOre(boolean requiredOre)
		{
			this.isRequiredOre = requiredOre;
			return this;
		}
		
		public ModMaterial create()
		{
			return new ModMaterial(this);
		}
		
		
	}

	
}

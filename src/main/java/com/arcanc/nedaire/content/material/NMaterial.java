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
import java.util.function.Supplier;

import com.arcanc.nedaire.content.block.NBlockOre;
import com.arcanc.nedaire.content.item.NBaseBlockItem;
import com.arcanc.nedaire.content.item.armor.NArmorBase;
import com.arcanc.nedaire.content.item.armor.NHorseArmorItemBase;
import com.arcanc.nedaire.content.item.tool.NAxeBase;
import com.arcanc.nedaire.content.item.tool.NFishingRodBase;
import com.arcanc.nedaire.content.item.tool.NHoeBase;
import com.arcanc.nedaire.content.item.tool.NPickaxeBase;
import com.arcanc.nedaire.content.item.tool.NShearsBase;
import com.arcanc.nedaire.content.item.tool.NShovelBase;
import com.arcanc.nedaire.content.item.weapon.NBowBase;
import com.arcanc.nedaire.content.item.weapon.NCrossbowBase;
import com.arcanc.nedaire.content.item.weapon.NShieldBase;
import com.arcanc.nedaire.content.item.weapon.NSwordBase;
import com.arcanc.nedaire.content.material.armor.horse.NHorseArmorMaterial;
import com.arcanc.nedaire.content.material.armor.player.NAbstractPlayerArmorMaterial;
import com.arcanc.nedaire.content.material.armor.player.NPlayerArmorMaterial;
import com.arcanc.nedaire.content.material.tool.NAbstractToolMaterial;
import com.arcanc.nedaire.content.material.tool.NToolMaterial;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterBlocks.BlockRegObject;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterItems.ItemRegObject;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.database.NDatabase.Items;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.TierSortingRegistry;

public class NMaterial 
{

	private final String name;
	private final NAbstractToolMaterial toolMat;
	private final NAbstractPlayerArmorMaterial armorMat;
	private final NHorseArmorMaterial horseArmorMat;
	
	private static final Supplier<Item.Properties> baseProps = Item.Properties::new;
	
	private final ItemRegObject<? extends Item> ingot, nugget, raw, dust,
	   pickaxe, axe, shovel, hoe, shears, fishingRod,
	   sword, shield, bow, crossbow,
	   armorHorse, playerArmorHead, playerArmorChest, playerArmorLegs, playerArmorFeet;
	
	private final boolean isRequiredOre;
	private final BlockRegObject <? extends Block, ? extends Item> storageBlock, rawStorageBlock, oreBlock, deepSlateOre;
	
	private NMaterial(NMaterialProperties props) 
	{
		this.name = props.name;
		
		this.ingot = NRegistration.RegisterItems.simple(StringHelper.slashPlacer(this.name, Items.Names.INGOT));
		
		this.toolMat = new NToolMaterial.Builder().
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
		
		this.armorMat =	new NPlayerArmorMaterial.Builder().
				setName(this.name).
				setDurabilityForSlot(props.playerArmorDurability).
				setDefenseForSlot(props.playerArmorDefense).
				setToughness(props.playerArmorToughness).
				setEnchantmentValue(props.playerArmorEnchantmentValue).
				setEquipSound(props.playerArmorEquipSound).
				setRepairIngredient(props.playerArmorRepairIngredient != null ? props.playerArmorRepairIngredient : () -> Ingredient.of(ingot.get())).
				setKnockbackResistance(0.0f).
				setTexturePathMain(props.playerArmorTexturePathMain != null ? props.playerArmorTexturePathMain : StringHelper.getStrLocFStr("textures/entity/" + NDatabase.Items.Names.ARMOR + "/" + NDatabase.Items.Names.PLAYER_ARMOR + "/") + name + "_"  + "1" + ".png").
				setTexturePathSecondary(props.playerArmorTexturePathSecondary != null ? props.playerArmorTexturePathSecondary : StringHelper.getStrLocFStr("textures/entity/" + NDatabase.Items.Names.ARMOR + "/" + NDatabase.Items.Names.PLAYER_ARMOR + "/") + name + "_"  + "2" + ".png").
				build();
		
		this.horseArmorMat = new NHorseArmorMaterial.Builder().
				setDurability(props.horseArmorDurability).
				setDefense(props.horseArmorDefense).
				setTexturePath(props.horseArmorTexturePath != null ? props.horseArmorTexturePath : StringHelper.getLocFStr("textures/entity/armor/horse/" + this.name + ".png")).
				build();

		
		this.nugget = NRegistration.RegisterItems.simple(StringHelper.slashPlacer(this.name, Items.Names.NUGGET));
		this.dust = NRegistration.RegisterItems.simple(StringHelper.slashPlacer(this.name, Items.Names.DUST));
		
		this.pickaxe = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.PICKAXE), (p) -> new NPickaxeBase(toolMat));
		this.axe = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.AXE), (p) -> new NAxeBase(toolMat, toolMat.getAttackDamageBonus() + 5 - 1, -3.2f));
		this.shovel = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.SHOVEL), (p) -> new NShovelBase(toolMat));
		this.hoe = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.HOE), (p) -> new NHoeBase(toolMat));
		this.shears = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.SHEARS), (p) -> new NShearsBase(toolMat));
		this.fishingRod = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.TOOL, Items.Names.Tools.FISHING_ROD), (p) -> new NFishingRodBase(toolMat));
	
		this.sword = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.WEAPON, Items.Names.Weapon.SWORD), (p) -> new NSwordBase(toolMat));
		this.shield = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.WEAPON, Items.Names.Weapon.SHIELD), (p) -> new NShieldBase(toolMat));
		this.bow = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.WEAPON, Items.Names.Weapon.BOW), (p) -> new NBowBase(toolMat));
		this.crossbow = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.WEAPON, Items.Names.Weapon.CROSSBOW), (p) -> new NCrossbowBase(toolMat));
	
		this.armorHorse = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.ARMOR, Items.Names.Armor.ARMOR_HORSE), (p) -> new NHorseArmorItemBase(horseArmorMat));
		this.playerArmorHead = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.ARMOR, Items.Names.PLAYER_ARMOR, Items.Names.Armor.ARMOR_HEAD), (p) -> new NArmorBase(armorMat, ArmorItem.Type.HELMET));
		this.playerArmorChest = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.ARMOR, Items.Names.PLAYER_ARMOR, Items.Names.Armor.ARMOR_CHEST), (p) -> new NArmorBase(armorMat, ArmorItem.Type.CHESTPLATE));
		this.playerArmorLegs = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.ARMOR, Items.Names.PLAYER_ARMOR, Items.Names.Armor.ARMOR_LEGS), (p) -> new NArmorBase(armorMat, ArmorItem.Type.LEGGINGS));
		this.playerArmorFeet = new ItemRegObject<>(StringHelper.slashPlacer(this.name, Items.Names.ARMOR, Items.Names.PLAYER_ARMOR, Items.Names.Armor.ARMOR_FEET), (p) -> new NArmorBase(armorMat, ArmorItem.Type.BOOTS));
	
		this.storageBlock = NRegistration.RegisterBlocks.BlockRegObject.simple(StringHelper.slashPlacer(this.name, NDatabase.Blocks.Names.STORAGE_BLOCK), () ->
				Block.Properties.of().
						mapColor(MapColor.METAL).
						instrument(NoteBlockInstrument.IRON_XYLOPHONE).
						requiresCorrectToolForDrops().
						strength(5.0f, 6.0f).
						sound(SoundType.METAL));
	
		this.isRequiredOre = props.isRequiredOre;
		
		if (this.isRequiredOre)
		{
			this.raw = NRegistration.RegisterItems.simple(StringHelper.slashPlacer(this.name, Items.Names.RAW));

			this.oreBlock = new BlockRegObject<>(StringHelper.slashPlacer(this.name, NDatabase.Blocks.Names.ORE), 
					() -> Block.Properties.of().
							mapColor(MapColor.STONE).
							instrument(NoteBlockInstrument.BASEDRUM).
							requiresCorrectToolForDrops().
							strength(3.0f, 3.0f).
							sound(SoundType.STONE),
					NBlockOre :: new, 
					baseProps, 
					NBaseBlockItem :: new);
			
			this.rawStorageBlock = NRegistration.RegisterBlocks.BlockRegObject.simple(StringHelper.slashPlacer(this.name, NDatabase.Blocks.Names.STORAGE_BLOCK, 
					NDatabase.Items.Names.RAW), 
					() -> Block.Properties.of().
							mapColor(MapColor.QUARTZ) /*FIXME: add custom color for it*/.
							instrument(NoteBlockInstrument.BASEDRUM).
							requiresCorrectToolForDrops().
							strength(5.0f, 6.0f));
			
			this.deepSlateOre = new BlockRegObject<>(StringHelper.slashPlacer(this.name, NDatabase.Blocks.Names.DEEPSLATE), 
					() -> Block.Properties.copy(this.oreBlock.get()).
							mapColor(MapColor.DEEPSLATE).
							strength(4.5f, 3.0f).
							sound(SoundType.DEEPSLATE),
					NBlockOre :: new,
					baseProps,
					NBaseBlockItem :: new);
		}
		else
		{
			this.raw = null;
			this.oreBlock = null;
			this.rawStorageBlock = null;
			this.deepSlateOre = null;
		}
	}	
	
	public String getName() 
	{
		return name;
	}
	
	public NAbstractToolMaterial getToolMat() 
	{
		return toolMat;
	}
	
	public NAbstractPlayerArmorMaterial getArmorMat() 
	{
		return armorMat;
	}
	
	public NHorseArmorMaterial getHorseArmorMat() 
	{
		return horseArmorMat;
	}
	
	public ItemRegObject<? extends Item> getIngot() 
	{
		return ingot;
	}
	
	public ItemRegObject<? extends Item> getRaw() 
	{
		return raw;
	}
	
	public ItemRegObject<? extends Item> getNugget() 
	{
		return nugget;
	}
	
	public ItemRegObject<? extends Item> getDust() 
	{
		return dust;
	}
	
	public BlockRegObject<? extends Block, ? extends Item> getStorageBlock() 
	{
		return storageBlock;
	}
	
	public BlockRegObject<? extends Block, ? extends Item> getOreBlock() 
	{
		return oreBlock;
	}
	
	public BlockRegObject<? extends Block, ? extends Item> getRawStorageBlock() 
	{
		return rawStorageBlock;
	}
	
	public BlockRegObject<? extends Block, ? extends Item> getDeepSlateOre() 
	{
		return deepSlateOre;
	}
	
	public ItemRegObject<? extends Item> getPickaxe() 
	{
		return pickaxe;
	}
	
	public ItemRegObject<? extends Item> getAxe() 
	{
		return axe;
	}
	
	public ItemRegObject<? extends Item> getShovel() 
	{
		return shovel;
	}
	
	public ItemRegObject<? extends Item> getHoe() 
	{
		return hoe;
	}
	
	public ItemRegObject<? extends Item> getShears() 
	{
		return shears;
	}
	
	public ItemRegObject<? extends Item> getFishingRod() 
	{
		return fishingRod;
	}
	
	public ItemRegObject<? extends Item> getSword() 
	{
		return sword;
	}
	
	public ItemRegObject<? extends Item> getShield() 
	{
		return shield;
	}
	
	public ItemRegObject<? extends Item> getBow() 
	{
		return bow;
	}
	
	public ItemRegObject<? extends Item> getCrossbow() 
	{
		return crossbow;
	}
	
	public ItemRegObject<? extends Item> getArmorHorse() 
	{
		return armorHorse;
	}
	
	public ItemRegObject<? extends Item> getPlayerArmorHead() 
	{
		return playerArmorHead;
	}
	
	public ItemRegObject<? extends Item> getPlayerArmorChest() 
	{
		return playerArmorChest;
	}
	
	public ItemRegObject<? extends Item> getPlayerArmorLegs() 
	{
		return playerArmorLegs;
	}
	
	public ItemRegObject<? extends Item> getPlayerArmorFeet() 
	{
		return playerArmorFeet;
	}
	
	public boolean requiredOre()
	{
		return this.isRequiredOre;
	}
	
	public static class NMaterialProperties
	{
		private final String name;

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
		
		public NMaterialProperties(String name) 
		{
			this.name = name;
		}
		
		public NMaterialProperties setToolDurability(int toolDurability) 
		{
			this.toolDurability = toolDurability;
			return this;
		}
		
		public NMaterialProperties setToolSpeed(float toolSpeed) 
		{
			this.toolSpeed = toolSpeed;
			return this;
		}

		public NMaterialProperties setToolEnchantmentValue(int toolEnchantmentValue) 
		{
			this.toolEnchantmentValue = toolEnchantmentValue;
			return this;
		}
		
		public NMaterialProperties setToolAttackDamageBonus(float toolAttackDamageBonus) 
		{
			this.toolAttackDamageBonus = toolAttackDamageBonus;
			return this;
		}
		
		public NMaterialProperties setToolAttackSpeed(float toolAttackSpeed) 
		{
			this.toolAttackSpeed = toolAttackSpeed;
			return this;
		}
		
		public NMaterialProperties setToolLevel(int toolLevel) 
		{
			this.toolLevel = toolLevel;
			return this;
		}
		
		public NMaterialProperties setToolTag(TagKey<Block> toolTag) 
		{
			this.toolTag = toolTag;
			return this;
		}
		
		public NMaterialProperties setBetterThan (Tier tier)
		{
			this.betterThan = tier;
			return this;
		}
		
		public NMaterialProperties setWorseThan (Tier tier)
		{
			this.worseThan = tier;
			return this;
		}
		
		public NMaterialProperties setToolRepairIngredient(Supplier<Ingredient> toolRepairIngredient) 
		{
			this.toolRepairIngredient = toolRepairIngredient;
			return this;
		}
		
		public NMaterialProperties setPlayerArmorDurability(int[] playerArmorDurability) 
		{
			this.playerArmorDurability = playerArmorDurability;
			return this;
		}
		
		public NMaterialProperties setPlayerArmorDurability(int playerArmorDurability) 
		{
			this.playerArmorDurability = new int[] {playerArmorDurability, playerArmorDurability, playerArmorDurability, playerArmorDurability};
			return this;
		}
		
		public NMaterialProperties setPlayerArmorDefense(int[] playerArmorDefense) 
		{
			this.playerArmorDefense = playerArmorDefense;
			return this;
		}
		
		public NMaterialProperties setPlayerArmorEnchantmentValue(int playerArmorEnchantmentValue) 
		{
			this.playerArmorEnchantmentValue = playerArmorEnchantmentValue;
			return this;
		}
		
		public NMaterialProperties setPlayerArmorToughness(float playerArmorToughness) 
		{
			this.playerArmorToughness = playerArmorToughness;
			return this;
		}
		
		public NMaterialProperties setPlayerArmorEquipSound(SoundEvent playerArmorEquipSound) 
		{
			this.playerArmorEquipSound = playerArmorEquipSound;
			return this;
		}
		
		public NMaterialProperties setPlayerArmorRepairIngredient(Supplier<Ingredient> playerArmorRepairIngredient) 
		{
			this.playerArmorRepairIngredient = playerArmorRepairIngredient;
			return this;
		}
		
		/**
		 * @param playerArmorTexturePathMain the playerArmorTexturePathMain to set
		 */
		public NMaterialProperties setPlayerArmorTexturePathMain(String playerArmorTexturePathMain) 
		{
			this.playerArmorTexturePathMain = playerArmorTexturePathMain;
			return this;
		}
		
		/**
		 * @param playerArmorTexturePathSecondary the playerArmorTexturePathSecondary to set
		 */
		public NMaterialProperties setPlayerArmorTexturePathSecondary(String playerArmorTexturePathSecondary) 
		{
			this.playerArmorTexturePathSecondary = playerArmorTexturePathSecondary;
			return this;
		}
		
		public NMaterialProperties setHorseArmorDefense(int horseArmorDefense) 
		{
			this.horseArmorDefense = horseArmorDefense;
			return this;
		}
		
		public NMaterialProperties setHorseArmorDurability(int horseArmorDurability) 
		{
			this.horseArmorDurability = horseArmorDurability;
			return this;
		}
		
		public NMaterialProperties setHorseArmorTexturePath(ResourceLocation horseArmorTexturePath) 
		{
			this.horseArmorTexturePath = horseArmorTexturePath;
			return this;
		}

		public NMaterialProperties setDurability(int durability)
		{
			setHorseArmorDurability(durability);
			setPlayerArmorDurability(durability);
			setToolDurability(durability);
			return this;
		}
		
		public NMaterialProperties setEnchantmentValue (int enchantmentValue)
		{
			setPlayerArmorEnchantmentValue(enchantmentValue);
			setToolEnchantmentValue(enchantmentValue);
			return this;
		}
		
		public NMaterialProperties requiredOre(boolean requiredOre)
		{
			this.isRequiredOre = requiredOre;
			return this;
		}
		
		public NMaterial create()
		{
			return new NMaterial(this);
		}
		
		
	}

	
}

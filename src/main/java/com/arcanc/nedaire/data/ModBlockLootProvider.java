/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data;

import java.util.Set;
import java.util.stream.Stream;

import com.arcanc.nedaire.content.material.ModMaterial;
import com.arcanc.nedaire.content.registration.ModRegistration;
import com.arcanc.nedaire.data.loot.ModLootProvider;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.google.common.collect.ImmutableSet;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModBlockLootProvider extends ModLootProvider
{
	private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.
			toolMatches(ItemPredicate.Builder.item().
					hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
	
	   private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(Blocks.DRAGON_EGG, 
			   														  Blocks.BEACON, 
			   														  Blocks.CONDUIT, 
			   														  Blocks.SKELETON_SKULL, 
			   														  Blocks.WITHER_SKELETON_SKULL, 
			   														  Blocks.PLAYER_HEAD, 
			   														  Blocks.ZOMBIE_HEAD, 
			   														  Blocks.CREEPER_HEAD,
			   														  Blocks.DRAGON_HEAD, 
			   														  Blocks.SHULKER_BOX, 
			   														  Blocks.BLACK_SHULKER_BOX,
			   														  Blocks.BLUE_SHULKER_BOX,
			   														  Blocks.BROWN_SHULKER_BOX,
			   														  Blocks.CYAN_SHULKER_BOX, 
			   														  Blocks.GRAY_SHULKER_BOX, 
			   														  Blocks.GREEN_SHULKER_BOX,
			   														  Blocks.LIGHT_BLUE_SHULKER_BOX, 
			   														  Blocks.LIGHT_GRAY_SHULKER_BOX, 
			   														  Blocks.LIME_SHULKER_BOX, 
			   														  Blocks.MAGENTA_SHULKER_BOX, 
			   														  Blocks.ORANGE_SHULKER_BOX, 
			   														  Blocks.PINK_SHULKER_BOX, 
			   														  Blocks.PURPLE_SHULKER_BOX, 
			   														  Blocks.RED_SHULKER_BOX, 
			   														  Blocks.WHITE_SHULKER_BOX, 
			   														  Blocks.YELLOW_SHULKER_BOX).
			   map(ItemLike::asItem).collect(ImmutableSet.toImmutableSet());

	
	public ModBlockLootProvider(DataGenerator dataGenerator) 
	{
		super(dataGenerator);
	}

	@Override
	protected void registerTables() 
	{
		ModMaterial mat = ModRegistration.RegisterMaterials.CORIUM;
		
		registerSelfDrop(mat.getStorageBlock().get());
		if (mat.requiredOre())
		{
			registerSelfDrop(mat.getRawStorageBlock().get());
			register(mat.getOreBlock().get(), createOreDrop(mat.getOreBlock().get(), mat.getRaw().get()));
			register(mat.getDeepSlateOre().get(), createOreDrop(mat.getDeepSlateOre().get(), mat.getRaw().get()));
		}
		
		registerSelfDrop(ModRegistration.RegisterBlocks.SKYSTONE.get());
		
		register(ModRegistration.RegisterBlocks.PEDESTAL.get(), createNameableBlockEntityTable(ModRegistration.RegisterBlocks.PEDESTAL.get()));
		register(ModRegistration.RegisterBlocks.HOLDER.get(), createNameableBlockEntityTable(ModRegistration.RegisterBlocks.HOLDER.get()));
	}
	
	private void registerSelfDrop(Block block)
	{
		register(block, singleItem(block));
	}

	private void register (Block block, LootPool.Builder pool)
	{
		register (block, LootTable.lootTable().withPool(pool));
	}
	
	private void register(Block block, LootTable.Builder table)
	{
		register (BlockHelper.getRegistryName(block), table);
	}	
	
	private void register(ResourceLocation name, LootTable.Builder table)
	{
		if(tables.put(toTableLoc(name), table.setParamSet(LootContextParamSets.BLOCK).build())!=null)
			throw new IllegalStateException("Duplicate loot table " + name);
	}	
	
	private LootPool.Builder singleItem(ItemLike in)
	{
		return createPoolBuilder()
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(in));
	}
	
	private LootPool.Builder createPoolBuilder()
	{
		return LootPool.lootPool().when(ExplosionCondition.survivesExplosion());
	}
	
	private LootTable.Builder createOreDrop (Block block, Item item)
	{
		return createSilkTouchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(item).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
	
	private LootTable.Builder createSilkTouchTable (Block block, LootPoolEntryContainer.Builder<?> entryContainer)
	{
		return createSelfDropTable(block, HAS_SILK_TOUCH, entryContainer);
	}
	
	private LootTable.Builder createSelfDropTable(Block block,LootItemCondition.Builder hasSilkTouch, LootPoolEntryContainer.Builder<?> entryContainer) 
	{
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(block).when(hasSilkTouch).otherwise(entryContainer)));
	}

	private ResourceLocation toTableLoc(ResourceLocation in)
	{
		return new ResourceLocation(in.getNamespace(), "blocks/"+in.getPath());
	}
	
	private static LootTable.Builder createNameableBlockEntityTable(Block p_124293_) 
	{
		return LootTable.lootTable().withPool(applyExplosionCondition(p_124293_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124293_).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)))));
	}
	
	private <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike p_124132_, FunctionUserBuilder<T> p_124133_) 
	{
		return (T)(!EXPLOSION_RESISTANT.contains(p_124132_.asItem()) ? p_124133_.apply(ApplyExplosionDecay.explosionDecay()) : p_124133_.unwrap());
    }
	
	private static <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike p_124135_, ConditionUserBuilder<T> p_124136_) 
	{
		return (T)(!EXPLOSION_RESISTANT.contains(p_124135_.asItem()) ? p_124136_.when(ExplosionCondition.survivesExplosion()) : p_124136_.unwrap());
	}
	
	@Override
	public String getName() 
	{
		return "Nedaire Block Loot Provider";
	}

}

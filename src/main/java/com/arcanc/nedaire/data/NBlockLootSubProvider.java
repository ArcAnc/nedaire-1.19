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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arcanc.nedaire.content.material.ModMaterial;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.google.common.collect.ImmutableSet;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

public class NBlockLootSubProvider extends BlockLootSubProvider
{
	private static final Set<Item> EXPLOSION_RESISTANT = 	Stream.of(Blocks.DRAGON_EGG, 
			   														  Blocks.BEACON, 
			   														  Blocks.CONDUIT, 
			   														  Blocks.SKELETON_SKULL, 
			   														  Blocks.WITHER_SKELETON_SKULL, 
			   														  Blocks.PLAYER_HEAD, 
			   														  Blocks.ZOMBIE_HEAD, 
			   														  Blocks.CREEPER_HEAD,
			   														  Blocks.DRAGON_HEAD,
			   														  Blocks.PIGLIN_HEAD,
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
	
	public NBlockLootSubProvider() 
	{
		super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() 
	{
		ModMaterial mat = NRegistration.RegisterMaterials.CORIUM;
		
		this.dropSelf(mat.getStorageBlock().get());
		if (mat.requiredOre())
		{
			this.dropSelf(mat.getRawStorageBlock().get());
			this.add(mat.getOreBlock().get(), createOreDrop(mat.getOreBlock().get(), mat.getRaw().get()));
			this.add(mat.getDeepSlateOre().get(), createOreDrop(mat.getDeepSlateOre().get(), mat.getRaw().get()));
		}
		
		this.dropSelf(NRegistration.RegisterBlocks.SKYSTONE.get());
		
		this.add(NRegistration.RegisterBlocks.TERRAMORFER.get(), createNameableBlockEntityTable(NRegistration.RegisterBlocks.TERRAMORFER.get()));
		this.add(NRegistration.RegisterBlocks.PEDESTAL.get(), createNameableBlockEntityTable(NRegistration.RegisterBlocks.PEDESTAL.get()));
		this.add(NRegistration.RegisterBlocks.HOLDER.get(), createNameableBlockEntityTable(NRegistration.RegisterBlocks.HOLDER.get()));
		this.add(NRegistration.RegisterBlocks.MANUAL_CRUSHER.get(), createNameableBlockEntityTable(NRegistration.RegisterBlocks.MANUAL_CRUSHER.get()));
		this.add(NRegistration.RegisterBlocks.VIM_STORAGE.get(), createNameableBlockEntityTable(NRegistration.RegisterBlocks.VIM_STORAGE.get()));
		this.add(NRegistration.RegisterBlocks.DELIVERY_STATION.get(), createNameableBlockEntityTable(NRegistration.RegisterBlocks.DELIVERY_STATION.get()));
		this.add(NRegistration.RegisterBlocks.HOOVER.get(), createNameableBlockEntityTable(NRegistration.RegisterBlocks.HOOVER.get()));
	
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks() 
	{
		return NRegistration.RegisterBlocks.BLOCKS.getEntries().stream().map(RegistryObject :: get).collect(Collectors.toSet());
	}
	
/*	private void registerSelfDrop(Block block)
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
	
	private static LootTable.Builder createNameableBlockEntityTable(Block block) 
	{
		return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)))));
	}
	
	private <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike item, FunctionUserBuilder<T> builder) 
	{
		return (T)(!EXPLOSION_RESISTANT.contains(item.asItem()) ? builder.apply(ApplyExplosionDecay.explosionDecay()) : builder.unwrap());
    }
	
	private static <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike item, ConditionUserBuilder<T> builder) 
	{
		return (T)(!EXPLOSION_RESISTANT.contains(item.asItem()) ? builder.when(ExplosionCondition.survivesExplosion()) : builder.unwrap());
	}
	
	@Override
	public String getName() 
	{
		return "Nedaire Block Loot Provider";
	}
*/
}

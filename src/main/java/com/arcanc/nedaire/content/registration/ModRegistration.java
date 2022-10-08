/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.registration;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.compress.utils.Lists;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.block.ModBaseBlock;
import com.arcanc.nedaire.content.block.ModBlockHolder;
import com.arcanc.nedaire.content.block.ModBlockPedestal;
import com.arcanc.nedaire.content.block.ModTileProviderBlock;
import com.arcanc.nedaire.content.block.entities.ModBEHolder;
import com.arcanc.nedaire.content.block.entities.ModBEPedestal;
import com.arcanc.nedaire.content.item.FakeIconItem;
import com.arcanc.nedaire.content.item.ModBaseBlockItem;
import com.arcanc.nedaire.content.item.ModBaseItem;
import com.arcanc.nedaire.content.item.gem.GemEffect;
import com.arcanc.nedaire.content.item.gem.GemEffectHealth;
import com.arcanc.nedaire.content.item.gem.GemEffectRegen;
import com.arcanc.nedaire.content.item.tool.ModBook;
import com.arcanc.nedaire.content.item.tool.ModHammer;
import com.arcanc.nedaire.content.material.ModMaterial;
import com.arcanc.nedaire.content.material.ModMaterial.ModMaterialProperties;
import com.arcanc.nedaire.data.crafting.recipe.ModShieldRecipes;
import com.arcanc.nedaire.util.database.ModDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.collect.ImmutableSet;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistration 
{
	public static class RegisterItems
	{
		public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModDatabase.MOD_ID);

		protected static final Supplier<Item.Properties> baseProps = () -> new Item.Properties().tab(Nedaire.getInstance().TAB);
		
		public static final ItemRegObject<Item> HAMMER = new ItemRegObject<>(
				StringHelper.slashPlacer(
						ModDatabase.Items.Names.HAMMER, 
						ModDatabase.Items.Names.TOOL),
				ModHammer :: new);
		
		public static final ItemRegObject<ModBook> BOOK = new ItemRegObject<>(
				StringHelper.slashPlacer(
						ModDatabase.GUI.Enchiridion.ENCHIRIDION, 
						ModDatabase.Items.Names.TOOL), 
				ModBook :: new);
		
		public static final ItemRegObject<ModBaseItem> CHALK = new ItemRegObject<>(
				StringHelper.slashPlacer(
						ModDatabase.Items.Names.CHALK, 
						ModDatabase.Items.Names.TOOL), 
				() -> baseProps.get().durability(220), 
				ModBaseItem :: new);
		
		public static void init()
		{
		}
		
		public static ItemRegObject<FakeIconItem> icon(String name)
		{
			return new ItemRegObject<>(name, Item.Properties :: new, prop -> new FakeIconItem());
		}
		
		public static ItemRegObject<ModBaseItem> simpleWithStackSize(String name, int maxSize)
		{
			return simple(name, props -> props.stacksTo(maxSize), item -> {});
		}
		
		public static ItemRegObject<ModBaseItem> simple(String name)
		{
			return simple(name, props -> {}, item -> {});
		}
		
		public static ItemRegObject<ModBaseItem> simple(String name, Consumer<Item.Properties> makeProps, Consumer<ModBaseItem> processItem)
		{
			return new ItemRegObject<>(name, () -> Util.make(baseProps.get(), makeProps), (props) -> Util.make(new ModBaseItem(props), processItem));
		}
		
		public static class ItemRegObject<T extends Item> implements Supplier<T>, ItemLike
		{
			public static final Collection<ItemRegObject<? extends Item>> LIST = new ArrayList<>();
			
			private final RegistryObject<T> regObject;
			private final Supplier<Item.Properties> props;
			
			public ItemRegObject(String name, Function<Item.Properties, T> make)
			{
				this(name, baseProps, make);
			}
			
			public ItemRegObject(String name, Supplier<Item.Properties> props, Function<Item.Properties, T> make) 
			{
				this.props = props;
				this.regObject = ITEMS.register(name, () -> make.apply(props.get()));
				
				LIST.add(this);
			}
			
			public Item.Properties getProperties() 
			{
				return props.get();
			}
			
			@Override
			@Nonnull
			public T get() 
			{
				return regObject.get();
			}
			
			@Override
			@Nonnull
			public Item asItem() 
			{
				return regObject.get();
			}
			
			public ResourceLocation getId()
			{
				return regObject.getId();
			}
		}

		public static Supplier<Properties> copyProps(Item itemBlock) 
		{
			Item.Properties p = new Item.Properties().
					food(itemBlock.getFoodProperties()).
					craftRemainder(itemBlock.getCraftingRemainingItem()).
					durability(itemBlock.getMaxDamage()).
					stacksTo(itemBlock.getMaxStackSize()).
					tab(itemBlock.getItemCategory()).
					rarity(itemBlock.getRarity(itemBlock.getDefaultInstance()));
			
			if (itemBlock.isFireResistant())
				p.fireResistant();
			if(!itemBlock.canBeDepleted())
				p.setNoRepair();
			return () -> p;
		}
	}
	
	public static class RegisterBlocks
	{
		public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModDatabase.MOD_ID);

		private static final Supplier<Block.Properties> baseProps = () -> Block.Properties.of(Material.STONE);
		private static final Supplier<Block.Properties> baseMachineProps = () -> Block.Properties.of(Material.METAL).
				requiresCorrectToolForDrops().
				strength(2.0f);
		
		public static final BlockRegObject<ModBaseBlock, ModBaseBlockItem> SKYSTONE = BlockRegObject.simple(
				ModDatabase.Blocks.Names.SKYSTONE, 
				() -> baseProps.get().requiresCorrectToolForDrops().strength(2.0f));

		
		public static final BlockRegObject<ModBlockPedestal, ModBaseBlockItem> PEDESTAL = new BlockRegObject<>(
				ModDatabase.Blocks.BlockEntities.Names.PEDESTAL, 
				baseMachineProps,
				ModBlockPedestal :: new, 
				ModRegistration.RegisterItems.baseProps,
				(b, t) -> new ModBaseBlockItem(b, t));		
		
		public static final BlockRegObject<ModBlockHolder, ModBaseBlockItem> HOLDER = new BlockRegObject<>(
				ModDatabase.Blocks.BlockEntities.Names.HOLDER,
				baseMachineProps,
				ModBlockHolder :: new,
				ModRegistration.RegisterItems.baseProps,
				(b, t) -> new ModBaseBlockItem(b, t));
		
		public static class BlockRegObject<T extends Block, I extends Item> implements Supplier<T>, ItemLike
		{
			public static final Collection<BlockRegObject<? extends Block, ? extends Item>> ALL_ENTRIES = Lists.newArrayList();
			
			private final RegistryObject<T> regObject;
			private final Supplier<Block.Properties> blockProps;
			private final RegistryObject<I> itemBlock;
			private final Supplier<Item.Properties> itemProps;
			
			public static BlockRegObject<ModBaseBlock, ModBaseBlockItem> simple (String name, Supplier<Block.Properties> props)
			{
				return simple(name, props, p -> {});
			}
			
			public static BlockRegObject<ModBaseBlock, ModBaseBlockItem> simple (String name, Supplier<Block.Properties> props, Consumer<ModBaseBlock> extra)
			{
				return new BlockRegObject<>(name, props, p -> Util.make(new ModBaseBlock(p), extra), ModRegistration.RegisterItems.baseProps, (b, t) -> new ModBaseBlockItem(b, t));
			}
			
			public BlockRegObject(String name, Supplier<Block.Properties> blockProps, Function<Block.Properties, T> makeBlock, Supplier<Item.Properties> itemProps, BiFunction<T, Item.Properties, I> makeItem)
			{
				this.blockProps = blockProps;
				this.regObject = BLOCKS.register(name, () -> makeBlock.apply(blockProps.get()));
				this.itemProps = itemProps;
				this.itemBlock = ModRegistration.RegisterItems.ITEMS.register(name, () -> makeItem.apply(regObject.get(), itemProps.get()));
				
				ALL_ENTRIES.add(this);
			}
			
			public BlockRegObject (T existing)
			{
				this.blockProps = () -> Block.Properties.copy(existing);
				this.regObject = RegistryObject.create(Registry.BLOCK.getKey(existing), ForgeRegistries.BLOCKS);
				this.itemBlock = RegistryObject.create(Registry.ITEM.getKey(existing.asItem()), ForgeRegistries.ITEMS);
				this.itemProps = ModRegistration.RegisterItems.copyProps(itemBlock.get());
			}

			@Override
			public Item asItem() 
			{
				return itemBlock.get();
			}

			@Override
			public T get() 
			{
				return regObject.get();
			}
			
			public Supplier<Block.Properties> getBlockProperties() 
			{
				return blockProps;
			}
			
			public Supplier<Item.Properties> getItemProperties() 
			{
				return itemProps;
			}
			
			public ResourceLocation getId()
			{
				return regObject.getId();
			}
			
			public BlockState getDefaultBlockState()
			{
				return get().defaultBlockState();
			}
		}
		
		private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, boolean isItemRequired)
		{
			RegistryObject<T> b = BLOCKS.register(name, block);
			
			if (isItemRequired)
			{
				RegisterItems.ITEMS.register(b.getId().getPath(), ()-> new ModBaseBlockItem(b.get(), RegisterItems.baseProps.get()));
			}
			
			return b;
		}
		
		private static <T extends Block, R extends BlockEntity> RegistryObject<ModTileProviderBlock<R>> registerBlockWithEntity(String name, Block.Properties props, BiFunction<BlockPos, BlockState, R> tile)
		{
			RegistryObject<ModTileProviderBlock<R>> b = BLOCKS.register(name, () -> new ModTileProviderBlock<R>(props, tile));
			
			RegisterItems.ITEMS.register(b.getId().getPath(), ()-> new ModBaseBlockItem(b.get(), RegisterItems.baseProps.get()));
			
			return b;
		}
		
		private static <T extends Block> RegistryObject<T> registerBlockWithEntity(String name, Supplier<T> block)
		{
			RegistryObject<T> b = BLOCKS.register(name, block);
			
			RegisterItems.ITEMS.register(b.getId().getPath(), ()-> new ModBaseBlockItem(b.get(), RegisterItems.baseProps.get()));
			
			return b;
		}

	}
	
	public static class RegisterMaterials
	{
		public static void init() {};
		
		public static final ModMaterial CORIUM = new ModMaterialProperties(ModDatabase.Materials.CORIUM).
				setDurability(512).
				setEnchantmentValue(20).
				setToolSpeed(10).
				setToolAttackDamageBonus(6).
				setToolAttackSpeed(-2.8f).
				setToolLevel(3).
				setToolTag(BlockTags.create(StringHelper.getLocFStr("needs_" + ModDatabase.Materials.CORIUM + "_tool"))).
				setBetterThan(Tiers.DIAMOND).
				setWorseThan(Tiers.NETHERITE).
				setPlayerArmorDefense(new int[] {1, 2, 2, 1}).
				setPlayerArmorToughness(0f).
				setPlayerArmorEquipSound(SoundEvents.ARMOR_EQUIP_GOLD).
				setHorseArmorDefense(5).
				requiredOre(true).	
				create();

	}
	
	public static class RegisterBlockEntities
	{
		public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModDatabase.MOD_ID);
	

		public static final RegistryObject<BlockEntityType<ModBEPedestal>> BE_PEDESTAL = BLOCK_ENTITIES.register(
				ModDatabase.Blocks.BlockEntities.Names.PEDESTAL,
				makeType(ModBEPedestal :: new, 
						RegisterBlocks.PEDESTAL));

		public static final RegistryObject<BlockEntityType<ModBEHolder>> BE_HOLDER = BLOCK_ENTITIES.register(
				ModDatabase.Blocks.BlockEntities.Names.HOLDER,
				makeType(ModBEHolder :: new, 
						RegisterBlocks.HOLDER));
		
		public static <T extends BlockEntity> Supplier<BlockEntityType<T>> makeType(BlockEntityType.BlockEntitySupplier<T> create, Supplier<? extends Block> valid)
		{
			return makeTypeMultipleBlocks(create, ImmutableSet.of(valid));
		}

		public static <T extends BlockEntity> Supplier<BlockEntityType<T>> makeTypeMultipleBlocks(
				BlockEntityType.BlockEntitySupplier<T> create, Collection<? extends Supplier<? extends Block>> valid)
		{
			return () -> new BlockEntityType<>(
					create, ImmutableSet.copyOf(valid.stream().map(Supplier::get).collect(Collectors.toList())), null);
		}
	}
	public static class RegisterRecipes
	{
		public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ModDatabase.MOD_ID);

		public static final RegistryObject<SimpleRecipeSerializer<ModShieldRecipes>> SHIELD_SERIALIZER = RECIPE_SERIALIZERS.register(ModDatabase.Recipes.VanillaTypes.SHIELD_DECORATION, () -> new SimpleRecipeSerializer<>(ModShieldRecipes :: new));

		public static class Types
		{
			/**
			 * TODO: add recipe types here
			 */
		}
	
	}

	public static class RegisterWorldGen
	{
		public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, ModDatabase.MOD_ID);
	}
	
	public static class RegisterGemEffects
	{
		public static final ResourceKey<Registry<GemEffect<?>>> EFFECT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(ModDatabase.Capabilities.Socket.TAG_LOCATION));

		public static final DeferredRegister<GemEffect<?>> EFFECTS = DeferredRegister.create(EFFECT_KEY, ModDatabase.MOD_ID);

		public static final Supplier<IForgeRegistry<GemEffect<?>>> EFFECTS_BUILTIN = EFFECTS.makeRegistry(() -> makeRegistry(EFFECT_KEY).disableSaving());
	
		public static final RegistryObject<GemEffectHealth<?>> HEALTH = EFFECTS.register(
				ModDatabase.Capabilities.Socket.Health.NAME, 
				() -> new GemEffectHealth<>(Color.RED));
		
		public static final RegistryObject<GemEffectRegen<?>> REGEN = EFFECTS.register(
				ModDatabase.Capabilities.Socket.Regeneration.NAME, 
				() -> new GemEffectRegen<>(Color.BLUE));
	}

	public static class InitEnchiridion
	{
		
	}
	
    public static <T> RegistryBuilder<T> makeRegistry(ResourceKey<? extends Registry<T>> key)
    {
        return new RegistryBuilder<T>().setName(key.location()).setMaxID(Integer.MAX_VALUE - 1);
    }
}

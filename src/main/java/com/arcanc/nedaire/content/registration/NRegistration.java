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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import com.arcanc.nedaire.content.block.NBaseBlock;
import com.arcanc.nedaire.content.block.NBlockDeliveryStation;
import com.arcanc.nedaire.content.block.NBlockFluidStorage;
import com.arcanc.nedaire.content.block.NBlockGeneratorSolar;
import com.arcanc.nedaire.content.block.NBlockHolder;
import com.arcanc.nedaire.content.block.NBlockHoover;
import com.arcanc.nedaire.content.block.NBlockManualCrusher;
import com.arcanc.nedaire.content.block.NBlockPedestal;
import com.arcanc.nedaire.content.block.NBlockTerramorfer;
import com.arcanc.nedaire.content.block.NBlockVimStorage;
import com.arcanc.nedaire.content.block.entities.NBEDeliveryStation;
import com.arcanc.nedaire.content.block.entities.NBEFluidStorage;
import com.arcanc.nedaire.content.block.entities.NBEGeneratorSolar;
import com.arcanc.nedaire.content.block.entities.NBEHolder;
import com.arcanc.nedaire.content.block.entities.NBEHoover;
import com.arcanc.nedaire.content.block.entities.NBEManualCrusher;
import com.arcanc.nedaire.content.block.entities.NBEPedestal;
import com.arcanc.nedaire.content.block.entities.NBETerramorfer;
import com.arcanc.nedaire.content.block.entities.NBEVimStorage;
import com.arcanc.nedaire.content.container.menu.NContainerMenu;
import com.arcanc.nedaire.content.container.menu.NDeliveryStationMenu;
import com.arcanc.nedaire.content.container.menu.NFluidStorageMenu;
import com.arcanc.nedaire.content.container.menu.NGeneratorSolarMenu;
import com.arcanc.nedaire.content.container.menu.NHooverMenu;
import com.arcanc.nedaire.content.container.menu.NVimStorageMenu;
import com.arcanc.nedaire.content.entities.DeliveryDroneEntity;
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
import com.arcanc.nedaire.data.crafting.recipe.NCrusherRecipe;
import com.arcanc.nedaire.data.crafting.serializers.NCrusherRecipeSerializer;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.database.NDatabase.Items;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.collect.ImmutableSet;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class NRegistration 
{
	public static class RegisterItems
	{
		public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NDatabase.MOD_ID);

		protected static final Supplier<Item.Properties> baseProps = () -> new Item.Properties();
		
		public static final ItemRegObject<ModBaseItem> NUGGET_SKYSTONE = simple(
				StringHelper.slashPlacer(
						NDatabase.Blocks.Names.SKYSTONE, 
						Items.Names.NUGGET));
		
		public static final ItemRegObject<ModHammer> HAMMER = new ItemRegObject<>(
				StringHelper.slashPlacer(
						NDatabase.Items.Names.HAMMER, 
						NDatabase.Items.Names.TOOL),
				ModHammer :: new);
		
		public static final ItemRegObject<ModBook> BOOK = new ItemRegObject<>(
				StringHelper.slashPlacer(
						NDatabase.GUI.Enchiridion.ENCHIRIDION, 
						NDatabase.Items.Names.TOOL), 
				ModBook :: new);
		
		public static final ItemRegObject<ModBaseItem> CHALK = new ItemRegObject<>(
				StringHelper.slashPlacer(
						NDatabase.Items.Names.CHALK, 
						NDatabase.Items.Names.TOOL), 
				() -> baseProps.get().durability(220), 
				ModBaseItem :: new);
		
		public static final ItemRegObject<Item> DRONE_SPAWN_EGG = new ItemRegObject<>(
				"drone_spawn_egg", 
				() -> baseProps.get(), 
				(p) -> new ForgeSpawnEggItem(RegisterEntities.DELIVERY_DRONE, 0x22b341, 0x19732e, p));
		
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

		@SuppressWarnings("deprecation")
		public static Supplier<Properties> copyProps(Item itemBlock) 
		{
			Item.Properties p = new Item.Properties().
					food(itemBlock.getFoodProperties()).
					craftRemainder(itemBlock.getCraftingRemainingItem()).
					durability(itemBlock.getMaxDamage()).
					stacksTo(itemBlock.getMaxStackSize()).
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
		public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NDatabase.MOD_ID);

		private static final Supplier<Block.Properties> baseProps = () -> Block.Properties.of(Material.STONE);
		private static final Supplier<Block.Properties> baseMachineProps = () -> Block.Properties.of(Material.METAL).
				requiresCorrectToolForDrops().
				strength(2.0f);
		
		public static final BlockRegObject<NBaseBlock, ModBaseBlockItem> SKYSTONE = BlockRegObject.simple(
				NDatabase.Blocks.Names.SKYSTONE, 
				() -> baseProps.get().requiresCorrectToolForDrops().strength(2.0f));
		
		public static final BlockRegObject<NBlockTerramorfer, ModBaseBlockItem> TERRAMORFER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.TERRAMORFER,
				baseMachineProps,
				NBlockTerramorfer :: new,
				NRegistration.RegisterItems.baseProps, 
				(b, p) -> new ModBaseBlockItem(b, p));
		
		public static final BlockRegObject<NBlockGeneratorSolar, ModBaseBlockItem> GENERATOR_SOLAR = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.Generators.SOLAR,
				baseMachineProps,
				NBlockGeneratorSolar :: new,
				NRegistration.RegisterItems.baseProps,
				(b, p) -> new ModBaseBlockItem(b, p));
		
		public static final BlockRegObject<NBlockPedestal, ModBaseBlockItem> PEDESTAL = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.PEDESTAL, 
				baseMachineProps,
				NBlockPedestal :: new, 
				NRegistration.RegisterItems.baseProps,
				(b, p) -> new ModBaseBlockItem(b, p));		
		
		public static final BlockRegObject<NBlockHolder, ModBaseBlockItem> HOLDER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.HOLDER,
				baseMachineProps,
				NBlockHolder :: new,
				NRegistration.RegisterItems.baseProps,
				(b, p) -> new ModBaseBlockItem(b, p));
		
		public static final BlockRegObject<NBlockManualCrusher, ModBaseBlockItem> MANUAL_CRUSHER = new BlockRegObject<> (
				NDatabase.Blocks.BlockEntities.Names.MANUAL_CRUSHER,
				baseMachineProps,
				NBlockManualCrusher :: new,
				NRegistration.RegisterItems.baseProps,
				(b, p) -> new ModBaseBlockItem(b, p));
		
		public static final BlockRegObject<NBlockFluidStorage, ModBaseBlockItem> FLUID_STORAGE = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.FLUID_STORAGE, 
				baseMachineProps, 
				NBlockFluidStorage :: new, 
				NRegistration.RegisterItems.baseProps, 
				(b, p) -> new ModBaseBlockItem(b, p));
		
		public static final BlockRegObject<NBlockVimStorage, ModBaseBlockItem> VIM_STORAGE = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE, 
				baseMachineProps, 
				NBlockVimStorage :: new, 
				NRegistration.RegisterItems.baseProps, 
				(b, p) -> new ModBaseBlockItem(b, p));
		
		public static final BlockRegObject<NBlockDeliveryStation, ModBaseBlockItem> DELIVERY_STATION = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION,
				baseMachineProps,
				NBlockDeliveryStation :: new,
				NRegistration.RegisterItems.baseProps,
				(b, p) -> new ModBaseBlockItem(b, p));
		
		public static final BlockRegObject<NBlockHoover, ModBaseBlockItem> HOOVER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.HOOVER,
				baseMachineProps,
				NBlockHoover :: new, 
				NRegistration.RegisterItems.baseProps,
				(b, p) -> new ModBaseBlockItem(b, p));
		
		public static class BlockRegObject<T extends Block, I extends Item> implements Supplier<T>, ItemLike
		{
			public static final Collection<BlockRegObject<? extends Block, ? extends Item>> ALL_ENTRIES = Lists.newArrayList();
			
			private final RegistryObject<T> regObject;
			private final Supplier<Block.Properties> blockProps;
			private final RegistryObject<I> itemBlock;
			private final Supplier<Item.Properties> itemProps;
			
			public static BlockRegObject<NBaseBlock, ModBaseBlockItem> simple (String name, Supplier<Block.Properties> props)
			{
				return simple(name, props, p -> {});
			}
			
			public static BlockRegObject<NBaseBlock, ModBaseBlockItem> simple (String name, Supplier<Block.Properties> props, Consumer<NBaseBlock> extra)
			{
				return new BlockRegObject<>(name, props, p -> Util.make(new NBaseBlock(p), extra), NRegistration.RegisterItems.baseProps, (b, t) -> new ModBaseBlockItem(b, t));
			}
			
			public BlockRegObject(String name, Supplier<Block.Properties> blockProps, Function<Block.Properties, T> makeBlock, Supplier<Item.Properties> itemProps, BiFunction<T, Item.Properties, I> makeItem)
			{
				this.blockProps = blockProps;
				this.regObject = BLOCKS.register(name, () -> makeBlock.apply(blockProps.get()));
				this.itemProps = itemProps;
				this.itemBlock = NRegistration.RegisterItems.ITEMS.register(name, () -> makeItem.apply(regObject.get(), itemProps.get()));
				
				ALL_ENTRIES.add(this);
			}
			
			@SuppressWarnings("deprecation")
			public BlockRegObject (T existing)
			{
				this.blockProps = () -> Block.Properties.copy(existing);
				this.regObject = RegistryObject.create(BuiltInRegistries.BLOCK.getKey(existing), ForgeRegistries.BLOCKS);
				this.itemBlock = RegistryObject.create(BuiltInRegistries.ITEM.getKey(existing.asItem()), ForgeRegistries.ITEMS);
				this.itemProps = NRegistration.RegisterItems.copyProps(itemBlock.get());
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
	}
	
	public static class RegisterMaterials
	{
		public static void init() {};
		
		public static final ModMaterial CORIUM = new ModMaterialProperties(NDatabase.Materials.CORIUM).
				setDurability(512).
				setEnchantmentValue(20).
				setToolSpeed(10).
				setToolAttackDamageBonus(6).
				setToolAttackSpeed(-2.8f).
				setToolLevel(3).
				setToolTag(BlockTags.create(StringHelper.getLocFStr("needs_" + NDatabase.Materials.CORIUM + "_tool"))).
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
		public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NDatabase.MOD_ID);
	
		public static final RegistryObject<BlockEntityType<NBETerramorfer>> BE_TERRAMORFER = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.TERRAMORFER, 
				makeType(NBETerramorfer :: new,
						RegisterBlocks.TERRAMORFER));
		
		public static final RegistryObject<BlockEntityType<NBEGeneratorSolar>> BE_GENERATOR_SOLAR = BLOCK_ENTITIES.register(NDatabase.Blocks.BlockEntities.Names.Generators.SOLAR, 
				makeType(NBEGeneratorSolar :: new, 
						RegisterBlocks.GENERATOR_SOLAR));
		
		public static final RegistryObject<BlockEntityType<NBEPedestal>> BE_PEDESTAL = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.PEDESTAL,
				makeType(NBEPedestal :: new, 
						RegisterBlocks.PEDESTAL));

		public static final RegistryObject<BlockEntityType<NBEHolder>> BE_HOLDER = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.HOLDER,
				makeType(NBEHolder :: new, 
						RegisterBlocks.HOLDER));
		
		public static final RegistryObject<BlockEntityType<NBEManualCrusher>> BE_MANUAL_CRUSHER = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.MANUAL_CRUSHER, 
				makeType(NBEManualCrusher :: new,
						RegisterBlocks.MANUAL_CRUSHER));

		public static final RegistryObject<BlockEntityType<NBEFluidStorage>> BE_FLUID_STORAGE = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.FLUID_STORAGE, 
				makeType(NBEFluidStorage :: new, 
						RegisterBlocks.FLUID_STORAGE));
		
		public static final RegistryObject<BlockEntityType<NBEVimStorage>> BE_VIM_STORAGE = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE, 
				makeType(NBEVimStorage :: new, 
						RegisterBlocks.VIM_STORAGE));
		
		public static final RegistryObject<BlockEntityType<NBEDeliveryStation>> BE_DELIVERY_STATION = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION,
				makeType(NBEDeliveryStation :: new,
						RegisterBlocks.DELIVERY_STATION));
		
		public static final RegistryObject<BlockEntityType<NBEHoover>> BE_HOOVER = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.HOOVER,
				makeType(NBEHoover :: new,
						RegisterBlocks.HOOVER));
		
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
		public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, NDatabase.MOD_ID);

		public static final RegistryObject<SimpleCraftingRecipeSerializer<ModShieldRecipes>> SHIELD_SERIALIZER = RECIPE_SERIALIZERS.register(
				NDatabase.Recipes.VanillaTypes.SHIELD_DECORATION, 
				() -> new SimpleCraftingRecipeSerializer<>(ModShieldRecipes :: new));

		public static final RegistryObject<NCrusherRecipeSerializer> CRUSHER_SERIALIZER = RECIPE_SERIALIZERS.register(
				NDatabase.Recipes.Types.CRUSHER, 
				NCrusherRecipeSerializer :: new);
		
		public static class Types
		{
			private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, NDatabase.MOD_ID);
			
			public static final TypeWithClass<NCrusherRecipe> MANUAL_CRUSHER = register(NDatabase.Recipes.Types.CRUSHER, NCrusherRecipe.class);
		
			private static <T extends Recipe<?>> TypeWithClass<T> register(String name, Class<T> type)
			{
				RegistryObject<RecipeType<T>> regObj = RECIPE_TYPES.register(name, () -> new RecipeType<T>()
				{
				});
				return new TypeWithClass<>(regObj, type);
			}

			public static void init(IEventBus bus)
			{
				RECIPE_TYPES.register(bus);
			}

			public record TypeWithClass<T extends Recipe<?>>(RegistryObject<RecipeType<T>> type, Class<T> recipeClass) implements Supplier<RecipeType<T>>
			{
				@Override
				public RecipeType<T> get()
				{
					return type.get();
				}
			}
		}
	
	}

	public static class RegisterEntities
	{
		public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NDatabase.MOD_ID);
	
		public static final RegistryObject<EntityType<DeliveryDroneEntity>> DELIVERY_DRONE = register(
				NDatabase.Entities.Names.DELIVERY_DRONE,
				() -> Builder.<DeliveryDroneEntity>of(DeliveryDroneEntity :: new, MobCategory.MISC).sized(0.3125f, 0.3125f).clientTrackingRange(16));
		
		private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<Builder<T>> builder)
		{
			return ENTITIES.register(name, () -> builder.get().build(StringHelper.getStrLocFStr(name)));
		}
	}

	public static class RegisterWorldGen
	{
		public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, NDatabase.MOD_ID);
	}
	
	public static class RegisterVillage
	{
		public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, NDatabase.MOD_ID);
		
		public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, NDatabase.MOD_ID);
	
		public static final RegistryObject<PoiType> UNDERGROUNDER_POI = POI_TYPES.register(NDatabase.Village.Villagers.Poi_Types.UNDERGROUNDER_POI, 
				() -> new PoiType(ImmutableSet.copyOf(NRegistration.RegisterBlocks.PEDESTAL.get().getStateDefinition().getPossibleStates()), 1, 1));
		
		public static final RegistryObject<VillagerProfession> UNDERGROUNDER = VILLAGER_PROFESSIONS.register(NDatabase.Village.Villagers.UNDERGROUNDER, 
				() -> new VillagerProfession(NDatabase.Village.Villagers.UNDERGROUNDER, x -> x.get() == UNDERGROUNDER_POI.get(), 
				x -> x.get() == UNDERGROUNDER_POI.get(), ImmutableSet.of(), ImmutableSet.of(), 
				SoundEvents.VILLAGER_WORK_CLERIC)); 
	
	
		public static void registerPois()
		{
			try
			{
				Method m = ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class);
				m.invoke(null, UNDERGROUNDER_POI.get());
			} 
			catch (InvocationTargetException | IllegalAccessException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static class RegisterGemEffects
	{
		public static final ResourceKey<Registry<GemEffect<?>>> EFFECT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(NDatabase.Capabilities.Socket.TAG_LOCATION));

		public static final DeferredRegister<GemEffect<?>> EFFECTS = DeferredRegister.create(EFFECT_KEY, NDatabase.MOD_ID);

		public static final Supplier<IForgeRegistry<GemEffect<?>>> EFFECTS_BUILTIN = EFFECTS.makeRegistry(() -> makeRegistry(EFFECT_KEY).disableSaving());
	
		public static final RegistryObject<GemEffectHealth<?>> HEALTH = EFFECTS.register(
				NDatabase.Capabilities.Socket.Health.NAME, 
				() -> new GemEffectHealth<>(Color.RED));
		
		public static final RegistryObject<GemEffectRegen<?>> REGEN = EFFECTS.register(
				NDatabase.Capabilities.Socket.Regeneration.NAME, 
				() -> new GemEffectRegen<>(Color.BLUE));
	}

	public static class InitEnchiridion
	{
		
	}
	
	public static class RegisterMenuTypes
	{
		public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, NDatabase.MOD_ID);
	
		public static final BEContainer<NBEHoover, NHooverMenu> HOOVER = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.HOOVER, NHooverMenu :: makeServer, NHooverMenu :: makeClient);
		
		public static final BEContainer<NBEGeneratorSolar, NGeneratorSolarMenu> GENERATOR_SOLAR = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.Generators.SOLAR, NGeneratorSolarMenu :: makeServer, NGeneratorSolarMenu :: makeClient);
		
		public static final BEContainer<NBEDeliveryStation, NDeliveryStationMenu> DELIVERY_STATION = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION, NDeliveryStationMenu :: makeServer, NDeliveryStationMenu :: makeClient);

		public static final BEContainer<NBEFluidStorage, NFluidStorageMenu> FLUID_STORAGE = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.FLUID_STORAGE, NFluidStorageMenu :: makeServer, NFluidStorageMenu :: makeClient);
		
		public static final BEContainer<NBEVimStorage, NVimStorageMenu> VIM_STORAGE = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE, NVimStorageMenu :: makeServer, NVimStorageMenu :: makeClient);
		
		public static <T extends BlockEntity, C extends NContainerMenu>	BEContainer<T, C> registerBENew
		(String name, BEContainerConstructor<T, C> container, ClientContainerConstructor<C> client)
		{
			RegistryObject<MenuType<C>> typeRef = registerType(name, client);
			return new BEContainer<>(typeRef, container);
		}

		public static <C extends NContainerMenu> ItemContainerType<C> registerItem
		(String name, ItemContainerConstructor<C> container, ClientContainerConstructor<C> client)
		{
			RegistryObject<MenuType<C>> typeRef = registerType(name, client);
			return new ItemContainerType<>(typeRef, container);
		}

		private static <C extends NContainerMenu> RegistryObject<MenuType<C>> registerType(String name, ClientContainerConstructor<C> client)
		{
			return MENU_TYPES.register(
					name, () -> 
					{
						Mutable<MenuType<C>> typeBox = new MutableObject<>();
						MenuType<C> type = IForgeMenuType.create((id, inv, data) -> client.construct(typeBox.getValue(), id, inv, data.readBlockPos()));
						typeBox.setValue(type);
						return type;
					}
			);
		}

		public static <C extends AbstractContainerMenu>	ItemContainerType<C> register(String name, ItemContainerConstructor<C> container)
		{
			RegistryObject<MenuType<C>> typeRef = MENU_TYPES.register(
					name, () -> 
					{
						Mutable<MenuType<C>> typeBox = new MutableObject<>();
						MenuType<C> type = IForgeMenuType.create((windowId, inv, data) -> 
						{
							Minecraft mc = RenderHelper.mc();
							Level world = mc.level;
							// Matches NBaseItem#openGui
							int slotOrdinal = data.readInt();
							EquipmentSlot slot = EquipmentSlot.values()[slotOrdinal];
							ItemStack stack = mc.player.getItemBySlot(slot);
							return container.construct(typeBox.getValue(), windowId, inv, slot, stack);
						});
						typeBox.setValue(type);
						return type;
					}
			);
			return new ItemContainerType<>(typeRef, container);
		}

		public static <M extends AbstractContainerMenu>	RegistryObject<MenuType<M>> registerSimple(String name, SimpleContainerConstructor<M> factory)
		{
			return MENU_TYPES.register(
					name, () -> {
						Mutable<MenuType<M>> typeBox = new MutableObject<>();
						MenuType<M> type = IForgeMenuType.create((id, inv, data) -> factory.construct(typeBox.getValue(), id, inv));
						typeBox.setValue(type);
						return type;
					}
			);
		}
		
		public static class BEContainer<T extends BlockEntity, C extends NContainerMenu>
		{
			private final RegistryObject<MenuType<C>> type;
			private final BEContainerConstructor<T, C> factory;

			private BEContainer(RegistryObject<MenuType<C>> type, BEContainerConstructor<T, C> factory)
			{
				this.type = type;
				this.factory = factory;
			}

			public C create(int windowId, Inventory playerInv, T tile)
			{
				return factory.construct(getType(), windowId, playerInv, tile);
			}

			public MenuType<C> getType()
			{
				return type.get();
			}
		}
		
		public record ItemContainerType<C extends AbstractContainerMenu>(RegistryObject<MenuType<C>> type, ItemContainerConstructor<C> factory)
		{
			public C create(int id, Inventory inv, EquipmentSlot slot, ItemStack stack)
			{
				return factory.construct(getType(), id, inv, slot, stack);
			}

			public MenuType<C> getType()
			{
				return type.get();
			}
		}
		
		public interface BEContainerConstructor<T extends BlockEntity, C extends NContainerMenu>
		{
			C construct(MenuType<C> type, int windowId, Inventory inventoryPlayer, T te);
		}

		public interface ClientContainerConstructor<C extends NContainerMenu>
		{
			C construct(MenuType<C> type, int windowId, Inventory inventoryPlayer, BlockPos pos);
		}

		public interface ItemContainerConstructor<C extends AbstractContainerMenu>
		{
			C construct(MenuType<C> type, int windowId, Inventory inventoryPlayer, EquipmentSlot slot, ItemStack stack);
		}

		public interface SimpleContainerConstructor<C extends AbstractContainerMenu>
		{
			C construct(MenuType<?> type, int windowId, Inventory inventoryPlayer);
		}

	}
	
	public static class RegisterParticleTypes
	{
		public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, NDatabase.MOD_ID);
		
		public static final RegistryObject<SimpleParticleType> ESSENCE = PARTICLE_TYPES.register("essence", () -> new SimpleParticleType(false));
	}
	
    public static <T> RegistryBuilder<T> makeRegistry(ResourceKey<? extends Registry<T>> key)
    {
        return new RegistryBuilder<T>().setName(key.location()).setMaxID(Integer.MAX_VALUE - 1);
    }
}

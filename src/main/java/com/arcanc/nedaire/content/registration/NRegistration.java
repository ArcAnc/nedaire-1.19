/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.registration;

import com.arcanc.nedaire.content.block.*;
import com.arcanc.nedaire.content.block.entities.*;
import com.arcanc.nedaire.content.block.entities.NBETerramorfer.CoreType;
import com.arcanc.nedaire.content.container.menu.*;
import com.arcanc.nedaire.content.entities.DeliveryDroneEntity;
import com.arcanc.nedaire.content.entities.ThrownCrystalPrison;
import com.arcanc.nedaire.content.fluid.NFluid;
import com.arcanc.nedaire.content.fluid.NFluidType;
import com.arcanc.nedaire.content.fluid.NFluidType.FogGetter;
import com.arcanc.nedaire.content.item.*;
import com.arcanc.nedaire.content.item.gem.GemEffect;
import com.arcanc.nedaire.content.item.gem.GemEffectHealth;
import com.arcanc.nedaire.content.item.gem.GemEffectRegen;
import com.arcanc.nedaire.content.item.tool.NBook;
import com.arcanc.nedaire.content.item.tool.NHammer;
import com.arcanc.nedaire.content.material.NMaterial;
import com.arcanc.nedaire.content.material.NMaterial.NMaterialProperties;
import com.arcanc.nedaire.content.world.level.levelgen.feature.core.CoreConfiguration;
import com.arcanc.nedaire.content.world.level.levelgen.feature.core.CoreFeature;
import com.arcanc.nedaire.data.crafting.recipe.NCrusherRecipe;
import com.arcanc.nedaire.data.crafting.recipe.NDiffuserRecipe;
import com.arcanc.nedaire.data.crafting.recipe.NShieldRecipes;
import com.arcanc.nedaire.data.crafting.serializers.NCrusherRecipeSerializer;
import com.arcanc.nedaire.data.crafting.serializers.NDiffuserRecipeSerializer;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.database.NDatabase.Items;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.*;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class NRegistration 
{
	public static class RegisterItems
	{
		public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NDatabase.MOD_ID);

		protected static final Supplier<Item.Properties> baseProps = () -> new Item.Properties();
		
		public static final ItemRegObject<NBaseItem> NUGGET_SKYSTONE = simple(
				StringHelper.slashPlacer(
						NDatabase.Blocks.Names.SKYSTONE, 
						Items.Names.NUGGET));
		
		public static final ItemRegObject<NHammer> HAMMER = new ItemRegObject<>(
				StringHelper.slashPlacer(
						NDatabase.Items.Names.HAMMER, 
						NDatabase.Items.Names.TOOL),
				NHammer :: new);
		
		public static final ItemRegObject<NBook> BOOK = new ItemRegObject<>(
				StringHelper.slashPlacer(
						NDatabase.GUI.Enchiridion.ENCHIRIDION, 
						NDatabase.Items.Names.TOOL), 
				NBook :: new);

		public static final ItemRegObject<CrystalPrisonItem> CRYSTAL_PRISON = new ItemRegObject<>(
				NDatabase.Items.Names.CRYSTAL_PRISON,
				CrystalPrisonItem :: new);
		
		public static final ItemRegObject<NBaseItem> CHALK = new ItemRegObject<>(
				StringHelper.slashPlacer(
						NDatabase.Items.Names.CHALK, 
						NDatabase.Items.Names.TOOL), 
				() -> baseProps.get().durability(220), 
				NBaseItem :: new);
		
		public static final ItemRegObject<Item> DRONE_SPAWN_EGG = new ItemRegObject<>(
				"drone_spawn_egg", 
				() -> baseProps.get(), 
				(p) -> new ForgeSpawnEggItem(RegisterEntities.DELIVERY_DRONE, 0x22b341, 0x19732e, p));
		
		public static final ItemRegObject<NJewelryToolsItem> JEWELRY_TOOLS = new ItemRegObject<>(
				StringHelper.slashPlacer(
						NDatabase.Items.Names.JEWELRY, 
						NDatabase.Items.Names.TOOL),
				() -> baseProps.get().stacksTo(1),
				NJewelryToolsItem :: new);
		
		public static void init()
		{
		}
		
		public static ItemRegObject<FakeIconItem> icon(String name)
		{
			return new ItemRegObject<>(name, Item.Properties :: new, prop -> new FakeIconItem());
		}
		
		public static ItemRegObject<NBaseItem> simpleWithStackSize(String name, int maxSize)
		{
			return simple(name, props -> props.stacksTo(maxSize), item -> {});
		}
		
		public static ItemRegObject<NBaseItem> simple(String name)
		{
			return simple(name, props -> {}, item -> {});
		}
		
		public static ItemRegObject<NBaseItem> simple(String name, Consumer<Item.Properties> makeProps, Consumer<NBaseItem> processItem)
		{
			return new ItemRegObject<>(name, () -> Util.make(baseProps.get(), makeProps), (props) -> Util.make(new NBaseItem(props), processItem));
		}
		
		public static class ItemRegObject<T extends Item> implements Supplier<T>, ItemLike
		{
//			public static final Collection<ItemRegObject<? extends Item>> LIST = new ArrayList<>();
			
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
				
//				LIST.add(this);
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

		private static final Supplier<Block.Properties> baseProps = () -> Block.Properties.of().
				mapColor(MapColor.STONE).
				instrument(NoteBlockInstrument.BASEDRUM).
				requiresCorrectToolForDrops().
				strength(1.5F, 6.0F).
				sound(SoundType.STONE);
		private static final Supplier<Block.Properties> baseMachineProps = () -> Block.Properties.of().
				mapColor(MapColor.METAL).
				instrument(NoteBlockInstrument.IRON_XYLOPHONE).
				requiresCorrectToolForDrops().
				strength(2.0f).
				sound(SoundType.METAL);
		
		public static final BlockRegObject<NBaseBlock, NBaseBlockItem> SKYSTONE = BlockRegObject.simple(
				NDatabase.Blocks.Names.SKYSTONE, 
				() -> baseProps.get().requiresCorrectToolForDrops().strength(2.0f));
		
		public static final BlockRegObject<NBlockStairs, NBaseBlockItem> SKYSTONE_STAIRS = new BlockRegObject<>(
				NDatabase.Blocks.Names.SKYSTONE.concat("_stairs"),
				() -> SKYSTONE.getBlockProperties().get().noOcclusion(),
				(p) -> new NBlockStairs(SKYSTONE::getDefaultBlockState, p),
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem :: new);
		
		public static final BlockRegObject<NBlockWall, NBaseBlockItem> SKYSTONE_WALL = new BlockRegObject<>(
				NDatabase.Blocks.Names.SKYSTONE.concat("_wall"),
				() -> SKYSTONE.getBlockProperties().get().noOcclusion(),
				NBlockWall :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem :: new);
		
		public static final BlockRegObject<NBlockSlab, NBaseBlockItem> SKYSTONE_SLAB = new BlockRegObject<>(
				NDatabase.Blocks.Names.SKYSTONE.concat("_slab"),
				() -> SKYSTONE.getBlockProperties().get().noOcclusion().strength(2.0F, 6.0F),
				NBlockSlab :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem :: new
				);
		
		public static final BlockRegObject<NBaseBlock, NBaseBlockItem> FRAMEWORK = BlockRegObject.simple(
				NDatabase.Blocks.Names.FRAMEWORK, 
				() -> baseMachineProps.get().noOcclusion());
		
		public static final BlockRegObject<NBlockTerramorfer, NBaseBlockItem> TERRAMORFER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.TERRAMORFER,
				baseMachineProps,
				NBlockTerramorfer :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockGeneratorSolar, NBaseBlockItem> GENERATOR_SOLAR = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.Generators.SOLAR,
				baseMachineProps,
				NBlockGeneratorSolar :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockGeneratorFood, NBaseBlockItem> GENERATOR_FOOD = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.Generators.FOOD,
				() -> baseMachineProps.get().noOcclusion().emissiveRendering((state, getter, pos) -> state.getValue(BlockHelper.BlockProperties.LIT)).
				lightLevel(state -> state.getValue(BlockHelper.BlockProperties.LIT) ? 15 : 0),
				NBlockGeneratorFood :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);

		public static final BlockRegObject<NBlockGeneratorMob, NBaseBlockItem> GENERATOR_MOB = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.Generators.MOB,
				() -> baseMachineProps.get().noOcclusion().emissiveRendering((state, getter, pos) -> state.getValue(BlockHelper.BlockProperties.LIT)).
				lightLevel(state -> state.getValue(BlockHelper.BlockProperties.LIT) ? 15 : 0),
				NBlockGeneratorMob :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);

		public static final BlockRegObject<NBlockGeneratorLightning, NBaseBlockItem> GENERATOR_LIGHTNING = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.Generators.LIGHTNING,
				() -> baseMachineProps.get().noOcclusion().emissiveRendering((state, getter, pos) -> state.getValue(BlockHelper.BlockProperties.LIT)).
						lightLevel(state -> state.getValue(BlockHelper.BlockProperties.LIT) ? 15 : 0),
				NBlockGeneratorLightning :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockPedestal, NBaseBlockItem> PEDESTAL = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.PEDESTAL, 
				baseMachineProps,
				NBlockPedestal :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockHolder, NBaseBlockItem> HOLDER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.HOLDER,
				baseMachineProps,
				NBlockHolder :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockManualCrusher, NBaseBlockItem> MANUAL_CRUSHER = new BlockRegObject<> (
				NDatabase.Blocks.BlockEntities.Names.MANUAL_CRUSHER,
				baseMachineProps,
				NBlockManualCrusher :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockFluidStorage, NBaseBlockItem> FLUID_STORAGE = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.FLUID_STORAGE, 
				baseMachineProps, 
				NBlockFluidStorage :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockVimStorage, NBaseBlockItem> VIM_STORAGE = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE, 
				baseMachineProps, 
				NBlockVimStorage :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockDeliveryStation, NBaseBlockItem> DELIVERY_STATION = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION,
				baseMachineProps,
				NBlockDeliveryStation :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockHoover, NBaseBlockItem> HOOVER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.HOOVER,
				baseMachineProps,
				NBlockHoover :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockMobCatcher, NBaseBlockItem> MOB_CATCHER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.MOB_CATCHER,
				baseMachineProps,
				NBlockMobCatcher :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);

		public static final BlockRegObject<NBlockFurnace, NBaseBlockItem> FURNACE = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.FURNACE,
				() -> baseMachineProps.get().noOcclusion().emissiveRendering((state, getter, pos) -> state.getValue(BlockHelper.BlockProperties.LIT)).
				lightLevel(state -> state.getValue(BlockHelper.BlockProperties.LIT) ? 15 : 0),
				NBlockFurnace :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockCrusher, NBaseBlockItem> CRUSHER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.CRUSHER,
				() -> baseMachineProps.get().noOcclusion().emissiveRendering((state, getter, pos) -> state.getValue(BlockHelper.BlockProperties.LIT)).
				lightLevel(state -> state.getValue(BlockHelper.BlockProperties.LIT) ? 15 : 0),
				NBlockCrusher :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockJewelryTable, NBaseBlockItem> JEWERLY_TABLE = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.JEWELRY_TABLE,
				() -> Block.Properties.of().
						mapColor(MapColor.WOOD).
						instrument(NoteBlockInstrument.BASS).
						noOcclusion().
						requiresCorrectToolForDrops().
						strength(1).
						sound(SoundType.WOOD).
						ignitedByLava(),
				NBlockJewelryTable :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);

		public static final BlockRegObject<NBlockCrystalGrowth, NBaseBlockItem> CRYSTAL_GROWTH = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.Crystal.GROWTH,
				() -> baseProps.get().noCollission().emissiveRendering((state, getter, pos) -> true).sound(SoundType.AMETHYST_CLUSTER).lightLevel(state -> 5).requiresCorrectToolForDrops().strength(2),
				NBlockCrystalGrowth :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockExtruder, NBaseBlockItem> EXTRUDER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.EXTRUDER,
				() -> baseMachineProps.get().noOcclusion().emissiveRendering((state, getter, pos) -> state.getValue(BlockHelper.BlockProperties.LIT)).
				lightLevel(state -> state.getValue(BlockHelper.BlockProperties.LIT) ? 15 : 0),
				NBlockExtruder :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockFluidFiller, NBaseBlockItem> FLUID_FILLER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.FLUID_FILLER,
				baseMachineProps,
				NBlockFluidFiller :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockDiffuser, NBaseBlockItem> DIFFUSER = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.DIFFUSER,
				baseMachineProps,
				NBlockDiffuser :: new, 
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem::new);
		
		public static final BlockRegObject<NBlockExpExtractor, NBaseBlockItem> EXP_EXTRACTOR = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.EXP_EXTRACTOR,
				() -> baseMachineProps.get().noOcclusion().emissiveRendering((state, getter, pos) -> state.getValue(BlockHelper.BlockProperties.LIT)).
				lightLevel(state -> state.getValue(BlockHelper.BlockProperties.LIT) ? 15 : 0),
				NBlockExpExtractor :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem :: new);
		
		public static final BlockRegObject<NBlockCore, NBaseBlockItem> CORE = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.CORE,
				() -> baseMachineProps.get().noLootTable().noParticlesOnBreak().noOcclusion().lightLevel(state -> 15).emissiveRendering((state, getter, pos) -> true),
				NBlockCore :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem :: new);

		public static final BlockRegObject<NBlockPlatform, NBaseBlockItem> PLATFORM = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.PLATFORM,
				() -> baseMachineProps.get().noOcclusion(),
				NBlockPlatform :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem :: new);

		public static final BlockRegObject<NBlockBore, NBaseBlockItem> BORE = new BlockRegObject<>(
				NDatabase.Blocks.BlockEntities.Names.BORE,
				() -> baseMachineProps.get().noOcclusion(),
				NBlockBore :: new,
				NRegistration.RegisterItems.baseProps,
				NBaseBlockItem :: new);
		
		public static class BlockRegObject<T extends Block, I extends Item> implements Supplier<T>, ItemLike
		{
//			public static final Collection<BlockRegObject<? extends Block, ? extends Item>> ALL_ENTRIES = Lists.newArrayList();
			
			private final RegistryObject<T> regObject;
			private final Supplier<Block.Properties> blockProps;
			private final RegistryObject<I> itemBlock;
			private final Supplier<Item.Properties> itemProps;
			
			public static BlockRegObject<NBaseBlock, NBaseBlockItem> simple (String name, Supplier<Block.Properties> props)
			{
				return simple(name, props, p -> {});
			}
			
			public static BlockRegObject<NBaseBlock, NBaseBlockItem> simple (String name, Supplier<Block.Properties> props, Consumer<NBaseBlock> extra)
			{
				return new BlockRegObject<>(name, props, p -> Util.make(new NBaseBlock(p), extra), NRegistration.RegisterItems.baseProps, (b, t) -> new NBaseBlockItem(b, t));
			}
			
			public BlockRegObject(String name, Supplier<Block.Properties> blockProps, Function<Block.Properties, T> makeBlock, Supplier<Item.Properties> itemProps, BiFunction<T, Item.Properties, I> makeItem)
			{
				this.blockProps = blockProps;
				this.regObject = BLOCKS.register(name, () -> makeBlock.apply(blockProps.get()));
				this.itemProps = itemProps;
				this.itemBlock = NRegistration.RegisterItems.ITEMS.register(name, () -> makeItem.apply(regObject.get(), itemProps.get()));
				
//				ALL_ENTRIES.add(this);
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
	
	public static class RegisterFluids
	{
		public static final FluidEntry EXPERIENCE = FluidEntry.make(NDatabase.Fluids.Names.EXPERIENCE, 
				() -> 
					{
						Minecraft mc = RenderHelper.mc();
						long levelTime = mc.level.getGameTime();
						float partialTicks = mc.getPartialTick();

						final int MIN_R = 0;
						final int MAX_R = 181;
						final int MIN_G = 204;
						final int MAX_G = 235;
						final int MIN_B = 0;
						final int MAX_B = 34;
						final int MAX_TIME = 40;

						final Vector3f max = new Vector3f(MAX_R/255f, MAX_G/255f, MAX_B/255f);
						final Vector3f min = new Vector3f(MIN_R/255f, MIN_G/255f, MIN_B/255f);

						float time = (levelTime + partialTicks) % MAX_TIME;

						if(time < 20)
						{
							return FogGetter.getIntFromColor(FogGetter.interpColor(max, min, time/20));
						}
						else
						{
							return FogGetter.getIntFromColor(FogGetter.interpColor(min, max, (time - 20)/20));
						}
					},
				(camera, partialTicks, level, renderDistance, darkenWorldAmount, fluidFogColor) -> 
					{
						final int MIN_R = 0;
						final int MAX_R = 181;
						final int MIN_G = 204;
						final int MAX_G = 235;
						final int MIN_B = 0;
						final int MAX_B = 34;
						final int MAX_TIME = 40;

						final Vector3f max = new Vector3f(MAX_R/255f, MAX_G/255f, MAX_B/255f);
						final Vector3f min = new Vector3f(MIN_R/255f, MIN_G/255f, MIN_B/255f);
						
						long levelTime = level.getGameTime();
						
						float time = (levelTime + partialTicks) % MAX_TIME;
						
						if(time < 20)
						{
							return FogGetter.interpColor(max, min, time/20);
						}
						else
						{
							return FogGetter.interpColor(min, max, (time - 20)/20);
						}
					},
				props -> 
					{
						props.slopeFindDistance(2).levelDecreasePerBlock(2).explosionResistance(100);
					},
				typeProps -> 
					{
						typeProps.
						lightLevel(10).
						density(250).
						viscosity(500).
						rarity(Rarity.UNCOMMON);
					}
				);
		
		public static class FluidTypes
		{
			public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, NDatabase.MOD_ID);
		    
		}
		
		public static class Fluids
		{
			public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, NDatabase.MOD_ID);
		}
		
		public record FluidEntry(RegistryObject<NFluid> still,
								 RegistryObject<NFluid> flowing,
								 RegistryObject<LiquidBlock> block,
								 RegistryObject<BucketItem> bucket,
								 RegistryObject<FluidType> type,
								 List<Property<?>> props)
		{
			private static FluidEntry make(String name, NFluidType.FogGetter fogColor, Consumer<ForgeFlowingFluid.Properties> props)
			{
				return make(name, () -> 0xffffffff, fogColor, props);
			}
			
			private static FluidEntry make(String name, Supplier<Integer> tintColor, NFluidType.FogGetter fogColor, Consumer<ForgeFlowingFluid.Properties> props)
			{
				return make(name, 0, NDatabase.Fluids.getStillLoc(name), NDatabase.Fluids.getFlowLoc(name), NDatabase.Fluids.getOverlayLoc(name), tintColor, fogColor, props);
			}
			
			private static FluidEntry make(String name, int burnTime, Supplier<Integer> tintColor, NFluidType.FogGetter fogColor, Consumer<ForgeFlowingFluid.Properties> props)
			{
				return make(name, burnTime, NDatabase.Fluids.getStillLoc(name), NDatabase.Fluids.getFlowLoc(name), NDatabase.Fluids.getOverlayLoc(name), tintColor, fogColor, props);
			}
			
			private static FluidEntry make(String name, ResourceLocation stillTex, ResourceLocation flowingTex, ResourceLocation overlayTex, Supplier<Integer> tintColor, NFluidType.FogGetter fogColor, Consumer<ForgeFlowingFluid.Properties> props)
			{
				return make(name, 0, stillTex, flowingTex, overlayTex, tintColor, fogColor, props);
			}

			private static FluidEntry make(
					String name, Supplier<Integer> tintColor, NFluidType.FogGetter fogColor, Consumer<ForgeFlowingFluid.Properties> props, Consumer<FluidType.Properties> buildAttributes
			)
			{
				return make(name, 0, NDatabase.Fluids.getStillLoc(name), NDatabase.Fluids.getFlowLoc(name), NDatabase.Fluids.getOverlayLoc(name), tintColor, fogColor, props, buildAttributes);
			}

			private static FluidEntry make(String name, int burnTime, ResourceLocation stillTex, ResourceLocation flowingTex, ResourceLocation overlayTex, Supplier<Integer> tintColor, NFluidType.FogGetter fogColor, Consumer<ForgeFlowingFluid.Properties> props)
			{
				return make(name, burnTime, stillTex, flowingTex, overlayTex, tintColor, fogColor, props, null);
			}

			private static FluidEntry make(
					String name, int burnTime,
					ResourceLocation stillTex, ResourceLocation flowingTex, ResourceLocation overlayTex, Supplier<Integer> tintColor, NFluidType.FogGetter fogColor, @Nullable Consumer<ForgeFlowingFluid.Properties> props,
					@Nullable Consumer<FluidType.Properties> buildAttributes
			)
			{
				return make(
						name, burnTime, stillTex, flowingTex, overlayTex, tintColor, fogColor, NFluid.NFluidSource :: new, NFluid.NFluidFlowing :: new, props, buildAttributes,
						ImmutableList.of()
				);
			}
			
			private static FluidEntry make(
					String name, int burnTime,
					ResourceLocation stillTex, ResourceLocation flowingTex, ResourceLocation overlayTex, Supplier<Integer> tintColor, NFluidType.FogGetter fogColor,
					Function<ForgeFlowingFluid.Properties, ? extends NFluid> makeStill, Function<ForgeFlowingFluid.Properties, ? extends NFluid> makeFlowing, @Nullable Consumer<ForgeFlowingFluid.Properties> props,
					@Nullable Consumer<FluidType.Properties> buildAttributes, List<Property<?>> properties)
			{
				FluidType.Properties builder = FluidType.Properties.create()
						.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
						.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);
				if(buildAttributes!=null)
					buildAttributes.accept(builder);
				RegistryObject<FluidType> type = FluidTypes.FLUID_TYPES.register(
						name, () -> makeTypeWithTextures(builder, stillTex, flowingTex, overlayTex, tintColor, fogColor)
				);
				Mutable<FluidEntry> thisMutable = new MutableObject<>();
				NFluid.FluidPropsGetter fluidProps = (fluidType, stillFluid, flowingFluid) -> new ForgeFlowingFluid.Properties(fluidType, stillFluid, flowingFluid);
				RegistryObject<NFluid> still = Fluids.FLUIDS.register(NDatabase.Fluids.getStillLoc(name).getPath(), () -> NFluid.makeFluid(makeStill, 
						fluidProps.get(
								thisMutable.getValue().type(), 
								thisMutable.getValue().still(), 
								thisMutable.getValue().flowing()).
						block(thisMutable.getValue().block()).
						bucket(thisMutable.getValue().bucket()), 
						props));
				RegistryObject<NFluid> flowing = Fluids.FLUIDS.register(NDatabase.Fluids.getFlowLoc(name).getPath(), () -> NFluid.makeFluid(makeFlowing, 
						fluidProps.get(
								thisMutable.getValue().type(), 
								thisMutable.getValue().still(), 
								thisMutable.getValue().flowing()).
						block(thisMutable.getValue().block()).
						bucket(thisMutable.getValue().bucket()), 
						props));
				RegistryObject<LiquidBlock> block = RegisterBlocks.BLOCKS.register(NDatabase.Fluids.getBlockLocation(name).getPath(),
						() -> new LiquidBlock(thisMutable.getValue().still(), BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
				RegistryObject<BucketItem> bucket = RegisterItems.ITEMS.register(NDatabase.Fluids.getBucketLocation(name).getPath(), () -> makeBucket(still, burnTime));
				FluidEntry entry = new FluidEntry(still, flowing, block, bucket, type, properties);
				thisMutable.setValue(entry);
				return entry;
			}
			
			private static FluidType makeTypeWithTextures(FluidType.Properties props, ResourceLocation stillTex, ResourceLocation flowingTex, ResourceLocation overlayTex, Supplier<Integer> tintColor, NFluidType.FogGetter fogColor)
			{
				return new NFluidType(stillTex, flowingTex, overlayTex, tintColor, fogColor, props);
			}
			
			private static BucketItem makeBucket (RegistryObject<NFluid> still, int burnTime)
			{
				return new BucketItem(still, new Item.Properties().stacksTo(1).craftRemainder(net.minecraft.world.item.Items.BUCKET))
						{
							@Override
							public int getBurnTime(ItemStack itemStack,	@org.jetbrains.annotations.Nullable RecipeType<?> recipeType) 
							{
								return burnTime;
							}
							
							@Override
							public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) 
							{
								return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
							}
						};
			}

		}
		
		public static void register(final IEventBus bus)
		{
			FluidTypes.FLUID_TYPES.register(bus);
			Fluids.FLUIDS.register(bus);
		}
	}
	
	public static class RegisterMaterials
	{
		public static void init() {};
		
		public static final NMaterial CORIUM = new NMaterialProperties(NDatabase.Materials.CORIUM).
				setDurability(1862).
				setEnchantmentValue(20).
				setToolSpeed(8).
				setToolAttackDamageBonus(3).
				setToolAttackSpeed(-2.4f).
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
				makeType((pos, state) -> new NBETerramorfer(pos, state, CoreType.STABLE),
						RegisterBlocks.TERRAMORFER));
		
		public static final RegistryObject<BlockEntityType<NBEGeneratorSolar>> BE_GENERATOR_SOLAR = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.Generators.SOLAR, 
				makeType(NBEGeneratorSolar :: new, 
						RegisterBlocks.GENERATOR_SOLAR));
		
		public static final RegistryObject<BlockEntityType<NBEGeneratorFood>> BE_GENERATOR_FOOD = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.Generators.FOOD, 
				makeType(NBEGeneratorFood :: new, 
						RegisterBlocks.GENERATOR_FOOD));
		
		public static final RegistryObject<BlockEntityType<NBEGeneratorMob>> BE_GENERATOR_MOB = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.Generators.MOB, 
				makeType(NBEGeneratorMob :: new, 
						RegisterBlocks.GENERATOR_MOB));

		public static final RegistryObject<BlockEntityType<NBEGeneratorLightning>> BE_GENERATOR_LIGHTNING = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.Generators.LIGHTNING,
				makeType(NBEGeneratorLightning :: new,
						RegisterBlocks.GENERATOR_LIGHTNING));

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
		
		public static final RegistryObject<BlockEntityType<NBEMobCatcher>> BE_MOB_CATHER = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.MOB_CATCHER,
				makeType(NBEMobCatcher :: new,
						RegisterBlocks.MOB_CATCHER));
		
		public static final RegistryObject<BlockEntityType<NBEFurnace>> BE_FURNACE = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.FURNACE,
				makeType(NBEFurnace :: new,
						RegisterBlocks.FURNACE));
		
		public static final RegistryObject<BlockEntityType<NBECrusher>> BE_CRUSHER = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.CRUSHER,
				makeType(NBECrusher :: new,
						RegisterBlocks.CRUSHER));

		public static final RegistryObject<BlockEntityType<NBEJewelryTable>> BE_JEWELRY_TABLE = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.JEWELRY_TABLE,
				makeType(NBEJewelryTable :: new,
						RegisterBlocks.JEWERLY_TABLE));

		public static final RegistryObject<BlockEntityType<NBECrystalGrowth>> BE_CRYSTAL_GROWTH = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.Crystal.GROWTH,
				makeType(NBECrystalGrowth :: new,
						RegisterBlocks.CRYSTAL_GROWTH));
		
		public static final RegistryObject<BlockEntityType<NBEExtruder>> BE_EXTRUDER = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.EXTRUDER,
				makeType(NBEExtruder :: new,
						RegisterBlocks.EXTRUDER));

		public static final RegistryObject<BlockEntityType<NBEFluidFiller>> BE_FLUID_FILLER = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.FLUID_FILLER,
				makeType(NBEFluidFiller :: new,
						RegisterBlocks.FLUID_FILLER));

		public static final RegistryObject<BlockEntityType<NBEDiffuser>> BE_DIFFUSER = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.DIFFUSER,
				makeType(NBEDiffuser :: new,
						RegisterBlocks.DIFFUSER));
		
		public static final RegistryObject<BlockEntityType<NBEExpExtractor>> BE_EXP_EXTRACTOR = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.EXP_EXTRACTOR,
				makeType(NBEExpExtractor :: new,
						RegisterBlocks.EXP_EXTRACTOR));
		
		public static final RegistryObject<BlockEntityType<NBETerramorfer>> BE_CORE = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.CORE,
				makeType((pos, state) -> new NBETerramorfer(pos, state, CoreType.UNSTABLE),
						RegisterBlocks.CORE));

		public static final RegistryObject<BlockEntityType<NBEPlatform>> BE_PLATFORM = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.PLATFORM,
				makeType(NBEPlatform::new,
						RegisterBlocks.PLATFORM));

		public static final RegistryObject<BlockEntityType<NBEBore>> BE_BORE = BLOCK_ENTITIES.register(
				NDatabase.Blocks.BlockEntities.Names.BORE,
				makeType(NBEBore::new,
						RegisterBlocks.BORE));
		
		
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

		public static final RegistryObject<SimpleCraftingRecipeSerializer<NShieldRecipes>> SHIELD_SERIALIZER = RECIPE_SERIALIZERS.register(
				NDatabase.Recipes.VanillaTypes.SHIELD_DECORATION, 
				() -> new SimpleCraftingRecipeSerializer<>(NShieldRecipes :: new));

		public static final RegistryObject<NCrusherRecipeSerializer> CRUSHER_SERIALIZER = RECIPE_SERIALIZERS.register(
				NDatabase.Recipes.Types.CRUSHER, 
				NCrusherRecipeSerializer :: new);
		
		public static final RegistryObject<NDiffuserRecipeSerializer> DIFFUSER_SERIALIZER = RECIPE_SERIALIZERS.register(
				NDatabase.Recipes.Types.DIFFUSER, 
				NDiffuserRecipeSerializer :: new);
		
		public static class Types
		{
			private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, NDatabase.MOD_ID);
			
			public static final TypeWithClass<NCrusherRecipe> MANUAL_CRUSHER = register(NDatabase.Recipes.Types.CRUSHER, NCrusherRecipe.class);
			public static final TypeWithClass<NDiffuserRecipe> DIFFUSER = register(NDatabase.Recipes.Types.DIFFUSER, NDiffuserRecipe.class);
		
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
		
		public static final RegistryObject<EntityType<ThrownCrystalPrison>> CRYSTAL_PRISON = register(
				NDatabase.Entities.Names.CRYSTAL_PRISON,
				() -> Builder.<ThrownCrystalPrison>of(ThrownCrystalPrison :: new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10));
		
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

		public static final BEContainer<NBEMobCatcher, NMobCatcherMenu> MOB_CATCHER = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.MOB_CATCHER, NMobCatcherMenu :: makeServer, NMobCatcherMenu :: makeClient);
		
		public static final BEContainer<NBEGeneratorSolar, NGeneratorSolarMenu> GENERATOR_SOLAR = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.Generators.SOLAR, NGeneratorSolarMenu :: makeServer, NGeneratorSolarMenu :: makeClient);

		public static final BEContainer<NBEGeneratorFood, NGeneratorFoodMenu> GENERATOR_FOOD = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.Generators.FOOD, NGeneratorFoodMenu :: makeServer, NGeneratorFoodMenu :: makeClient);

		public static final BEContainer<NBEGeneratorMob, NGeneratorMobMenu> GENERATOR_MOB = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.Generators.MOB, NGeneratorMobMenu :: makeServer, NGeneratorMobMenu :: makeClient);
		
		public static final BEContainer<NBEDeliveryStation, NDeliveryStationMenu> DELIVERY_STATION = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION, NDeliveryStationMenu :: makeServer, NDeliveryStationMenu :: makeClient);

		public static final BEContainer<NBEFluidStorage, NFluidStorageMenu> FLUID_STORAGE = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.FLUID_STORAGE, NFluidStorageMenu :: makeServer, NFluidStorageMenu :: makeClient);
		
		public static final BEContainer<NBEVimStorage, NVimStorageMenu> VIM_STORAGE = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE, NVimStorageMenu :: makeServer, NVimStorageMenu :: makeClient);
		
		public static final BEContainer<NBECrusher, NCrusherMenu> CRUSHER = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.CRUSHER, NCrusherMenu :: makeServer, NCrusherMenu :: makeClient);

		public static final BEContainer<NBEFurnace, NFurnaceMenu> FURNACE = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.FURNACE, NFurnaceMenu :: makeServer, NFurnaceMenu :: makeClient);

		public static final BEContainer<NBEExtruder, NExtruderMenu> EXTRUDER = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.EXTRUDER, NExtruderMenu :: makeServer, NExtruderMenu :: makeClient);

		public static final BEContainer<NBEFluidFiller, NFluidFillerMenu> FLUID_FILLER = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.FLUID_FILLER, NFluidFillerMenu :: makeServer, NFluidFillerMenu :: makeClient);
		
		public static final BEContainer<NBEExpExtractor, NExpExtractorMenu> EXP_EXTRACTOR = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.EXP_EXTRACTOR, NExpExtractorMenu :: makeServer, NExpExtractorMenu :: makeClient);

		public static final BEContainer<NBEBore, NBoreMenu> BORE = registerBENew(
				NDatabase.Blocks.BlockEntities.Names.BORE, NBoreMenu :: makeServer, NBoreMenu :: makeClient);

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
		
		public static final RegistryObject<SimpleParticleType> ESSENCE = PARTICLE_TYPES.register(NDatabase.Particles.ESSENCE, () -> new SimpleParticleType(false));
	}
	
	public static class RegisterFeatures
	{
		public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, NDatabase.MOD_ID);
	
		public static final RegistryObject<Feature<CoreConfiguration>> CORE = FEATURES.register(NDatabase.WorldGen.Features.CORE, () -> new CoreFeature(CoreConfiguration.CODEC));
	}

	public static class RegisterCreativeTabs
	{
		public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NDatabase.MOD_ID);

		public static final RegistryObject<CreativeModeTab> MAIN_TAB = register(
				NDatabase.ItemGroups.Main.MAIN,
				new ItemStack(Blocks.BEACON.asItem()),
				NRegistration.RegisterItems.ITEMS.getEntries().
					stream().
					filter(obj -> !(obj.get() instanceof FakeIconItem)).
					map(RegistryObject :: get).
					map(ItemStack :: new).
					sorted(Comparator.comparing(stack -> stack.getDisplayName().getString())).
					toList());
		private static RegistryObject<CreativeModeTab> register(String name, ItemStack icon, Collection<ItemStack> items)
		{
			return CREATIVE_MODE_TABS.register(name, () -> CreativeModeTab.builder().
					icon(() -> icon).
					title(Component.translatable(NDatabase.MOD_ID + ".itemgroup." + name)).
					hideTitle().
					withBackgroundLocation(StringHelper.getLocFStr(NDatabase.ItemGroups.BACKGROUND_IMAGE_PATH + name + ".png")).
					withSearchBar().
					displayItems((params, output) ->
					{
						output.acceptAll(items);
					}).
					build());
		}
	}

    public static <T> RegistryBuilder<T> makeRegistry(ResourceKey<? extends Registry<T>> key)
    {
        return new RegistryBuilder<T>().setName(key.location()).setMaxID(Integer.MAX_VALUE - 1);
    }
}

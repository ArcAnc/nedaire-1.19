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
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.block.ModBaseBlock;
import com.arcanc.nedaire.content.block.ModBlockHolder;
import com.arcanc.nedaire.content.block.ModBlockPedestal;
import com.arcanc.nedaire.content.block.ModTileProviderBlock;
import com.arcanc.nedaire.content.block.entities.ModBEHolder;
import com.arcanc.nedaire.content.block.entities.ModBEPedestal;
import com.arcanc.nedaire.content.item.ModBlockItemBase;
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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
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
		protected static final Supplier<Item.Properties> fakeIconProps = () -> new Item.Properties().stacksTo(1);
		
		public static final RegistryObject<Item> HAMMER = ITEMS.register(
				StringHelper.slashPlacer(
						ModDatabase.Items.Names.HAMMER, 
						ModDatabase.Items.Names.TOOL), 
				() -> new ModHammer(baseProps.get()));
		
		public static final RegistryObject<Item> BOOK = ITEMS.register(
				StringHelper.slashPlacer(
						ModDatabase.GUI.Enchiridion.ENCHIRIDION, 
						ModDatabase.Items.Names.TOOL), 
				() -> new ModBook(baseProps.get()));
	}
	
	public static class RegisterBlocks
	{
		public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModDatabase.MOD_ID);

		private static final Supplier<Block.Properties> baseProps = () -> Block.Properties.of(Material.STONE);
		private static final Supplier<Block.Properties> baseMachineProps = () -> Block.Properties.of(Material.METAL).
				requiresCorrectToolForDrops().
				strength(2.0f);
		
		public static final RegistryObject<Block> SKYSTONE = registerBlock(
				ModDatabase.Blocks.Names.SKYSTONE, 
				()-> new ModBaseBlock(baseProps.get().requiresCorrectToolForDrops().strength(2.0f)), true);

		
		public static final RegistryObject<ModBlockPedestal> PEDESTAL = registerBlockWithEntity(
				ModDatabase.Blocks.BlockEntities.Names.PEDESTAL, 
				() -> new ModBlockPedestal(
						baseMachineProps.get(),
						ModBEPedestal :: new));		
		
		public static final RegistryObject<ModBlockHolder> HOLDER = registerBlockWithEntity(
				ModDatabase.Blocks.BlockEntities.Names.HOLDER, 
				() -> new ModBlockHolder(
						baseMachineProps.get(),
						ModBEHolder :: new));
		
		private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, boolean isItemRequired)
		{
			RegistryObject<T> b = BLOCKS.register(name, block);
			
			if (isItemRequired)
			{
				RegisterItems.ITEMS.register(b.getId().getPath(), ()-> new ModBlockItemBase(b.get(), RegisterItems.baseProps.get()));
			}
			
			return b;
		}
		
		private static <T extends Block, R extends BlockEntity> RegistryObject<ModTileProviderBlock<R>> registerBlockWithEntity(String name, Block.Properties props, BiFunction<BlockPos, BlockState, R> tile)
		{
			RegistryObject<ModTileProviderBlock<R>> b = BLOCKS.register(name, () -> new ModTileProviderBlock<R>(props, tile));
			
			RegisterItems.ITEMS.register(b.getId().getPath(), ()-> new ModBlockItemBase(b.get(), RegisterItems.baseProps.get()));
			
			return b;
		}
		
		private static <T extends Block> RegistryObject<T> registerBlockWithEntity(String name, Supplier<T> block)
		{
			RegistryObject<T> b = BLOCKS.register(name, block);
			
			RegisterItems.ITEMS.register(b.getId().getPath(), ()-> new ModBlockItemBase(b.get(), RegisterItems.baseProps.get()));
			
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
	

		public static final RegistryObject<BlockEntityType<ModBEPedestal>> BE_PEDESTAL = register(
				ModDatabase.Blocks.BlockEntities.Names.PEDESTAL, 
				ModBEPedestal :: new, 
				RegisterBlocks.PEDESTAL);

		public static final RegistryObject<BlockEntityType<ModBEHolder>> BE_HOLDER = register(
				ModDatabase.Blocks.BlockEntities.Names.HOLDER, 
				ModBEHolder :: new, 
				RegisterBlocks.HOLDER);
		
		@SafeVarargs
		private static <T extends BlockEntity, R extends Block> RegistryObject<BlockEntityType<T>> register (String name, BlockEntityType.BlockEntitySupplier<T> blockEntity, RegistryObject<R>... blocks)
		{
			return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(blockEntity, Stream.of(blocks).map(RegistryObject :: get).toArray(Block[] :: new)).build(null));
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

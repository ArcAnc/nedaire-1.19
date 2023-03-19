/**
 * @author ArcAnc
 * Created at: 2022-04-01
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data;

import java.util.function.Consumer;

import com.arcanc.nedaire.content.material.ModMaterial;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.IngredientWithSize;
import com.arcanc.nedaire.data.crafting.builders.NCrusherRecipeBuilder;
import com.arcanc.nedaire.data.crafting.builders.NDiffuserRecipeBuilder;
import com.arcanc.nedaire.data.tags.NTags;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.Tags.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class NRecipeProvider extends RecipeProvider
{
	public NRecipeProvider(PackOutput output) 
	{
		super(output);
	}
	
	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> out) 
	{
		ModMaterial mat = NRegistration.RegisterMaterials.CORIUM;
		
		//=========================
		//Nuggets to Ingot and back
		//=========================
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getIngot().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getNugget())).
		pattern("AAA").
		pattern("AAA").
		pattern("AAA").
		unlockedBy("has_" + NDatabase.Items.Names.NUGGET +"_" + mat.getName(), has(NTags.Items.MATERIALS.get(mat.getName()).getNugget())).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getNugget().getId().getPath() + "_to_" + mat.getIngot().getId().getPath()));
	
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, mat.getNugget().get(), 9).
		requires(Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + NDatabase.Items.Names.INGOT + "_" + mat.getName(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getIngot().getId().getPath() + "_to_" + mat.getNugget().getId().getPath()));

		//=========================
		//Storage Block to Ingots and back
		//=========================
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, mat.getStorageBlock().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("AAA").
		pattern("AAA").
		pattern("AAA").
		unlockedBy("has_" + NDatabase.Items.Names.INGOT +"_" + mat.getName(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getIngot().getId().getPath() + "_to_" + mat.getStorageBlock().getId().getPath()));
	
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, mat.getIngot().get(), 9).
		requires(Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getStorageBlock())).
		unlockedBy("has_" + NDatabase.Blocks.Names.STORAGE_BLOCK +"_" + mat.getName(), has(NTags.Items.MATERIALS.get(mat.getName()).getStorageBlock())).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getStorageBlock().getId().getPath() + "_to_" + mat.getIngot().getId().getPath()));

		//==========================
		//Smelting and Blasting
		//==========================
	
		addBlasting(NTags.Items.MATERIALS.get(mat.getName()).getDust(), mat.getIngot().get(), 0.0f, out);
		addSmelting(NTags.Items.MATERIALS.get(mat.getName()).getDust(), mat.getIngot().get(), 0.0f, out);
		
		if (mat.requiredOre())
		{
			addBlasting(NTags.Items.MATERIALS.get(mat.getName()).getRaw(), mat.getIngot().get(), 1.0f, out);
			addSmelting(NTags.Items.MATERIALS.get(mat.getName()).getRaw(), mat.getIngot().get(), 1.0f, out);
			
			addBlasting(NTags.Items.MATERIALS.get(mat.getName()).getOre(), mat.getIngot().get(), 1.0f, out);
			addSmelting(NTags.Items.MATERIALS.get(mat.getName()).getOre(), mat.getIngot().get(), 1.0f, out);
		
			//=========================
			//Raw Storage Block to raw and back
			//=========================
			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, mat.getRawStorageBlock().get()).
			define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getRaw())).
			pattern("AAA").
			pattern("AAA").
			pattern("AAA").
			unlockedBy("has_" + NDatabase.Items.Names.RAW +"_" + mat.getName(), has(NTags.Items.MATERIALS.get(mat.getName()).getRaw())).
			save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getRaw().getId().getPath() + "_to_" + mat.getRawStorageBlock().getId().getPath()));
		
			ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, mat.getRaw().get(), 9).
			requires(Ingredient.of(mat.getRawStorageBlock().get())).
			unlockedBy("has_" + NDatabase.Blocks.Names.STORAGE_BLOCK + "_" + NDatabase.Items.Names.RAW + "_" + mat.getName(), has(mat.getRawStorageBlock().get())).
			save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getRawStorageBlock().getId().getPath() + "_to_" + mat.getRaw().getId().getPath()));
		}
		
		
		//=========================
		//Skystone nuggets to Block and back
		//=========================
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.SKYSTONE.get()).
		define('A', Ingredient.of(NRegistration.RegisterItems.NUGGET_SKYSTONE.get())).
		pattern("AAA").
		pattern("AAA").
		pattern("AAA").
		unlockedBy("has_" + NDatabase.Items.Names.NUGGET + "_" + NRegistration.RegisterItems.NUGGET_SKYSTONE.getId().getPath(), has(NRegistration.RegisterItems.NUGGET_SKYSTONE.get())).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + NRegistration.RegisterItems.NUGGET_SKYSTONE.getId().getPath() + "_to_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath()));
	
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NRegistration.RegisterItems.NUGGET_SKYSTONE.get(), 9).
		requires(Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.get())).
		unlockedBy("has_" + NDatabase.Items.Names.NUGGET + "_" + NDatabase.Blocks.Names.SKYSTONE, has(NRegistration.RegisterBlocks.SKYSTONE.get())).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath() + "_to_" + NRegistration.RegisterItems.NUGGET_SKYSTONE.getId().getPath()));
		
		
		//==========================
		//Tools
		//==========================
		addTools(mat, out);
		
		//==========================
		//Weapon
		//==========================
		addWeapon(mat, out);
		
		//==========================
		//Armor
		//==========================
		addArmor(mat, out);
		
		addShieldDecorationRecipe(out, NRegistration.RegisterRecipes.SHIELD_SERIALIZER.get());
		
		
		//==========================
		//BOOK
		//==========================
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NRegistration.RegisterItems.BOOK.get()).
		requires(Ingredient.of(NRegistration.RegisterItems.NUGGET_SKYSTONE.get())).
		requires(Ingredient.of(net.minecraft.world.item.Items.BOOK)).
		unlockedBy("has_" + NRegistration.RegisterItems.NUGGET_SKYSTONE.getId(), has(NRegistration.RegisterItems.NUGGET_SKYSTONE.get())).
		save(out, NRegistration.RegisterItems.BOOK.getId());
		
		//Hammer
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterItems.HAMMER.get()).
		define('A', Ingredient.of(Tags.Items.RODS_WOODEN)).
		define('B', Ingredient.of(Tags.Items.INGOTS_IRON)).
		pattern(" B ").
		pattern("BA ").
		pattern("  A").
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		unlockedBy("has_" + Tags.Items.INGOTS_IRON.location().getPath(), has(Tags.Items.INGOTS_IRON)).
		save(out, NRegistration.RegisterItems.HAMMER.getId());
		
		//Terramorfer
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.TERRAMORFER.get()).
		define('A', Ingredient.of(Tags.Items.GLASS_PANES)).
		define('B', Ingredient.of(Tags.Items.STONE)).
		define('C', Ingredient.of(NRegistration.RegisterItems.NUGGET_SKYSTONE.get())).
		pattern(" A ").
		pattern("ACA").
		pattern("BBB").
		unlockedBy("has_" + Tags.Items.GLASS_PANES.location().getPath(), has(Tags.Items.GLASS_PANES)).
		unlockedBy("has_" + Tags.Items.STONE.location().getPath(), has(Tags.Items.STONE)).
		unlockedBy("has_" + NRegistration.RegisterItems.NUGGET_SKYSTONE.getId(), has(NRegistration.RegisterItems.NUGGET_SKYSTONE.get())).
		save(out, NRegistration.RegisterBlocks.TERRAMORFER.getId());
		
		crusherRecipes(out);
		diffuserRecipes(out);
		
		//Pedestal
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.PEDESTAL.asItem()).
		define('A', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.get().asItem())).
		pattern(" A ").
		pattern(" A ").
		pattern("AAA").
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.get())).
		save(out, NRegistration.RegisterBlocks.PEDESTAL.getId());

		//Holder
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.HOLDER.asItem()).
		define('A', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.get().asItem())).
		pattern("AAA").
		pattern(" A ").
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.get())).
		save(out, NRegistration.RegisterBlocks.HOLDER.getId());
		
		//Diffuser
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.DIFFUSER.asItem()).
		define('A', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.get().asItem())).
		pattern("A A").
		pattern("A A").
		pattern("AAA").
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.get())).
		save(out, NRegistration.RegisterBlocks.DIFFUSER.getId());
		
		//Framework
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.FRAMEWORK.get().asItem()).
		define('A', Ingredient.of(Blocks.CLAY)).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		pattern("BAB").
		pattern("A A").
		pattern("BAB").
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.CLAY).getPath(), has(Blocks.CLAY)).
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		save(out, NRegistration.RegisterBlocks.FRAMEWORK.getId());

		//VimStorage
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.VIM_STORAGE.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(Items.DUSTS_REDSTONE)).
		pattern("CAC").
		pattern("ABA").
		pattern("CAC").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + Items.DUSTS_REDSTONE.location().getPath(), has(Items.DUSTS_REDSTONE)).
		save(out, NRegistration.RegisterBlocks.VIM_STORAGE.getId());
	
		//Hoover
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.HOOVER.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(Blocks.CHEST.asItem())).
		define('D', Ingredient.of(Blocks.HOPPER.asItem())).
		define('E', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		pattern(" C ").
		pattern("ABA").
		pattern("EDE").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.CHEST).getPath(), has(Blocks.CHEST.asItem())).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.HOPPER).getPath(), has(Blocks.HOPPER.asItem())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		save(out, NRegistration.RegisterBlocks.HOOVER.getId());
		
		//Mob Catcher
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.MOB_CATCHER.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(NRegistration.RegisterItems.CRYSTAL_PRISON)).
		define('D', Ingredient.of(Blocks.HOPPER.asItem())).
		define('E', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		pattern(" C ").
		pattern("ABA").
		pattern("EDE").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + NRegistration.RegisterItems.CRYSTAL_PRISON.getId().getPath(), has(NRegistration.RegisterItems.CRYSTAL_PRISON)).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.HOPPER).getPath(), has(Blocks.HOPPER.asItem())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		save(out, NRegistration.RegisterBlocks.MOB_CATCHER.getId());
		
		//Manual Crusher
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.MANUAL_CRUSHER.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(Blocks.PISTON)).
		define('D', Ingredient.of(Items.DUSTS_REDSTONE)).
		define('E', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		pattern("DCD").
		pattern("ABA").
		pattern("EEE").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.PISTON).getPath(), has(Blocks.PISTON)).
		unlockedBy("has_" + Items.DUSTS_REDSTONE.location().getPath(), has(Items.DUSTS_REDSTONE)).
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		save(out, NRegistration.RegisterBlocks.MANUAL_CRUSHER.getId());
		
		//Solar Generator
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.GENERATOR_SOLAR.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(Blocks.DAYLIGHT_DETECTOR)).
		define('D', Ingredient.of(Items.DUSTS_REDSTONE)).
		define('E', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		pattern(" C ").
		pattern("ABA").
		pattern("EDE").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.DAYLIGHT_DETECTOR).getPath(), has(Blocks.DAYLIGHT_DETECTOR)).
		unlockedBy("has_" + Items.DUSTS_REDSTONE.location().getPath(), has(Items.DUSTS_REDSTONE)).
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		save(out, NRegistration.RegisterBlocks.GENERATOR_SOLAR.getId());
		
		//Mob Generator
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.GENERATOR_MOB.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(NRegistration.RegisterItems.CRYSTAL_PRISON)).
		define('D', Ingredient.of(NRegistration.RegisterBlocks.VIM_STORAGE)).
		define('E', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		pattern(" C ").
		pattern("ABA").
		pattern("EDE").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + NRegistration.RegisterItems.CRYSTAL_PRISON.getId().getPath(), has(NRegistration.RegisterItems.CRYSTAL_PRISON)).
		unlockedBy("has_" + NRegistration.RegisterBlocks.VIM_STORAGE.getId().getPath(), has(NRegistration.RegisterBlocks.VIM_STORAGE)).
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		save(out, NRegistration.RegisterBlocks.GENERATOR_MOB.getId());
	
		//Food Generator
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.GENERATOR_FOOD.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(Blocks.HOPPER)).
		define('D', Ingredient.of(NRegistration.RegisterBlocks.VIM_STORAGE)).
		define('E', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		pattern(" C ").
		pattern("ABA").
		pattern("EDE").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.HOPPER).getPath(), has(Blocks.HOPPER)).
		unlockedBy("has_" + NRegistration.RegisterBlocks.VIM_STORAGE.getId().getPath(), has(NRegistration.RegisterBlocks.VIM_STORAGE)).
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		save(out, NRegistration.RegisterBlocks.GENERATOR_FOOD.getId());

		//Furnace
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.FURNACE.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(Blocks.HOPPER)).
		define('D', Ingredient.of(Blocks.FURNACE)).
		define('E', Ingredient.of(Blocks.BRICKS)).
		pattern(" C ").
		pattern("ABA").
		pattern("EDE").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.HOPPER).getPath(), has(Blocks.HOPPER)).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.FURNACE).getPath(), has(Blocks.FURNACE)).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.BRICKS).getPath(), has(Blocks.BRICKS)).
		save(out, NRegistration.RegisterBlocks.FURNACE.getId());
		
		//Fluid Storage
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.FLUID_STORAGE.get().asItem()).
		define('A', Ingredient.of(Tags.Items.GLASS)).
		define('B', Ingredient.of(Blocks.CAULDRON)).
		define('C', Ingredient.of(Items.DUSTS_REDSTONE)).
		define('E', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		pattern("ECE").
		pattern("ABA").
		pattern("ECE").
		unlockedBy("has_" + Items.GLASS.location().getPath(), has(Items.GLASS)).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.CAULDRON).getPath(), has(Blocks.CAULDRON)).
		unlockedBy("has_" + Items.DUSTS_REDSTONE.location().getPath(), has(Items.DUSTS_REDSTONE)).
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		save(out, NRegistration.RegisterBlocks.FLUID_STORAGE.getId());
		
		//Fluid Filler
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.FLUID_FILLER.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(net.minecraft.world.item.Items.BUCKET)).
		pattern("ACA").
		pattern("CBC").
		pattern("ACA").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + ItemHelper.getRegistryName(net.minecraft.world.item.Items.BUCKET).getPath(), has(net.minecraft.world.item.Items.BUCKET)).
		save(out, NRegistration.RegisterBlocks.FLUID_FILLER.getId());
		
		//Extruder
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.EXTRUDER.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(NRegistration.RegisterBlocks.FLUID_STORAGE)).
		define('D', Ingredient.of(net.minecraft.world.item.Items.LAVA_BUCKET)).
		define('E', Ingredient.of(net.minecraft.world.item.Items.WATER_BUCKET)).
		define('F', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		pattern("ACA").
		pattern("DBE").
		pattern("FFF").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FLUID_STORAGE.getId().getPath(), has(NRegistration.RegisterBlocks.FLUID_STORAGE)).
		unlockedBy("has_" + ItemHelper.getRegistryName(net.minecraft.world.item.Items.WATER_BUCKET).getPath(), has(net.minecraft.world.item.Items.WATER_BUCKET)).
		unlockedBy("has_" + ItemHelper.getRegistryName(net.minecraft.world.item.Items.LAVA_BUCKET).getPath(), has(net.minecraft.world.item.Items.LAVA_BUCKET)).
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		save(out, NRegistration.RegisterBlocks.EXTRUDER.getId());
		
		//Crusher
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.CRUSHER.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(Blocks.PISTON)).
		define('D', Ingredient.of(Blocks.CHEST)).
		define('E', Ingredient.of(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		define('F', Ingredient.of(Items.DUSTS_REDSTONE)).
		pattern("FCF").
		pattern("ABA").
		pattern("EDE").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.PISTON).getPath(), has(Blocks.PISTON)).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.CHEST).getPath(), has(Blocks.CHEST)).
		unlockedBy("has_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath(), has(NRegistration.RegisterBlocks.SKYSTONE.asItem())).
		unlockedBy("has_" + Items.DUSTS_REDSTONE.location().getPath(), has(Items.DUSTS_REDSTONE)).
		save(out, NRegistration.RegisterBlocks.CRUSHER.getId());
		
		//Delivery Station
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NRegistration.RegisterBlocks.DELIVERY_STATION.get().asItem()).
		define('A', Ingredient.of(NTags.Items.CORIUM.getIngot())).
		define('B', Ingredient.of(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		define('C', Ingredient.of(Blocks.DISPENSER)).
		pattern("ACA").
		pattern("CBC").
		pattern("ACA").
		unlockedBy("has_" + NTags.Items.CORIUM.getIngot().location().getPath(), has(NTags.Items.CORIUM.getIngot())).
		unlockedBy("has_" + NRegistration.RegisterBlocks.FRAMEWORK.getId().getPath(), has(NRegistration.RegisterBlocks.FRAMEWORK.asItem())).
		unlockedBy("has_" + BlockHelper.getRegistryName(Blocks.DISPENSER).getPath(), has(Blocks.DISPENSER)).
		save(out, NRegistration.RegisterBlocks.DELIVERY_STATION.getId());
	}

	private void crusherRecipes(Consumer<FinishedRecipe> out) 
	{
/*		NCrusherRecipeBuilder.builder(NRegistration.RegisterItems.CHALK.asItem()).
			addInput(Items.ENDER_PEARLS).
			setEnergy(1600).
			build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + NRegistration.RegisterItems.CHALK.getId().getPath()));
*/
		NCrusherRecipeBuilder.builder(Blocks.COBBLESTONE.asItem()).
		addInput(Blocks.STONE.asItem()).
		setEnergy(500).
		build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + getConversionRecipeName(Blocks.COBBLESTONE, Blocks.STONE)));
	
		NCrusherRecipeBuilder.builder(Blocks.GRAVEL.asItem()).
		addInput(Blocks.COBBLESTONE.asItem()).
		setEnergy(1000).
		addSecondary(Blocks.SAND.asItem(), 0.15f).
		build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + getConversionRecipeName(Blocks.GRAVEL, Blocks.COBBLESTONE)));
		
		NCrusherRecipeBuilder.builder(Blocks.SAND.asItem()).
		addInput(Blocks.GRAVEL.asItem()).
		setEnergy(750).
		addSecondary(net.minecraft.world.item.Items.FLINT, 0.15f).
		build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + getConversionRecipeName(Blocks.SAND, Blocks.GRAVEL)));
		
		ModMaterial mat = NRegistration.RegisterMaterials.CORIUM;

		NCrusherRecipeBuilder.builder(new IngredientWithSize(NTags.Items.CORIUM.getDust(), 2)).
		addInput(NTags.Items.CORIUM.getOre()).
		setEnergy(1000).
		build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + getConversionRecipeName(mat.getDust(), mat.getOreBlock())));
		
		NCrusherRecipeBuilder.builder(new IngredientWithSize(Items.DUSTS_REDSTONE, 8)).
		addInput(ItemTags.REDSTONE_ORES).
		setEnergy(1000).
		build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + getConversionRecipeName(net.minecraft.world.item.Items.REDSTONE, Blocks.REDSTONE_ORE)));
		
		NCrusherRecipeBuilder.builder(new IngredientWithSize(ItemTags.COALS, 8)).
		addInput(ItemTags.COAL_ORES).
		setEnergy(1000).
		build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + getConversionRecipeName(net.minecraft.world.item.Items.COAL, Blocks.COAL_ORE)));
	
		NCrusherRecipeBuilder.builder(new IngredientWithSize(Items.GEMS_EMERALD, 8)).
		addInput(ItemTags.EMERALD_ORES).
		setEnergy(1000).
		build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + getConversionRecipeName(net.minecraft.world.item.Items.EMERALD, Blocks.EMERALD_ORE)));
		
		NCrusherRecipeBuilder.builder(new IngredientWithSize(Items.GEMS_LAPIS, 8)).
		addInput(ItemTags.LAPIS_ORES).
		setEnergy(1000).
		build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + getConversionRecipeName(net.minecraft.world.item.Items.LAPIS_LAZULI, Blocks.LAPIS_ORE)));
		
		NCrusherRecipeBuilder.builder(new IngredientWithSize(Items.GEMS_DIAMOND, 8)).
		addInput(ItemTags.DIAMOND_ORES).
		setEnergy(1000).
		build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + getConversionRecipeName(net.minecraft.world.item.Items.DIAMOND, Blocks.DIAMOND_ORE)));
		
		NCrusherRecipeBuilder.builder(new IngredientWithSize(Items.GEMS_QUARTZ, 8)).
		addInput(Items.ORES_QUARTZ).
		setEnergy(1000).
		build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + getConversionRecipeName(net.minecraft.world.item.Items.QUARTZ, Blocks.NETHER_QUARTZ_ORE)));
	}

	private void diffuserRecipes(Consumer<FinishedRecipe> out)
	{
		NDiffuserRecipeBuilder.builder(NRegistration.RegisterBlocks.CRYSTAL_GROWTH.asItem()).
			addInput(Blocks.AMETHYST_CLUSTER).
			addFluid(Fluids.WATER, 1000).
			setTime(500).
			build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.DIFFUSER + "/" + NRegistration.RegisterBlocks.CRYSTAL_GROWTH.getId().getPath()));

		NDiffuserRecipeBuilder.builder(NRegistration.RegisterMaterials.CORIUM.getOreBlock().asItem()).
			addInput(Tags.Items.ORES_IRON).
			addFluid(Fluids.LAVA, 1000).
			setTime(500).
			build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.DIFFUSER + "/" + NRegistration.RegisterMaterials.CORIUM.getOreBlock().getId().getPath()));
		
		NDiffuserRecipeBuilder.builder(NRegistration.RegisterItems.CRYSTAL_PRISON.get()).
			addInput(Items.GEMS_AMETHYST).
			addFluid(Fluids.WATER, 1000).
			setTime(200).
			build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.DIFFUSER + "/" + NRegistration.RegisterItems.CRYSTAL_PRISON.getId().getPath()));
	}
	
	private void addBlasting (TagKey<Item> input, Item output, float exp, Consumer<FinishedRecipe> out)
	{	
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.MISC, output, exp, 100).
		unlockedBy("has_" + input.location().getPath(), has(input)).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.BLASTING + "/" + input.location().getPath() + "_to_" + ItemHelper.getRegistryName(output).getPath()));
	}
	
	private void addSmelting (TagKey<Item> input, Item output, float exp, Consumer<FinishedRecipe> out)
	{
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, output, exp, 200).
		unlockedBy("has_" + input.location().getPath(), has(input)).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.SMELTING + "/" + input.location().getPath() + "_to_" + ItemHelper.getRegistryName(output).getPath()));
	}

	private void addTools(ModMaterial mat, Consumer<FinishedRecipe> out)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getAxe().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.RODS_WOODEN)).
		pattern("AA").
		pattern("AB").
		pattern(" B").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		save(out, StringHelper.getLocFStr(mat.getAxe().getId().getPath()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getPickaxe().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.RODS_WOODEN)).
		pattern("AAA").
		pattern(" B ").
		pattern(" B ").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		save(out, StringHelper.getLocFStr(mat.getPickaxe().getId().getPath()));
	
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getShovel().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.RODS_WOODEN)).
		pattern("A").
		pattern("B").
		pattern("B").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		save(out, StringHelper.getLocFStr(mat.getShovel().getId().getPath()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getHoe().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.RODS_WOODEN)).
		pattern("AA").
		pattern(" B").
		pattern(" B").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		save(out, StringHelper.getLocFStr(mat.getHoe().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getShears().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("A ").
		pattern(" A").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(mat.getShears().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getFishingRod().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.STRING)).
		pattern("  A").
		pattern(" AB").
		pattern("A B").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.STRING.location().getPath(), has(Tags.Items.STRING)).
		save(out, StringHelper.getLocFStr(mat.getFishingRod().getId().getPath()));
	}
	
	private void addWeapon(ModMaterial mat, Consumer<FinishedRecipe> out)
	{
		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.getSword().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.RODS_WOODEN)).
		pattern("A").
		pattern("A").
		pattern("B").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		save(out, StringHelper.getLocFStr(mat.getSword().getId().getPath()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.getBow().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.STRING)).
		pattern("BA ").
		pattern("B A").
		pattern("BA ").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.STRING.location().getPath(), has(Tags.Items.STRING)).
		save(out, StringHelper.getLocFStr(mat.getBow().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.getCrossbow().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.STRING)).
		define('C', Ingredient.of(Tags.Items.RODS_WOODEN)).
		define('D', Ingredient.of(net.minecraft.world.item.Items.TRIPWIRE_HOOK)).
		pattern("CAC").
		pattern("BDB").
		pattern(" C ").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.STRING.location().getPath(), has(Tags.Items.STRING)).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		unlockedBy("has_" + ItemHelper.getRegistryName(net.minecraft.world.item.Items.TRIPWIRE_HOOK).getPath(), has(net.minecraft.world.item.Items.TRIPWIRE_HOOK)).
		save(out, StringHelper.getLocFStr(mat.getCrossbow().getId().getPath()));

		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.getShield().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(ItemTags.PLANKS)).
		pattern("BAB").
		pattern("BBB").
		pattern(" B ").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + ItemTags.PLANKS.location().getPath(), has(ItemTags.PLANKS)).
		save(out, StringHelper.getLocFStr(mat.getShield().getId().getPath()));
	}
	
	private void addArmor(ModMaterial mat, Consumer<FinishedRecipe> out)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getArmorHorse().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.LEATHER)).
		define('C', Ingredient.of(ItemTags.WOOL)).
		pattern("A A").
		pattern("BCB").
		pattern("A A").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.LEATHER.location().getPath(), has(Tags.Items.LEATHER)).
		unlockedBy("has_" + ItemTags.WOOL.location().getPath(), has(ItemTags.WOOL)).
		save(out, StringHelper.getLocFStr(mat.getArmorHorse().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getPlayerArmorHead().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("AAA").
		pattern("A A").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(mat.getPlayerArmorHead().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getPlayerArmorChest().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("A A").
		pattern("AAA").
		pattern("AAA").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(mat.getPlayerArmorChest().getId().getPath()));
	
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getPlayerArmorLegs().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("AAA").
		pattern("A A").
		pattern("A A").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(mat.getPlayerArmorLegs().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getPlayerArmorFeet().get()).
		define('A', Ingredient.of(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("A A").
		pattern("A A").
		unlockedBy("has_" + NTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(NTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(mat.getPlayerArmorFeet().getId().getPath()));

	}
	
	private void addShieldDecorationRecipe(Consumer<FinishedRecipe> out, SimpleCraftingRecipeSerializer<?> serializer)
	{
		SpecialRecipeBuilder.special(serializer).save(out, StringHelper.getStrLocFStr("dynamic/" + ForgeRegistries.RECIPE_SERIALIZERS.getKey(serializer).getPath()));
	}
	
/*	@Override
	public String getName() 
	{
		return "Nedaire Recipe Provider";
	}
*/
}

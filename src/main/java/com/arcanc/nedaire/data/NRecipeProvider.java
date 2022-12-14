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
import com.arcanc.nedaire.data.crafting.builders.NCrusherRecipeBuilder;
import com.arcanc.nedaire.data.tags.ModTags;
import com.arcanc.nedaire.util.database.NDatabase;
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
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getNugget())).
		pattern("AAA").
		pattern("AAA").
		pattern("AAA").
		unlockedBy("has_" + NDatabase.Items.Names.NUGGET +"_" + mat.getName(), has(ModTags.Items.MATERIALS.get(mat.getName()).getNugget())).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getNugget().getId().getPath() + "_to_" + mat.getIngot().getId().getPath()));
	
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, mat.getNugget().get(), 9).
		requires(Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + NDatabase.Items.Names.INGOT + "_" + mat.getName(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getIngot().getId().getPath() + "_to_" + mat.getNugget().getId().getPath()));

		//=========================
		//Storage Block to Ingots and back
		//=========================
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, mat.getStorageBlock().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("AAA").
		pattern("AAA").
		pattern("AAA").
		unlockedBy("has_" + NDatabase.Items.Names.INGOT +"_" + mat.getName(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getIngot().getId().getPath() + "_to_" + mat.getStorageBlock().getId().getPath()));
	
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, mat.getIngot().get(), 9).
		requires(Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getStorageBlock())).
		unlockedBy("has_" + NDatabase.Blocks.Names.STORAGE_BLOCK +"_" + mat.getName(), has(ModTags.Items.MATERIALS.get(mat.getName()).getStorageBlock())).
		save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getStorageBlock().getId().getPath() + "_to_" + mat.getIngot().getId().getPath()));

		//==========================
		//Smelting and Blasting
		//==========================
	
		addBlasting(ModTags.Items.MATERIALS.get(mat.getName()).getDust(), mat.getIngot().get(), 0.0f, out);
		addSmelting(ModTags.Items.MATERIALS.get(mat.getName()).getDust(), mat.getIngot().get(), 0.0f, out);
		
		if (mat.requiredOre())
		{
			addBlasting(ModTags.Items.MATERIALS.get(mat.getName()).getRaw(), mat.getIngot().get(), 1.0f, out);
			addSmelting(ModTags.Items.MATERIALS.get(mat.getName()).getRaw(), mat.getIngot().get(), 1.0f, out);
			
			addBlasting(ModTags.Items.MATERIALS.get(mat.getName()).getOre(), mat.getIngot().get(), 1.0f, out);
			addSmelting(ModTags.Items.MATERIALS.get(mat.getName()).getOre(), mat.getIngot().get(), 1.0f, out);
		
			//=========================
			//Raw Storage Block to raw and back
			//=========================
			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, mat.getRawStorageBlock().get()).
			define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getRaw())).
			pattern("AAA").
			pattern("AAA").
			pattern("AAA").
			unlockedBy("has_" + NDatabase.Items.Names.RAW +"_" + mat.getName(), has(ModTags.Items.MATERIALS.get(mat.getName()).getRaw())).
			save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getRaw().getId().getPath() + "_to_" + mat.getRawStorageBlock().getId().getPath()));
		
			ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, mat.getRaw().get(), 9).
			requires(Ingredient.of(mat.getRawStorageBlock().get())).
			unlockedBy("has_" + NDatabase.Blocks.Names.STORAGE_BLOCK + "_" + NDatabase.Items.Names.RAW + "_" + mat.getName(), has(mat.getRawStorageBlock().get())).
			save(out, StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + mat.getRawStorageBlock().getId().getPath() + "_to_" + mat.getRaw().getId().getPath()));
		}
		
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
		requires(Ingredient.of(mat.getDust().get())).
		unlockedBy("has_" + NDatabase.Items.Names.DUST + "_" + mat.getName(), has(mat.getDust().get())).
		save(out, NRegistration.RegisterItems.BOOK.getId());
		
		crusherRecipes(out);
	}

	private void crusherRecipes(Consumer<FinishedRecipe> out) 
	{
		NCrusherRecipeBuilder.builder(NRegistration.RegisterItems.CHALK.asItem()).
			addInput(Items.ENDER_PEARLS).
			setEnergy(1600).
			build(out, StringHelper.getLocFStr(NDatabase.Recipes.Types.CRUSHER + "/" + NRegistration.RegisterItems.CHALK.getId().getPath()));
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
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.RODS_WOODEN)).
		pattern("AA").
		pattern("AB").
		pattern(" B").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		save(out, StringHelper.getLocFStr(mat.getAxe().getId().getPath()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getPickaxe().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.RODS_WOODEN)).
		pattern("AAA").
		pattern(" B ").
		pattern(" B ").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		save(out, StringHelper.getLocFStr(mat.getPickaxe().getId().getPath()));
	
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getShovel().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.RODS_WOODEN)).
		pattern("A").
		pattern("B").
		pattern("B").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		save(out, StringHelper.getLocFStr(mat.getShovel().getId().getPath()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getHoe().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.RODS_WOODEN)).
		pattern("AA").
		pattern(" B").
		pattern(" B").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		save(out, StringHelper.getLocFStr(mat.getHoe().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getShears().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("A ").
		pattern(" A").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(mat.getShears().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, mat.getFishingRod().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.STRING)).
		pattern("  A").
		pattern(" AB").
		pattern("A B").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.STRING.location().getPath(), has(Tags.Items.STRING)).
		save(out, StringHelper.getLocFStr(mat.getFishingRod().getId().getPath()));
	}
	
	private void addWeapon(ModMaterial mat, Consumer<FinishedRecipe> out)
	{
		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.getSword().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.RODS_WOODEN)).
		pattern("A").
		pattern("A").
		pattern("B").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		save(out, StringHelper.getLocFStr(mat.getSword().getId().getPath()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.getBow().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.STRING)).
		pattern("BA ").
		pattern("B A").
		pattern("BA ").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.STRING.location().getPath(), has(Tags.Items.STRING)).
		save(out, StringHelper.getLocFStr(mat.getBow().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.getCrossbow().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.STRING)).
		define('C', Ingredient.of(Tags.Items.RODS_WOODEN)).
		define('D', Ingredient.of(net.minecraft.world.item.Items.TRIPWIRE_HOOK)).
		pattern("CAC").
		pattern("BDB").
		pattern(" C ").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.STRING.location().getPath(), has(Tags.Items.STRING)).
		unlockedBy("has_" + Tags.Items.RODS_WOODEN.location().getPath(), has(Tags.Items.RODS_WOODEN)).
		unlockedBy("has_" + ItemHelper.getRegistryName(net.minecraft.world.item.Items.TRIPWIRE_HOOK).getPath(), has(net.minecraft.world.item.Items.TRIPWIRE_HOOK)).
		save(out, StringHelper.getLocFStr(mat.getCrossbow().getId().getPath()));

		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.getShield().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(ItemTags.PLANKS)).
		pattern("BAB").
		pattern("BBB").
		pattern(" B ").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + ItemTags.PLANKS.location().getPath(), has(ItemTags.PLANKS)).
		save(out, StringHelper.getLocFStr(mat.getShield().getId().getPath()));
	}
	
	private void addArmor(ModMaterial mat, Consumer<FinishedRecipe> out)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getArmorHorse().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		define('B', Ingredient.of(Tags.Items.LEATHER)).
		define('C', Ingredient.of(ItemTags.WOOL)).
		pattern("A A").
		pattern("BCB").
		pattern("A A").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		unlockedBy("has_" + Tags.Items.LEATHER.location().getPath(), has(Tags.Items.LEATHER)).
		unlockedBy("has_" + ItemTags.WOOL.location().getPath(), has(ItemTags.WOOL)).
		save(out, StringHelper.getLocFStr(mat.getArmorHorse().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getPlayerArmorHead().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("AAA").
		pattern("A A").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(mat.getPlayerArmorHead().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getPlayerArmorChest().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("A A").
		pattern("AAA").
		pattern("AAA").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(mat.getPlayerArmorChest().getId().getPath()));
	
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getPlayerArmorLegs().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("AAA").
		pattern("A A").
		pattern("A A").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		save(out, StringHelper.getLocFStr(mat.getPlayerArmorLegs().getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mat.getPlayerArmorFeet().get()).
		define('A', Ingredient.of(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
		pattern("A A").
		pattern("A A").
		unlockedBy("has_" + ModTags.Items.MATERIALS.get(mat.getName()).getIngot().location().getPath(), has(ModTags.Items.MATERIALS.get(mat.getName()).getIngot())).
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

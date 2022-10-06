/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.language;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.material.ModMaterial;
import com.arcanc.nedaire.content.registration.ModRegistration;
import com.arcanc.nedaire.util.database.ModDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModEnUsLangProvider extends LanguageProvider 
{

	public ModEnUsLangProvider(DataGenerator gen) 
	{
		super(gen, ModDatabase.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() 
	{
		add(Nedaire.getInstance().TAB.getDisplayName().getString(), "Nedaire");
		
		ModMaterial mat = ModRegistration.RegisterMaterials.CORIUM;
		
		add(mat.getIngot().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.INGOT));
		add(mat.getNugget().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.NUGGET));
		add(mat.getDust().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.DUST));
		
		add(mat.getPickaxe().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Tools.PICKAXE));
		add(mat.getAxe().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Tools.AXE));
		add(mat.getShovel().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Tools.SHOVEL));
		add(mat.getHoe().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Tools.HOE));
		add(mat.getShears().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Tools.SHEARS));
		add(mat.getFishingRod().get(), StringHelper.capitalize(mat.getName()) + " " + "Fishhing Rod");

		add(mat.getShield().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Weapon.SHIELD));
		add(mat.getSword().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Weapon.SWORD));
		add(mat.getBow().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Weapon.BOW));
		add(mat.getCrossbow().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Weapon.CROSSBOW));

		add(mat.getArmorHorse().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Armor.ARMOR_HORSE) + " " + StringHelper.capitalize(ModDatabase.Items.Names.ARMOR));
		add(ModDatabase.MOD_ID + ".item.modifiers.horse", "When on Horse:");
		add(mat.getPlayerArmorHead().get(), StringHelper.capitalize(mat.getName()) + " " + "Helmet");
		add(mat.getPlayerArmorChest().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Items.Names.Armor.ARMOR_CHEST + "plate"));
		add(mat.getPlayerArmorLegs().get(), StringHelper.capitalize(mat.getName()) + " " + "Leggings");
		add(mat.getPlayerArmorFeet().get(), StringHelper.capitalize(mat.getName()) + " " + "Boots");
		
		add(mat.getStorageBlock().get(), "Block" + " of " +  StringHelper.capitalize(mat.getName()));
	
		if (mat.requiredOre())
		{
			add(mat.getRaw().get(), StringHelper.capitalize(ModDatabase.Items.Names.RAW) + " " + StringHelper.capitalize(mat.getName())); 
			
			add(mat.getRawStorageBlock().get(), "Block" + " of " +  StringHelper.capitalize(ModDatabase.Items.Names.RAW) + " " + StringHelper.capitalize(mat.getName()));
			
			add(mat.getOreBlock().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Blocks.Names.ORE));

			add(mat.getDeepSlateOre().get(), StringHelper.capitalize(ModDatabase.Items.Names.DEEPSLATE) + " " + StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(ModDatabase.Blocks.Names.ORE));
		}
		
		add(ModRegistration.RegisterItems.HAMMER.get(), StringHelper.capitalize(ModDatabase.Items.Names.HAMMER));

		add(ModRegistration.RegisterBlocks.SKYSTONE.get().asItem(), StringHelper.capitalize(ModDatabase.Blocks.Names.SKYSTONE));
		
		add(ModRegistration.RegisterBlocks.PEDESTAL.get().asItem(), StringHelper.capitalize(ModDatabase.Blocks.BlockEntities.Names.PEDESTAL));
		add(ModRegistration.RegisterBlocks.HOLDER.get().asItem(), StringHelper.capitalize(ModDatabase.Blocks.BlockEntities.Names.HOLDER));
		
		
		//************************************************************************
		//SOCKET
		add(ModDatabase.MOD_ID + ".socket.contains", "Contains Unside:");
		add(ModDatabase.MOD_ID + ".socket.number", "Slot #%s : %s");
		add(ModDatabase.MOD_ID + ".socket.empty", "Empty");
		add(ModDatabase.MOD_ID + ".socket.unknown", "Wrong Item");
		//************************************************************************
		//BOOK
		add(ModRegistration.RegisterItems.BOOK.get(), StringHelper.capitalize(ModDatabase.GUI.Enchiridion.ENCHIRIDION));
		//************************************************************************
		//Full Book Content
		addEnchiridionContent();
		//************************************************************************
		
		
	}
	
	private void addEnchiridionContent() 
	{
		add(ModDatabase.GUI.Enchiridion.Recipes.Translatable.TICKS, "Ticks: %s");
		add(ModDatabase.GUI.Enchiridion.Recipes.Translatable.EXPERIENCE, "Exp: %s");
		add(ModDatabase.GUI.Enchiridion.Recipes.Translatable.SHAPED, "Shaped crafting");
		add(ModDatabase.GUI.Enchiridion.Recipes.Translatable.SHAPELESS, "Shapeless crafting");
		add(ModDatabase.GUI.Enchiridion.Arrows.ARROW_LEFT, "Previous Page");
		add(ModDatabase.GUI.Enchiridion.Arrows.ARROW_RIGHT, "Next Page");
		add(ModDatabase.GUI.Enchiridion.Arrows.ARROW_TO_START, "To Title");
		
		/* TESTTING! */
		//-------------------------------------------------------------------------------------------------------------------------------------------------------
		add(ModDatabase.GUI.Enchiridion.Section.ResourceLocations.ALCHEMY.toLanguageKey(), StringHelper.capitalize(ModDatabase.GUI.Enchiridion.Section.ALCHEMY));
		
		add(ModDatabase.GUI.Enchiridion.Section.ResourceLocations.JEWELRY.toLanguageKey(), StringHelper.capitalize(ModDatabase.GUI.Enchiridion.Section.JEWELRY));
		
		add(ModDatabase.GUI.Enchiridion.Section.ResourceLocations.MAGIC.toLanguageKey(), StringHelper.capitalize(ModDatabase.GUI.Enchiridion.Section.MAGIC));
	
		add(ModDatabase.GUI.Enchiridion.Chapters.test.toLanguageKey(), "Just test chapter");
		
		add(ModDatabase.GUI.Enchiridion.Chapters.test2.toLanguageKey(), "Another test chapter");

		add(ModDatabase.GUI.Enchiridion.Chapters.test3.toLanguageKey(), "Yet another one test chapter");
		add(ModDatabase.GUI.Enchiridion.Chapters.test4.toLanguageKey(), "Test4");
		add(ModDatabase.GUI.Enchiridion.Chapters.test5.toLanguageKey(), "Yet another one test chapter5");
		add(ModDatabase.GUI.Enchiridion.Chapters.test6.toLanguageKey(), "Yet another one test chapter6");
		add(ModDatabase.GUI.Enchiridion.Chapters.test7.toLanguageKey(), "Yet another one test chapter7");
		add(ModDatabase.GUI.Enchiridion.Chapters.test8.toLanguageKey(), "Yet another one test chapter8");
		add(ModDatabase.GUI.Enchiridion.Chapters.test9.toLanguageKey(), "Yet another one test chapter9");
		add(ModDatabase.GUI.Enchiridion.Chapters.test10.toLanguageKey(), "Yet another one test chapter10");
		add(ModDatabase.GUI.Enchiridion.Chapters.test11.toLanguageKey(), "Yet another one test chapter11");
		add(ModDatabase.GUI.Enchiridion.Chapters.test12.toLanguageKey(), "Yet another one test chapter12");
		add(ModDatabase.GUI.Enchiridion.Chapters.test13.toLanguageKey(), "Yet another one test chapter13");
		add(ModDatabase.GUI.Enchiridion.Chapters.test14.toLanguageKey(), "Yet another one test chapter14");
		add(ModDatabase.GUI.Enchiridion.Chapters.test15.toLanguageKey(), "Yet another one test chapter15");
		add(ModDatabase.GUI.Enchiridion.Chapters.test16.toLanguageKey(), "Yet another one test chapter16");
		
		add(ModDatabase.GUI.Enchiridion.Pages.test.toLanguageKey(), "It's just text to test options. Here I was draw item </item;minecraft:diamond/> and entity </entity;minecraft:cat/> But Here I have another item </item;minecraft:black_candle/> which must be taken from string. So now I need even bigger string for test. </entity;minecraft:piglin/>. And Im' thinking about drawing EVEN more entity... Like this! </entity;minecraft:rabbit/> </entity;minecraft:drowned/> </entity;minecraft:magma_cube/> and last </recipe;minecraft:diamond_from_blasting_deepslate_diamond_ore/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test2.toLanguageKey(), "This is not too long test page, just with some text, to be drawed");
		add(ModDatabase.GUI.Enchiridion.Pages.test3.toLanguageKey(), "Only testing page3, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test4.toLanguageKey(), "Only testing page4, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test5.toLanguageKey(), "Only testing page5, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test6.toLanguageKey(), "Only testing page6, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test7.toLanguageKey(), "Only testing page7, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test8.toLanguageKey(), "Only testing page8, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test9.toLanguageKey(), "Only testing page9, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test10.toLanguageKey(), "Only testing page10, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test11.toLanguageKey(), "Only testing page11, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test12.toLanguageKey(), "Only testing page12, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test13.toLanguageKey(), "Only testing page13, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test14.toLanguageKey(), "Only testing page14, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test15.toLanguageKey(), "Only testing page15, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		add(ModDatabase.GUI.Enchiridion.Pages.test16.toLanguageKey(), "Only testing page16, with 2 different testing entity: </entity;minecraft:squid/> </entity;minecraft:cat/>");
		//-------------------------------------------------------------------------------------------------------------------------------------------------------
	}
	
	
	
	@Override
	public String getName() 
	{
		return "Nedaire EnUs Provider";
	}

}

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
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class NEnUsLangProvider extends LanguageProvider 
{

	public NEnUsLangProvider(DataGenerator gen) 
	{
		super(gen, NDatabase.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() 
	{
		add(Nedaire.getInstance().TAB.getDisplayName().getString(), "Nedaire");
		
		ModMaterial mat = NRegistration.RegisterMaterials.CORIUM;
		
		add(mat.getIngot().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.INGOT));
		add(mat.getNugget().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.NUGGET));
		add(mat.getDust().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.DUST));
		
		add(mat.getPickaxe().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Tools.PICKAXE));
		add(mat.getAxe().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Tools.AXE));
		add(mat.getShovel().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Tools.SHOVEL));
		add(mat.getHoe().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Tools.HOE));
		add(mat.getShears().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Tools.SHEARS));
		add(mat.getFishingRod().get(), StringHelper.capitalize(mat.getName()) + " " + "Fishhing Rod");

		add(mat.getShield().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Weapon.SHIELD));
		add(mat.getSword().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Weapon.SWORD));
		add(mat.getBow().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Weapon.BOW));
		add(mat.getCrossbow().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Weapon.CROSSBOW));

		add(mat.getArmorHorse().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Armor.ARMOR_HORSE) + " " + StringHelper.capitalize(NDatabase.Items.Names.ARMOR));
		add(NDatabase.MOD_ID + ".item.modifiers.horse", "When on Horse:");
		add(mat.getPlayerArmorHead().get(), StringHelper.capitalize(mat.getName()) + " " + "Helmet");
		add(mat.getPlayerArmorChest().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Armor.ARMOR_CHEST + "plate"));
		add(mat.getPlayerArmorLegs().get(), StringHelper.capitalize(mat.getName()) + " " + "Leggings");
		add(mat.getPlayerArmorFeet().get(), StringHelper.capitalize(mat.getName()) + " " + "Boots");
		
		add(mat.getStorageBlock().get(), "Block" + " of " +  StringHelper.capitalize(mat.getName()));
	
		if (mat.requiredOre())
		{
			add(mat.getRaw().get(), StringHelper.capitalize(NDatabase.Items.Names.RAW) + " " + StringHelper.capitalize(mat.getName())); 
			
			add(mat.getRawStorageBlock().get(), "Block" + " of " +  StringHelper.capitalize(NDatabase.Items.Names.RAW) + " " + StringHelper.capitalize(mat.getName()));
			
			add(mat.getOreBlock().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Blocks.Names.ORE));

			add(mat.getDeepSlateOre().get(), StringHelper.capitalize(NDatabase.Items.Names.DEEPSLATE) + " " + StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Blocks.Names.ORE));
		}
		
		add(NRegistration.RegisterItems.HAMMER.get(), StringHelper.capitalize(NDatabase.Items.Names.HAMMER));
		add(NRegistration.RegisterItems.CHALK.get(), StringHelper.capitalize(NDatabase.Items.Names.CHALK));

		add(NRegistration.RegisterBlocks.SKYSTONE.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.Names.SKYSTONE));
		
		add(NRegistration.RegisterBlocks.PEDESTAL.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.PEDESTAL));
		add(NRegistration.RegisterBlocks.HOLDER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.HOLDER));
		add(NRegistration.RegisterBlocks.MANUAL_CRUSHER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.MANUAL_CRUSHER.replace("_c", " C")));
		
		
		//************************************************************************
		//SOCKET
		add(NDatabase.MOD_ID + ".socket.contains", "Contains Unside:");
		add(NDatabase.MOD_ID + ".socket.number", "Slot #%s : %s");
		add(NDatabase.MOD_ID + ".socket.empty", "Empty");
		add(NDatabase.MOD_ID + ".socket.unknown", "Wrong Item");
		//************************************************************************
		//BOOK
		add(NRegistration.RegisterItems.BOOK.get(), StringHelper.capitalize(NDatabase.GUI.Enchiridion.ENCHIRIDION));
		//************************************************************************
		//Full Book Content
		addEnchiridionContent();
		//************************************************************************
		
		
	}
	
	private void addEnchiridionContent() 
	{
		add(NDatabase.GUI.Enchiridion.Recipes.Translatable.TICKS, "Ticks: %s");
		add(NDatabase.GUI.Enchiridion.Recipes.Translatable.EXPERIENCE, "Exp: %s");
		add(NDatabase.GUI.Enchiridion.Recipes.Translatable.SHAPED, "Shaped crafting");
		add(NDatabase.GUI.Enchiridion.Recipes.Translatable.SHAPELESS, "Shapeless crafting");
		add(NDatabase.GUI.Enchiridion.Arrows.ARROW_LEFT, "Previous Page");
		add(NDatabase.GUI.Enchiridion.Arrows.ARROW_RIGHT, "Next Page");
		add(NDatabase.GUI.Enchiridion.Arrows.ARROW_TO_START, "To Title");
		
		/* TESTTING! */
		//-------------------------------------------------------------------------------------------------------------------------------------------------------
		add(NDatabase.GUI.Enchiridion.Section.ResourceLocations.ALCHEMY.toLanguageKey(), StringHelper.capitalize(NDatabase.GUI.Enchiridion.Section.ALCHEMY));
		
		add(NDatabase.GUI.Enchiridion.Section.ResourceLocations.JEWELRY.toLanguageKey(), StringHelper.capitalize(NDatabase.GUI.Enchiridion.Section.JEWELRY));
		
		add(NDatabase.GUI.Enchiridion.Section.ResourceLocations.MAGIC.toLanguageKey(), StringHelper.capitalize(NDatabase.GUI.Enchiridion.Section.MAGIC));
	
//		add(ModDatabase.GUI.Enchiridion.Chapters.test.toLanguageKey(), "Just test chapter");
		
		//-------------------------------------------------------------------------------------------------------------------------------------------------------
	}
	
	
	
	@Override
	public String getName() 
	{
		return "Nedaire EnUs Provider";
	}

}

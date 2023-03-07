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

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class NEnUsLangProvider extends LanguageProvider 
{

	public NEnUsLangProvider(PackOutput output) 
	{
		super(output, NDatabase.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() 
	{
		add(Nedaire.TAB.getDisplayName().getString(), "Nedaire");
		
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
		
		add(NRegistration.RegisterItems.NUGGET_SKYSTONE.get(), StringHelper.capitalize(NDatabase.Blocks.Names.SKYSTONE) + " " + "Nugget");
		
		add(NRegistration.RegisterItems.HAMMER.get(), StringHelper.capitalize(NDatabase.Items.Names.HAMMER));
		add(NRegistration.RegisterItems.CHALK.get(), StringHelper.capitalize(NDatabase.Items.Names.CHALK));

		add(NRegistration.RegisterItems.CRYSTAL_PRISON.get(), StringHelper.capitalize(NDatabase.Items.Names.CRYSTAL_PRISON.replace("_p", " P")));

		add(NRegistration.RegisterBlocks.SKYSTONE.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.Names.SKYSTONE));
		
		add(NRegistration.RegisterBlocks.TERRAMORFER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.TERRAMORFER));
		add(NRegistration.RegisterBlocks.GENERATOR_SOLAR.get().asItem(), "Solar Generator");
		add(NRegistration.RegisterBlocks.GENERATOR_FOOD.get().asItem(), "Food Generator");
		add(NRegistration.RegisterBlocks.GENERATOR_MOB.get().asItem(), "Mob Generator");
		add(NRegistration.RegisterBlocks.PEDESTAL.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.PEDESTAL));
		add(NRegistration.RegisterBlocks.HOLDER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.HOLDER));
		add(NRegistration.RegisterBlocks.MANUAL_CRUSHER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.MANUAL_CRUSHER.replace("_c", " C")));
		add(NRegistration.RegisterBlocks.FLUID_STORAGE.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.FLUID_STORAGE.replace("_s", " S")));
		add(NRegistration.RegisterBlocks.VIM_STORAGE.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE.replace("_s", " S")));
		add(NRegistration.RegisterBlocks.DELIVERY_STATION.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION.replace("_s", " S")));
		add(NRegistration.RegisterBlocks.HOOVER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.HOOVER));
		add(NRegistration.RegisterBlocks.MOB_CATCHER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.MOB_CATCHER.replace("_c", " C")));
		add(NRegistration.RegisterBlocks.FURNACE.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.FURNACE));
		add(NRegistration.RegisterBlocks.CRUSHER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.CRUSHER));
		add(NRegistration.RegisterBlocks.CRYSTAL_GROWTH.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.Crystal.GROWTH.replace("_c", " C")));
		
		//************************************************************************
		//SOCKET
		add(NDatabase.MOD_ID + ".socket.contains", "Contains Inside:");
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
		//GUI Descriptions 
		// 			Vim Energy
		add(NDatabase.Capabilities.Vim.Lang.DESCRIPTION_MAIN, "This battery contains:\n%d | %d Vim");
		//          Fluid Handler
		add(NDatabase.Capabilities.FluidHandler.Lang.DESCRIPTION_MAIN, "This tank contains:\n%d | %d %s");
		//          RedStroneSensitivePanel
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_NAME, "Restone Control");
		//			Progress Bar
		add(NDatabase.GUI.Elements.ProgressBar.Description.PERCENT, "%d ");
		add(NDatabase.GUI.Elements.ProgressBar.Description.PERCENT_FULL, "%.2f | %.2f");
		
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_CONTROL_MODE, "Control Mode: ");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_CONTROL_MODE_DISABLED, "Disabled");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_CONTROL_MODE_ENABLED, "Enabled");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_NAME, "Required Signal:");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_LOW, "Low");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_HIGHT, "Hight");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_DISABLED, "Disabled");
		//			PanelFilter
		add(NDatabase.GUI.Filter.Description.EXTRACTING_STACK_DECREASE, "Decrease Extraction");
		add(NDatabase.GUI.Filter.Description.EXTRACTING_STACK_INCREASE, "Increase Extraction");
		
		add(NDatabase.GUI.Filter.Description.AMOUNT_IN_DECREASE, "Decrease Max Amount In Inventory");
		add(NDatabase.GUI.Filter.Description.AMOUNT_IN_INCREASE, "Increase Max Amount In Inventory");
		
		add(NDatabase.GUI.Filter.Description.WHITELIST, "Whitelist");
		add(NDatabase.GUI.Filter.Description.BLACKLIST, "Blacklist");
		
		add(NDatabase.GUI.Filter.Description.MOD_OWNER, "Check Mod Owner");
		add(NDatabase.GUI.Filter.Description.MOD_OWNER_IGNORE, "Ignore Mod Owner");
		
		add(NDatabase.GUI.Filter.Description.TAG_USE, "Check Tag Similarity");
		add(NDatabase.GUI.Filter.Description.TAG_IGNORE, "Ignore Tag Similarity");
			
		//			PanelSwitcherPanel
		add(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_NAME, "Main Panel");
		add(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_MAIN, "Panel Swither: %s");
		add(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_FILTER_ITEM, "Item Filter Panel");
		add(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_FILTER_FLUID, "Fluid Filter Panel");
		add(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_FILTER_VIM, "Vim Filter panel");
		//			Delivery Station
		add(NDatabase.GUI.BlockEntities.DeliveryStation.Description.MODE_ITEMS, "This Delivery Station will transfer Items");
		add(NDatabase.GUI.BlockEntities.DeliveryStation.Description.MODE_FLUIDS, "This Delivery Station will transfer Fluids");
		add(NDatabase.GUI.BlockEntities.DeliveryStation.Description.MODE_VIM, "This Delivery Station will transfer Vim Energy");
		//************************************************************************
		//Village
		
		//Villagers
		add("entity.minecraft.villager.nedaire." + NDatabase.Village.Villagers.UNDERGROUNDER, "Undergrounder");
		//************************************************************************
		//Spawn Eggs
		add(NRegistration.RegisterItems.DRONE_SPAWN_EGG.get(), "Drone Spawn Egg");
		
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

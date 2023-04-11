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
import com.arcanc.nedaire.content.material.NMaterial;
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
		
		NMaterial mat = NRegistration.RegisterMaterials.CORIUM;
		
		add(mat.getIngot().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.INGOT));
		add(mat.getNugget().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.NUGGET));
		add(mat.getDust().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.DUST));
		
		add(mat.getPickaxe().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Tools.PICKAXE));
		add(mat.getAxe().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Tools.AXE));
		add(mat.getShovel().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Tools.SHOVEL));
		add(mat.getHoe().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Tools.HOE));
		add(mat.getShears().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Items.Names.Tools.SHEARS));
		add(mat.getFishingRod().get(), StringHelper.capitalize(mat.getName()) + " " + "Fishing Rod");

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
		
		add(mat.getStorageBlock().get(), "Block of " +  StringHelper.capitalize(mat.getName()));
	
		if (mat.requiredOre())
		{
			add(mat.getRaw().get(), StringHelper.capitalize(NDatabase.Items.Names.RAW) + " " + StringHelper.capitalize(mat.getName())); 
			
			add(mat.getRawStorageBlock().get(), "Block of " +  StringHelper.capitalize(NDatabase.Items.Names.RAW) + " " + StringHelper.capitalize(mat.getName()));
			
			add(mat.getOreBlock().get(), StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Blocks.Names.ORE));

			add(mat.getDeepSlateOre().get(), StringHelper.capitalize(NDatabase.Items.Names.DEEPSLATE) + " " + StringHelper.capitalize(mat.getName()) + " " + StringHelper.capitalize(NDatabase.Blocks.Names.ORE));
		}
		
		add(NRegistration.RegisterItems.NUGGET_SKYSTONE.get(), StringHelper.capitalize(NDatabase.Blocks.Names.SKYSTONE) + " " + "Nugget");
		
		add(NRegistration.RegisterItems.HAMMER.get(), StringHelper.capitalize(NDatabase.Items.Names.HAMMER));
		add(NRegistration.RegisterItems.CHALK.get(), StringHelper.capitalize(NDatabase.Items.Names.CHALK));

		add(NRegistration.RegisterItems.CRYSTAL_PRISON.get(), StringHelper.capitalize(NDatabase.Items.Names.CRYSTAL_PRISON.replace("_p", " P")));

		add(NRegistration.RegisterItems.JEWELRY_TOOLS.get(), StringHelper.capitalize(NDatabase.Items.Names.JEWELRY + " Tools"));

		add(NRegistration.RegisterBlocks.SKYSTONE.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.Names.SKYSTONE));
		add(NRegistration.RegisterBlocks.SKYSTONE_STAIRS.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.Names.SKYSTONE).concat(" Stairs"));
		add(NRegistration.RegisterBlocks.SKYSTONE_WALL.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.Names.SKYSTONE).concat(" Wall"));
		add(NRegistration.RegisterBlocks.SKYSTONE_SLAB.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.Names.SKYSTONE).concat(" Slab"));
		
		add(NRegistration.RegisterBlocks.FRAMEWORK.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.Names.FRAMEWORK));
		
		add(NRegistration.RegisterBlocks.TERRAMORFER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.TERRAMORFER));
		add(NRegistration.RegisterBlocks.GENERATOR_SOLAR.get().asItem(), "Solar Generator");
		add(NRegistration.RegisterBlocks.GENERATOR_FOOD.get().asItem(), "Food Generator");
		add(NRegistration.RegisterBlocks.GENERATOR_MOB.get().asItem(), "Mob Generator");
		add(NRegistration.RegisterBlocks.GENERATOR_LIGHTNING.get().asItem(), "Lightning Generator");
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
		add(NRegistration.RegisterBlocks.JEWERLY_TABLE.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.JEWELRY_TABLE.replace("_t", " T")));
		add(NRegistration.RegisterBlocks.CRYSTAL_GROWTH.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.Crystal.GROWTH.replace("_c", " C")));
		add(NRegistration.RegisterBlocks.EXTRUDER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.EXTRUDER));
		add(NRegistration.RegisterBlocks.FLUID_FILLER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.FLUID_FILLER.replace("_f", " F")));
		add(NRegistration.RegisterBlocks.DIFFUSER.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.DIFFUSER));
		add(NRegistration.RegisterBlocks.EXP_EXTRACTOR.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.EXP_EXTRACTOR).replace("_e", " E"));
		add(NRegistration.RegisterBlocks.PLATFORM.get().asItem(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.PLATFORM));

		add(NRegistration.RegisterFluids.EXPERIENCE.bucket().get(), StringHelper.capitalize(NDatabase.Fluids.Names.EXPERIENCE) + " Bucket");
		add(NRegistration.RegisterFluids.EXPERIENCE.type().get().getDescriptionId(), StringHelper.capitalize(NDatabase.Fluids.Names.EXPERIENCE));
		
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
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_NAME, "Redstone Control");
		//			Progress Bar
		add(NDatabase.GUI.Elements.ProgressBar.Description.PERCENT, "%d ");
		add(NDatabase.GUI.Elements.ProgressBar.Description.PERCENT_FULL, "%.2f | %.2f");
		
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_CONTROL_MODE, "Control Mode: ");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_CONTROL_MODE_DISABLED, "Disabled");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_CONTROL_MODE_ENABLED, "Enabled");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_NAME, "Required Signal:");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_LOW, "Low");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_STRONG, "Strong");
		add(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_DISABLED, "Disabled");
		//			PanelFilter
		add(NDatabase.GUI.Filter.Description.EXTRACTING_STACK_DECREASE, "Decrease Extraction");
		add(NDatabase.GUI.Filter.Description.EXTRACTING_STACK_INCREASE, "Increase Extraction");
		
		add(NDatabase.GUI.Filter.Description.AMOUNT_IN_DECREASE, "Decrease Max Amount In Inventory");
		add(NDatabase.GUI.Filter.Description.AMOUNT_IN_INCREASE, "Increase Max Amount In Inventory");
		
		add(NDatabase.GUI.Filter.Description.WHITELIST, "Allow List");
		add(NDatabase.GUI.Filter.Description.BLACKLIST, "Ban List");
		
		add(NDatabase.GUI.Filter.Description.MOD_OWNER, "Check Mod Owner");
		add(NDatabase.GUI.Filter.Description.MOD_OWNER_IGNORE, "Ignore Mod Owner");
		
		add(NDatabase.GUI.Filter.Description.TAG_USE, "Check Tag Similarity");
		add(NDatabase.GUI.Filter.Description.TAG_IGNORE, "Ignore Tag Similarity");
			
		//			PanelSwitcherPanel
		add(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_NAME, "Main Panel");
		add(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_MAIN, "Panel Switcher: %s");
		add(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_FILTER_ITEM, "Item Filter Panel");
		add(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_FILTER_FLUID, "Fluid Filter Panel");
		add(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_FILTER_VIM, "Vim Filter panel");
		//			Delivery Station
		add(NDatabase.GUI.BlockEntities.DeliveryStation.Description.MODE_ITEMS, "This Delivery Station will transfer Items");
		add(NDatabase.GUI.BlockEntities.DeliveryStation.Description.MODE_FLUIDS, "This Delivery Station will transfer Fluids");
		add(NDatabase.GUI.BlockEntities.DeliveryStation.Description.MODE_VIM, "This Delivery Station will transfer Vim Energy");
		//			Extruder
		add(NDatabase.GUI.BlockEntities.Extruder.Description.MODE_COBBLE, "Generate Cobblestone");
		add(NDatabase.GUI.BlockEntities.Extruder.Description.MODE_STONE, "Generate Stone");
		add(NDatabase.GUI.BlockEntities.Extruder.Description.MODE_OBSIDIAN, "Generate Obsidian");
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
		
		add(NDatabase.GUI.Elements.EnchElementDiffuserRecipe.AMOUNT, "Fluid Amount: %s");
		
		/* TESTTING! */
		//-------------------------------------------------------------------------------------------------------------------------------------------------------
		add(NDatabase.GUI.Enchiridion.Section.ResourceLocations.PATCH_NOTES.toLanguageKey(), StringHelper.capitalize(NDatabase.GUI.Enchiridion.Section.PATCH_NOTES.replace("_n", " N")));
		
		add(NDatabase.GUI.Enchiridion.Section.ResourceLocations.BASIC.toLanguageKey(), StringHelper.capitalize(NDatabase.GUI.Enchiridion.Section.BASIC));
		
		add(NDatabase.GUI.Enchiridion.Section.ResourceLocations.ADVANCED.toLanguageKey(), StringHelper.capitalize(NDatabase.GUI.Enchiridion.Section.ADVANCED));
	
		add(NDatabase.GUI.Enchiridion.Chapters.PATCH_0_1.toLanguageKey(), "Initial Release");
		add(NDatabase.GUI.Enchiridion.Pages.PATCH_0_1.toLanguageKey(), "First release. Basic mechanics, like energy furnace, crusher, manual crusher, delivery station and more");

		add(NDatabase.GUI.Enchiridion.Chapters.PATCH_0_3.toLanguageKey(), "Patch 0.3");
		add(NDatabase.GUI.Enchiridion.Pages.PATCH_0_3.toLanguageKey(), "Added: \n\u2022 Experience fluid\n\u2022 Experience Extractor\n\u2022 Lightning generator\nFixed:\n\u2022 Huge amount of bugs");

		add(NDatabase.GUI.Enchiridion.Chapters.PATCH_0_4.toLanguageKey(), "Patch 0.4");
		add(NDatabase.GUI.Enchiridion.Pages.PATCH_0_4.toLanguageKey(), "Added: \n\u2022 Platform Block\n\u2022 Bore Block\n\u2022 Some fixes and internal reworks");

		
		add(NDatabase.GUI.Enchiridion.Chapters.SKYSTONE.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.Names.SKYSTONE));
		add(NDatabase.GUI.Enchiridion.Pages.SKYSTONE.toLanguageKey(), "This is basic block required for next steps of crafting. As any another block, this may be divided into nuggets and back </recipe;" + StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + NRegistration.RegisterItems.NUGGET_SKYSTONE.getId().getPath() + "_to_" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath()) + "/> </recipe;" + StringHelper.getLocFStr(NDatabase.Recipes.VanillaTypes.CONVERSION + "/" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath() + "_to_" + NRegistration.RegisterItems.NUGGET_SKYSTONE.getId().getPath()) +"/> \nBut how it's can be obtained? Oh, that's pretty simple. Just place and stone block, like andesite, diorite, granite, regular stone and e.g in diffuser, fill it with 1 bucket of liquid experience and you will get your skystone</recipe;" + StringHelper.getLocFStr(NDatabase.Recipes.Types.DIFFUSER + "/" + NRegistration.RegisterBlocks.SKYSTONE.getId().getPath()) + "/>");
		
		add(NDatabase.GUI.Enchiridion.Chapters.TERRAMORFER.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.TERRAMORFER));
		add(NDatabase.GUI.Enchiridion.Pages.TERRAMORFER.toLanguageKey(), "First, hard to understand thing, with strange working mechanism. Seems like little black hole emit... Wait, what? Black hole and emit something? Oh, strange, but ok, so... This emit some kind of particles which morph ordinary stone to skystone. But, first, you need to craft those thing </recipe;" + NRegistration.RegisterBlocks.TERRAMORFER.getId()+"/> \nTo get starting crafting resources try to find any village and make some trades or you can find core somewhere in world depths.");
		
		add(NDatabase.GUI.Enchiridion.Chapters.CORIUM.toLanguageKey(), StringHelper.capitalize(NDatabase.Materials.CORIUM));
		add(NDatabase.GUI.Enchiridion.Pages.CORIUM.toLanguageKey(), "This is our nice looking metal. But you can not find this in world. Really, this is doesn't spawn. For getting it try place any iron ore at one block distance from terramorfer and wait a bit. Later this will be transformed in to corium. In all another ways Corium is pretty simple to any another metall. As material for instruments and armor it's a bit better then diamond");
		
		add(NDatabase.GUI.Enchiridion.Chapters.DIFFUSER.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.DIFFUSER));
		add(NDatabase.GUI.Enchiridion.Pages.DIFFUSER.toLanguageKey(), "Interesting thing, which allow you to fill items with liquids power. It's looks like cauldron, but have have much more difficult internal structure and working mechanism Can be crafted from Skystone </recipe;" + NRegistration.RegisterBlocks.DIFFUSER.getId() + "/> Hope you already crafted Terramorfer to get unlimited Skystone" );
		
		add(NDatabase.GUI.Enchiridion.Chapters.MANUAL_CRUSHER.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.MANUAL_CRUSHER).replace("_c", " C"));
		add(NDatabase.GUI.Enchiridion.Pages.MANUAL_CRUSHER.toLanguageKey(), "Om nom nom nom nom... Hart' t`fu! But, to be honest, it's pretty simple grinder, which can help you get much more resource from ore or raw pieces. If you looking for crafting recipe, so, it's here </recipe;" + NRegistration.RegisterBlocks.MANUAL_CRUSHER.getId() + "/>");
		
		add(NDatabase.GUI.Enchiridion.Chapters.GENERATION.toLanguageKey(), "Vim Generation");
		add(NDatabase.GUI.Enchiridion.Pages.GENERATORS.toLanguageKey(), "All devices can be supplied with vim. But, you need generate it for start. Here you can find some generators.\n\n    Solar Generator\n\n</recipe;" + NRegistration.RegisterBlocks.GENERATOR_SOLAR.getId() + "/> Just place it's under sky and enjoy results.\n     Food Generator\n   </recipe;" + NRegistration.RegisterBlocks.GENERATOR_FOOD.getId() + "/> You need really huge amount of food, so supply this generator. And yea, the better food, you supply for generator, the more energy you get\n\n      Mob Generator\n   </recipe;" + NRegistration.RegisterBlocks.GENERATOR_MOB.getId() + "/> This strange generator can eat nearly any mob. Cows, villagers, golems, zombies, raiders and much more. But you can get really huge amount of energy from this generator \n Lightning Generator \n Of course, lighting it's true source of energy. And now you can use even this a natural phenomenon for your own needs. </recipe;"+ NRegistration.RegisterBlocks.GENERATOR_LIGHTNING.getId() +"/>");
		
		add(NDatabase.GUI.Enchiridion.Chapters.RACK.toLanguageKey(), "Holders");
		add(NDatabase.GUI.Enchiridion.Pages.RACK.toLanguageKey(), "Those racks can help you deal with many different instruments. They working like shelf, but someone think they have another use... Maybe it's true, will see... </recipe;" + NRegistration.RegisterBlocks.PEDESTAL.getId() + "/> </recipe;" + NRegistration.RegisterBlocks.HOLDER.getId() + "/>");
		
		add(NDatabase.GUI.Enchiridion.Chapters.FLUID_STORAGE.toLanguageKey(), "Fluid Storage");
		add(NDatabase.GUI.Enchiridion.Pages.FLUID_STORAGE.toLanguageKey(), "If you need a barrel - you find it! Gratz! </recipe;" + NRegistration.RegisterBlocks.FLUID_STORAGE.getId() + "/>");
		
		add(NDatabase.GUI.Enchiridion.Chapters.EXP_EXTRACTOR.toLanguageKey(), "Experience Extractor");
		add(NDatabase.GUI.Enchiridion.Pages.EXP_EXTRACTOR.toLanguageKey(), "Only way to transfer your experience to liquid state. Warning, it's deal solid damage! Be care! </recipe;"+ NRegistration.RegisterBlocks.EXP_EXTRACTOR.getId() +"/>");
		
		add(NDatabase.GUI.Enchiridion.Chapters.FLUID_FILLER.toLanguageKey(), "Fluid Filler");
		add(NDatabase.GUI.Enchiridion.Pages.FLUID_FILLER.toLanguageKey(), "Find nearby liquids in world and fill them inside. WARNING! It's a hot thing, if has lava inside! </recipe;" + NRegistration.RegisterBlocks.FLUID_FILLER.getId() + "/>");
		
		add(NDatabase.GUI.Enchiridion.Chapters.FURNACE.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.FURNACE));
		add(NDatabase.GUI.Enchiridion.Pages.FURNACE.toLanguageKey(), "Just like typical furnace, but don't required any fuel. Oh, wait. I forgot, it's required energy. But, it's a big deal, yea? </recipe;"+ NRegistration.RegisterBlocks.FURNACE.getId() +"/>");
	
		add(NDatabase.GUI.Enchiridion.Chapters.CRUSHER.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.CRUSHER));
		add(NDatabase.GUI.Enchiridion.Pages.CRUSHER.toLanguageKey(), "Just like manual crusher, but your hand is finally free! Yea! Finally! </recipe;"+ NRegistration.RegisterBlocks.CRUSHER.getId() +"/>");
		
		add(NDatabase.GUI.Enchiridion.Chapters.HOOVER.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.HOOVER));
		add(NDatabase.GUI.Enchiridion.Pages.HOOVER.toLanguageKey(), "Help to keep your house clear. Don't forgot to clear this brilliant device </recipe;"+ NRegistration.RegisterBlocks.HOOVER.getId() +"/>");
		
		add(NDatabase.GUI.Enchiridion.Chapters.GROWTH_CRYSTAL.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.Crystal.GROWTH.replace("_c", " C")));
		add(NDatabase.GUI.Enchiridion.Pages.GROWTH_CRYSTAL.toLanguageKey(), "The best farmer helper. You wheat will growth MUCH more faster. And don't forget to supply it with energy. You even can place this magic crystal in liquid! </recipe;"+ StringHelper.getLocFStr(NDatabase.Recipes.Types.DIFFUSER + "/" + NRegistration.RegisterBlocks.CRYSTAL_GROWTH.getId().getPath()) +"/>");
		
		add(NDatabase.GUI.Enchiridion.Chapters.EXTRUDER.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.EXTRUDER));
		add(NDatabase.GUI.Enchiridion.Pages.EXTRUDER.toLanguageKey(), "Your personal cobblestone generator. And... A bit more then just cobblestone. But consume liquid for some recipes </recipe;"+ NRegistration.RegisterBlocks.EXTRUDER.getId() +"/>");

		add(NDatabase.GUI.Enchiridion.Chapters.DELIVERY.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION).replace("_s", " S"));
		add(NDatabase.GUI.Enchiridion.Pages.DELIVERY.toLanguageKey(), "The most complicated and strange device. Can transport items, liquid or vim for 32 blocks range, depending on choosed mode. </recipe;"+ NRegistration.RegisterBlocks.DELIVERY_STATION.getId() + "/> Even can filter, what this this thing will deliver. Extract choosed thing from nearby placed device and transport it to connected receptor. For connect receptor just use hammer on it, and after that use hammer again on Delivery Station");

		add(NDatabase.GUI.Enchiridion.Chapters.PLATFORM.toLanguageKey(), StringHelper.capitalize(NDatabase.Blocks.BlockEntities.Names.PLATFORM));
		add(NDatabase.GUI.Enchiridion.Pages.PLATFORM.toLanguageKey(), "It's just platform for blocks like Bore or Defensive Tower. They may be placed ONLY on platform, on top or bottom side. May be rotated with hammer. Bore mining result will expel from face of this block. Oh yea, almost forgot... Recipe... </recipe;" + NRegistration.RegisterBlocks.PLATFORM.getId() + "/>");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------
	}
	
	@Override
	public String getName() 
	{
		return "Nedaire EnUs Provider";
	}

}

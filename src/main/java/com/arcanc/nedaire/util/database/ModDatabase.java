/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.database;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.base.Preconditions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ModDatabase 
{
	public static final String MOD_ID = "nedaire";
	public static final String VERSION = "${version}";

	public static class ItemGroups
	{
		public static class Main
		{
			public static final String MAIN = "main";
		}
		public static final String BACKGROUND_IMAGE_PATH = "textures/gui/itemgroups/";
	}
	
	public static class Materials
	{
		public static final String CORIUM = "corium";
	}
	
	public static class Items
	{
		public static class Names
		{
			public static final String INGOT = "ingot";
			public static final String NUGGET = "nugget";
			public static final String DUST = "dust";
			public static final String RAW = "raw";
			public static final String DEEPSLATE = "deepslate";
			
			public static final String TOOL = "tool";
			public static final String WEAPON = "weapon";
			public static final String ARMOR = "armor";
			public static final String PLAYER_ARMOR = "player";
			
			public static final String HAMMER = "hammer";
			
			public static class Tools
			{
				public static final String AXE = "axe";
				
				public static final String PICKAXE = "pickaxe";
				public static final String SHOVEL = "shovel";
				public static final String HOE = "hoe";
				public static final String SHEARS = "shears";
				public static final String FISHING_ROD = "fishing_rod";
			}
			
			public static class Armor
			{
				public static final String ARMOR_HORSE = "horse";
				
				public static final String ARMOR_CHEST = "chest";
				public static final String ARMOR_LEGS = "legs";
				public static final String ARMOR_FEET = "feet";
				public static final String ARMOR_HEAD = "head";
			}
			
			public static class Weapon
			{
				public static final String SHIELD = "shield";
				public static final String BOW = "bow";
				public static final String CROSSBOW = "crossbow";
				public static final String SWORD = "sword";
			}
			
		}
	}

	public static class Blocks
	{
		public static class Names
		{
			public static final String STORAGE_BLOCK = "storage_block";
			public static final String ORE = "ore";
			public static final String DEEPSLATE = "deepslate";
			
			public static final String SKYSTONE = "skystone";
		}
		
		public static class BlockEntities
		{
			public static class Names
			{
				public static final String PEDESTAL = "pedestal";
				public static final String HOLDER = "holder";
			}
			
			public static class TagAddress
			{
				public static class Machines
				{
					public static class RedstoneSensitive
					{
						public static final String REDSTONE_MOD = "redstone_mod";
					}
				}
				
			}
			
		}
	}

	public static class Recipes
	{
		
		public static class VanillaTypes
		{
			public static final String SMELTING = "smelting";
			public static final String BLASTING = "blasting";
			public static final String CONVERSION = "conversion";
			public static final String TOOL = "tool";
			public static final String SHIELD_DECORATION = "shield_decoration";
		}
	}

	public static class Capabilities
	{
		public static class ItemHandler
		{
			public static final String SLOTS = "slots";
			public static final String TAG_LOCATION = StringHelper.getStrLocFStr("inventory");
		
			public static class ItemHolder
			{
				public static final String SLOT_LIMIT = "slot_limit";
			}
		}
		
		public static class Vim
		{
			public static final String TAG_LOCATION = StringHelper.getStrLocFStr("vim");
			
			public static final String ENERGY = "vim";
			public static final String MAX_ENERGY = "max_vim";
			public static final String INPUT = "input";
			public static final String EXTRACT = "extracting";
		}
		
		public static class Socket
		{
			public static final String TAG_LOCATION = StringHelper.getStrLocFStr("socket");
			
			public static final String COLOR = "color";
			public static final String NAME = "name";
			public static final String ID = "id";
			
			public static class Health
			{
				public static final String AMOUNT = "amount"; 
				
				public static final String NAME = "health";
			}
			
			public static class Regeneration
			{
				public static final String HEAL_POWER = "heal_power";
				public static final String PERIOD = "period";
				public static final String LAST_TICK = "last_tick";
				
				public static final String NAME = "regeneration";
			}
		}
	}

	public static class GUI
	{
		public static final String PATH = "gui/";
		public static class Slots
		{
			public static final String PATH = GUI.PATH + "slots/";
			public static class Textures
			{
				public static final String STANDART = Slots.PATH + "standart";
				public static final String INPUT = Slots.PATH + "input";
				public static final String OUTPUT = Slots.PATH + "output";
				public static final String BOTH = Slots.PATH + "both";
			}
		}
		public static class Enchiridion
		{
			public static class Recipes
			{
				/**
				 * Item - </item;"item ResourceLocation"/>
				 * Block - </block;"block ResourceLocation"/>
				 * Entity - </entity;"entity ResourceLocation"/>
				 * Tag - </tag;"tag type" (item, block or fluid);"tag name"/>
				 * Crafting Recipe - </recipe;"location"/>
				 */
				public static class Syntax
				{
				}
				
				public static class Translatable
				{
					public static final String TICKS = getEnchiridionPath("recipe", "ticks").toLanguageKey();
					public static final String EXPERIENCE = getEnchiridionPath( "recipe", "exp").toLanguageKey();
					public static final String SHAPED = getEnchiridionPath("recipe", "shaped").toLanguageKey();
					public static final String SHAPELESS = getEnchiridionPath("recipe", "shapeless").toLanguageKey();
				}
				
			}
			
			public static class Arrows
			{
				public static final String ARROW_LEFT = getEnchiridionPath("arrow", "left").toLanguageKey();
				public static final String ARROW_RIGHT = getEnchiridionPath("arrow", "right").toLanguageKey();
				public static final String ARROW_TO_START = getEnchiridionPath("arrow", "to_start").toLanguageKey();
			}
			public static final String ENCHIRIDION = "enchiridion";

			public static final String SECTION = "section";
			
			public static class Section 
			{
				public static final String ALCHEMY = "alchemy";
				public static final String JEWELRY = "jewelry";
				public static final String MAGIC = "magic";

				public static class ResourceLocations
				{
					public static final ResourceLocation ALCHEMY = getEnchiridionPath(SECTION, Section.ALCHEMY);
					public static final ResourceLocation JEWELRY = getEnchiridionPath(SECTION, Section.JEWELRY);
					public static final ResourceLocation MAGIC = getEnchiridionPath(SECTION, Section.MAGIC);
				}
				
				public static record SectionData (ResourceLocation name, ItemStack icon, Map<ResourceLocation, ResourceLocation> data)
				{
					
					public SectionData(@Nonnull ResourceLocation name, @Nonnull ItemStack icon, @Nonnull Map<ResourceLocation, ResourceLocation> data) 
					{
						this.name = name;
						this.icon = icon;
						this.data = data;
					}
					
					public SectionData(ResourceLocation name, ItemStack icon) 
					{
						this(name, icon, new HashMap<>());
					}
					
					public SectionData addChapter(ResourceLocation loc)
					{
						Preconditions.checkNotNull(loc);
						this.data.put(loc, null);
						return this;
					}
					
					public SectionData addChapters(ResourceLocation... locations)
					{
						Preconditions.checkNotNull(locations);
						Arrays.asList(locations).forEach(loc -> 
						{
							Preconditions.checkNotNull(loc);
							this.data.put(loc, null);
						});
						return this;
					}
					
					public SectionData addPage(ResourceLocation chapter, ResourceLocation page)
					{
						Preconditions.checkNotNull(chapter);
						Preconditions.checkNotNull(page);
						
						this.data.putIfAbsent(chapter, page);
						
						return this;
					}
				}
				
				public static final List<SectionData> SECTIONS = new LinkedList<>(
						Arrays.asList(
								new SectionData (Section.ResourceLocations.ALCHEMY,
									new ItemStack (net.minecraft.world.level.block.Blocks.CAULDRON)).
										addPage(Chapters.test, Pages.test).
										addPage(Chapters.test2, Pages.test2).
										addPage(Chapters.test3, Pages.test3).
										addPage(Chapters.test4, Pages.test4).
										addPage(Chapters.test5, Pages.test5).
										addPage(Chapters.test6, Pages.test6).
										addPage(Chapters.test7, Pages.test7).
										addPage(Chapters.test8, Pages.test8),
								new SectionData (Section.ResourceLocations.JEWELRY,
									new ItemStack (net.minecraft.world.item.Items.DIAMOND)).
										addPage(Chapters.test9, Pages.test9).
										addPage(Chapters.test10, Pages.test10).
										addPage(Chapters.test11, Pages.test11).
										addPage(Chapters.test12, Pages.test12).
										addPage(Chapters.test13, Pages.test13),
								new SectionData (Section.ResourceLocations.MAGIC,
									new ItemStack (net.minecraft.world.item.Items.ENDER_PEARL)).
								addPage(Chapters.test14, Pages.test14).
								addPage(Chapters.test15, Pages.test15).
								addPage(Chapters.test16, Pages.test16)
						));
			}
			public static final String CHAPTER = "chapter";
			public static class Chapters
			{
				public static final ResourceLocation test = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test"));
				public static final ResourceLocation test2 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test2"));
				public static final ResourceLocation test3 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test3"));
				public static final ResourceLocation test4 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test4"));
				public static final ResourceLocation test5 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test5"));
				public static final ResourceLocation test6 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test6"));
				public static final ResourceLocation test7 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test7"));
				public static final ResourceLocation test8 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test8"));
				public static final ResourceLocation test9 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test9"));
				public static final ResourceLocation test10 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test10"));
				public static final ResourceLocation test11 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test11"));
				public static final ResourceLocation test12 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test12"));
				public static final ResourceLocation test13 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test13"));
				public static final ResourceLocation test14 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test14"));
				public static final ResourceLocation test15 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test15"));
				public static final ResourceLocation test16 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION, 
						CHAPTER,
						"test16"));
			}
			public static final String PAGE = "page";
			public static class Pages 
			{
				public static final ResourceLocation test = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test"));
				public static final ResourceLocation test2 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test2"));
				public static final ResourceLocation test3 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test3"));
				public static final ResourceLocation test4 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test4"));
				public static final ResourceLocation test5 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test5"));
				public static final ResourceLocation test6 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test6"));
				public static final ResourceLocation test7 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test7"));
				public static final ResourceLocation test8 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test8"));
				public static final ResourceLocation test9 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test9"));
				public static final ResourceLocation test10 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test10"));
				public static final ResourceLocation test11 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test11"));
				public static final ResourceLocation test12 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test12"));
				public static final ResourceLocation test13 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test13"));
				public static final ResourceLocation test14 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test14"));
				public static final ResourceLocation test15 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test15"));
				public static final ResourceLocation test16 = StringHelper.getLocFStr(StringHelper.symbolPlacer('.', 
						ENCHIRIDION,
						PAGE,
						"test16"));
			}
			
			private static ResourceLocation getEnchiridionPath (String part, String path)
			{
				return StringHelper.getLocFStr(StringHelper.symbolPlacer('.', ENCHIRIDION, part, path));  
			}
		}

	}
}
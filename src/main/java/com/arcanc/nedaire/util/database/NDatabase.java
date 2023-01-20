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

public class NDatabase 
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
			public static final String CHALK = "chalk";
			
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
				public static final String MANUAL_CRUSHER = "manual_crusher";
				public static final String VIM_STORAGE = "vim_storage";
				public static final String DELIVERY_STATION = "delivery_station";
				public static final String HOOVER = "hoover";
			}
			
			public static class TagAddress
			{
				public static class Machines
				{
					public static class RedstoneSensitive
					{
						public static final String REDSTONE_MOD = "redstone_mod";
					}
					
					public static class Manual_Crusher
					{
						public static final String LAST_ACTIVE_TIME = "last_active_time";
						public static final String CURRENT_ANGLE = "current_angle";
						public static final String USED_ENERGY = "used_energy";
					}
				}
			}
			
		}
	}

	public static class Recipes
	{
		public static class StackWithChanceNBT
		{
			public static final String CHANCE = "chance";
			public static final String STACK = "stack";
		}
		
		public static class IngredientWithSizeNBT
		{
			public static final String COUNT_KEY = "count";
			public static final String BASE_KEY = "base_ingredient";
		}
		
		public static class Types
		{
			public static final String CRUSHER = "crusher";
			public static class Crusher
			{
				public static final String RESULT = "result";
				public static final String INPUT = "input";
				public static final String SECONDARIES = "secondaries";
				public static final String ENERGY = "energy";
				public static final String IS_MANUAL = "is_manual";
			}
		}
		public static class VanillaTypes
		{
			public static final String SMELTING = "smelting";
			public static final String BLASTING = "blasting";
			public static final String CONVERSION = "conversion";
			public static final String TOOL = "tool";
			public static final String SHIELD_DECORATION = "shield_decoration";
		}
	}

	public static class Entities
	{
		public static final String DEFAULT_TEXTURE_PATH = "textures/entity";
		public static class Names
		{
			public static final String DELIVERY_DRONE = "delivery_drone";
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
			public static class Lang 
			{
				public static final String DESCRIPTION_MAIN = TAG_LOCATION.replace(':', '.').replace('/', '.') + ".description";
			
			}
		}
		
		public static class FluidHandler
		{
			public static final String TAG_LOCATION = StringHelper.getStrLocFStr("fluid");
			public static class Lang
			{
				public static final String DESCRIPTION_MAIN = TAG_LOCATION.replace(':', '.').replace('/', '.') + ".description";
			}
		}
		
		public static class Filter
		{
			public static final String TAG_LOCATION = StringHelper.getStrLocFStr("filter");
			public static final String TAG_LOCATION_ITEM = TAG_LOCATION + "_item";
			public static final String TAG_LOCATION_FLUID = TAG_LOCATION + "_fluid";
			public static final String TAG_LOCATION_VIM = TAG_LOCATION + "_vim";
			
			public static final String SIZE = "size";
			public static final String CONTENT = "content";
			public static final String WHITELIST = "whitelist";
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
		public static class Background
		{
			public static final String PATH = GUI.PATH + "background/";
			public static final class Textures
			{
				public static final String MIDDLE = Background.PATH + "middle";

				public static final String MIDDLE_LEFT = MIDDLE + "_left"; 
				public static final String MIDDLE_RIGHT = MIDDLE + "_right"; 
				public static final String MIDDLE_TOP = MIDDLE + "_top"; 
				public static final String MIDDLE_BOTTOM = MIDDLE + "_bottom"; 

				public static final String LEFT_TOP = Background.PATH + "corner_left_top";
				public static final String LEFT_BOTTOM = Background.PATH + "corner_left_bottom";
				public static final String RIGHT_TOP = Background.PATH + "corner_right_top";
				public static final String RIGHT_BOTTOM = Background.PATH + "corner_right_bottom";
			}
		}
		
		public static class Elements
		{
			public static final String PATH = GUI.PATH + "elements/";
			public static class Vim
			{
				public static final String PATH = GUI.Elements.PATH + "vim/vim";
			}
			public static class FluidHandler
			{
				public static final String PATH = GUI.Elements.PATH + "fluid/fluid";
			}
			public static class RadioButton
			{
				public static final String PATH = GUI.Elements.PATH + "radiobutton/radiobutton";
			}
		}
		
		public static class Filter
		{
			public static final String PATH = GUI.PATH + "filter/filter";
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
									new ItemStack (net.minecraft.world.level.block.Blocks.CAULDRON)),
//										addPage(Chapters.test, Pages.test).
								new SectionData (Section.ResourceLocations.JEWELRY,
									new ItemStack (net.minecraft.world.item.Items.DIAMOND)),
								new SectionData (Section.ResourceLocations.MAGIC,
									new ItemStack (net.minecraft.world.item.Items.ENDER_PEARL))
						));
			}
			public static final String CHAPTER = "chapter";
			public static class Chapters
			{
			}
			public static final String PAGE = "page";
			public static class Pages 
			{
			}
			
			private static ResourceLocation getEnchiridionPath (String part, String path)
			{
				return StringHelper.getLocFStr(StringHelper.symbolPlacer('.', ENCHIRIDION, part, path));  
			}
		}
	
		public static String getTexturePath(String str)
		{
			return "textures/" + str + ".png";
		}
	}
}
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

import com.arcanc.nedaire.content.container.widget.icon.Icon;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.base.Preconditions;

import net.minecraft.resources.ResourceLocation;

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
			
			public static final String CRYSTAL_PRISON = "crystal_prison";
			
			public static final String CHALK = "chalk";
			public static final String JEWELRY = "jewelry";
			
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
			public static final String FRAMEWORK = "framework";
		}
		
		public static class BlockEntities
		{
			public static class Names
			{
				public static final String GENERATOR = "generator";
				
				public static class Generators
				{
					public static final String SOLAR = GENERATOR + "/solar";
					public static final String FOOD = GENERATOR + "/food";
					public static final String MOB = GENERATOR + "/mob";
				}
				
				public static final String FURNACE = "furnace";
				public static final String CRUSHER = "crusher";
				public static final String TERRAMORFER = "terramorfer";
				public static final String PEDESTAL = "pedestal";
				public static final String HOLDER = "holder";
				public static final String MANUAL_CRUSHER = "manual_" + CRUSHER;
				public static final String VIM_STORAGE = "vim_storage";
				public static final String FLUID_STORAGE = "fluid_storage";
				public static final String DELIVERY_STATION = "delivery_station";
				public static final String HOOVER = "hoover";
				public static final String MOB_CATCHER = "mob_catcher";
				public static final String JEWELRY_TABLE = "jewelry_table";
				public static final String EXTRUDER = "extruder";
				public static final String FLUID_FILLER = "fluid_filler";
				public static final String DIFFUSER = "diffuser";
				
				public static class Crystal
				{
					private static final String CRYSTAL = "crystal";
					public static final String GROWTH = "growth" + "_" + CRYSTAL; 
				}
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
					public static class Terramorfer
					{
						public static final String PLACED_TIME = "placed_time";
					}
					public static class DeliveryStation
					{
						public static final String ATTACHED_POSES = "poses";
						public static final String MODE = "delivery_mode";
						public static final String PATH = "path";
					}
					public static class MobCatcher
					{
						public static final String USED_ENERGY = "used_energy";
						public static final String WORKING = "working";
						public static final String MOB_STACK = "mob_stack";
					}
					public static class GeneratorMob
					{
						public static final String REMAINING_ENERGY = "remaining_energy";
						public static final String ENTITY_TYPE = "entity_type";
					}
					public static class Crusher
					{
						public static final String USED_ENERGY = "used_energy";
						public static final String CURRENT_RECIPE = "rec";
					}
					
					public static class Furnace
					{
						public static final String USED_ENERGY = "used_energy";
					}
					
					public static class Extruder
					{
						public static final String MODE = "mode";
					}
					
					public static class Diffuser
					{
						public static final String ELAPSED_TIME = "elapsed_time";
						public static final String DRAINED_FLUID = "drained_fluid";
					}
				}
			}
			
		}
	}

	public static class Fluids
	{
		public static class Names
		{
			public static final String EXPERIENCE = "experience";
		}
		
		public static ResourceLocation getStillLoc(String name)
		{
			return StringHelper.getLocFStr("block/fluids/" + name +"/still");
		}
		
		public static ResourceLocation getFlowLoc(String name)
		{
			return StringHelper.getLocFStr("block/fluids/" + name +"/flow");
		}
		
		public static ResourceLocation getOverlayLoc(String name)
		{
			return StringHelper.getLocFStr("block/fluids/" + name + "/overlay");
		}
		
		public static ResourceLocation getBlockLocation(String name)
		{
			return StringHelper.getLocFStr("block/fluids/" + name + "/block");
		}
		
		public static ResourceLocation getBucketLocation(String name)
		{
			return StringHelper.getLocFStr("fluids/" + name + "/bucket");
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
			public static final String DIFFUSER = "diffuser";
			public static class Diffuser
			{
				public static final String RESULT = "result";
				public static final String INPUT = "input";
				public static final String FLUID = "fluid";
				public static final String TIME = "time";
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
			public static final String CRYSTAL_PRISON = Items.Names.CRYSTAL_PRISON;
		}
	}
	
	public static class Capabilities
	{
		public static final String SIDED_ACCESS = StringHelper.getStrLocFStr("sided_access");
		
		public static class ItemHandler
		{
			public static final String SLOTS = "slots";
			public static final String TAG_LOCATION = StringHelper.getStrLocFStr("inventory");
		
			public static class ItemHolder
			{
				public static final String SLOT_LIMIT = "slot_limit";
			}
		
			public static class ManagedInventory
			{
				public static final String DIVIDER_INDEX = "divider";
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
			public static final String TANKS = "tanks";
			public static class FluidHolder
			{
				public static final String SLOT_LIMIT = "slot_limit";
			}
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
			
			public static final String WHITELIST = "whitelist";
			public static final String MAX_EXTRACTING_STACK = "max_extracting_stack";
			public static final String MAX_AMOUNT_IN = "max_amount_in";
			public static final String MOD_OWNER = "mod_owner";
			public static final String CHECK_TAG = "check_tag";
			
			public static final String CONTENT = "content";
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
				public static final String BACKGROUND = GUI.Elements.PATH + "radiobutton/radiobutton_background";
			}
			public static class ProgressBar
			{
				public static final String PATH = GUI.Elements.PATH + "progress_bar/";
				public static final String ARROW_RIGHT = PATH + "arrow_right";
				public static class Description
				{
					public static final String PERCENT = PATH.replace(':', '.').replace('/', '.') + "percent";
					public static final String PERCENT_FULL = PATH.replace(':', '.').replace('/', '.') + "percent_full";
				}
			}
			public static class EnchElementDiffuserRecipe
			{
				public static final String PATH = GUI.Elements.PATH + "diffuser_recipe";
				public static final String AMOUNT = PATH.replace(':', '.').replace('/', '.') + "amount";
			}
			public static class DropPanel
			{
				public static final String PATH = GUI.PATH + "drop_panel/";
				public static class RedstoneSensitivePanel
				{
					public static final String PATH = DropPanel.PATH + "redstone_sensitive";
					
					public static final String DESCRIPTION_NAME = PATH.replace(':', '.').replace('/', '.') + ".description";
					public static final String DESCRIPTION_CONTROL_MODE = PATH.replace(':', '.').replace('/', '.') + ".control_mode";
					public static final String DESCRIPTION_CONTROL_MODE_DISABLED = DESCRIPTION_CONTROL_MODE + ".disabled";
					public static final String DESCRIPTION_CONTROL_MODE_ENABLED = DESCRIPTION_CONTROL_MODE + ".enabled";
					
					public static final String DESCRIPTION_REQUIRED_SIGNAL_NAME = PATH.replace(':', '.').replace('/', '.') + ".required_signal";
					public static final String DESCRIPTION_REQUIRED_SIGNAL_LOW = DESCRIPTION_REQUIRED_SIGNAL_NAME + ".low";
					public static final String DESCRIPTION_REQUIRED_SIGNAL_HIGHT = DESCRIPTION_REQUIRED_SIGNAL_NAME + ".hight";
					public static final String DESCRIPTION_REQUIRED_SIGNAL_DISABLED = DESCRIPTION_REQUIRED_SIGNAL_NAME + ".disabled";
				}
				public static class PanelSwitcherPanel 
				{
					public static final String PATH = DropPanel.PATH + "panel_swither";
					
					public static final String DESCRIPTION_NAME = PATH.replace(':','.').replace('/', '.') + ".description";
					public static final String DESCRIPTION_MAIN = PATH.replace(':','.').replace('/', '.') + ".main";
					public static final String DESCRIPTION_FILTER_ITEM = PATH.replace(':','.').replace('/', '.') + ".filter_item";
					public static final String DESCRIPTION_FILTER_FLUID = PATH.replace(':','.').replace('/', '.') + ".filter_fluid";
					public static final String DESCRIPTION_FILTER_VIM = PATH.replace(':','.').replace('/', '.') + ".filter_vim";
				}
			}
		}
		
		public static class BlockEntities
		{
			public static final String PATH = GUI.PATH + "block_entities/";
			public static class DeliveryStation
			{
				public static final String PATH = GUI.BlockEntities.PATH + "delivery_station/";
				public static final String MODE = PATH + "mode/";
				public static class Description
				{
					public static final String MODE_ITEMS = MODE.replace(':','.').replace('/', '.') + "items";
					public static final String MODE_FLUIDS = MODE.replace(':','.').replace('/', '.') + "fluids";
					public static final String MODE_VIM = MODE.replace(':','.').replace('/', '.') + "vim";
				}
			}
			
			public static class Extruder
			{
				public static final String PATH = GUI.BlockEntities.PATH + "extruder/";
				public static final String MODE = PATH +"mode/";
				public static class Description
				{
					public static final String MODE_COBBLE = MODE.replace(':','.').replace('/', '.') + "cobble";
					public static final String MODE_STONE = MODE.replace(':','.').replace('/', '.') + "stone";
					public static final String MODE_OBSIDIAN = MODE.replace(':','.').replace('/', '.') + "obsidian";
				}
			}
		}
		
		public static class Filter
		{
			public static final String PATH = GUI.PATH + "filter/";
			
			public static final String WHITELIST = PATH + "whitelist";
			public static final String TAG = PATH + "tag";
			public static final String MOD_OWNER = PATH + "mod_owner";

			public static final String ICON_FILTER_ITEM = PATH + "filter_item";
			public static final String ICON_FILTER_FLUID = PATH + "fliter_fluid";
			public static final String ICON_FILTER_VIM = PATH + "filter_vim";
			
			public static class Description
			{
				public static final String WHITELIST = Filter.WHITELIST.replace('/', '.').replace(':', '.');
				public static final String BLACKLIST = Filter.PATH.replace('/', '.').replace(':', '.') + "blackList";
				
				public static final String TAG_USE = Filter.TAG.replace('/', '.').replace(':', '.');
				public static final String TAG_IGNORE = Filter.TAG.replace('/', '.').replace(':', '.') + "_ignore";
				
				public static final String MOD_OWNER = Filter.MOD_OWNER.replace('/', '.').replace(':', '.');
				public static final String MOD_OWNER_IGNORE = Filter.MOD_OWNER.replace('/', '.').replace(':', '.') + "_ignore";

				public static final String EXTRACTING_STACK_INCREASE = Filter.PATH.replace('/', '.').replace(':', '.') + "extracting_stack_increase";
				public static final String EXTRACTING_STACK_DECREASE = Filter.PATH.replace('/', '.').replace(':', '.') + "extracting_stack_decrease";

				public static final String AMOUNT_IN_INCREASE = Filter.PATH.replace('/', '.').replace(':', '.') + "amount_in_increase";
				public static final String AMOUNT_IN_DECREASE = Filter.PATH.replace('/', '.').replace(':', '.') + "amount_in_decrease";
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
				public static final String PATCH_NOTES = "patch_notes";
				public static final String BASIC = "basic";
				public static final String ADVANCED = "advanced";

				public static class ResourceLocations
				{
					public static final ResourceLocation PATCH_NOTES = getEnchiridionPath(SECTION, Section.PATCH_NOTES);
					public static final ResourceLocation BASIC = getEnchiridionPath(SECTION, Section.BASIC);
					public static final ResourceLocation ADVANCED = getEnchiridionPath(SECTION, Section.ADVANCED);
				}
				
				public static record SectionData (ResourceLocation name, Icon<?> icon, Map<ResourceLocation, ResourceLocation> data)
				{
					
					public SectionData(@Nonnull ResourceLocation name, @Nonnull Icon<?> icon, @Nonnull Map<ResourceLocation, ResourceLocation> data) 
					{
						this.name = name;
						this.icon = icon;
						this.data = data;
					}
					
					public SectionData(ResourceLocation name, Icon<?> icon) 
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
								new SectionData (Section.ResourceLocations.PATCH_NOTES,
									Icon.of(net.minecraft.world.item.Items.WRITABLE_BOOK)).
										addPage(Chapters.CHANGELOG, Pages.PATCH_0_1),
								new SectionData (Section.ResourceLocations.BASIC,
									Icon.of(getTexturePath("gui/enchiridion/" + BASIC), 5, 0, 0, 16, 16, 16, 16)).
										addPage(Chapters.SKYSTONE, Pages.SKYSTONE).
										addPage(Chapters.TERRAMORFER, Pages.TERRAMORFER).
										addPage(Chapters.CORIUM, Pages.CORIUM).
										addPage(Chapters.DIFFUSER, Pages.DIFFUSER).
										addPage(Chapters.MANUAL_CRUSHER, Pages.MANUAL_CRUSHER).
										addPage(Chapters.GENERATION, Pages.GENERATORS).
										addPage(Chapters.FLUID_STORAGE, Pages.FLUID_STORAGE).
										addPage(Chapters.RACK, Pages.RACK),
								new SectionData (Section.ResourceLocations.ADVANCED,
									Icon.of(getTexturePath("gui/enchiridion/" + ADVANCED), 5, 0, 0, 16, 16, 16, 16)).
										addPage(Chapters.FLUID_FILLER, Pages.FLUID_FILLER).
										addPage(Chapters.FURNACE, Pages.FURNACE).
										addPage(Chapters.CRUSHER, Pages.CRUSHER).
										addPage(Chapters.HOOVER, Pages.HOOVER).
										addPage(Chapters.GROWTH_CRYSTAL, Pages.GROWTH_CRYSTAL).
										addPage(Chapters.EXTRUDER, Pages.EXTRUDER).
										addPage(Chapters.DELIVERY, Pages.DELIVERY)
						));
			}
			public static final String CHAPTER = "chapter";
			public static class Chapters
			{
				//PatchNotes
				public static final ResourceLocation CHANGELOG = getEnchiridionPath(getChapt(Section.PATCH_NOTES), "changelog");
				//Basic
				public static final ResourceLocation SKYSTONE = getEnchiridionPath(getChapt(Section.BASIC), Blocks.Names.SKYSTONE);
				public static final ResourceLocation TERRAMORFER = getEnchiridionPath(getChapt(Section.BASIC), Blocks.BlockEntities.Names.TERRAMORFER);
				public static final ResourceLocation CORIUM = getEnchiridionPath(getChapt(Section.BASIC), Materials.CORIUM);
				public static final ResourceLocation DIFFUSER = getEnchiridionPath(getChapt(Section.BASIC), Blocks.BlockEntities.Names.DIFFUSER);
				public static final ResourceLocation MANUAL_CRUSHER = getEnchiridionPath(getChapt(Section.BASIC), Blocks.BlockEntities.Names.MANUAL_CRUSHER);
				public static final ResourceLocation GENERATION = getEnchiridionPath(getChapt(Section.BASIC), "vim_generation");
				public static final ResourceLocation FLUID_STORAGE = getEnchiridionPath(getChapt(Section.BASIC), Blocks.BlockEntities.Names.FLUID_STORAGE);
				public static final ResourceLocation RACK = getEnchiridionPath(getChapt(Section.BASIC), "racks");
				//Advanced
				public static final ResourceLocation FLUID_FILLER = getEnchiridionPath(getChapt(Section.ADVANCED), Blocks.BlockEntities.Names.FLUID_FILLER); 
				public static final ResourceLocation FURNACE = getEnchiridionPath(getChapt(Section.ADVANCED), Blocks.BlockEntities.Names.FURNACE); 
				public static final ResourceLocation CRUSHER = getEnchiridionPath(getChapt(Section.ADVANCED), Blocks.BlockEntities.Names.CRUSHER); 
				public static final ResourceLocation HOOVER = getEnchiridionPath(getChapt(Section.ADVANCED), Blocks.BlockEntities.Names.HOOVER); 
				public static final ResourceLocation GROWTH_CRYSTAL = getEnchiridionPath(getChapt(Section.ADVANCED), Blocks.BlockEntities.Names.Crystal.GROWTH); 
				public static final ResourceLocation EXTRUDER = getEnchiridionPath(getChapt(Section.ADVANCED), Blocks.BlockEntities.Names.EXTRUDER); 
				public static final ResourceLocation DELIVERY = getEnchiridionPath(getChapt(Section.ADVANCED), Blocks.BlockEntities.Names.DELIVERY_STATION); 
				
				private static String getChapt(String name)
				{
					return CHAPTER + "." + name;
				}
			}
			public static final String PAGE = "page";
			public static class Pages 
			{
				//patch notes
				public static final ResourceLocation PATCH_0_1 = StringHelper.getLocFStr(getPage(Chapters.CHANGELOG, "0.1"));
				//basic
				public static final ResourceLocation SKYSTONE = StringHelper.getLocFStr(getPage(Chapters.SKYSTONE));
				public static final ResourceLocation TERRAMORFER = StringHelper.getLocFStr(getPage(Chapters.TERRAMORFER));
				public static final ResourceLocation CORIUM = StringHelper.getLocFStr(getPage(Chapters.CORIUM));
				public static final ResourceLocation DIFFUSER = StringHelper.getLocFStr(getPage(Chapters.DIFFUSER));
				public static final ResourceLocation MANUAL_CRUSHER = StringHelper.getLocFStr(getPage(Chapters.MANUAL_CRUSHER));
				public static final ResourceLocation GENERATORS = StringHelper.getLocFStr(getPage(Chapters.GENERATION));
				public static final ResourceLocation RACK = StringHelper.getLocFStr(getPage(Chapters.RACK));
				public static final ResourceLocation FLUID_STORAGE = StringHelper.getLocFStr(getPage(Chapters.FLUID_STORAGE));
				//advanced
				public static final ResourceLocation FLUID_FILLER = StringHelper.getLocFStr(getPage(Chapters.FLUID_FILLER)); 
				public static final ResourceLocation FURNACE = StringHelper.getLocFStr(getPage(Chapters.FURNACE)); 
				public static final ResourceLocation CRUSHER = StringHelper.getLocFStr(getPage(Chapters.CRUSHER)); 
				public static final ResourceLocation HOOVER = StringHelper.getLocFStr(getPage(Chapters.HOOVER)); 
				public static final ResourceLocation GROWTH_CRYSTAL = StringHelper.getLocFStr(getPage(Chapters.GROWTH_CRYSTAL)); 
				public static final ResourceLocation EXTRUDER = StringHelper.getLocFStr(getPage(Chapters.EXTRUDER)); 
				public static final ResourceLocation DELIVERY = StringHelper.getLocFStr(getPage(Chapters.DELIVERY));
				
				private static String getPage(ResourceLocation chapter)
				{
					return getPage(chapter, "description");
				}
				
				private static String getPage(ResourceLocation chapter, String name)
				{
					return chapter.getPath() + "." + PAGE + "." + name;
				}
		}
			
			private static ResourceLocation getEnchiridionPath (String part, String path)
			{
				return StringHelper.getLocFStr(StringHelper.symbolPlacer('.', ENCHIRIDION, part, path));  
			}
		}
	
		public static ResourceLocation getTexturePath(String str)
		{
			return StringHelper.getLocFStr("textures/" + str + ".png");
		}
	}

	public static class Village
	{
		public static class Villagers
		{
			public static final String UNDERGROUNDER = "undergrounder";
			
			public static class Poi_Types
			{
				public static final String UNDERGROUNDER_POI = "poi_" + UNDERGROUNDER;
			}
		}
	}
}
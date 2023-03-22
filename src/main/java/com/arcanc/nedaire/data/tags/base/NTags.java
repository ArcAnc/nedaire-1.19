/**
 * @author ArcAnc
 * Created at: 2022-04-01
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.tags.base;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class NTags 
{
	public static class Blocks 
	{
		public static final MaterialTag CORIUM = new NTags.Blocks.MaterialTag(NDatabase.Materials.CORIUM);
		
		public static final Map<String, NTags.Blocks.MaterialTag> MATERIALS = Stream.of(CORIUM).collect(Collectors.toMap(NTags.Blocks.MaterialTag :: getName, mat -> mat));
		
		public static class MaterialTag
		{
			private final String name;
			
			private final TagKey<Block> storageBlock;
			private final TagKey<Block> ore;
		
			public MaterialTag(String name) 
			{
				this.name = name;
				
				this.storageBlock = forgeTag(NDatabase.Blocks.Names.STORAGE_BLOCK + "s/" + name);
				this.ore = forgeTag(NDatabase.Blocks.Names.ORE + "s/" + name);
			}
			
			public String getName() 
			{
				return name;
			}
			
			public TagKey<Block> getStorageBlock() 
			{
				return storageBlock;
			}
			
			public TagKey<Block> getOre() 
			{
				return ore;
			}
		}
		
		@SuppressWarnings("unused")
		private static TagKey<Block> tag (String name)
		{
			return BlockTags.create(StringHelper.getLocFStr(name));
		}
		
		private static TagKey<Block> forgeTag(String name)
		{
			return BlockTags.create(new ResourceLocation("forge", name));
		}
		
		@SuppressWarnings("unused")
		private static TagKey<Block> minecraftTag(String name)
		{
			return BlockTags.create(new ResourceLocation("minecraft", name));
		}
	
	}
	
	public static class Items
	{
	
		public static final MaterialTag CORIUM = new NTags.Items.MaterialTag(NDatabase.Materials.CORIUM);
		
		public static final Map<String, NTags.Items.MaterialTag> MATERIALS = Stream.of(CORIUM).collect(Collectors.toMap(NTags.Items.MaterialTag :: getName, mat -> mat));
		
		public static class MaterialTag
		{
			private final String name;
			
			private final TagKey<Item> ingot;
			private final TagKey<Item> nugget;
			private final TagKey<Item> dust;
			private final TagKey<Item> raw;
			
			private final TagKey<Item> storageBlock;
			private final TagKey<Item> ore;
		
			public MaterialTag(String name) 
			{
				this.name = name;
				
				this.ingot = forgeTag(NDatabase.Items.Names.INGOT + "s/" + name);
				this.nugget = forgeTag(NDatabase.Items.Names.NUGGET + "s/" + name);
				this.dust = forgeTag(NDatabase.Items.Names.DUST + "s/" + name);
				this.raw = forgeTag(NDatabase.Items.Names.RAW + "_materials/" + name);

				this.storageBlock = forgeTag(NDatabase.Blocks.Names.STORAGE_BLOCK + "s/" + name);
				this.ore = forgeTag(NDatabase.Blocks.Names.ORE + "s/" + name);
			}
			
			public String getName() 
			{
				return name;
			}
			
			public TagKey<Item> getIngot() 
			{
				return ingot;
			}
			
			public TagKey<Item> getDust() 
			{
				return dust;
			}
			
			public TagKey<Item> getNugget() 
			{
				return nugget;
			}
			
			public TagKey<Item> getRaw() 
			{
				return raw;
			}
			
			public TagKey<Item> getStorageBlock() 
			{
				return storageBlock;
			}
			
			public TagKey<Item> getOre() 
			{
				return ore;
			}
		}

		public static final TagKey<Item> WRENCH = forgeTag("wrenches"); 
		
		
		@SuppressWarnings("unused")
		private static TagKey<Item> tag(String name)
	    {
	        return ItemTags.create(StringHelper.getLocFStr(name));
	    }
		
		private static TagKey<Item> forgeTag(String name)
		{
			return ItemTags.create(new ResourceLocation("forge", name));
		}
		
		@SuppressWarnings("unused")
		private static TagKey<Item> minecraftTag(String name)
		{
			return ItemTags.create(new ResourceLocation("minecraft", name));
		}
	}

	public static class Fluids
	{
		public static final TagKey<Fluid> EXPERIENCE = forgeTag(NDatabase.Fluids.Names.EXPERIENCE);
		
		@SuppressWarnings("unused")
		private static TagKey<Fluid> tag(String name)
	    {
	        return FluidTags.create(StringHelper.getLocFStr(name));
	    }
		
		private static TagKey<Fluid> forgeTag(String name)
		{
			return FluidTags.create(new ResourceLocation("forge", name));
		}
		
		@SuppressWarnings("unused")
		private static TagKey<Fluid> minecraftTag(String name)
		{
			return FluidTags.create(new ResourceLocation("minecraft", name));
		}

	}
	
}

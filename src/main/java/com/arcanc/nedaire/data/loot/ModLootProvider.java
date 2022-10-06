/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.loot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import com.arcanc.nedaire.Nedaire;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public abstract class ModLootProvider implements DataProvider 
{

//	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(); 
	private final DataGenerator generator;
	protected final Map<ResourceLocation, LootTable> tables = Maps.<ResourceLocation, LootTable>newHashMap();
	
	protected abstract void registerTables();
	
	public ModLootProvider(DataGenerator gen) 
	{
		this.generator = gen;
	}
	
	@Override
	public void run(CachedOutput cache) throws IOException 
	{
		tables.clear();
		Path outFolder = this.generator.getOutputFolder();

		registerTables();

		ValidationContext validationtracker = new ValidationContext(LootContextParamSets.EMPTY, 
	    															(loc) -> null,
	    															tables::get);
	      
		tables.forEach((name, table) -> 
		{
			LootTables.validate(validationtracker, name, table);
		});
		
		Multimap<String, String> problems = validationtracker.getProblems();
		
		if(!problems.isEmpty())
		{
			problems.forEach((name, table) -> 
			{
				Nedaire.getLogger().warn("Found validation problem in " + name + ": "+ table);
			});
			throw new IllegalStateException("Failed to validate loot tables, see logs");
		}
		else
		{
			tables.forEach((name, table) -> 
			{
				Path out = getPath(outFolder, name);

				try
				{
					DataProvider.saveStable(cache, LootTables.serialize(table), out);
				} 
				catch(IOException x)
				{
					Nedaire.getLogger().error("Couldn't save loot table {}", out, x);
				}

			});
		}
	}

	private Path getPath(Path path, ResourceLocation loc)
	{
		return path.resolve("data/" + loc.getNamespace() + "/loot_tables/" + loc.getPath() + ".json");
	}

	@Override
	public String getName() 
	{
		return "Nedaire Loot Provider";
	}

}

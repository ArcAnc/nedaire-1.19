/**
 * @author ArcAnc
 * Created at: 2023-01-03
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.loot;

import java.util.Collections;
import java.util.List;

import com.arcanc.nedaire.data.NBlockLootSubProvider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class NLootProvider 
{
	public static LootTableProvider create(PackOutput out)
	{
		return new LootTableProvider(out,
				Collections.emptySet(),
				List.of(
						new LootTableProvider.SubProviderEntry(NBlockLootSubProvider :: new, LootContextParamSets.BLOCK)
						));
	}
}

/**
 * @author ArcAnc
 * Created at: 2023-03-20
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import java.util.function.Supplier;

import com.arcanc.nedaire.util.helpers.BlockHelper;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class NBlockStairs extends StairBlock 
{

	public NBlockStairs(Supplier<BlockState> state, Properties properties) 
	{
		super(state, properties);
	}

	@Override
	public @NotNull String getDescriptionId()
	{
		return BlockHelper.getRegistryName(this).toString().replace(':', '.').replace(':', '.').replace('/', '.');
	}
	
}

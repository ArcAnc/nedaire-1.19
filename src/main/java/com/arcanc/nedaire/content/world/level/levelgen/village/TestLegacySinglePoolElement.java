/**
 * @author ArcAnc
 * Created at: 2023-01-28
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.world.level.levelgen.village;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class TestLegacySinglePoolElement extends LegacySinglePoolElement 
{

	private int groundLevelDelta = 1;
	
	protected TestLegacySinglePoolElement(Either<ResourceLocation, StructureTemplate> p_210348_,
			Holder<StructureProcessorList> p_210349_, Projection p_210350_, int groundLevelDelta) 
	{
		super(p_210348_, p_210349_, p_210350_);
		this.groundLevelDelta = groundLevelDelta;
	}
	
	@Override
	public int getGroundLevelDelta() 
	{
		return groundLevelDelta;
	}

}

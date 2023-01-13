/**
 * @author ArcAnc
 * Created at: 2023-01-11
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.filter;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IFilter<Inv, T> extends INBTSerializable<CompoundTag>
{
	/**
	 * Return true if obj is eligible for next manipulations. If contains in whitelist or missing is blacklist
	 */
	boolean filter (T obj);
	
	boolean isWhitelist();
	
	void setWhitelist(boolean value);
	
	Inv getContent();
	
}

/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util;

import javax.annotation.Nullable;

import net.minecraft.util.StringRepresentable;

public enum AccessType implements StringRepresentable
{
	NONE ("none", 0x00000000),
	INPUT ("input", 0xff2626b7),
	OUTPUT ("output", 0xffb78426),
	FULL ("full", 0xff30e530);

	private final String name;
	private final int color;
	@SuppressWarnings("deprecation")
	public static final StringRepresentable.EnumCodec<AccessType> CODEC = StringRepresentable.fromEnum(AccessType :: values);
	
	private AccessType (String name, int color)
	{
		this.name = name;
		this.color = color;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public String getString() 
	{
		return name;
	}
	
	public int getColor() 
	{
		return color;
	}

	@Override
	public String getSerializedName() 
	{
		return name;
	}
	
	@SuppressWarnings("deprecation")
	public static AccessType byName(@Nullable String str) 
	{
		return CODEC.byName(str);
	}

}

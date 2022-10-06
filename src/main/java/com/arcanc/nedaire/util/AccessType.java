/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util;

public enum AccessType 
{
	NONE ("none"),
	INPUT ("input"),
	OUTPUT ("output"),
	FULL ("full");

	private final String name;
	
	private AccessType (String name)
	{
		this.name = name;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public String getString() 
	{
		return name;
	}
}

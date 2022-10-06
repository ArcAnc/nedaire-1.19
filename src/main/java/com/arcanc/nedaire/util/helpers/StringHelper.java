/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import com.arcanc.nedaire.util.database.ModDatabase;

import net.minecraft.resources.ResourceLocation;

public class StringHelper 
{
	public static String getStrLocFStr (String s)
	{
		return getLocFStr(s).toString();
	}
	
	public static ResourceLocation getLocFStr(String s)
	{
		return new ResourceLocation(ModDatabase.MOD_ID, s);
	}
	
	public static String symbolPlacer (char symbol, String... strings)
	{
		if (strings.length > 1)
		{
			String s = "";
			for (int i = 0; i< strings.length; i++)
			{
				s = i > 0 ? s + symbol + strings[i] : s + strings[i];
			}
			return s;
		}
		return strings[0];
	}
	
	public static String underscorePlacer (String... strings)
	{
		if (strings.length > 1)
		{
			String s = "";
			for (int i = 0; i< strings.length; i++)
			{
				s = i > 0 ? s + "_" + strings[i] : s + strings[i];
			}
			return s;
		}
		return strings[0];
	}
	
	public static final String slashPlacer(String name, String... path )
	{
		String s = "";
		
		for (int i = 0; i < path.length; i++)
		{
			s = s + path[i] + "/"; 
		}
		
		s += name;
		
		return s;
	}
	
	public static String plural (String s)
	{
		return s + "s";
	}
	
	public static String capitalize(String str)
	{
	    if(str == null) return str;
	    return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}

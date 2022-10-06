/**
 * @author ArcAnc
 * Created at: 2022-09-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.arcanc.nedaire.content.book.gui.EnchiridionScreen;
import com.arcanc.nedaire.content.book.parts.Chapter;
import com.arcanc.nedaire.content.book.parts.EnchElementList;
import com.arcanc.nedaire.util.database.ModDatabase;
import com.arcanc.nedaire.util.database.ModDatabase.GUI.Enchiridion.Section.SectionData;
import com.google.common.collect.Maps;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnchiridionInstance 
{
	public static final EnchiridionInstance INSTANCE = initEnchiridion();
	private EnchiridionScreen screen;
	
	private static final Map<String, Map<String, String>> FULLCONTENT = new HashMap<String, Map<String, String>>();	
	
	private static final Map<ResourceLocation, Supplier<Chapter>> CONTENT = Maps.newHashMap();   
	
	private EnchiridionInstance() 
	{
	
	}
	
	public void fillContent() 
	{
		int upper = 0;
		int bottom = 0;
		
		CONTENT.clear();
		
		/** FIXME: Add custom event for filling  database*/
		
		for (SectionData data : ModDatabase.GUI.Enchiridion.Section.SECTIONS)
		{
			int upperS = upper;
			int bottomS = bottom;
			boolean isNative = data.name().getNamespace() == ModDatabase.MOD_ID;
			CONTENT.put(data.name(), () -> new Chapter(
					isNative ? screen.guiLeft + 15 + (20 * upperS) + (4 * upperS) : screen.guiLeft + 15 + (20 * bottomS) + (4 * bottomS), 
					isNative ? screen.guiTop + 175 : screen.guiTop + 21, 
					data.name(), 
					INSTANCE, 
					data.icon(), 
					but -> {}, 
					(but, stack, x, y) -> 
					{
						screen.renderTooltip(stack, Component.translatable(data.name().toLanguageKey()), x, y);
					}).addData(new EnchElementList(INSTANCE, data.data())));
			if (isNative)
			{
				upper++;
			}
			else
			{
				bottom++;
			}
		}
	}
	
	public static EnchiridionInstance initEnchiridion()
	{
		return new EnchiridionInstance();
	}
	
	/**
	 * @return the content
	 */
	public static Map<ResourceLocation, Supplier<Chapter>> getContent() 
	{
		return CONTENT;
	}
	
	public EnchiridionScreen getScreen()
	{
		return screen;
	}
	
	public EnchiridionInstance setScreen(EnchiridionScreen screen) 
	{
		this.screen = screen;
		return this;
	}
}

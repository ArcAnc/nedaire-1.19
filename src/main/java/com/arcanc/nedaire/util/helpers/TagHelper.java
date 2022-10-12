/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TagHelper 
{
	public static <T> Stream<T> elementStream(RegistryAccess tags, ResourceKey<Registry<T>> registry, ResourceLocation tag) 
	{
		return holderStream(tags, registry, tag).map(Holder::value);
	}

	public static <T> Stream<T> elementStream(RegistryAccess tags, TagKey<T> key) 
	{
		return holderStream(tags.registryOrThrow(key.registry()), key).map(Holder::value);
	}

	public static <T> Stream<T> elementStream(Registry<T> registry, TagKey<T> tag) 
	{
		return holderStream(registry, tag).map(Holder::value);
	}
	
	public static <T> Stream<Holder<T>> holderStream(RegistryAccess tags, ResourceKey<Registry<T>> registry, ResourceLocation tag) 
	{
		return holderStream(tags.registryOrThrow(registry), TagKey.create(registry, tag));
	}

	public static <T> Stream<Holder<T>> holderStream(Registry<T> registry, TagKey<T> tag) 
	{
		return StreamSupport.stream(registry.getTagOrEmpty(tag).spliterator(), false);
	}
}

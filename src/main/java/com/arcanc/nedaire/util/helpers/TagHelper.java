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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.phys.Vec3;

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
	
	public static Vec3 readVec3(CompoundTag compound, String address)
	{
		Vec3 vec = Vec3.ZERO;
		if (compound.contains(address))
		{
			CompoundTag tag = compound.getCompound(address);
			vec = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
		}
		return vec;
	}
	
	public static CompoundTag writeVec3(Vec3 vec)
	{
		CompoundTag tag = new CompoundTag();
		tag.putDouble("x", vec.x());
		tag.putDouble("y", vec.y());
		tag.putDouble("z", vec.z());
		
		return tag;
	}
	
	public static CompoundTag writeVec3(Vec3 vec, CompoundTag dest, String address)
	{
		dest.put(address, writeVec3(vec));
		return dest;
	}
}

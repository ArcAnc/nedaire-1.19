/**
 * @author ArcAnc
 * Created at: 2022-11-05
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.joml.Vector3d;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class PacketHelper 
{
	public static <T> List<T> readList(FriendlyByteBuf buf, Function<FriendlyByteBuf, T> readElement)
	{
		int numElements = buf.readVarInt();
		List<T> result = new ArrayList<>(numElements);
		for(int q = 0; q < numElements; q++)
			result.add(readElement.apply(buf));
		return result;
	}
	
	public static <T> void writeListReverse(FriendlyByteBuf buffer, List<T> toWrite, BiConsumer<FriendlyByteBuf, T> writeElement)
	{
		writeList(buffer, toWrite, (t, buf) -> writeElement.accept(buf, t));
	}

	public static <T> void writeList(FriendlyByteBuf buf, List<T> toWrite, BiConsumer<T, FriendlyByteBuf> writeElement)
	{
		buf.writeVarInt(toWrite.size());
		for (T element : toWrite)
			writeElement.accept(element, buf);
	}
	
	public static void writeVector3d (FriendlyByteBuf buf, Vector3d vec)
	{
		buf.writeDouble(vec.x());
		buf.writeDouble(vec.y());
		buf.writeDouble(vec.z());
	}
	
	public static void writeVector3d (FriendlyByteBuf buf, Vec3 vec)
	{
		buf.writeDouble(vec.x());
		buf.writeDouble(vec.y());
		buf.writeDouble(vec.z());
	}
}

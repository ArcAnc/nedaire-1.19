/**
 * @author ArcAnc
 * Created at: 2022-11-04
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GenericDataSerializers 
{
	private static final List<DataSerializer<?>> SERIALIZERS = new ArrayList<>();
	public static final DataSerializer<Integer> INT32 = register(
			FriendlyByteBuf :: readVarInt, FriendlyByteBuf :: writeVarInt);
	
	public static final DataSerializer<FluidStack> FLUID_STACK = register(
			FriendlyByteBuf :: readFluidStack, FriendlyByteBuf :: writeFluidStack,
			FluidStack :: copy, FluidStack :: isFluidStackIdentical);
	
	public static final DataSerializer<Boolean> BOOLEAN = register(
			FriendlyByteBuf :: readBoolean, FriendlyByteBuf :: writeBoolean);
	
	public static final DataSerializer<Float> FLOAT = register(
			FriendlyByteBuf :: readFloat, FriendlyByteBuf :: writeFloat);
	
	//Allows items to be synced without requiring a slot
	public static final DataSerializer<ItemStack> ITEM_STACK = register(
			FriendlyByteBuf :: readItem, FriendlyByteBuf :: writeItem, ItemStack :: copy, ItemStack :: matches);

	public static final DataSerializer<List<FluidStack>> FLUID_STACKS = register(
			fbb -> fbb.readList(FriendlyByteBuf :: readFluidStack),
			(fbb, stacks) -> fbb.writeCollection(stacks, FriendlyByteBuf :: writeFluidStack),
			l -> l.stream().map(FluidStack :: copy).toList(),
			(l1, l2) -> 
			{
				if (l1.size() != l2.size())
					return false;
				for (int q = 0; q < l1.size(); q++)
					if(!l1.get(q).isFluidStackIdentical(l2.get(q)))
						return false;
				return true;
			});
	
	public static final DataSerializer<List<String>> STRINGS = register(
			fbb -> fbb.readList(FriendlyByteBuf :: readUtf),
			(fbb, list) -> fbb.writeCollection(list, FriendlyByteBuf :: writeUtf),
			ArrayList :: new,
			List :: equals);
	
	private static <T> DataSerializer<T> register(Function<FriendlyByteBuf, T> read, BiConsumer<FriendlyByteBuf, T> write)
	{
		return register(read, write, t -> t, Objects :: equals);
	}
	
	private static <T> DataSerializer<T> register(Function<FriendlyByteBuf, T> read, BiConsumer<FriendlyByteBuf, T> write, UnaryOperator<T> copy, BiPredicate<T, T> equals)
	{
		DataSerializer<T> serializer = new DataSerializer<>(read, write, copy, equals, SERIALIZERS.size());
		SERIALIZERS.add(serializer);
		return serializer;
	}
	
	public static DataPair<?> read (FriendlyByteBuf buffer)
	{
		DataSerializer<?> serializer = SERIALIZERS.get(buffer.readVarInt());
		return serializer.read(buffer);
	}
	
	public record DataSerializer<T> (Function<FriendlyByteBuf, T> read, BiConsumer<FriendlyByteBuf, T> write, UnaryOperator<T> copy, BiPredicate<T, T> equals, int id)
	{
		public DataPair<T> read (FriendlyByteBuf from)
		{
			return new DataPair<>(this, read().apply(from));
		}
	}

	public record DataPair<T> (DataSerializer<T> serializer, T data)
	{
		public void write (FriendlyByteBuf to)
		{
			to.writeVarInt(serializer.id());
			serializer.write().accept(to, data);
		}
	}
}

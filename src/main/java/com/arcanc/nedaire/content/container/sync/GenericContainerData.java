/**
 * @author ArcAnc
 * Created at: 2022-11-04
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.sync;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.container.sync.GenericDataSerializers.DataPair;
import com.arcanc.nedaire.content.container.sync.GenericDataSerializers.DataSerializer;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class GenericContainerData<T> 
{
	private final DataSerializer<T> serializer;
	private final Supplier<T> get;
	private final Consumer<T> set;
	private T current;
	
	public GenericContainerData(DataSerializer<T> serializer, Supplier<T> get, Consumer<T> set) 
	{
		this.serializer = serializer;
		this.get = get;
		this.set = set;
	}
	
	public GenericContainerData(DataSerializer<T> serializer, GetterAndSetter<T> io) 
	{
		this.serializer = serializer;
		this.get = io.getter();
		this.set = io.setter();
	}
	
	public static GenericContainerData<Integer> int32 (Supplier<Integer> get, Consumer<Integer> set)
	{
		return new GenericContainerData<>(GenericDataSerializers.INT32, get, set);
	}
	
	public static GenericContainerData<?> vim(IVim storage)
	{
		return int32(storage :: getEnergyStored, storage :: setEnergyStored);
	}
	
	public static GenericContainerData<FluidStack> fluid(FluidTank tank)
	{
		return new GenericContainerData<>(GenericDataSerializers.FLUID_STACK, tank :: getFluid, tank :: setFluid);
	}
	
	public static GenericContainerData<Boolean> bool (Supplier<Boolean> get, Consumer<Boolean> set)
	{
		return new GenericContainerData<>(GenericDataSerializers.BOOLEAN, get, set);
	}
	
	public static GenericContainerData<Float> float32(Supplier<Float> get, Consumer<Float> set)
	{
		return new GenericContainerData<>(GenericDataSerializers.FLOAT, get, set);
	}
	
	public boolean needsUpdate()
	{
		T newValue = get.get();
		if (newValue == null && current == null)
			return false;
		if (current != null && newValue != null && serializer.equals().test(current, newValue))
			return false;
		current = serializer.copy().apply(newValue);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void processSync (Object receivedData)
	{
		current = (T)receivedData;
		set.accept(serializer.copy().apply(current));
	}
	
	public DataPair<T> dataPair()
	{
		return new DataPair<>(serializer, current);
	}
}

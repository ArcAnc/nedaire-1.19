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
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

public record GetterAndSetter<T> (Supplier<T> getter, Consumer<T> setter) implements Supplier<T>, Consumer<T> 
{
	public static <T> GetterAndSetter<T> standalone(T initial)
	{
		Mutable<T> box = new MutableObject<>(initial);
		return new GetterAndSetter<>(box :: getValue, box :: setValue);
	}
	
	public static <T> GetterAndSetter<T> getterOnly (Supplier<T> getter)
	{
		return new GetterAndSetter<>(getter, zero -> {});
	}
	
	public static <T> List<GetterAndSetter<T>> forArray(T[] data)
	{
		List<GetterAndSetter<T>> result = new ArrayList<>(data.length);
		for (int q = 0; q < data.length; q++)
		{
			int finalQ = q;
			result.add(new GetterAndSetter<>(() -> data[finalQ], o -> data[finalQ] = o));
		}
		return result;
	}
	
	public void set(T newValue) 
	{
		setter.accept(newValue);
	}

	@Override
	public T get() 
	{
		return getter.get();
	}
	
	@Override
	public void accept(T t) 
	{
		set(t);
	}
}

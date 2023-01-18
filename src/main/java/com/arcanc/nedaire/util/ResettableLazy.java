/**
 * @author ArcAnc
 * Created at: 2023-01-15
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.common.util.NonNullSupplier;

public class ResettableLazy<T> implements Supplier<T> 
{
	private final NonNullSupplier<T> getter;
	private final NonNullConsumer<T> destructor;
	@Nullable
	private T cached;

	public ResettableLazy(NonNullSupplier<T> getter)
	{
		this(getter, v -> {
		});
	}

	public ResettableLazy(NonNullSupplier<T> getter, NonNullConsumer<T> destructor)
	{
		this.getter = getter;
		this.destructor = destructor;
	}

	@Nonnull
	public T get()
	{
		if(cached==null)
			cached = getter.get();
		return cached;
	}

	public void reset()
	{
		if(cached!=null)
		{
			destructor.accept(cached);
			cached = null;
		}
	}	
}

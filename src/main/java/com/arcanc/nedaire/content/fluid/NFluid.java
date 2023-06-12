/**
 * @author ArcAnc
 * Created at: 2023-03-21
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.fluid;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.Util;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

public abstract class NFluid extends ForgeFlowingFluid 
{

	public static NFluid makeFluid(Function<Properties, ? extends NFluid> make, Properties properties, Consumer<Properties> props)
	{
		return make.apply(Util.make(properties, props));
	}
	
	protected NFluid(Properties properties) 
	{
		super(properties);
	}

	public static class NFluidSource extends NFluid
	{

		public NFluidSource(Properties properties) 
		{
			super(properties);
		}

		@Override
		public int getAmount(@NotNull FluidState state)
		{
			return 8;
		}

		@Override
		public boolean isSource(@NotNull FluidState state)
		{
			return true;
		}
	}
	
	public static class NFluidFlowing extends NFluid
	{

		public NFluidFlowing(Properties properties) 
		{
			super(properties);
	        registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
		}

		@Override
		public int getAmount(FluidState state) 
		{
			return state.getValue(LEVEL);
		}

		@Override
		public boolean isSource(@NotNull FluidState state)
		{
			return false;
		}
		
		@Override
		protected void createFluidStateDefinition(@NotNull Builder<Fluid, FluidState> builder)
		{
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}
		
	}
	
	public interface FluidPropsGetter
	{
		ForgeFlowingFluid.Properties get(Supplier<? extends FluidType> fluidType, Supplier<? extends Fluid> still, Supplier<? extends Fluid> flowing);
	}
	
}

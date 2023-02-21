/**
 * @author ArcAnc
 * Created at: 2023-02-16
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.fluid;

import javax.annotation.Nonnull;

import com.google.common.base.Predicate;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public interface IFluidStackAccess 
{
    @Nonnull
    FluidStack getFluidStack();
    
    void setFluidStack(@Nonnull FluidStack stack);

    int getCount();
    
    void setCount(int newCount);
    
    /**
     * @param amount - the size by which to modify the number of items
     * @return new stack size
     */
    int modify(int amount);

    int getSizeLimit();

    boolean isEmpty();

    @Nonnull
    int insert(@Nonnull FluidStack stack, FluidAction simulate);
    
    @Nonnull
    FluidStack extract (FluidStack stack, FluidAction simulate);
    
    @Nonnull
    FluidStack extract(int amount, FluidAction simulate);
    
    void clear();
    
    IFluidStackAccess load(CompoundTag nbt);
    
    CompoundTag save();
    
    IFluidStackAccess setValidator(Predicate<FluidStack> validator);
    
    boolean isValid(@Nonnull FluidStack stack);
    
    default void onContentsChanged()
    {
    	
    }

}

/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.inventory;

import javax.annotation.Nonnull;

import com.google.common.base.Predicate;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IItemStackAcess 
{
    @Nonnull
    ItemStack getItemStack();
    
    void setItemStack(@Nonnull ItemStack stack);

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
    ItemStack insert(@Nonnull ItemStack stack, boolean simulate);
    
    @Nonnull
    ItemStack extract(int amount, boolean simulate);
    
    void clear();
    
    IItemStackAcess load(CompoundTag nbt);
    
    CompoundTag save();
    
    IItemStackAcess setValidator(Predicate<ItemStack> validator);
    
    boolean isValid(@Nonnull ItemStack stack);
    
    default void onContentsChanged()
    {
    	
    }
}

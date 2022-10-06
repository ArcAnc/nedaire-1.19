/**
 * @author ArcAnc
 * Created at: 2022-06-24
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.module.jewelry.capability;

import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISocket extends INBTSerializable<CompoundTag>
{

	boolean isActivated();
	
    int getSlots();

    void setStackInSlot(int slot, @NotNull ItemStack stack);
    
    @NotNull
    ItemStack getStackInSlot(int slot);

    @NotNull
    ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate);

    @NotNull
    ItemStack extractItem(int slot, boolean simulate);

    boolean isItemValid(int slot, @NotNull ItemStack stack);

}

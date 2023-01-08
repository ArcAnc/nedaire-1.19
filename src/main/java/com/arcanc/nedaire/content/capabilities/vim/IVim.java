/**
 * @author ArcAnc
 * Created at: 2022-04-23
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.vim;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IVim extends INBTSerializable<CompoundTag>
{
    /**
    * Adds energy to the storage. Returns quantity of energy that was accepted.
    *
    * @param maxReceive
    *            Maximum amount of energy to be inserted.
    * @param simulate
    *            If TRUE, the insertion will only be simulated.
    * @return Amount of energy that was (or would have been, if simulated) accepted by the storage.
    */
    int add(int count, boolean simulate);

    /**
    * Removes energy from the storage. Returns quantity of energy that was removed.
    *
    * @param maxExtract
    *            Maximum amount of energy to be extracted.
    * @param simulate
    *            If TRUE, the extraction will only be simulated.
    * @return Amount of energy that was (or would have been, if simulated) extracted from the storage.
    */
    int extract(int count, boolean simulate);
    
    /**
    * Returns the amount of energy currently stored.
    */
    int getEnergyStored();

    /**
    * Returns the maximum amount of energy that can be stored.
    */
    int getMaxEnergyStored();

    /**
     * Returns if this storage can have energy extracted.
     * If this is false, then any calls to extractEnergy will return 0.
     */
    boolean isAllowedExtraction ();

    /**
     * Used to determine if this storage can receive energy.
     * If this is false, then any calls to receiveEnergy will return 0.
     */
    boolean isAllowedReceiving ();

    
    /**
     * Directly changed current energy amount. Can not be lower than 0 or more than {@link #getMaxEnergyStored()}
     */
    void setEnergyStored(int count);
    
    /**
     * Directly changed current max energy amount. Can not be lower than 0 
     */
    void setMaxEnergyStored(int count);
}

/**
 * @author ArcAnc
 * Created at: 2023-01-11
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.filter;

import com.arcanc.nedaire.content.capabilities.vim.IVim;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

public class CapabilityFilter 
{
    public static final Capability<IFilter<IItemHandler, ItemStack>> FILTER_ITEM = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IFilter<IItemHandler, FluidStack>> FILTER_FLUID = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IFilter<IVim, Integer>> FILTER_VIM = CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(IFilter.class);
    }
}

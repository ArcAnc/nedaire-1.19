/**
 * @author ArcAnc
 * Created at: 2023-01-11
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.capabilities.filter;

import com.arcanc.nedaire.content.capabilities.filter.IFilter.IFluidFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IItemFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IVimFilter;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityFilter 
{
    public static final Capability<IItemFilter> FILTER_ITEM = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IFluidFilter> FILTER_FLUID = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IVimFilter> FILTER_VIM = CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(IFilter.class);
    }
}

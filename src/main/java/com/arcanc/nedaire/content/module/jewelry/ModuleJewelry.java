/**
 * @author ArcAnc
 * Created at: 2022-08-28
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.module.jewelry;

import com.arcanc.nedaire.content.item.gem.GemItem;
import com.arcanc.nedaire.content.item.gem.GemUtils;
import com.arcanc.nedaire.content.module.jewelry.capability.CapabilitySocket;
import com.arcanc.nedaire.content.module.jewelry.capability.SocketStorage;
import com.arcanc.nedaire.content.registration.ModRegistration.RegisterItems.ItemRegObject;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModuleJewelry
{
	public static boolean MUST_PRESENT = false;
	
	public static final ItemRegObject<Item> GEM = MUST_PRESENT ? new ItemRegObject<>("gem", (p) -> new GemItem(p)) : null;
	
	public static void init(IEventBus bus) 
	{
	    if (MUST_PRESENT)
	    {
			MinecraftForge.EVENT_BUS.addListener(GemUtils :: changeEquipEvent);
		    MinecraftForge.EVENT_BUS.addListener(GemUtils :: entityTickEvent);
		    MinecraftForge.EVENT_BUS.addListener(GemUtils :: addArmorTooltip);
		    
		    MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, EventPriority.LOWEST, SocketStorage :: attachCapability);
		    
		    bus.addListener(ModuleJewelry :: registerCap);
		    bus.addListener(ModuleJewelry :: registerItemColors);
	    }
	}
	
	private static void registerCap (final RegisterCapabilitiesEvent event)
	{
		if (MUST_PRESENT)
		{
			CapabilitySocket.register(event);
		}
	}
	
	private static void registerItemColors(final RegisterColorHandlersEvent.Item event)
	{
		if (MUST_PRESENT)
		{
			event.register((stack, overlay) -> 
			{
				if (overlay == 0 && stack.getItem() instanceof GemItem)
				{
					return overlay > 0 ? -1 : GemUtils.getColor(stack);
				}
				return -1;
			}, ModuleJewelry.GEM.get());
		}
	}
}

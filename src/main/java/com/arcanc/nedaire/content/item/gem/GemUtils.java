/**
 * @author ArcAnc
 * Created at: 2022-06-14
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.gem;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.item.ItemInterfaces.IGemItem;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.SocketHelper;
import com.google.common.collect.Maps;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class GemUtils
{
	public static final Map<UUID, GemEffectInstance<? extends GemEffect<?>>> effectMap = Maps.newHashMap();
	
	public static ItemStack makeGem (ItemStack stack, Color color)
	{
		CompoundTag tag = new CompoundTag();
		
		tag.putInt(NDatabase.Capabilities.Socket.COLOR, color.getRGB());
		stack.getOrCreateTag().put(NDatabase.Capabilities.Socket.TAG_LOCATION, tag);
		
		return stack;
	}
	
	public static int getColor (ItemStack stack)
	{
		CompoundTag compoundtag = stack.getTag().getCompound(NDatabase.Capabilities.Socket.TAG_LOCATION);
	    if (compoundtag != null && compoundtag.contains(NDatabase.Capabilities.Socket.COLOR, Tag.TAG_ANY_NUMERIC)) 
	    {
	    	return compoundtag.getInt(NDatabase.Capabilities.Socket.COLOR);
	    }
	    else
	    {
	    	return Color.RED.getRGB();
	    }
	}
	
	public static <T extends GemEffect<T>> T getEffect(ItemStack stack)
	{
		CompoundTag tag = stack.getOrCreateTag().getCompound(NDatabase.Capabilities.Socket.TAG_LOCATION);
		if (tag!= null)
		{
			UUID id = tag.getUUID(NDatabase.Capabilities.Socket.ID);
			return (T) GemUtils.effectMap.get(id).getEffect();
		}
		return null;
	}
	
	public static void changeEquipEvent (final LivingEquipmentChangeEvent event)
	{
		LivingEntity ent = event.getEntity();
		ItemStack to = event.getTo();
		ItemStack from = event.getFrom();
		
		//remove
		SocketHelper.getSocketHandler(from).ifPresent(handler -> 
		{
			for (int q = 0; q < handler.getSlots(); q++)
			{
				ItemStack stack = handler.getStackInSlot(q);
				
				if (!stack.isEmpty())
				{
					GemEffect<?> effect = GemUtils.getEffect(stack);
					
					effect.removeInstanteousEffect(stack, ent.getLevel(), ent);
				}
			}

		});

		
		//equip
		SocketHelper.getSocketHandler(to).ifPresent(handler -> 
		{
			for (int q = 0; q < handler.getSlots(); q++)
			{
				ItemStack stack = handler.getStackInSlot(q);
				
				if (!stack.isEmpty())
				{
					GemEffect<?> effect = GemUtils.getEffect(stack);
					
					effect.applyInstateousEffect(stack, ent.getLevel(), ent);
				}
			}
		});
		
	}
	
	public static void entityTickEvent (final LivingTickEvent event)
	{
		LivingEntity ent = event.getEntity();
		
		for (EquipmentSlot slot : EquipmentSlot.values())
		{
			if(slot.getType() == EquipmentSlot.Type.ARMOR)
			{
				ItemStack armor = ent.getLastArmorItem(slot);
				SocketHelper.getSocketHandler(armor).ifPresent(handler -> 
				{
					for (int q = 0; q < handler.getSlots(); q++)
					{
						ItemStack stack = handler.getStackInSlot(q);
						
						if (!stack.isEmpty())
						{
							GemEffect<?> effect = GemUtils.getEffect(stack);
							
							effect.tick(stack, ent.getLevel(), ent);
						}
					}

				});
			}
		}
	}
	
	public static void addArmorTooltip (final ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();
//		Nedaire.getLogger().debug("I'm working!");
		if (stack.getItem() instanceof ArmorItem armor || stack.getItem() instanceof HorseArmorItem horseArmor)
		{
//			Nedaire.getLogger().debug("Armor Item");
			stack.getCapability(SocketHelper.socketHandler).ifPresent(cap -> 
			{
				if (cap.isActivated() && cap.getSlots() > 0)
				{
					List<Component> descr = event.getToolTip();
					
					descr.add(Component.empty());
					descr.add(Component.translatable(NDatabase.MOD_ID + ".socket.contains").withStyle(ChatFormatting.GRAY));
					for (int q = 0 ; q < cap.getSlots(); q++)
					{
						ItemStack socket = cap.getStackInSlot(q);
						if (socket == ItemStack.EMPTY)
						{
							descr.add(Component.translatable(NDatabase.MOD_ID + ".socket.number", q , Component.translatable(NDatabase.MOD_ID + ".socket.empty")).withStyle(ChatFormatting.BLUE));
						}
						else
						{
							if (socket.getItem() instanceof IGemItem gem)
							{
								descr.add(Component.translatable(NDatabase.MOD_ID + ".socket.number", q + ": " + gem.getEffect(socket).getEffect().getDisplayDescription()));
							}
							else
							{
								descr.add(Component.translatable(NDatabase.MOD_ID + ".socket.number", q + ": " + Component.translatable(NDatabase.MOD_ID + ".socket.unknown" )).withStyle(ChatFormatting.RED));
							}
						}
					}
				}
			});
		}
	}
}

/**
 * @author ArcAnc
 * Created at: 2022-04-28
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container;

import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.arcanc.nedaire.util.inventory.IItemStackAcess;
import com.arcanc.nedaire.util.inventory.ModManagedItemStorage;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class NSlot extends SlotItemHandler 
{	
	public static final ResourceLocation BACKGROUND_STANDART = StringHelper.getLocFStr(NDatabase.GUI.Slots.Textures.STANDART);
	public static final ResourceLocation BACKGROUND_INPUT = StringHelper.getLocFStr(NDatabase.GUI.Slots.Textures.INPUT);
	public static final ResourceLocation BACKGROUND_OUPUT = StringHelper.getLocFStr(NDatabase.GUI.Slots.Textures.OUTPUT);
	public static final ResourceLocation BACKGROUND_BOTH = StringHelper.getLocFStr(NDatabase.GUI.Slots.Textures.BOTH);
	
	private final AbstractContainerMenu containerMenu;

	public NSlot(AbstractContainerMenu menu, IItemHandler inv, int id, int x, int y) 
	{
		super(inv, id, x, y);
		this.containerMenu = menu;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) 
	{
		return true;
	}
	
	@Override
	public void set(ItemStack stack) 
	{
		if (getItemHandler() instanceof NSimpleItemStorage handler)
		{
			handler.setStackInSlot(stack, getSlotIndex());
			handler.onInventoryChange(getSlotIndex());
		}
	}
	
	@Override
	public void initialize(ItemStack stack) 
	{
		if (getItemHandler() instanceof NSimpleItemStorage handler)
		{
			handler.setStackInSlot(stack, getSlotIndex());
			handler.onInventoryChange(getSlotIndex());
		}
	}
	
	public NSlot setColoredBackground ()
	{
		if (getItemHandler() instanceof ModManagedItemStorage storage)
		{
			IItemStackAcess slot = storage.getFullHandler().getSlot(getSlotIndex());
			if (storage.getInputHandler().getItems().contains(slot))
			{
				setBackground(InventoryMenu.BLOCK_ATLAS, BACKGROUND_INPUT);
				return this;
			}
			else if (storage.getOutputHandler().getItems().contains(slot))
			{
				setBackground(InventoryMenu.BLOCK_ATLAS, BACKGROUND_OUPUT);
				return this;
			}
			else
			{
				setBackground(InventoryMenu.BLOCK_ATLAS, BACKGROUND_BOTH);
				return this;
			}
		}
		setBackground(InventoryMenu.BLOCK_ATLAS, BACKGROUND_STANDART);
		return this;
	}
	
	@Override
	public NSlot setBackground(ResourceLocation atlas, ResourceLocation sprite) 
	{

		super.setBackground(atlas, sprite);
		return this;
	}
	
	public static class Output extends NSlot 
	{
		public Output(AbstractContainerMenu menu, IItemHandler inv, int id, int x, int y) 
		{
			super(menu, inv, id, x, y);
		}
	
		@Override
		public boolean mayPlace(ItemStack stack) 
		{
			return false;
		}
	}
	
	public static class ItemHandlerGhost extends SlotItemHandler
	{

		public ItemHandlerGhost(IItemHandler itemHandler, int index, int xPosition, int yPosition) 
		{
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean mayPickup(Player playerIn) 
		{
			return false;
		}
	}
}

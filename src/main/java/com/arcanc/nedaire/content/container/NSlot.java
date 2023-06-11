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
import com.arcanc.nedaire.util.inventory.IItemStackAccess;
import com.arcanc.nedaire.util.inventory.NManagedItemStorage;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;
import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Predicate;

public class NSlot extends SlotItemHandler 
{	
	public static final ResourceLocation BACKGROUND_STANDART = StringHelper.getLocFStr(NDatabase.GUI.Slots.Textures.STANDART);
	public static final ResourceLocation BACKGROUND_INPUT = StringHelper.getLocFStr(NDatabase.GUI.Slots.Textures.INPUT);
	public static final ResourceLocation BACKGROUND_OUPUT = StringHelper.getLocFStr(NDatabase.GUI.Slots.Textures.OUTPUT);
	public static final ResourceLocation BACKGROUND_BOTH = StringHelper.getLocFStr(NDatabase.GUI.Slots.Textures.BOTH);
	protected boolean active = true;
	protected int panelIndex = 0;
	protected Predicate<ItemStack> mayPlace = null;
	
	public NSlot(IItemHandler inv, int panelIndex, int id, int x, int y) 
	{
		super(inv, id, x, y);
		this.panelIndex = panelIndex;
		mayPlace = $ -> true;
	}

	public NSlot(IItemHandler inv, int panelIndex, int id, int x, int y, Predicate<ItemStack> mayPlace)
	{
		this(inv, panelIndex, id, x, y);
		Preconditions.checkNotNull(mayPlace);
		this.mayPlace = mayPlace;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) 
	{
		return this.mayPlace.test(stack);
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
		if (getItemHandler() instanceof NManagedItemStorage storage)
		{
			IItemStackAccess slot = storage.getFullHandler().getSlot(getSlotIndex());
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
	
	/**
	 * 
	 * @param active New state of slot
	 * @return this, to allow chaining.
	 */
	public NSlot setActive(boolean active) 
	{
		this.active = active;
		return this;
	}
	
	@Override
	public boolean isActive() 
	{
		return active;
	}
	
	public int getPanelIndex() 
	{
		return panelIndex;
	}

	public static class ItemHandlerGhost extends NSlot
	{

		public ItemHandlerGhost(IItemHandler itemHandler, int panelIndex, int index, int xPosition, int yPosition, Predicate<ItemStack> mayPlace)
		{
			super(itemHandler, panelIndex, index, xPosition, yPosition);
			Preconditions.checkNotNull(mayPlace);
			this.mayPlace = mayPlace;
		}
		public ItemHandlerGhost(IItemHandler itemHandler, int panelIndex, int index, int xPosition, int yPosition) 
		{
			this(itemHandler, panelIndex, index, xPosition, yPosition, $ -> true);
		}

		@Override
		public boolean mayPickup(Player playerIn) 
		{
			return false;
		}
	}
}

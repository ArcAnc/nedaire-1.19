/**
 * @author ArcAnc
 * Created at: 2022-04-28
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import javax.annotation.Nullable;

import com.arcanc.nedaire.content.container.ModSlot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class ModAbstractCotainerMenu <T extends BlockEntity> extends AbstractContainerMenu 
{
	
	public T tile;
	@Nullable
	public Container inv;
	public int slotCount;
	
	
	public ModAbstractCotainerMenu(MenuType<?> type, int windowId, T tile) 
	{
		super(type, windowId);
	}

	@Override
	public boolean stillValid(Player player) 
	{
		return false;
	}
	
	@Override
	public void broadcastChanges() 
	{
		super.broadcastChanges();
	}
	
    private int addSlotRange(Inventory handler, int index, int x, int y, int amount, int dx) 
    {
        for (int i = 0 ; i < amount ; i++) 
        {
            addSlot(new Slot(handler, index, x, y).setBackground(InventoryMenu.BLOCK_ATLAS, ModSlot.BACKGROUND_STANDART));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Inventory handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) 
    {
        for (int j = 0 ; j < verAmount ; j++) 
        {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void layoutPlayerInventorySlots(Inventory playerInv, int leftCol, int topRow) 
    {
        // Player inventory
        addSlotBox(playerInv, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInv, 0, leftCol, topRow, 9, 18);
    }
	
}

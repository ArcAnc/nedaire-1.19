/**
 * @author ArcAnc
 * Created at: 2023-01-03
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEHoover;
import com.arcanc.nedaire.content.capabilities.filter.IFilter;
import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FilterHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class NHooverMenu extends NContainerMenu 
{

	public final IItemHandler inv;
	public final LazyOptional<IFilter<IItemHandler, ItemStack>> itemFilter;
	
	public static NHooverMenu makeServer(MenuType<?> type, int id, Inventory player, NBEHoover be)
	{
		return new NHooverMenu(blockCtx(type, id, be), player, ItemHelper.getItemHandler(be).
																map(handler -> handler).
																orElse(new ItemStackHandler(NBEHoover.INVENTORY_SIZE)), FilterHelper.getItemFilter(be));
	}
	
	public static NHooverMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.getLevel();
		BlockEntity te = BlockHelper.getTileEntity(l, pos);
		return new NHooverMenu(clientCtx(type, id), player, ItemHelper.getItemHandler(te).
															map(handler -> handler).
															orElse(new ItemStackHandler(NBEHoover.INVENTORY_SIZE)), FilterHelper.getItemFilter(te));
	}
	
	private NHooverMenu(MenuContext ctx, Inventory inventoryPlayer, IItemHandler inv , LazyOptional<IFilter<IItemHandler, ItemStack>> itemFilter) 
	{
		super(ctx);
		this.inv = inv;
		this.itemFilter = itemFilter;
	
		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
		
		for (int q = 0; q < inv.getSlots(); q++)
		{
			this.addSlot(new NSlot(inv, 0, q, 62 + (q % 3) * 18, 15 + (q / 3) * 18).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_STANDART).setActive(true));
		}
		
		addItemFilterSlots(itemFilter, 67, 18);
	}

}

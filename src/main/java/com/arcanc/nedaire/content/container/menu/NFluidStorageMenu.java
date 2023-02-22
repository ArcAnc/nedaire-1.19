/**
 * @author ArcAnc
 * Created at: 2023-02-22
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEFluidStorage;
import com.arcanc.nedaire.util.helpers.BlockHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

public class NFluidStorageMenu extends NContainerMenu 
{
	public final NBEFluidStorage be;

	public static NFluidStorageMenu makeServer(MenuType<?> type, int id, Inventory player, NBEFluidStorage be)
	{
		return new NFluidStorageMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NFluidStorageMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.getLevel();
		NBEFluidStorage be = BlockHelper.castTileEntity(l, pos, NBEFluidStorage.class).get();
		return new NFluidStorageMenu(clientCtx(type, id), player, be);
	}
	
	private NFluidStorageMenu(MenuContext ctx, Inventory inventoryPlayer, NBEFluidStorage be) 
	{
		super(ctx);
		this.be = be;
	
		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
	}
}

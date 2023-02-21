/**
 * @author ArcAnc
 * Created at: 2023-02-22
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEVimStorage;
import com.arcanc.nedaire.util.helpers.BlockHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

public class NVimStorageMenu extends NContainerMenu 
{

	public final NBEVimStorage be;

	public static NVimStorageMenu makeServer(MenuType<?> type, int id, Inventory player, NBEVimStorage be)
	{
		return new NVimStorageMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NVimStorageMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.getLevel();
		NBEVimStorage be = BlockHelper.castTileEntity(l, pos, NBEVimStorage.class).get();
		return new NVimStorageMenu(clientCtx(type, id), player, be);
	}
	
	private NVimStorageMenu(MenuContext ctx, Inventory inventoryPlayer, NBEVimStorage be) 
	{
		super(ctx);
		this.be = be;
	
		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
	}

}

/**
 * @author ArcAnc
 * Created at: 2023-02-21
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEDeliveryStation;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FilterHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

public class NDeliveryStationMenu extends NContainerMenu 
{
	public final NBEDeliveryStation be;

	public static NDeliveryStationMenu makeServer(MenuType<?> type, int id, Inventory player, NBEDeliveryStation be)
	{
		return new NDeliveryStationMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NDeliveryStationMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.getLevel();
		NBEDeliveryStation be = BlockHelper.castTileEntity(l, pos, NBEDeliveryStation.class).get();
		return new NDeliveryStationMenu(clientCtx(type, id), player, be);
	}
	
	private NDeliveryStationMenu(MenuContext ctx, Inventory inventoryPlayer, NBEDeliveryStation be) 
	{
		super(ctx);
		this.be = be;
	
		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);

		addItemFilterSlots(FilterHelper.getItemFilter(be), 62, 15);
		
		addFluidFilterSlots(FilterHelper.getFluidFilter(be), 62, 15);
	}
}

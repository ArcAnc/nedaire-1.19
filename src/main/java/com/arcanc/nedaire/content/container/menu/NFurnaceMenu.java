/**
 * @author ArcAnc
 * Created at: 2023-03-07
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEFurnace;
import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class NFurnaceMenu extends NContainerMenu 
{
	public final NBEFurnace be;
	
	public static NFurnaceMenu makeServer(MenuType<?> type, int id, Inventory player, NBEFurnace be)
	{
		return new NFurnaceMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NFurnaceMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.level();
		NBEFurnace be = BlockHelper.castTileEntity(l, pos, NBEFurnace.class).get();
		return new NFurnaceMenu(clientCtx(type, id), player, be);
	}
	
	private NFurnaceMenu(MenuContext ctx, Inventory inventoryPlayer, NBEFurnace be) 
	{
		super(ctx);
		this.be = be;
	
		IItemHandler inv = ItemHelper.getItemHandler(be).
				map(handler -> handler).
				orElse(new ItemStackHandler(5));

		this.addSlot(new NSlot(inv, 0, 0, 70, 30).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_INPUT).setActive(true));

		this.addSlot(new NSlot(inv, 0, 1, 145, 30, stack -> false).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_OUTPUT).setActive(true));

		this.ownSlotCount = 2;

		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
	}

}

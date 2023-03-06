/**
 * @author ArcAnc
 * Created at: 2023-02-26
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEMobCatcher;
import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.content.container.NSlot.MobCatcherSlot;
import com.arcanc.nedaire.content.container.NSlot.Output;
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

public class NMobCatcherMenu extends NContainerMenu 
{

	public final NBEMobCatcher be;
	
	public static NMobCatcherMenu makeServer(MenuType<?> type, int id, Inventory player, NBEMobCatcher be)
	{
		return new NMobCatcherMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NMobCatcherMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.getLevel();
		NBEMobCatcher be = BlockHelper.castTileEntity(l, pos, NBEMobCatcher.class).get();
		return new NMobCatcherMenu(clientCtx(type, id), player, be);
	}
	
	private NMobCatcherMenu(MenuContext ctx, Inventory inventoryPlayer, NBEMobCatcher be) 
	{
		super(ctx);
		this.be = be;
	
		IItemHandler inv = ItemHelper.getItemHandler(be).
				map(handler -> handler).
				orElse(new ItemStackHandler(5));

		
		this.addSlot(new MobCatcherSlot(inv, 0, 0, 145, 15).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_INPUT).setActive(true));
		
		for (int q = 1; q < inv.getSlots(); q++)
		{
			this.addSlot(new Output(inv, 0, q, 70 + ((q - 1) % 2) * 18, 20 + ((q - 1) / 2) * 18).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_OUPUT).setActive(true));
		}

		this.ownSlotCount = inv.getSlots();
		
		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
	}
}

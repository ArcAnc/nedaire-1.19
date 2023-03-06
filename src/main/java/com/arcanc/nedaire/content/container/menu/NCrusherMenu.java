/**
 * @author ArcAnc
 * Created at: 2023-03-06
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBECrusher;
import com.arcanc.nedaire.content.container.NSlot;
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

public class NCrusherMenu extends NContainerMenu 
{
	public final NBECrusher be;
	
	public static NCrusherMenu makeServer(MenuType<?> type, int id, Inventory player, NBECrusher be)
	{
		return new NCrusherMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NCrusherMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.getLevel();
		NBECrusher be = BlockHelper.castTileEntity(l, pos, NBECrusher.class).get();
		return new NCrusherMenu(clientCtx(type, id), player, be);
	}
	
	private NCrusherMenu(MenuContext ctx, Inventory inventoryPlayer, NBECrusher be) 
	{
		super(ctx);
		this.be = be;
	
		IItemHandler inv = ItemHelper.getItemHandler(be).
				map(handler -> handler).
				orElse(new ItemStackHandler(5));

		this.addSlot(new NSlot(inv, 0, 0, 70, 30).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_INPUT).setActive(true));

		this.addSlot(new Output(inv, 0, 1, 145, 20).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_OUPUT).setActive(true));

		this.addSlot(new Output(inv, 0, 2, 145, 40).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_OUPUT).setActive(true));

		this.ownSlotCount = 3;

		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
	}
}

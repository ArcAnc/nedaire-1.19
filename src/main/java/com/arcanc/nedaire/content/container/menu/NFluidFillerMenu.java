/**
 * @author ArcAnc
 * Created at: 2023-03-11
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEFluidFiller;
import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.content.container.NSlot.FluidHandler;
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

public class NFluidFillerMenu extends NContainerMenu 
{

	public final NBEFluidFiller be;

	public static NFluidFillerMenu makeServer(MenuType<?> type, int id, Inventory player, NBEFluidFiller be)
	{
		return new NFluidFillerMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NFluidFillerMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.getLevel();
		NBEFluidFiller be = BlockHelper.castTileEntity(l, pos, NBEFluidFiller.class).get();
		return new NFluidFillerMenu(clientCtx(type, id), player, be);
	}
	
	private NFluidFillerMenu(MenuContext ctx, Inventory inventoryPlayer, NBEFluidFiller be) 
	{
		super(ctx);
		this.be = be;
		
		IItemHandler inv = ItemHelper.getItemHandler(be).
				map(handler -> handler).
				orElse(new ItemStackHandler(2));

		this.addSlot(new FluidHandler(inv, 0, 0, 87, 25).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_INPUT).setActive(true));

		this.addSlot(new Output(inv, 0, 1, 87, 45).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_OUPUT).setActive(true));
		
		ownSlotCount = 2;
		
		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
	}
}
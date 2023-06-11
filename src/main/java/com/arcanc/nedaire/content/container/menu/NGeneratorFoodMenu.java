/**
 * @author ArcAnc
 * Created at: 2023-02-23
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEGeneratorFood;
import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class NGeneratorFoodMenu extends NContainerMenu 
{

	public final NBEGeneratorFood be;

	public static NGeneratorFoodMenu makeServer(MenuType<?> type, int id, Inventory player, NBEGeneratorFood be)
	{
		return new NGeneratorFoodMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NGeneratorFoodMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.level();
		NBEGeneratorFood be = BlockHelper.castTileEntity(l, pos, NBEGeneratorFood.class).get();
		return new NGeneratorFoodMenu(clientCtx(type, id), player, be);
	}
	
	private NGeneratorFoodMenu(MenuContext ctx, Inventory inventoryPlayer, NBEGeneratorFood be) 
	{
		super(ctx);
		this.be = be;
	

		IItemHandler inv = ItemHelper.getItemHandler(be).
				map(handler -> handler).
				orElse(new ItemStackHandler(1));
		
		this.addSlot(new NSlot(inv, 0, 0, 62 + 18, 15 + 18, ItemStack::isEdible).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_BOTH).setActive(true));
		
		this.ownSlotCount = 1;
		
		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
	}
}

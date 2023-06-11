/**
 * @author ArcAnc
 * Created at: 2023-03-04
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEGeneratorMob;
import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.content.item.CrystalPrisonItem;
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

public class NGeneratorMobMenu extends NContainerMenu 
{
	public final NBEGeneratorMob be;
	
	public static NGeneratorMobMenu makeServer(MenuType<?> type, int id, Inventory player, NBEGeneratorMob be)
	{
		return new NGeneratorMobMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NGeneratorMobMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.level();
		NBEGeneratorMob be = BlockHelper.castTileEntity(l, pos, NBEGeneratorMob.class).get();
		return new NGeneratorMobMenu(clientCtx(type, id), player, be);
	}
	
	private NGeneratorMobMenu(MenuContext ctx, Inventory inventoryPlayer, NBEGeneratorMob be) 
	{
		super(ctx);
		this.be = be;
	
		IItemHandler inv = ItemHelper.getItemHandler(be).
				map(handler -> handler).
				orElse(new ItemStackHandler(5));

		for (int q = 0; q < 4; q++)
		{
			this.addSlot(new NSlot(inv, 0, q, 70 + (q % 2) * 18, 20 + (q / 2) * 18, stack -> stack.getItem() instanceof CrystalPrisonItem && !stack.getTag().getCompound(CrystalPrisonItem.ENTITY_DATA).isEmpty()).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_INPUT).setActive(true));
		}

		this.addSlot(new NSlot(inv, 0, 4, 145, 29, stack -> false).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_OUPUT).setActive(true));

		this.ownSlotCount = 5;

		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
	}
}

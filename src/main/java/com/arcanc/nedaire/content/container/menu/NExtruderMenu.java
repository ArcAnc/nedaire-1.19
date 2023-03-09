/**
 * @author ArcAnc
 * Created at: 2023-03-09
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEExtruder;
import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.content.container.NSlot.Output;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class NExtruderMenu extends NContainerMenu 
{
	public final NBEExtruder be;

	public static NExtruderMenu makeServer(MenuType<?> type, int id, Inventory player, NBEExtruder be)
	{
		return new NExtruderMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NExtruderMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.getLevel();
		NBEExtruder be = BlockHelper.castTileEntity(l, pos, NBEExtruder.class).get();
		return new NExtruderMenu(clientCtx(type, id), player, be);
	}
	
	private NExtruderMenu(MenuContext ctx, Inventory inventoryPlayer, NBEExtruder be) 
	{
		super(ctx);
		this.be = be;
	
		IItemHandler inv = ItemHelper.getItemHandler(be).
				map(handler -> handler).
				orElse(new ItemStackHandler(1));

		this.addSlot(new Output(inv, 0, 0, 87, 45).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_OUPUT).setActive(true));
		
		this.ownSlotCount = 1;
		
		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
	}
	
	@Override
	public void receiveMessageFromScreen(CompoundTag tag) 
	{
		super.receiveMessageFromScreen(tag);
		
		ServerPlayer player = usingPlayers.get(0);
		ServerLevel level = player.getLevel();
		BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
		if (tag.contains(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Extruder.MODE))
		{
			BlockHelper.castTileEntity(level, pos, NBEExtruder.class).ifPresent(tile -> 
			{
				tile.setMode(tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Extruder.MODE));
				tile.setChanged();
			});
		}
	}
}


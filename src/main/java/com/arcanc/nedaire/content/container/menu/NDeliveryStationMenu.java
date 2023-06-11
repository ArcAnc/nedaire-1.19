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
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FilterHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
		Level l = p.level();
		NBEDeliveryStation be = BlockHelper.castTileEntity(l, pos, NBEDeliveryStation.class).get();
		return new NDeliveryStationMenu(clientCtx(type, id), player, be);
	}
	
	private NDeliveryStationMenu(MenuContext ctx, Inventory inventoryPlayer, NBEDeliveryStation be) 
	{
		super(ctx);
		this.be = be;
	
		addItemFilterSlots(FilterHelper.getItemFilter(be), 62, 15);
		
		addFluidFilterSlots(FilterHelper.getFluidFilter(be), 62, 15);

//		this.ownSlotCount = NBEDeliveryStation.INVENTORY_SIZE * 2;
		
		layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
	}
	
	@Override
	public void receiveMessageFromScreen(CompoundTag tag) 
	{
		super.receiveMessageFromScreen(tag);
		
		ServerPlayer player = usingPlayers.get(0);
		ServerLevel level = player.serverLevel();
		BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
		if (tag.contains(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.MODE))
		{
			BlockHelper.castTileEntity(level, pos, NBEDeliveryStation.class).ifPresent(tile -> 
			{
				tile.setMode(tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.MODE));
				tile.setChanged();
			});
		}
	}
}

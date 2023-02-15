/**
 * @author ArcAnc
 * Created at: 2023-02-14
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEGeneratorSolar;
import com.arcanc.nedaire.util.helpers.BlockHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

public class NGeneratorSolarMenu extends NContainerMenu 
{
	public final NBEGeneratorSolar be;

	public static NGeneratorSolarMenu makeServer(MenuType<?> type, int id, Inventory player, NBEGeneratorSolar be)
	{
		return new NGeneratorSolarMenu(blockCtx(type, id, be), player, be);
	}
	
	public static NGeneratorSolarMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
	{
		Player p = player.player;
		Level l = p.getLevel();
		NBEGeneratorSolar be = BlockHelper.castTileEntity(l, pos, NBEGeneratorSolar.class).get();
		return new NGeneratorSolarMenu(clientCtx(type, id), player, be);
	}
	
	private NGeneratorSolarMenu(MenuContext ctx, Inventory player, NBEGeneratorSolar be) 
	{
		super(ctx);
		this.be = be;
		
		layoutPlayerInventorySlots(player, 13, 90);
	}

}

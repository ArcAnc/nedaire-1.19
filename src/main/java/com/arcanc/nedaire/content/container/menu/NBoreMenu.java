/**
 * @author ArcAnc
 * Created at: 28.04.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.container.menu;

import com.arcanc.nedaire.content.block.entities.NBEBore;
import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class NBoreMenu extends NContainerMenu
{
    public final NBEBore be;

    public static NBoreMenu makeServer(MenuType<?> type, int id, Inventory player, NBEBore be)
    {
        return new NBoreMenu(blockCtx(type, id, be), player, be);
    }

    public static NBoreMenu makeClient(MenuType<?> type, int id, Inventory player, BlockPos pos)
    {
        Player p = player.player;
        Level l = p.level();
        NBEBore be = BlockHelper.castTileEntity(l, pos, NBEBore.class).get();
        return new NBoreMenu(clientCtx(type, id), player, be);
    }

    private NBoreMenu(MenuContext ctx, Inventory inventoryPlayer, NBEBore be)
    {
        super(ctx);
        this.be = be;

        IItemHandler inv = ItemHelper.getItemHandler(be).
                map(handler -> handler).
                orElse(new ItemStackHandler(1));

        this.addSlot(new NSlot(inv, 0, 0, 87, 35, stack -> stack.is(ItemTags.PICKAXES)).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_OUPUT).setActive(true));

        ownSlotCount = 1;

        layoutPlayerInventorySlots(inventoryPlayer, 13, 90);
    }
}

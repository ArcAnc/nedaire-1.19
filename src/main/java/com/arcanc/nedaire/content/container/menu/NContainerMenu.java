/**
 * @author ArcAnc
 * Created at: 2022-11-03
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.arcanc.nedaire.content.block.entities.NBERedstoneSensitive;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IFluidFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IItemFilter;
import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.content.container.sync.GenericContainerData;
import com.arcanc.nedaire.content.container.sync.GenericDataSerializers.DataPair;
import com.arcanc.nedaire.content.network.NNetworkEngine;
import com.arcanc.nedaire.content.network.messages.MessageContainerData;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FilterHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.NetworkDirection;

public abstract class NContainerMenu extends AbstractContainerMenu 
{
	private final List<GenericContainerData<?>> genericData = new ArrayList<>();
	private final List<ServerPlayer> usingPlayers = new ArrayList<>();
	private final Runnable setChanged;
	private final Predicate<Player> isValid;
	public int ownSlotCount;
	
	protected NContainerMenu(MenuContext ctx) 
	{
		super(ctx.type(), ctx.id());
		this.setChanged = ctx.setChanged();
		this.isValid = ctx.isValid();
	}
	
	/**
	 * Slot index = 10
	 */
	protected void addItemFilterSlots(LazyOptional<IItemFilter> filter, int xPos, int yPos)
	{
		filter.ifPresent(fil -> 
		{
			for (int q = 0; q < fil.getContent().getSlots(); q++)
			{
				this.addSlot(new NSlot.ItemHandlerGhost(fil.getContent(), 10, q, xPos + (q % 3) * 18, yPos + (q / 3) * 18).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_STANDART).setActive(false));
			}
		});
	}
	
	/**
	 * Slot index = 11
	 */
	protected void addFluidFilterSlots(LazyOptional<IFluidFilter> filter, int xPos, int yPos)
	{
		filter.ifPresent(fil -> 
		{
			for (int q = 0; q < fil.getContent().getSlots(); q++)
			{
				this.addSlot(new NSlot.ItemHandlerGhost(fil.getContent(), 11, q, xPos + (q % 3) * 18, yPos + (q / 3) * 18).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_STANDART).setActive(false));
			}
		});
	}
	
    private int addSlotRange(Inventory handler, int index, int x, int y, int amount, int dx) 
    {
        for (int i = 0; i < amount ; i++) 
        {
            addSlot(new Slot(handler, index, x, y).setBackground(InventoryMenu.BLOCK_ATLAS, NSlot.BACKGROUND_STANDART));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Inventory handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) 
    {
        for (int j = 0 ; j < verAmount ; j++) 
        {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void layoutPlayerInventorySlots(Inventory playerInv, int leftCol, int topRow) 
    {
        // Player inventory
        addSlotBox(playerInv, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInv, 0, leftCol, topRow, 9, 18);
    }
	
	@Override
	public void broadcastChanges() 
	{
		super.broadcastChanges();
		List<Pair<Integer, DataPair<?>>> toSync = new ArrayList<>();
		for (int q = 0; q < genericData.size(); q++)
		{
			GenericContainerData<?> data = genericData.get(q);
			if (data.needsUpdate())
				toSync.add(Pair.of(q, data.dataPair()));
		}
		if (!toSync.isEmpty())
			for (ServerPlayer player : usingPlayers)
				NNetworkEngine.packetHandler.sendTo(new MessageContainerData(toSync), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
				
	}
	
	public void receiveSync(List<Pair<Integer, DataPair<?>>> synced)
	{
		for (Pair<Integer, DataPair<?>> syncElement : synced)
			genericData.get(syncElement.getFirst()).processSync(syncElement.getSecond().data());
	}
	
	@Override
	public void clicked(int id, int dragType, ClickType clickType, Player player) 
	{
		Slot slot = id < 0 ? null : this.slots.get(id);
		if (!(slot instanceof NSlot.ItemHandlerGhost))
		{
			super.clicked(id, dragType, clickType, player);
			return;
		}
		ItemStack stackSlot = slot.getItem();
		
		if (dragType == 2)
			slot.set(ItemStack.EMPTY);
		else if(dragType == 0 || dragType == 1)
		{
			ItemStack stackHeld = getCarried();
			
			int amount = Math.min(slot.getMaxStackSize(), stackHeld.getCount());
			if (dragType == 1)
				amount = 1;
			if (stackSlot.isEmpty())
			{
				if(!stackHeld.isEmpty() && slot.mayPlace(stackHeld))
					slot.set(ItemHelper.copyStackWithAmount(stackHeld, amount));
			}
			else if (stackHeld.isEmpty())
				slot.set(ItemStack.EMPTY);
			else if(slot.mayPlace(stackHeld))
			{
				if (ItemStack.isSame(stackSlot, stackHeld))
					stackSlot.grow(amount);
				else
					slot.set(ItemHelper.copyStackWithAmount(stackHeld, amount));
			}
			if (stackSlot.getCount() > slot.getMaxStackSize())
				stackSlot.setCount(slot.getMaxStackSize());
		}
		else if(dragType == 5)
		{
			ItemStack stackHeld = getCarried();
			int amount = Math.min(slot.getMaxStackSize(), stackHeld.getCount());
				if(!slot.hasItem())
					slot.set(ItemHelper.copyStackWithAmount(stackHeld, amount));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slot) 
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slotObject = this.slots.get(slot);
		if (slotObject != null && slotObject.hasItem())
		{
			ItemStack stack1 = slotObject.getItem();
			stack = stack1.copy();
			
			if (slot < ownSlotCount)
			{
				if (!this.moveItemStackTo(stack1, ownSlotCount, this.slots.size(), true))
					return ItemStack.EMPTY;
			}
			else if(!this.moveItemStackToWithMayPlace(stack1, 0, ownSlotCount))
				return ItemStack.EMPTY;
			if (stack1.isEmpty())
				slotObject.set(ItemStack.EMPTY);
			else
				slotObject.setChanged();
		}
		return stack;
	}
	
	protected boolean moveItemStackToWithMayPlace(ItemStack pStack, int pStartIndex, int pEndIndex)
	{
		boolean inAllowedRange = true;
		int allowedStart = pStartIndex;
		for (int q = pStartIndex; q < pEndIndex; q++)
		{
			boolean mayPlace = this.slots.get(q).mayPlace(pStack);
			if (inAllowedRange && !mayPlace)
			{
				if (moveItemStackTo(pStack, allowedStart, q, false))
					return true;
				inAllowedRange = false;
			}
			else if (!inAllowedRange && mayPlace)
			{
				allowedStart = q;
				inAllowedRange = true;
			}
		}
		return inAllowedRange && moveItemStackTo(pStack, allowedStart, pEndIndex, false); 
	}
	
	public void receiveMessageFromScreen(CompoundTag tag)
	{
		if (tag.contains(NDatabase.Blocks.BlockEntities.TagAddress.Machines.RedstoneSensitive.REDSTONE_MOD))
		{
			ServerPlayer player = usingPlayers.get(0);
			ServerLevel level = player.getLevel();
			BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
			BlockHelper.castTileEntity(level, pos, NBERedstoneSensitive.class).ifPresent(tile -> 
			{
				tile.setCurrentRedstoneMod(tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.RedstoneSensitive.REDSTONE_MOD));
				tile.setChanged();
			});
		}
		
		if (tag.contains(NDatabase.Capabilities.Filter.MAX_EXTRACTING_STACK))
		{
			ServerPlayer player = usingPlayers.get(0);
			ServerLevel level = player.getLevel();
			BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
			BlockEntity tile = BlockHelper.getTileEntity(level, pos);
			FilterHelper.getItemFilter(tile).ifPresent( filter -> filter.setExtracion(tag.getInt(NDatabase.Capabilities.Filter.MAX_EXTRACTING_STACK)));
		}
		
		if (tag.contains(NDatabase.Capabilities.Filter.MAX_AMOUNT_IN))
		{
			ServerPlayer player = usingPlayers.get(0);
			ServerLevel level = player.getLevel();
			BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
			BlockEntity tile = BlockHelper.getTileEntity(level, pos);
			FilterHelper.getItemFilter(tile).ifPresent( filter -> filter.setMaxInInventory(tag.getInt(NDatabase.Capabilities.Filter.MAX_AMOUNT_IN)));
		}
		
		if (tag.contains(NDatabase.Capabilities.Filter.WHITELIST))
		{
			ServerPlayer player = usingPlayers.get(0);
			ServerLevel level = player.getLevel();
			BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
			BlockEntity tile = BlockHelper.getTileEntity(level, pos);
			FilterHelper.getItemFilter(tile).ifPresent( filter -> filter.setWhitelist(tag.getBoolean(NDatabase.Capabilities.Filter.WHITELIST)));
		}
		
		if (tag.contains(NDatabase.Capabilities.Filter.MOD_OWNER))
		{
			ServerPlayer player = usingPlayers.get(0);
			ServerLevel level = player.getLevel();
			BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
			BlockEntity tile = BlockHelper.getTileEntity(level, pos);
			FilterHelper.getItemFilter(tile).ifPresent( filter -> filter.setModOwner(tag.getBoolean(NDatabase.Capabilities.Filter.MOD_OWNER)));
		}
		
		if (tag.contains(NDatabase.Capabilities.Filter.CHECK_TAG))
		{
			ServerPlayer player = usingPlayers.get(0);
			ServerLevel level = player.getLevel();
			BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
			BlockEntity tile = BlockHelper.getTileEntity(level, pos);
			FilterHelper.getItemFilter(tile).ifPresent( filter -> filter.setCheckTag(tag.getBoolean(NDatabase.Capabilities.Filter.CHECK_TAG)));
		}
	}
	
	@Override
	public void removed(Player player) 
	{
		super.removed(player);
		setChanged.run();
	}
	
	@Override
	public boolean stillValid(Player player) 
	{
		return isValid.test(player);
	}
	
	public static void onContainerOpen(PlayerContainerEvent.Open event)
	{
		if (event.getContainer() instanceof NContainerMenu cont && event.getEntity() instanceof ServerPlayer serverPlayer)
		{
			cont.usingPlayers.add(serverPlayer);
			List<Pair<Integer, DataPair<?>>> list = new ArrayList<>();
			for (int q = 0; q < cont.genericData.size(); q++)
				list.add(Pair.of(q, cont.genericData.get(q).dataPair()));
			NNetworkEngine.packetHandler.sendTo(new MessageContainerData(list), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
		}
	}
	
	public static void onContainerClosed(PlayerContainerEvent.Close event)
	{
		if (event.getContainer() instanceof NContainerMenu cont && event.getEntity() instanceof ServerPlayer serverPlayer)
			cont.usingPlayers.remove(serverPlayer);
	}
	
	public static MenuContext blockCtx(@Nullable MenuType<?> pMenuType, int pContainerId, BlockEntity be)
	{
		return new MenuContext(pMenuType, pContainerId, () -> 
		{
			be.setChanged();
		}, 
		p -> 
		{
			BlockPos pos = be.getBlockPos();
			Level level = be.getLevel();
			if (level == null || level.getBlockEntity(pos) != be)
				return false;
			else
				return !(p.distanceToSqr(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d) > 64.0d);
		});
	}
	
	public static MenuContext itemCtx(@Nullable MenuType<?> pMenyType, int pContainerId, Inventory playerInv, EquipmentSlot slot, ItemStack stack)
	{
		return new MenuContext(pMenyType, pContainerId, () -> 
		{}, 
		p -> 
		{
			if (p != playerInv.player)
				return false;
			return ItemStack.isSame(p.getItemBySlot(slot), stack);
		}); 
	}
	
	public static MenuContext clientCtx(@Nullable MenuType<?> pMenuType, int pContainerId)
	{
		return new MenuContext(pMenuType, pContainerId, () -> {}, p -> true);
	}
	
	protected record MenuContext(MenuType<?> type, int id, Runnable setChanged, Predicate<Player> isValid)
	{}
	
}

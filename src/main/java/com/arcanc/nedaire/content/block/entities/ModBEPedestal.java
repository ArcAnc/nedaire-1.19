/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.registration.ModRegistration;
import com.arcanc.nedaire.util.database.ModDatabase;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.inventory.ItemStackHolder;
import com.arcanc.nedaire.util.inventory.ModSimpleItemStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class ModBEPedestal extends ModBaseBlockEntity implements IInventoryCallback
{

	protected ModSimpleItemStorage inv;
	protected final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inv);

	
	public ModBEPedestal(BlockPos pos, BlockState state) 
	{
		super(ModRegistration.RegisterBlockEntities.BE_PEDESTAL.get(), pos, state);

		inv = new ModSimpleItemStorage(this).addSlots(
				Stream.generate(
						()-> new ItemStackHolder(1)).
				limit(9).
				collect(Collectors.toList())
				);
	}

	public ItemStack usePedestal(Player player, ItemStack stack)
	{
		if (!player.isCrouching())
		{
			if (!stack.isEmpty() && ItemHelper.hasEmptySpace(itemHandler))
			{
				return addItems(player, stack);
			}
		}
		return removeItem(player, stack);
	}
	
	private ItemStack addItems (Player player, ItemStack stack)
	{
		if (player != null && !getLevel().isClientSide())
		{
			if(stack.isEmpty())
				return stack;
			ItemStack retStack = ItemStack.EMPTY; 
			for (int q = 0; q < inv.getSlots(); q++)
			{
				retStack = addItem(player, stack, q);
				if (!retStack.equals(stack, true))
				{
					return retStack;
				}
			}
		}
		return stack;

	}
	
	private ItemStack addItem(Player player, ItemStack stack, int slot)
	{
		ItemStack newStack = inv.insertItem(slot, stack, false);
		if (!stack.equals(newStack, true))
		{
			if (player.isCreative())
			{
				newStack.setCount(stack.getCount());
				return newStack;
			}
			else
			{
				player.setItemInHand(InteractionHand.MAIN_HAND, newStack);
				return newStack;
			}
		}
		return stack;
	}	
	
	private ItemStack removeItem(Player player, ItemStack stack)
	{
		if (player != null && !getLevel().isClientSide())
		{
			for (int q = inv.getSlots()-1; q > -1; --q)
			{
				ItemStack extracted = inv.extractItem(q, inv.getSlotLimit(q), false);
				
				if (!extracted.isEmpty())
				{
					player.addItem(extracted);
					break;
				}
			}
		}
		return stack;
	}
	
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		inv.deserializeNBT(tag.getCompound(ModDatabase.Capabilities.ItemHandler.TAG_LOCATION));
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		tag.put(ModDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
	}
	
	@Override
	public void invalidateCaps() 
	{
		itemHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) 
	{
		if (cap == ItemHelper.itemHandler)
		{
			return itemHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onInventoryChange(int slot) 
	{
		setChanged();
	}

}

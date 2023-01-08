/**
 * @author ArcAnc
 * Created at: 2022-04-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.inventory.ItemStackHolder;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;

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

public class NBEHolder extends NBaseBlockEntity implements IInventoryCallback 
{

	protected NSimpleItemStorage inv;
	protected final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inv);

	public NBEHolder(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_HOLDER.get(), pos, state);
		inv = new NSimpleItemStorage(this).addSlot(new ItemStackHolder(1));
	}
	
	public ItemStack useHolder(Player player, ItemStack stack)
	{
		if (!player.isCrouching())
		{
			if (!stack.isEmpty() && ItemHelper.hasEmptySpace(itemHandler))
			{
				return addItem(player, stack);
			}
		}
		return removeItem(player, stack);
	}
	
	private ItemStack addItem(Player player, ItemStack stack)
	{
		if (player != null && !getLevel().isClientSide())
		{
			if(stack.isEmpty())
				return stack;
			ItemStack newStack = inv.insertItem(0, stack, false);
			if (!stack.equals(newStack, true))
			{
				if (player.isCreative())
				{
					return stack;
				}
				else
				{
					player.setItemInHand(InteractionHand.MAIN_HAND, newStack);
					return newStack;
				}
			}
		}
		return stack;
	}
	
	private ItemStack removeItem(Player player, ItemStack stack)
	{
		if (player != null && !getLevel().isClientSide())
		{
			ItemStack extracted = inv.extractItem(0, 1, false);
			
			if (!extracted.isEmpty())
			{
				player.addItem(extracted);
			}
		}
		return stack;
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
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

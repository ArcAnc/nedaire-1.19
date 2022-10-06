/**
 * @author ArcAnc
 * Created at: 2022-06-24
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.module.jewelry.capability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.item.ItemInterfaces.IGemItem;
import com.arcanc.nedaire.util.database.ModDatabase;
import com.arcanc.nedaire.util.helpers.SocketHelper;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class SocketStorage implements ISocket
{
	
	protected boolean activated = false;
	protected NonNullList<ItemStack> stacks;
	protected int maxSize;
	
	public SocketStorage()
	{
		this(0, 1);
	}
	
	public SocketStorage(int size, int maxSize)
	{
		stacks =  NonNullList.withSize(size, ItemStack.EMPTY);
		this.maxSize = maxSize;
	}
	
    public SocketStorage(NonNullList<ItemStack> stacks)
    {
        this.stacks = stacks;
    }
	
    public SocketStorage setSize (int count)
    {
    	stacks = NonNullList.withSize(count, ItemStack.EMPTY);
    	return this;
    }
    
    public SocketStorage setMaxSize (int count)
    {
   		this.maxSize = count;
    	return this;
    }
    
	@Override
	public int getSlots() 
	{
        return stacks.size();
    }
	
	public void active()
	{
		activated = true;
	}
	
	@Override
	public boolean isActivated() 
	{
		return activated;
	}


	@Override
	public void setStackInSlot(int slot, @NotNull ItemStack stack) 
	{
		if (!activated)
			return;
        validateSlotIndex(slot);
        validateStack(stack);
        this.stacks.set(slot, stack);
        onContentsChanged(slot);		
	}

	@Override
	public @NotNull ItemStack getStackInSlot(int slot) 
	{
        if (!activated)
        	return ItemStack.EMPTY;
		validateSlotIndex(slot);
        return this.stacks.get(slot);	
    }

	@Override
	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) 
	{
        if (!activated)
        	return stack;
		if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        if (!existing.isEmpty())
        {
        	return stack;
        }

        if (!simulate)
        {
            if (existing.isEmpty())
            {
                this.stacks.set(slot, stack);
                onContentsChanged(slot);
            }
        }

        return ItemStack.EMPTY;
	}

	@Override
	public @NotNull ItemStack extractItem(int slot, boolean simulate) 
	{
        if (activated)
        	return ItemStack.EMPTY;
		validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        if (existing.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else 
        {
        	if (!simulate)
        	{
                this.stacks.set(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
                return existing;
        	}
        	else
        		return existing.copy();
        }
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) 
	{
		return activated && stack.getItem() instanceof IGemItem;
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (!stacks.get(i).isEmpty())
            {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("slot", i);
                stacks.get(i).save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("items", nbtTagList);
        nbt.putInt("size", stacks.size());
        nbt.putBoolean("active", activated);
        nbt.putInt("max_size", maxSize);
        return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
        setSize(nbt.contains("size", Tag.TAG_INT) ? nbt.getInt("size") : stacks.size());
        this.maxSize = nbt.getInt("max_size");
        this.activated = nbt.getBoolean("active");
        ListTag tagList = nbt.getList("items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("slot");

            if (slot >= 0 && slot < stacks.size())
            {
                stacks.set(slot, ItemStack.of(itemTags));
            }
        }
        onLoad();
		
	}
	
    protected void validateSlotIndex(int slot)
    {
        if (slot < 0 || slot >= stacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }
    
    protected void validateStack(ItemStack stack)
    {
    	if (!(stack.getItem() instanceof IGemItem))
    		throw new RuntimeException("Stack " + stack + " can't be placed in Socket");
    }

    protected void onLoad()
    {

    }

    protected void onContentsChanged(int slot)
    {

    }
    
    public static void attachCapability(final AttachCapabilitiesEvent<ItemStack> event)
    {
    	Item item = event.getObject().getItem();
    	if (item instanceof ArmorItem || item instanceof HorseArmorItem)
    	{
    		event.addCapability(new ResourceLocation(ModDatabase.Capabilities.Socket.TAG_LOCATION), new ICapabilityProvider() 
    		{
				private SocketStorage storage = new SocketStorage();
    			
				@Override
				public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
				{
					if (cap == SocketHelper.socketHandler)
						return LazyOptional.of(() -> storage).cast();
					return LazyOptional.empty();
				}
			});
    	}
    }
}

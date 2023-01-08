/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.google.common.base.Preconditions;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockInterfaces 
{
	public interface IInventoryCallback
	{
	    default void onInventoryChange(int slot) 
	    {

	    }

	    default void onTankChange(int tank) 
	    {

	    }
	    
	    default void onVimChange ()
	    {
	    	
	    }

	    default boolean clearSlot(int slot) 
	    {
	        return false;
	    }

	    default boolean clearTank(int tank) 
	    {
	        return false;
	    }
	    
	    /**
	     * FIXME: add essence or another energy which will be implemented soon
	     */
	}
	/*
	 * taken from Immersive Engineering*/
	public interface IInteractionObjectN<T extends BlockEntity & IInteractionObjectN<T>> extends MenuProvider
	{
		@Nullable
		T getBE();

		BEContainer<? super T, ?> getContainerType();

		boolean canUseGui(Player player);

		default boolean isValid()
		{
			return getBE()!=null;
		}

		@Nonnull//Super is annotated nullable, but Forge assumes Nonnull in at least one place
		@Override
		default AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity)
		{
			T tile = getBE();
			Preconditions.checkNotNull(tile);
			BEContainer<? super T, ?> type = getContainerType();
			return type.create(id, playerInventory, tile);
		}

		@Override
		default Component getDisplayName()
		{
			return Component.literal("");
		}
	}

}

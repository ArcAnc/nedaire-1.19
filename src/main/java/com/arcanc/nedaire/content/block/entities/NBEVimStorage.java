/**
 * @author ArcAnc
 * Created at: 2022-10-29
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class NBEVimStorage extends NBaseBlockEntity implements IInventoryCallback
{

	protected VimStorage energy;
	protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
	
	public NBEVimStorage(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_VIM_STORAGE.get(), pos, state);

		
		this.energy = VimStorage.newConfig(this).setMaxEnergy(5000).setEnergy(0).build();
	}

	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		energy.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Vim.TAG_LOCATION));
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		tag.put(NDatabase.Capabilities.Vim.TAG_LOCATION, energy.serializeNBT());
	}

	@Override
	public void invalidateCaps() 
	{
		energyHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		if (cap == VimHelper.vimHandler)
		{
			Direction dir = getBlockState().getValue(BlockHelper.BlockProperties.FACING); 
			if ((dir.getAxis().isHorizontal() && dir.getAxis() != side.getAxis()) || side == null)
			{
				return energyHandler.cast();
			}
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onVimChange() 
	{
		setChanged();
	}
}

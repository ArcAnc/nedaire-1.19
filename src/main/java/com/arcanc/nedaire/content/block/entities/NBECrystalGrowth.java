/**
 * @author ArcAnc
 * Created at: 2023-03-07
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.VimHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class NBECrystalGrowth extends NBaseBlockEntity implements IInventoryCallback, ModServerTickerBlockEntity
{

	private static final int PERIOD = 100;
	private static final int PERIOD_EMPTY = 1000;
	private static final int PER_USE = 100;

	private static final int MINX = -2;
	private static final int MINY = -2;
	private static final int MINZ = -2;

	private static final int MAXX = 3;
	private static final int MAXY = 3;
	private static final int MAXZ = 3;
	
	protected VimStorage energy;
	protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
	
	public NBECrystalGrowth(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_CRYSTAL_GROWTH.get(), pos, state);

		this.energy = VimStorage.newConfig(this).setMaxEnergy(300).setEnergy(0).build();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tickServer() 
	{
		boolean empty = false;
		
		if (energy.getEnergyStored() < PER_USE)
		{
			empty = true;
		}
		
		if (empty ? getLevel().getGameTime() % PERIOD_EMPTY == 0 : getLevel().getGameTime() % PERIOD == 0)
		{
			if (!empty)
			{
				energy.extract(PER_USE, false);
			}
			BlockPos pos = getBlockPos();
			for (int x = pos.getX() + MINX; x < pos.getX() + MAXX; x++)
			{
				for (int y = pos.getY() + MINY; y < pos.getY() + MAXY; y++)
				{
					for(int z = pos.getZ() + MINZ; z < pos.getZ() + MAXZ; z++)
					{
						BlockPos curPos = new BlockPos(x, y, z);
						BlockState state = getLevel().getBlockState(curPos);
						Block block = state.getBlock();
						ServerLevel lvl = (ServerLevel)level;
						if (block instanceof IPlantable)
						{
							RandomSource random = lvl.random;
							block.randomTick(state, lvl, curPos, random);

							lvl.levelEvent(1505, curPos, 0);
						}
					}
				}
			}
		}
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
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		if (cap == VimHelper.vimHandler)
		{
			return energyHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void invalidateCaps() 
	{
		energyHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public void onVimChange() 
	{
		setChanged();
	}
}

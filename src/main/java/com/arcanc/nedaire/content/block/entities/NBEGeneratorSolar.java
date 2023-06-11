/**
 * @author ArcAnc
 * Created at: 2023-02-14
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.BlockInterfaces.INInteractionObject;
import com.arcanc.nedaire.content.block.entities.ticker.NServerTickerBlockEntity;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.VimHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NBEGeneratorSolar extends NBERedstoneSensitive implements IInventoryCallback, NServerTickerBlockEntity, INInteractionObject<NBEGeneratorSolar>
{

	private VimStorage energy;
	private final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
	
	private static final int PERIOD = 100;
	private static final int AMOUNT = 2;
	
	public NBEGeneratorSolar(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_GENERATOR_SOLAR.get(), pos, state);
		
		this.ports.put(Direction.DOWN, AccessType.OUTPUT);
		
		energy = VimStorage.newConfig(this).setMaxEnergy(6000).build();
	}

	@Override
	public void tickServer() 
	{
		if (isPowered())
		{
			if (getLevel().canSeeSky(getBlockPos()))
			{
				if (getLevel().getGameTime() % PERIOD == 0)
				{
					this.energy.add(AMOUNT, false);
				}
			}
		}
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		if (tag.contains(NDatabase.Capabilities.Vim.TAG_LOCATION))
		{
			energy.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Vim.TAG_LOCATION));
		}
	}
	
	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
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
			if (side == Direction.DOWN)
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
	
	@Override
	public NBEGeneratorSolar getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEGeneratorSolar, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.GENERATOR_SOLAR;
	}

	@Override
	public boolean canUseGui(Player player) 
	{
		return true;
	}
}

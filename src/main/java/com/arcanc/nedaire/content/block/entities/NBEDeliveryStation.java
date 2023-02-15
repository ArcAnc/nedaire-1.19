/**
 * @author ArcAnc
 * Created at: 2022-10-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInteractionObjectN;
import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.content.capabilities.filter.CapabilityFilter;
import com.arcanc.nedaire.content.capabilities.filter.FluidFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IFluidFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IItemFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IVimFilter;
import com.arcanc.nedaire.content.capabilities.filter.ItemFilter;
import com.arcanc.nedaire.content.capabilities.filter.VimFilter;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.google.common.base.Preconditions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class NBEDeliveryStation extends NBaseBlockEntity implements IInventoryCallback, ModServerTickerBlockEntity, IInteractionObjectN<NBEDeliveryStation>
{

	private final List<BlockPos> attachedEntities = new ArrayList<>();
	private static final int INVENTORY_SIZE = 9;
	
	/**
	 * 0 - transfer Items
	 * 1 - transfer Liquids
	 * 2 - transfer Vim
	 */
	private int mode = 0;
	
	private ItemFilter itemfilter;
	private final LazyOptional<IItemFilter> itemFilterHanlder = LazyOptional.of(() -> itemfilter);
	private FluidFilter fluidFilter;
	private final LazyOptional<IFluidFilter> fluidFilterHandler = LazyOptional.of(() -> fluidFilter);
	private VimFilter vimFilter;
	private final LazyOptional<IVimFilter> vimFilterHandler = LazyOptional.of(() -> vimFilter);
	
	
	public NBEDeliveryStation(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_DELIVERY_STATION.get(), pos, state);
		itemfilter = new ItemFilter(true, this, INVENTORY_SIZE);
	}

	@Override
	public void tickServer() 
	{
		
	}
	
	public IFilter<?, ?> chooseCorrentFilter ()
	{
		return switch (mode)
				{
					case 0 -> itemfilter;
					case 1 -> fluidFilter;
					case 2 -> vimFilter;
					default -> null;
				}; 
	}
	
	public boolean addTile(Level lvl, BlockPos pos)
	{
		Preconditions.checkNotNull(lvl);
		Preconditions.checkNotNull(pos);
		if(lvl.dimension().equals(getLevel().dimension()))
		{
			Optional<BlockEntity> tile = BlockHelper.castTileEntity(lvl, pos, BlockEntity.class);
			return tile.map( t -> 
			{
				return switch (mode) 
						{
							case 0 :
								yield ItemHelper.getItemHandler(t).map(handler -> 
								{
									return this.attachedEntities.add(pos);
								}).orElse(false);
							case 1 :
								yield FluidHelper.getFluidHandler(t).map(handler -> 
								{
									return this.attachedEntities.add(pos);
								}).orElse(false);
							case 2 :
								yield VimHelper.getVimHandler(t).map(hanlder -> 
								{
									return this.attachedEntities.add(pos);
								}).orElse(false);
							default:
								yield false;
						};
			}).orElse(false);
		}
		return false;
	}
	
	public boolean removeTile(BlockPos pos)
	{
		Preconditions.checkNotNull(pos);
		return attachedEntities.remove(pos);
	}
	
	public void checkTiles()
	{
		List<BlockPos> poses = new ArrayList<>(attachedEntities);
		
		poses = poses.stream().filter(pos -> 
		{
			return BlockHelper.castTileEntity(getLevel(), pos, BlockEntity.class).map(tile -> 
			{
				return switch (mode)
						{
							case 0 :
								yield ItemHelper.isItemHandler(tile);
							case 1 :
								yield FluidHelper.isFluidHandler(tile);
							case 2 :
								yield VimHelper.isVimHandler(tile);
							default:
								yield false;
						};
			}).orElse(false);
		}).toList();
		
		
		attachedEntities.clear();
		attachedEntities.addAll(poses);
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		
	}
	
	@Override
	public void invalidateCaps() 
	{
		itemFilterHanlder.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		if (cap == CapabilityFilter.FILTER_ITEM)
		{
			return itemFilterHanlder.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onInventoryChange(int slot) 
	{
		setChanged();
	}
	
	@Override
	public NBEDeliveryStation getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEDeliveryStation, ?> getContainerType() 
	{
		return null;
	}

	@Override
	public boolean canUseGui(Player player) 
	{
		return true;
	}
}

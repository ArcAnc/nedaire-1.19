/**
 * @author ArcAnc
 * Created at: 2023-02-22
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInteractionObjectN;
import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.FluidHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class NBEFluidStorage extends NBaseBlockEntity  implements IInventoryCallback, IInteractionObjectN<NBEFluidStorage> 
{

	protected FluidTank fluid;
	protected final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluid);
	
	public NBEFluidStorage(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_FLUID_STORAGE.get(), pos, state);

		
		this.fluid = new FluidTank(5000)
				{
					@Override
					protected void onContentsChanged() 
					{
						setChanged();
					}
				};
		
		this.fluid.fill(new FluidStack(Fluids.WATER, 1000), FluidAction.EXECUTE);
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		fluid.readFromNBT(tag.getCompound(NDatabase.Capabilities.FluidHandler.TAG_LOCATION));
	}
	
	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		tag.put(NDatabase.Capabilities.FluidHandler.TAG_LOCATION, fluid.writeToNBT(new CompoundTag()));
	}
	
	@Override
	public void invalidateCaps() 
	{
		fluidHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		if (cap == FluidHelper.fluidHandler)
		{
			if (side == null || (side != null && side.getAxis().isVertical()))
			{
				return fluidHandler.cast();
			}
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onTankChange(int tank) 
	{
		setChanged();
	}
	
	@Override
	public NBEFluidStorage getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEFluidStorage, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.FLUID_STORAGE;
	}

	@Override
	public boolean canUseGui(Player player) 
	{
		return true;
	}
}

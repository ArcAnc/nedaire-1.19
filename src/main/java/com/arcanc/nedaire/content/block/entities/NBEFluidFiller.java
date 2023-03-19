/**
 * @author ArcAnc
 * Created at: 2023-03-10
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInteractionObjectN;
import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.inventory.NManagedItemStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class NBEFluidFiller extends NBERedstoneSensitive implements IInventoryCallback, ModServerTickerBlockEntity, IInteractionObjectN<NBEFluidFiller>
{

	private static final BlockPos[] POSES = new BlockPos[] 
			{
				BlockPos.ZERO.north(),
				BlockPos.ZERO.south(),
				BlockPos.ZERO.east(),
				BlockPos.ZERO.west()
			};
	private final List<BlockPos> zone;
	private static final int PERIOD = 200;
	
	protected FluidTank fluid;
	protected final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluid);
	
	protected final NManagedItemStorage inv;
	
	public NBEFluidFiller(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_FLUID_FILLER.get(), pos, state);

		ports.put(Direction.UP, AccessType.FULL);
		ports.put(Direction.DOWN, AccessType.FULL);
		
		zone = Stream.of(POSES).map(p -> p.offset(getBlockPos())).toList();
	
		this.fluid = new FluidTank(5000)
		{
			@Override
			protected void onContentsChanged() 
			{
				setChanged();
			}
		};
		
		this.inv = new NManagedItemStorage(this).
				addInputSlot(1, stack -> FluidHelper.isFluidHandler(stack)).
				addOutputSlot(1).
				build();
	}

	@Override
	public void tickServer() 
	{
		boolean enabled = false;
		if(isPowered())
		{
			enabled = true;
			if (getLevel().getDayTime() % PERIOD == 0)
			{
				for (BlockPos pos : zone)
				{
					FluidState state = getLevel().getFluidState(pos);
					if (!state.isEmpty())
					{
						FluidStack stack = state.isSource() ? new FluidStack(state.holder().get(), 100) : FluidStack.EMPTY;
						if(!stack.isEmpty())
						{
							fluid.fill(stack, FluidAction.EXECUTE);
						}
					}
				}
			}
		}
		//fill buckets

		if(!inv.getInputHandler().getStackInSlot(0).isEmpty() && inv.getOutputHandler().getStackInSlot(0).isEmpty())
		{
			ItemStack stack = inv.getInputHandler().getStackInSlot(0).copy();
			
			FluidHelper.getFluidHandler(stack).ifPresent(handler -> 
			{
				handler.fill(fluid.drain(handler.fill(fluid.drain(Integer.MAX_VALUE, FluidAction.SIMULATE), FluidAction.SIMULATE), FluidAction.EXECUTE), FluidAction.EXECUTE);

				inv.getInputHandler().getSlot(0).modify(-1);
				inv.getOutputHandler().getSlot(0).setItemStack(handler.getContainer());
			});
		}
		
		for (Direction dir : Direction.values())
		{
			if (ports.get(dir) == AccessType.OUTPUT || ports.get(dir) == AccessType.FULL)
			{
				FluidHelper.getNearbyFluidHandler(this, dir).ifPresent(handler -> 
				{
					handler.fill(fluid.drain(handler.fill(fluid.drain(Integer.MAX_VALUE, FluidAction.SIMULATE), FluidAction.SIMULATE), FluidAction.EXECUTE), FluidAction.EXECUTE);
				});
			}
		}
		
		if (getBlockState().getValue(BlockHelper.BlockProperties.ENABLED) != enabled)
		{
			getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockHelper.BlockProperties.ENABLED, enabled), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
		tag.put(NDatabase.Capabilities.FluidHandler.TAG_LOCATION, fluid.writeToNBT(new CompoundTag()));
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
		fluid.readFromNBT(tag.getCompound(NDatabase.Capabilities.FluidHandler.TAG_LOCATION));
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		if (cap == FluidHelper.fluidHandler)
		{
			if (side == null || side == Direction.DOWN || side == Direction.UP)
			{
				return fluidHandler.cast();
			}
		}
		else if (cap == ItemHelper.itemHandler)
		{
			if (side == null)
			{
				return inv.getHandler(AccessType.FULL).cast();
			}
			else
			{
				return inv.getHandler(ports.get(side)).cast(); 
			}
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public NBEFluidFiller getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEFluidFiller, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.FLUID_FILLER;
	}

	@Override
	public boolean canUseGui(Player player) 
	{
		return true;
	}
	
	@Override
	public void onInventoryChange(int slot) 
	{
		setChanged();
	}
}

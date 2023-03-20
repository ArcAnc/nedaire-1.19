/**
 * @author ArcAnc
 * Created at: 2023-03-08
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
import com.arcanc.nedaire.content.block.entities.ticker.NServerTickerBlockEntity;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.fluid.NSimpleFluidStorage;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.IItemHandler;

public class NBEExtruder extends NBERedstoneSensitive implements IInventoryCallback, NServerTickerBlockEntity, IInteractionObjectN<NBEExtruder>
{
	
	protected int mode = 0;
	protected NSimpleFluidStorage fluid;
	protected final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluid);
	protected NSimpleItemStorage inv;
	protected final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inv);
	
	
	public NBEExtruder(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_EXTRUDER.get(), pos, state);

		for (Direction dir : Direction.values())
		{
			if (dir != Direction.SOUTH)
			{
				this.ports.put(dir, AccessType.FULL);
			}
		}
		
		fluid = new NSimpleFluidStorage(this).
				addTank(5000, stack -> stack.getFluid().isSame(Fluids.WATER)).
				addTank(5000, stack -> stack.getFluid().isSame(Fluids.LAVA));
		
		inv = new NSimpleItemStorage(this).addSlot(64, stack -> false);
	}
	
	@Override
	public void tickServer() 
	{
		boolean isLit = false;
		if (isPowered())
		{
			if(getLevel().getGameTime() % 20 == 0)
			{
				FluidStack water = fluid.getTank(0).getFluidStack();
				FluidStack lava = fluid.getTank(1).getFluidStack();
				if (!lava.isEmpty() && !water.isEmpty())
				{
					if (lava.getAmount() >= FluidType.BUCKET_VOLUME && water.getAmount() >= FluidType.BUCKET_VOLUME)
					{
						ItemStack output = getOutput(mode);
						
						if (inv.getStackInSlot(0).isEmpty() || inv.getStackInSlot(0).is(output.getItem()))
						{
							switch (mode) 
							{
								case 0:
									break;
								case 2:
									fluid.drain(1, FluidType.BUCKET_VOLUME, FluidAction.EXECUTE);
								case 1:
									fluid.drain(0, FluidType.BUCKET_VOLUME, FluidAction.EXECUTE);
									break;
								default: 
									break;
							}
							if (inv.getStackInSlot(0).isEmpty())
							{
								inv.setStackInSlot(output, 0);
							}
							else
							{
								inv.getSlot(0).modify(1);
							}
						}
					}
				}
			}
			isLit = true;
		}
		if (getBlockState().getValue(BlockHelper.BlockProperties.LIT) != isLit)
		{
			getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockHelper.BlockProperties.LIT, isLit), Block.UPDATE_CLIENTS);
		}
	}
	
	/**
	 * @return the Extruder working mode
	 * @implNote 
	 * 0 - Cobblestone
	 * 1 - Stone
	 * 2 - Obsidian
	 */
	public int getMode() 
	{
		return mode;
	}
	
	/**
	 * @param mode the working mode
	 * @implNote 
	 * 0 - Cobblestone
	 * 1 - Stone
	 * 2 - Obsidian
	 */
	public void setMode(int mode) 
	{
		this.mode = mode;
	}
	
	private ItemStack getOutput (int value)
	{
		return switch (mode) 
		{
			case 0:
				yield new ItemStack(Blocks.COBBLESTONE);
			case 1:
				yield new ItemStack(Blocks.STONE);
			case 2:
				yield new ItemStack(Blocks.OBSIDIAN);
			default: 
				yield ItemStack.EMPTY;
		};		
	}
	
	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
		
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
		tag.put(NDatabase.Capabilities.FluidHandler.TAG_LOCATION, fluid.serializeNBT());
		tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Extruder.MODE, mode);
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		
		mode = tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Extruder.MODE);
		fluid.deserializeNBT(tag.getCompound(NDatabase.Capabilities.FluidHandler.TAG_LOCATION));
		inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		if (cap == FluidHelper.fluidHandler)
		{
			if (side == null)
				return fluidHandler.cast();
			else if (ports.get(side) != AccessType.NONE)
				return fluidHandler.cast();
		}
		else if (cap == ItemHelper.itemHandler)
		{
			if (side == null)
				return itemHandler.cast();
			else if (ports.get(side) != AccessType.NONE)
				return itemHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void invalidateCaps() 
	{
		itemHandler.invalidate();
		fluidHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public NBEExtruder getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEExtruder, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.EXTRUDER;
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
	
	@Override
	public void onTankChange(int tank) 
	{
		setChanged();
	}
}

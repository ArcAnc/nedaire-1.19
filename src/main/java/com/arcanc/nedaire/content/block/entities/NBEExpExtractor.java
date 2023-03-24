/**
 * @author ArcAnc
 * Created at: 2023-03-24
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.block.BlockInterfaces.IInteractionObjectN;
import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.NServerTickerBlockEntity;
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
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class NBEExpExtractor extends NBERedstoneSensitive implements IInventoryCallback, NServerTickerBlockEntity, IInteractionObjectN<NBEExpExtractor>
{
	private static final AABB AABB = new AABB(0.0625d, 0.0625d, 0.0625d, 0.9375d, 1d, 0.9375d);
	private static final int PERIOD = 10;
	private static final float DAMAGE = 4;
	private static final int EXP_DRAIN = 200;
	
	private final AABB players;
	
	protected NManagedItemStorage inv;
	protected FluidTank fluid;
	protected final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluid);

	public NBEExpExtractor(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_EXP_EXTRACTOR.get(), pos, state);
	
		for (Direction dir : Direction.values())
		{
			if (dir.getAxis() == Axis.X || dir == Direction.DOWN)
			{
				this.ports.put(dir, AccessType.FULL);
			}
		}
		
		this.players = AABB.move(getBlockPos());
		
		this.fluid = new FluidTank(2000)
		{
			@Override
			protected void onContentsChanged() 
			{
				setChanged();
			}
		};
		
		this.inv = new NManagedItemStorage(this).
				addInputSlot(64, stack -> FluidHelper.isFluidHandler(stack)).
				addOutputSlot(1).
				build();
	}
	
	@Override
	public void tickServer() 
	{
		if (isPowered())
		{
			if (getLevel().getGameTime() % PERIOD == 0)
			{
				boolean isLit = false;
				if (FluidHelper.hasEmptySpace(fluid)) 
				{
					List<Player> entities = getLevel().getEntitiesOfClass(Player.class, players);
					if (!entities.isEmpty()) 
					{
						isLit = true;
						for (Player pl : entities) 
						{
							int exp = pl.totalExperience;
							if (exp > 0) 
							{
								int left = exp - fluid.fill(new FluidStack(NRegistration.RegisterFluids.EXPERIENCE.still().get(), Math.min(EXP_DRAIN, exp)), FluidAction.EXECUTE);

								pl.giveExperiencePoints(-left);
								pl.hurt(pl.damageSources().dryOut(), DAMAGE);
							}
						}
					}
				}
				if (getBlockState().getValue(BlockHelper.BlockProperties.LIT) != isLit)
				{
					getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockHelper.BlockProperties.LIT, isLit), Block.UPDATE_CLIENTS);
				}
			}
		}
		
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
	}
	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
		tag.put(NDatabase.Capabilities.FluidHandler.TAG_LOCATION, fluid.writeToNBT(new CompoundTag()));
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
		if (cap == ItemHelper.itemHandler)
		{
			if (side == null)
				return inv.getHandler(AccessType.FULL).cast();
			else 
				return inv.getHandler(ports.get(side)).cast();
		}
		else if (cap == FluidHelper.fluidHandler)
		{
			if (side == null)
				return fluidHandler.cast();
			else if (ports.get(side) != AccessType.NONE)
				return fluidHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void invalidateCaps() 
	{
		inv.invalidate();
		fluidHandler.invalidate();
		super.invalidateCaps();
	}

	@Override
	public BEContainer<NBEExpExtractor, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.EXP_EXTRACTOR;
	}

	@Override
	public boolean canUseGui(Player player) 
	{
		return true;
	}

	@Override
	public NBEExpExtractor getBE() 
	{
		return this;
	}
	
	@Override
	public void onInventoryChange(int slot) 
	{
		setChanged();
	}

}

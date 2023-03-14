/**
 * @author ArcAnc
 * Created at: 2023-03-12
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.recipe.NDiffuserRecipe;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;

public class NBEDiffuser extends NBERedstoneSensitive implements IInventoryCallback, ModServerTickerBlockEntity
{
	public int elapsedTime = 0;
	public int drainedFluid = 0;
	
	protected FluidTank fluid;
	protected final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluid);
	protected NSimpleItemStorage inv;
	protected final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inv	);

	public NBEDiffuser(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_DIFFUSER.get(), pos, state);
	
		this.fluid = new FluidTank(1000)
		{
			@Override
			protected void onContentsChanged() 
			{
				setChanged();
			}
		};
		
		this.inv = new NSimpleItemStorage(this).
				addSlot(1, stack -> true);
	}

	@Override
	public void tickServer() 
	{
		if (isPowered())
		{
			ItemStack stack = inv.getStackInSlot(0).copy();
			FluidStack fluidS = fluid.getFluidInTank(0).copy();
			NDiffuserRecipe.findRecipe(getLevel(), stack, fluidS).ifPresentOrElse(rec ->
			{
				float percent = rec.fluid.getAmount() / (float)rec.getTotalProcessTime();
				int perTick = percent < 1 ? 1 : (int)percent;
				
				FluidStack drained = fluid.drain(Math.min(fluid.drain(FluidHelper.copyFluidStackWithAmount(fluidS, perTick), FluidAction.SIMULATE).getAmount(), perTick), FluidAction.EXECUTE);
				
				elapsedTime++;
				drainedFluid += drained.getAmount();
				
				if (getLevel().getRandom().nextFloat() < 0.2f)
				{
					Vector3f particlePos = new Vector3f(getBlockPos().getX() + 0.2f + (getLevel().getRandom().nextFloat() * (14f/16f - 2f/16f)),
							getBlockPos().getY() + 0.4f + (getLevel().getRandom().nextFloat() * (0.9375f - 4f/16f)),
							getBlockPos().getZ() + 0.2f + (getLevel().getRandom().nextFloat() * (14f/16f - 2f/16f)));

				    ServerLevel lvl = (ServerLevel)getLevel();  
					
					for(int j = 0; j < lvl.players().size(); ++j) 
					{
				          ServerPlayer serverplayer = lvl.players().get(j);

				          lvl.sendParticles(serverplayer, ParticleTypes.BUBBLE_POP, true, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0, 0.4f, 0, 0.0002f); 
					}
				}
				
				if (elapsedTime >= rec.getTotalProcessTime() && drainedFluid >= rec.fluid.getAmount())
				{
					ItemStack output = rec.output.get();
					inv.setStackInSlot(output, 0);
					
					elapsedTime = 0;
					drainedFluid = 0;
				}
			}, 
			() -> 
			{
				elapsedTime = 0;
				drainedFluid = 0;
			});
		}
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		if (cap == FluidHelper.fluidHandler)
		{
			return fluidHandler.cast();
		}
		else if (cap == ItemHelper.itemHandler)
		{
			return itemHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
		tag.put(NDatabase.Capabilities.FluidHandler.TAG_LOCATION, fluid.writeToNBT(new CompoundTag()));
		
		tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Diffuser.ELAPSED_TIME, elapsedTime);
		tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Diffuser.DRAINED_FLUID, drainedFluid);
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		
		inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
		fluid.readFromNBT(tag.getCompound(NDatabase.Capabilities.FluidHandler.TAG_LOCATION));
		
		elapsedTime = tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Diffuser.ELAPSED_TIME);
		drainedFluid = tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Diffuser.DRAINED_FLUID);
	}
	
	@Override
	public void invalidateCaps() 
	{
		itemHandler.invalidate();
		fluidHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public void onInventoryChange(int slot) 
	{
		setChanged();
	}
}

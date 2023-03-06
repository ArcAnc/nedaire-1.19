/**
 * @author ArcAnc
 * Created at: 2023-03-06
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.block.BlockInterfaces.IInteractionObjectN;
import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.data.crafting.recipe.NCrusherRecipe;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.arcanc.nedaire.util.inventory.NManagedItemStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class NBECrusher extends NBERedstoneSensitive implements IInventoryCallback, ModServerTickerBlockEntity, IInteractionObjectN<NBECrusher> 
{
	public int usedEnergy = 0;
	public Optional<NCrusherRecipe> currentRecipe = Optional.empty();
	
	protected VimStorage energy;
	protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
	protected NManagedItemStorage inv;

	public NBECrusher(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_CRUSHER.get(), pos, state);
		
		for (Direction dir : Direction.values())
		{
			if (dir != Direction.SOUTH)
			{
				ports.put(dir, AccessType.FULL);
			}
		}
		
		this.energy = VimStorage.newConfig(this).setMaxEnergy(5000).setEnergy(0).build();
		
		this.inv = new NManagedItemStorage(this).
				addInputSlot(1, stack -> true).
				addOutputSlot(64).
				addOutputSlot(64).
				build();
	}

	@Override
	public void tickServer() 
	{
		if(isPowered())
		{
			if (currentRecipe.isEmpty())
			{
				currentRecipe = NCrusherRecipe.findRecipe(getLevel(), inv.getInputHandler().getStackInSlot(0)).map(rec -> 
				{
					Nedaire.getLogger().warn(rec.getId().toString());
					inv.getInputHandler().extractItem(0, 1, false);
					return rec;
				});
			}
			else
			{
				currentRecipe.ifPresent(rec -> 
				{
					if (inv.getOutputHandler().getStackInSlot(0).getCount() < inv.getOutputHandler().getSlot(0).getSizeLimit())
					{
						if (rec.secondaryOutputs.isEmpty() || (!rec.secondaryOutputs.isEmpty() && inv.getOutputHandler().getStackInSlot(1).getCount() < inv.getOutputHandler().getSlot(1).getSizeLimit()))
						{
							int perTick = rec.getTotalProcessEnergy() / rec.getTotalProcessTime();
							if (energy.getEnergyStored() >= perTick)
							{
								if (usedEnergy >= rec.getTotalProcessEnergy())
								{
									if (inv.getOutputHandler().getStackInSlot(0).isEmpty())
									{
										inv.getOutputHandler().setStackInSlot(rec.getItemOutputs().get(0).copy(), 0);
									}
									else
									{
										inv.getOutputHandler().getSlot(0).modify(rec.getItemOutputs().get(0).getCount());
									}
									
									if (!rec.secondaryOutputs.isEmpty() && getLevel().getRandom().nextFloat() < rec.secondaryOutputs.get(0).chance())
									{
										if (inv.getOutputHandler().getStackInSlot(1).isEmpty())
										{
											inv.getOutputHandler().setStackInSlot(rec.secondaryOutputs.get(0).stack().get(), perTick);
										}
										else
										{
											inv.getOutputHandler().getSlot(1).modify(rec.secondaryOutputs.get(0).stack().get().getCount());
										}
									}
									
									usedEnergy = 0;
									currentRecipe = Optional.empty();
								}
								else
								{
									usedEnergy += perTick;
								}
								energy.extract(perTick, false);
							}				
						}
					}
				});				
			}
		}
	}
	
	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);

		tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Crusher.USED_ENERGY, usedEnergy);
		tag.putString(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Crusher.CURRENT_RECIPE, currentRecipe.map(rec -> rec.getId().toString()).orElse("null"));
	
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
		tag.put(NDatabase.Capabilities.Vim.TAG_LOCATION, energy.serializeNBT());
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		
		inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
		energy.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Vim.TAG_LOCATION));
		
		usedEnergy = tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Crusher.USED_ENERGY);
		String str = tag.getString(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Crusher.CURRENT_RECIPE);
		currentRecipe = NCrusherRecipe.RECIPES.getRecipes(getLevel()).stream().filter(rec -> rec.getId().toString().equals(str)).findFirst();
	}
	
	@Override
	public void invalidateCaps() 
	{
		inv.invalidate();
		energyHandler.invalidate();
		super.invalidateCaps();
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
		else if (cap == VimHelper.vimHandler)
		{
			if (side == null)
				return energyHandler.cast();
			else if (ports.get(side) != AccessType.NONE)
				return energyHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public NBECrusher getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBECrusher, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.CRUSHER;
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
	public void onVimChange() 
	{
		setChanged();
	}

}

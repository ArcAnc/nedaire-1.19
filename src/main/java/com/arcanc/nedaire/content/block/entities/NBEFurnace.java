/**
 * @author ArcAnc
 * Created at: 2023-03-06
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
import com.arcanc.nedaire.data.crafting.CachedRecipeList;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.arcanc.nedaire.util.inventory.NManagedItemStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NBEFurnace extends NBERedstoneSensitive implements IInventoryCallback, NServerTickerBlockEntity, INInteractionObject<NBEFurnace>
{
	public static final CachedRecipeList<SmeltingRecipe> RECIPES = new CachedRecipeList<>(() -> RecipeType.SMELTING, SmeltingRecipe.class);

	public int usedEnergy = 0;
	public static final int RECIPE_MAX_ENERGY = 200;

	protected VimStorage energy;
	protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
	protected NManagedItemStorage inv;
	
	public NBEFurnace(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_FURNACE.get(), pos, state);
	
		for (Direction dir : Direction.values())
		{
			if (dir != Direction.SOUTH)
			{
				ports.put(dir, AccessType.FULL);
			}
		}
		
		this.energy = VimStorage.newConfig(this).setMaxEnergy(5000).setEnergy(0).build();
		
		this.inv = new NManagedItemStorage(this).
				addInputSlot(64, stack -> true).
				addOutputSlot(64).
				build();	
	}

	@Override
	public void tickServer() 
	{
		if (isPowered())
		{
			boolean isLit = false;
			
			Optional<SmeltingRecipe> rec = RECIPES.getRecipes(getLevel()).
					stream().
					filter(recipe -> recipe.getIngredients().get(0).test(inv.getInputHandler().getStackInSlot(0))).
					findFirst();
			
			isLit = rec.map(recipe -> 
			{
				if (inv.getOutputHandler().getStackInSlot(0).getCount() < inv.getOutputHandler().getSlot(0).getSizeLimit())
				{
					int perTick = RECIPE_MAX_ENERGY / recipe.getCookingTime();
					if (energy.getEnergyStored() >= perTick)
					{
						energy.extract(perTick, false);
						if (usedEnergy >= RECIPE_MAX_ENERGY)
						{
							inv.getInputHandler().getSlot(0).modify(-1);
							if (inv.getOutputHandler().getStackInSlot(0).isEmpty())
							{
								inv.getOutputHandler().setStackInSlot(recipe.getResultItem(getLevel().registryAccess()).copy(), 0);
							}
							else
							{
								inv.getOutputHandler().getSlot(0).modify(recipe.getResultItem(getLevel().registryAccess()).getCount());
							}
							usedEnergy = 0;
							return false;
						}
						else
						{
							usedEnergy += perTick;
							return true;
						}
					}
				}
				return false;
			}).orElseGet(() -> 
			{
				usedEnergy = 0;
				return false;
			});
			if (getBlockState().getValue(BlockHelper.BlockProperties.LIT) != isLit)
			{
				getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockHelper.BlockProperties.LIT, isLit), Block.UPDATE_CLIENTS);
			}
		}
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
		tag.put(NDatabase.Capabilities.Vim.TAG_LOCATION, energy.serializeNBT());
		tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Furnace.USED_ENERGY, usedEnergy);
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		
		inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
		energy.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Vim.TAG_LOCATION));
		
		usedEnergy = tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Furnace.USED_ENERGY);
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
	public void invalidateCaps() 
	{
		inv.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public NBEFurnace getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEFurnace, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.FURNACE;
	}

	@Override
	public boolean canUseGui(Player player) 
	{
		return true;
	}
	
	@Override
	public void onVimChange() 
	{
		setChanged();
	}
	
	@Override
	public void onInventoryChange(int slot) 
	{
		setChanged();
	}
}

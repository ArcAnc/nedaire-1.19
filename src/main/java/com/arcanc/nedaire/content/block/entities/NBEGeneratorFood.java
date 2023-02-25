/**
 * @author ArcAnc
 * Created at: 2023-02-23
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInteractionObjectN;
import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.arcanc.nedaire.util.inventory.ItemStackHolder;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class NBEGeneratorFood extends NBERedstoneSensitive implements IInventoryCallback, ModServerTickerBlockEntity, IInteractionObjectN<NBEGeneratorFood> 
{

	private final AABB suctionZone;
	
	protected VimStorage energy;
	protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
	protected NSimpleItemStorage inv;
	protected final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inv);
	
	public NBEGeneratorFood(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_GENERATOR_FOOD.get(), pos, state);
	
		for (Direction dir : Direction.values())
		{
			if (dir != Direction.SOUTH)
			{
				this.ports.put(dir, AccessType.FULL);
			}
		}
		
		Direction dir = getBlockState().getValue(BlockHelper.BlockProperties.FACING);
		suctionZone = new AABB(getBlockPos().offset(dir.getNormal()));
		
		this.energy = VimStorage.newConfig(this).setMaxEnergy(10000).setEnergy(0).build();
		this.inv = new NSimpleItemStorage(this).addSlot(new ItemStackHolder( stack -> stack.isEdible())); 
	}

	@Override
	public void tickServer() 
	{
		if (isPowered())
		{
			if (VimHelper.hasEmptySpace(energy))
			{
				ItemStack stack = inv.getStackInSlot(0);
				if (!stack.isEmpty() && stack.isEdible()) 
				{
					getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockHelper.BlockProperties.LIT, true), Block.UPDATE_CLIENTS);
					FoodProperties props = stack.getFoodProperties(null);
					int energy = props.getNutrition() + props.getNutrition() * props.getNutrition();
					if (!props.getEffects().isEmpty())
					{
						for (Pair<MobEffectInstance, Float> pair : props.getEffects())
						{
							energy += pair.getFirst().getEffect().isBeneficial() ? 10 * props.getNutrition() : 0;
						}
					}
					stack = ItemHelper.copyStackWithAmount(stack, stack.getCount() - 1);
					this.energy.add(energy, false);
					inv.setStackInSlot(stack, 0);
					if (stack.isEmpty())
					{
						getLevel().playSound(null, getBlockPos(), SoundEvents.PLAYER_BURP, SoundSource.BLOCKS, 1f, 1f);
						getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockHelper.BlockProperties.LIT, false), Block.UPDATE_CLIENTS);
					}
					else
					{
						if (getLevel().getGameTime() % 5 == 0)
						{
							getLevel().playSound(null, getBlockPos(), SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 0.5f, 1f);
						}
					}
					setChanged();
				}
			}
			else
			{
				if (getBlockState().getValue(BlockHelper.BlockProperties.LIT))
				{
					getLevel().playSound(null, getBlockPos(), SoundEvents.PLAYER_BURP, SoundSource.BLOCKS, 1f, 1f);
					getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockHelper.BlockProperties.LIT, false), Block.UPDATE_CLIENTS);
				}
			}
			
			gatherInput();
		}
	}
	
	private void gatherInput()
	{
		if (ItemHelper.hasEmptySpace(itemHandler))
		{
			List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class, suctionZone);
			if (!list.isEmpty())
			{
				ItemStack stack = inv.insertItem(0, list.get(0).getItem().copy(), false);
				list.get(0).setItem(stack);
			}
		}		
	}

	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		if (tag.contains(NDatabase.Capabilities.ItemHandler.TAG_LOCATION))
			inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
		if (tag.contains(NDatabase.Capabilities.Vim.TAG_LOCATION))
			energy.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Vim.TAG_LOCATION));
	}
	
	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
		tag.put(NDatabase.Capabilities.Vim.TAG_LOCATION, energy.serializeNBT());
	}
	
	@Override
	public void invalidateCaps() 
	{
		this.energyHandler.invalidate();
		this.itemHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		Direction dir = getBlockState().getValue(BlockHelper.BlockProperties.FACING);
		if (dir != side)
		{
			if (cap == VimHelper.vimHandler)
				return energyHandler.cast();
			if (cap == ItemHelper.itemHandler)
				return itemHandler.cast();
		}
		return super.getCapability(cap, side);
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

	@Override
	public NBEGeneratorFood getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEGeneratorFood, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.GENERATOR_FOOD;
	}

	@Override
	public boolean canUseGui(Player player) 
	{
		return true;
	}
}

/**
 * @author ArcAnc
 * Created at: 2022-10-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.NServerTickerBlockEntity;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.crafting.recipe.NCrusherRecipe;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.arcanc.nedaire.util.helpers.WorldHelper;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class NBEManualCrusher extends NBERedstoneSensitive implements IInventoryCallback, NServerTickerBlockEntity
{
	public long lastActiveTime;
	/*360 angles max*/
	public final int anglePerTick = 6; 
	public final int rotationTime = 10;
	public int currentAngle = 0;
	
	protected final int energyPerUse = 100;
	public int usedEnergy = 0;
	
	protected VimStorage energy;
	protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
	protected NSimpleItemStorage inv;
	protected final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inv);
	
	private final AABB zone;
	
	public NBEManualCrusher(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_MANUAL_CRUSHER.get(), pos, state);
	
		this.energy = VimStorage.newConfig(this).setMaxEnergy(100).setEnergy(0).build();
		this.inv = new NSimpleItemStorage(this, 1);
		
		zone = new AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 1.2f, pos.getZ() + 1);
	
		this.ports.put(Direction.DOWN, AccessType.INPUT);
	}

	public void power() 
	{
		if (!this.level.isClientSide())
		{
			energy.add(this.energyPerUse, false);
			lastActiveTime = getLevel().getGameTime();
			setChanged();
		}
	}

	@Override
	public void tickServer() 
	{
		if (lastActiveTime + rotationTime >= getLevel().getGameTime())
		{
			currentAngle += anglePerTick;
			if (currentAngle >= 360)
			{
				currentAngle = 0;
			}
			markDirty();
		}
		
		if (isPowered())
		{
			/*Recipe crafting*/
			if(!ItemHelper.isEmpty(itemHandler))
			{
				NCrusherRecipe.findRecipe(getLevel(), inv.getStackInSlot(0)).ifPresentOrElse(rec -> 
				{
					int perTick = rec.getTotalProcessEnergy() / rec.getTotalProcessTime();
					if (energy.getEnergyStored() >= perTick)
					{
						if (usedEnergy >= rec.getTotalProcessEnergy())
						{
							popResult(rec.getActualItemOutputs(this));
							usedEnergy = 0;
							inv.extractItem(0, 1, false);
						}
						else
						{
							usedEnergy += perTick;
						}
						energy.extract(perTick, false);
					}
				}, 
				() -> 
				{
					ItemStack stack = inv.getStackInSlot(0).copy();
					if (!stack.isEmpty())
					{
						popResult(NonNullList.of(ItemStack.EMPTY, stack));
					}
					inv.getSlot(0).clear();
				});
			}
			
			gatherInput();
		}
	}
	
	private void gatherInput() 
	{
		if (ItemHelper.hasEmptySpace(itemHandler))
		{
			List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class, zone);
			if (!list.isEmpty())
			{
				ItemStack stack = inv.getSlot(0).insert(list.get(0).getItem().copy(), false);
				list.get(0).setItem(stack);
			}
		}
	}

	private void popResult(NonNullList<ItemStack> stacks) 
	{
		BlockState state = getBlockState();
		Direction dir = state.getValue(BlockHelper.BlockProperties.FACING);
		Vec3 pos = new Vec3(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()).add(0.5f, 0.2f, 0.5f).relative(dir, 0.6f);
		Double speed = level.random.nextDouble() * 0.2D - 0.1D;
		for (ItemStack stack : stacks)
		{
			WorldHelper.spawnItemEntity(getLevel(), pos.x(), pos.y(), pos.z(), speed * dir.getStepX(), 0, speed * dir.getStepZ(), stack);
		}
	}

	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		
		inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
		energy.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Vim.TAG_LOCATION));
		
		this.lastActiveTime = tag.getLong(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Manual_Crusher.LAST_ACTIVE_TIME);
		this.currentAngle = tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Manual_Crusher.CURRENT_ANGLE);
		this.usedEnergy = tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Manual_Crusher.USED_ENERGY);
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
		
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
		tag.put(NDatabase.Capabilities.Vim.TAG_LOCATION, energy.serializeNBT());
		
		tag.putLong(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Manual_Crusher.LAST_ACTIVE_TIME, lastActiveTime);
		tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Manual_Crusher.CURRENT_ANGLE, currentAngle);
		tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Manual_Crusher.USED_ENERGY, usedEnergy);
	}
	
	@Override
	public void invalidateCaps() 
	{
		energyHandler.invalidate();
		itemHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		if (cap == ItemHelper.itemHandler && side == Direction.DOWN)
		{
			return itemHandler.cast();
		} 
		else if (cap == VimHelper.vimHandler && side == null)
		{
			return energyHandler.cast();
		}
		return super.getCapability(cap, side);
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

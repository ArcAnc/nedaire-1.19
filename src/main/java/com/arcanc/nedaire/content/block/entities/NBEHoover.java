/**
 * @author ArcAnc
 * Created at: 2022-11-02
 * Copyright (c) 2022
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
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.WorldHelper;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class NBEHoover extends NBERedstoneSensitive implements IInventoryCallback, ModServerTickerBlockEntity, IInteractionObjectN<NBEHoover>
{

	private NSimpleItemStorage inv;
	private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inv);
	
	public static final int INVENTORY_SIZE = 9;
	
	private static final AABB SUCK_ZONE = new AABB(-2, -2, -2, 3, 3, 3);
	private static final AABB EAT_ZONE = new AABB(0, 0.5f, 0, 1, 1.1f, 1);
	
	private final AABB suck_zone_local;
	private final Vec3 suck_zone_center; 
	
	private final AABB eat_zone_local;
	
	
	public NBEHoover(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_HOOVER.get(), pos, state);
		inv = new NSimpleItemStorage(this, INVENTORY_SIZE);
		
		this.suck_zone_center = new Vec3(pos.getX() + 0.5f, pos.getY() + 1.1f, pos.getZ() + 0.5f);
		this.suck_zone_local = SUCK_ZONE.move(pos);
		
		this.eat_zone_local = EAT_ZONE.move(pos);
	}

	@Override
	public void tickServer() 
	{
		if (isPowered())
		{
			if (ItemHelper.hasEmptySpace(itemHandler))
			{
				//sucking items to self
				/*FIXME: add predicate to filter suckuble items*/
				List<ItemEntity> list = getLevel().getEntitiesOfClass(ItemEntity.class, suck_zone_local, EntitySelector.NO_SPECTATORS);
				if (!list.isEmpty())
				{
					list.stream().forEach(entity -> 
					{
						Vec3 moveVector = new Vec3(suck_zone_center.x() - entity.getX(), suck_zone_center.y() - entity.getY(), suck_zone_center.z() - entity.getZ()).
								normalize().scale(0.02f); 
						entity.push(moveVector.x(), moveVector.y(), moveVector.z());
					});
				}
				
				if (getLevel().getGameTime() % 20 == 0)
				{
					for (int x = (int) suck_zone_local.minX; x < suck_zone_local.maxX; x++)
					{
						for(int y = (int)suck_zone_local.minY; y < suck_zone_local.maxY; y++)
						{
							for (int z = (int)suck_zone_local.minZ; z < suck_zone_local.maxZ; z++)
							{
								BlockEntity tile = level.getBlockEntity(new BlockPos(x, y, z));
								if (tile != null && tile != this && !(tile instanceof NBEHoover))
								{
									ItemHelper.getItemHandler(tile).ifPresent(handler -> 
									{
										/*FIXME: add filter items*/
										for (int q = 0; q < handler.getSlots(); q++)
										{
											if (!handler.getStackInSlot(q).isEmpty())
											{
												ItemStack stack = handler.extractItem(q, 1, false);
												WorldHelper.spawnItemEntity(level, tile.getBlockPos().getX() + 0.5d, tile.getBlockPos().getY() + 0.5d, tile.getBlockPos().getZ() + 0.5d, stack);
												
												break;
											}
										}
									});
								}
							}
						}
					}
				}
				
				//eating items
				list.clear();
				list = getLevel().getEntitiesOfClass(ItemEntity.class, eat_zone_local, EntitySelector.NO_SPECTATORS);
				if (!list.isEmpty())
				{
					for(int i = 0; i < list.size(); i++)
					{
						ItemEntity ent = list.get(i);
						ItemStack stack = ent.getItem().copy();
						for(int q = 0; q < inv.getSlots(); q++)
						{
							if(!stack.isEmpty())
							{
								stack = inv.insertItem(q, stack, false);
							}
						}
						ent.setItem(stack);
					}
				}
			}
		}
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		if (tag.contains(NDatabase.Capabilities.ItemHandler.TAG_LOCATION))
			inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
	}
	
	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
	}
	
	@Override
	public void invalidateCaps() 
	{
		itemHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		if (cap == ItemHelper.itemHandler)
			return itemHandler.cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onInventoryChange(int slot) 
	{
		setChanged();
	}

	@Override
	public NBEHoover getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEHoover, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.HOOVER;
	}

	@Override
	public boolean canUseGui(Player player) 
	{
		return true;
	}

}

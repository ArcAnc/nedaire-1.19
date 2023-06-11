/**
 * @author ArcAnc
 * Created at: 2023-02-26
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.BlockInterfaces.INInteractionObject;
import com.arcanc.nedaire.content.block.entities.ticker.NClientTickerBlockEntity;
import com.arcanc.nedaire.content.block.entities.ticker.NServerTickerBlockEntity;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.item.CrystalPrisonItem;
import com.arcanc.nedaire.content.network.NNetworkEngine;
import com.arcanc.nedaire.content.network.messages.MessageBlockEntitySync;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.arcanc.nedaire.util.inventory.NManagedItemStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NBEMobCatcher extends NBERedstoneSensitive implements IInventoryCallback, NClientTickerBlockEntity, NServerTickerBlockEntity, INInteractionObject<NBEMobCatcher>
{
	public AnimationState catchAnim = new AnimationState();
	
	private final AABB zone;
	
	protected VimStorage energy;
	protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
	protected NManagedItemStorage inv;
	
	public static final int REQUIRED_ENERGY = 2500;
	protected static final int PER_TICK = 50;
	protected int usedEnergy = 0;

	protected boolean working = false;
	protected ItemStack mobStack = ItemStack.EMPTY;
	protected Entity ent = null;
	public int age = 0;
	
	public NBEMobCatcher(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_MOB_CATHER.get(), pos, state);
	
		zone = new AABB(getBlockPos().getX(), getBlockPos().getY() + 1, getBlockPos().getZ(), getBlockPos().getX() + 1, getBlockPos().getY() + 3, getBlockPos().getZ() + 1);
		
		for (Direction dir : Direction.values())
		{
			if (dir != Direction.UP)
			{
				ports.put(dir, AccessType.FULL);
			}
		}
		
		this.energy = VimStorage.newConfig(this).setMaxEnergy(5000).setEnergy(0).build();
		
		this.inv = new NManagedItemStorage(this).
				addInputSlot(64, stack -> stack.getItem() instanceof CrystalPrisonItem && stack.getOrCreateTag().getCompound(CrystalPrisonItem.ENTITY_DATA).isEmpty()).
				addOutputSlot(1).
				addOutputSlot(1).
				addOutputSlot(1).
				addOutputSlot(1).
				build();
	}

	@Override
	public void tickClient() 
	{
		age++;
	}
	
	@Override
	public void tickServer() 
	{
		age++;
		
		if (isPowered())
		{
			boolean emptyOutput = ItemHelper.hasEmptySpace(inv.getOutputHandler());
			boolean containsStack = ItemHelper.containsStack(NRegistration.RegisterItems.CRYSTAL_PRISON.get().getDefaultInstance(), inv.getInputHandler());
			if (emptyOutput && containsStack)
			{
				if(!this.working)
				{
					List<Mob> entities = getLevel().getEntitiesOfClass(Mob.class, zone, mob -> !CrystalPrisonItem.BLOCK_LIST.contains(mob.getType()));
					if (!entities.isEmpty())
					{
						Mob mob = entities.get(0);
						this.working = true;
						
						ItemStack stack = ItemHelper.copyStackWithAmount(inv.getInputHandler().getStackInSlot(0), 1);
						
						CompoundTag data = new CompoundTag();
						mob.save(data);
						
						mob.discard();
						
						stack.getOrCreateTag().put(CrystalPrisonItem.ENTITY_DATA, data);
						
						mobStack = stack;
						
						sendAnimationPacket();
					}
				}
				else
				{
					if (usedEnergy >= REQUIRED_ENERGY)
					{
						inv.getInputHandler().getSlot(0).modify(-1);
						for (int q = 0; q < inv.getOutputHandler().getSlots(); q++)
						{
							ItemStack st = inv.getOutputHandler().getStackInSlot(q);
							if (st.isEmpty())
							{
								inv.getOutputHandler().setStackInSlot(mobStack, q);
								break;
							}
						}
						mobStack = ItemStack.EMPTY;
						usedEnergy = 0;
						working = false;
					}
					else
					{
						usedEnergy += this.energy.extract(PER_TICK, false);
					}
				}
			}
		}
	}

	protected void sendAnimationPacket()
	{
		CompoundTag tag = new CompoundTag();
		
		tag.putInt("time", age);
		
		tag.put("entity", mobStack.getTag().get(CrystalPrisonItem.ENTITY_DATA));
		
		NNetworkEngine.packetHandler.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(getBlockPos())), new MessageBlockEntitySync(this, tag));
	}
	
	@Override
	public void handlePacketFromServer(CompoundTag tag) 
	{
		this.catchAnim.start(tag.getInt("time"));
		
		CompoundTag data = tag.getCompound("entity");
		
		EntityType.by(data).ifPresent(type -> 
		{
			ent = type.create(getLevel());
			ent.load(data);
		});
	}
	
	public int getUsedEnergy() 
	{
		return usedEnergy;
	}
	
	public ItemStack getMobStack() 
	{
		return mobStack;
	}
	
	public Entity getEnt() 
	{
		return ent;
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
	
		inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
		
		energy.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Vim.TAG_LOCATION));
		
		mobStack = ItemStack.of(tag.getCompound(NDatabase.Blocks.BlockEntities.TagAddress.Machines.MobCatcher.MOB_STACK));
		working = tag.getBoolean(NDatabase.Blocks.BlockEntities.TagAddress.Machines.MobCatcher.WORKING);
		usedEnergy = tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.MobCatcher.USED_ENERGY);
	}
	
	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
	
		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
		
		tag.put(NDatabase.Capabilities.Vim.TAG_LOCATION, energy.serializeNBT());
		
		tag.put(NDatabase.Blocks.BlockEntities.TagAddress.Machines.MobCatcher.MOB_STACK, mobStack.save(new CompoundTag()));
		tag.putBoolean(NDatabase.Blocks.BlockEntities.TagAddress.Machines.MobCatcher.WORKING, working);
		tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.MobCatcher.USED_ENERGY, usedEnergy);
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
		energyHandler.invalidate();
		inv.invalidate();

		super.invalidateCaps();
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

	@Override
	public NBEMobCatcher getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEMobCatcher, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.MOB_CATCHER;
	}

	@Override
	public boolean canUseGui(Player player) 
	{
		return true;
	}
	
	@Override
	public AABB getRenderBoundingBox() 
	{
		return new AABB(getBlockPos()).move(new Vec3(1, 3, 1));
	}
}

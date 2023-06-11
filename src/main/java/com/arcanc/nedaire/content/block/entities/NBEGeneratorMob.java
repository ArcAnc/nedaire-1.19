/**
 * @author ArcAnc
 * Created at: 2023-03-03
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
import com.arcanc.nedaire.content.item.CrystalPrisonItem;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.arcanc.nedaire.util.inventory.NManagedItemStorage;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class NBEGeneratorMob extends NBERedstoneSensitive implements IInventoryCallback, NServerTickerBlockEntity, INInteractionObject<NBEGeneratorMob>
{

	public static Map<EntityType<?>, Integer> FUEL = Maps.newHashMap();
	
	protected VimStorage energy;
	protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
	protected NManagedItemStorage inv;
	
	private int remainingEnergy = 0;
	private Optional<EntityType<?>> currentType = Optional.empty();
	
	private static final int PER_TICK = 25;
	
	public NBEGeneratorMob(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_GENERATOR_MOB.get(), pos, state);

		for (Direction dir : Direction.values())
		{
			if (dir != Direction.SOUTH)
			{
				ports.put(dir, AccessType.FULL);
			}
		}
		
		this.energy = VimStorage.newConfig(this).setMaxEnergy(50000).setEnergy(0).build();
		
		this.inv = new NManagedItemStorage(this).
				addInputSlot(1, stack -> stack.getItem() instanceof CrystalPrisonItem && !stack.getTag().getCompound(CrystalPrisonItem.ENTITY_DATA).isEmpty()).
				addInputSlot(1, stack -> stack.getItem() instanceof CrystalPrisonItem && !stack.getTag().getCompound(CrystalPrisonItem.ENTITY_DATA).isEmpty()).
				addInputSlot(1, stack -> stack.getItem() instanceof CrystalPrisonItem && !stack.getTag().getCompound(CrystalPrisonItem.ENTITY_DATA).isEmpty()).
				addInputSlot(1, stack -> stack.getItem() instanceof CrystalPrisonItem && !stack.getTag().getCompound(CrystalPrisonItem.ENTITY_DATA).isEmpty()).
				addOutputSlot(64).
				build();
		
		FUEL = getFuel(); 
	}
	
	@Override
	public void tickServer() 
	{
		if(isPowered())
		{
			//working
			if (remainingEnergy > 0 )
			{
				ItemStack st = inv.getOutputHandler().getStackInSlot(0);
				if (st.getCount() < Math.min(inv.getOutputHandler().getSlotLimit(0), st.getMaxStackSize()))
				{
					int amount = Math.min(PER_TICK, remainingEnergy);

					if (energy.getMaxEnergyStored() - energy.getEnergyStored() >= amount)
					{
						remainingEnergy -= energy.add(amount, false);
					}
					
					if (remainingEnergy <= 0)
					{
						this.remainingEnergy = 0;
						this.currentType = Optional.empty();
						getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockHelper.BlockProperties.LIT, false), Block.UPDATE_CLIENTS);
						ItemStack stack = inv.getOutputHandler().getStackInSlot(0);
						if (stack.isEmpty())
						{
							inv.getOutputHandler().setStackInSlot(NRegistration.RegisterItems.CRYSTAL_PRISON.get().getDefaultInstance(), 0);
						}
						else
						{
							inv.getOutputHandler().getSlot(0).modify(1);
						}
					}
				}
			}
			else
			{
				for(int q = 0; q < inv.getInputHandler().getSlots(); q++ )
				{
					int i = q;
					ItemStack stack = inv.getInputHandler().getStackInSlot(q);
					if(!stack.isEmpty())
					{
						String str = stack.getOrCreateTag().getCompound(CrystalPrisonItem.ENTITY_DATA).getString("id");
						
						Optional<EntityType<?>> type = EntityType.byString(str);
						
						boolean stop = type.map(t -> 
						{
							this.currentType = type;
							this.remainingEnergy = FUEL.get(t);
							inv.getInputHandler().getSlot(i).clear();
							getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockHelper.BlockProperties.LIT, true), Block.UPDATE_CLIENTS);
							return true;
						}).orElse(false);
						
						if (stop)
							break;
					}
				}
			}
		}
	}

	public Optional<EntityType<?>> getCurrentType() 
	{
		return currentType;
	}
	
	public int getCurrentEnergy() 
	{
		return remainingEnergy > 0 ? currentType.map(NBEGeneratorMob.FUEL :: get).orElse(0) - remainingEnergy : 0;
	}
	
	public static Map<EntityType<?>, Integer> getFuel()
	{
		Map<EntityType<?>, Integer> map = Maps.newLinkedHashMap();
		
		add(map, EntityType.ALLAY, 2000);
		add(map, EntityType.AXOLOTL, 300);
		add(map, EntityType.BAT, 200);
		add(map, EntityType.BEE, 300);
		add(map, EntityType.BLAZE, 800);
		add(map, EntityType.CAT, 400);
		add(map, EntityType.CAMEL, 600);
		add(map, EntityType.CAVE_SPIDER, 900);
		add(map, EntityType.CHICKEN, 100);
		add(map, EntityType.COD, 400);
		add(map, EntityType.COW, 300);
		add(map, EntityType.CREEPER, 2000);
		add(map, EntityType.DOLPHIN, 900);
		add(map, EntityType.DONKEY, 500);
		add(map, EntityType.DROWNED, 600);
		add(map, EntityType.ELDER_GUARDIAN, 1500);
		add(map, EntityType.ENDERMAN, 1100);
		add(map, EntityType.ENDERMITE, 500);
		add(map, EntityTypeTags.RAIDERS, 900);
		add(map, EntityType.FOX, 400);
		add(map, EntityType.FROG, 200);
		add(map, EntityType.GHAST, 800);
		add(map, EntityType.GLOW_SQUID, 400);
		add(map, EntityType.GOAT, 500);
		add(map, EntityType.GUARDIAN, 1200);
		add(map, EntityType.HOGLIN, 700);
		add(map, EntityType.HORSE, 500);
		add(map, EntityType.HUSK, 600);
		add(map, EntityType.IRON_GOLEM, 1100);
		add(map, EntityType.LLAMA, 600);
		add(map, EntityType.MAGMA_CUBE, 700);
		add(map, EntityType.MULE, 500);
		add(map, EntityType.MOOSHROOM, 400);
		add(map, EntityType.OCELOT, 500);
		add(map, EntityType.PANDA, 600);
		add(map, EntityType.PARROT, 400);
		add(map, EntityType.PHANTOM, 700);
		add(map, EntityType.PIG, 250);
		add(map, EntityType.PIGLIN, 700);
		add(map, EntityType.PIGLIN_BRUTE, 800);
		add(map, EntityType.POLAR_BEAR, 600);
		add(map, EntityType.PUFFERFISH, 400);
		add(map, EntityType.RABBIT, 300);
		add(map, EntityType.SALMON, 400);
		add(map, EntityType.SHEEP, 250);
		add(map, EntityType.SHULKER, 1000);
		add(map, EntityType.SILVERFISH, 400);
		add(map, EntityType.SKELETON, 600);
		add(map, EntityType.SLIME, 700);
		add(map, EntityType.SNOW_GOLEM, 400);
		add(map, EntityType.SPIDER, 700);
		add(map, EntityType.SQUID, 400);
		add(map, EntityType.STRAY, 600);
		add(map, EntityType.STRIDER, 500);
		add(map, EntityType.TADPOLE, 200);
		add(map, EntityType.TRADER_LLAMA, 400);
		add(map, EntityType.TROPICAL_FISH, 400);
		add(map, EntityType.TURTLE, 400);
		add(map, EntityType.VEX, 600);
		add(map, EntityType.VILLAGER, 400);
		add(map, EntityType.WANDERING_TRADER, 400);
		add(map, EntityType.WITHER_SKELETON, 800);
		add(map, EntityType.WOLF, 500);
		add(map, EntityType.ZOGLIN, 600);
		add(map, EntityType.ZOMBIE, 600);
		add(map, EntityType.ZOMBIE_VILLAGER, 600);
		add(map, EntityType.ZOMBIFIED_PIGLIN, 600);

		return map;
	}
	
	private static void add(Map<EntityType<?>, Integer> map, EntityType<?> entityType, int value) 
	{
		if (!CrystalPrisonItem.BLOCK_LIST.contains(entityType))
		{
			map.put(entityType, value);
		}
	}
	
	private static void add(Map<EntityType<?>, Integer> map, TagKey<EntityType<?>> tag, int value) 
	{
		ForgeRegistries.ENTITY_TYPES.tags().getTag(tag).forEach(type -> 
		{
			if (!CrystalPrisonItem.BLOCK_LIST.contains(type))
			{
				map.put(type, value);
			}
		}); 
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);

		tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
		
		tag.put(NDatabase.Capabilities.Vim.TAG_LOCATION, energy.serializeNBT());
		
		tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.GeneratorMob.REMAINING_ENERGY, remainingEnergy);
		
		currentType.ifPresentOrElse(type ->
		{
			tag.putString(NDatabase.Blocks.BlockEntities.TagAddress.Machines.GeneratorMob.ENTITY_TYPE, EntityType.getKey(type).toString());
		}, () -> 
		{
			tag.putString(NDatabase.Blocks.BlockEntities.TagAddress.Machines.GeneratorMob.ENTITY_TYPE, "null");
		});
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);

		inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
		
		energy.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Vim.TAG_LOCATION));
		
		remainingEnergy = tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.GeneratorMob.REMAINING_ENERGY);
		
		String id = tag.getString(NDatabase.Blocks.BlockEntities.TagAddress.Machines.GeneratorMob.ENTITY_TYPE);
		currentType = id.equals("null") ? Optional.empty() : EntityType.byString(id);
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
		energyHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public NBEGeneratorMob getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEGeneratorMob, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.GENERATOR_MOB;
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

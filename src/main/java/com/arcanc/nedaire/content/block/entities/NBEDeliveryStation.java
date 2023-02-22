/**
 * @author ArcAnc
 * Created at: 2022-10-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInteractionObjectN;
import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.content.capabilities.filter.CapabilityFilter;
import com.arcanc.nedaire.content.capabilities.filter.FluidFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IFluidFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IItemFilter;
import com.arcanc.nedaire.content.capabilities.filter.IFilter.IVimFilter;
import com.arcanc.nedaire.content.capabilities.filter.ItemFilter;
import com.arcanc.nedaire.content.capabilities.filter.VimFilter;
import com.arcanc.nedaire.content.network.NNetworkEngine;
import com.arcanc.nedaire.content.network.messages.MessageDeliveryStationToClient;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.registration.NRegistration.RegisterMenuTypes.BEContainer;
import com.arcanc.nedaire.content.renderer.particle.delivery.DeliveryParticle;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.TagHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.google.common.base.Preconditions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.ItemHandlerHelper;

public class NBEDeliveryStation extends NBERedstoneSensitive implements IInventoryCallback, ModServerTickerBlockEntity, IInteractionObjectN<NBEDeliveryStation>
{
	private final List<BlockPos> attachedEntities = new ArrayList<>();
	public List<TransferData> path = new ArrayList<>();
	public static final int INVENTORY_SIZE = 9;
	
	/**
	 * 0 - transfer Items
	 * 1 - transfer Liquids
	 * 2 - transfer Vim
	 */
	private int mode = 1;
	
	private ItemFilter itemFilter;
	private final LazyOptional<IItemFilter> itemFilterHanlder = LazyOptional.of(() -> itemFilter);
	private FluidFilter fluidFilter;
	private final LazyOptional<IFluidFilter> fluidFilterHandler = LazyOptional.of(() -> fluidFilter);
	private VimFilter vimFilter;
	private final LazyOptional<IVimFilter> vimFilterHandler = LazyOptional.of(() -> vimFilter);
	
	
	public NBEDeliveryStation(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_DELIVERY_STATION.get(), pos, state);
		itemFilter = new ItemFilter(false, this, INVENTORY_SIZE);
		itemFilter.setExtracion(10);
		itemFilter.setMaxInInventory(64);
		fluidFilter = new FluidFilter(true, this, INVENTORY_SIZE);
		fluidFilter.setExtracion(100);
		fluidFilter.setMaxInInventory(2000);
		vimFilter = new VimFilter(20, 2000);
	}

	@Override
	public void tickServer() 
	{
		if (isPowered())
		{
			if (getLevel().getGameTime() % 30 == 0)
			{
				for (Direction dir : Direction.values())
				{
					if (dir.getAxis() != Direction.Axis.Y)
					{
						//extracting
						
						BlockHelper.castTileEntity(getLevel(), getBlockPos().offset(dir.getNormal()), BlockEntity.class).ifPresent(tile -> 
						{
							switch (mode)
							{
								case 0 ->
								{
									ItemHelper.getItemHandler(tile).ifPresent(handler ->
									{
										for (int q = 0; q < handler.getSlots(); q++)
										{
											int index = q;
											ItemStack stack = handler.getStackInSlot(q);
											
											if(itemFilter.filter(stack) && itemFilter.filterMaxInInventory(handler, stack))
											{
												Optional<BlockEntity> tr = getTargetBlock(0);
												if (tr.isPresent())
												{
													tr.ifPresent(target -> 
													{
														ItemStack st = handler.extractItem(index, Math.min(itemFilter.getExtraction(), itemFilter.getMaxInInventory()), false);
														
														NNetworkEngine.sendToAllPlayers(new MessageDeliveryStationToClient(
																tile.getBlockPos(),
																target.getBlockPos(),
																getBlockPos(),
																true, st.copy()));
														
														Vec3[] p = DeliveryParticle.calculatePath(
																new Vec3(
																		tile.getBlockPos().getX() + 0.5d,
																		tile.getBlockPos().getY() + 0.5d, 
																		tile.getBlockPos().getZ() + 0.5d), 
																new Vec3(
																		getBlockPos().getX() + 0.5d,
																		getBlockPos().getY() + 0.5d,
																		getBlockPos().getZ() + 0.5d),
																getLevel());
														
														path.add(new TransferData(
																new Vec3(
																		tile.getBlockPos().getX() + 0.5d,
																		tile.getBlockPos().getY() + 0.5d,
																		tile.getBlockPos().getZ() + 0.5d),
																new Vec3(
																		target.getBlockPos().getX() + 0.5d,
																		target.getBlockPos().getY() + 0.5d,
																		target.getBlockPos().getZ() + 0.5d),
																new Vec3(getBlockPos().getX() + 0.5d,
																		 getBlockPos().getY() + 0.5d,
																		 getBlockPos().getZ() + 0.5d),
																new MutableBoolean(true), 
																new MutableInt(0),
																new MutableInt(p.length), 
																st));
													});
													break;
												}
											}
										}
									});
									break;
								}
								case 1 ->
								{
									FluidHelper.getFluidHandler(tile).ifPresent(handler -> 
									{
										for (int q = 0; q < handler.getTanks(); q++)
										{
											FluidStack stack = handler.getFluidInTank(q);
											
											/**
											 * FIXME: add check for max amount in TARGET inv. Not attach inventory
											 */
											if(fluidFilter.filter(stack))// && fluidFilter.filterMaxInInventory(handler, stack))
											{
												Optional<BlockEntity> tr = getTargetBlock(1);
												if(tr.isPresent())
												{
													tr.ifPresent(target -> 
													{
														FluidStack st = handler.drain(
																FluidHelper.copyFluidStackWithAmount(
																		stack, 
																		Math.min(fluidFilter.getExtraction(), fluidFilter.getMaxInInventory())), 
																FluidAction.EXECUTE);

														NNetworkEngine.sendToAllPlayers(new MessageDeliveryStationToClient(
																tile.getBlockPos(),
																target.getBlockPos(),
																getBlockPos(),
																true, st));
													
														Vec3[] p = DeliveryParticle.calculatePath(
																new Vec3(
																		tile.getBlockPos().getX() + 0.5d,
																		tile.getBlockPos().getY() + 0.5d, 
																		tile.getBlockPos().getZ() + 0.5d), 
																new Vec3(
																		getBlockPos().getX() + 0.5d,
																		getBlockPos().getY() + 0.5d,
																		getBlockPos().getZ() + 0.5d),
																getLevel());
														
														path.add(new TransferData(
																new Vec3(
																		tile.getBlockPos().getX() + 0.5d,
																		tile.getBlockPos().getY() + 0.5d,
																		tile.getBlockPos().getZ() + 0.5d),
																new Vec3(
																		target.getBlockPos().getX() + 0.5d,
																		target.getBlockPos().getY() + 0.5d,
																		target.getBlockPos().getZ() + 0.5d),
																new Vec3(getBlockPos().getX() + 0.5d,
																		 getBlockPos().getY() + 0.5d,
																		 getBlockPos().getZ() + 0.5d),
																new MutableBoolean(true), 
																new MutableInt(0),
																new MutableInt(p.length), 
																st));
													});
												}
											}
										}
									});
									break;
								}
								case 2 -> 
								{
									VimHelper.getVimHandler(tile).ifPresent(handler -> 
									{
										int amount = handler.getEnergyStored();
											
											if(vimFilter.filter(amount) && vimFilter.filterMaxInInventory(handler, amount))
											{
												Optional<BlockEntity> tr = getTargetBlock(2);
												if(tr.isPresent())
												{
													tr.ifPresent(target -> 
													{
														int st = handler.extract(
																Math.min(vimFilter.getExtraction(), vimFilter.getMaxInInventory()), 
																false);
														
														NNetworkEngine.sendToAllPlayers(new MessageDeliveryStationToClient(
																tile.getBlockPos(),
																target.getBlockPos(),
																getBlockPos(),
																true, st));
													
														Vec3[] p = DeliveryParticle.calculatePath(
																new Vec3(
																		tile.getBlockPos().getX() + 0.5d,
																		tile.getBlockPos().getY() + 0.5d, 
																		tile.getBlockPos().getZ() + 0.5d), 
																new Vec3(
																		getBlockPos().getX() + 0.5d,
																		getBlockPos().getY() + 0.5d,
																		getBlockPos().getZ() + 0.5d),
																getLevel());
														
														path.add(new TransferData(
																new Vec3(
																		tile.getBlockPos().getX() + 0.5d,
																		tile.getBlockPos().getY() + 0.5d,
																		tile.getBlockPos().getZ() + 0.5d),
																new Vec3(
																		target.getBlockPos().getX() + 0.5d,
																		target.getBlockPos().getY() + 0.5d,
																		target.getBlockPos().getZ() + 0.5d),
																new Vec3(getBlockPos().getX() + 0.5d,
																		 getBlockPos().getY() + 0.5d,
																		 getBlockPos().getZ() + 0.5d),
																new MutableBoolean(true), 
																new MutableInt(0),
																new MutableInt(p.length), 
																st));
													});
												}
											}
									});
									break;
								}
							}
						});
					}
				}
			}
		}
		moveObjects();
	}
	
	private void moveObjects()
	{
		List<TransferData> additionalInfo = new ArrayList<>();
		path = path.stream().filter(data -> 
		{
			int length = data.path().getValue();
			if (data.currentPos().incrementAndGet() == length)
			{
				if (data.toStation().isTrue())
				{
					data.toStation().setFalse();
					data.currentPos().setValue(0);
					
					Vec3[] p = DeliveryParticle.calculatePath(data.station(), data.finishPos(), getLevel());
					
					data.path().setValue(p.length);
					return true;
				}
				else
				{
					if (data.obj() instanceof ItemStack stack)
					{
						BlockHelper.castTileEntity(getLevel(), data.finishPos(), BlockEntity.class).ifPresent(tile -> 
						{
							ItemHelper.getItemHandler(tile).ifPresent(handler -> 
							{
								ItemStack s = ItemHandlerHelper.insertItem(handler, stack, false);
								
								if (!s.isEmpty())
								{
									additionalInfo.add(new TransferData(data.finishPos(), data.startPos(), data.station(),																
											new MutableBoolean(true), 
											new MutableInt(0),
											new MutableInt(data.path().intValue()), s));
								
									NNetworkEngine.sendToAllPlayers(new MessageDeliveryStationToClient(
											data.finishPos(),
											data.startPos(),
											data.station(),
											true, s.copy()));
								}
							});
						}); 
					}
					else if(data.obj() instanceof FluidStack stack)
					{
						BlockHelper.castTileEntity(getLevel(), data.finishPos(), BlockEntity.class).ifPresent(tile -> 
						{
							FluidHelper.getFluidHandler(tile).ifPresent(handler -> 
							{
								FluidStack s = FluidHelper.copyFluidStackWithAmount(stack, stack.getAmount() - handler.fill(stack, FluidAction.EXECUTE));
							
								if (!s.isEmpty())
								{
									additionalInfo.add(new TransferData(data.finishPos(), data.startPos(), data.station(),																
											new MutableBoolean(true), 
											new MutableInt(0),
											new MutableInt(data.path().intValue()), s));
								
									NNetworkEngine.sendToAllPlayers(new MessageDeliveryStationToClient(
											data.finishPos(),
											data.startPos(),
											data.station(),
											true, s.copy()));
								}
							});
						}); 
					}
					else if(data.obj() instanceof Integer stack)
					{
						BlockHelper.castTileEntity(getLevel(), data.finishPos(), BlockEntity.class).ifPresent(tile -> 
						{
							VimHelper.getVimHandler(tile).ifPresent(handler -> 
							{
								int s = stack - handler.add(stack, false);
								if (s > 0)
								{
									additionalInfo.add(new TransferData(data.finishPos(), data.startPos(), data.station(),																
											new MutableBoolean(true), 
											new MutableInt(0),
											new MutableInt(data.path().intValue()), s));
								
									NNetworkEngine.sendToAllPlayers(new MessageDeliveryStationToClient(
											data.finishPos(),
											data.startPos(),
											data.station(),
											true, s));
								}
							});
						}); 
					}
					return false;
				}
			}
			return true;
		}).collect(Collectors.toList());
		path.addAll(additionalInfo);
	}
	
	private Optional<BlockEntity> getTargetBlock(int filterMode)
	{
		return switch (filterMode)
		{
			case 0:
				yield attachedEntities.stream().map(pos -> BlockHelper.castTileEntity(getLevel(), pos, BlockEntity.class)).
								filter(tile -> 
								{
									if (tile.isPresent())
									{
										if (ItemHelper.hasEmptySpace(tile.get()))
										{
											return true;
										}
									}
									return false;
								}).
								map(Optional :: get).
								findFirst();
			
			case 1: 
				yield attachedEntities.stream().map(pos -> BlockHelper.castTileEntity(getLevel(), pos, BlockEntity.class)).
								filter(tile ->
								{
									if (tile.isPresent())
									{
										if (FluidHelper.hasEmptySpace(tile.get()))
										{
											return true;
										}
									}
									return false;
								}).
								map(Optional :: get).
								findFirst();
			case 2: 
				yield attachedEntities.stream().map(pos -> BlockHelper.castTileEntity(getLevel(), pos, BlockEntity.class)).
								filter(tile ->
								{
									if (tile.isPresent())
									{
										if (VimHelper.hasEmptySpace(tile.get()))
										{
											return true;
										}
									}
									return false;
								}).
								map(Optional :: get).
								findFirst();
			default: 
				yield null;
		};
	}
	
	public IFilter<?, ?, ?> chooseCorrentFilter ()
	{
		return switch (mode)
				{
					case 0 -> itemFilter;
					case 1 -> fluidFilter;
					case 2 -> vimFilter;
					default -> null;
				}; 
	}
	
	public boolean addTile(Level lvl, BlockPos pos)
	{
		Preconditions.checkNotNull(lvl);
		Preconditions.checkNotNull(pos);
		if(lvl.dimension().equals(getLevel().dimension()))
		{
			Optional<BlockEntity> tile = BlockHelper.castTileEntity(lvl, pos, BlockEntity.class);
			return tile.map( t -> 
			{
				return switch (mode) 
						{
							case 0 :
								yield ItemHelper.getItemHandler(t).map(handler -> 
								{
									return this.attachedEntities.add(pos);
								}).orElse(false);
							case 1 :
								yield FluidHelper.getFluidHandler(t).map(handler -> 
								{
									return this.attachedEntities.add(pos);
								}).orElse(false);
							case 2 :
								yield VimHelper.getVimHandler(t).map(hanlder -> 
								{
									return this.attachedEntities.add(pos);
								}).orElse(false);
							default:
								yield false;
						};
			}).orElse(false);
		}
		return false;
	}
	
	public boolean removeTile(BlockPos pos)
	{
		Preconditions.checkNotNull(pos);
		return attachedEntities.remove(pos);
	}
	
	public void checkTiles()
	{
		List<BlockPos> poses = new ArrayList<>(attachedEntities);
		
		poses = poses.stream().filter(pos -> 
		{
			return BlockHelper.castTileEntity(getLevel(), pos, BlockEntity.class).map(tile -> 
			{
				return switch (mode)
						{
							case 0 :
								yield ItemHelper.isItemHandler(tile);
							case 1 :
								yield FluidHelper.isFluidHandler(tile);
							case 2 :
								yield VimHelper.isVimHandler(tile);
							default:
								yield false;
						};
			}).orElse(false);
		}).toList();
		
		
		attachedEntities.clear();
		attachedEntities.addAll(poses);
	}
	
	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		
		if (tag.contains(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.ATTACHED_POSES))
		{
			this.attachedEntities.clear();
			ListTag attach = tag.getList(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.ATTACHED_POSES, Tag.TAG_COMPOUND);
			for (int q = 0; q < attach.size(); q++)
				this.attachedEntities.add(NbtUtils.readBlockPos(attach.getCompound(q)));
		}
		if (tag.contains(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.MODE))
			this.mode = tag.getInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.MODE);
		if (tag.contains(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.PATH))
		{
			this.path.clear();
			ListTag list = tag.getList(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.PATH, Tag.TAG_COMPOUND);
			for (int q = 0; q < list.size(); q++)
				this.path.add(TransferData.loadData(list.getCompound(q)));
		}
		if (tag.contains(NDatabase.Capabilities.Filter.TAG_LOCATION_ITEM))
			itemFilter.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Filter.TAG_LOCATION_ITEM));
		if (tag.contains(NDatabase.Capabilities.Filter.TAG_LOCATION_FLUID))
			fluidFilter.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Filter.TAG_LOCATION_FLUID));
		if (tag.contains(NDatabase.Capabilities.Filter.TAG_LOCATION_VIM))
			vimFilter.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Filter.TAG_LOCATION_VIM));
		
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);
		ListTag attach = new ListTag();
		for (BlockPos pos : attachedEntities)
			attach.add(NbtUtils.writeBlockPos(pos));
		
		tag.put(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.ATTACHED_POSES, attach);
		tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.MODE, mode);
		ListTag list = new ListTag();
		for (TransferData data : path)
		{
			list.add(data.saveData());
		}
		tag.put(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.PATH, list);
		
		tag.put(NDatabase.Capabilities.Filter.TAG_LOCATION_ITEM, itemFilter.serializeNBT());
		tag.put(NDatabase.Capabilities.Filter.TAG_LOCATION_FLUID, fluidFilter.serializeNBT());
		tag.put(NDatabase.Capabilities.Filter.TAG_LOCATION_VIM, vimFilter.serializeNBT());
	}
	
	@Override
	public void invalidateCaps() 
	{
		itemFilterHanlder.invalidate();
		fluidFilterHandler.invalidate();
		vimFilterHandler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		if (cap == CapabilityFilter.FILTER_ITEM)
		{
			return itemFilterHanlder.cast();
		}
		else if (cap == CapabilityFilter.FILTER_FLUID)
		{
			return fluidFilterHandler.cast();
		}
		else if (cap == CapabilityFilter.FILTER_VIM)
		{
			return vimFilterHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onInventoryChange(int slot) 
	{
		setChanged();
	}
	
	@Override
	public NBEDeliveryStation getBE() 
	{
		return this;
	}

	@Override
	public BEContainer<NBEDeliveryStation, ?> getContainerType() 
	{
		return NRegistration.RegisterMenuTypes.DELIVERY_STATION;
	}

	@Override
	public boolean canUseGui(Player player) 
	{
		return true;
	}
	
	private record TransferData(Vec3 startPos, Vec3 finishPos, Vec3 station, MutableBoolean toStation, MutableInt currentPos, MutableInt path, Object obj)
	{
		
		private static final String START = "start";
		private static final String FINISH = "finish";
		private static final String STATION = "station";
		private static final String TO_STATION = "to_station"; 
		private static final String CURRENT_POS = "current";
		private static final String PATH = "path";
		private static final String DATA_OBJ = "data";
		
		public static TransferData loadData(CompoundTag tag)
		{
			Object o = null;
			Tag t = tag.get(DATA_OBJ);
			if (t instanceof IntTag i)
			{
				o = i;
			}
			else if(t instanceof CompoundTag compound)
			{
				if (compound.contains("id") )
					o = ItemStack.of(compound);
				else
					o = FluidStack.loadFluidStackFromNBT(compound);
			}
			
			return new TransferData(
					TagHelper.readVec3(tag, START),
					TagHelper.readVec3(tag, FINISH),
					TagHelper.readVec3(tag, STATION),
					new MutableBoolean(tag.getBoolean(TO_STATION)),
					new MutableInt(tag.getInt(CURRENT_POS)),
					new MutableInt(tag.getInt(PATH)),
					o);
		}
		
		public CompoundTag saveData()
		{
			CompoundTag tag = new CompoundTag();
			
			TagHelper.writeVec3(startPos, tag, START);
			TagHelper.writeVec3(finishPos, tag, FINISH);
			TagHelper.writeVec3(station, tag, STATION);
			
			tag.putBoolean(TO_STATION, toStation.booleanValue());
			tag.putInt(CURRENT_POS, currentPos.intValue());
			tag.putInt(PATH, path.intValue());
			
			if (obj instanceof Integer object)
				tag.put(DATA_OBJ, IntTag.valueOf(object));
			else if(obj instanceof ItemStack object)
				tag.put(DATA_OBJ, object.save(new CompoundTag()));
			else if (obj instanceof FluidStack object)
				tag.put(DATA_OBJ, object.writeToNBT(new CompoundTag()));
			
			return tag;
		}
		
	}
}

/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class NBaseBlockEntity extends BlockEntity
{

	public NBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) 
	{
		super(type, pos, state);
	}
	
	@Override
	public void load(@NotNull CompoundTag tag)
	{
		super.load(tag);
		
		this.readCustomTag(tag, false);
	}
	
	public abstract void readCustomTag (CompoundTag tag, boolean descPacket);
	
	@Override
	protected void saveAdditional(@NotNull CompoundTag tag)
	{
		super.saveAdditional(tag);
		this.writeCustomTag(tag, false);
	}
	
	public abstract void writeCustomTag(CompoundTag tag, boolean descPacket);

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() 
	{
		return ClientboundBlockEntityDataPacket.create(this, blockEntity -> 
		{
			CompoundTag nbtTag = new CompoundTag();
			this.writeCustomTag(nbtTag, true);
			return nbtTag;
		});
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) 
	{
		CompoundTag nonNullTag = pkt.getTag() != null ? pkt.getTag() : new CompoundTag();
		this.readCustomTag(nonNullTag, true);
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag) 
	{
		this.readCustomTag(tag, true);
	}
	
	@Override
	public @NotNull CompoundTag getUpdateTag()
	{
		CompoundTag tag = super.getUpdateTag();
		writeCustomTag(tag, true);
		return tag;
	}
	
	public void handlePacketFromClient(CompoundTag tag)
	{
		
	}

	public void handlePacketFromServer(CompoundTag tag)
	{
		
	}

	/**
	 * Most calls to {@link BlockEntity#setChanged} should be replaced by this. The vanilla mD also updates comparator
	 * states and re-caches the block state, while in most cases we just want to say "this needs to be saved to disk"
	 */
	@SuppressWarnings("deprecation")
	protected void markDirty()
	{
		if (this.level != null && this.level.hasChunkAt(getBlockPos()))
		{
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
			this.level.getChunkAt(getBlockPos()).setUnsaved(true);			
		}
	}
	
	/**
	 *  Based on the super version, but works around a Forge patch to World#markChunkDirty causing duplicate comparator
	 * updates and only performs comparator updates if this TE actually has comparator behavior
	 */
	@Override
	public void setChanged()
	{
		if(this.level != null)
		{
			markDirty();
			BlockState state = getBlockState();
			if(state.hasAnalogOutputSignal())
				this.level.updateNeighbourForOutputSignal(this.worldPosition, state.getBlock());
		}
	}

}

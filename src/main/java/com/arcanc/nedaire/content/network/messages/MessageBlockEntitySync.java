/**
 * @author ArcAnc
 * Created at: 2023-02-28
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.network.messages;

import com.arcanc.nedaire.content.block.entities.NBaseBlockEntity;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.Objects;

public class MessageBlockEntitySync implements IMessage 
{

	private final BlockPos pos;
	private final CompoundTag nbt;
	
	/**
	 * FIXME: get rid of the NBT in packets
	 */
	public MessageBlockEntitySync(NBaseBlockEntity tile, CompoundTag nbt)
	{
		this.pos = tile.getBlockPos();
		this.nbt = nbt;
	}

	public MessageBlockEntitySync(FriendlyByteBuf buf)
	{
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.nbt = buf.readNbt();
	}

	@Override
	public void toBytes(FriendlyByteBuf buf)
	{
		buf.writeInt(pos.getX()).writeInt(pos.getY()).writeInt(pos.getZ());
		buf.writeNbt(this.nbt);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void process(CustomPayloadEvent.Context ctx)
	{
		if(ctx.getDirection().getReceptionSide() == LogicalSide.SERVER)
			ctx.enqueueWork(() -> 
			{
				ServerLevel world = Objects.requireNonNull(ctx.getSender()).serverLevel();
				if(world.isAreaLoaded(pos, 1))	
				{
					BlockEntity tile = world.getBlockEntity(pos);
					if(tile instanceof NBaseBlockEntity te)
						te.handlePacketFromClient(nbt);
				}
			});
		else
			ctx.enqueueWork(() -> 
			{
				Minecraft mc = RenderHelper.mc();
				Level world = mc.level;
				if(world != null) // This can happen if the task is scheduled right before leaving the world
				{
					BlockEntity tile = world.getBlockEntity(pos);
					if(tile instanceof NBaseBlockEntity te)
						te.handlePacketFromServer(nbt);
				}
			});
	}
	
}

/**
 * @author ArcAnc
 * Created at: 2022-11-05
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.network.messages;

import com.arcanc.nedaire.content.container.menu.NContainerMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class MessageContainerUpdate implements IMessage 
{
	private final int windowId;
	private final CompoundTag tag;
	
	public MessageContainerUpdate(int windowId, CompoundTag tag) 
	{
		this.windowId = windowId;
		this.tag = tag;
	}
	
	public MessageContainerUpdate(FriendlyByteBuf buf) 
	{
		this.windowId = buf.readInt();
		this.tag = buf.readNbt();
	}
	
	@Override
	public void toBytes(FriendlyByteBuf buf) 
	{
		buf.writeInt(windowId);
		buf.writeNbt(tag);
	}

	@Override
	public void process(CustomPayloadEvent.Context ctx)
	{
		ServerPlayer player = ctx.getSender();
		assert player!= null;
		ctx.enqueueWork( ()-> 
		{
			player.resetLastActionTime();
			if (player.containerMenu.containerId == windowId && player.containerMenu instanceof NContainerMenu cont)
				cont.receiveMessageFromScreen(tag);
		});
	}

}

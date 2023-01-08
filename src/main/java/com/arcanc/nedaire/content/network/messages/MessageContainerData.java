/**
 * @author ArcAnc
 * Created at: 2022-11-04
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.network.messages;

import java.util.List;
import java.util.function.Supplier;

import com.arcanc.nedaire.content.container.menu.NContainerMenu;
import com.arcanc.nedaire.content.container.sync.GenericDataSerializers;
import com.arcanc.nedaire.content.container.sync.GenericDataSerializers.DataPair;
import com.arcanc.nedaire.util.helpers.PacketHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent.Context;

public class MessageContainerData implements IMessage 
{
	private final List<Pair<Integer, DataPair<?>>> synced;
	
	public MessageContainerData(List<Pair<Integer, DataPair<?>>> synced) 
	{
		this.synced = synced;
	}
	
	public MessageContainerData(FriendlyByteBuf buf)
	{
		this(PacketHelper.readList(buf, pb -> Pair.of(pb.readVarInt(), GenericDataSerializers.read(pb))));
	}
	
	@Override
	public void toBytes(FriendlyByteBuf buf) 
	{
		PacketHelper.writeList(buf, synced, (pair, b) -> 
		{
			b.writeVarInt(pair.getFirst());
			pair.getSecond().write(b);
		});
	}

	@Override
	public void process(Supplier<Context> context) 
	{
		context.get().enqueueWork(() ->
		{
			Minecraft mc = RenderHelper.mc();
			AbstractContainerMenu currentContainer = mc.player.containerMenu;
			if (currentContainer != null && currentContainer instanceof NContainerMenu cont)
				cont.receiveSync(synced);
		});
	}

}

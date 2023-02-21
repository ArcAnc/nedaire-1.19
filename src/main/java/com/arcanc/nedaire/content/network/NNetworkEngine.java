/**
 * @author ArcAnc
 * Created at: 2022-11-04
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.network;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.arcanc.nedaire.content.network.messages.IMessage;
import com.arcanc.nedaire.content.network.messages.MessageContainerData;
import com.arcanc.nedaire.content.network.messages.MessageContainerUpdate;
import com.arcanc.nedaire.content.network.messages.MessageDeliveryStationToClient;
import com.arcanc.nedaire.content.network.messages.MessageEssenceParticle;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NNetworkEngine 
{
	public static final String VERSION = "currentVersion";
	
	/*FIXME: add version getter from api*/
	public static final SimpleChannel packetHandler = NetworkRegistry.ChannelBuilder.
			named(StringHelper.getLocFStr("main")).
			networkProtocolVersion(() -> VERSION).
			serverAcceptedVersions(VERSION :: equals).
			clientAcceptedVersions(VERSION :: equals).
			simpleChannel();
	
	private static int messageId = 0;
	private static final Set<Class<?>> knownPacketTypes = new HashSet<>();
	
	public static void init()
	{
		registerMessage(MessageContainerData.class, MessageContainerData :: new, NetworkDirection.PLAY_TO_CLIENT);
		registerMessage(MessageContainerUpdate.class, MessageContainerUpdate :: new, NetworkDirection.PLAY_TO_SERVER);
		registerMessage(MessageEssenceParticle.class, MessageEssenceParticle :: new, NetworkDirection.PLAY_TO_CLIENT);
		registerMessage(MessageDeliveryStationToClient.class, MessageDeliveryStationToClient :: new, NetworkDirection.PLAY_TO_CLIENT);
	}
	
	public static void sendToAllPlayers(IMessage message)
	{
		packetHandler.send(PacketDistributor.ALL.noArg(), message);
	}
	
	private static <T extends IMessage> void registerMessage(Class<T> packetType, Function<FriendlyByteBuf, T> decoder)
	{
		registerMessage(packetType, decoder, Optional.empty());
	}
	
	private static <T extends IMessage> void registerMessage(Class<T> packetType, Function<FriendlyByteBuf, T> decoder, NetworkDirection dir)
	{
		registerMessage(packetType, decoder, Optional.of(dir));
	}
	
	private static <T extends IMessage> void registerMessage(Class<T> packetType, Function<FriendlyByteBuf, T> decoder, Optional<NetworkDirection> dir)
	{
		if (!knownPacketTypes.add(packetType))
			throw new IllegalStateException("Dublicate packcet type: " + packetType.getName());
		packetHandler.registerMessage(messageId++, packetType, IMessage :: toBytes, decoder, (t, ctx) -> {
			t.process(ctx);
			ctx.get().setPacketHandled(true);
		}, dir);
	}
}

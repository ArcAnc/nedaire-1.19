/**
 * @author ArcAnc
 * Created at: 2022-11-04
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.network;

import com.arcanc.nedaire.content.network.messages.*;
import com.arcanc.nedaire.util.helpers.StringHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class NNetworkEngine 
{
	public static final int VERSION = 1;

	public static final SimpleChannel packetHandler = ChannelBuilder.
			named(StringHelper.getLocFStr("main")).
			networkProtocolVersion(VERSION).
			serverAcceptedVersions(Channel.VersionTest.exact(VERSION)).
			clientAcceptedVersions(Channel.VersionTest.exact(VERSION)).
			simpleChannel();

	private static final Set<Class<?>> knownPacketTypes = new HashSet<>();
	
	public static void init()
	{
		registerMessage(MessageContainerData.class, MessageContainerData :: new, NetworkDirection.PLAY_TO_CLIENT);
		registerMessage(MessageContainerUpdate.class, MessageContainerUpdate :: new, NetworkDirection.PLAY_TO_SERVER);
		registerMessage(MessageEssenceParticle.class, MessageEssenceParticle :: new, NetworkDirection.PLAY_TO_CLIENT);
		registerMessage(MessageDeliveryStationToClient.class, MessageDeliveryStationToClient :: new, NetworkDirection.PLAY_TO_CLIENT);
		registerMessage(MessageBlockEntitySync.class, MessageBlockEntitySync::new);
		registerMessage(MessageMultiblockSyncToClient.class, MessageMultiblockSyncToClient :: new, NetworkDirection.PLAY_TO_CLIENT);
	}
	
	public static void sendToAllPlayers(IMessage message)
	{
		packetHandler.send(message, PacketDistributor.ALL.noArg());
	}

	public static void sendToPlayer(ServerPlayer player, IMessage message)
	{
		packetHandler.send(message, PacketDistributor.PLAYER.with(player));
	}

	public static void sendToServer(IMessage message)
	{
		packetHandler.send(message, PacketDistributor.SERVER.noArg());
	}
	
	private static <T extends IMessage> void registerMessage(Class<T> packetType, Function<FriendlyByteBuf, T> decoder)
	{
		registerMessage(packetType, decoder, null);
	}

	private static <T extends IMessage> void registerMessage(Class<T> packetType, Function<FriendlyByteBuf, T> decoder, NetworkDirection dir)
	{
		if (!knownPacketTypes.add(packetType))
			throw new IllegalStateException("Duplicate packet type: " + packetType.getName());
		packetHandler.messageBuilder(packetType, dir).
		decoder(decoder).
		encoder(IMessage :: toBytes).
		consumerNetworkThread((msg, ctx) ->
		{
			msg.process(ctx);
			ctx.setPacketHandled(true);
		}).add();
	}
}

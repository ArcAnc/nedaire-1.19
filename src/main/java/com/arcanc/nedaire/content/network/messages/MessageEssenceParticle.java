/**
 * @author ArcAnc
 * Created at: 2023-02-14
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.network.messages;

import com.arcanc.nedaire.content.renderer.EssenceRender;
import com.arcanc.nedaire.util.helpers.PacketHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.joml.Vector3d;

public class MessageEssenceParticle implements IMessage 
{

	private final Vector3d startPoint;
	private final Vector3d finishPoint;
	private final boolean destroy;
	
	public MessageEssenceParticle(Vector3d start, Vector3d finish, boolean destroy) 
	{
		this.startPoint = start;
		this.finishPoint = finish;
		this.destroy = destroy;
	}
	
	public MessageEssenceParticle(FriendlyByteBuf buf) 
	{
		this.startPoint = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		this.finishPoint = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		this.destroy = buf.readBoolean();
	}
	
	@Override
	public void toBytes(FriendlyByteBuf buf) 
	{
		PacketHelper.writeVector3d(buf, startPoint);
		PacketHelper.writeVector3d(buf, finishPoint);
		buf.writeBoolean(destroy);
	}

	@Override
	public void process(CustomPayloadEvent.Context context)
	{
		context.enqueueWork(() ->
		{
			if (!destroy)
				EssenceRender.addNewPointPatricle(startPoint, finishPoint);
			else
				EssenceRender.removePointParticle(startPoint, finishPoint);
		});
	}

}

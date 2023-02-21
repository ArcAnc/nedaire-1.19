/**
 * @author ArcAnc
 * Created at: 2023-02-19
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.network.messages;

import java.util.Optional;
import java.util.function.Supplier;

import com.arcanc.nedaire.content.block.entities.NBEDeliveryStation;
import com.arcanc.nedaire.content.renderer.particle.delivery.FluidDeliveryParticle;
import com.arcanc.nedaire.content.renderer.particle.delivery.ItemDeliveryParticle;
import com.arcanc.nedaire.content.renderer.particle.delivery.VimDeliveryParticle;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.PacketHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class MessageDeliveryStationToClient implements IMessage 
{

	private Object obj;
	private Vec3 startPos;
	private Vec3 finishPos;
	private Vec3 station;
	private boolean toStation;
	
	public MessageDeliveryStationToClient(BlockPos startPos, BlockPos finishPos, BlockPos station, boolean toStation, Object obj)
	{
		this(new Vec3(startPos.getX() + .5d, startPos.getY() + .5d, startPos.getZ() +.5d),
			 new Vec3(finishPos.getX() + .5d, finishPos.getY() + .5d, finishPos.getZ() + .5d),
			 new Vec3(station.getX() + .5d, station.getY() + .5d, station.getZ() +.5d),
			 toStation, obj);
	}
	
	public MessageDeliveryStationToClient(Vec3 startPos, Vec3 finishPos, Vec3 station, boolean toStation, Object obj) 
	{
		this.startPos = startPos;
		this.finishPos = finishPos;
		this.station = station;
		this.toStation = toStation;
		if (obj != null && ((obj instanceof ItemStack) || (obj instanceof FluidStack) || (obj instanceof Integer)))
		{
			this.obj = obj;
		}
	}
	
	public MessageDeliveryStationToClient(FriendlyByteBuf buf) 
	{
		this.startPos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
		this.finishPos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
		this.station = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
		this.toStation = buf.readBoolean();
		int type = buf.readInt();
		this.obj = switch (type)
		{
			case 0 -> buf.readItem(); 
			case 1 -> buf.readFluidStack();
			case 2 -> buf.readInt();
			default -> null;
		};
	}
	
	@Override
	public void toBytes(FriendlyByteBuf buf) 
	{
		PacketHelper.writeVector3d(buf, startPos);
		PacketHelper.writeVector3d(buf, finishPos);
		PacketHelper.writeVector3d(buf, station);
		buf.writeBoolean(toStation);
		int mode = obj instanceof ItemStack ? 0 : obj instanceof FluidStack ? 1 : 2;
		buf.writeInt(mode);
		
		switch (mode) 
		{
			case 0 -> 
			{
				buf.writeItem((ItemStack)obj);
				break;
			}
			case 1 -> 
			{
				buf.writeFluidStack((FluidStack)obj);
				break;
			}
			case 2 ->
			{
				buf.writeInt((Integer)obj);
				break;
			}
			default -> {break;}
		}
		
	}

	@Override
	public void process(Supplier<Context> ctx) 
	{
		ctx.get().enqueueWork(() -> 
		{
			Minecraft mc = RenderHelper.mc();
			ClientLevel level = mc.level;
			
			Optional<BlockEntity> start = BlockHelper.castTileEntity(level, startPos, BlockEntity.class);
			Optional<BlockEntity> finish = BlockHelper.castTileEntity(level, finishPos, BlockEntity.class);
			Optional<NBEDeliveryStation> stat = BlockHelper.castTileEntity(level, station, NBEDeliveryStation.class);
			BlockHelper.castTileEntity(level, station, NBEDeliveryStation.class).ifPresent(st -> 
			{
				if (start.isPresent() && finish.isPresent() && stat.isPresent())
				{
					if(obj instanceof ItemStack stack)
					{
						ItemEntity ent = new ItemEntity(level, startPos.x(), startPos.y(), startPos.z(), stack, 0, 0, 0);
						ent.setNeverPickUp();
					
						ItemDeliveryParticle particle = new ItemDeliveryParticle(mc.getEntityRenderDispatcher(), mc.renderBuffers(), level, ent, finishPos, station, toStation); 
						
						mc.particleEngine.add(particle);
					}
					else if (obj instanceof FluidStack stack)
					{
						FluidDeliveryParticle particle = new FluidDeliveryParticle(level, startPos, finishPos, station, toStation, stack);
						
						mc.particleEngine.add(particle);
					}
					else if (obj instanceof Integer stack)
					{
						VimDeliveryParticle particle = new VimDeliveryParticle(level, startPos, finishPos, station, toStation, stack);
						
						mc.particleEngine.add(particle);
					}
				}
			});
		});
	}
}

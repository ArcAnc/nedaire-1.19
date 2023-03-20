/**
 * @author ArcAnc
 * Created at: 2023-02-05
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import org.joml.Vector3d;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.NServerTickerBlockEntity;
import com.arcanc.nedaire.content.network.NNetworkEngine;
import com.arcanc.nedaire.content.network.messages.MessageEssenceParticle;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class NBETerramorfer extends NBERedstoneSensitive implements IInventoryCallback, NServerTickerBlockEntity 
	{
	
	private final BlockPos[] poses;
	private long[] placedTime = new long[] { -1, -1, -1, -1 };
	private final int morfTime = 100;

	public NBETerramorfer(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_TERRAMORFER.get(), pos, state);

		poses = new BlockPos[] { pos.north(2), pos.south(2), pos.west(2), pos.east(2) };
	}

	@Override
	public void tickServer() 
	{
		if (isPowered()) 
		{
			for (int q = 0; q < 4; q++) 
			{
				BlockState state = getLevel().getBlockState(poses[q]);
				if (state.getBlock() == Blocks.STONE) 
				{
					if (placedTime[q] == -1) 
					{
						placedTime[q] = getLevel().getGameTime();
						NNetworkEngine.sendToAllPlayers(new MessageEssenceParticle(new Vector3d(getBlockPos().getX() + 0.5d,
																								getBlockPos().getY() + 0.5d, 
																								getBlockPos().getZ() + 0.5d),
																				   new Vector3d(poses[q].getX() + 0.5d, 
																						   		poses[q].getY() + 0.5d,
																						   		poses[q].getZ() + 0.5d),
																				   false));
						setChanged();
					} 
					else 
					{
						if (getLevel().getGameTime() >= placedTime[q] + morfTime) 
						{
							getLevel().setBlockAndUpdate(poses[q],
									NRegistration.RegisterBlocks.SKYSTONE.getDefaultBlockState());
							getLevel().playSound(null, poses[q], SoundEvents.STONE_BREAK, SoundSource.MASTER, 3f, 0.5f);
							placedTime[q] = -1;
							NNetworkEngine.sendToAllPlayers(new MessageEssenceParticle(new Vector3d(getBlockPos().getX() + 0.5d,
																									getBlockPos().getY() + 0.5d, 
																									getBlockPos().getZ() + 0.5d),
																					   new Vector3d(poses[q].getX() + 0.5d, 
																							   		poses[q].getY() + 0.5d,
																							   		poses[q].getZ() + 0.5d),
																					   true));
							setChanged();
						}
					}
				} 
				else 
				{
					placedTime[q] = -1;
				}
			}
		}
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) 
	{
		super.onDataPacket(net, pkt);
		
/*		 if (this.hasLevel() && this.getLevel().isClientSide()) 
		 { 
			 for (int q = 0; q < 4; q++) 
			 { 
				 if(placedTime[q] != -1) 
				 { 
					 EssenseRender.addNewPoint(new Vec3(getBlockPos().getX() + 0.5d, getBlockPos().getY() + 0.5d,
							 getBlockPos().getZ() + 0.5d), new Vec3(poses[q].getX() + 0.5d,
									 poses[q].getY() + 0.5d, poses[q].getZ() + 0.5d)); 
				 } 
				 else
				 {
					 EssenseRender.removePoint(new Vec3(getBlockPos().getX() + 0.5d, getBlockPos().getY() + 0.5d, 
							 getBlockPos().getZ() + 0.5d), new Vec3(poses[q].getX() + 0.5d, poses[q].getY() + 0.5d, 
									 poses[q].getZ() + 0.5d)); 
				 } 
			 }
		  
		 }
*/	}

	@Override
	public void readCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.readCustomTag(tag, descPacket);
		for (int q = 0; q < 4; q++) 
		{
			if (tag.contains(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Terramorfer.PLACED_TIME + "_" + q)) 
			{
				placedTime[q] = tag.getLong(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Terramorfer.PLACED_TIME + "_" + q);
			}
		}
	}

	@Override
	public void writeCustomTag(CompoundTag tag, boolean descPacket) 
	{
		super.writeCustomTag(tag, descPacket);

		for (int q = 0; q < 4; q++)
			tag.putLong(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Terramorfer.PLACED_TIME + "_" + q, placedTime[q]);
	}

}

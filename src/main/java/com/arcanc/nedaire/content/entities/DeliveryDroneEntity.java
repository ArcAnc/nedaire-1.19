/**
 * @author ArcAnc
 * Created at: 2022-11-01
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.entities;

import java.util.Optional;

import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

public class DeliveryDroneEntity extends PathfinderMob 
{
	/*FIXME: добавить логику работу, починить модель (там я не совсем верно указал аниманию) и прописать остальные запчасти, такие как коллизия и получение урона*/
	private static final EntityDataAccessor<Optional<BlockPos>> BASE_POS = SynchedEntityData.defineId(DeliveryDroneEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
	private static final EntityDataAccessor<Optional<BlockPos>> TARGET_POS = SynchedEntityData.defineId(DeliveryDroneEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

	public DeliveryDroneEntity(EntityType<? extends DeliveryDroneEntity> type, Level level) 
	{
		super(type, level);
		this.moveControl = new FlyingMoveControl(this, 20, true);
	}
	
	public DeliveryDroneEntity(Level level, float x, float y, float z)
	{
		this(NRegistration.RegisterEntities.DELIVERY_DRONE.get(), level);
	}
	
	@Override
	protected void registerGoals() 
	{
	      this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 6.0F));
	}

	public static void createAttributes(EntityAttributeCreationEvent event) 
	{
		event.put(NRegistration.RegisterEntities.DELIVERY_DRONE.get(), 
		Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).
		add(Attributes.FLYING_SPEED, (double)0.6F).
		add(Attributes.MOVEMENT_SPEED, (double)0.3F).
		add(Attributes.FOLLOW_RANGE, 48.0D).
		build());
		
	}

	
}

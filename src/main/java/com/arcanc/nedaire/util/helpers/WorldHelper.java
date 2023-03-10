/**
 * @author ArcAnc
 * Created at: 2022-11-03
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WorldHelper 
{
	public static final RandomSource RANDOM_SOURCE = RandomSource.createNewThreadLocalInstance();
	
	public static ItemEntity spawnItemEntity(Level level, Vec3 pos, Vec3 velocity, ItemStack stack)
	{
		return spawnItemEntity(level, pos.x(), pos.y(), pos.z(), velocity.x(), velocity.y(), velocity.z(), stack);
	}
	
	public static ItemEntity spawnItemEntity(Level level, double posX, double posY, double posZ, ItemStack stack)
	{
		return spawnItemEntity(level, posX, posY, posZ, level.random.nextDouble() * 0.2D - 0.1D, 0.2D, level.random.nextDouble() * 0.2D - 0.1D, stack);
	}
	
	public static ItemEntity spawnItemEntity(Level level, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ, ItemStack stack)
	{
		ItemEntity item = new ItemEntity(level, posX, posY, posZ, stack, velocityX, velocityY, velocityZ); 
		level.addFreshEntity(item);
		return item;
	}
}

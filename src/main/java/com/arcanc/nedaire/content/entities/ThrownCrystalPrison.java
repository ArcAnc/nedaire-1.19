/**
 * @author ArcAnc
 * Created at: 2023-02-24
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.entities;

import javax.annotation.Nullable;

import com.arcanc.nedaire.content.item.CrystalPrisonItem;
import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownCrystalPrison extends ThrowableItemProjectile 
{

	public ThrownCrystalPrison(EntityType<? extends ThrowableItemProjectile> type, Level level) 
	{
		super(type, level);
	}

	public ThrownCrystalPrison(Level level, LivingEntity player)
	{
		super(NRegistration.RegisterEntities.CRYSTAL_PRISON.get(), player, level);
	}
	
	@Override
	protected Item getDefaultItem() 
	{
		return NRegistration.RegisterItems.CRYSTAL_PRISON.get();
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) 
	{
		super.onHitEntity(hitResult);
		Entity entity = hitResult.getEntity();
		ItemStack stack = this.getItem();
		CompoundTag tag = stack.getOrCreateTag();
		
		CompoundTag data = tag.getCompound(CrystalPrisonItem.ENTITY_DATA);
		if (data.isEmpty())
		{
			if ((entity instanceof Mob) && !CrystalPrisonItem.BLOCK_LIST.contains(entity.getType()))
			{
				tryCatchEntity(hitResult);
			}
		}
		else
		{
			entity.hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
			tryReleaseEntity(hitResult);
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult hitResult) 
	{
		super.onHitBlock(hitResult);
		ItemStack stack = this.getItem();

		CompoundTag data = stack.getOrCreateTag().getCompound(CrystalPrisonItem.ENTITY_DATA);
		
		if (!data.isEmpty())
		{
			tryReleaseEntity(hitResult);
		}
		else
		{
	        this.getLevel().addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack, 0, 0.25f, 0));
		}
 	}
	
	private void tryReleaseEntity(HitResult hitResult)
	{
		Level level = this.getLevel();
		
		if (!level.isClientSide())
		{
			ItemStack stack = this.getItem();
			
			CompoundTag data = stack.getOrCreateTag().getCompound(CrystalPrisonItem.ENTITY_DATA);

			EntityType.create(data, level).ifPresent(entity ->
			{
				entity.setPos(hitResult.getLocation());
				level.addFreshEntity(entity);
			});
			
			stack.getOrCreateTag().put(CrystalPrisonItem.ENTITY_DATA, new CompoundTag());
	        level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack, 0, 0.25f, 0));
 		}
	}

	private void tryCatchEntity(EntityHitResult hitResult) 
	{
		Level level = this.getLevel();
		ItemStack stack = this.getItem();
		CompoundTag data = new CompoundTag();
			
		Entity ent = hitResult.getEntity();
			
		ent.save(data);
			
		stack.getOrCreateTag().put(CrystalPrisonItem.ENTITY_DATA, data);
		
        data.put("Pos", this.newDoubleList(hitResult.getLocation().x(), hitResult.getLocation().y(), hitResult.getLocation().z()));
        data.put("Motion", this.newDoubleList(0, 0, 0));
		
		ent.discard();
			
		if (!getLevel().isClientSide()) 
		{
			level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack, 0, 0.25f, 0));
		}
	}

	protected void onHit(HitResult hitResult) 
	{
		super.onHit(hitResult);

		for(int i = 0; i < 32; ++i) 
		{
			this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
		}
		this.discard();
	}

	
	@Override
	public void tick() 
	{
		Entity entity = this.getOwner();
		if (entity instanceof Player && !entity.isAlive()) 
		{
			this.discard();
		} 
		else 
		{
			super.tick();
		}
	}

	@Nullable
	@Override
	public Entity changeDimension(ServerLevel level, net.minecraftforge.common.util.ITeleporter teleporter) 
	{
		Entity entity = this.getOwner();
		if (entity != null && entity.level.dimension() != level.dimension()) 
		{
			this.setOwner((Entity)null);
		}
		return super.changeDimension(level, teleporter);
	}
}

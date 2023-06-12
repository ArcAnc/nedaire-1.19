/**
 * @author ArcAnc
 * Created at: 2023-02-19
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.particle.delivery;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ItemDeliveryParticle extends DeliveryParticle<ItemStack> 
{
	private final Entity item;
	
	private final EntityRenderDispatcher entityRenderDispatcher;
	private final RenderBuffers renderBuffers;

	public ItemDeliveryParticle(EntityRenderDispatcher renderDispatcher, RenderBuffers buffers, ClientLevel level, Entity item, Vec3 finishPos, Vec3 station, boolean toStation) 
	{
		super(level, item.position(), finishPos, station, toStation);
		this.renderBuffers = buffers;
		this.item = item;
		this.entityRenderDispatcher = renderDispatcher;	
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		this.item.setPos(this.x, this.y, this.z);
//		this.level.addAlwaysVisibleParticle(ParticleTypes.DRAGON_BREATH, this.x, this.y + 0.2d, this.z, 0, -0.015d, 0);
	}
	
	@Override
	protected ItemStack getContent() 
	{
		return ((ItemEntity)item).getItem();
	}
	
	@Override
	public void render(@NotNull VertexConsumer vertex, Camera camera, float partialTicks)
	{
		double dx = Mth.lerp(partialTicks, this.xo, this.x);
		double dy = Mth.lerp(partialTicks, this.yo, this.y);
		double dz = Mth.lerp(partialTicks, this.zo, this.z);
		
		MultiBufferSource.BufferSource multibuffersource$buffersource = this.renderBuffers.bufferSource();
		this.entityRenderDispatcher.render(this.item, 
				dx - camera.getPosition().x(), dy - camera.getPosition().y(), dz - camera.getPosition().z(), this.item.getYRot(), 
	    		partialTicks, 
	    		new PoseStack(), 
	    		multibuffersource$buffersource, 
	    		LightTexture.FULL_BRIGHT);
	    multibuffersource$buffersource.endBatch();
	}
}

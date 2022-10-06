/**
 * @author ArcAnc
 * Created at: 2022-09-20
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.book.parts;

import javax.annotation.Nullable;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchElementEntity extends EnchElementAbstract 
{
	/** FIXME: fix entity drawing. Some strange shit happens with it. Wrong position placement*/
	
	protected EntityType<?> type;
	private final Lazy<RenderData> renderData;
	
	public EnchElementEntity(EnchiridionInstance instance, EntityType<?> type, @Nullable CompoundTag entityData, int x, int y, int width, int height) 
	{
		super(instance, x, y, width, height);
		
		setBaseShiftX(45);
		
		this.type = type;

		this.renderData = Lazy.of(() -> new RenderData(type, entityData));
	}
	
	public EnchElementEntity(EnchiridionInstance instance, EntityType<?> type, int x, int y, int width, int height) 
	{
		this(instance, type, null, x, y, width, height);
	}
	
	public EnchElementEntity(EnchiridionInstance instance, EntityType<?> type, int x, int y) 
	{
		this(instance, type, null, x, y, 40, 40);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDraw(PoseStack pos, int mouseX, int mouseY, float f) 
	{
		
		// Entity rendering code was largely borrowed from JustEnoughResources by way2muchnoise
		
/*		PoseStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.pushPose();
		modelViewStack.mulPoseMatrix(pos.last().pose());
		modelViewStack.translate(x + 60, y + yOff, 50);
		modelViewStack.scale(-scale, scale, scale);

		PoseStack mobPoseStack = new PoseStack();
		mobPoseStack.mulPose(Vector3f.ZP.rotationDegrees(180));

		float pitch = (yOff/2) - mouseY;
		float yaw = 60 - mouseX;

		mobPoseStack.mulPose(Vector3f.XN.rotationDegrees(((float)Math.atan((pitch / 40f))) * 20f));
		entity.yo = (float)Math.atan(yaw / 40f) * 20f;
		float yRot = (float)Math.atan(yaw / 40f) * 40f;
		float xRot = -((float)Math.atan(pitch / 40f)) * 20f;
		entity.setYRot(yRot);
		entity.setYRot(yRot);
		entity.setXRot(xRot);
		if(entity instanceof LivingEntity)
		{
			((LivingEntity)entity).yHeadRot = yRot;
			((LivingEntity)entity).yHeadRotO = yRot;
			((LivingEntity)entity).yBodyRot = yRot;
		}

		RenderSystem.applyModelViewMatrix();
		EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		entityRenderDispatcher.setRenderShadow(false);
		MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> {
			entityRenderDispatcher.render(entity, 0, 0, 0, 0, 1, mobPoseStack, bufferSource, 15728880);
		});
		bufferSource.endBatch();
		entityRenderDispatcher.setRenderShadow(true);
		modelViewStack.popPose();
		RenderSystem.applyModelViewMatrix();
*/		
		Entity entity = renderData.get().entity;
		float scale = renderData.get().scale;
		PoseStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.pushPose();
		modelViewStack.mulPoseMatrix(pos.last().pose());
		modelViewStack.translate(this.x + (this.width / 2) + (entity.getBbWidth() * 16) / 4, this.y + (this.height / 2) + (entity.getBbHeight() * 16) / 2 , 50);
		modelViewStack.scale(-scale < 1 ? scale * entity.getBbWidth() : scale / entity.getBbWidth(), entity.getBbHeight() > 1 ? scale / entity.getBbHeight() : scale * entity.getBbHeight(), scale);
		PoseStack mobPoseStack = new PoseStack();
		mobPoseStack.mulPose(Vector3f.ZP.rotationDegrees(180));

		float pitch = this.y + (this.height / 2) + (entity.getBbHeight() * 16) / 4 - mouseY;
		float yaw = this.x + (this.width / 2) + (entity.getBbWidth() * 16) / 4 - mouseX;

		mobPoseStack.mulPose(Vector3f.XN.rotationDegrees(((float)Math.atan((pitch/40f)))*20f));
		entity.yo = (float)Math.atan(yaw/40f)*20f;
		float yRot = -(float)Math.atan(yaw/40f)*40f;
		float xRot = -((float)Math.atan(pitch/40f))*20f;
		entity.setYRot(yRot);
		entity.setXRot(xRot);
		if(entity instanceof LivingEntity)
		{
			((LivingEntity)entity).yHeadRot = yRot;
			((LivingEntity)entity).yHeadRotO = yRot;
			((LivingEntity)entity).yBodyRot = yRot;
		}

		RenderSystem.applyModelViewMatrix();
		EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		entityRenderDispatcher.setRenderShadow(false);
		MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> {
			entityRenderDispatcher.render(entity, 0, 0, 0, 0, 0.5f, mobPoseStack, bufferSource, 15728880);
		});
		bufferSource.endBatch();
		entityRenderDispatcher.setRenderShadow(true);
		modelViewStack.popPose();
		RenderSystem.applyModelViewMatrix();
		
		if (isHovered())
		{
			ench.getScreen().renderTooltip(pos, type.getDescription(), (int) mouseX, (int) mouseY);
		}
		
	}

	@Override
	protected boolean hovered(int mouseX, int mouseY) 
	{
		return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;	
	}
	
	public static String enchModule(EntityType<?> entity)
	{
		return String.format(" </entity;%s/> ", ForgeRegistries.ENTITY_TYPES.getKey(entity).toString());
	}
	
	public EnchElementEntity decode(ResourceLocation loc) 
	{
		return null;
	}
	
	private static class RenderData
	{
		final Entity entity;
		final float entitySize;
		final float scale;

		RenderData(EntityType<? extends Entity> entityType, CompoundTag entityData)
		{
			Minecraft mc = RenderHelper.mc();
			this.entity = entityType.create(mc.level);
			if(entityData != null)
				this.entity.load(entityData);
			this.entitySize = Math.max(entity.getBbWidth(), entity.getBbHeight());
			this.scale = entitySize <= 1?36: entitySize <= 3?28: 26f-entitySize;
		}
	}

	@Override
	public void onPress(double mouseX, double mouseY) 
	{
		
	}
}

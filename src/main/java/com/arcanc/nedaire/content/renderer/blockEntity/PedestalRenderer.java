/**
 * @author ArcAnc
 * Created at: 2022-04-10
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import java.util.Set;

import org.joml.Matrix4f;

import com.arcanc.nedaire.content.block.entities.NBEPedestal;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper.TintedVertexConsumer;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class PedestalRenderer implements BlockEntityRenderer<NBEPedestal> 
{

	public PedestalRenderer(BlockEntityRendererProvider.Context ctx) 
	{
	}
	
	@Override
	public void render(NBEPedestal blockEntity, float partialTicks, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) 
	{
		if (blockEntity != null)
		{
			if (!ItemHelper.isEmpty(blockEntity))
			{
				LazyOptional<IItemHandler> handler = ItemHelper.getItemHandler(blockEntity);
				
				handler.ifPresent( stackHandler ->
				{
					int amount = 0;
					Set<Integer> presentSlots = Sets.newHashSet();
					for (int q = 0; q < stackHandler.getSlots(); q++)
					{
						if (!stackHandler.getStackInSlot(q).isEmpty())
						{
							amount++;
							presentSlots.add(q);
						}
					}

					
					if (amount == 0)
					{
						return;
					}
					else if (amount == 1)
					{
						renderProjector(blockEntity, partialTicks, mStack, buffer);
						renderSolo(blockEntity, partialTicks, mStack, buffer, combinedLight, combinedOverlay, stackHandler, presentSlots);
					}
					else
					{
						renderProjector(blockEntity, partialTicks, mStack, buffer);
						renderMore(blockEntity, partialTicks, mStack, buffer, combinedLight, combinedOverlay, stackHandler, amount, presentSlots);
					}
				});
			}
			
		}
	}

	private void renderProjector(NBEPedestal blockEntity, float partialTicks, PoseStack mStack, MultiBufferSource buffer)
	{
		Minecraft mc = RenderHelper.mc();
		Vec3 playerPos = mc.player.getPosition(partialTicks);
		BlockPos pos = blockEntity.getBlockPos();
        double xDifference = playerPos.x - (pos.getX() + 0.5D);
        double zDifference = playerPos.z - (pos.getZ() + 0.5D);
        float rotation = (float) Math.toDegrees(Math.atan2(zDifference, xDifference));
		VertexConsumer builder = buffer.getBuffer(RenderType.lightning());
		
		mStack.pushPose();

		mStack.translate(0.5f, 0.75f, 0.5f);
		mStack.mulPose(Axis.YP.rotationDegrees(-rotation));
        
		Matrix4f matrix4f = mStack.last().pose();
		builder.vertex(matrix4f, 0.0f, 0.0f, 0.0f).color(255, 0, 255, 255).endVertex();
		builder.vertex(matrix4f, 0.0f, 0.6f, -0.6f).color(255, 0, 255, 0).endVertex();
		builder.vertex(matrix4f, 0.0f, 0.6f, 0.6f).color(255, 0, 255, 0).endVertex();
		builder.vertex(matrix4f, 0.0f, 0.0f, 0.0f).color(255, 0, 255, 255).endVertex();
		
		
		mStack.popPose();
	}
	
	private void renderSolo(NBEPedestal blockEntity, float partialTicks, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, IItemHandler handler, Set<Integer> presentSlots)
	{
		presentSlots.forEach( i -> 
		{
			ItemStack stack = handler.getStackInSlot(i);
			
			
			mStack.pushPose();
			mStack.translate(0.5f, 1.1f, 0.5f);
			mStack.translate(0, Math.sin((blockEntity.getLevel().getGameTime() + partialTicks) / 10.0F) * 0.1 + 0.1, 0); //Item bobbing
			mStack.scale(0.75F, 0.75F, 0.75F);
	        long time = blockEntity.getLevel().getGameTime();
	        float angle = (time) % 360 + partialTicks;
	        mStack.mulPose(Axis.YP.rotationDegrees(angle));
	        
	        BakedModel model = RenderHelper.renderItem().getModel(stack, blockEntity.getLevel(), null, 0);
            RenderHelper.renderItem().render(
            		stack,
            		ItemDisplayContext.GROUND,
            		false,
            		mStack,
            		renderType -> new TintedVertexConsumer(buffer.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS)), 1f, 0.5f, 1f, 175f/255f),
            		combinedLight, 
            		combinedOverlay,
            		model);
	        mStack.popPose();
			
		});
	}
	
	private void renderMore(NBEPedestal blockEntity, float partialTicks, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, IItemHandler stackHandler, int amount, Set<Integer> presentSlots) 
	{
		float offsetPerItem = 360/amount;
		float ticks = (blockEntity.getLevel().getGameTime() + partialTicks) * 0.5f;
		
		final float modifier = 6F;
		final float rotationModifier = 0.25F;
		final float radiusBase = 0.75F;
		final float radiusMod = 0.1F;					
		
		mStack.pushPose();
		mStack.translate(0.5f, 1.1f, 0.5f);
		
		int q = 0;
		for (int i : presentSlots)
		{
			float offset = offsetPerItem * q;
			float deg = (int) (ticks / rotationModifier % 360F + offset);
			float rad = deg * (float)Math.PI /180f;
			float radiusX = (float) (radiusBase + radiusMod * Math.sin(ticks/modifier));
			float radiusZ = (float) (radiusBase + radiusMod * Math.cos(ticks/modifier));
			float x = (float) (radiusX * Math.cos(rad));
			float z = (float) (radiusZ * Math.sin(rad));
			float y = (float) Math.cos((ticks + 50 * q) / 5f) / 10f;
			
			mStack.pushPose();
			mStack.translate(x, y, z);
			mStack.scale(0.75F, 0.75F, 0.75F);
			
			mStack.mulPose(Axis.YP.rotationDegrees((ticks * q) % 360));
			
			ItemStack stack = stackHandler.getStackInSlot(i);
	        
	        BakedModel model = RenderHelper.renderItem().getModel(stack, blockEntity.getLevel(), null, 0);
            RenderHelper.renderItem().render(
            		stack,
            		ItemDisplayContext.GROUND,
            		false,
            		mStack,
            		renderType -> new TintedVertexConsumer(buffer.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS)), 1f, 0.5f, 1f, 175f/255f),
            		combinedLight, 
            		combinedOverlay,
            		model);
            
			mStack.popPose();
			
			q++;
		}
		mStack.popPose();		
	}


}

/**
 * @author ArcAnc
 * Created at: 2022-04-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import org.joml.Quaternionf;

import com.arcanc.nedaire.content.block.entities.NBEHolder;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HolderRenderer implements BlockEntityRenderer<NBEHolder>  
{

	public HolderRenderer(BlockEntityRendererProvider.Context ctx) 
	{
	}
	
	@Override
	public void render(NBEHolder blockEntity, float partialTicks, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) 
	{
		
		if(blockEntity != null)
		{
			if (!ItemHelper.isEmpty(blockEntity))
			{
				ItemHelper.getItemHandler(blockEntity).ifPresent(handler -> 
				{
					ItemStack stack = handler.getStackInSlot(0);

					mStack.pushPose();
					mStack.translate(0.5f, 0.15f, 0.5f);
					mStack.translate(0, Math.sin((blockEntity.getLevel().getGameTime()) / 10.0F) * 0.1 + 0.1, 0); //Item bobbing
					mStack.scale(0.75F, 0.75F, 0.75F);
			        long time = blockEntity.getLevel().getGameTime();
			        float angle = (time) % 360;
			        mStack.mulPose(new Quaternionf().fromAxisAngleDeg(angle, 0, 1, 0));
			        RenderHelper.renderItem().renderStatic(
			        		stack, 
			        		ItemDisplayContext.GROUND, 
			        		combinedLight, 
			        		combinedOverlay,
			        		mStack, 
			        		buffer,
			        		blockEntity.getLevel(),
			        		0);
			        mStack.popPose();
					
				});
			}
		}
	}

}

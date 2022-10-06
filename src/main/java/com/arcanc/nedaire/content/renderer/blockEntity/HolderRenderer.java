/**
 * @author ArcAnc
 * Created at: 2022-04-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import com.arcanc.nedaire.content.block.entities.ModBEHolder;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class HolderRenderer implements BlockEntityRenderer<ModBEHolder>  
{

	public HolderRenderer(BlockEntityRendererProvider.Context ctx) 
	{
	}
	
	@Override
	public void render(ModBEHolder blockEntity, float partialTicks, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) 
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
			        mStack.mulPose(Vector3f.YP.rotationDegrees(angle));
			        Minecraft.getInstance().getItemRenderer().renderStatic(
			        		stack, 
			        		ItemTransforms.TransformType.GROUND, 
			        		combinedLight, 
			        		combinedOverlay,
			        		mStack, 
			        		buffer, 
			        		0);
			        mStack.popPose();
					
				});
			}
		}
	}

}

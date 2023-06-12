/**
 * @author ArcAnc
 * Created at: 2023-03-25
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import com.arcanc.nedaire.content.block.entities.NBEExpExtractor;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


public class ExpExtractorRenderer implements BlockEntityRenderer<NBEExpExtractor> 
{
  
    private static final RandomSource RANDOM_SOURCE = RandomSource.create();
	private static final float HEIGHT_1 = 3/16f;
	private static final float HEIGHT_2 = 6/16f;
	private static final float HEIGHT_3 = 11/16f;
	private static final float HEIGHT_4 = 14/16f;
    
	public ExpExtractorRenderer(BlockEntityRendererProvider.Context ctx)
	{
	}

	@Override
	public void render(@NotNull NBEExpExtractor tile, float partialTicks, @NotNull PoseStack mStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		Direction dir = tile.getBlockState().getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING);

		if (tile.getBlockState().getValue(BlockHelper.BlockProperties.LIT))
		{
			renderLightning(mStack, buffer, HEIGHT_1, dir);
			renderLightning(mStack, buffer, HEIGHT_2, dir);
			renderLightning(mStack, buffer, HEIGHT_3, dir);
			renderLightning(mStack, buffer, HEIGHT_4, dir);
		}
		else
		{
			if (RANDOM_SOURCE.nextInt(10) % 10 == 0)
			{
				renderLightning(mStack, buffer, HEIGHT_1, dir);
				renderLightning(mStack, buffer, HEIGHT_2, dir);
				renderLightning(mStack, buffer, HEIGHT_3, dir);
				renderLightning(mStack, buffer, HEIGHT_4, dir);

			}
		}
	}

	
	private void renderLightning(PoseStack mStack, MultiBufferSource buffer, float height, Direction dir)
	{
		mStack.pushPose();
		Matrix4f matrix = mStack.last().pose();
		Matrix3f normal = mStack.last().normal();
	    VertexConsumer builder = buffer.getBuffer(RenderType.lineStrip());
	    
	    boolean bool = dir.getAxis() == Direction.Axis.X;
	    
		builder.vertex(matrix, bool ? 0.5f : 1f / 16f, height, !bool ? 0.5f : 1f / 16f).color(255, 0, 255, 255).normal(normal, 1, 1, 1).endVertex();
		
		int count = 13;
		for (int q = 0; q < count; q++)
		{
			float h = height + (2f / 16f) * RANDOM_SOURCE.nextFloat();
			builder.vertex(matrix, bool ? 0.5f : (2 + q) / 16f, h, !bool ? 0.5f : (2 + q)/16f).color(255, 0, 255, 255).normal(normal, 1, 1, 1).endVertex();
		}
		
		builder.vertex(matrix, bool ? 0.5f : 15f / 16f, height, !bool? 0.5f : 15f / 16f).color(255, 0, 255, 255).normal(normal, 1, 1, 1).endVertex();
		mStack.popPose();
	}
}

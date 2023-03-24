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
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ExpExtractorRenderer implements BlockEntityRenderer<NBEExpExtractor> 
{
    
	public ExpExtractorRenderer(BlockEntityRendererProvider.Context ctx) 
	{
	}

	@Override
	public void render(NBEExpExtractor tile, float partialTicks, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		// FIXME: add electricity between stands 
		
	}

}

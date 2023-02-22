/**
 * @author ArcAnc
 * Created at: 2023-02-22
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.arcanc.nedaire.content.block.entities.NBEFluidStorage;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class FluidStorageRenderer implements BlockEntityRenderer<NBEFluidStorage>  
{
    private static final float MIN_X =  3.01F/16F;
    private static final float MAX_X = 12.99F/16F;
    private static final float MIN_Y =  0.01F/16F;
    private static final float MAX_Y = 15.99F/16F;
    private static final float MIN_Z =  3.01F/16F;
    private static final float MAX_Z = 12.99F/16F;

    private static final float MIN_UV_T =  3.01F;
    private static final float MAX_UV_T = 12.99F;
    private static final float MIN_U_S  =  3.01F / 2F;
    private static final float MAX_U_S  = 12.99F / 2F;
    private static final float MIN_V_S  =  0.01F / 2F;
    private static final float MAX_V_S  = 15.99F / 2F;
	
    public FluidStorageRenderer(BlockEntityRendererProvider.Context ctx) 
	{
	}
	
	@Override
	public void render(NBEFluidStorage blockEntity, float partialTicks, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		if (blockEntity != null)
		{
			FluidHelper.getFluidHandler(blockEntity).ifPresent(handler -> 
			{
				FluidStack stack = handler.getFluidInTank(0);
				if (!stack.isEmpty())
				{
					renderContent(stack, (float)stack.getAmount()/handler.getTankCapacity(0), mStack, buffer, combinedLight, combinedOverlay);
				}
			});
		}
	}

	private void renderContent(FluidStack stack, float height, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		IClientFluidTypeExtensions renderProps = IClientFluidTypeExtensions.of(stack.getFluid());

		ResourceLocation stillTex = renderProps.getStillTexture();
		TextureAtlasSprite still = RenderHelper.mc().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTex);

		ResourceLocation flowTex = renderProps.getFlowingTexture();
		TextureAtlasSprite flow = RenderHelper.mc().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(flowTex);
		

		boolean gas = stack.getFluid().getFluidType().isLighterThanAir();
		int[] color = RenderHelper.splitRGBA(renderProps.getTintColor());

        VertexConsumer builder = buffer.getBuffer(Sheets.translucentCullBlockSheet());
        Matrix4f matrix = mStack.last().pose();
        Matrix3f normal = mStack.last().normal();	
        
        drawTop(builder, matrix, normal, height, still, color, gas, combinedOverlay, combinedLight);
        drawSides(builder, matrix, normal, height, flow, color, gas, combinedOverlay, combinedLight);
	}
	
    private void drawTop(VertexConsumer builder, Matrix4f matrix, Matrix3f normal, float height, TextureAtlasSprite tex, int[] color, boolean gas, int overlay, int light)
    {
        float minX = gas ? MAX_X : MIN_X;
        float maxX = gas ? MIN_X : MAX_X;
        float y = MIN_Y + (gas ? (1F - height) * (MAX_Y - MIN_Y): height * (MAX_Y - MIN_Y));
        float ny = gas ? -1 : 1;

        float minU = tex.getU(MIN_UV_T);
        float maxU = tex.getU(MAX_UV_T);
        float minV = tex.getV(MIN_UV_T);
        float maxV = tex.getV(MAX_UV_T);

        builder.vertex(matrix, maxX, y, MIN_Z).color(color[0], color[1], color[2], color[3]).uv(minU, minV).overlayCoords(overlay).uv2(light).normal(normal, 0, ny, 0).endVertex();
        builder.vertex(matrix, minX, y, MIN_Z).color(color[0], color[1], color[2], color[3]).uv(maxU, minV).overlayCoords(overlay).uv2(light).normal(normal, 0, ny, 0).endVertex();
        builder.vertex(matrix, minX, y, MAX_Z).color(color[0], color[1], color[2], color[3]).uv(maxU, maxV).overlayCoords(overlay).uv2(light).normal(normal, 0, ny, 0).endVertex();
        builder.vertex(matrix, maxX, y, MAX_Z).color(color[0], color[1], color[2], color[3]).uv(minU, maxV).overlayCoords(overlay).uv2(light).normal(normal, 0, ny, 0).endVertex();
    }

    private void drawSides(VertexConsumer builder, Matrix4f matrix, Matrix3f normal, float height, TextureAtlasSprite tex, int[] color, boolean gas, int overlay, int light)
    {
        float minY = gas ? MAX_Y - (height * (MAX_Y - MIN_Y)) : MIN_Y;
        float maxY = gas ? MAX_Y : MIN_Y + height * (MAX_Y - MIN_Y);

        float minU = tex.getU(MIN_U_S);
        float maxU = tex.getU(MAX_U_S);
        float minV = tex.getV(MIN_V_S);
        float maxV = tex.getV(MIN_V_S + height * (MAX_V_S - MIN_V_S));

        //North
        builder.vertex(matrix, MIN_X, maxY, MIN_Z).color(color[0], color[1], color[2], color[3]).uv(minU, minV).overlayCoords(overlay).uv2(light).normal(normal,  0, 0, -1).endVertex();
        builder.vertex(matrix, MAX_X, maxY, MIN_Z).color(color[0], color[1], color[2], color[3]).uv(maxU, minV).overlayCoords(overlay).uv2(light).normal(normal,  0, 0, -1).endVertex();
        builder.vertex(matrix, MAX_X, minY, MIN_Z).color(color[0], color[1], color[2], color[3]).uv(maxU, maxV).overlayCoords(overlay).uv2(light).normal(normal,  0, 0, -1).endVertex();
        builder.vertex(matrix, MIN_X, minY, MIN_Z).color(color[0], color[1], color[2], color[3]).uv(minU, maxV).overlayCoords(overlay).uv2(light).normal(normal,  0, 0, -1).endVertex();

        //South
        builder.vertex(matrix, MAX_X, maxY, MAX_Z).color(color[0], color[1], color[2], color[3]).uv(minU, minV).overlayCoords(overlay).uv2(light).normal(normal,  0, 0,  1).endVertex();
        builder.vertex(matrix, MIN_X, maxY, MAX_Z).color(color[0], color[1], color[2], color[3]).uv(maxU, minV).overlayCoords(overlay).uv2(light).normal(normal,  0, 0,  1).endVertex();
        builder.vertex(matrix, MIN_X, minY, MAX_Z).color(color[0], color[1], color[2], color[3]).uv(maxU, maxV).overlayCoords(overlay).uv2(light).normal(normal,  0, 0,  1).endVertex();
        builder.vertex(matrix, MAX_X, minY, MAX_Z).color(color[0], color[1], color[2], color[3]).uv(minU, maxV).overlayCoords(overlay).uv2(light).normal(normal,  0, 0,  1).endVertex();

        //East
        builder.vertex(matrix, MAX_X, maxY, MIN_Z).color(color[0], color[1], color[2], color[3]).uv(minU, minV).overlayCoords(overlay).uv2(light).normal(normal,  1, 0,  0).endVertex();
        builder.vertex(matrix, MAX_X, maxY, MAX_Z).color(color[0], color[1], color[2], color[3]).uv(maxU, minV).overlayCoords(overlay).uv2(light).normal(normal,  1, 0,  0).endVertex();
        builder.vertex(matrix, MAX_X, minY, MAX_Z).color(color[0], color[1], color[2], color[3]).uv(maxU, maxV).overlayCoords(overlay).uv2(light).normal(normal,  1, 0,  0).endVertex();
        builder.vertex(matrix, MAX_X, minY, MIN_Z).color(color[0], color[1], color[2], color[3]).uv(minU, maxV).overlayCoords(overlay).uv2(light).normal(normal,  1, 0,  0).endVertex();

        //West
        builder.vertex(matrix, MIN_X, maxY, MAX_Z).color(color[0], color[1], color[2], color[3]).uv(minU, minV).overlayCoords(overlay).uv2(light).normal(normal, -1, 0,  0).endVertex();
        builder.vertex(matrix, MIN_X, maxY, MIN_Z).color(color[0], color[1], color[2], color[3]).uv(maxU, minV).overlayCoords(overlay).uv2(light).normal(normal, -1, 0,  0).endVertex();
        builder.vertex(matrix, MIN_X, minY, MIN_Z).color(color[0], color[1], color[2], color[3]).uv(maxU, maxV).overlayCoords(overlay).uv2(light).normal(normal, -1, 0,  0).endVertex();
        builder.vertex(matrix, MIN_X, minY, MAX_Z).color(color[0], color[1], color[2], color[3]).uv(minU, maxV).overlayCoords(overlay).uv2(light).normal(normal, -1, 0,  0).endVertex();
    }
	
}

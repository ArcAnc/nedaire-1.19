/**
 * @author ArcAnc
 * Created at: 2023-03-14
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.arcanc.nedaire.content.block.entities.NBEDiffuser;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class DiffuserRenderer implements BlockEntityRenderer<NBEDiffuser>
{

    private static final float MIN_X =     2f/16F;
    private static final float MAX_X =  14.0F/16F;
	private static final float MIN_Y = 	 4.1f/16f;
	private static final float MAX_Y =	  0.9375f;
    private static final float MIN_Z =     2f/16F;
    private static final float MAX_Z =  14.0F/16F;

    private static final float MIN_UV_T =      2F;
    private static final float MAX_UV_T =     14F;

    public DiffuserRenderer(BlockEntityRendererProvider.Context ctx) 
	{
	}
	
	@Override
	public void render(NBEDiffuser tile, float partialTicks, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) 
	{
		if (tile != null)
		{
			FluidHelper.getFluidHandler(tile).ifPresent(handler -> 
			{
				if (!FluidHelper.isEmpty(handler))
				{
					FluidStack fluid = handler.getFluidInTank(0);
					
					float height = fluid.getAmount() / (float)handler.getTankCapacity(0);
				
					IClientFluidTypeExtensions renderProps = IClientFluidTypeExtensions.of(fluid.getFluid());

					ResourceLocation stillTex = renderProps.getStillTexture();
					TextureAtlasSprite still = RenderHelper.mc().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTex);

					boolean gas = fluid.getFluid().getFluidType().isLighterThanAir();
					int[] color = RenderHelper.splitRGBA(renderProps.getTintColor());

			        VertexConsumer builder = buffer.getBuffer(Sheets.translucentCullBlockSheet());
			        Matrix4f matrix = mStack.last().pose();
			        Matrix3f normal = mStack.last().normal();	
					
					mStack.pushPose();
					
					drawTop(builder, matrix, normal, height, still, color, gas, combinedOverlay, combinedLight);
					
					mStack.popPose();
				}
			});
			
			ItemHelper.getItemHandler(tile).ifPresent(handler ->
			{
				if (!ItemHelper.isEmpty(handler))
				{
					ItemStack stack = handler.getStackInSlot(0);

					mStack.pushPose();
					mStack.translate(0.5f, 0.5f, 0.5f);
					mStack.translate(0, Math.sin((tile.getLevel().getGameTime()) / 10.0F) * 0.1 + 0.1, 0); //Item bobbing
					mStack.scale(0.75F, 0.75F, 0.75F);
			        long time = tile.getLevel().getGameTime();
			        float angle = (time) % 360 + partialTicks;
			        mStack.mulPose(Axis.YP.rotationDegrees(angle));
			        RenderHelper.renderItem().renderStatic(
			        		stack, 
			        		ItemDisplayContext.GROUND, 
			        		combinedLight, 
			        		combinedOverlay,
			        		mStack, 
			        		buffer,
			        		tile.getLevel(),
			        		0);
			        mStack.popPose();

				}
			});
		}
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

}

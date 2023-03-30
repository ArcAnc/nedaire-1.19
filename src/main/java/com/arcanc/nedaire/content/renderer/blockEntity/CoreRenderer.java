/**
 * @author ArcAnc
 * Created at: 2023-03-25
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.arcanc.nedaire.content.block.entities.NBETerramorfer;
import com.arcanc.nedaire.content.renderer.NRenderTypes;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec2;

public class CoreRenderer implements BlockEntityRenderer<NBETerramorfer>
{
	private static final ResourceLocation TEXTURE = StringHelper.getLocFStr("textures/block/core/core.png");
	private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);

	private static final Vector3f[] VERTEX = new Vector3f[] 
			{
					new Vector3f(-0.960938f, 3.174692f, 0.949169f),
					new Vector3f(-1.232939f, 6.093693f, 0.380769f),
					new Vector3f(-1.327039f, 4.877493f, -0.828231f),
					new Vector3f(-1.039739f, 1.973993f, -0.026031f),
					new Vector3f(-0.400538f, 7.695592f, -0.053330f),
					new Vector3f(-0.344239f, 5.518692f, 1.306469f),
					new Vector3f(1.269462f, 4.109593f, 0.155669f),
					new Vector3f(0.173262f, 6.038393f, -1.186932f),
					new Vector3f(0.573961f, 3.942593f, -1.584031f),
					new Vector3f(0.006462f, 2.185893f, -1.453931f),
					new Vector3f(0.030362f, -0.110407f, 0.003268f),
					new Vector3f(0.266562f, 2.014693f, 1.384569f),
					new Vector3f(-0.108438f, 3.823693f, 1.751069f),
					new Vector3f(0.953961f, 1.619193f, -0.074632f),
					new Vector3f(0.966461f, 2.524893f, -0.731832f),
					new Vector3f(0.907062f, 2.188293f, 0.622068f)
			};
	
	private static final Vec2[] UV = new Vec2[] 
			{
					new Vec2(0.8408f, 0.5050f),
					new Vec2(0.8088f, 0.7926f),
					new Vec2(0.6913f, 0.6846f),
					new Vec2(0.7223f, 0.4051f),
					new Vec2(0.7662f, 0.9782f),
					new Vec2(0.9493f, 0.7470f),
					new Vec2(0.2239f, 0.0215f),
					new Vec2(0.2384f, 0.4781f),
					new Vec2(0.0729f, 0.2310f),
					new Vec2(0.4736f, 0.6184f),
					new Vec2(0.5777f, 0.8381f),
					new Vec2(0.5405f, 0.4228f),
					new Vec2(0.6897f, 0.1681f),
					new Vec2(0.9087f, 0.3374f),
					new Vec2(0.9772f, 0.5400f),
					new Vec2(0.2366f, 0.7492f),
					new Vec2(0.1528f, 0.6560f),
					new Vec2(0.3107f, 0.6806f),
					new Vec2(0.4241f, 0.2639f),
					new Vec2(0.4822f, 0.4798f),
					new Vec2(0.4294f, 0.7068f),
					new Vec2(0.2598f, 0.9776f),
					new Vec2(0.0297f, 0.7352f),
					new Vec2(0.0237f, 0.5001f)
			};
	
	private static final Vector3f[] NORMAL = new Vector3f[] 
			{
					new Vector3f(-0.8283f, -0.1535f, 0.5388f),
					new Vector3f(-0.8901f, 0.3285f, 0.3157f),
					new Vector3f(-0.8473f, 0.0799f, -0.5251f),
					new Vector3f(-0.9343f, -0.3531f, -0.0482f),
					new Vector3f(-0.0096f, 0.9999f, 0.0008f),
					new Vector3f(0.0211f, 0.3562f, 0.9341f),
					new Vector3f(0.9713f, 0.1645f, 0.1715f),
					new Vector3f(0.3483f, 0.4770f, -0.8069f),
					new Vector3f(0.4223f, 0.0851f, -0.9024f),
					new Vector3f(-0.0726f, -0.3768f, -0.9234f),
					new Vector3f(-0.0131f, -0.9999f, -0.0047f),
					new Vector3f(0.1808f, -0.3727f, 0.9102f),
					new Vector3f(0.0351f, 0.0605f, 0.9975f),
					new Vector3f(0.9265f, -0.3667f, -0.0842f),
					new Vector3f(0.8747f, -0.2328f, -0.4251f),
					new Vector3f(0.8831f, -0.2173f, 0.4159f)
			}; 
	
	private static final Face[] MESH = new Face[] 
			{
					new Face(1,1,1,
							 2,2,2,
							 3,3,3),
					new Face(3,3,3,
							 4,4,4,
							 1,1,1),
					new Face(5,5,5,
							 2,2,2,
							 6,6,6),
					new Face(5,7,5,
							 7,8,7,
							 8,9,8),
					new Face(9,10,9,
							 3,3,3,
							 8,11,8),
					new Face(3,3,3,
							 9,10,9,
							 10,12,10),
					new Face(1,1,1,
							 4,4,4,
							 11,13,11),
					new Face(11,13,11,
							 12,14,12,
							 1,1,1),
					new Face(13,15,13,
							 1,1,1,
							 12,14,12),
					new Face(4,4,4,
							 10,12,10,
							 11,13,11),
					new Face(14,16,14,
							 15,17,15,
							 16,18,16),
					new Face(16,18,16,
							 15,17,15,
							 7,8,7),
					new Face(3,3,3,
							 2,2,2,
							 5,5,5),
					new Face(5,5,5,
							 8,11,8,
							 3,3,3),
					new Face(2,2,2,
							 1,1,1,
							 6,6,6),
					new Face(7,8,7,
							 5,7,5,
							 6,19,6),
					new Face(1,1,1,
							 13,15,13,
							 6,6,6),
					new Face(13,20,13,
							 16,18,16,
							 7,8,7),
					new Face(14,16,14,
							 12,21,12,
							 11,22,11),
					new Face(14,16,14,
							 10,23,10,
							 15,17,15),
					new Face(15,17,15,
							 10,23,10,
							 9,24,9),
					new Face(15,17,15,
							 9,24,9,
							 7,8,7),
					new Face(6,19,6,
							 13,20,13,
							 7,8,7),
					new Face(7,8,7,
							 9,24,9,
							 8,9,8),
					new Face(3,3,3,
							 10,12,10,
							 4,4,4),
					new Face(13,20,13,
							 12,21,12,
							 16,18,16),
					new Face(14,16,14,
							 16,18,16,
							 12,21,12),
					new Face(14,16,14,
							 11,22,11,
							 10,23,10)
			};
	
    private static final RandomSource RANDOM_SOURCE = RandomSource.create();
    
	public CoreRenderer(BlockEntityRendererProvider.Context ctx) 
	{
	}

	@Override
	public void render(NBETerramorfer blockEntity, float partialTicks, PoseStack mStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		if (blockEntity != null)
		{
			mStack.pushPose();

			mStack.translate(0.5f, 0.2f, 0.5f);
			mStack.scale(0.1f, 0.1f, 0.1f);
			mStack.mulPose(Axis.YP.rotationDegrees((blockEntity.getLevel().getGameTime() + partialTicks) % 360));
			
		    int overlay;
		    if (blockEntity.isPrimed() && blockEntity.getExplosionTimer() / 5 % 2 == 0) 
		    {
		    	overlay = OverlayTexture.pack(OverlayTexture.u(1.0F), OverlayTexture.WHITE_OVERLAY_V);
		    } 
		    else 
		    {
		    	overlay = OverlayTexture.NO_OVERLAY;
		    }
		    
		    RANDOM_SOURCE.setSeed(432l);
		    
		    int boomTime = NBETerramorfer.MAX_EXPLOSION_TIMER - blockEntity.getExplosionTimer();
		    if ((float)boomTime - partialTicks < 10.0F) 
		    {
		    	float f = 1.0F - ((float)boomTime - partialTicks) / 10.0F;
		        f = Mth.clamp(f, 0.0F, 1.0F);
		        f *= f;
		        f *= f;
		        float f1 = 1.0F + f * 0.3F;
		        mStack.scale(f1, f1, f1);
		    }
			VertexConsumer builder = buffer.getBuffer(NRenderTypes.coreRenderType(TEXTURE));
			renderCrystal(builder, mStack, overlay, combinedLight);
			mStack.popPose();
	
			float time = (blockEntity.getLevel().getGameTime() + partialTicks) % 200 / 200.0F;
		    float scale = Math.min(time > 0.8F ? (time - 0.8F) / 0.2F : 0.0F, 1.0F);
		    
		    VertexConsumer vertexconsumer2 = buffer.getBuffer(RenderType.lightning());
		    mStack.pushPose();
		    
		    mStack.translate(0.5f, 0.5f, 0.5f);

		    for(int i = 0; (float)i < (time + time * time) / 2.0F * 60.0F; ++i) 
		    {
		    	mStack.mulPose(Axis.XP.rotationDegrees(RANDOM_SOURCE.nextFloat() * 360.0F));
		        mStack.mulPose(Axis.YP.rotationDegrees(RANDOM_SOURCE.nextFloat() * 360.0F));
		        mStack.mulPose(Axis.ZP.rotationDegrees(RANDOM_SOURCE.nextFloat() * 360.0F));
		        mStack.mulPose(Axis.XP.rotationDegrees(RANDOM_SOURCE.nextFloat() * 360.0F));
		        mStack.mulPose(Axis.YP.rotationDegrees(RANDOM_SOURCE.nextFloat() * 360.0F));
		        mStack.mulPose(Axis.ZP.rotationDegrees(RANDOM_SOURCE.nextFloat() * 360.0F + time * 90.0F));
		        float f3 = RANDOM_SOURCE.nextFloat() * 1f + 0.05F + scale * 0.5F;
		        float f4 = RANDOM_SOURCE.nextFloat() * 0.02f + 0.01f + scale * 0.02f;
		        Matrix4f matrix4f = mStack.last().pose();
		        int j = (int)(255.0F * (1.0F - scale));
		        vertex01(vertexconsumer2, matrix4f, j);
		        vertex2(vertexconsumer2, matrix4f, f3, f4);
		        vertex3(vertexconsumer2, matrix4f, f3, f4);
		        vertex01(vertexconsumer2, matrix4f, j);
		        vertex3(vertexconsumer2, matrix4f, f3, f4);
		        vertex4(vertexconsumer2, matrix4f, f3, f4);
		        vertex01(vertexconsumer2, matrix4f, j);
		        vertex4(vertexconsumer2, matrix4f, f3, f4);
		        vertex2(vertexconsumer2, matrix4f, f3, f4);
		    }
			mStack.popPose();
		}
	}

	private static void vertex01(VertexConsumer builder, Matrix4f matrix, int alpha) 
	{
		builder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
	}

	 private static void vertex2(VertexConsumer builder, Matrix4f matrix, float offsetVertical, float offsetHorizontal) 
	 {
		 builder.vertex(matrix, -HALF_SQRT_3 * offsetHorizontal, offsetVertical, -0.5F * offsetHorizontal).color(255, 0, 255, 0).endVertex();
	 }

	 private static void vertex3(VertexConsumer builder, Matrix4f matrix, float offsetVertical, float offsetHorizontal) 
	 {
		 builder.vertex(matrix, HALF_SQRT_3 * offsetHorizontal, offsetVertical, -0.5F * offsetHorizontal).color(255, 0, 255, 0).endVertex();
	 }

	 private static void vertex4(VertexConsumer builder, Matrix4f matrix, float offsetVertical, float offsetHorizontal) 
	 {
		 builder.vertex(matrix, 0.0F, offsetVertical, 1.0F * offsetHorizontal).color(255, 0, 255, 0).endVertex();
	 }
		
	private void renderCrystal(VertexConsumer builder, PoseStack mStack, int overlay, int light)
	{
		Matrix4f matrix = mStack.last().pose();
		Matrix3f normal = mStack.last().normal();
		
		for (Face face : MESH)
		{
			builder.vertex(matrix, 
						VERTEX[face.v00()].x(), 
						VERTEX[face.v00()].y(), 
						VERTEX[face.v00()].z()).
					color(255, 255, 255, 200).
					uv(UV[face.v01()].x, 
					   UV[face.v01()].y).
					overlayCoords(overlay).
					uv2(light).
					normal(normal, 
						NORMAL[face.v02()].x(), 
						NORMAL[face.v02()].y(), 
						NORMAL[face.v02()].z()).
					endVertex();

			builder.vertex(matrix, 
						VERTEX[face.v10()].x(), 
						VERTEX[face.v10()].y(), 
						VERTEX[face.v10()].z()).
					color(255, 255, 255, 200).
					uv(UV[face.v11()].x, 
					   UV[face.v11()].y).
					overlayCoords(overlay).
					uv2(light).
					normal(normal, 
						NORMAL[face.v12()].x(), 
						NORMAL[face.v12()].y(), 
						NORMAL[face.v12()].z()).
					endVertex();

			builder.vertex(matrix, 
						VERTEX[face.v20()].x(), 
						VERTEX[face.v20()].y(), 
						VERTEX[face.v20()].z()).
					color(255, 255, 255, 200).
					uv(UV[face.v21()].x, 
					   UV[face.v21()].y).
					overlayCoords(overlay).
					uv2(light).
					normal(normal, 
						NORMAL[face.v22()].x(), 
						NORMAL[face.v22()].y(), 
						NORMAL[face.v22()].z()).
					endVertex();

		}
	}
	
	private static record Face (int v00, int v01, int v02,
								int v10, int v11, int v12,
								int v20, int v21, int v22)
	{
		public Face(int v00, int v01, int v02, int v10, int v11, int v12, int v20, int v21, int v22) 
		{
			this.v00 = v00 - 1;
			this.v01 = v01 - 1;
			this.v02 = v02 - 1;
			this.v10 = v10 - 1;
			this.v11 = v11 - 1;
			this.v12 = v12 - 1;
			this.v20 = v20 - 1;
			this.v21 = v21 - 1;
			this.v22 = v22 - 1;
		}
	}
}

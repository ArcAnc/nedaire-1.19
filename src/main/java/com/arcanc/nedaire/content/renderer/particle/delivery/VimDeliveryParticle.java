/**
 * @author ArcAnc
 * Created at: 2023-02-20
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.particle.delivery;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class VimDeliveryParticle extends DeliveryParticle<Integer> 
{
	protected static final ResourceLocation TEXTURE_LOC = StringHelper.getLocFStr("misc/lightning");
	protected final TextureAtlasSprite TEXTURE;

	protected float quadSize = 0.2f;
	
	private Integer stack;
	
	public VimDeliveryParticle(ClientLevel level,Vec3 startPos, Vec3 finishPos, Vec3 station, boolean toStation, int stack) 
	{
		super(level, startPos, finishPos, station, toStation);
		
		this.stack = stack;

		Minecraft mc = RenderHelper.mc();
		TEXTURE = mc.particleEngine.textureAtlas.getSprite(TEXTURE_LOC);
		
		this.alpha = 255 / 255.0F;
	    this.rCol = 240 / 255.0F;
	    this.gCol = 135 / 255.0F;
	    this.bCol = 204 / 255.0F;

	}

	@Override
	protected Integer getContent() 
	{
		return stack;
	}

	@Override
	public void render(VertexConsumer vertex, Camera camera, float partialTicks) 
	{
	      Vec3 vec3 = camera.getPosition();
	      float f = (float)(Mth.lerp((double)partialTicks, this.xo, this.x) - vec3.x());
	      float f1 = (float)(Mth.lerp((double)partialTicks, this.yo, this.y) - vec3.y());
	      float f2 = (float)(Mth.lerp((double)partialTicks, this.zo, this.z) - vec3.z());
	      Quaternionf quaternionf;
	      if (this.roll == 0.0F) 
	      {
	         quaternionf = camera.rotation();
	      } 
	      else 
	      {
	         quaternionf = new Quaternionf(camera.rotation());
	         quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
	      }

	      Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
	      float f3 = this.getQuadSize(partialTicks);

	      for(int i = 0; i < 4; ++i) 
	      {
	         Vector3f vector3f = avector3f[i];
	         vector3f.rotate(quaternionf);
	         vector3f.mul(f3);
	         vector3f.add(f, f1, f2);
	      }

	      float f6 = this.getU0();
	      float f7 = this.getU1();
	      float f4 = this.getV0();
	      float f5 = this.getV1();
	      int j = this.getLightColor(partialTicks);
	      vertex.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
	      vertex.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
	      vertex.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
	      vertex.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
		
	}
	
	public float getQuadSize(float p_107681_) 
	{
		return this.quadSize;
	}
	
	protected float getU0()
	{
		return TEXTURE.getU0();
	}

	protected float getU1()
	{
		return TEXTURE.getU1();
	}

	protected float getV0()
	{
		return TEXTURE.getV0();
	}

	protected float getV1()
	{
		return TEXTURE.getV1();
	}
	
	@Override
	public ParticleRenderType getRenderType() 
	{
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

}

/**
 * @author ArcAnc
 * Created at: 2023-02-14
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class EssenceParticle extends TextureSheetParticle
{

	protected EssenceParticle(ClientLevel level, double x, double y, double z, double red, double green, double blue, SpriteSet sprites) 
	{
		super(level, x, y, z);
		
		setColor((float)red, (float)green, (float)blue);
		setParticleSpeed(0, 0, 0);
		setPos(x, y, z);
		setSize(alpha, age);
		this.lifetime = 16;
		this.friction = 0.86f;
		this.quadSize = 0.2f;
		this.hasPhysics = false;
		this.pickSprite(sprites);
	}

	@Override
	public void tick() 
	{
		this.xo = this.x;
	    this.yo = this.y;
	    this.zo = this.z;
	    if (this.age++ >= this.lifetime) 
	    {
	       this.remove();
	    }
	    else
	    {
	    	scale(this.friction);
	    }
	 }
	
	@Override
	protected int getLightColor(float partialTicks) 
	{
	      float f = ((float)this.age + partialTicks) / (float)this.lifetime;
	      f = Mth.clamp(f, 0.0F, 1.0F);
	      int i = super.getLightColor(partialTicks);
	      int j = i & 255;
	      int k = i >> 16 & 255;
	      j += (int)(f * 15.0F * 16.0F);
	      if (j > 240) 
	      {
	         j = 240;
	      }

	      return j | k << 16;
	}
	@Override
	public @NotNull ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public static class Provider implements ParticleProvider<SimpleParticleType> 
	{
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) 
	    {
			this.sprites = sprites;
		}
		
	    @Override
		public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double red, double green, double blue)
		{
			return new EssenceParticle(level, x, y, z, red, green, blue, sprites);
		}
		
	}

}

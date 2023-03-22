/**
 * @author ArcAnc
 * Created at: 2023-03-21
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.fluid;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

public class NFluidType extends FluidType 
{
	private final ResourceLocation stillTexture;
	private final ResourceLocation flowingTexture;
	private final ResourceLocation overlayTexture;
	private final Supplier<Integer> tintColor;
	private final NFluidType.FogGetter fogColor;

	public NFluidType(final ResourceLocation stillTexture, final ResourceLocation flowingTexture, final ResourceLocation overlayTexture, final Supplier<Integer> tintColor, final NFluidType.FogGetter fogColor, final Properties properties) 
	{
		super(properties);
		
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.overlayTexture = overlayTexture;
		this.tintColor = tintColor;
		this.fogColor = fogColor;
	}

	@Override
	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) 
	{
		consumer.accept(new IClientFluidTypeExtensions()  
		{
			@Override
			public ResourceLocation getStillTexture() 
			{
				return stillTexture;
			}
			
			@Override
			public ResourceLocation getFlowingTexture() 
			{
				return flowingTexture;
			}

			@Override
			public @Nullable ResourceLocation getOverlayTexture() 
			{
				return overlayTexture;
			}
			
			@Override
			public int getTintColor() 
			{
				return tintColor.get();
			}
			
			@Override
			public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) 
			{
				return fogColor.getFog(camera, partialTick, level, renderDistance, darkenWorldAmount, fluidFogColor);
			}
			
			@Override
			public void modifyFogRender(Camera camera, FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) 
			{
				RenderSystem.setShaderFogStart(1f);
				RenderSystem.setShaderFogEnd(6f); // distance when the fog starts
			}
		});
	}
	
	public ResourceLocation getStillTexture() 
	{
		return stillTexture;
	}
	
	public ResourceLocation getFlowingTexture() 
	{
		return flowingTexture;
	}
	
	public Supplier<Integer> getTintColor() 
	{
		return tintColor;
	}
	
	public ResourceLocation getOverlayTexture() 
	{
		return overlayTexture;
	}
	
	public NFluidType.FogGetter getFogColor() 
	{
		return fogColor;
	}
	
	public interface FogGetter
	{
		Vector3f getFog(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor);
	
		static Vector3f interpColor(Vector3f curColor, Vector3f targetColor, float delta)
		{
			Vector3f diff = targetColor.sub(curColor, new Vector3f());
			Vector3f move = new Vector3f();
			Vector3f ret = new Vector3f();
			
			if (diff.equals(0,0,0))
				return targetColor;
			
			diff.mul(delta, move);
			return curColor.add(move, ret);
		}
		
		static int getIntFromColor(Vector3f color)
		{
		    int R = Math.round(255 * color.x());
		    int G = Math.round(255 * color.y());
		    int B = Math.round(255 * color.z());

		    R = (R << 16) & 0x00FF0000;
		    G = (G << 8) & 0x0000FF00;
		    B = B & 0x000000FF;

		    return 0xFF000000 | R | G | B;
		}
	}
}

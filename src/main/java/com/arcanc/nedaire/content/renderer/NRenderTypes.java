/**
 * @author ArcAnc
 * Created at: 2023-03-26
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer;

import java.util.function.Function;

import com.arcanc.nedaire.util.database.NDatabase;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class NRenderTypes 
{
	public static RenderType coreRenderType (ResourceLocation texture)
	{
		return NRenderTypeProviders.CORE.apply(texture);
	}
	
	public static RenderType translucentEntity(ResourceLocation texture)
	{
		return NRenderTypeProviders.TRANSLUCENT_ENTITY.apply(texture);
	}
	
	public static RenderType translucentTriangleFan(ResourceLocation texture)
	{
		return NRenderTypeProviders.POS_TEX_TRANSLUCENT_UNCULLED_TRIANGLE.apply(texture);
	}
	
    protected static final RenderStateShard.OutputStateShard TRANSLUCENT_TARGET_NO_DEPTH_MASK = new RenderStateShard.OutputStateShard("translucent_target_no_depth_mask", () -> 
    {
        if (Minecraft.useShaderTransparency()) 
        {
            Minecraft mc = Minecraft.getInstance();
			mc.levelRenderer.getTranslucentTarget().bindWrite(false);
        }
        RenderSystem.depthMask(false);
    }, () -> 
    {
        if (Minecraft.useShaderTransparency()) 
        {
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        }
        RenderSystem.depthMask(true);
    });
	
	private static class NRenderTypeProviders extends RenderType
	{
		private NRenderTypeProviders(String s, VertexFormat v, VertexFormat.Mode m, int i, boolean b, boolean b2, Runnable r, Runnable r2)
        {
            super(s, v, m, i, b, b2, r, r2);
            throw new IllegalStateException("This class is not meant to be constructed!");
        }

		public static Function<ResourceLocation, RenderType> CORE = Util.memoize(NRenderTypeProviders :: core);

		public static Function<ResourceLocation, RenderType> TRANSLUCENT_ENTITY = Util.memoize(NRenderTypeProviders :: translucentEntity);

	    public static Function<ResourceLocation, RenderType> POS_TEX_TRANSLUCENT_UNCULLED_TRIANGLE = Util.memoize(NRenderTypeProviders :: translucentTriangleFan);
		
		private static RenderType core(ResourceLocation loc)
		{
			RenderType.CompositeState state =  RenderType.CompositeState.builder().
					setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER).
					setTextureState(new RenderStateShard.TextureStateShard(loc, false, false)).
					setTransparencyState(TRANSLUCENT_TRANSPARENCY).
					setCullState(NO_CULL).
					setLightmapState(LIGHTMAP).
					setOverlayState(OVERLAY).
					setWriteMaskState(COLOR_WRITE).
					createCompositeState(true);
			return create(NDatabase.Render.RenderTypes.CORE, DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 256, true, true, state);
		}
		
		private static RenderType translucentEntity (ResourceLocation loc)
		{
			RenderType.CompositeState state = RenderType.CompositeState.builder().
	            	setShaderState(NEW_ENTITY_SHADER).
	            	setTextureState(new RenderStateShard.TextureStateShard(loc, false, false)).
	            	setTransparencyState(TRANSLUCENT_TRANSPARENCY).
	            	setOutputState(RenderPostProcessor.GLITCH_TARGET).
	            	createCompositeState(true);
			
			return create(NDatabase.Render.RenderTypes.TRANSLUCENT_ENTITY, DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
		}
		
		private static RenderType translucentTriangleFan (ResourceLocation loc)
		{
			RenderType.CompositeState state = RenderType.CompositeState.builder().
					setShaderState(POSITION_TEX_SHADER).
					setTextureState(new RenderStateShard.TextureStateShard(loc, false, false)).
					setTransparencyState(TRANSLUCENT_TRANSPARENCY).
					setOutputState(RenderPostProcessor.GLITCH_TARGET).
					setCullState(NO_CULL).
					createCompositeState(true);
			return create(NDatabase.Render.RenderTypes.TRANSLUCENT_TRIANGLE_FAN, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.TRIANGLES, 256, true, true, state);
		}
		
	}
}

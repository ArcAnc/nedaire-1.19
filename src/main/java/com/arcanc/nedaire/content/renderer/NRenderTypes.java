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
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class NRenderTypes 
{
	public static RenderType coreRenderType (ResourceLocation texture)
	{
		return NRenderTypeProviders.CORE.apply(texture);
	}
	
/*	public static RenderType glitchRenderType(ResourceLocation texture)
	{
		return NRenderTypeProviders.GLITCH.apply(texture);
	}
	
	public static void registerShaders (final RegisterShadersEvent event)
	{
		try {
			event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(NDatabase.MOD_ID + ":glitch"), DefaultVertexFormat.BLIT_SCREEN), shaderInstance -> 
			{
				NRenderTypeProviders.glitchRender = shaderInstance;
			});
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
*/	
	private static class NRenderTypeProviders extends RenderType
	{
/*		private static ShaderInstance glitchRender;
		private static final ShaderStateShard GLITCH_RENDER = new ShaderStateShard( () -> glitchRender);

		public static Function<ResourceLocation, RenderType> GLITCH = Util.memoize(NRenderTypeProviders :: glitch);
*/		public static Function<ResourceLocation, RenderType> CORE = Util.memoize(NRenderTypeProviders :: core);

		
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
		
/*		private static RenderType glitch(ResourceLocation loc)
		{
			RenderType.CompositeState state = RenderType.CompositeState.builder().
					setShaderState(GLITCH_RENDER).
					setTextureState(new RenderStateShard.TextureStateShard(loc, false, false)).
					setTransparencyState(TRANSLUCENT_TRANSPARENCY).
					setOutputState(TRANSLUCENT_TARGET).
					createCompositeState(true);
			return create(NDatabase.Render.RenderTypes.TRANSLUCENT_ENTITY, DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
		}
*/	
	
		private NRenderTypeProviders(String s, VertexFormat v, VertexFormat.Mode m, int i, boolean b, boolean b2, Runnable r, Runnable r2)
        {
            super(s, v, m, i, b, b2, r, r2);
            throw new IllegalStateException("This class is not meant to be constructed!");
        }

	}
}

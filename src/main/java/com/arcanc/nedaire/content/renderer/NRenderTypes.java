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
		return NRenderTypesProviders.CORE.apply(texture);
	}
	
	private static class NRenderTypesProviders extends RenderType
	{
		private NRenderTypesProviders(String s, VertexFormat v, VertexFormat.Mode m, int i, boolean b, boolean b2, Runnable r, Runnable r2)
        {
            super(s, v, m, i, b, b2, r, r2);
            throw new IllegalStateException("This class is not meant to be constructed!");
        }

		public static Function<ResourceLocation, RenderType> CORE = Util.memoize(NRenderTypesProviders :: core);
		
		private static RenderType core(ResourceLocation loc)
		{
			RenderType.CompositeState state =  RenderType.CompositeState.builder().
					setShaderState(RENDERTYPE_TRANSLUCENT_SHADER).
					setTextureState(new RenderStateShard.TextureStateShard(loc, false, false)).
					setTransparencyState(TRANSLUCENT_TRANSPARENCY).
					setLightmapState(LIGHTMAP).
					setOverlayState(OVERLAY).
					createCompositeState(true);
			return create(NDatabase.Render.RenderTypes.CORE, DefaultVertexFormat.BLOCK, VertexFormat.Mode.TRIANGLES, 256, true, true, state);
		}
		
	}
}

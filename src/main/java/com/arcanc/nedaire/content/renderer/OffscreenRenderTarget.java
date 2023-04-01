/**
 * @author ArcAnc
 * Created at: 2023-04-01
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;


public class OffscreenRenderTarget extends RenderTarget 
{
   public OffscreenRenderTarget(int width, int height) 
   {
      super(true);
      RenderSystem.assertOnRenderThreadOrInit();
      resize(width, height, Minecraft.ON_OSX);
   }
}
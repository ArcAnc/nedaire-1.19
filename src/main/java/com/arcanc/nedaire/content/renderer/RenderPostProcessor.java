/**
 * @author ArcAnc
 * Created at: 2023-04-01
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer;

import org.joml.Matrix4f;

import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;

public class RenderPostProcessor 
{
    public static final RenderStateShard.OutputStateShard GLITCH_TARGET = new RenderStateShard.OutputStateShard(NDatabase.MOD_ID + ":glitch_target", () -> 
    {
        if (RenderPostProcessor.enableGlitchEffect) 
        {
            RenderPostProcessor.glitchRenderTarget.bindWrite(false);
            RenderSystem.depthMask(true);
        } 
        else 
        {
            NRenderTypes.TRANSLUCENT_TARGET_NO_DEPTH_MASK.setupRenderState();
        }
    }, () -> 
    {
        if (RenderPostProcessor.enableGlitchEffect) 
        {
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        } 
        else 
        {
            NRenderTypes.TRANSLUCENT_TARGET_NO_DEPTH_MASK.clearRenderState();
        }
    });
    
    private static final ResourceLocation NOISE_SEED = StringHelper.getLocFStr("textures/misc/noise.png");

    static RenderTarget glitchRenderTarget;
    private static Matrix4f shaderOrthoMatrix;
    private static PostPass postProcessPass;
    private static boolean enableGlitchEffect;

    public static void initRenderTarget() 
    {
        enableGlitchEffect = true;//MFFSConfig.CLIENT.enableProjectorModeGlitch.get();
        if (enableGlitchEffect) {
            Minecraft minecraft = RenderHelper.mc();
            RenderTarget mainRenderTarget = minecraft.getMainRenderTarget();
            Window window = minecraft.getWindow();
            glitchRenderTarget = new OffscreenRenderTarget(window.getWidth(), window.getHeight());
            glitchRenderTarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            shaderOrthoMatrix = new Matrix4f().setOrtho(0.0F, mainRenderTarget.width, mainRenderTarget.height, 0.0F, 0.1F, 1000.0F);
        }
    }

    public static void reloadPostProcessPass() 
    {
        if (enableGlitchEffect) 
        {
            Minecraft minecraft = RenderHelper.mc();
            RenderTarget mainRenderTarget = minecraft.getMainRenderTarget();
            if (postProcessPass != null) 
            {
                postProcessPass.close();
            }
            try 
            {
                postProcessPass = new PostPass(minecraft.getResourceManager(), NDatabase.MOD_ID + ":glitch", glitchRenderTarget, mainRenderTarget);
                postProcessPass.setOrthoMatrix(shaderOrthoMatrix);
            } 
            catch (Exception e) 
            {
                throw new RuntimeException(e);
            }
        }
    }
	
    /**
	 * FIXME: add injection for {@link Minecraft#resizeDisplay()}	
	 */
    public static void resizeDisplay() 
    {
        if (enableGlitchEffect) 
        {
            Minecraft minecraft = RenderHelper.mc();
            RenderTarget mainRenderTarget = minecraft.getMainRenderTarget();
            Window window = minecraft.getWindow();
            if (glitchRenderTarget != null) 
            {
                glitchRenderTarget.resize(window.getWidth(), window.getHeight(), Minecraft.ON_OSX);
            }
            shaderOrthoMatrix = new Matrix4f().setOrtho(0.0F, mainRenderTarget.width, mainRenderTarget.height, 0.0F, 0.1F, 1000.0F);
            if (postProcessPass != null) 
            {
                postProcessPass.setOrthoMatrix(shaderOrthoMatrix);
            }
        }
    }

    public static void prepareRender() 
    {
        if (enableGlitchEffect) 
        {
            // Clear glitch target
            glitchRenderTarget.clear(Minecraft.ON_OSX);
            RenderHelper.mc().getMainRenderTarget().bindWrite(false);
        }
    }

    public static void process(int ticks) 
    {
    	if (enableGlitchEffect) 
        {
            // Process glitch target
            process(postProcessPass, ticks);
            RenderHelper.mc().getMainRenderTarget().bindWrite(false);
        }
    }

    private static void process(PostPass pass, float time) 
    {
        EffectInstance effect = pass.getEffect();
        Minecraft minecraft = RenderHelper.mc();
        Window window = minecraft.getWindow();
        float width = (float) pass.outTarget.width;
        float height = (float) pass.outTarget.height;
        float farPlane = 500.0F;

        pass.inTarget.unbindWrite();
        RenderSystem.viewport(0, 0, (int) width, (int) height);
        effect.setSampler("DiffuseSampler", pass.inTarget::getColorTextureId);
        effect.setSampler("NoiseSampler", () -> minecraft.textureManager.getTexture(NOISE_SEED).getId());
        effect.setSampler("DepthSampler", pass.inTarget::getDepthTextureId);
        effect.safeGetUniform("ProjMat").set(shaderOrthoMatrix);
        effect.safeGetUniform("InSize").set((float) pass.inTarget.width, (float) pass.inTarget.height);
        effect.safeGetUniform("OutSize").set(width, height);
        effect.safeGetUniform("Time").set(time);
        effect.safeGetUniform("ScreenSize").set((float) window.getWidth(), (float) window.getHeight());
        effect.apply();
        pass.outTarget.bindWrite(false);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        bufferbuilder.vertex(0.0D, 0.0D, farPlane).endVertex();
        bufferbuilder.vertex(width, 0.0D, farPlane).endVertex();
        bufferbuilder.vertex(width, height, farPlane).endVertex();
        bufferbuilder.vertex(0.0D, height, farPlane).endVertex();
        BufferUploader.draw(bufferbuilder.end());

        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();
        effect.clear();
        pass.outTarget.unbindWrite();
        pass.inTarget.unbindRead();
    }

    private RenderPostProcessor() {}
}

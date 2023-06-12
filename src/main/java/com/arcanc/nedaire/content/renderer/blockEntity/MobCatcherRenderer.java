/**
 * @author ArcAnc
 * Created at: 2023-02-27
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import com.arcanc.nedaire.content.block.entities.NBEMobCatcher;
import com.arcanc.nedaire.content.renderer.blockEntity.model.CageModel;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import org.jetbrains.annotations.NotNull;

public class MobCatcherRenderer implements BlockEntityRenderer<NBEMobCatcher>  
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/block/iron_bars.png"); 
//	private static final Material MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("block/iron_bars")); 
	
	public static final ModelLayerLocation CAGE = new ModelLayerLocation(StringHelper.getLocFStr("mob_catcher/cage"), /*ModelLayers.DEFAULT_LAYER*/"main");
	private final CageModel cageModel;
	
	public MobCatcherRenderer(BlockEntityRendererProvider.Context ctx) 
	{
		this.cageModel = new CageModel(ctx.bakeLayer(CAGE));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void render(@NotNull NBEMobCatcher tile, float partialTicks, @NotNull PoseStack mStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		if (tile.getUsedEnergy() > 0)
		{
			mStack.pushPose();
			mStack.translate(0.5, 2.5, 0.5);
			mStack.scale(1, -1, 1);

			this.cageModel.setupAnim(tile, tile.age, 1f);
			VertexConsumer vertex = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE, false));

			this.cageModel.renderToBuffer(mStack, vertex, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);

			mStack.popPose();

			mStack.pushPose();

			mStack.translate(0.5f, 1f, 0.5f);

			if (tile.getEnt() != null)
			{
				EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
				RenderSystem.runAsFancy(() ->
						entityRenderDispatcher.render(tile.getEnt(), 0, 0, 0, 0, 0.5f, mStack, buffer, 15728880));
			}

			mStack.popPose();
		}
	}
	
	public static void registerModelLocation(final RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(CAGE, CageModel::createLayer);
	}
}

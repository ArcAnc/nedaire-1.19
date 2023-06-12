/**
 * @author ArcAnc
 * Created at: 2022-10-24
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import com.arcanc.nedaire.content.block.entities.NBEManualCrusher;
import com.arcanc.nedaire.content.renderer.blockEntity.model.CrankModel;
import com.arcanc.nedaire.content.renderer.blockEntity.model.PressModel;
import com.arcanc.nedaire.data.crafting.recipe.NCrusherRecipe;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class ManualCrusherRenderer implements BlockEntityRenderer<NBEManualCrusher> 
{
	public static final Material TEXT_LOCATION = new Material(InventoryMenu.BLOCK_ATLAS, StringHelper.getLocFStr("block/manual_crusher/manual_crusher"));
	public static final ModelLayerLocation CRANK = new ModelLayerLocation(StringHelper.getLocFStr("manual_crusher/crank"), /*ModelLayers.DEFAULT_LAYER*/"main");
	private final CrankModel crankModel;  
	public static final ModelLayerLocation PRESS = new ModelLayerLocation(StringHelper.getLocFStr("manual_crusher/press"), /*ModelLayers.DEFAULT_LAYER*/"main");
	private final PressModel pressModel;  
	
	public ManualCrusherRenderer(BlockEntityRendererProvider.Context ctx) 
	{
		crankModel = new CrankModel(ctx.bakeLayer(CRANK));
		pressModel = new PressModel(ctx.bakeLayer(PRESS));
	}
	
	@Override
	public void render(@NotNull NBEManualCrusher blockEntity, float partialTicks, @NotNull PoseStack mStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		mStack.pushPose();
		mStack.translate(0.5d, 0, 0.5d);
		mStack.mulPose(new Quaternionf().fromAxisAngleDeg(0, 1, 0, blockEntity.currentAngle));

		VertexConsumer vertexconsumer = TEXT_LOCATION.buffer(buffer, RenderType::entitySolid);
		this.crankModel.renderToBuffer(mStack, vertexconsumer, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

		mStack.popPose();

		mStack.pushPose();
		mStack.translate(0.5d, 0d, 0.5d);

		ItemHelper.getItemHandler(blockEntity).ifPresent(handler ->
		{
			if (!handler.getStackInSlot(0).isEmpty())
			{
				NCrusherRecipe.findRecipe(blockEntity.getLevel(), handler.getStackInSlot(0)).ifPresentOrElse(rec ->
						mStack.translate(0d, (1 - ((float)blockEntity.usedEnergy / rec.getTotalProcessEnergy())) / 2d, 0d),
				() ->
						mStack.translate(0d, 0.5d, 0d));
			}
			else
				mStack.translate(0d, 0.5d, 0d);
		});

		this.pressModel.renderToBuffer(mStack, vertexconsumer, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

		mStack.popPose();
	}
	
	public static void registerModelLocation(final RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(CRANK, CrankModel::createLayer);
		event.registerLayerDefinition(PRESS, PressModel::createLayer);
	}

}

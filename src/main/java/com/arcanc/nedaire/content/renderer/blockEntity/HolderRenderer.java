/**
 * @author ArcAnc
 * Created at: 2022-04-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity;

import com.arcanc.nedaire.content.block.entities.NBEHolder;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper.TintedVertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class HolderRenderer implements BlockEntityRenderer<NBEHolder>  
{

	public HolderRenderer(BlockEntityRendererProvider.Context ctx) 
	{
	}
	
	@Override
	public void render(@NotNull NBEHolder blockEntity, float partialTicks, @NotNull PoseStack mStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		if (!ItemHelper.isEmpty(blockEntity))
		{
			ItemHelper.getItemHandler(blockEntity).ifPresent(handler ->
			{
				Minecraft mc = RenderHelper.mc();
				Vec3 playerPos = mc.player.getPosition(partialTicks);
				BlockPos pos = blockEntity.getBlockPos();
				double xDifference = playerPos.x - (pos.getX() + 0.5D);
				double zDifference = playerPos.z - (pos.getZ() + 0.5D);
				float rotation = (float) Math.toDegrees(Math.atan2(zDifference, xDifference));

				ItemStack stack = handler.getStackInSlot(0);

				VertexConsumer builder = buffer.getBuffer(RenderType.lightning());
				mStack.pushPose();

				mStack.translate(0.5f, 0.85f, 0.5f);

				mStack.mulPose(Axis.YP.rotationDegrees(-rotation));

				Matrix4f matrix4f = mStack.last().pose();
				builder.vertex(matrix4f, 0.0f, 0.0f, 0.0f).color(255, 0, 255, 255).endVertex();
				builder.vertex(matrix4f, 0.0f, -0.6f, 0.6f).color(255, 0, 255, 0).endVertex();
				builder.vertex(matrix4f, 0.0f, -0.6f, -0.6f).color(255, 0, 255, 0).endVertex();
				builder.vertex(matrix4f, 0.0f, 0.0f, 0.0f).color(255, 0, 255, 255).endVertex();

				mStack.popPose();

				mStack.pushPose();
				mStack.translate(0.5f, 0.05f, 0.5f);
				mStack.translate(0, Math.sin((blockEntity.getLevel().getGameTime() + partialTicks) / 10.0F) * 0.1 + 0.1, 0); //Item bobbing
				mStack.scale(0.75F, 0.75F, 0.75F);
				mStack.mulPose(Axis.YP.rotationDegrees(-rotation));

	//FIXME: fix render type add change this to glitching effect
	//RenderType.entityTranslucentCull
	//NRenderTypes.translucentEntity
				BakedModel model = RenderHelper.renderItem().getModel(stack, blockEntity.getLevel(), null, 0);
				RenderHelper.renderItem().render(
						stack,
						ItemDisplayContext.GROUND,
						false,
						mStack,
						wrapBuffer(buffer, 1f, 0.5f, 1f, 175f / 255f, RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS)),
						combinedLight,
						combinedOverlay,
						model);
				mStack.popPose();
			});
		}
	}

    private static MultiBufferSource wrapBuffer(MultiBufferSource buffer, float red, float green, float blue, float alpha, RenderType type)
    {
        return renderType -> new TintedVertexConsumer(buffer.getBuffer(type), red, green, blue, alpha);
    }
}

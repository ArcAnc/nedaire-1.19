/**
 * @author ArcAnc
 * Created at: 25.04.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.renderer.blockEntity;

import com.arcanc.nedaire.content.block.entities.NBEBore;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.generators.ModelProvider;
import org.jetbrains.annotations.NotNull;

public class BoreRenderer implements BlockEntityRenderer<NBEBore>
{
    private static final StringBuilder path = new StringBuilder().append(ModelProvider.BLOCK_FOLDER).append("/").append(name(NRegistration.RegisterBlocks.BORE.get()));
    private static final ResourceLocation BASE_TEXTURE = StringHelper.getLocFStr("textures/block/bore/base.png");
    private static final ResourceLocation ROTOR_TEXTURE = StringHelper.getLocFStr("textures/block/bore/rotor.png");
    private final BakedModel base;
    private final BakedModel control;
    private final BakedModel rotor;

    private final RandomSource random = RandomSource.create();

    public BoreRenderer(BlockEntityRendererProvider.Context ctx)
    {
        Minecraft mc = RenderHelper.mc();
        ModelManager manager = mc.getModelManager();

        base = manager.getModel(StringHelper.getLocFStr(path + "/base"));
        control = manager.getModel(StringHelper.getLocFStr(path + "/control"));
        rotor = manager.getModel(StringHelper.getLocFStr(path + "/rotor"));
    }
    @Override
    public void render(NBEBore blockEntity, float partialTicks, PoseStack mStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay)
    {
        Direction attach = blockEntity.getBlockState().getValue(BlockHelper.BlockProperties.VERTICAL_ATTACHMENT);
        Vec3 rotVec = blockEntity.getCurrentRot();

        Minecraft mc = RenderHelper.mc();
        BlockRenderDispatcher renderer = mc.getBlockRenderer();

        mStack.pushPose();
        if (attach == Direction.UP)
        {
            mStack.translate(0f, 1f, 1f);
            mStack.mulPose(Axis.XP.rotationDegrees(180f));
            mStack.mulPose(Axis.YP.rotationDegrees((float)rotVec.y()));
            renderer.getModelRenderer().tesselateWithoutAO(
                    blockEntity.getLevel(),
                    base,
                    blockEntity.getBlockState(),
                    blockEntity.getBlockPos(),
                    mStack,
                    buffer.getBuffer(RenderType.cutout()),
                    false,
                    random,
                    444l,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    null);



            renderer.getModelRenderer().tesselateWithoutAO(
                    blockEntity.getLevel(),
                    control,
                    blockEntity.getBlockState(),
                    blockEntity.getBlockPos(),
                    mStack,
                    buffer.getBuffer(RenderType.cutout()),
                    false,
                    random,
                    444l,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    null);

            renderer.getModelRenderer().tesselateWithoutAO(
                    blockEntity.getLevel(),
                    rotor,
                    blockEntity.getBlockState(),
                    blockEntity.getBlockPos(),
                    mStack,
                    buffer.getBuffer(RenderType.cutout()),
                    false,
                    random,
                    444l,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    null);
        }
        else
        {
            mStack.translate(0.5f, 0f, 0.5f);

            float rotY = 90 - (float)Math.toDegrees(Math.atan2(rotVec.z(), rotVec.x()));

            mStack.mulPose(Axis.YP.rotationDegrees(rotY));

            renderer.getModelRenderer().tesselateWithoutAO(
                    blockEntity.getLevel(),
                    base,
                    blockEntity.getBlockState(),
                    blockEntity.getBlockPos(),
                    mStack,
                    buffer.getBuffer(RenderType.cutout()),
                    false,
                    random,
                    444l,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    null);

            float rotX = (float)Math.abs(Math.toDegrees(Math.atan2(rotVec.z(), rotVec.y())));

            mStack.translate(0, 4/16f, 0);
            //mStack.mulPose(Axis.XP.rotationDegrees(-90));
            mStack.mulPose(Axis.XP.rotationDegrees(rotX));

            renderer.getModelRenderer().tesselateWithoutAO(
                    blockEntity.getLevel(),
                    control,
                    blockEntity.getBlockState(),
                    blockEntity.getBlockPos(),
                    mStack,
                    buffer.getBuffer(RenderType.cutout()),
                    false,
                    random,
                    444l,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    null);

            if (blockEntity.isWorking())
            {
                float time = blockEntity.getLevel().getGameTime() + partialTicks;
                float angle = time % 360;
                mStack.mulPose(Axis.YP.rotationDegrees(angle));
            }

            renderer.getModelRenderer().tesselateWithoutAO(
                    blockEntity.getLevel(),
                    rotor,
                    blockEntity.getBlockState(),
                    blockEntity.getBlockPos(),
                    mStack,
                    buffer.getBuffer(RenderType.cutout()),
                    false,
                    random,
                    444l,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    null);
        }


        mStack.popPose();
    }

    public static void registerModelLocation(final ModelEvent.RegisterAdditional event)
    {
        event.register(StringHelper.getLocFStr(path + "/base"));
        event.register(StringHelper.getLocFStr(path + "/control"));
        event.register(StringHelper.getLocFStr(path + "/rotor"));
    }

/*    public static void loadBakedModel(final ModelEvent.BakingCompleted event)
    {
        base =
        control = event.getModels().get(path + "/control");
        rotor = event.getModels().get(path + "/rotor");
    }
*/
    private static String name(Block block)
    {
        return BlockHelper.getRegistryName(block).getPath();
    }
}

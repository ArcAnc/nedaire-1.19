/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.item.shieldRenderer;

import java.util.List;

import com.arcanc.nedaire.content.item.weapon.NShieldBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.NotNull;

public class ShieldTileEntityRenderer extends BlockEntityWithoutLevelRenderer 
{
	private final EntityModelSet modelSet;
    private ShieldModel modelShield;
	
    public ShieldTileEntityRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) 
    {
        super(dispatcher, modelSet);
    	this.modelSet = modelSet;
    }
    
    @Override
    public void onResourceManagerReload(@NotNull ResourceManager manager)
    {
        this.modelShield = new ShieldModel(this.modelSet.bakeLayer(ModelLayers.SHIELD));
    }
	
	@Override
	public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext type, PoseStack matrixStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		matrixStack.pushPose();
		matrixStack.scale(1, -1, -1);
		boolean flag = stack.getTagElement("BlockEntityTag") != null;
		Material renderMaterial = flag ? ModelBakery.SHIELD_BASE
				: ModelBakery.NO_PATTERN_SHIELD;

		Item shield = stack.getItem();

		if (shield instanceof NShieldBase nShield)
		{

			renderMaterial = flag ? nShield.getMaterial().getShieldBase() : nShield.getMaterial().getShieldNoPattern();
		}
		
		VertexConsumer ivertexbuilder = renderMaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(
				buffer, modelShield.renderType(renderMaterial.atlasLocation()), true, stack.hasFoil()));
		modelShield.handle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F,
				1.0F, 1.0F, 1.0F);
		if (flag) 
		{
			List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(stack),
					BannerBlockEntity.getItemPatterns(stack));
			BannerRenderer.renderPatterns(matrixStack, buffer, combinedLight, combinedOverlay,
					modelShield.plate(), renderMaterial, false, list, stack.hasFoil());
		} 
		else 
		{
			modelShield.plate().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F,	1.0F, 1.0F, 1.0F);
		}
		matrixStack.popPose();

	}
}

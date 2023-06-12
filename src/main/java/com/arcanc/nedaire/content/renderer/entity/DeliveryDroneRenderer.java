/**
 * @author ArcAnc
 * Created at: 2022-11-01
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.entity;

import com.arcanc.nedaire.content.entities.DeliveryDroneEntity;
import com.arcanc.nedaire.content.renderer.entity.model.DeliveryDroneModel;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import org.jetbrains.annotations.NotNull;

public class DeliveryDroneRenderer extends LivingEntityRenderer<DeliveryDroneEntity, DeliveryDroneModel<DeliveryDroneEntity>>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringHelper.getLocFStr(NDatabase.Entities.Names.DELIVERY_DRONE), "main");
	public static final ResourceLocation TEXTURE = StringHelper.getLocFStr(StringHelper.slashPlacer(NDatabase.Entities.Names.DELIVERY_DRONE + ".png", 
			NDatabase.Entities.DEFAULT_TEXTURE_PATH, NDatabase.Entities.Names.DELIVERY_DRONE));

	public DeliveryDroneRenderer(Context context) 
	{
		super(context, new DeliveryDroneModel<>(context.bakeLayer(LAYER_LOCATION)), 0.3125f);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull DeliveryDroneEntity entity)
	{
		return TEXTURE;
	}
	
	public static void registerModelLocation(final RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(LAYER_LOCATION, DeliveryDroneModel::createLayer);
	}
}

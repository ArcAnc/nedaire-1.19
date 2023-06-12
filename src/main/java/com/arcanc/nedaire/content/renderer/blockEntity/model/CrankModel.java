/**
 * @author ArcAnc
 * Created at: 2022-10-24
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CrankModel extends Model 
{
	private static final String BOT = "bot";
	private static final String MIDDLE = "middle";
	private static final String TOP = "top";
	
	private final ModelPart root;
	private final ModelPart bot;
	private final ModelPart middle;
	private final ModelPart top;
	
	public CrankModel(ModelPart root) 
	{
		super(RenderType :: entitySolid);
		this.root = root;
		this.bot = root.getChild(BOT);
		this.middle = root.getChild(MIDDLE);
		this.top = root.getChild(TOP);
	}
	
	public static LayerDefinition createLayer()
	{
		MeshDefinition meshDef = new MeshDefinition();
		PartDefinition partDef = meshDef.getRoot();
		partDef.addOrReplaceChild(BOT, CubeListBuilder.create().texOffs(60, 37).addBox(BOT, 7.5f, 16f, 7.5f, 1, 5, 1), PartPose.offset(-8, 0, -8));
		partDef.addOrReplaceChild(MIDDLE, CubeListBuilder.create().texOffs(50, 21).addBox(MIDDLE, 7.5f, 21f, 7.5f, 6, 1, 1), PartPose.offset(-8, 0, -8));
		partDef.addOrReplaceChild(TOP, CubeListBuilder.create().texOffs(60, 9).addBox(TOP, 12.5f, 22f, 7.5f, 1, 2, 1), PartPose.offset(-8, 0, -8));
		
		return LayerDefinition.create(meshDef, 64, 64);
	}
	
	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}

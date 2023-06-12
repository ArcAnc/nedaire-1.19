/**
 * @author ArcAnc
 * Created at: 2022-10-25
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
import org.jetbrains.annotations.NotNull;

public class PressModel extends Model 
{

	private static final String PRESS = "press";
	private static final String TOP = "top";
	
	private final ModelPart root;
	private final ModelPart press;
	private final ModelPart top;
	
	public PressModel(ModelPart root) 
	{
		super(RenderType :: entitySolid);
		
		this.root = root;
		this.press = root.getChild(PRESS);
		this.top = root.getChild(TOP);
	}

	public static LayerDefinition createLayer()
	{
		MeshDefinition meshDef = new MeshDefinition();
		PartDefinition partDef = meshDef.getRoot();
		partDef.addOrReplaceChild(PRESS, CubeListBuilder.create().texOffs(24, 53).addBox(PRESS, 3.006f, 6f, 3.006f, 9.988f, 1f, 9.988f), PartPose.offset(-8, 0, -8));
		partDef.addOrReplaceChild(TOP, CubeListBuilder.create().texOffs(60, 0).addBox(TOP, 7.5f, 7f, 7.5f, 1f, 7f, 1f), PartPose.offset(-8, 0, -8));
		
		return LayerDefinition.create(meshDef, 64, 64);
	}
	
	@Override
	public void renderToBuffer(@NotNull PoseStack p_103111_, @NotNull VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_)
	{
		this.root.render(p_103111_, p_103112_, p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_);
	}

}

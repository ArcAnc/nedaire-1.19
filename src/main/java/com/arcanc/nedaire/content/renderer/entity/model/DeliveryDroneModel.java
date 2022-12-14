package com.arcanc.nedaire.content.renderer.entity.model;

import com.arcanc.nedaire.content.entities.DeliveryDroneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DeliveryDroneModel<T extends DeliveryDroneEntity> extends EntityModel<T> 
{
	private static final String CABIN = "cabin";
	private static final String TAIL = "tail";
	private static final String NOSE = "nose";
	private static final String VANES_BASE = "vanes_base";
	private static final String VANES_FIRST = "vanes_first";
	private static final String VANES_SECOND = "vanes_secondary";
	private static final String VANES_TAIL = "vanes_tail";
	
	private final ModelPart root;
	private final ModelPart cabin;
	private final ModelPart tail;
	private final ModelPart nose;
	private final ModelPart vanes_base;
	private final ModelPart vanes_first;
	private final ModelPart vanes_second;
	private final ModelPart vanes_tail;

	public DeliveryDroneModel(ModelPart root) 
	{
		super();
		this.root = root;
		this.cabin = root.getChild(CABIN);
		this.tail = cabin.getChild(TAIL);
		this.nose = cabin.getChild(NOSE);
		this.vanes_base = cabin.getChild(VANES_BASE);
		this.vanes_first = vanes_base.getChild(VANES_FIRST);
		this.vanes_second = vanes_base.getChild(VANES_SECOND);
		this.vanes_tail = tail.getChild(VANES_TAIL);
	
	}

	public static LayerDefinition createLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		PartDefinition cabin = root.addOrReplaceChild(CABIN, CubeListBuilder.create().texOffs(0, 2).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.ZERO);
		PartDefinition tail = cabin.addOrReplaceChild(TAIL, CubeListBuilder.create().texOffs(5, 7).addBox(-1.0F, -1.0F, -2.5F, 2.0F, 1.0F, 1.0F), PartPose.ZERO);
		cabin.addOrReplaceChild(NOSE, CubeListBuilder.create().texOffs(0, 7).addBox(-0.5F, -1.0F, 1.5F, 1.0F, 1.0F, 3.0F), PartPose.ZERO);
		PartDefinition vanes = cabin.addOrReplaceChild(VANES_BASE, CubeListBuilder.create().texOffs(9, 2).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);

		tail.addOrReplaceChild(VANES_TAIL, CubeListBuilder.create().texOffs(0, 1).addBox(0.0F, -1.0F, -0.5F, 0.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-0.5F, -0.5F, 4.0F, -0.7854F, 0.0F, 0.0F));

		vanes.addOrReplaceChild(VANES_SECOND, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		vanes.addOrReplaceChild(VANES_FIRST, CubeListBuilder.create().texOffs(0, 1).addBox(-4.0F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		setupAnim(State.FLYING);
	}

	public void setupAnim(DeliveryDroneModel.State state)
	{
		switch (state) 
		{
			case FLYING:
				cabin.xRot = -18.5f;
				vanes_base.yRot = Mth.sin(System.currentTimeMillis() % 360);
				vanes_tail.xRot = Mth.sin(System.currentTimeMillis() % 360);
				break;
			case STANDING:
				break;
		}
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	@OnlyIn(Dist.CLIENT)
	public static enum State 
	{
		FLYING,
	    STANDING
	}
}
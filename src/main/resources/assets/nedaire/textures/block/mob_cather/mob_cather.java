/*// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class mob_cather<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "mob_cather"), "main");
	private final ModelPart base;

	public mob_cather(ModelPart root) {
		this.base = root.getChild("base");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -15.0F, -7.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition e_base_r1 = base.addOrReplaceChild("e_base_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -15.0F, 7.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition w_base_r1 = base.addOrReplaceChild("w_base_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -15.0F, 7.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition s_base_r1 = base.addOrReplaceChild("s_base_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -15.0F, -7.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition top = base.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -30.0F, -7.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition w_top_r1 = top.addOrReplaceChild("w_top_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -30.0F, 7.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition s_top_r1 = top.addOrReplaceChild("s_top_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -30.0F, -7.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition e_top_r1 = top.addOrReplaceChild("e_top_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -30.0F, 7.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition lid = top.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -30.0F, -7.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
*/
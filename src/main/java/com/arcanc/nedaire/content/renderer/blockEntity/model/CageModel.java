/**
 * @author ArcAnc
 * Created at: 2023-02-27
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.blockEntity.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.joml.Vector3f;

import com.arcanc.nedaire.content.block.entities.NBEMobCatcher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;

public class CageModel extends Model 
{
	private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
	private static final String BASE = "base";
	private static final String TOP = "top";
	private static final String LID = "lid";
	
	public static final AnimationDefinition CATCH = AnimationDefinition.Builder.withLength(2.5f).
			addAnimation("base",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, -15.5f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(2f, KeyframeAnimations.posVec(0f, -15.5f, 0f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("top",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, -15f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.posVec(0f, -15f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2f, KeyframeAnimations.posVec(0f, 0.5f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.5f, KeyframeAnimations.posVec(0f, -15f, 0f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("lid",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1.5f, KeyframeAnimations.degreeVec(-270f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(2f, KeyframeAnimations.degreeVec(-270f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(2.5f, KeyframeAnimations.degreeVec(-360f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM))).
			build();
	
	private final ModelPart root;
	private final ModelPart base;
	private final ModelPart top;
	private final ModelPart lid;

	public CageModel(ModelPart root) 
	{
		super(RenderType :: entityTranslucent);
		this.root = root;
		this.base = root.getChild(BASE);
		this.top = base.getChild(TOP);
		this.lid = top.getChild(LID);
	}

	public static LayerDefinition createLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partDef = meshdefinition.getRoot();

		PartDefinition base = partDef.addOrReplaceChild(BASE, CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -15.0F, -7.0F, 14.0F, 15.0F, 0.0F), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition e_base = base.addOrReplaceChild("e_base", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -15.0F, 7.0F, 14.0F, 15.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition w_base = base.addOrReplaceChild("w_base", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -15.0F, 7.0F, 14.0F, 15.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition s_base = base.addOrReplaceChild("s_base", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -15.0F, -7.0F, 14.0F, 15.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition top = base.addOrReplaceChild(TOP, CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -30.0F, -7.0F, 14.0F, 15.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition w_top = top.addOrReplaceChild("w_top", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -30.0F, 7.0F, 14.0F, 15.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition s_top = top.addOrReplaceChild("s_top", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -30.0F, -7.0F, 14.0F, 15.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition e_top = top.addOrReplaceChild("e_top", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -30.0F, 7.0F, 14.0F, 15.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition lid = top.addOrReplaceChild(LID, CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 15.0F, 0.0F), PartPose.offset(0.0F, -30.0F, -7.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	public void setupAnim(NBEMobCatcher tile, long worldTime, float partialTicks) 
	{
		this.root.getAllParts().forEach(ModelPart :: resetPose);
		this.animate(tile.catchAnim, CATCH, worldTime, partialTicks);
	}
	
	protected void animate(AnimationState state, AnimationDefinition animation, float time) 
	{
		this.animate(state, animation, time, 1.0F);
	}

	protected void animate(AnimationState state, AnimationDefinition animation, float time, float f) 
	{
		state.updateTime(time, f);
		state.ifStarted((st) -> 
		{
			animate(this, animation, st.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
		});
	}
	
	public void animate(Model model, AnimationDefinition animation, long startTime, float fl, Vector3f vectorCache) 
	{
		float f = getElapsedSeconds(animation, startTime);

		for(Map.Entry<String, List<AnimationChannel>> entry : animation.boneAnimations().entrySet()) 
		{
			Optional<ModelPart> optional = getAnyDescendantWithName(entry.getKey());
			List<AnimationChannel> list = entry.getValue();
		    optional.ifPresent((part) -> 
		    {
		    	list.forEach((channel) -> 
		    	{
		    		Keyframe[] akeyframe = channel.keyframes();
		            int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, (q) -> 
		            {
		            	return f <= akeyframe[q].timestamp();
		            }) - 1);
		            int j = Math.min(akeyframe.length - 1, i + 1);
		            Keyframe keyframe = akeyframe[i];
		            Keyframe keyframe1 = akeyframe[j];
		            float f1 = f - keyframe.timestamp();
		            float f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
		            keyframe1.interpolation().apply(vectorCache, f2, akeyframe, i, j, fl);
		            channel.target().apply(part, vectorCache);
		    	});
		    });
		}

	}

	private static float getElapsedSeconds(AnimationDefinition animation, long time) 
	{
		float f = (float)time / 1000.0F;
		return animation.looping() ? f % animation.lengthInSeconds() : f;
	}
	
	public Optional<ModelPart> getAnyDescendantWithName(String str)
	{
		return str.equals("root") ? Optional.of(this.root) : this.root.getAllParts().filter((part) -> 
		{
			return part.hasChild(str);
		}).findFirst().map((part) -> 
		{
			return part.getChild(str);
		});
	}
}

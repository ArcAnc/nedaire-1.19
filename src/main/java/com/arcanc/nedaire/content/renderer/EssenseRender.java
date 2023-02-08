/**
 * @author ArcAnc
 * Created at: 2023-02-06
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;

public class EssenseRender 
{
	/**
	 * FIXME: add a bit more smooth transit from start to finish essence effect. Fix bug with add/remove work and add effect to moving this from start to finish
	 */
	
	private static final int LENGTH_POINT_COUNT = 20;
	private static final int RADIUS_POINT_COUNT = 6;
	private static final double SHIFT = 0.0015d;
	private static final double MAX_SHIFT = 0.05;
	
	private static final Map<Vec3, List<PointsData>> ESSENCE_MAP = Maps.newHashMap();
	
	public static final ResourceLocation ESSENCE_TEXURE = StringHelper.getLocFStr("misc/essence");

	public static void worldRender(final RenderLevelStageEvent evt)
	{
		if (evt.getStage() == Stage.AFTER_TRANSLUCENT_BLOCKS)
		{
			Minecraft mc = RenderHelper.mc();
			
			TextureAtlasSprite tex = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ESSENCE_TEXURE);
			float texW = tex.getU1() - tex.getU0();
			float texH = tex.getV1() - tex.getV0();

			for (Map.Entry<Vec3, List<PointsData>> e : ESSENCE_MAP.entrySet())
			{
				for(PointsData data : e.getValue())
				{
					PoseStack mStack = evt.getPoseStack();
					
					for(int q = 0; q < LENGTH_POINT_COUNT; q++)
					{
						for (int t = 0; t < RADIUS_POINT_COUNT; t++)
						{
							data.tube[q][t] = data.tube()[q][t].add(SHIFT * data.dir()[q][t].x(), SHIFT * data.dir()[q][t].y(), SHIFT * data.dir()[q][t].z());
							data.way[q][t] += SHIFT;
							
							if(data.way()[q][t] >= MAX_SHIFT)
							{
								data.way[q][t] = 0;
								data.dir[q][t] = data.dir()[q][t].reverse();
							}
						}
					}							
				
					for(int q = 0; q < LENGTH_POINT_COUNT - 1; q++)
					{
						for (int t = 0; t < RADIUS_POINT_COUNT; t++)
						{
							mStack.pushPose();
							
							mStack.translate(-evt.getCamera().getPosition().x(), -evt.getCamera().getPosition().y(), -evt.getCamera().getPosition().z());
							Matrix4f matrix = mStack.last().pose();
							Matrix3f normal = mStack.last().normal();
							
							VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(Sheets.translucentCullBlockSheet());
							float texU0 = tex.getU0() + texW / RADIUS_POINT_COUNT * t;
							float texU1 = tex.getU0() + texW / RADIUS_POINT_COUNT * (t + 1);
							float texV0 = tex.getV0() + texH / RADIUS_POINT_COUNT * t;
							float texV1 = tex.getV0() + texH / RADIUS_POINT_COUNT * (t + 1);

							if (t != RADIUS_POINT_COUNT - 1 )
							{
								buffer.vertex(matrix, 	(float)data.tube[q]	   [t    ].x(), 
										  				(float)data.tube[q]	   [t    ].y(), 
										  				(float)data.tube[q]    [t    ].z()).
									color(255, 255, 255, 255).
									uv(texU0, texV0).
									overlayCoords(OverlayTexture.NO_OVERLAY).
									uv2(LightTexture.FULL_BRIGHT).
									normal(normal, 0, 0, 1).
									endVertex();
								buffer.vertex(matrix, 	(float)data.tube[q + 1][t    ].x(),
										  				(float)data.tube[q + 1][t    ].y(), 
										  				(float)data.tube[q + 1][t    ].z()).
									color(255, 255, 255, 255).
									uv(texU1, texV0).
									overlayCoords(OverlayTexture.NO_OVERLAY).
									uv2(LightTexture.FULL_BRIGHT).
									normal(normal, 0, 0, 1).
									endVertex();
								buffer.vertex(matrix, 	(float)data.tube[q + 1][t + 1].x(),
										  				(float)data.tube[q + 1][t + 1].y(),
										  				(float)data.tube[q + 1][t + 1].z()).
									color(255, 255, 255, 255).
									uv(texU1, texV1).
									overlayCoords(OverlayTexture.NO_OVERLAY).
									uv2(LightTexture.FULL_BRIGHT).
									normal(normal, 0, 0, 1).
									endVertex();
								buffer.vertex(matrix, 	(float)data.tube[q]	   [t + 1].x(),
										  				(float)data.tube[q]	   [t + 1].y(),
										  				(float)data.tube[q]    [t + 1].z()).
									color(255, 255, 255, 255).
									uv(texU0, texV1).
									overlayCoords(OverlayTexture.NO_OVERLAY).
									uv2(LightTexture.FULL_BRIGHT).
									normal(normal, 0, 0, 1).
									endVertex();
								mStack.popPose();
								
							}
							else
							{
								buffer.vertex(matrix, 	(float)data.tube[q]	   [t    ].x(), 
														(float)data.tube[q]	   [t    ].y(), 
														(float)data.tube[q]    [t    ].z()).
									color(255, 255, 255, 255).
									uv(texU0, texV0).
									overlayCoords(OverlayTexture.NO_OVERLAY).
									uv2(LightTexture.FULL_BRIGHT).
									normal(normal, 0, 0, 1).
									endVertex();
								buffer.vertex(matrix, 	(float)data.tube[q + 1][t    ].x(),
										  				(float)data.tube[q + 1][t    ].y(), 
										  				(float)data.tube[q + 1][t    ].z()).
									color(255, 255, 255, 255).
									uv(texU1, texV0).
									overlayCoords(OverlayTexture.NO_OVERLAY).
									uv2(LightTexture.FULL_BRIGHT).
									normal(normal, 0, 0, 1).
									endVertex();
								buffer.vertex(matrix, 	(float)data.tube[q + 1][0].x(),
										  				(float)data.tube[q + 1][0].y(),
										  				(float)data.tube[q + 1][0].z()).
									color(255, 255, 255, 255).
									uv(texU1, texV1).
									overlayCoords(OverlayTexture.NO_OVERLAY).
									uv2(LightTexture.FULL_BRIGHT).
									normal(normal, 0, 0, 1).
									endVertex();
								buffer.vertex(matrix, 	(float)data.tube[q]	   [0].x(),
										  				(float)data.tube[q]	   [0].y(),
										  				(float)data.tube[q]    [0].z()).
									color(255, 255, 255, 255).
									uv(texU0, texV1).
									overlayCoords(OverlayTexture.NO_OVERLAY).
									uv2(LightTexture.FULL_BRIGHT).
									normal(normal, 0, 0, 1).
									endVertex();
								mStack.popPose();
								
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean addNewPoint(Vec3 startPoint, Vec3 finishPoint)
	{
		Minecraft mc = RenderHelper.mc();
		ClientLevel level = mc.level;
		
		double distance = finishPoint.distanceTo(startPoint);
		
		RandomSource rand = level.getRandom();
		Supplier<Double> rD = () -> (rand.nextDouble() - 0.5d) * 2;
		
		Vec3 s1 = startPoint.multiply(rD.get(), rD.get(), rD.get()).normalize().scale(distance);
		Vec3 s2 = finishPoint.multiply(rD.get(), rD.get(), rD.get()).normalize().scale(distance);
		
		Vec3 p1 = startPoint.add(s1.scale(1/3d));
		Vec3 p2 = finishPoint.subtract(s2.scale(1/3d));

		Vec3[][] tube = new Vec3[LENGTH_POINT_COUNT][RADIUS_POINT_COUNT];
		Vec3[][] dir = new Vec3[LENGTH_POINT_COUNT][RADIUS_POINT_COUNT];
		double[][] way = new double[LENGTH_POINT_COUNT][RADIUS_POINT_COUNT];
		Vec3[] curve = new Vec3[LENGTH_POINT_COUNT];
		
		int angleMod = 360 / RADIUS_POINT_COUNT;
		
		for (int q = 0 ; q < RADIUS_POINT_COUNT; q++)
		{
			tube[0][q] = startPoint;
			tube[LENGTH_POINT_COUNT - 1][q] = finishPoint;
			dir[0][q] = Vec3.ZERO;
			dir[LENGTH_POINT_COUNT - 1][q] = Vec3.ZERO;
		}
		curve[0] = startPoint;
		curve[LENGTH_POINT_COUNT - 1] = finishPoint;
		for(int q = 1; q < LENGTH_POINT_COUNT-1; q++)
		{
			float t = q / (float)LENGTH_POINT_COUNT;
			curve[q] = calculateBezierPoint(t, startPoint, p1, p2, finishPoint);
			int vec = level.random.nextFloat() <= 0.5d ? -1 : 1;
			float scale = level.random.nextFloat() + 0.5f;
			for (int angle = 0; angle < RADIUS_POINT_COUNT; angle++)
			{
				double angR = Math.toRadians(angle * angleMod);
				Supplier<Double> r = ()-> 
				{
					return 0.3d + (Math.sin(level.getRandom().nextDouble() * 360)) * 0.015d + Math.sin(angR) * 0.015d;  
				};
				
				Vec3 curvePoint = new Vec3(r.get() * Math.sin(angR) * Math.cos(Math.toRadians(90)), r.get() * Math.sin(angR) * Math.sin(Math.toRadians(90)), r.get() * Math.cos(angR)).add(curve[q]);

				tube[q][angle] = curvePoint;
				
				dir[q][angle] = curvePoint.subtract(curve[q]).scale(vec);

				double length = dir[q][angle].length();
				dir[q][angle] = new Vec3(dir[q][angle].x()/length, dir[q][angle].y() / length, dir[q][angle].z() / length);
				
				tube[q][angle] = tube[q][angle].add(MAX_SHIFT * scale * dir[q][angle].x() * -vec, MAX_SHIFT * scale * dir[q][angle].y() * -vec, MAX_SHIFT * scale * dir[q][angle].z() * -vec);
			}
		}

		PointsData data = new PointsData(p1, p2, finishPoint, tube, dir, way, curve);
		
		ESSENCE_MAP.putIfAbsent(startPoint, new ArrayList<>());
		List<PointsData> list = ESSENCE_MAP.getOrDefault(startPoint, new ArrayList<>());
		if (!list.contains(data))
			return list.add(data);
		return false;
	}
	
	public static boolean removePoint(Vec3 startPoint, Vec3 finishPoint)
	{
		List<PointsData> list = ESSENCE_MAP.getOrDefault(startPoint, new ArrayList<>());
		PointsData data = list.stream().filter(d -> d.finishPoint().equals(finishPoint)).findFirst().orElse(new PointsData(null, null, null, null, null, null, null));
		return list.remove(data);
	}
	
	public static Vec3 calculateBezierPoint(float t, Vec3 start, Vec3 p1, Vec3 p2, Vec3 finish)
	{
	    float oneMinusT = 1 - t;
	    return start.scale(Math.pow(oneMinusT, 3)).
	    		add(p1.scale(3f * oneMinusT * oneMinusT * t)).
	    		add(p2.scale(3f * oneMinusT * t * t)).
	    		add(finish.scale(t * t * t));
	}
	
	public static Vec3 getFirstDerivative (float t, Vec3 start, Vec3 p1, Vec3 p2, Vec3 finish)
	{
		float oneMinusT = 1 - t;
		
		return p1.subtract(start).scale(3f * oneMinusT * oneMinusT).
				add(p2.subtract(p1).scale(6f * oneMinusT * t)).
				add(finish.subtract(p2).scale(t * t * 3f));
	}
	
	private record PointsData(Vec3 p1, Vec3 p2, Vec3 finishPoint, Vec3[][] tube, Vec3[][] dir, double[][] way, Vec3[] curve)
	{
	}
}

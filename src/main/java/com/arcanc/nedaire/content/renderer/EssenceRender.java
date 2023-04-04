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
import org.joml.Vector3d;

import com.arcanc.nedaire.content.container.sync.GetterAndSetter;
import com.arcanc.nedaire.content.registration.NRegistration;
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
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;

public class EssenceRender 
{
	/**
	 * FIXME: add a bit more smooth transit from start to finish essence effect. Fix bug with add/remove work and add effect to moving this from start to finish
	 */
	
	private static final int LENGTH_POINT_COUNT = 20;
	private static final int RADIUS_POINT_COUNT = 6;
	private static final double SHIFT = 0.0015d;
	private static final double MAX_SHIFT = 0.05;
	
	private static final Map<Vector3d, List<PointsData>> ESSENCE_MAP = Maps.newHashMap();
	private static final Map<Vector3d, List<RenderData>> MAP = Maps.newHashMap();
	
	public static final ResourceLocation ESSENCE_TEXURE = StringHelper.getLocFStr("misc/essence");

	public static void worldRender(final RenderLevelStageEvent evt)
	{
		if (evt.getStage() == Stage.AFTER_TRANSLUCENT_BLOCKS)
		{
			Minecraft mc = RenderHelper.mc();
			
			TextureAtlasSprite tex = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ESSENCE_TEXURE);
			float texW = tex.getU1() - tex.getU0();
			float texH = tex.getV1() - tex.getV0();

			for (Map.Entry<Vector3d, List<PointsData>> e : ESSENCE_MAP.entrySet())
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
								data.dir[q][t] = data.dir()[q][t].negate();
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
	
	public static boolean addNewPoint(Vector3d startPoint, Vector3d finishPoint)
	{
		Minecraft mc = RenderHelper.mc();
		ClientLevel level = mc.level;
		
		double distance = finishPoint.distance(startPoint);
		
		RandomSource rand = level.getRandom();
		Supplier<Double> rD = () -> (rand.nextDouble() - 0.5d) * 2;
		
		Vector3d s1 = startPoint.mul(rD.get(), rD.get(), rD.get()).normalize().mul(distance);
		Vector3d s2 = finishPoint.mul(rD.get(), rD.get(), rD.get()).normalize().mul(distance);
		
		Vector3d p1 = startPoint.add(s1.mul(1/3d));
		Vector3d p2 = finishPoint.sub(s2.mul(1/3d));

		Vector3d[][] tube = new Vector3d[LENGTH_POINT_COUNT][RADIUS_POINT_COUNT];
		Vector3d[][] dir = new Vector3d[LENGTH_POINT_COUNT][RADIUS_POINT_COUNT];
		double[][] way = new double[LENGTH_POINT_COUNT][RADIUS_POINT_COUNT];
		Vector3d[] curve = getBerzierCurve(startPoint, p1, p2, finishPoint);
		
		int angleMod = 360 / RADIUS_POINT_COUNT;
		
		for (int q = 0 ; q < RADIUS_POINT_COUNT; q++)
		{
			tube[0][q] = startPoint;
			tube[LENGTH_POINT_COUNT - 1][q] = finishPoint;
			dir[0][q] = new Vector3d();
			dir[LENGTH_POINT_COUNT - 1][q] = new Vector3d();
		}
//		curve[0] = startPoint;
//		curve[LENGTH_POINT_COUNT - 1] = finishPoint;
		for(int q = 1; q < LENGTH_POINT_COUNT-1; q++)
		{
//			float t = q / (float)LENGTH_POINT_COUNT;
//			curve[q] = calculateBezierPoint(t, startPoint, p1, p2, finishPoint);
			int vec = level.random.nextFloat() <= 0.5d ? - 1 : 1;
			float mul = level.random.nextFloat() + 0.5f;
			for (int angle = 0; angle < RADIUS_POINT_COUNT; angle++)
			{
				double angR = Math.toRadians(angle * angleMod);
				Supplier<Double> r = ()-> 
				{
					return 0.3d + (Math.sin(level.getRandom().nextDouble() * 360)) * 0.015d + Math.sin(angR) * 0.015d;  
				};
				
				Vector3d direction = curve[q + 1].sub(curve[q]);
				Vector3d angleX = new Vector3d(direction.x()/direction.length(), direction.y()/direction.length(), direction.z()/direction.length());
				
				Vector3d curvePoint = new Vector3d(r.get() * Math.sin(angR) * Math.cos(Math.toRadians(90)), r.get() * Math.sin(angR) * Math.sin(Math.toRadians(90)) , r.get() * Math.cos(angR)).add(curve[q]);

				tube[q][angle] = curvePoint;
				
				dir[q][angle] = curvePoint.sub(curve[q]).mul(vec);

				double length = dir[q][angle].length();
				dir[q][angle] = new Vector3d(dir[q][angle].x()/length, dir[q][angle].y() / length, dir[q][angle].z() / length);
				
				tube[q][angle] = tube[q][angle].add(MAX_SHIFT * mul * dir[q][angle].x() * -vec, MAX_SHIFT * mul * dir[q][angle].y() * -vec, MAX_SHIFT * mul * dir[q][angle].z() * -vec);
			}
		}

		PointsData data = new PointsData(p1, p2, finishPoint, tube, dir, way, curve);
		
		ESSENCE_MAP.putIfAbsent(startPoint, new ArrayList<>());
		List<PointsData> list = ESSENCE_MAP.getOrDefault(startPoint, new ArrayList<>());
		if (!list.contains(data))
			return list.add(data);
		return false;
	}
	
	public static boolean removePoint(Vector3d startPoint, Vector3d finishPoint)
	{
		List<PointsData> list = ESSENCE_MAP.getOrDefault(startPoint, new ArrayList<>());
		PointsData data = list.stream().filter(d -> d.finishPoint().equals(finishPoint)).findFirst().orElse(new PointsData(null, null, null, null, null, null, null));
		return list.remove(data);
	}
	
	public static Vector3d[] getBerzierCurve(Vector3d start, Vector3d p1, Vector3d p2, Vector3d finish)
	{
		Vector3d[] points = new Vector3d[LENGTH_POINT_COUNT];
		for (int q = 0; q < LENGTH_POINT_COUNT; q++)
		{
			float t = q / (float)LENGTH_POINT_COUNT;
			points[q] = calculateBezierPoint(t, start, p1, p2, finish);
		}
		
		return points;
	}
	
	public static Vector3d calculateBezierPoint(float t, Vector3d start, Vector3d p1, Vector3d p2, Vector3d finish)
	{
	    float oneMinusT = 1 - t;
	    Vector3d vec0 = new Vector3d();
	    Vector3d vec1 = new Vector3d();
	    Vector3d vec2 = new Vector3d();
	    Vector3d vec3 = new Vector3d();
	    
	    return start.mul(Math.pow(oneMinusT, 3), vec0).
	    		add(p1.mul(3f * oneMinusT * oneMinusT * t, vec1), vec1).
	    		add(p2.mul(3f * oneMinusT * t * t, vec2), vec2).
	    		add(finish.mul(t * t * t, vec3), vec3);
	}
	
	public static Vector3d getFirstDerivative (float t, Vector3d start, Vector3d p1, Vector3d p2, Vector3d finish)
	{
		float oneMinusT = 1 - t;
		
		return p1.sub(start).mul(3f * oneMinusT * oneMinusT).
				add(p2.sub(p1).mul(6f * oneMinusT * t)).
				add(finish.sub(p2).mul(t * t * 3f));
	}
	
	private record PointsData(Vector3d p1, Vector3d p2, Vector3d finishPoint, Vector3d[][] tube, Vector3d[][] dir, double[][] way, Vector3d[] curve)
	{
	}

	public static void worldRenderPatricle(final RenderLevelStageEvent evt) 
	{
		if (evt.getStage() == Stage.AFTER_PARTICLES)
		{
			Minecraft mc = RenderHelper.mc();
			ClientLevel level = mc.level;
			if (level.getGameTime() % 5 == 0)
			{
				for (Map.Entry<Vector3d, List<RenderData>> entry : MAP.entrySet())
				{
					for (RenderData data : entry.getValue())
					{
						int step = data.currentStep().get();
						
						level.addParticle(NRegistration.RegisterParticleTypes.ESSENCE.get(), 
								data.curve()[step].x(), data.curve()[step].y(), data.curve()[step].z(), 102/255f, 0/255f, 204/255f);
						
						if (++step > LENGTH_POINT_COUNT - 1)
							step = 0;
						
						data.currentStep().set(step);
					}
				}
			}
		}
	}
	
	public static boolean addNewPointPatricle(Vector3d startPoint, Vector3d finishPoint)
	{
		Minecraft mc = RenderHelper.mc();
		ClientLevel level = mc.level;
		
		double distance = finishPoint.distance(startPoint);
		
		RandomSource rand = level.getRandom();
		Supplier<Double> rD = () -> (rand.nextDouble() - 0.5d) * 2;
		
		
		Vector3d s1 = new Vector3d();
		Vector3d s2 = new Vector3d();
		Vector3d p1 = new Vector3d();
		Vector3d p2 = new Vector3d();
		
		startPoint.mul(rD.get(), rD.get(), rD.get(), s1).normalize().mul(distance);
		
		finishPoint.mul(rD.get(), rD.get(), rD.get(), s2).normalize().mul(distance);
		
		startPoint.add(s1.mul(1/3d), p1);
		finishPoint.sub(s2.mul(1/3d), p2);

		Vector3d[] curve = getBerzierCurve(startPoint, p1, p2, finishPoint);

		RenderData data = new RenderData(p1, p2, finishPoint, curve, GetterAndSetter.standalone(0));
		
		
		MAP.putIfAbsent(startPoint, new ArrayList<>());
		List<RenderData> list = MAP.getOrDefault(startPoint, new ArrayList<>());
		if (!list.contains(data))
			return list.add(data);
		return false;
		}
	
	public static boolean removePointParticle(Vector3d startPoint, Vector3d finishPoint)
	{
		List<RenderData> list = MAP.getOrDefault(startPoint, new ArrayList<>());
		RenderData data = list.stream().filter(d -> d.finishPoint().equals(finishPoint)).findFirst().orElse(new RenderData(null, null, null, null, GetterAndSetter.standalone(0)));
		return list.remove(data);
	}
	
	private record RenderData(Vector3d p1, Vector3d p2, Vector3d finishPoint, Vector3d[] curve, GetterAndSetter<Integer> currentStep)
	{}
}
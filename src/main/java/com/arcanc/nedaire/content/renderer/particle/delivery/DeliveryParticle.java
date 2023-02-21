/**
 * @author ArcAnc
 * Created at: 2023-02-19
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.renderer.particle.delivery;

import java.util.function.Supplier;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class DeliveryParticle<T> extends Particle
{

	protected Vec3 startPos;
	protected Vec3 finishPos;
	protected Vec3 station;
	protected boolean toStation;
	
	protected Vec3[] path;
	
	protected T content;
	
	public DeliveryParticle(ClientLevel level, Vec3 startPos, Vec3 finishPos, Vec3 station, boolean toStation) 
	{
		super(level, startPos.x(), startPos.y(), startPos.z());
		
		this.startPos = startPos;
		this.finishPos = finishPos;
		this.station = station;
		this.toStation = toStation;
		
		this.path = toStation ? calculatePath(startPos, station, level) : calculatePath(station, finishPos, level);
		
		this.hasPhysics = false;
		this.age = 0;
	}

	@Override
	public void tick() 
	{
		this.xo = this.x;
	    this.yo = this.y;
	    this.zo = this.z;
	 
	    this.move(path[age].x() - xo, path[age].y() - yo, path[age].z() - zo);
	    
	    this.age++;
	    if (age == path.length)
	    {
	    	if (toStation)
	    	{
	    		this.toStation = false;
	    		this.age = 0;
	    		this.path = calculatePath(station, finishPos, level);
	    	}
	    	else
	    	{
	    		this.remove();
	    	}
	    }
	}
	
	protected abstract T getContent();
	
	public static Vec3[] calculatePath(Vec3 start, Vec3 finish, Level level)
	{
		RandomSource rand = level.getRandom();
		Supplier<Double> rD = () -> (rand.nextDouble() - 0.5d) * 2;
		
		double distance = finish.distanceTo(start);
		
		Vec3 s1 = start.multiply(rD.get(), rD.get(), rD.get()).normalize().scale(distance);
		Vec3 s2 = finish.multiply(rD.get(), rD.get(), rD.get()).normalize().scale(distance);
		Vec3 p1 = start.add(s1.scale(1/3d));
		Vec3 p2 = finish.subtract(s2.scale(1/3d));

		return getBerzierCurve(start, p1, p2, finish, distance);
	}
	
	public static Vec3[] getBerzierCurve(Vec3 start, Vec3 p1, Vec3 p2, Vec3 finish, double distance)
	{
		int modifier = 10;
		int amount = (int)(distance * modifier) < modifier ? modifier : (int)(distance * modifier);
		
		Vec3[] points = new Vec3[amount];
		for (int q = 0; q < amount; q++)
		{
			float t = q / (float)amount;
			points[q] = calculateBezierPoint(t, start, p1, p2, finish);
		}
		
		return points;
	}
	
	public static Vec3 calculateBezierPoint(float t, Vec3 start, Vec3 p1, Vec3 p2, Vec3 finish)
	{
	    float oneMinusT = 1 - t;

	    return start.scale(Math.pow(oneMinusT, 3)).
	    		add(p1.scale(3f * oneMinusT * oneMinusT * t)).
	    		add(p2.scale(3f * oneMinusT * t * t)).
	    		add(finish.scale(t * t * t));
	}
	
	@Override
	public ParticleRenderType getRenderType() 
	{
		return ParticleRenderType.CUSTOM;
	}

}

/**
 * @author ArcAnc
 * Created at: 2022-06-25
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.gem;

public class GemEffectInstance <T extends GemEffect<T>>  
{
	private final GemEffect<T> effect;
	
	public GemEffectInstance(GemEffect<T> effect) 
	{
		this.effect = effect;
		
		GemUtils.effectMap.putIfAbsent(effect.getId(), this);
	}
	
	public GemEffect<T> getEffect() 
	{
		return effect;
	}
}

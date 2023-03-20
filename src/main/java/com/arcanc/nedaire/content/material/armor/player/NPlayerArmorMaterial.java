/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.material.armor.player;

public class NPlayerArmorMaterial extends NAbstractPlayerArmorMaterial 
{

	protected NPlayerArmorMaterial(Builder builder) 
	{
		super(builder);
	}
	
	public static class Builder extends NAbstractPlayerArmorMaterial.PlayerArmorAbstractBuilder<Builder>
	{
		public Builder () {}
		
		@Override
		public NPlayerArmorMaterial build() 
		{
			return new NPlayerArmorMaterial(getSelf());
		}
		
		@Override
		protected Builder getSelf() 
		{
			return this;
		}
	}

}
	
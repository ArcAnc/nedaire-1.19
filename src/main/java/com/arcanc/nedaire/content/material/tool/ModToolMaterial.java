/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.material.tool;

public class ModToolMaterial extends ModAbstractToolMaterial
{

	public ModToolMaterial(ToolAbstractBuilder<?> builder) 
	{
		super(builder);
	}

	public static class Builder extends ModAbstractToolMaterial.ToolAbstractBuilder<Builder>
	{
		public Builder () {}
		
		@Override
		public ModToolMaterial build() 
		{
			return new ModToolMaterial(getSelf());
		}
		
		@Override
		protected Builder getSelf() 
		{
			return this;
		}
	}
}

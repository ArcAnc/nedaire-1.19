/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.material.armor.horse;

import net.minecraft.resources.ResourceLocation;

public interface IHorseArmorMaterial 
{
	public int getDurability();
	
	public int getDefense();
	
	public ResourceLocation getTexturePath ();
}

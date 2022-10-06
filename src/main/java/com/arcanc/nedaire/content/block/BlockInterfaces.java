/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import net.minecraft.client.renderer.RenderType;

public class BlockInterfaces 
{
	public interface IBlockRenderLayer
	{
		RenderType getRenderLayer(); 
		
		void setRenderLayer(RenderType renderLayer); 
	}
	
	public interface IInventoryCallback
	{
	    default void onInventoryChange(int slot) 
	    {

	    }

	    default void onTankChange(int tank) 
	    {

	    }
	    
	    default void onVimChange (int slot)
	    {
	    	
	    }

	    default boolean clearSlot(int slot) 
	    {
	        return false;
	    }

	    default boolean clearTank(int tank) 
	    {
	        return false;
	    }
	    
	    /**
	     * FIXME: add essence or another energy which will be implemented soon
	     */
	}

}

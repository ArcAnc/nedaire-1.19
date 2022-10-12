/**
 * @author ArcAnc
 * Created at: 2022-10-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.registration.NRegistration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NBEManualCrusher extends NBERedstoneSensitive implements IInventoryCallback
{
	/**FIXME: ������ ��������. ������� ����� - �� ������� ������� ��, ��� ��������� ������. �������� ����������� �����, �� ��� �� �����*/
	
	public NBEManualCrusher(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_MANUAL_CRUSHER.get(), pos, state);
		// TODO Auto-generated constructor stub
	}

	public void power() 
	{
		
	}

}

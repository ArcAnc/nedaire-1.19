/**
 * @author ArcAnc
 * Created at: 2022-10-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block.entities;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInventoryCallback;
import com.arcanc.nedaire.content.block.entities.ticker.ModClientTickerBlockEntity;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.inventory.ItemStackHolder;
import com.arcanc.nedaire.util.inventory.ModSimpleItemStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class NBEManualCrusher extends NBERedstoneSensitive implements IInventoryCallback, ModServerTickerBlockEntity, ModClientTickerBlockEntity
{
	/**FIXME: ручная дробилка. Крутишь ручку - по немного дробишь то, что находится внутри. Вероятно понадобится тикер, но это не точно*/
	
	protected int currentAngle;
	/*360 angles max*/
	protected final int anglePerTick = 6; 
	
	protected int currentTime;
	
	protected final int energyPerUse = 200;
	protected VimStorage energy;
	protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
	protected ModSimpleItemStorage inv;
	protected final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inv);
	
	public NBEManualCrusher(BlockPos pos, BlockState state) 
	{
		super(NRegistration.RegisterBlockEntities.BE_MANUAL_CRUSHER.get(), pos, state);
	
		this.energy = VimStorage.newConfig().setMaxEnergy(1000).setEnergy(0).build();
		this.inv = new ModSimpleItemStorage(this).addSlots(
				Stream.generate(
						()-> new ItemStackHolder()).
				limit(5).
				collect(Collectors.toList())
				);
	}

	public void power() 
	{
		if (!this.level.isClientSide())
		{
			energy.add(this.energyPerUse, false);
		}
	}

	@Override
	public void tickClient() 
	{
		
	}

	@Override
	public void tickServer() 
	{
		
	}

}

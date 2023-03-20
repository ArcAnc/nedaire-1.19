/**
 * @author ArcAnc
 * Created at: 2022-04-08
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.tool;

import java.util.Optional;

import com.arcanc.nedaire.content.block.entities.NBEDeliveryStation;
import com.arcanc.nedaire.content.item.NBaseItem;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NHammer extends NBaseItem
{

	private static final String COORDS = StringHelper.getStrLocFStr("coords");
	
	public NHammer(Properties props) 
	{
		super(props);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext ctx) 
	{
		ItemStack stack = ctx.getPlayer().getItemInHand(ctx.getHand());
		CompoundTag tag = stack.getOrCreateTag();
		BlockPos pos = ctx.getClickedPos();
		
		Optional<BlockEntity> tile = BlockHelper.getTileEntity(ctx.getLevel(), pos);
		if (tile.isPresent())
		{
			tile.ifPresent(t -> 
			{
				if (!ctx.getLevel().isClientSide())
				{
					if (t instanceof NBEDeliveryStation station)
					{
						if (tag.contains(COORDS) && ctx.getPlayer().isCrouching())
						{
							CompoundTag coords = tag.getCompound(COORDS);
							
							BlockPos tilePos = new BlockPos(coords.getInt("x"), coords.getInt("y"), coords.getInt("z"));
							if(tilePos.closerThan(pos, 32))
							{
								station.addTile(ctx.getLevel(), tilePos);
							}
							tag.remove(COORDS);
						}
						
					}
					else
					{
						if ((ItemHelper.isItemHandler(t) || FluidHelper.isFluidHandler(t) || VimHelper.isVimHandler(t)) && ctx.getPlayer().isCrouching())
						{
							CompoundTag coords = new CompoundTag();
							coords.putInt("x", pos.getX());
							coords.putInt("y", pos.getY());
							coords.putInt("z", pos.getZ());
							
							tag.put(COORDS, coords);
						}
					}
				}
			});
			return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide());
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) 
	{
		return false;
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) 
	{
		return false;
	}
	
	@Override
	public int getEnchantmentValue() 
	{
		return 0;
	}

}

/**
 * @author ArcAnc
 * Created at: 2022-04-08
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.tool;

import com.arcanc.nedaire.content.block.BlockInterfaces;
import com.arcanc.nedaire.content.item.NBaseItem;
import com.arcanc.nedaire.util.helpers.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.arcanc.nedaire.util.database.NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.COORDS;

public class NHammer extends NBaseItem
{
	public NHammer(Properties props)
	{
		super(props);
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx)
	{
		BlockPos pos = ctx.getClickedPos();
		Level level = ctx.getLevel();
		Player player = ctx.getPlayer();
		CompoundTag tag = stack.getOrCreateTag();

		return BlockHelper.getTileEntity(level, pos).map(t ->
		{
			if (!level.isClientSide())
			{
				if (player.isCrouching())
				{
					if (ItemHelper.isItemHandler(t) || FluidHelper.isFluidHandler(t) || VimHelper.isVimHandler(t))
					{
						TagHelper.writeBlockPos(pos, tag, COORDS);
						return InteractionResult.sidedSuccess(level.isClientSide());
					}
				}
				else
				{
					if (t instanceof BlockInterfaces.INWrencheble wrencheble)
					{
						return wrencheble.onUsed(ctx);
					}
				}
			}
			return InteractionResult.PASS;
		}).orElse(InteractionResult.PASS);
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx)
	{
		return super.useOn(ctx);
	}
	
	@Override
	public boolean isEnchantable(@NotNull ItemStack stack)
	{
		return false;
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) 
	{
		return false;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public int getEnchantmentValue() 
	{
		return 0;
	}

}

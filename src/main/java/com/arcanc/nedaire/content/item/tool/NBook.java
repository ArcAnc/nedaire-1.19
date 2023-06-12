/**
 * @author ArcAnc
 * Created at: 2022-09-08
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item.tool;

import javax.annotation.Nonnull;

import com.arcanc.nedaire.content.book.gui.EnchiridionScreen;
import com.arcanc.nedaire.content.item.NBaseItem;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class NBook extends NBaseItem 
{

	public NBook(Properties props) 
	{
		super(props.stacksTo(1));
	}
	
	
	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player pl, @NotNull InteractionHand hand)
	{
		ItemStack stack = pl.getItemInHand(hand);
		if (level.isClientSide)
		{
			Minecraft.getInstance().setScreen(new EnchiridionScreen());
		}
		pl.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
	
	@Override
	public @NotNull InteractionResult useOn(UseOnContext context)
	{
	      Level level = context.getLevel();
	      BlockPos blockpos = context.getClickedPos();
	      BlockState blockstate = level.getBlockState(blockpos);
	      if (blockstate.getBlock() instanceof LecternBlock) 
	         return LecternBlock.tryPlaceBook(context.getPlayer(), level, blockpos, blockstate, context.getItemInHand()) ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
         return InteractionResult.PASS;
	}
}

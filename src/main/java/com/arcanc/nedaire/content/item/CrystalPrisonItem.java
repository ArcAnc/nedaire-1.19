/**
 * @author ArcAnc
 * Created at: 2023-02-24
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.item;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arcanc.nedaire.content.entities.ThrownCrystalPrison;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CrystalPrisonItem extends NBaseItem
{

	public static final String ENTITY_DATA = StringHelper.getStrLocFStr("entity_data");
	
	public static final List<EntityType<?>> BLOCK_LIST = Stream.of(EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.WARDEN).collect(Collectors.toList());
	
	public CrystalPrisonItem(Properties props) 
	{
		super(props);
	}

	@Override
	public ItemStack getDefaultInstance() 
	{
		ItemStack stack = super.getDefaultInstance();
		stack.getOrCreateTag().put(ENTITY_DATA, new CompoundTag());
		return stack;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) 
	{
	      ItemStack itemstack = player.getItemInHand(hand);
	      level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
	      player.getCooldowns().addCooldown(this, 20);
	      if (!level.isClientSide()) 
	      {
	    	 ThrownCrystalPrison prison = new ThrownCrystalPrison(level, player);
	         prison.setItem(itemstack);
	         prison.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
	         level.addFreshEntity(prison);
	      }

	      player.awardStat(Stats.ITEM_USED.get(this));
	      if (!player.getAbilities().instabuild) 
	      {
	         itemstack.shrink(1);
	      }

	      return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}
}

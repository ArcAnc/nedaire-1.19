/**
 * @author ArcAnc
 * Created at: 2023-01-27
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.world.level.levelgen.village;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.arcanc.nedaire.content.registration.NRegistration;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class NVillage 
{
	/**
	 * FIXME: add custom recipes
	 */
	public static void addCustomTrades(final VillagerTradesEvent event)
	{
		Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
		if (event.getType() == NRegistration.RegisterVillage.UNDERGROUNDER.get())
		{
			trades.get(1).add(new EmeraldForItems(NRegistration.RegisterItems.NUGGET_SKYSTONE.get(), new PriceInterval(2, 4), 10, 8));
			trades.get(1).add(new ItemsForEmerald(NRegistration.RegisterItems.NUGGET_SKYSTONE.get(), new PriceInterval(-10, -6), 10, 8));
		}
	}
	
	private static class EmeraldForItems implements ItemListing
	{
		private final Function<Level, ItemStack> getBuyingItem;
		@Nullable
		private ItemStack buyingItem;
		private final PriceInterval buyAmounts;
		private final int maxUses;
		private final int xp;

		public EmeraldForItems(@Nonnull Function<Level, ItemStack> item, @Nonnull PriceInterval buyAmounts, int maxUses, int xp)
		{
			this.getBuyingItem = item;
			this.buyAmounts = buyAmounts;
			this.maxUses = maxUses;
			this.xp = xp;
		}

		public EmeraldForItems(@Nonnull ItemLike item, @Nonnull PriceInterval buyAmounts, int maxUses, int xp)
		{
			this(l -> new ItemStack(item), buyAmounts, maxUses, xp);
		}

/*		public EmeraldForItems(@Nonnull TagKey<Item> tag, @Nonnull PriceInterval buyAmounts, int maxUses, int xp)
		{
			this(l -> l != null ? IEApi.getPreferredTagStack(l.registryAccess(), tag): ItemStack.EMPTY,
					buyAmounts, maxUses, xp);
		}


		public EmeraldForItems(@Nonnull ResourceLocation tag, @Nonnull PriceInterval buyAmounts, int maxUses, int xp)
		{
			this(TagKey.create(Registries.ITEM, tag), buyAmounts, maxUses, xp);
		}
*/	

		@Nullable
		@Override
		public MerchantOffer getOffer(@Nullable Entity trader, RandomSource rand)
		{
			if(buyingItem==null)
				if(trader==null)
					this.buyingItem = Objects.requireNonNull(this.getBuyingItem.apply(null));
				else
					this.buyingItem = Objects.requireNonNull(this.getBuyingItem.apply(trader.level));
			return new MerchantOffer(
					ItemHandlerHelper.copyStackWithSize(this.buyingItem, this.buyAmounts.getPrice(rand)),
					new ItemStack(Items.EMERALD),
					//TODO adjust values for individual trades
					maxUses, xp, 0.05f);
		}
	}

	
	
	private static class ItemsForEmerald implements ItemListing
	{
		public ItemStack sellingItem;
		public PriceInterval priceInfo;
		final int maxUses;
		final int xp;
		final float priceMult;

		public ItemsForEmerald(ItemLike par1Item, PriceInterval priceInfo, int maxUses, int xp)
		{
			this(new ItemStack(par1Item), priceInfo, maxUses, xp);
		}

		public ItemsForEmerald(ItemStack par1Item, PriceInterval priceInfo, int maxUses, int xp)
		{
			this(par1Item, priceInfo, maxUses, xp, 0.05f);
		}

		public ItemsForEmerald(ItemLike par1Item, PriceInterval priceInfo, int maxUses, int xp, float priceMult)
		{
			this(new ItemStack(par1Item), priceInfo, maxUses, xp, priceMult);
		}

		public ItemsForEmerald(ItemStack par1Item, PriceInterval priceInfo, int maxUses, int xp, float priceMult)
		{
			this.sellingItem = par1Item;
			this.priceInfo = priceInfo;
			this.maxUses = maxUses;
			this.xp = xp;
			this.priceMult = priceMult;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand)
		{
			int i = 1;
			if(this.priceInfo!=null)
				i = this.priceInfo.getPrice(rand);
			ItemStack buying;
			ItemStack selling;
			if(i < 0)
			{
				buying = new ItemStack(Items.EMERALD);
				selling = ItemHandlerHelper.copyStackWithSize(sellingItem, -i);
			}
			else
			{
				buying = new ItemStack(Items.EMERALD, i);
				selling = sellingItem;
			}
			//TODO customize values
			return new MerchantOffer(buying, selling, maxUses, xp, priceMult);
		}
	}

	
	private static class PriceInterval
	{
		private final int min;
		private final int max;

		private PriceInterval(int min, int max)
		{
			this.min = min;
			this.max = max;
		}

		int getPrice(RandomSource rand)
		{
			return min >= max?min: min+rand.nextInt(max-min+1);
		}
	}
	
}

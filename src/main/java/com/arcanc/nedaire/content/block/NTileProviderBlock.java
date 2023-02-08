/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import java.util.Optional;
import java.util.function.BiFunction;

import javax.annotation.Nullable;

import com.arcanc.nedaire.content.block.BlockInterfaces.IInteractionObjectN;
import com.arcanc.nedaire.content.block.entities.NBaseBlockEntity;
import com.arcanc.nedaire.content.block.entities.ticker.ModClientTickerBlockEntity;
import com.arcanc.nedaire.content.block.entities.ticker.ModServerTickerBlockEntity;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;

public class NTileProviderBlock<T extends BlockEntity> extends NBaseBlock implements EntityBlock 
{
	private final BiFunction<BlockPos, BlockState, T> tile;
	private BlockEntityClassData classData;
	
	public NTileProviderBlock(Properties properties, BiFunction<BlockPos, BlockState, T> tile) 
	{
		super(properties);
		this.tile = tile;
	}
	
	public NTileProviderBlock(Properties props, RegistryObject<BlockEntityType<T>> type)
	{
		this(props, (bp, state) -> type.get().create(bp, state));
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) 
	{
		Optional<BlockEntity> tile = BlockHelper.castTileEntity(level, pos, BlockEntity.class);
			
		tile.map(t -> 
		{
			if(t instanceof MenuProvider prov && hand == InteractionHand.MAIN_HAND && !player.isShiftKeyDown())
			{
				if(player instanceof ServerPlayer serverPlayer)
				{
					if(prov instanceof IInteractionObjectN<?> interaction)
					{
						interaction = interaction.getBE();
						if(interaction != null && interaction.canUseGui(player))
						{
							NetworkHooks.openScreen(serverPlayer, interaction, pos);
						}
					}
					else
						NetworkHooks.openScreen(serverPlayer, prov, pos);
				}
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		});
		return InteractionResult.sidedSuccess(level.isClientSide());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean update) 
	{
		if (!level.isClientSide && !oldState.is(newState.getBlock())) 
		{
			BlockHelper.castTileEntity(BlockHelper.getTileEntity(level, pos), NBaseBlockEntity.class).ifPresent(ent -> 
			{
				ItemHelper.dropContents(level, pos, ItemHelper.getItemHandler(ent));			
			});
		super.onRemove(oldState, level, pos, newState, update);
		}
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) 
	{
		return tile.apply(pos, state);
	}
	
	@Override
	public <T2 extends BlockEntity> BlockEntityTicker<T2> getTicker(Level level, BlockState state, BlockEntityType<T2> type) 
	{
		BlockEntityTicker<T2> baseTicker = getClassData().makeBaseTicker(level.isClientSide);
		
		return baseTicker;
	}
	
	@Override
	protected BlockState getInitDefaultState() 
	{
		return super.getInitDefaultState();
	}
	
	private BlockEntityClassData getClassData()
	{
		if (this.classData == null)
		{
			T tempBE = tile.apply(BlockPos.ZERO, getInitDefaultState());
			this.classData = new BlockEntityClassData 
					(
					tempBE instanceof ModServerTickerBlockEntity,
					tempBE instanceof ModClientTickerBlockEntity
					);
					
		}
		return this.classData;
	}
	
	private record BlockEntityClassData (boolean serverTicking, boolean clientTicking)
	{
		@Nullable
		public <T extends BlockEntity> BlockEntityTicker<T> makeBaseTicker(boolean isClient)
		{
			if(serverTicking && !isClient)
				return ModServerTickerBlockEntity.makeTicker();
			else if(clientTicking && isClient)
				return ModClientTickerBlockEntity.makeTicker();
			else
				return null;
		}
	}

}

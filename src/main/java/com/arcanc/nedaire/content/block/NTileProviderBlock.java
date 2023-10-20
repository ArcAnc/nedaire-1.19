/**
 * @author ArcAnc
 * Created at: 2022-04-09
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.BlockInterfaces.INInteractionObject;
import com.arcanc.nedaire.content.block.entities.ticker.NClientTickerBlockEntity;
import com.arcanc.nedaire.content.block.entities.ticker.NServerTickerBlockEntity;
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
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiFunction;

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
	
	@SuppressWarnings("deprecation")
	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult)
	{
		Optional<BlockEntity> tile = BlockHelper.getTileEntity(level, pos);
			
		return tile.map(t -> 
		{
			if(t instanceof MenuProvider prov && hand == InteractionHand.MAIN_HAND && !player.isShiftKeyDown())
			{
				if(player instanceof ServerPlayer serverPlayer)
				{
					if(prov instanceof INInteractionObject<?> interaction)
					{
						interaction = interaction.getBE();
						if(interaction != null && interaction.canUseGui(player))
						{
							serverPlayer.openMenu(interaction, pos);
						}
					}
					else
						serverPlayer.openMenu(prov, pos);
				}
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		}).orElse(super.use(state, level, pos, player, hand, hitResult));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(@NotNull BlockState oldState, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean update)
	{
		if (!level.isClientSide && !oldState.is(newState.getBlock())) 
		{
			BlockHelper.getTileEntity(level, pos).ifPresent(ent -> 
			{
				ItemHelper.dropContents(level, pos, ItemHelper.getItemHandler(ent));			
			});
		super.onRemove(oldState, level, pos, newState, update);
		}
	}
	
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
	{
		return tile.apply(pos, state);
	}
	
	@Override
	public <T2 extends BlockEntity> BlockEntityTicker<T2> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T2> type)
	{
		BlockEntityTicker<T2> baseTicker = getClassData().makeBaseTicker(level.isClientSide());
		
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
					tempBE instanceof NServerTickerBlockEntity,
					tempBE instanceof NClientTickerBlockEntity
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
				return NServerTickerBlockEntity.makeTicker();
			else if(clientTicking && isClient)
				return NClientTickerBlockEntity.makeTicker();
			else
				return null;
		}
	}

}

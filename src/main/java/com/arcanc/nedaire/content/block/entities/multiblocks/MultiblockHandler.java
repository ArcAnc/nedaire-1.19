/**
 * @author ArcAnc
 * Created at: 21.07.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block.entities.multiblocks;

import com.arcanc.nedaire.content.registration.NRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class MultiblockHandler
{

    //TODO: прописать код получения. Сделать регистрацию их где нибудь. Хоть свой кастомный реестр, хоть форджовский. И сделать сохранение и загрузку из джсон файлов.
    @Nullable
    public static INMultiblock getByName(@Nullable ResourceLocation rl)
    {
        if(rl == null)
            return null;
        return NRegistration.RegisterMultiblocks.MULTIBLOCKS_BUILTIN.get().getValue(rl);
    }



    public static MultiblockFormEvent postMultiblockFormationEvent(Player player, INMultiblock multiblock, BlockPos clickedBlock, ItemStack hammer)
    {
        MultiblockFormEvent event = new MultiblockFormEvent(player, multiblock, clickedBlock, hammer);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    /**
     * This event is fired BEFORE the multiblock is attempted to be formed.<br>
     * No checks of the structure have been made. The event simply exists to cancel the formation of the multiblock before it ever happens.
     */
    @Cancelable
    public static class MultiblockFormEvent extends PlayerEvent
    {
        private final INMultiblock multiblock;
        private final BlockPos clickedBlock;
        private final ItemStack hammer;

        public MultiblockFormEvent(Player player, INMultiblock multiblock, BlockPos clickedBlock, ItemStack hammer)
        {
            super(player);
            this.multiblock = multiblock;
            this.clickedBlock = clickedBlock;
            this.hammer = hammer;
        }

        public INMultiblock getMultiblock()
        {
            return multiblock;
        }

        public BlockPos getClickedBlock()
        {
            return clickedBlock;
        }

        public ItemStack getHammer()
        {
            return hammer;
        }
    }
}

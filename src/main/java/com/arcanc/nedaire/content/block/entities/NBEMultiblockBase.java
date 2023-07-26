/**
 * @author ArcAnc
 * Created at: 23.07.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.block.entities.multiblocks.INMultiblock;
import com.arcanc.nedaire.content.block.entities.multiblocks.MultiblockHandler;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NBEMultiblockBase extends NBaseBlockEntity
{

    private final INMultiblock multiblock;
    private boolean isFormed = false;
    private Direction rotation = Direction.SOUTH;

    public NBEMultiblockBase(NRegistration.RegisterBlockEntities.MultiblockBEType<?, ?> type, BlockPos pos, BlockState state)
    {
        super(type.blockEntityType().get(), pos, state);
        multiblock = type.multiblock().get();
    }

    @Override
    public void readCustomTag(CompoundTag tag, boolean descPacket)
    {
        if(tag.contains(NDatabase.Multiblocks.IS_FORMED))
        {
            this.isFormed = tag.getBoolean(NDatabase.Multiblocks.IS_FORMED);
        }
        if (tag.contains(NDatabase.Multiblocks.ROTATION))
        {
            this.rotation = Direction.from3DDataValue(tag.getInt(NDatabase.Multiblocks.ROTATION));
        }
    }

    @Override
    public void writeCustomTag(CompoundTag tag, boolean descPacket)
    {
        tag.putBoolean(NDatabase.Multiblocks.IS_FORMED, isFormed);
        tag.putInt(NDatabase.Multiblocks.ROTATION, rotation.get3DDataValue());
    }
}

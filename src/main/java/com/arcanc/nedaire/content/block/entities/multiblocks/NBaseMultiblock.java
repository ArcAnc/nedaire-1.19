/**
 * @author ArcAnc
 * Created at: 24.07.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block.entities.multiblocks;

import com.arcanc.nedaire.content.block.entities.NBEMultiblockBase;
import com.arcanc.nedaire.data.multiblocks.writer.NMultiblockWriter;

import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.DirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NBaseMultiblock implements INMultiblock<NBaseMultiblock>
{

    private final ResourceLocation location;
    private final Map<BlockPos, NMultiblockWriter.StructureInfo> infoList;
    private final Vec3i size;
    private final BlockPos triggerPos;

    public NBaseMultiblock(@NotNull ResourceLocation location, @NotNull List<NMultiblockWriter.StructureInfo> infoList)
    {
        this.location = location;

        this.infoList = new HashMap<>();

        for (NMultiblockWriter.StructureInfo info : infoList)
        {
            this.infoList.putIfAbsent(info.pos(), info);
        }

        int[] size = new int[]{
                /*startX*/ 0,
                /*startY*/ 0,
                /*startZ*/ 0,
                /*finishX*/0,
                /*finishY*/0,
                /*finishZ*/0};

        BlockPos trgPos = new BlockPos(0,0,0);
        for (NMultiblockWriter.StructureInfo info : this.infoList.values())
        {
            BlockPos pos = info.pos();
            if (size[0] > pos.getX())
                size[0] = pos.getX();
            if (size[3] < pos.getX())
                size[3] = pos.getX();
            if (size[1] > pos.getY())
                size[1] = pos.getY();
            if (size[4] > pos.getY())
                size[4] = pos.getY();
            if (size[2] > pos.getZ())
                size[2] = pos.getZ();
            if (size[5] > pos.getZ())
                size[5] = pos.getZ();

            if (info.isTrigger())
                trgPos = pos;
        }

        this.triggerPos = trgPos;
        this.size = new Vec3i(size[3]-size[0], size[4]-size[1], size[5]-size[2]);
    }

    @Override
    public boolean createStructure(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction side, @NotNull Player player)
    {
        Rotation rot = DirectionHelper.getRotationBetweenFacings(Direction.SOUTH, side.getOpposite());

        if(rot==null)
            return false;

        BlockState state = level.getBlockState(pos);

        if (isBlockTrigger(state, side, level))
        {
            return checkAllBlocks(level, pos, side, rot);
        }
        return false;
    }

    private boolean checkAllBlocks(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction side, @NotNull Rotation rot)
    {
        boolean canForm = true;
        for (Map.Entry<BlockPos, NMultiblockWriter.StructureInfo> entry : infoList.entrySet())
        {
            BlockPos inWorldPos = entry.getKey().rotate(rot).offset(pos);
            BlockState worldState = level.getBlockState(inWorldPos);
            BlockState infoState = entry.getValue().state();

            if (BlockHelper.isRotatable(infoState))
            {
                infoState = BlockHelper.rotateBlockState(infoState, rot);
            }

            if (!BlockMatcher.matches(infoState, worldState, null, null))
            {
                canForm = false;
            }
        }

        if (canForm && !level.isClientSide())
            form (level, pos, side, rot);

        return canForm;
    }

    protected void form(Level level, BlockPos pos, Direction side, Rotation rot)
    {
        BlockHelper.castTileEntity(level, pos, NBEMultiblockBase.class).ifPresent(master ->
        {
            for (Map.Entry<BlockPos, NMultiblockWriter.StructureInfo> entry : infoList.entrySet())
            {
                BlockPos inWorldPos = entry.getKey().rotate(rot).offset(pos);

                BlockHelper.castTileEntity(level, inWorldPos, NBEMultiblockBase.class).ifPresent(tile ->
                {
                    if (entry.getValue().isTrigger())
                    {
                        tile.setIsMaster(true);
                        tile.setMaster(tile);
                    }
                    else
                    {
                        tile.setIsMaster(false);
                        tile.setMaster(master);
                    }
                    tile.setRotation(side);
                    tile.setFormed(true);
                    tile.setChanged();
                });
            }
        });
    }
    @Override
    public void disassemble(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction facing)
    {
        BlockHelper.castTileEntity(level, pos, NBEMultiblockBase.class).ifPresent(master ->
        {
            for (Map.Entry<BlockPos, NMultiblockWriter.StructureInfo> entry : infoList.entrySet())
            {
                Rotation rot = DirectionHelper.getRotationBetweenFacings(Direction.SOUTH, facing);
                BlockPos inWorldPos = entry.getKey().rotate(rot).offset(pos);

                BlockHelper.castTileEntity(level, inWorldPos, NBEMultiblockBase.class).ifPresent(tile ->
                {
                    tile.setIsMaster(false);
                    tile.setMaster(null);
                    tile.setRotation(Direction.SOUTH);
                    tile.setFormed(false);
                    tile.setChanged();
                });
            }
        });
    }

    @Override
    public Collection<NMultiblockWriter.StructureInfo> getStructure(@Nullable Level level)
    {
        return this.infoList.values();
    }

    public Map<BlockPos, NMultiblockWriter.StructureInfo> getInfoList()
    {
        return infoList;
    }

    @Override
    public Vec3i getSize(@Nullable Level level)
    {
        return this.size;
    }

    @Override
    public boolean isBlockTrigger(@NotNull BlockState state, @NotNull Direction side, @Nullable Level level)
    {
        BlockState triggerState = infoList.get(triggerPos).state();

        if (BlockHelper.isRotatable(state))
        {
            Rotation rot = DirectionHelper.getRotationBetweenFacings(Direction.SOUTH, side);
            triggerState = BlockHelper.rotateBlockState(triggerState, rot);
        }
        return BlockMatcher.matches(triggerState, state, null, null);
    }

    @Override
    public BlockPos getTriggerOffset()
    {
        return this.triggerPos;
    }

    @Override
    public @NotNull ResourceLocation getRegistryName()
    {
        return this.location;
    }
}

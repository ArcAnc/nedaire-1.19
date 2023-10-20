/**
 * @author ArcAnc
 * Created at: 17.09.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEMultiblockBase;
import com.arcanc.nedaire.content.block.entities.multiblocks.NBaseMultiblock;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.DirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class NBlockMultiblockPart<T extends NBEMultiblockBase> extends NTileProviderBlock<T>
{
    private final Supplier<NBaseMultiblock> multiblock;

    public NBlockMultiblockPart(Properties props, NRegistration.RegisterBlockEntities.MultiblockBEType<T> type)
    {
        super(props, type.blockEntityType());
        this.multiblock = () -> NRegistration.RegisterMultiblocks.MANAGER.byKey(type.multiblockLocation()).get();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context)
    {
        return BlockHelper.castTileEntity(level, pos, NBEMultiblockBase.class).map(tile ->
        {
            if (tile.isFormed())
            {
                BlockPos masterPos = tile.getMaster().getBlockPos();
                Rotation rot = DirectionHelper.getRotationBetweenFacings(tile.getRotation(), Direction.SOUTH);
                BlockPos difference = pos.subtract(masterPos).rotate(rot);
                return multiblock.get().getInfoList().get(difference).shape();
            }
            return Shapes.block();
        }).orElse(Shapes.block());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context)
    {
        return BlockHelper.castTileEntity(level, pos, NBEMultiblockBase.class).map(tile ->
        {
            if (tile.isFormed())
            {
                BlockPos masterPos = tile.getMaster().getBlockPos();
                Rotation rot = DirectionHelper.getRotationBetweenFacings(tile.getRotation(), Direction.SOUTH);
                BlockPos difference = pos.subtract(masterPos).rotate(rot);
                return multiblock.get().getInfoList().get(difference).shape();
            }
            return Shapes.block();
        }).orElse(Shapes.block());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos)
    {
        return BlockHelper.castTileEntity(level, pos, NBEMultiblockBase.class).map(tile ->
        {
            if (tile.isFormed())
            {
                BlockPos masterPos = tile.getMaster().getBlockPos();
                Rotation rot = DirectionHelper.getRotationBetweenFacings(tile.getRotation(), Direction.SOUTH);
                BlockPos difference = pos.subtract(masterPos).rotate(rot);
                return multiblock.get().getInfoList().get(difference).shape();
            }
            return Shapes.block();
        }).orElse(Shapes.block());
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor)
    {
        BlockHelper.castTileEntity(level, pos, NBEMultiblockBase.class).ifPresent(tile ->
        {
            if (tile.isFormed())
            {
                BlockPos masterPos = tile.getMaster().getBlockPos();
                BlockHelper.castTileEntity(level, masterPos, NBEMultiblockBase.class).ifPresent( master ->
                {
                    master.reCheckStructure();
                });
            }
        });
        super.onNeighborChange(state, level, pos, neighbor);
    }
}

/**
 * @author ArcAnc
 * Created at: 11.04.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEPlatform;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class NBlockPlatform extends NTileProviderBlock<NBEPlatform>
{
    private static final VoxelShape SHAPE = Shapes.or(
            box(0, 0, 0, 16, 1, 16),
            box(0, 15, 0, 16, 16, 16),
            box(1, 1, 11, 5, 15 ,15),
            box(11, 1, 11, 15, 15, 15),
            box(11, 1, 1, 15, 15, 5),
            box(1, 1,1, 5, 15, 5),
            box(6, 1, 6, 10, 15, 10)
    );

    public NBlockPlatform(Properties properties)
    {
        super(properties, NRegistration.RegisterBlockEntities.BE_PLATFORM);
    }
    @Override
    protected BlockState getInitDefaultState()
    {
        BlockState state = super.getInitDefaultState();
        if (state.hasProperty(BlockHelper.BlockProperties.HORIZONTAL_FACING))
        {
            state = state.setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, Direction.SOUTH);
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context)
    {
        return super.getStateForPlacement(context).setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot)
    {
        return state.setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, rot.rotate(state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(@NotNull BlockState oldState, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean update)
    {
        if (!level.isClientSide() && !oldState.is(newState.getBlock()))
        {
            for (Direction dir : Direction.values())
            {
                if (dir.getAxis() == Direction.Axis.Y)
                {
                    BlockPos attach = pos.relative(dir);
                    BlockHelper.castTileEntity(level, attach, NBEPlatform.Attachable.class).ifPresent(tile ->
                    {
                        level.destroyBlock(attach, true, null, 512);
                    });
                }
            }
            super.onRemove(oldState, level, pos, newState, update);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(BlockHelper.BlockProperties.HORIZONTAL_FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context)
    {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context)
    {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos)
    {
        return SHAPE;
    }
}

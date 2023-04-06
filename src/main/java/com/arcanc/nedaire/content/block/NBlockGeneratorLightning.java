/**
 * @author ArcAnc
 * Created at: 06.04.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEGeneratorLightning;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class NBlockGeneratorLightning extends NTileProviderBlock<NBEGeneratorLightning>
{
    private static final VoxelShape SHAPE = Shapes.block();
    public NBlockGeneratorLightning(Properties properties)
    {
        super(properties, NRegistration.RegisterBlockEntities.BE_GENERATOR_LIGHTNING);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(BlockHelper.BlockProperties.LIT);
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos)
    {
        return SHAPE;
    }
}

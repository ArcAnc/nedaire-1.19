/**
 * @author ArcAnc
 * Created at: 03.07.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block.entities.multiblocks;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BlockMatcher
{
    private static final List<PreProcessor> PREPROCESSORS = new ArrayList<>();

    public static final PreProcessor CHECK_AIR = (expected, found, level, pos) ->
    {
        if(expected.isAir() && !found.isAir())
            found = expected;
        return found;
    };

    public static final PreProcessor WATERLOGGING = (expected, found, level, pos) ->
    {
        if (expected.hasProperty(BlockHelper.BlockProperties.WATERLOGGED) && found.hasProperty(BlockHelper.BlockProperties.WATERLOGGED)
                && !expected.getValue(BlockHelper.BlockProperties.WATERLOGGED) && found.getValue(BlockHelper.BlockProperties.WATERLOGGED))
            return found.setValue(BlockHelper.BlockProperties.WATERLOGGED, false);
        else
            return found;
    };

    private static final MatcherPredicate BASIC = (expected, found, level, pos) -> expected.equals(found);

    public static boolean matches(BlockState expected, BlockState found, Level world, BlockPos pos)
    {
        return matches(expected, found, world, pos, ImmutableList.of());
    }

    public static boolean matches(BlockState expected, BlockState found, Level world, BlockPos pos,
                                 List<MatcherPredicate> additional)
    {
        for(PreProcessor p : PREPROCESSORS)
            found = p.preprocessFoundState(expected, found, world, pos);
        BlockState finalFound = found;
        return Stream.concat(Stream.of(BASIC), additional.stream())
                .map(predicate -> predicate.matches(expected, finalFound, world, pos))
                .reduce(true, (accumulator, elem) -> elem && accumulator);
    }

    public static void registerPreProcessor(PreProcessor preprocessor)
    {
        Preconditions.checkNotNull(preprocessor);
        PREPROCESSORS.add(preprocessor);
    }

    public interface MatcherPredicate
    {
        boolean matches(BlockState expected, BlockState found, @Nullable Level level, @Nullable BlockPos pos);
    }
    public interface PreProcessor
    {
        BlockState preprocessFoundState (BlockState expected, BlockState found, @Nullable Level level, @Nullable BlockPos pos);
    }
}

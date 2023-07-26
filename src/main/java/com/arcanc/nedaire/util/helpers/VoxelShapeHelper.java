/**
 * @author ArcAnc
 * Created at: 26.07.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.util.helpers;


import com.arcanc.nedaire.Nedaire;
import com.google.common.base.Function;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class VoxelShapeHelper
{

    private static final PrimitiveCodec<DoubleStream> DOUBLE_STREAM = new PrimitiveCodec<>()
    {
        @Override
        public <T> DataResult<DoubleStream> read(DynamicOps<T> ops, T input)
        {
            return ops.getStream(input).flatMap(stream ->
            {
                final List<T> list = stream.collect(Collectors.toList());
                if (list.stream().allMatch(element -> ops.getNumberValue(element).result().isPresent()))
                {
                    return DataResult.success(list.stream().mapToDouble(element -> ops.getNumberValue(element).result().get().doubleValue()));
                }
                return DataResult.error(() -> "Some elements are not ints: " + input);
            });
        }

        @Override
        public <T> T write(DynamicOps<T> ops, DoubleStream value)
        {
            return ops.createList(value.mapToObj(ops :: createDouble));
        }

        @Override
        public String toString()
        {
            return "DoubleStream";
        }
    };

    public static final Codec<VoxelShape> CODEC = DOUBLE_STREAM.comapFlatMap((stream) ->
    {
        double[] values = stream.toArray();

        if (values.length % 6 != 0)
            return DataResult.error(() -> "Wrong stream size");


        List<VoxelShape> shapes = new ArrayList<>();

        for (int q = 0; q < values.length; q++)
        {
            if (q % 6 == 0)
            {
               shapes.add(Shapes.create(values[q], values[q+1], values[q+2], values[q+3], values[q+4], values[q+5]));
            }
        }

        VoxelShape resultShape = Shapes.empty();

        for (int q = 0; q < shapes.size() - 1; q++)
        {
            VoxelShape shape = shapes.get(q);
            Shapes.or(resultShape, shape);
        }
        return DataResult.success(resultShape.optimize());
    }, (shape) ->
    {
        List<Double> list = new LinkedList<>();

        MutableInt i = new MutableInt(0);

        shape.forAllBoxes((startX, startY, startZ, finishX, finishY, finishZ) ->
        {
            list.add(startX);
            list.add(startY);
            list.add(startZ);
            list.add(finishX);
            list.add(finishY);
            list.add(finishZ);
        });

        return list.stream().mapToDouble(d -> d);
    }).stable();

}

/**
 * @author ArcAnc
 * Created at: 12.09.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.data;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.multiblocks.MultiblockProvider;
import com.arcanc.nedaire.data.multiblocks.writer.INMultiblockWriter;
import com.arcanc.nedaire.data.multiblocks.writer.NMultiblockWriter;
import com.arcanc.nedaire.data.multiblocks.writer.TemplateMultiblockWriter;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class NMultiblockProvider extends MultiblockProvider
{
    public NMultiblockProvider(PackOutput output)
    {
        super(output);
    }

    @Override
    protected void buildMultiblocks(@NotNull Consumer<INMultiblockWriter<TemplateMultiblockWriter>> consumer)
    {
/*       newWriter().
                newInfoBuilder().
                    setPos(new BlockPos(0,0,0)).
                    setShape(Shapes.block()).
                    setTag(new CompoundTag()).
                    setTrigger(true).
                    setState(NRegistration.RegisterBlocks.test_block.getDefaultBlockState()).
                build().
                newInfoBuilder().
                    setPos(new BlockPos(0, 1, 0)).
                    setShape(Shapes.block()).
                    setTag(new CompoundTag()).
                    setTrigger(false).
                    setState(NRegistration.RegisterBlocks.test_block.getDefaultBlockState()).
                build().
        build(consumer, StringHelper.getLocFStr("test"));
*/    }

    public String getName()
    {
        return "Nedaire Multiblock Provider";
    }
}

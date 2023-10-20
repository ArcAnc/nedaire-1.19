/**
 * @author ArcAnc
 * Created at: 12.09.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.data.multiblocks;

import com.arcanc.nedaire.content.block.entities.multiblocks.INMultiblock;
import com.arcanc.nedaire.data.multiblocks.writer.INMultiblockWriter;
import com.arcanc.nedaire.data.multiblocks.writer.NMultiblockWriter;
import com.arcanc.nedaire.data.multiblocks.writer.TemplateMultiblockWriter;
import com.google.common.collect.Sets;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class MultiblockProvider implements DataProvider
{

    protected final PackOutput.PathProvider pathProvider;

    public MultiblockProvider(PackOutput output)
    {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "multiblocks");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> list = Lists.newArrayList();

        this.buildMultiblocks(multiblock ->
        {
            if (!set.add(multiblock.getRegistryName()))
            {
                throw new IllegalStateException("Duplicate multiblock " + multiblock.getRegistryName());
            }
            else
            {
                list.add(DataProvider.saveStable(output, multiblock.serializeMultiblock(), this.pathProvider.json(multiblock.getRegistryName())));
            }
        });

        return CompletableFuture.allOf(list.toArray(index ->
                new CompletableFuture[index]));
    }

    protected abstract void buildMultiblocks(@NotNull Consumer<INMultiblockWriter<TemplateMultiblockWriter>> consumer);

    protected TemplateMultiblockWriter newWriter()
    {
        return new TemplateMultiblockWriter();
    }

    @Override
    public String getName()
    {
        return "Multiblocks";
    }
}

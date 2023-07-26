/**
 * @author ArcAnc
 * Created at: 26.07.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block.entities.multiblocks;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.util.database.NDatabase;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

public class NMultiblockSerializer<T extends NMultiblockSerializer<T>> implements INMultiblockSerializer
{
    private ResourceLocation registryName;
    private final List<Consumer<JsonObject>> writerFunctions;

    protected JsonArray infoArray = null;

    public NMultiblockSerializer()
    {
        this.writerFunctions = new ArrayList<>();
        this.infoArray = new JsonArray();
    }

    protected boolean isComplete ()
    {
        return true;
    }

    public void build(Consumer<INMultiblockSerializer> out, ResourceLocation registryName)
    {
        Preconditions.checkArgument(isComplete(), "This recipe is incomplete");
        this.registryName = registryName;
        out.accept(this);
    }

    @SuppressWarnings("unchecked")
    public T addWriter(Consumer<JsonObject> writer)
    {
        Preconditions.checkArgument(registryName == null, "This recipe has already been finalized");
        this.writerFunctions.add(writer);
        return (T)this;
    }

    /* ===============     Common Objects     =============== */

    public T addInfo()
    {
        return addWriter(jsonObject -> {});
    }

    /* =============== INMultiblockSerializer =============== */

    @Override
    public void serializeMultiblockData(@NotNull JsonObject obj)
    {
        for(Consumer<JsonObject> writer : this.writerFunctions)
            writer.accept(obj);
    }

    @Override
    public @NotNull ResourceLocation getRegistryName()
    {
        return registryName;
    }

    public record StructureInfo(BlockPos pos, BlockState state, @Nullable CompoundTag tag, VoxelShape shape, boolean isTrigger)
    {

        public StructureInfo(BlockPos pos, BlockState state, @Nullable CompoundTag tag, VoxelShape shape)
        {
            this(pos, state, tag, shape, false);
        }

        @NotNull
        public static JsonObject writeBlockPos(@NotNull BlockPos pos, @NotNull JsonObject obj)
        {
            BlockPos.CODEC.encodeStart(JsonOps.INSTANCE, pos).result().ifPresentOrElse
                    (
                        saved -> obj.add(NDatabase.Multiblocks.Serializing.POSITION, saved),
                        () ->
                        {
                            throw new NoSuchElementException("BlockPos is null");
                        }
                    );
            return obj;
        }

        @NotNull
        public static JsonObject writeBlockState(@NotNull BlockState state, @NotNull JsonObject obj)
        {
            BlockState.CODEC.encodeStart(JsonOps.INSTANCE, state).result().ifPresentOrElse
                    (
                            saved -> obj.add(NDatabase.Multiblocks.Serializing.STATE, saved),
                            () ->
                            {
                                throw new NoSuchElementException("BlockState is null");
                            }
                    );
            return obj;
        }
        public static JsonObject writeTag(@NotNull CompoundTag tag, @NotNull JsonObject obj)
        {
            CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, tag).result().ifPresentOrElse
                    (
                            saved -> obj.add(NDatabase.Multiblocks.Serializing.TAG, saved),
                            () ->
                            {
                                throw new NoSuchElementException("Tag is null");
                            }
                    );
            return obj;
        }


    }

    @SuppressWarnings("unchecked")
    public T getSelf()
    {
        return (T)this;
    }

    public InfoBuilder newInfoBuilder()
    {
        return new InfoBuilder();
    }

    private class InfoBuilder
    {
        private BlockPos pos;
        private BlockState state;
        private CompoundTag tag;
        private VoxelShape shape;
        private boolean isTrigger;

        @NotNull
        public InfoBuilder setPos(@NotNull BlockPos pos)
        {
            this.pos = pos;
            return this;
        }

        @NotNull
        public InfoBuilder setState(@NotNull BlockState state)
        {
            this.state = state;
            return this;
        }

        @NotNull
        public InfoBuilder setTag(@NotNull CompoundTag tag)
        {
            this.tag = tag;
            return this;
        }

        @NotNull
        public InfoBuilder setShape(@NotNull VoxelShape shape)
        {
            this.shape = shape;
            return this;
        }

        @NotNull
        public InfoBuilder setTrigger(boolean trigger)
        {
            isTrigger = trigger;
            return this;
        }

        public T build()
        {
            StructureInfo info = new StructureInfo(pos, state, tag, shape, isTrigger);



            return getSelf();
        }
    }
}

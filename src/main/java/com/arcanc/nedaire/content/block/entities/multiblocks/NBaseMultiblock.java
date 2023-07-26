/**
 * @author ArcAnc
 * Created at: 24.07.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block.entities.multiblocks;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class NBaseMultiblock implements INMultiblock<NBaseMultiblock>
{

    @Override
    public boolean createStructure(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction side, @NotNull Player player)
    {
        return false;
    }

    @Override
    public List<NMultiblockSerializer.StructureInfo> getStructure(@Nullable Level level)
    {
        return null;
    }

    @Override
    public Vec3i getSize(@Nullable Level level)
    {
        return null;
    }

    @Override
    public void disassemble(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction clickDirectionAtCreation)
    {

    }

    @Override
    public BlockPos getTriggerOffset()
    {
        return null;
    }

    @Override
    public NBaseMultiblock fromJson(ResourceLocation loc, JsonObject jObj)
    {
        return null;
    }

    @Nullable
    @Override
    public NBaseMultiblock fromNetwork(ResourceLocation loc, FriendlyByteBuf buf)
    {
        return null;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, NBaseMultiblock multiblock)
    {

    }

    public static class BaseMultiblockBuilder<T extends BaseMultiblockBuilder<T>>
    {
        private ResourceLocation loc;
        private final List<Consumer<JsonObject>> writerFunctions;


        public BaseMultiblockBuilder()
        {
            this.writerFunctions = new ArrayList<>();
        }

        protected boolean isComplete()
        {
            return true;
        }

        public void build(Consumer<BaseMultiblockBuilder> out, ResourceLocation loc)
        {
            Preconditions.checkArgument(isComplete(), "This Multiblock is incomplete");
            this.loc = loc;
            out.accept(this);
        }

        public T addWriter(Consumer<JsonObject> writer)
        {
            Preconditions.checkArgument(loc==null, "This recipe has already been finalized");
            this.writerFunctions.add(writer);
            return (T)this;
        }
    }



}

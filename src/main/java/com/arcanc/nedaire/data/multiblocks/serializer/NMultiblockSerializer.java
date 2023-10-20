/**
 * @author ArcAnc
 * Created at: 29.07.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.data.multiblocks.serializer;

import com.arcanc.nedaire.content.block.entities.multiblocks.INMultiblock;
import com.arcanc.nedaire.content.block.entities.multiblocks.NBaseMultiblock;
import com.arcanc.nedaire.data.multiblocks.writer.NMultiblockWriter;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.VoxelShapeHelper;
import com.google.gson.JsonArray;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NMultiblockSerializer implements INMultiblockSerializer<NBaseMultiblock>
{

    @Override
    public NBaseMultiblock fromJson(ResourceLocation location, JsonObject object)
    {
        JsonArray array = object.getAsJsonArray(NDatabase.Multiblocks.Serializing.INFO_ARRAY);

        List<NMultiblockWriter.StructureInfo> infoList = new ArrayList<>();

        for (int q = 0; q < array.size(); q++)
        {
            JsonObject obj = array.get(q).getAsJsonObject();
            BlockPos pos = readBlockPos(obj);
            BlockState state = readBlockState(obj);
            CompoundTag tag = readTag(obj);
            VoxelShape shape = readVoxelShape(obj);
            boolean isTrigger = readIsTrigger(obj);

            infoList.add(new NMultiblockWriter.StructureInfo(pos, state, tag, shape, isTrigger));
        }

        NBaseMultiblock multiblock = new NBaseMultiblock(location, infoList);

        return multiblock;
    }

    @Nullable
    @Override
    public NBaseMultiblock fromNetwork(ResourceLocation location, FriendlyByteBuf buf)
    {
        List<NMultiblockWriter.StructureInfo> infoList = new ArrayList<>();

        ResourceLocation name = buf.readResourceLocation();
        int size = buf.readInt();
        for (int q = 0; q < size; q++)
        {
            BlockPos pos = buf.readBlockPos();
            BlockState state = buf.readWithCodec(NbtOps.INSTANCE, BlockState.CODEC, NbtAccounter.unlimitedHeap());
            CompoundTag tag = buf.readNbt();
            VoxelShape shape = buf.readJsonWithCodec(VoxelShapeHelper.CODEC);
            boolean isTrigger = buf.readBoolean();

            infoList.add(new NMultiblockWriter.StructureInfo(pos, state, tag, shape, isTrigger));
        }

        return new NBaseMultiblock(name, infoList);
    }

    public NBaseMultiblock fromNetwork(FriendlyByteBuf buf)
    {
        return fromNetwork(new ResourceLocation(""), buf);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, NBaseMultiblock multiblock)
    {
        Collection<NMultiblockWriter.StructureInfo> infoList = multiblock.getStructure(null);

        buf.writeResourceLocation(multiblock.getRegistryName());
        buf.writeInt(infoList.size());

        for (NMultiblockWriter.StructureInfo entry : infoList)
        {
            BlockPos pos = entry.pos();
            BlockState state = entry.state();
            CompoundTag tag = entry.tag();
            VoxelShape shape = entry.shape();
            boolean isTrigger = entry.isTrigger();

            buf.writeBlockPos(pos);
            buf.writeWithCodec(NbtOps.INSTANCE, BlockState.CODEC, state);
            buf.writeNbt(tag);
            buf.writeJsonWithCodec(VoxelShapeHelper.CODEC, shape);
            buf.writeBoolean(isTrigger);
        }
    }

    @NotNull
    private BlockPos readBlockPos (@NotNull JsonObject object)
    {
        return BlockPos.CODEC.parse(JsonOps.INSTANCE, object.get(NDatabase.Multiblocks.Serializing.POSITION)).result().map(pos -> pos).orElseThrow(() ->
                new NoSuchElementException("BlockPos is missing in this info data"));
    }

    @NotNull
    private BlockState readBlockState(@NotNull JsonObject object)
    {
        return BlockState.CODEC.parse(JsonOps.INSTANCE, object.get(NDatabase.Multiblocks.Serializing.STATE)).result().map(state -> state).orElseThrow( () ->
                new NoSuchElementException("BlockState is missing in this info data"));
    }

    @NotNull
    private CompoundTag readTag(@NotNull JsonObject object)
    {
        return CompoundTag.CODEC.parse(JsonOps.INSTANCE, object.get(NDatabase.Multiblocks.Serializing.TAG)).result().map(tag -> tag).orElseThrow( () ->
                new NoSuchElementException("Compound Tag is missing in this info data"));
    }

    @NotNull
    private VoxelShape readVoxelShape(@NotNull JsonObject object)
    {
        return VoxelShapeHelper.CODEC.parse(JsonOps.INSTANCE, object.get(NDatabase.Multiblocks.Serializing.SHAPE)).result().orElseThrow(() ->
                new NoSuchElementException("Voxel Shape is missing in this info data"));
    }

    @NotNull
    private boolean readIsTrigger(@NotNull JsonObject object)
    {
        return Codec.BOOL.parse(JsonOps.INSTANCE, object.get(NDatabase.Multiblocks.Serializing.IS_TRIGGER)).result().orElseThrow(() ->
                new NoSuchElementException("Is Trigger is missing in this info data. WTF are you doing?"));
    }
}

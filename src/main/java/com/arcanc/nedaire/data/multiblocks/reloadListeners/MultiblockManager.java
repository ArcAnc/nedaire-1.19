/**
 * @author ArcAnc
 * Created at: 12.09.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.data.multiblocks.reloadListeners;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.block.entities.multiblocks.NBaseMultiblock;
import com.arcanc.nedaire.content.network.NNetworkEngine;
import com.arcanc.nedaire.content.network.messages.MessageMultiblockSyncToClient;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class MultiblockManager extends SimpleJsonResourceReloadListener
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private Map<ResourceLocation, NBaseMultiblock> multiblocks = ImmutableMap.of();
    private boolean hasErrors = false;

    public MultiblockManager()
    {
        super(GSON, "multiblocks");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler)
    {
        this.hasErrors = false;
        ImmutableMap.Builder<ResourceLocation, NBaseMultiblock> builder = new ImmutableMap.Builder();

        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet())
        {
            ResourceLocation resourcelocation = entry.getKey();
            if (resourcelocation.getPath().startsWith("_")) continue; //Forge: filter anything beginning with "_" as it's used for metadata.

            try
            {
                NBaseMultiblock multiblock = fromJson(resourcelocation, GsonHelper.convertToJsonObject(entry.getValue(), "top element"));
                if (multiblock == null)
                {
                    Nedaire.getLogger().info("Skipping loading multiblock {} as it's serializer returned null", resourcelocation);
                    continue;
                }
                builder.put(resourcelocation, multiblock);
            }
            catch (IllegalArgumentException | JsonParseException jsonParseException)
            {
                Nedaire.getLogger().error("Parsing error loading recipe {}", resourcelocation, jsonParseException);
            }

        }

        this.multiblocks = builder.build();
        Nedaire.getLogger().info("Loaded {} multiblocks", map.size());
    }

    public boolean hadErrorsLoading() {
        return this.hasErrors;
    }

    public static NBaseMultiblock fromJson(ResourceLocation loc, JsonObject jsonObject)
    {
        return NRegistration.RegisterMultiblocks.MULTIBLOCK_SERIALIZER.fromJson(loc, jsonObject);
    }

    public Optional<NBaseMultiblock> byKey(ResourceLocation location)
    {
        return Optional.ofNullable(this.multiblocks.get(location));
    }

    public static void registerListener(final AddReloadListenerEvent event)
    {
        event.addListener(NRegistration.RegisterMultiblocks.MANAGER);
    }

    public static void syncDataToClient(final OnDatapackSyncEvent event)
    {
        ServerPlayer player = event.getPlayer();
        if (player != null)
            NNetworkEngine.sendToPlayer(
                    player,
                    new MessageMultiblockSyncToClient(NRegistration.RegisterMultiblocks.MANAGER.multiblocks));
        else
            NNetworkEngine.sendToAllPlayers(new MessageMultiblockSyncToClient(NRegistration.RegisterMultiblocks.MANAGER.multiblocks));
    }

    public Map<ResourceLocation, NBaseMultiblock> getMultiblocks() {
        return multiblocks;
    }

    public void setMultiblocks(@NotNull Map<ResourceLocation, NBaseMultiblock> multiblocks)
    {
        this.multiblocks = multiblocks;
    }

    @Override
    public String getName() {
        return super.getName();
    }
}

/**
 * @author ArcAnc
 * Created at: 14.09.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.network.messages;

import com.arcanc.nedaire.content.block.entities.multiblocks.NBaseMultiblock;
import com.arcanc.nedaire.content.registration.NRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MessageMultiblockSyncToClient implements IMessage
{
    private Map<ResourceLocation, NBaseMultiblock> multiblocks;

    public MessageMultiblockSyncToClient(@NotNull  Map<ResourceLocation, NBaseMultiblock> multiblockMap)
    {
        this.multiblocks = multiblockMap;
    }

    public MessageMultiblockSyncToClient(FriendlyByteBuf buf)
    {
        multiblocks = buf.readMap(
                FriendlyByteBuf::readResourceLocation,
                NRegistration.RegisterMultiblocks.MULTIBLOCK_SERIALIZER::fromNetwork);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeMap(multiblocks,
                FriendlyByteBuf::writeResourceLocation,
                NRegistration.RegisterMultiblocks.MULTIBLOCK_SERIALIZER::toNetwork);
    }

    @Override
    public void process(CustomPayloadEvent.Context ctx)
    {
        ctx.enqueueWork(() ->
        {
            NRegistration.RegisterMultiblocks.MANAGER.setMultiblocks(this.multiblocks);
        });
    }
}

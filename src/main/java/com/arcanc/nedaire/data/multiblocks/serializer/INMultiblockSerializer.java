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
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public interface INMultiblockSerializer<T extends INMultiblock<T>>
{

    T fromJson(ResourceLocation location, JsonObject object);

    @Nullable
    T fromNetwork(ResourceLocation location, FriendlyByteBuf buf);

    void toNetwork(FriendlyByteBuf buf, T multiblock);

}

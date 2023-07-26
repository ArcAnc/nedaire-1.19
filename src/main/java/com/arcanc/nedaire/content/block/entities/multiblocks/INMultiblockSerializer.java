package com.arcanc.nedaire.content.block.entities.multiblocks;

import com.arcanc.nedaire.util.database.NDatabase;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface INMultiblockSerializer
{
    void serializeMultiblockData(@NotNull JsonObject obj);

    default JsonObject serializeMultiblock()
    {
        JsonObject jsonobject = new JsonObject();
        //FIXME: допилить добавление типа. ЭТО БЛЯТЬ ВАЖНО!
        jsonobject.addProperty(NDatabase.Multiblocks.Serializing.TYPE, "");//BuiltInRegistries.RECIPE_SERIALIZER.getKey(this.getType()).toString());
        this.serializeMultiblockData(jsonobject);
        return jsonobject;
    }

    @NotNull  ResourceLocation getRegistryName();




}

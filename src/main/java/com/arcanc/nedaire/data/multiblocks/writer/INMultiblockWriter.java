package com.arcanc.nedaire.data.multiblocks.writer;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface INMultiblockWriter<T extends INMultiblockWriter<T>>
{
    void serializeMultiblockData(@NotNull JsonObject obj);

    default JsonObject serializeMultiblock()
    {
        JsonObject jsonobject = new JsonObject();
        //TODO: возможно допилить добавление типа или другой информации. Ситуативно
        //        jsonobject.addProperty(NDatabase.Multiblocks.Serializing.TYPE, "");//BuiltInRegistries.RECIPE_SERIALIZER.getKey(this.getType()).toString());
        this.serializeMultiblockData(jsonobject);
        return jsonobject;
    }

    @NotNull  ResourceLocation getRegistryName();
}

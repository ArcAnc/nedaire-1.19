package com.arcanc.nedaire.content.block.entities.multiblocks;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public interface INMultiblock<T extends INMultiblock<T>>
{
    @NotNull ResourceLocation getRegistryName();

    boolean isBlockTrigger(@NotNull BlockState state, @NotNull Direction side, @Nullable Level level);

    boolean createStructure(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction side, @NotNull Player player);

    List<NMultiblockSerializer.StructureInfo> getStructure(@Nullable Level level);

    Vec3i getSize(@Nullable Level level);

    void disassemble(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction clickDirectionAtCreation);

    BlockPos getTriggerOffset();

    T fromJson(ResourceLocation loc, JsonObject jObj);

    @Nullable
    T fromNetwork(ResourceLocation loc, FriendlyByteBuf buf);

    void toNetwork(FriendlyByteBuf buf, T multiblock);
}

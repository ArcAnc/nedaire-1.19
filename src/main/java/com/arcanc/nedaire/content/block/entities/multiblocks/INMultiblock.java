package com.arcanc.nedaire.content.block.entities.multiblocks;

import com.arcanc.nedaire.data.multiblocks.writer.NMultiblockWriter;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface INMultiblock<T extends INMultiblock<T>>
{
    @NotNull
    ResourceLocation getRegistryName();

    boolean isBlockTrigger(@NotNull BlockState state, @NotNull Direction side, @Nullable Level level);

    boolean createStructure(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction side, @NotNull Player player);

    Collection<NMultiblockWriter.StructureInfo> getStructure(@Nullable Level level);

    Vec3i getSize(@Nullable Level level);

    void disassemble(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction facing);

    BlockPos getTriggerOffset();
}

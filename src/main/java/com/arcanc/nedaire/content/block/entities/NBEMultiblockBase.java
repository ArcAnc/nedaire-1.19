/**
 * @author ArcAnc
 * Created at: 23.07.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.block.BlockInterfaces;
import com.arcanc.nedaire.content.block.entities.multiblocks.BlockMatcher;
import com.arcanc.nedaire.content.block.entities.multiblocks.INMultiblock;
import com.arcanc.nedaire.content.block.entities.multiblocks.NBaseMultiblock;
import com.arcanc.nedaire.content.item.tool.NHammer;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.data.multiblocks.reloadListeners.MultiblockManager;
import com.arcanc.nedaire.data.multiblocks.writer.NMultiblockWriter;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.DirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class NBEMultiblockBase extends NBaseBlockEntity implements BlockInterfaces.INWrencheble
{

    protected final Supplier<INMultiblock<?>> multiblock;
    protected boolean isFormed = false;

    protected Direction rotation = Direction.SOUTH;
    protected boolean isMaster = false;
    protected NBEMultiblockBase master;

    public NBEMultiblockBase(NRegistration.RegisterBlockEntities.MultiblockBEType<?> type, BlockPos pos, BlockState state)
    {
        super(type.blockEntityType().get(), pos, state);
        multiblock = () -> NRegistration.RegisterMultiblocks.MANAGER.byKey(type.multiblockLocation()).get();
    }
    @Override
    public InteractionResult onUsed(@NotNull UseOnContext ctx)
    {

        Player player = ctx.getPlayer();
        InteractionHand hand = ctx.getHand();
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Direction direction = ctx.getHorizontalDirection();

        ItemStack stack = player.getItemInHand(hand);

        if(level.isClientSide())
            return  InteractionResult.PASS;
        if (!(stack.getItem() instanceof NHammer))
            return InteractionResult.PASS;

        Nedaire.getLogger().warn("Try form multiblock " + multiblock.get().createStructure(level, pos, direction, player));

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void readCustomTag(CompoundTag tag, boolean descPacket)
    {
        if(tag.contains(NDatabase.Multiblocks.IS_FORMED))
        {
            this.isFormed = tag.getBoolean(NDatabase.Multiblocks.IS_FORMED);
        }
        if (tag.contains(NDatabase.Multiblocks.ROTATION))
        {
            this.rotation = Direction.from3DDataValue(tag.getInt(NDatabase.Multiblocks.ROTATION));
        }
        if (tag.contains(NDatabase.Multiblocks.IS_MASTER))
        {
            this.isMaster = tag.getBoolean(NDatabase.Multiblocks.IS_MASTER);
        }
        if (tag.contains(NDatabase.Multiblocks.MASTER_POS))
        {
            this.master = BlockHelper.castTileEntity(level, BlockPos.of(tag.getLong(NDatabase.Multiblocks.MASTER_POS)), NBEMultiblockBase.class).get();
        }
        else
            this.master = null;
    }

    @Override
    public void writeCustomTag(CompoundTag tag, boolean descPacket)
    {
        tag.putBoolean(NDatabase.Multiblocks.IS_FORMED, isFormed);
        tag.putInt(NDatabase.Multiblocks.ROTATION, rotation.get3DDataValue());
        tag.putBoolean(NDatabase.Multiblocks.IS_MASTER, isMaster);
        if (isFormed)
            tag.putLong(NDatabase.Multiblocks.MASTER_POS, master.getBlockPos().asLong());
    }

    public void reCheckStructure()
    {
        //FIXME: проверить работу кода. Он написан, но не проверен
        boolean formed = true;

        for (NMultiblockWriter.StructureInfo info : multiblock.get().getStructure(getLevel()))
        {
            Rotation rot = DirectionHelper.getRotationBetweenFacings(Direction.SOUTH, rotation);
            BlockPos inWorldPos = info.pos().rotate(rot).offset(getBlockPos());

            BlockState state = getLevel().getBlockState(inWorldPos);

            BlockState toCheckState = info.state();

            if (BlockHelper.isRotatable(toCheckState))
                toCheckState = BlockHelper.rotateBlockState(toCheckState, rot);
            if(!BlockMatcher.matches(toCheckState, state, getLevel(), getBlockPos()))
                formed = false;
        }

        if (!formed)
            multiblock.get().disassemble(getLevel(), getBlockPos(), rotation);
    }


    public void setFormed(boolean formed)
    {
        isFormed = formed;
    }

    public void setRotation(Direction rotation)
    {
        this.rotation = rotation;
    }

    public void setIsMaster(boolean master)
    {
        isMaster = master;
    }

    public void setMaster (NBEMultiblockBase master)
    {
        this.master = master;
    }

    public boolean isFormed()
    {
        return isFormed;
    }

    public boolean isMaster()
    {
        return isMaster;
    }

    public NBEMultiblockBase getMaster()
    {
        return master;
    }

    public Direction getRotation()
    {
        return rotation;
    }

    public INMultiblock<?> getMultiblock()
    {
        return multiblock.get();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if (isFormed)
        {
            if (!isMaster)
                master.getCapability(cap, side);
        }
        return super.getCapability(cap, side);
    }
}

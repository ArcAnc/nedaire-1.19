/**
 * @author ArcAnc
 * Created at: 10.04.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.Nedaire;
import com.arcanc.nedaire.content.block.BlockInterfaces;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.item.tool.NHammer;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.arcanc.nedaire.util.helpers.WorldHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.arcanc.nedaire.util.helpers.BlockHelper.BlockProperties.VERTICAL_ATTACHMENT;

public class NBEPlatform extends NBESidedAccess implements BlockInterfaces.IInventoryCallback, BlockInterfaces.INWrencheble
{
    private final BlockPos[] poses;
    protected VimStorage energy;
    protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);
    public NBEPlatform(BlockPos pos, BlockState state)
    {
        super(NRegistration.RegisterBlockEntities.BE_PLATFORM.get(), pos, state);

        for (Direction dir : Direction.values())
        {
            if (dir == Direction.SOUTH)
                this.ports.put(dir, AccessType.OUTPUT);
        }

        this.energy = VimStorage.newConfig(this).setMaxEnergy(5000).setEnergy(0).build();

        poses = new BlockPos[]{getBlockPos().above(), getBlockPos().below()};
    }

    @Override
    public InteractionResult onUsed(UseOnContext ctx)
    {
        Nedaire.getLogger().warn("Platform used");
        BlockPos pos = ctx.getClickedPos();
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        InteractionHand hand = ctx.getHand();
        BlockState state = level.getBlockState(pos);

        if (level.isClientSide())
            return InteractionResult.PASS;

        Nedaire.getLogger().warn("We are on server");

        ItemStack stack = player.getItemInHand(hand);
        Direction dir = ctx.getClickedFace();
        if (stack.getItem() instanceof NHammer && dir.getAxis().isHorizontal())
        {
            BlockState newState = state.setValue(BlockHelper.BlockProperties.HORIZONTAL_FACING, dir);
            level.setBlockAndUpdate(pos, newState);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    public BlockPos[] getPoses()
    {
        return poses;
    }

    @Override
    public void readCustomTag(CompoundTag tag, boolean descPacket)
    {
        super.readCustomTag(tag, descPacket);
        energy.deserializeNBT(tag.getCompound(NDatabase.Capabilities.Vim.TAG_LOCATION));
    }

    @Override
    public void writeCustomTag(CompoundTag tag, boolean descPacket)
    {
        super.writeCustomTag(tag, descPacket);
        tag.put(NDatabase.Capabilities.Vim.TAG_LOCATION, energy.serializeNBT());
    }

    @Override
    public void invalidateCaps()
    {
        energyHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == VimHelper.vimHandler)
        {
            if (side == null || ports.get(side) != AccessType.NONE)
            {
                return energyHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onVimChange()
    {
        setChanged();
    }

    @Override
    public void onInventoryChange(int slot)
    {
        setChanged();
    }

    public void throwMined(List<ItemStack> dropList)
    {
        BlockState state = getBlockState();
        Direction dir = state.getValue(BlockHelper.BlockProperties.HORIZONTAL_FACING);
        Vec3 pos = Vec3.atCenterOf(getBlockPos()).relative(dir, 0.4f);
        double speed = getLevel().random.nextDouble() * 0.2D - 0.1D;
        for (ItemStack stack : dropList)
        {
            WorldHelper.spawnItemEntity(getLevel(), pos.x(), pos.y(), pos.z(), speed * dir.getStepX(), 0, speed * dir.getStepZ(), stack);
        }

    }

    public static abstract class Attachable extends NBERedstoneSensitive
    {
        protected NBEPlatform platform;

        public Attachable(BlockEntityType<?> type, BlockPos pos, BlockState state)
        {
            super(type, pos, state);

            Direction dir = state.getValue(VERTICAL_ATTACHMENT);

            Nedaire.getLogger().warn(dir.toString());

            BlockHelper.castTileEntity(getLevel(), pos.relative(dir), NBEPlatform.class).ifPresent(tile -> platform = tile);
        }

        public void setPlatform(NBEPlatform platform)
        {
            this.platform = platform;
        }

        public NBEPlatform getPlatform()
        {
            return platform;
        }
    }
}

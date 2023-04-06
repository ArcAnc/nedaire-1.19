/**
 * @author ArcAnc
 * Created at: 2023-04-06
 * Copyright (c) 2023
 *
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.block.BlockInterfaces;
import com.arcanc.nedaire.content.block.entities.ticker.NServerTickerBlockEntity;
import com.arcanc.nedaire.content.capabilities.vim.IVim;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.AccessType;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NBEGeneratorLightning extends NBERedstoneSensitive implements BlockInterfaces.IInventoryCallback, NServerTickerBlockEntity
{
    protected VimStorage energy;
    protected final LazyOptional<IVim> energyHandler = LazyOptional.of(() -> energy);

    public NBEGeneratorLightning(BlockPos pos, BlockState state)
    {
        super(NRegistration.RegisterBlockEntities.BE_GENERATOR_LIGHTNING.get(), pos, state);

        for (Direction dir : Direction.values())
        {
            if (dir != Direction.UP)
            {
                this.ports.put(dir, AccessType.FULL);
            }
        }

        this.energy = VimStorage.newConfig(this).setMaxEnergy(10000).setEnergy(0).build();
    }

    @Override
    public void tickServer()
    {
        if (isPowered())
        {
            boolean isLit = false;

            BlockState state = getLevel().getBlockState(getBlockPos().above());

            if (state.getBlock() instanceof LightningRodBlock && state.getValue(LightningRodBlock.POWERED))
            {
                isLit = true;
                this.energy.setEnergyStored(energy.getMaxEnergyStored());

            }

            if (getBlockState().getValue(BlockHelper.BlockProperties.LIT) != isLit)
            {
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockHelper.BlockProperties.LIT, isLit), Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    public void readCustomTag(CompoundTag tag, boolean descPacket)
    {
        super.readCustomTag(tag, descPacket);
        if (tag.contains(NDatabase.Capabilities.Vim.TAG_LOCATION))
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
        this.energyHandler.invalidate();
        super.invalidateCaps();
    }

    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if (side != Direction.UP)
        {
            if (cap == VimHelper.vimHandler)
                return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onVimChange()
    {
        setChanged();
    }
}

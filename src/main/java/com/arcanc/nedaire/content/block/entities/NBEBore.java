/**
 * @author ArcAnc
 * Created at: 10.04.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block.entities;

import com.arcanc.nedaire.content.block.BlockInterfaces;
import com.arcanc.nedaire.content.block.entities.ticker.NServerTickerBlockEntity;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.TagHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.arcanc.nedaire.util.helpers.BlockHelper.BlockProperties.FACING;
import static com.arcanc.nedaire.util.helpers.BlockHelper.BlockProperties.WATERLOGGED;

public class NBEBore extends NBEPlatform.Attachable implements NServerTickerBlockEntity, BlockInterfaces.IInteractionObjectN<NBEBore>, BlockInterfaces.IInventoryCallback
{
    private static final int PER_TICK = 2;
    private static final int DIG_DIST = 40;
    private static final int DIG_RAD = 6;

    protected NSimpleItemStorage inv;
    protected final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inv);
    private Optional<BlockPos> mining = Optional.empty();
    private long startDiggingBlock;

    public NBEBore(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);

        this.inv = new NSimpleItemStorage(this).addSlot(1, stack -> stack.is(ItemTags.PICKAXES));
    }

    @Override
    public void tickServer()
    {
        if (isPowered())
        {
            VimHelper.getVimHandler(platform).ifPresent(energyHandler ->
            {
                if (energyHandler.getEnergyStored() >= PER_TICK)
                {
                    mining.ifPresentOrElse( pos ->
                    {
                        ItemStack stack = inv.getStackInSlot(0);
                        if (!stack.isEmpty())
                        {
                            energyHandler.extract(PER_TICK, false);

                            BlockState state = getLevel().getBlockState(pos);
                            float speed = stack.getDestroySpeed(state);

                            if (speed > 1.0f)
                            {
                                int effLvl = stack.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
                                if (effLvl > 0)
                                {
                                    speed += (float)(effLvl * effLvl + 1);
                                }
                                if (state.getValue(WATERLOGGED))
                                {
                                    speed /= 5.0f;
                                }

                                float destroyProgress = speed / state.getDestroySpeed(getLevel(), pos) / 30;

                                mineBlock(destroyProgress, pos, state);
                            }
                        }
                    }, () -> mining = getClosesMinebleBlock());

                }
            });
        }
    }

    private void mineBlock(float destroyProgress, BlockPos pos, BlockState state)
    {
        if (this.startDiggingBlock == 0)
        {
            this.startDiggingBlock = getLevel().getGameTime();
        }

        long timeDiff = getLevel().getGameTime() - startDiggingBlock;
        int stage = (int)(((timeDiff + 1) * destroyProgress) * 10);

        AABB around = new AABB(pos).inflate(16);

        List<Player> pl =  getLevel().getEntitiesOfClass(Player.class, around);
        if (!pl.isEmpty())
        {
            for (Player p : pl)
            {
                getLevel().destroyBlockProgress(p.getId(), getBlockPos(), stage);
            }
        }
        if (destroyProgress >= 1.0f)
        {
            ItemStack stack = inv.getStackInSlot(0).copy();
            stack.hurt(1, getLevel().getRandom(), null);

            ItemHelper.getItemHandler(platform).ifPresent(itemHandler ->
            {
                BlockEntity blockentity = state.hasBlockEntity() ? this.getLevel().getBlockEntity(pos) : null;
                LootContext.Builder builder = new LootContext.Builder((ServerLevel) getLevel()).
                        withRandom(getLevel().getRandom()).
                        withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(platform.getBlockPos())).
                        withParameter(LootContextParams.TOOL, stack).
                        withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).
                        withOptionalParameter(LootContextParams.THIS_ENTITY, null);

                List<ItemStack> dropList = state.getDrops(builder);

                platform.throwMined(dropList);
            });
            getLevel().destroyBlock(pos, false, null, Block.UPDATE_LIMIT);

            startDiggingBlock = 0;
            mining = Optional.empty();
        }
    }

    private Optional<BlockPos> getClosesMinebleBlock()
    {
        Direction facing = getBlockState().getValue(FACING);
        BlockPos startPos = getBlockPos().relative(facing);
        Vec3 startVec = Vec3.atCenterOf(startPos);
        for (int d = 0; d < DIG_DIST; d++)
        {
            Vec3 vec = startVec.relative(facing, d);

            for (int horiz = -DIG_RAD; horiz < DIG_RAD; ++horiz)
            {
                for (int vert = -DIG_RAD; vert < DIG_RAD; ++vert)
                {
                    Vec3 pos = new Vec3(vec.x(), vec.y(), vec.z());
                    if (facing.getAxis() == Direction.Axis.X)
                        pos = pos.add(0, vert, horiz);
                    else if (facing.getAxis() == Direction.Axis.Y)
                        pos = pos.add(vert, 0, horiz);
                    else
                        pos = pos.add(vert, horiz, 0);

                    BlockPos mineble = BlockPos.containing(pos);
                    BlockState state = getLevel().getBlockState(mineble);
                    if (state.is(BlockTags.MINEABLE_WITH_PICKAXE))
                        return Optional.of(mineble);
                }
            }
        }
        return Optional.empty();
    }

    public boolean isWorking()
    {
        return startDiggingBlock > 0;
    }

    public Optional<BlockPos> getMining ()
    {
        return mining;
    }

    public void stopProcess()
    {
        startDiggingBlock = 0;
        mining = Optional.empty();
    }
    @Override
    public boolean isPowered()
    {
        if (getLevel() == null)
            return false;
        if (getCurrentRedstoneMod() == 0 && getLevel().hasNeighborSignal(getPlatform().getBlockPos()))
            return true;
        else if (getCurrentRedstoneMod() == 1 && !getLevel().hasNeighborSignal(getPlatform().getBlockPos()))
            return true;
        else return getCurrentRedstoneMod() == 2;
    }

    @Override
    public void readCustomTag(CompoundTag tag, boolean descPacket)
    {
        super.readCustomTag(tag, descPacket);
        inv.deserializeNBT(tag.getCompound(NDatabase.Capabilities.ItemHandler.TAG_LOCATION));
        startDiggingBlock = tag.getLong(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.START_DIG);
        CompoundTag compound = tag.getCompound(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.MINING);
        if (compound.isEmpty())
            mining = Optional.empty();
        else
        {
            BlockPos pos = TagHelper.readBlockPos(tag, NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.MINING);
            mining = Optional.of(pos);
        }
    }

    @Override
    public void writeCustomTag(CompoundTag tag, boolean descPacket)
    {
        super.writeCustomTag(tag, descPacket);
        tag.put(NDatabase.Capabilities.ItemHandler.TAG_LOCATION, inv.serializeNBT());
        tag.putLong(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.START_DIG, startDiggingBlock);
        mining.ifPresentOrElse(pos ->
        {
            TagHelper.writeBlockPos(pos, tag, NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.MINING);
        }, () ->
        {
            tag.put(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.MINING, new CompoundTag());
        });
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == ItemHelper.itemHandler && side == null)
        {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps()
    {
        itemHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    public NRegistration.RegisterMenuTypes.BEContainer<? super NBEBore, ?> getContainerType()
    {
        return null;
    }

    @Override
    public boolean canUseGui(Player player)
    {
        return true;
    }

    @Nullable
    @Override
    public NBEBore getBE()
    {
        return this;
    }

    @Override
    public void onInventoryChange(int slot)
    {
        setChanged();
    }
}

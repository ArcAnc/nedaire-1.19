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
import com.arcanc.nedaire.content.block.entities.ticker.NServerTickerBlockEntity;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.*;
import com.arcanc.nedaire.util.inventory.NSimpleItemStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
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

import static com.arcanc.nedaire.util.helpers.BlockHelper.BlockProperties.WATERLOGGED;

public class NBEBore extends NBEPlatform.Attachable implements NServerTickerBlockEntity, BlockInterfaces.INInteractionObject<NBEBore>, BlockInterfaces.IInventoryCallback, BlockInterfaces.INWrencheble
{
    private static final int PER_TICK = 2;
    private static final int DIG_DIST = 40;
    private static final int DIG_RAD = 6;
    private static final double ROT_STEP = 0.125d;
    private static final double ROT_CHECK = ROT_STEP/8d;

    protected NSimpleItemStorage inv;
    protected final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inv);
    private Optional<BlockPos> mining = Optional.empty();
    private long startDiggingBlock;

    private Vec3 prevRot;
    private Vec3 currentRot;
    private Vec3 targetRot;

    public NBEBore(BlockPos pos, BlockState state)
    {
        super(NRegistration.RegisterBlockEntities.BE_BORE.get(), pos, state);

        this.inv = new NSimpleItemStorage(this).addSlot(1, stack -> stack.is(ItemTags.PICKAXES));

        prevRot = Vec3.ZERO;
        currentRot = Vec3.ZERO;
        targetRot = Vec3.ZERO;
    }

    @Override
    public void tickServer()
    {
        if (isPowered())
        {
            Direction attach = getBlockState().getValue(BlockHelper.BlockProperties.VERTICAL_ATTACHMENT);
            BlockPos blockPos = getBlockPos().relative(attach);
            BlockHelper.castTileEntity(getLevel(), blockPos, NBEPlatform.class).ifPresent( tile ->
            {
                VimHelper.getVimHandler(tile).ifPresent(energyHandler ->
                {
                    if (energyHandler.getEnergyStored() >= PER_TICK)
                    {
                        rotate();
                        mining.ifPresentOrElse(pos ->
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
                                    if (state.hasProperty(WATERLOGGED) && state.getValue(WATERLOGGED))
                                    {
                                        speed /= 5.0f;
                                    }

                                    float destroyProgress = speed / state.getDestroySpeed(getLevel(), pos) / 30;

                                    //Nedaire.getLogger().warn("Speed: " + speed + " DestroyProgress: " + destroyProgress);

                                    mineBlock(destroyProgress, pos, state);
                                }
                            }
                        }, () -> mining = getClosestMinebleBlock());

                    }
                });
            });
        }
    }

    @Override
    public InteractionResult onUsed(@NotNull UseOnContext ctx)
    {
        Level level = ctx.getLevel();
        if (level.isClientSide())
            return InteractionResult.PASS;

        Direction dir = ctx.getClickedFace();
        Nedaire.getLogger().warn("Direction: " + dir.getName());
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Direction attachState = state.getValue(BlockHelper.BlockProperties.VERTICAL_ATTACHMENT);

        if (dir != attachState)
        {
            Vec3i newDir = dir.getNormal();
            targetRot = new Vec3(newDir.getX(), newDir.getY(), newDir.getZ());
            setChanged();
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    private void rotate()
    {
        float x = Math.abs((float)(targetRot.x() - currentRot.x()));
        float y = Math.abs((float)(targetRot.y() - currentRot.y()));
        float z = Math.abs((float)(targetRot.z() - currentRot.z()));
        Nedaire.getLogger().warn("X: " + x + " Y: " + y + " Z: " + z);
        if (x > ROT_CHECK || y > ROT_CHECK || z > ROT_CHECK)
        {
            Nedaire.getLogger().warn("Rotated");
            prevRot = new Vec3(currentRot.x(), currentRot.y(), currentRot.z());
            currentRot = MathHelper.lerp(currentRot, targetRot, ROT_STEP);
            //Nedaire.getLogger().warn(String.format("Current: %s, Target: %s", currentRot, targetRot));
            setChanged();
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

        //Nedaire.getLogger().warn("Stage: " + stage + " TimeDiff: " + timeDiff);

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
                LootParams.Builder builder = new LootParams.Builder((ServerLevel) getLevel()).
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

    private Optional<BlockPos> getClosestMinebleBlock()
    {
        Direction facing = Direction.getNearest(currentRot.x(), currentRot.y(), currentRot.z());
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
                    {
                        targetRot = getBlockPos().getCenter().vectorTo(pos);
                        return Optional.of(mineble);
                    }
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

    public Vec3 getCurrentRot()
    {
        return currentRot;
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
        prevRot = TagHelper.readVec3(tag, NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.PREV_ROT);
        currentRot = TagHelper.readVec3(tag, NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.CUR_ROT);
        targetRot = TagHelper.readVec3(tag, NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.TARGET_ROT);
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
        TagHelper.writeVec3(prevRot, tag, NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.PREV_ROT);
        TagHelper.writeVec3(currentRot, tag, NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.CUR_ROT);
        TagHelper.writeVec3(targetRot, tag, NDatabase.Blocks.BlockEntities.TagAddress.Machines.Bore.TARGET_ROT);
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
    public NRegistration.RegisterMenuTypes.BEContainer<NBEBore, ?> getContainerType()
    {
        return NRegistration.RegisterMenuTypes.BORE;
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

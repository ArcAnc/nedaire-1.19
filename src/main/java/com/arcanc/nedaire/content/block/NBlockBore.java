/**
 * @author ArcAnc
 * Created at: 15.04.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.block;

import com.arcanc.nedaire.content.block.entities.NBEBore;
import com.arcanc.nedaire.content.block.entities.NBEPlatform;
import com.arcanc.nedaire.content.item.tool.NHammer;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Quaternionf;

import java.util.Optional;

public class NBlockBore extends NTileProviderBlock<NBEBore>
{
    private static final VoxelShape SHAPE = Shapes.block();

    public NBlockBore(Properties properties)
    {
        super(properties, NRegistration.RegisterBlockEntities.BE_BORE);
    }

    protected BlockState getInitDefaultState()
    {
        BlockState state = super.getInitDefaultState();
        if (state.hasProperty(BlockHelper.BlockProperties.FACING))
        {
            state = state.setValue(BlockHelper.BlockProperties.VERTICAL_ATTACHMENT, Direction.DOWN);
        }
        return state;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (!level.isClientSide())
        {
            ItemStack stack = player.getItemInHand(hand);
            Direction dir = hitResult.getDirection();
            Direction attachState = state.getValue(BlockHelper.BlockProperties.VERTICAL_ATTACHMENT);

            if (stack.getItem() instanceof NHammer && dir != attachState)
            {
                BlockHelper.castTileEntity(level, pos, NBEBore.class).ifPresent(tile ->
                {
                    Quaternionf rotation = dir.getRotation();
                });
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return super.getStateForPlacement(context).setValue(BlockHelper.BlockProperties.VERTICAL_ATTACHMENT, context.getNearestLookingVerticalDirection());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos newPos, Block block, BlockPos oldPos, boolean bool)
    {
        BlockHelper.castTileEntity(level, newPos, NBEBore.class).ifPresent(tile ->
        {
            BlockPos basePose = newPos.relative(state.getValue(BlockHelper.BlockProperties.VERTICAL_ATTACHMENT));
            Optional<NBEPlatform> platform = BlockHelper.castTileEntity(level, basePose, NBEPlatform.class);

            if(platform.isEmpty())
            {
                dropBlock(level, newPos);
            }
        });

        super.neighborChanged(state, level, newPos, block, oldPos, bool);
    }

    private void dropBlock(Level level, BlockPos pos)
    {
        level.destroyBlock(pos, true);
        level.sendBlockUpdated(pos, defaultBlockState(), level.getBlockState(pos), UPDATE_ALL);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity player, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, player, stack);

        BlockHelper.castTileEntity(level, pos, NBEBore.class).ifPresent(tile ->
        {
            Optional<NBEPlatform> down = BlockHelper.castTileEntity(level, pos.below(), NBEPlatform.class);
            Optional<NBEPlatform> up = BlockHelper.castTileEntity(level, pos.above(), NBEPlatform.class);
            if (down.isEmpty() && up.isEmpty())
            {
                dropBlock(level, pos);
            }
        });
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(BlockHelper.BlockProperties.VERTICAL_ATTACHMENT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos)
    {
        return SHAPE;
    }
}

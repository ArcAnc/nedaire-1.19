/**
 * @author ArcAnc
 * Created at: 28.04.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.content.container.screen;

import com.arcanc.nedaire.content.block.entities.NBEPlatform;
import com.arcanc.nedaire.content.capabilities.vim.VimStorage;
import com.arcanc.nedaire.content.container.menu.NBoreMenu;
import com.arcanc.nedaire.content.container.widget.info.EnergyInfoArea;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NBoreScreen  extends NContainerScreen<NBoreMenu>
{
    public NBoreScreen(NBoreMenu slots, Inventory player, Component title)
    {
        super(slots, player, title);

        this.imageHeight = 175;
        this.imageWidth =  185;
    }

    @Override
    protected void init()
    {
        super.init();

        Level level = menu.be.getLevel();
        BlockState state = menu.be.getBlockState();
        Direction dir = state.getValue(BlockHelper.BlockProperties.VERTICAL_ATTACHMENT);

        BlockPos platformPos = menu.be.getBlockPos().relative(dir);

        BlockHelper.castTileEntity(level, platformPos, NBEPlatform.class).ifPresent( tile ->
        {
            addRedstoneSensitiveDropPanel(tile);

            addRenderableWidget(new EnergyInfoArea(new Rect2i(this.getGuiLeft() + 15, this.getGuiTop() + 20, 14, 42),
                    VimHelper.getVimHandler(tile).
                            orElseGet(() -> VimStorage.newConfig(null).setMaxEnergy(5000).build())));

        });
    }
}

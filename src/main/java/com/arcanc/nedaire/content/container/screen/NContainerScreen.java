/**
 * @author ArcAnc
 * Created at: 2023-01-03
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.screen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.arcanc.nedaire.content.block.entities.NBERedstoneSensitive;
import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.content.container.NSlot.ItemHandlerGhost;
import com.arcanc.nedaire.content.container.widget.ChangeSizeButton;
import com.arcanc.nedaire.content.container.widget.ChangeSizeButton.ButtonCtx;
import com.arcanc.nedaire.content.container.widget.DropPanel;
import com.arcanc.nedaire.content.container.widget.DropPanel.Side;
import com.arcanc.nedaire.content.container.widget.Label;
import com.arcanc.nedaire.content.container.widget.Panel;
import com.arcanc.nedaire.content.container.widget.RadioButton;
import com.arcanc.nedaire.content.container.widget.RadioButton.CustomCheckbox;
import com.arcanc.nedaire.content.container.widget.icon.Icon;
import com.arcanc.nedaire.content.container.widget.info.IconCheckbox;
import com.arcanc.nedaire.content.container.widget.info.InfoArea;
import com.arcanc.nedaire.content.network.NNetworkEngine;
import com.arcanc.nedaire.content.network.messages.MessageContainerUpdate;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.FilterHelper;
import com.arcanc.nedaire.util.helpers.FluidHelper;
import com.arcanc.nedaire.util.helpers.ItemHelper;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.arcanc.nedaire.util.helpers.VimHelper;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class NContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> 
{
	public static final ResourceLocation MIDDLE = NDatabase.GUI.getTexturePath(NDatabase.GUI.Background.Textures.MIDDLE); 
	public static final ResourceLocation MIDDLE_TOP = NDatabase.GUI.getTexturePath(NDatabase.GUI.Background.Textures.MIDDLE_TOP); 
	public static final ResourceLocation MIDDLE_BOT = NDatabase.GUI.getTexturePath(NDatabase.GUI.Background.Textures.MIDDLE_BOTTOM); 
	public static final ResourceLocation MIDDLE_LEFT = NDatabase.GUI.getTexturePath(NDatabase.GUI.Background.Textures.MIDDLE_LEFT); 
	public static final ResourceLocation MIDDLE_RIGHT = NDatabase.GUI.getTexturePath(NDatabase.GUI.Background.Textures.MIDDLE_RIGHT); 
	public static final ResourceLocation LEFT_TOP = NDatabase.GUI.getTexturePath(NDatabase.GUI.Background.Textures.LEFT_TOP); 
	public static final ResourceLocation LEFT_BOT = NDatabase.GUI.getTexturePath(NDatabase.GUI.Background.Textures.LEFT_BOTTOM); 
	public static final ResourceLocation RIGHT_TOP = NDatabase.GUI.getTexturePath(NDatabase.GUI.Background.Textures.RIGHT_TOP); 
	public static final ResourceLocation RIGHT_BOT = NDatabase.GUI.getTexturePath(NDatabase.GUI.Background.Textures.RIGHT_BOTTOM); 
	
	public static final ResourceLocation FILTER_WHITELIST = NDatabase.GUI.getTexturePath(NDatabase.GUI.Filter.WHITELIST);
	public static final ResourceLocation FILTER_TAG = NDatabase.GUI.getTexturePath(NDatabase.GUI.Filter.TAG);
	public static final ResourceLocation FILTER_MOD_OWNER = NDatabase.GUI.getTexturePath(NDatabase.GUI.Filter.MOD_OWNER);

	public static final ResourceLocation ICON_FILTER_ITEM = NDatabase.GUI.getTexturePath(NDatabase.GUI.Filter.ICON_FILTER_ITEM);
	public static final ResourceLocation ICON_FILTER_FLUID = NDatabase.GUI.getTexturePath(NDatabase.GUI.Filter.ICON_FILTER_FLUID);
	public static final ResourceLocation ICON_FILTER_VIM = NDatabase.GUI.getTexturePath(NDatabase.GUI.Filter.ICON_FILTER_VIM);
	
	protected List<Panel> panelList = new ArrayList<>();
	protected int currentPanel = 0;
	
	protected List<DropPanel> dropPanelList = new LinkedList<>();
	
	public NContainerScreen(T slots, Inventory player, Component title) 
	{
		super(slots, player, title);
		this.inventoryLabelY = this.imageHeight - 91;
	}
	
	protected List<InfoArea> makeInfoAreas()
	{
		return ImmutableList.of();
	}
	
	@Override
	protected void init() 
	{
		super.init();
	}
	
	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		this.renderTooltip(stack, mouseX, mouseY);
	}
	
	@Override
	protected void containerTick() 
	{
		DropPanel prevPanelLeft = null;
		DropPanel prevPanelRight = null;

		for (int q = 0; q < dropPanelList.size(); q++)
		{
			DropPanel panel = dropPanelList.get(q);

			Rect2i coordLeft = prevPanelLeft == null ? new Rect2i(0, this.getGuiTop() + 2, 0, 0) : new Rect2i(prevPanelLeft.getX(), prevPanelLeft.getY(), prevPanelLeft.getWidth(), prevPanelLeft.getHeight());
			Rect2i coordRight = prevPanelRight == null ? new Rect2i(0, this.getGuiTop() + 2, 0, 0) : new Rect2i(prevPanelRight.getX(), prevPanelRight.getY(), prevPanelRight.getWidth(), prevPanelRight.getHeight());
			
			Rect2i relativeCoord = new Rect2i(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight());
			if (panel.getSide() == Side.LEFT)
			{
				panel.setX(this.getGuiLeft() + 3 - panel.getWidth());
				panel.setY(coordLeft.getY() + coordLeft.getHeight());
			}
			else
			{
				panel.setX(this.getGuiLeft() + this.imageWidth - 3 );
				panel.setY(coordRight.getY() + coordRight.getHeight());
			}

			if (panel.getSide() == Side.LEFT)
			{
				prevPanelLeft = panel;
			}
			else
			{
				prevPanelRight = panel;
			}
			
			for (AbstractWidget widget : panel.getWidgets())
			{
				widget.setX(widget.getX() - relativeCoord.getX() + panel.getX());
				widget.setY(widget.getY() - relativeCoord.getY() + panel.getY());
			}
		}
		
		for (Slot s : this.menu.slots)
		{
			if (s instanceof NSlot slot)
			{
				if (slot.getPanelIndex() == currentPanel)
					slot.setActive(true);
				else
					slot.setActive(false);
			}
		}
		
/*		if (panelSwitcher != null)
		{
			if (panelSwitcher.selected())
				currentPanel = 0;
			else
				currentPanel = 10;
		}
*/		
		for (Panel p : panelList)
		{
			if (p.index == currentPanel)
			{
				p.visible = true;
			}
			else
				p.visible = false;
		}
	}
	
	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int x, int y) 
	{
		renderBackground(stack);
		int x_pos = this.leftPos;
		int	y_pos = this.topPos;

		stack.pushPose();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

		RenderSystem.setShaderTexture(0, NContainerScreen.LEFT_TOP);
		blit(stack, x_pos, y_pos, 0, 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_TOP);
		blit(stack, x_pos + 8, y_pos, 0, 0, 0, this.imageWidth - 16, 8, 8, 8);
		
		RenderSystem.setShaderTexture(0, NContainerScreen.RIGHT_TOP);
		blit(stack, x_pos + this.imageWidth - 8, y_pos, 0, 0, 0, 8, 8, 8, 8);
		
		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_LEFT);
		blit(stack, x_pos, y_pos + 8, 0, 0, 0, 8, this.imageHeight - 16, 8, 8);
		
		RenderSystem.setShaderTexture(0, NContainerScreen.LEFT_BOT);
		blit(stack, x_pos, y_pos + this.imageHeight - 8, 0, 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_BOT);
		blit(stack, x_pos + 8, y_pos + this.imageHeight - 8, 0, 0, 0, this.imageWidth - 16, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.RIGHT_BOT);
		blit(stack, x_pos + this.imageWidth - 8, y_pos + this.imageHeight - 8, 0, 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_RIGHT);
		blit(stack, x_pos + this.imageWidth - 8, y_pos + 8, 0, 0, 0, 8, this.imageHeight - 16, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE);
		blit(stack, x_pos + 8, y_pos + 8, 0, 0, 0, this.imageWidth - 16, this.imageHeight - 16, 8, 8);

		stack.popPose();
	}
	
	protected Panel addPanel(Panel panel)
	{
		addRenderableOnly(panel);
		panelList.add(panel);
		for (AbstractWidget comp : panel.getObjects())
			addRenderableWidget(comp);
		return panel;
	}
	
	protected DropPanel addDropPanel(DropPanel panel)
	{
		addRenderableWidget(panel);
		dropPanelList.add(panel);
		for (AbstractWidget comp : panel.getWidgets())
			addRenderableWidget(comp);
		return panel;
	}
	
	protected DropPanel addRedstoneSensitiveDropPanel(BlockEntity be)
	{
		return BlockHelper.castTileEntity(be, NBERedstoneSensitive.class).map(tile -> 
		{
			return addDropPanel(new DropPanel(100, 90, Side.LEFT, false, new Color (150, 40, 40), () -> Icon.of(Items.REDSTONE),
					() -> Tooltip.create(Component.translatable(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_NAME))).
					addWidget(new Label(50, 5, 20, 10, () -> Component.translatable(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_NAME).withStyle(ChatFormatting.YELLOW))).
					
					addWidget(new Label(50, 15, 20, 10, () -> Component.translatable(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_CONTROL_MODE).withStyle(ChatFormatting.BLACK))).
					addWidget(new Label(50, 25, 20, 10, () -> Component.translatable(tile.getCurrentRedstoneMod() == 2 ? NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_CONTROL_MODE_DISABLED : 
																								NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_CONTROL_MODE_ENABLED).withStyle(ChatFormatting.GRAY))).
					addWidget(new Label(50, 35, 20, 10, () -> Component.translatable(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_NAME).withStyle(ChatFormatting.YELLOW))).
					addWidget(new Label(50, 45, 20, 10, () -> Component.translatable(tile.getCurrentRedstoneMod() == 0 ? NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_HIGHT :
																					 tile.getCurrentRedstoneMod() == 1 ? NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_LOW :
																								NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_DISABLED).withStyle(ChatFormatting.GRAY))).
					
					addWidget(RadioButton.newRadioButton(3, 2).
					setPos(18, 62).
					setSize(60, 20).
					setCurrentButtonId(tile.getCurrentRedstoneMod()).
					build().
						addButton(RadioButton.newButton(Icon.of(new ItemStack(Items.REDSTONE_BLOCK), false), () -> Tooltip.create(Component.translatable(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_HIGHT))).
								pressAction(but -> 
								{
									CompoundTag tag = new CompoundTag();
									
									tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.RedstoneSensitive.REDSTONE_MOD, 0);
									tag.putInt("x", tile.getBlockPos().getX());
									tag.putInt("y", tile.getBlockPos().getY());
									tag.putInt("z", tile.getBlockPos().getZ());
									
									sendUpdateToServer(tag);
								}).build()).
						addButton(RadioButton.newButton(Icon.of(new ItemStack(Items.REDSTONE_TORCH), false), () -> Tooltip.create(Component.translatable(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_LOW))).
								pressAction(but ->
								{
									CompoundTag tag = new CompoundTag();
									
									tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.RedstoneSensitive.REDSTONE_MOD, 1);
									tag.putInt("x", tile.getBlockPos().getX());
									tag.putInt("y", tile.getBlockPos().getY());
									tag.putInt("z", tile.getBlockPos().getZ());
									
									sendUpdateToServer(tag);
								}).build()).
						addButton(RadioButton.newButton(Icon.of(new ItemStack(Items.REDSTONE), false), () -> Tooltip.create(Component.translatable(NDatabase.GUI.Elements.DropPanel.RedstoneSensitivePanel.DESCRIPTION_REQUIRED_SIGNAL_DISABLED))).
								pressAction(but -> 
								{
									CompoundTag tag = new CompoundTag();
									
									tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.RedstoneSensitive.REDSTONE_MOD, 2);
									tag.putInt("x", tile.getBlockPos().getX());
									tag.putInt("y", tile.getBlockPos().getY());
									tag.putInt("z", tile.getBlockPos().getZ());
									
									sendUpdateToServer(tag);
								}).build()).
					finishRadioButton()));
		}).orElse(null);
	}
	
	
	protected DropPanel addPanelSwitcherDropPanel(BlockEntity be)
	{
		DropPanel panel = null;
		
		if (this.panelList.size() > 1)
		{
			int countInRow = 2;
			int rowCount = Mth.ceil((float)this.panelList.size() / countInRow);
			
			RadioButton button = RadioButton.newRadioButton(countInRow, 2).
					setPos(5, 7).
					setSize((countInRow * 20) + ((countInRow - 1) * 2), (rowCount * 20) + ((rowCount - 1) * 2)).
					setCurrentButtonId(this.currentPanel).
					build();
			
			CustomCheckbox main = RadioButton.newButton(Icon.of(new ItemStack(be.getBlockState().getBlock()), false), 
					() -> Tooltip.create(Component.translatable(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_NAME))).
					pressAction(but -> 
					{
						this.currentPanel = 0;
					}).build();
			
			CustomCheckbox itemFilter = FilterHelper.getItemFilter(be).map(handler -> RadioButton.newButton(Icon.of(ICON_FILTER_ITEM, 0, 0, 0, 16, 16, 16, 16), 
					() -> Tooltip.create(Component.translatable(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_FILTER_ITEM))).
					pressAction(but -> 
					{
						this.currentPanel = 10;
					}).build()).orElse(null);
			
			CustomCheckbox fluidFilter = FilterHelper.getFluidFilter(be).map(handler -> RadioButton.newButton(Icon.of(new ItemStack(Items.BUCKET), false), 
					() -> Tooltip.create(Component.translatable(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_FILTER_FLUID))).
					pressAction(but -> 
					{
						this.currentPanel = 11;
					}).build()).orElse(null);
			
			CustomCheckbox vimFilter = FilterHelper.getVimFilter(be).map(handler -> RadioButton.newButton(Icon.of(StringHelper.getLocFStr("textures/misc/lightning.png"), 5, 0, 0, 16, 16, 16, 64), 
					() -> Tooltip.create(Component.translatable(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_FILTER_VIM))).
					pressAction(but -> 
					{
						this.currentPanel = 12;
					}).build()).orElse(null);

			
			panel = addDropPanel(new DropPanel(button.getWidth() + 10, button.getHeight() + 15, Side.LEFT, false, new Color(40, 40, 150), 
					() -> button.getButtons().get(button.currentButtonId).getIcon(), 
					() -> Tooltip.create(Component.translatable(NDatabase.GUI.Elements.DropPanel.PanelSwitcherPanel.DESCRIPTION_MAIN,
							be.getBlockState().getBlock().asItem().getDescription()))).
				addWidget(button.
							addButton(main)));
			
			if (itemFilter != null)
				button.addButton(itemFilter);
			if (fluidFilter != null)
				button.addButton(fluidFilter);
			if (vimFilter != null)
				button.addButton(vimFilter);
			
			button.finishRadioButton();
		}
		
		return panel;
	}
	
	protected Panel addItemFilterPanel(BlockEntity be)
	{
		Panel panel = new Panel(10, this.getGuiLeft(), this.getGuiTop(), this.getXSize(), this.getYSize() - (this.getYSize() / 2) - 10 );
		FilterHelper.getItemFilter(be).ifPresent(filter -> panel.
				addWidget(new ChangeSizeButton(this.getGuiLeft() + 115, this.getGuiTop() + 5, 0, 64, filter.getExtraction(), but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Capabilities.Filter.MAX_EXTRACTING_STACK, but.getCurrentValue());
				
					sendItemFilterUpdate(tag, be);
				}, 
				new ButtonCtx(() -> "-", () -> NDatabase.GUI.Filter.Description.EXTRACTING_STACK_DECREASE),
				new ButtonCtx(() -> "+", () -> NDatabase.GUI.Filter.Description.EXTRACTING_STACK_INCREASE))).
				addWidget(new ChangeSizeButton(this.getGuiLeft() + 115, this.getGuiTop() + 38, 0, 64 * ItemHelper.getItemHandler(be).map(handler -> handler.getSlots()).orElse(9), filter.getMaxInInventory(), but ->
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Capabilities.Filter.MAX_AMOUNT_IN, but.getCurrentValue());
					
					sendItemFilterUpdate(tag, be);
				},
				new ButtonCtx(() -> "-", () -> NDatabase.GUI.Filter.Description.AMOUNT_IN_DECREASE),
				new ButtonCtx(() -> "+", () -> NDatabase.GUI.Filter.Description.AMOUNT_IN_INCREASE))).
				addWidget(new IconCheckbox(this.getGuiLeft() + 15, this.getGuiTop() + 10, 20, 20, filter.isWhitelist(), FILTER_WHITELIST, but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putBoolean(NDatabase.Capabilities.Filter.WHITELIST, but.selected());
					
					sendItemFilterUpdate(tag, be);
				}, () -> Tooltip.create(Component.translatable(filter.isWhitelist() ? NDatabase.GUI.Filter.Description.WHITELIST : NDatabase.GUI.Filter.Description.BLACKLIST)
						))).
				addWidget(new IconCheckbox(this.getGuiLeft() + 15, this.getGuiTop() + 40, 20, 20, filter.isModOwner(), FILTER_MOD_OWNER, but ->
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putBoolean(NDatabase.Capabilities.Filter.MOD_OWNER, but.selected());
					
					sendItemFilterUpdate(tag, be);
				}, () -> Tooltip.create(Component.translatable(filter.isModOwner() ? NDatabase.GUI.Filter.Description.MOD_OWNER : NDatabase.GUI.Filter.Description.MOD_OWNER_IGNORE)
						))).
				addWidget(new IconCheckbox(this.getGuiLeft() + 40, this.getGuiTop() + 25, 20, 20, filter.isCheckTag(), FILTER_TAG, but ->
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putBoolean(NDatabase.Capabilities.Filter.CHECK_TAG, but.selected());
					
					sendItemFilterUpdate(tag, be);
				}, () -> Tooltip.create(Component.translatable(filter.isCheckTag() ? NDatabase.GUI.Filter.Description.TAG_USE : NDatabase.GUI.Filter.Description.TAG_IGNORE)
						))));
		
		return addPanel(panel);
	}

	protected Panel addFluidFilterPanel(BlockEntity be)
	{
		Panel panel = new Panel(11, this.getGuiLeft(), this.getGuiTop(), this.getXSize(), this.getYSize() - (this.getYSize() / 2) - 10 );
		FilterHelper.getFluidFilter(be).ifPresent(filter -> panel.
				addWidget(new ChangeSizeButton(this.getGuiLeft() + 115, this.getGuiTop() + 5, 0, 1000, filter.getExtraction(), but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Capabilities.Filter.MAX_EXTRACTING_STACK, but.getCurrentValue());
					
					sendFluidFilterUpdate(tag, be);
				}, 
				new ButtonCtx(() -> "-", () -> NDatabase.GUI.Filter.Description.EXTRACTING_STACK_DECREASE),
				new ButtonCtx(() -> "+", () -> NDatabase.GUI.Filter.Description.EXTRACTING_STACK_INCREASE))).
				addWidget(new ChangeSizeButton(this.getGuiLeft() + 115, this.getGuiTop() + 38, 0, FluidHelper.getFluidHandler(be).map(handler -> handler.getTanks() * handler.getTankCapacity(0)).orElse(10000), filter.getMaxInInventory(), but ->
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Capabilities.Filter.MAX_AMOUNT_IN, but.getCurrentValue());
					
					sendFluidFilterUpdate(tag, be);
				},
				new ButtonCtx(() -> "-", () -> NDatabase.GUI.Filter.Description.AMOUNT_IN_DECREASE),
				new ButtonCtx(() -> "+", () -> NDatabase.GUI.Filter.Description.AMOUNT_IN_INCREASE))).
				addWidget(new IconCheckbox(this.getGuiLeft() + 15, this.getGuiTop() + 10, 20, 20, filter.isWhitelist(), FILTER_WHITELIST, but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putBoolean(NDatabase.Capabilities.Filter.WHITELIST, but.selected());
					
					sendFluidFilterUpdate(tag, be);
				}, () -> Tooltip.create(Component.translatable(filter.isWhitelist() ? NDatabase.GUI.Filter.Description.WHITELIST : NDatabase.GUI.Filter.Description.BLACKLIST)
						))).
				addWidget(new IconCheckbox(this.getGuiLeft() + 15, this.getGuiTop() + 40, 20, 20, filter.isModOwner(), FILTER_MOD_OWNER, but ->
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putBoolean(NDatabase.Capabilities.Filter.MOD_OWNER, but.selected());
					
					sendFluidFilterUpdate(tag, be);
				}, () -> Tooltip.create(Component.translatable(filter.isModOwner() ? NDatabase.GUI.Filter.Description.MOD_OWNER : NDatabase.GUI.Filter.Description.MOD_OWNER_IGNORE)
						))).
				addWidget(new IconCheckbox(this.getGuiLeft() + 40, this.getGuiTop() + 25, 20, 20, filter.isCheckTag(), FILTER_TAG, but ->
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putBoolean(NDatabase.Capabilities.Filter.CHECK_TAG, but.selected());
					
					sendFluidFilterUpdate(tag, be);
				}, () -> Tooltip.create(Component.translatable(filter.isCheckTag() ? NDatabase.GUI.Filter.Description.TAG_USE : NDatabase.GUI.Filter.Description.TAG_IGNORE)
						))));
		
		return addPanel(panel);

	}
	
	protected Panel addVimFilterPanel(BlockEntity be)
	{
		Panel panel = new Panel(12, this.getGuiLeft(), this.getGuiTop(), this.getXSize(), this.getYSize() - (this.getYSize() / 2) - 10 );
		FilterHelper.getVimFilter(be).ifPresent(filter -> panel.
				addWidget(new ChangeSizeButton(this.getGuiLeft() + 115, this.getGuiTop() + 5, 0, 1000, filter.getExtraction(), but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Capabilities.Filter.MAX_EXTRACTING_STACK, but.getCurrentValue());
				
					sendVimFilterUpdate(tag, be);
				}, 
				new ButtonCtx(() -> "-", () -> NDatabase.GUI.Filter.Description.EXTRACTING_STACK_DECREASE),
				new ButtonCtx(() -> "+", () -> NDatabase.GUI.Filter.Description.EXTRACTING_STACK_INCREASE))).
				addWidget(new ChangeSizeButton(this.getGuiLeft() + 115, this.getGuiTop() + 38, 0, VimHelper.getVimHandler(be).map(handler -> handler.getMaxEnergyStored()).orElse(10000), filter.getMaxInInventory(), but ->
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Capabilities.Filter.MAX_AMOUNT_IN, but.getCurrentValue());
					
					sendVimFilterUpdate(tag, be);
				},
				new ButtonCtx(() -> "-", () -> NDatabase.GUI.Filter.Description.AMOUNT_IN_DECREASE),
				new ButtonCtx(() -> "+", () -> NDatabase.GUI.Filter.Description.AMOUNT_IN_INCREASE))));
	
		return addPanel(panel);
	}
	
	@Override
	protected void renderLabels(PoseStack stack, int x_pos, int y_pos) 
	{
	}
	
	@Override
	public void renderSlot(PoseStack stack, Slot slot) 
	{
	      int i = slot.x;
	      int j = slot.y;
	      ItemStack itemstack = slot.getItem();
	      boolean flag = false;
	      boolean flag1 = slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
	      ItemStack itemstack1 = this.menu.getCarried();
	      String s = null;
	      if (slot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !itemstack.isEmpty()) 
	      {
	         itemstack = itemstack.copy();
	         itemstack.setCount(itemstack.getCount() / 2);
	      } 
	      else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !itemstack1.isEmpty()) 
	      {
	         if (this.quickCraftSlots.size() == 1) 
	         {
	            return;
	         }

	         if (AbstractContainerMenu.canItemQuickReplace(slot, itemstack1, true) && this.menu.canDragTo(slot)) 
	         {
	            itemstack = itemstack1.copy();
	            flag = true;
	            AbstractContainerMenu.getQuickCraftSlotCount(this.quickCraftSlots, this.quickCraftingType, itemstack, slot.getItem().isEmpty() ? 0 : slot.getItem().getCount());
	            int k = Math.min(itemstack.getMaxStackSize(), slot.getMaxStackSize(itemstack));
	            if (itemstack.getCount() > k) {
	               s = ChatFormatting.YELLOW.toString() + k;
	               itemstack.setCount(k);
	            }
	         } 
	         else 
	         {
	            this.quickCraftSlots.remove(slot);
	            this.recalculateQuickCraftRemaining();
	         }
	      }

	      stack.pushPose();
	      stack.translate(0.0F, 0.0F, 100.0F);
	      if (slot.isActive()) 
	      {
	         Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
	         if (pair != null) 
	         {
	            TextureAtlasSprite textureatlassprite = this.minecraft.getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
	            RenderSystem.setShaderTexture(0, textureatlassprite.atlasLocation());
	            blit(stack, i-1, j-1, 0, 18, 18, textureatlassprite);
	         }
	      }

	      if (!flag1) 
	      {
	         if (flag) 
	         {
	        	 fill(stack, i, j, i + 16, j + 16, -2130706433);
	         }

	         RenderSystem.enableDepthTest();
	         if (slot instanceof ItemHandlerGhost)
	         {
	        	 RenderHelper.renderFakeItemTransparent(itemstack, getGuiLeft() + i, getGuiTop() + j, 0.5f);
	         }
	         else 
	         {
		         this.itemRenderer.renderAndDecorateItem(stack, this.minecraft.player, itemstack, i, j, slot.x + slot.y * this.imageWidth);
		         this.itemRenderer.renderGuiItemDecorations(stack, this.font, itemstack, i, j, s);
	         }
	      }
	      stack.popPose();
	}
	
	@Override
	public void recalculateQuickCraftRemaining() 
	{
		ItemStack itemstack = this.menu.getCarried();
		if (!itemstack.isEmpty() && this.isQuickCrafting) 
		{
			if (this.quickCraftingType == 2) 
			{
				this.quickCraftingRemainder = itemstack.getMaxStackSize();
		    } 
			else 
			{
				this.quickCraftingRemainder = itemstack.getCount();

		        for(Slot slot : this.quickCraftSlots) 
		        {
		        	ItemStack itemstack1 = itemstack.copy();
		            ItemStack itemstack2 = slot.getItem();
		            int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
		            AbstractContainerMenu.getQuickCraftSlotCount(this.quickCraftSlots, this.quickCraftingType, itemstack1, i);
		            int j = Math.min(itemstack1.getMaxStackSize(), slot.getMaxStackSize(itemstack1));
		            if (itemstack1.getCount() > j) 
		            {
		            	itemstack1.setCount(j);
		            }

		               this.quickCraftingRemainder -= itemstack1.getCount() - i;
		        }

			}
		}
	}
	
	protected void sendItemFilterUpdate(CompoundTag message, BlockEntity be)
	{
		message.putInt("x", be.getBlockPos().getX());
		message.putInt("y", be.getBlockPos().getY());
		message.putInt("z", be.getBlockPos().getZ());
		
		message.putString("type", "item");
		
		sendUpdateToServer(message);
	}
	
	protected void sendFluidFilterUpdate(CompoundTag message, BlockEntity be)
	{
		message.putInt("x", be.getBlockPos().getX());
		message.putInt("y", be.getBlockPos().getY());
		message.putInt("z", be.getBlockPos().getZ());
		
		message.putString("type", "fluid");
		
		sendUpdateToServer(message);
	}

	protected void sendVimFilterUpdate(CompoundTag message, BlockEntity be)
	{
		message.putInt("x", be.getBlockPos().getX());
		message.putInt("y", be.getBlockPos().getY());
		message.putInt("z", be.getBlockPos().getZ());
		
		message.putString("type", "vim");
		
		sendUpdateToServer(message);
	}

	protected void sendUpdateToServer(CompoundTag message)
	{
		NNetworkEngine.packetHandler.sendToServer(new MessageContainerUpdate(menu.containerId, message));
	}
	
}	

/**
 * @author ArcAnc
 * Created at: 2023-01-03
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.screen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.arcanc.nedaire.content.container.NSlot;
import com.arcanc.nedaire.content.container.widget.DropPanel;
import com.arcanc.nedaire.content.container.widget.Panel;
import com.arcanc.nedaire.content.container.widget.info.InfoArea;
import com.arcanc.nedaire.content.network.NNetworkEngine;
import com.arcanc.nedaire.content.network.messages.MessageContainerUpdate;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class NContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> 
{
	public static final ResourceLocation MIDDLE = StringHelper.getLocFStr("textures/" + NDatabase.GUI.Background.Textures.MIDDLE + ".png"); 
	public static final ResourceLocation MIDDLE_TOP = StringHelper.getLocFStr("textures/" + NDatabase.GUI.Background.Textures.MIDDLE_TOP + ".png"); 
	public static final ResourceLocation MIDDLE_BOT = StringHelper.getLocFStr("textures/" + NDatabase.GUI.Background.Textures.MIDDLE_BOTTOM + ".png"); 
	public static final ResourceLocation MIDDLE_LEFT = StringHelper.getLocFStr("textures/" + NDatabase.GUI.Background.Textures.MIDDLE_LEFT + ".png"); 
	public static final ResourceLocation MIDDLE_RIGHT = StringHelper.getLocFStr("textures/" + NDatabase.GUI.Background.Textures.MIDDLE_RIGHT + ".png"); 
	public static final ResourceLocation LEFT_TOP = StringHelper.getLocFStr("textures/" + NDatabase.GUI.Background.Textures.LEFT_TOP + ".png"); 
	public static final ResourceLocation LEFT_BOT = StringHelper.getLocFStr("textures/" + NDatabase.GUI.Background.Textures.LEFT_BOTTOM + ".png"); 
	public static final ResourceLocation RIGHT_TOP = StringHelper.getLocFStr("textures/" + NDatabase.GUI.Background.Textures.RIGHT_TOP + ".png"); 
	public static final ResourceLocation RIGHT_BOT = StringHelper.getLocFStr("textures/" + NDatabase.GUI.Background.Textures.RIGHT_BOTTOM + ".png"); 
	
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
		for (int q = 0; q < dropPanelList.size(); q++)
		{
			DropPanel panel = dropPanelList.get(q);
			DropPanel prevPanel = q > 0 ? dropPanelList.get(q - 1) : null;
			
			Rect2i coord = prevPanel == null ? new Rect2i(0, 0, 0, 0) : new Rect2i(prevPanel.getX(), prevPanel.getY(), prevPanel.getWidth(), prevPanel.getHeight());
			Rect2i relativeCoord = new Rect2i(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight());
			panel.setX(q == 0 ? this.getGuiLeft() + 2 : coord.getX() + coord.getWidth());
			panel.setY(this.getGuiTop() - panel.getHeight() + 3);
		
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
		blit(stack, x_pos, y_pos, this.getBlitOffset(), 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_TOP);
		blit(stack, x_pos + 8, y_pos, this.getBlitOffset(), 0, 0, this.imageWidth - 16, 8, 8, 8);
		
		RenderSystem.setShaderTexture(0, NContainerScreen.RIGHT_TOP);
		blit(stack, x_pos + this.imageWidth - 8, y_pos, this.getBlitOffset(), 0, 0, 8, 8, 8, 8);
		
		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_LEFT);
		blit(stack, x_pos, y_pos + 8, this.getBlitOffset(), 0, 0, 8, this.imageHeight - 16, 8, 8);
		
		RenderSystem.setShaderTexture(0, NContainerScreen.LEFT_BOT);
		blit(stack, x_pos, y_pos + this.imageHeight - 8, this.getBlitOffset(), 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_BOT);
		blit(stack, x_pos + 8, y_pos + this.imageHeight - 8, this.getBlitOffset(), 0, 0, this.imageWidth - 16, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.RIGHT_BOT);
		blit(stack, x_pos + this.imageWidth - 8, y_pos + this.imageHeight - 8, this.getBlitOffset(), 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE_RIGHT);
		blit(stack, x_pos + this.imageWidth - 8, y_pos + 8, this.getBlitOffset(), 0, 0, 8, this.imageHeight - 16, 8, 8);

		RenderSystem.setShaderTexture(0, NContainerScreen.MIDDLE);
		blit(stack, x_pos + 8, y_pos + 8, this.getBlitOffset(), 0, 0, this.imageWidth - 16, this.imageHeight - 16, 8, 8);

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

	      this.setBlitOffset(100);
	      this.itemRenderer.blitOffset = 100.0F;
	      if (slot.isActive()) 
	      {
	         Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
	         if (pair != null) 
	         {
	            TextureAtlasSprite textureatlassprite = this.minecraft.getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
	            RenderSystem.setShaderTexture(0, textureatlassprite.atlasLocation());
	            blit(stack, i-1, j-1, this.getBlitOffset(), 18, 18, textureatlassprite);
	         }
	      }

	      if (!flag1) 
	      {
	         if (flag) 
	         {
	        	 fill(stack, i, j, i + 16, j + 16, -2130706433);
	         }

	         RenderSystem.enableDepthTest();
	         this.itemRenderer.renderAndDecorateItem(this.minecraft.player, itemstack, i, j, slot.x + slot.y * this.imageWidth);
	         this.itemRenderer.renderGuiItemDecorations(this.font, itemstack, i, j, s);
	      }

	      this.itemRenderer.blitOffset = 0.0F;
	      this.setBlitOffset(0);
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
	
	protected void SendUpdateToServer(CompoundTag message)
	{
		NNetworkEngine.packetHandler.sendToServer(new MessageContainerUpdate(menu.containerId, message));
	}
	
}	

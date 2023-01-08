/**
 * @author ArcAnc
 * Created at: 2023-01-03
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.screen;

import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
	
	public NContainerScreen(T slots, Inventory player, Component title) 
	{
		super(slots, player, title);
		this.inventoryLabelY = this.imageHeight - 91;
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int x, int y) 
	{
		renderBackground(stack);
		int x_pos = this.leftPos;
		int	y_pos = this.topPos;

		stack.pushPose();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		RenderSystem.setShaderTexture(0, LEFT_TOP);
		blit(stack, x_pos, y_pos, this.getBlitOffset(), 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, MIDDLE_TOP);
		blit(stack, x_pos + 8, y_pos, this.getBlitOffset(), 0, 0, imageWidth - 8, 8, 8, 8);
		
		RenderSystem.setShaderTexture(0, RIGHT_TOP);
		blit(stack, x_pos + this.imageWidth,  y_pos, this.getBlitOffset(), 0, 0, 8, 8, 8, 8);
		
		RenderSystem.setShaderTexture(0, MIDDLE_LEFT);
		blit(stack, x_pos, y_pos + 8, this.getBlitOffset(), 0, 0, 8, imageHeight - 8, 8, 8);
		
		RenderSystem.setShaderTexture(0, LEFT_BOT);
		blit(stack, x_pos, y_pos + this.imageHeight, this.getBlitOffset(), 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, MIDDLE_BOT);
		blit(stack, x_pos + 8, y_pos + this.imageHeight, this.getBlitOffset(), 0, 0, this.imageWidth - 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, RIGHT_BOT);
		blit(stack, x_pos + this.imageWidth, y_pos + this.imageHeight, this.getBlitOffset(), 0, 0, 8, 8, 8, 8);

		RenderSystem.setShaderTexture(0, MIDDLE_RIGHT);
		blit(stack, x_pos + this.imageWidth, y_pos + 8, this.getBlitOffset(), 0, 0, 8, imageHeight - 8, 8, 8);

		RenderSystem.setShaderTexture(0, MIDDLE);
		blit(stack, x_pos + 8, y_pos + 8, this.getBlitOffset(), 0, 0, imageWidth - 8, imageHeight - 8, 8, 8);

		stack.popPose();
	    
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
	
}	

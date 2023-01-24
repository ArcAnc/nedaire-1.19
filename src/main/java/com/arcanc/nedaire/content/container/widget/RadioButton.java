/**
 * @author ArcAnc
 * Created at: 2023-01-19
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.widget;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Vector2i;

import com.arcanc.nedaire.content.container.widget.RadioButton.CustomCheckbox.ButtonBuilder;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.RenderHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RadioButton extends Button 
{

	public static final ResourceLocation TEXTURE = StringHelper.getLocFStr(NDatabase.GUI.getTexturePath(NDatabase.GUI.Elements.RadioButton.PATH));
	public static final ResourceLocation BACKGROUND = StringHelper.getLocFStr(NDatabase.GUI.getTexturePath(NDatabase.GUI.Elements.RadioButton.BACKGROUND));
	
	protected Supplier<Component> mainLabel;
	protected List<CustomCheckbox> buttons = new LinkedList<>();
	public int currentButtonId = 0;
	private int countInRow = 1;
	private Vector2i distanceBetweenButtons = new Vector2i();
	
	public RadioButton(int x, int y, int width, int height, int countInRow, Vector2i distanceBetweenButtons, int currentbuttonId) 
	{
		super(x, y, width, height, Component.empty(), but -> {}, Button.DEFAULT_NARRATION);
		this.countInRow = countInRow;
		this.distanceBetweenButtons = distanceBetweenButtons;
		this.currentButtonId = currentbuttonId;
	}
	
	protected RadioButton(RadioButtonBuilder builder)
	{
		this(builder.pos.getX(), builder.pos.getY(), builder.pos.getWidth(), builder.pos.getHeight(), builder.countInRow, builder.distanceBetweenButtons, builder.currentButtonId);
	}

	public static RadioButtonBuilder newRadioButton (int countInRow, int distanceBetweenButtons)
	{
		return new RadioButtonBuilder(countInRow, new Vector2i(distanceBetweenButtons, distanceBetweenButtons));
	}
	
	public static RadioButtonBuilder newRadioButton (int countInRow, Vector2i distanceBetweenButtons)
	{
		return new RadioButtonBuilder(countInRow, distanceBetweenButtons);
	}
	
	public static ButtonBuilder newButton(ItemStack icon, Supplier<Tooltip> tooltip)
	{
		return new ButtonBuilder(icon, tooltip);
	}
	
	public RadioButton addButton(CustomCheckbox button)
	{
		this.buttons.add(button);
		return this;
	}
	
	/**
	 * FIXME: fix vertical positioning 
	 */
	public RadioButton finishRadioButton()
	{
		
		int buttonWidth = (this.width - (countInRow > 1 ? (distanceBetweenButtons.x() * (countInRow - 1)) : 0)) / countInRow ;
		int rowsCount = Mth.ceil((float)((this.buttons.size() * buttonWidth) + (distanceBetweenButtons.x() * (buttons.size() - 1))) / ((countInRow * buttonWidth) + (countInRow > 1 ? (distanceBetweenButtons.x() * (countInRow - 1)) : 0)));
		int buttonHeight = (this.height - (rowsCount > 1 ? (distanceBetweenButtons.y() * (rowsCount - 1)) : 0)) / rowsCount;
		
		for (int q = 0; q < this.buttons.size(); q++)
		{
			CustomCheckbox cc = buttons.get(q);
			
			cc.setX(this.getX() + (q % countInRow) * buttonWidth + (q % countInRow) * distanceBetweenButtons.x());
			cc.setY(this.getY() + (q / (rowsCount == 1 ? countInRow : rowsCount)) * buttonHeight + (q / (rowsCount == 1 ? countInRow : rowsCount)) * distanceBetweenButtons.y());
			cc.setWidth(buttonWidth);
			cc.setHeight(buttonHeight);
		}
		
		buttons.get(currentButtonId).selected = true;
		return this;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int buttonId) 
	{
		boolean clicked = false;
		
		if (isActive())
		{
			if (!buttons.isEmpty())
			{
				for (int q = 0; q < buttons.size(); q++)
				{
					CustomCheckbox cc = buttons.get(q);
					if (cc.mouseClicked(mouseX, mouseY, buttonId))
					{
						clicked = true;
						if (q != currentButtonId)
						{
							currentButtonId = q;
							for (CustomCheckbox box : buttons)
							{
								box.selected = false;
							}
							buttons.get(q).selected = true;
						}
					}
				}
			}
		}
		return clicked;
	}
	
	@Override
	public boolean keyPressed(int buttonId, int mouseX, int mouseY) 
	{
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public static class RadioButtonBuilder
	{
		private Rect2i pos = new Rect2i(0, 0, 0, 0);
		
		private int currentButtonId = 0;
		private int countInRow = 1;
		private Vector2i distanceBetweenButtons;
		
		public RadioButtonBuilder(int countInRow, Vector2i distanceBetweenButtons) 
		{
			this.countInRow = countInRow;
			this.distanceBetweenButtons = distanceBetweenButtons;
		}
		
		public RadioButtonBuilder setPos(int x, int y)
		{
			pos.setPosition(x, y);
			return this;
		}
		
		public RadioButtonBuilder setSize(int width, int height)
		{
			pos.setWidth(width);
			pos.setHeight(height);
			return this;
		}
		
		public RadioButtonBuilder setCurrentButtonId(int id)
		{
			this.currentButtonId = id;
			return this;
		}
		
		public RadioButton build()
		{
			return this.build(RadioButton :: new);
		}

		private RadioButton build(Function<RadioButtonBuilder, RadioButton> builder) 
		{
			return builder.apply(this);
		}
		
	}
	
	@Override
	public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
	    stack.pushPose(); 
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
	    RenderSystem.setShaderTexture(0, BACKGROUND);
	    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.75f);
	    RenderSystem.enableBlend();
	    RenderSystem.defaultBlendFunc();
	    RenderSystem.enableDepthTest();	    
	    
		blit(stack, this.getX() - 3, this.getY() - 2, this.width + 3, this.height + 4, 0.0F, 0.0F, 16, 16, 16, 16);
	    
	    stack.popPose();
	}
	
	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
	{
		if (this.visible) 
		{
			this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
			this.renderButton(stack, mouseX, mouseY, partialTicks);
			if (!buttons.isEmpty())
			{
				finishRadioButton();
				for (CustomCheckbox cc : buttons)
				{
					cc.render(stack, mouseX, mouseY, partialTicks);
				}
			}
		}
	}
	
	public static class CustomCheckbox extends AbstractButton
	{
		
		private Supplier<Tooltip> tooltip;
		private boolean selected = false;
		private ItemStack icon;
		private CustomCheckbox.OnPress press;
		
		public CustomCheckbox(Rect2i pos, boolean active, Supplier<Tooltip> tooltip, ItemStack icon, CustomCheckbox.OnPress press) 
		{
			super(pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), Component.empty());
			
			this.tooltip = tooltip;
			this.icon = icon;
			this.press = press;
		}

		@Override
		public void onPress() 
		{
			selected = true;
			press.onPress(this);
		}
		
		public boolean isSelected() 
		{
			return selected;
		}

		@Override
		public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
		{
			
			stack.pushPose();
			
			RenderSystem.setShaderTexture(0, TEXTURE);
			RenderSystem.enableDepthTest();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
			
			blit(stack, this.getX(), this.getY(), this.width, this.height, this.isHoveredOrFocused() ? 20.0F : 0.0F, this.selected ? 20.0F : 0.0F, 20, 20, 64, 64);
			
			RenderHelper.renderItemStack(stack, icon, this.getX() + this.getWidth() / 2 - 8, this.getY() + this.getHeight() / 2 - 8, false);
			
			RenderSystem.disableBlend();
			stack.popPose();
		}
		
		@Override
		public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) 
		{
			if (this.visible) 
			{
				this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
				this.renderButton(stack, mouseX, mouseY, partialTicks);
				this.renderTootip();
			}
		}
		
		private void renderTootip() 
		{
			if (this.tooltip != null) 
		    {
				if (isHoveredOrFocused())
				{
					Minecraft mc = RenderHelper.mc();
					Screen screen = mc.screen;
			        if (screen != null) 
			        {
			        	screen.setTooltipForNextRenderPass(this.tooltip.get(), this.createTooltipPositioner(), this.isFocused());
			        }
				}
	       }
			
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput p_259858_) 
		{
			
		}
		
		@Override
		public boolean keyPressed(int mouseX, int mouseY, int buttonId) 
		{
			return false;
		}
		
		public static class ButtonBuilder 
		{
			private Rect2i bounds = new Rect2i(0, 0, 0, 0);
		
			private Supplier<Tooltip> tooltip;
			private ItemStack icon;
			private CustomCheckbox.OnPress pressAction;
			
			private ButtonBuilder(ItemStack icon, Supplier<Tooltip> tooltip)
			{
				this.tooltip = tooltip;
				this.icon = icon;
			}
			
			public ButtonBuilder setPos(int x, int y)
			{
				bounds.setPosition(x, y);
				return this;
			}
			
			public ButtonBuilder setSize(int width, int height)
			{
				bounds.setWidth(width);
				bounds.setHeight(height);
				return this;
			}
			
			public ButtonBuilder pressAction(CustomCheckbox.OnPress press)
			{
				pressAction = press;
				return this;
			}
			
			public CustomCheckbox build()
			{
				return new CustomCheckbox(bounds, false, tooltip, icon, pressAction);
			}
		}

		@OnlyIn(Dist.CLIENT)
		public interface OnPress 
		{
			void onPress(CustomCheckbox button);
		}
	}
}

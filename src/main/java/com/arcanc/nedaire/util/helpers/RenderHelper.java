/**
 * @author ArcAnc
 * Created at: 2022-09-20
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.util.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.extensions.common.IClientItemExtensions.FontContext;

public class RenderHelper 
{
	public static Minecraft mc()
	{
		return Minecraft.getInstance();
	}
	
	public static ItemRenderer renderItem()
	{
		return mc().getItemRenderer();
	}
	
	public static void renderItemStack(PoseStack transform, ItemStack stack, int x, int y, boolean overlay)
	{
		renderItemStack(transform, stack, x, y, overlay, null);
	}

	public static void renderItemStack(PoseStack transform, ItemStack stack, int x, int y, boolean overlay, String count)
	{
		if(!stack.isEmpty())
		{
			// Include the matrix transformation
			PoseStack modelViewStack = RenderSystem.getModelViewStack();
			modelViewStack.pushPose();
			modelViewStack.mulPoseMatrix(transform.last().pose());
			RenderSystem.applyModelViewMatrix();

			// Counteract the zlevel increase, because multiplied with the matrix, it goes out of view
			ItemRenderer itemRenderer = renderItem();
			itemRenderer.blitOffset -= 50;
			itemRenderer.renderAndDecorateItem(stack, x, y);
			itemRenderer.blitOffset += 50;

			if(overlay)
			{
				// Use the Item's font renderer, if available
				Font font = IClientItemExtensions.of(stack.getItem()).getFont(stack, FontContext.ITEM_COUNT);
				font = font!=null?font: Minecraft.getInstance().font;
				itemRenderer.renderGuiItemDecorations(font, stack, x, y, count);
			}
			modelViewStack.popPose();
			RenderSystem.applyModelViewMatrix();
		}
	}
	
	
/*	private static Font tooltipFont = null;
	private static ItemStack tooltipStack = ItemStack.EMPTY;
	
	public static void renderTooltip(PoseStack p_96566_, ItemStack p_96567_, int p_96568_, int p_96569_) 
	{
		tooltipStack = p_96567_;
		renderTooltip(p_96566_, getTooltipFromItem(p_96567_), p_96567_.getTooltipImage(), p_96568_, p_96569_);
		tooltipStack = ItemStack.EMPTY;
	}

	public static void renderTooltip(PoseStack poseStack, List<Component> textComponents, Optional<TooltipComponent> tooltipComponent, int x, int y, ItemStack stack) 
	{
		renderTooltip(poseStack, textComponents, tooltipComponent, x, y, null, stack);
	}
	
	public static void renderTooltip(PoseStack poseStack, List<Component> textComponents, Optional<TooltipComponent> tooltipComponent, int x, int y, @Nullable Font font) 
	{
		renderTooltip(poseStack, textComponents, tooltipComponent, x, y, font, ItemStack.EMPTY);
	}
	
	public static void renderTooltip(PoseStack poseStack, List<Component> textComponents, Optional<TooltipComponent> tooltipComponent, int x, int y, @Nullable Font font, ItemStack stack) 
	{
		tooltipFont = font;
		tooltipStack = stack;
		renderTooltip(poseStack, textComponents, tooltipComponent, x, y);
		tooltipFont = null;
		tooltipStack = ItemStack.EMPTY;
	}
	
	public static void renderTooltip(PoseStack p_169389_, List<Component> p_169390_, Optional<TooltipComponent> p_169391_, int p_169392_, int p_169393_) 
	{
		Minecraft mc = mc();
		Screen screen = mc.screen;
		if (screen == null)
			return;
		List<ClientTooltipComponent> list = net.minecraftforge.client.ForgeHooksClient.gatherTooltipComponents(tooltipStack, p_169390_, p_169391_, p_169392_, screen.width, screen.height, tooltipFont, mc.font);
		renderTooltipInternal(p_169389_, list, p_169392_, p_169393_);
	}

	public static List<Component> getTooltipFromItem(ItemStack p_96556_) 
	{
		Minecraft mc = mc();
		return p_96556_.getTooltipLines(mc.player, mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
	}

	public static void renderTooltip(PoseStack p_96603_, Component p_96604_, int p_96605_, int p_96606_) 
	{
		renderTooltip(p_96603_, Arrays.asList(p_96604_.getVisualOrderText()), p_96605_, p_96606_);
	}

	public static void renderComponentTooltip(PoseStack p_96598_, List<Component> p_96599_, int p_96600_, int p_96601_) 
	{
		Minecraft mc = mc();
		Screen screen = mc.screen;
		if (screen == null)
			return;
		List<ClientTooltipComponent> components = net.minecraftforge.client.ForgeHooksClient.gatherTooltipComponents(tooltipStack, p_96599_, p_96600_, screen.width, screen.height, tooltipFont, mc.font);
		renderTooltipInternal(p_96598_, components, p_96600_, p_96601_);
	}
	
	public static void renderComponentTooltip(PoseStack poseStack, List<? extends net.minecraft.network.chat.FormattedText> tooltips, int mouseX, int mouseY, ItemStack stack) 
	{
		renderComponentTooltip(poseStack, tooltips, mouseX, mouseY, null, stack);
	}
	
	public static void renderComponentTooltip(PoseStack poseStack, List<? extends net.minecraft.network.chat.FormattedText> tooltips, int mouseX, int mouseY, @Nullable Font font) 
	{
		renderComponentTooltip(poseStack, tooltips, mouseX, mouseY, font, ItemStack.EMPTY);
	}

	public static void renderComponentTooltip(PoseStack poseStack, List<? extends net.minecraft.network.chat.FormattedText> tooltips, int mouseX, int mouseY, @Nullable Font font, ItemStack stack) 
	{
		Minecraft mc = mc();
		Screen screen = mc.screen;
		if (screen == null)
			return;
		tooltipFont = font;
		tooltipStack = stack;
		List<ClientTooltipComponent> components = net.minecraftforge.client.ForgeHooksClient.gatherTooltipComponents(stack, tooltips, mouseX, screen.width, screen.height, tooltipFont, mc.font);
		renderTooltipInternal(poseStack, components, mouseX, mouseY);
		tooltipFont = null;
		tooltipStack = ItemStack.EMPTY;
	}

	public static void renderTooltip(PoseStack p_96618_, List<? extends FormattedCharSequence> p_96619_, int p_96620_, int p_96621_) 
	{
		renderTooltipInternal(p_96618_, p_96619_.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), p_96620_, p_96621_);
	}

	public static void renderTooltip(PoseStack poseStack, List<? extends FormattedCharSequence> lines, int x, int y, Font font) 
	{
		tooltipFont = font;
		renderTooltip(poseStack, lines, x, y);
		tooltipFont = null;
	}

	private static void renderTooltipInternal(PoseStack p_169384_, List<ClientTooltipComponent> p_169385_, int p_169386_, int p_169387_) 
	{
		Minecraft mc = mc();
		Screen screen = mc.screen;
		if (screen == null)
			return;
		if (!p_169385_.isEmpty()) 
		{
			net.minecraftforge.client.event.RenderTooltipEvent.Pre preEvent = net.minecraftforge.client.ForgeHooksClient.onRenderTooltipPre(tooltipStack, p_169384_, p_169386_, p_169387_, screen.width, screen.height, p_169385_, tooltipFont, mc.font);
		    if (preEvent.isCanceled()) 
		    	return;
		    int i = 0;
		    int j = p_169385_.size() == 1 ? -2 : 0;

		    for(ClientTooltipComponent clienttooltipcomponent : p_169385_) 
		    {
		    	int k = clienttooltipcomponent.getWidth(preEvent.getFont());
		        if (k > i) 
		        {
		        	i = k;
		        }

		        j += clienttooltipcomponent.getHeight();
		    }

		    int j2 = preEvent.getX() + 12;
		    int k2 = preEvent.getY() - 12;
		    if (j2 + i > screen.width) 
		    {
		    	j2 -= 28 + i;
		    }

		    if (k2 + j + 6 > screen.height) 
		    {
		    	k2 = screen.height - j - 6;
		    }

		    p_169384_.pushPose();
		    ItemRenderer itemRenderer = renderItem();
		    float f = itemRenderer.blitOffset;
		    itemRenderer.blitOffset = 400.0F;
		    Tesselator tesselator = Tesselator.getInstance();
		    BufferBuilder bufferbuilder = tesselator.getBuilder();
		    RenderSystem.setShader(GameRenderer::getPositionColorShader);
		    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		    Matrix4f matrix4f = p_169384_.last().pose();
		    net.minecraftforge.client.event.RenderTooltipEvent.Color colorEvent = net.minecraftforge.client.ForgeHooksClient.onRenderTooltipColor(tooltipStack, p_169384_, j2, k2, preEvent.getFont(), p_169385_);
		    fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 4, j2 + i + 3, k2 - 3, 400, colorEvent.getBackgroundStart(), colorEvent.getBackgroundStart());
		    fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 + j + 3, j2 + i + 3, k2 + j + 4, 400, colorEvent.getBackgroundEnd(), colorEvent.getBackgroundEnd());
		    fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 3, j2 + i + 3, k2 + j + 3, 400, colorEvent.getBackgroundStart(), colorEvent.getBackgroundEnd());
		    fillGradient(matrix4f, bufferbuilder, j2 - 4, k2 - 3, j2 - 3, k2 + j + 3, 400, colorEvent.getBackgroundStart(), colorEvent.getBackgroundEnd());
		    fillGradient(matrix4f, bufferbuilder, j2 + i + 3, k2 - 3, j2 + i + 4, k2 + j + 3, 400, colorEvent.getBackgroundStart(), colorEvent.getBackgroundEnd());
		    fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + j + 3 - 1, 400, colorEvent.getBorderStart(), colorEvent.getBorderEnd());
		    fillGradient(matrix4f, bufferbuilder, j2 + i + 2, k2 - 3 + 1, j2 + i + 3, k2 + j + 3 - 1, 400, colorEvent.getBorderStart(), colorEvent.getBorderEnd());
		    fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 3, j2 + i + 3, k2 - 3 + 1, 400, colorEvent.getBorderStart(), colorEvent.getBorderStart());
		    fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 + j + 2, j2 + i + 3, k2 + j + 3, 400, colorEvent.getBorderEnd(), colorEvent.getBorderEnd());
		    RenderSystem.enableDepthTest();
		    RenderSystem.disableTexture();
		    RenderSystem.enableBlend();
		    RenderSystem.defaultBlendFunc();
		    BufferUploader.drawWithShader(bufferbuilder.end());
		    RenderSystem.disableBlend();
		    RenderSystem.enableTexture();
		    MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		    p_169384_.translate(0.0D, 0.0D, 400.0D);
		    int l1 = k2;

		    for(int i2 = 0; i2 < p_169385_.size(); ++i2) 
		    {
		    	ClientTooltipComponent clienttooltipcomponent1 = p_169385_.get(i2);
		        clienttooltipcomponent1.renderText(preEvent.getFont(), j2, l1, matrix4f, multibuffersource$buffersource);
		        l1 += clienttooltipcomponent1.getHeight() + (i2 == 0 ? 2 : 0);
		    }

		    multibuffersource$buffersource.endBatch();
		    p_169384_.popPose();
		    l1 = k2;

		    for(int l2 = 0; l2 < p_169385_.size(); ++l2) 
		    {
		    	ClientTooltipComponent clienttooltipcomponent2 = p_169385_.get(l2);
		        clienttooltipcomponent2.renderImage(preEvent.getFont(), j2, l1, p_169384_, itemRenderer, 400);
		        l1 += clienttooltipcomponent2.getHeight() + (l2 == 0 ? 2 : 0);
		    }

		    itemRenderer.blitOffset = f;
		}
	}
	
	public static void fillGradient(Matrix4f p_93124_, BufferBuilder p_93125_, int p_93126_, int p_93127_, int p_93128_, int p_93129_, int p_93130_, int p_93131_, int p_93132_) 
	{
		float f = (float)(p_93131_ >> 24 & 255) / 255.0F;
		float f1 = (float)(p_93131_ >> 16 & 255) / 255.0F;
		float f2 = (float)(p_93131_ >> 8 & 255) / 255.0F;
		float f3 = (float)(p_93131_ & 255) / 255.0F;
		float f4 = (float)(p_93132_ >> 24 & 255) / 255.0F;
		float f5 = (float)(p_93132_ >> 16 & 255) / 255.0F;
		float f6 = (float)(p_93132_ >> 8 & 255) / 255.0F;
		float f7 = (float)(p_93132_ & 255) / 255.0F;
		p_93125_.vertex(p_93124_, (float)p_93128_, (float)p_93127_, (float)p_93130_).color(f1, f2, f3, f).endVertex();
		p_93125_.vertex(p_93124_, (float)p_93126_, (float)p_93127_, (float)p_93130_).color(f1, f2, f3, f).endVertex();
		p_93125_.vertex(p_93124_, (float)p_93126_, (float)p_93129_, (float)p_93130_).color(f5, f6, f7, f4).endVertex();
		p_93125_.vertex(p_93124_, (float)p_93128_, (float)p_93129_, (float)p_93130_).color(f5, f6, f7, f4).endVertex();
	}
*/
}

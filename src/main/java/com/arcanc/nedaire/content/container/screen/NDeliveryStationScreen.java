/**
 * @author ArcAnc
 * Created at: 2023-02-21
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.screen;

import com.arcanc.nedaire.content.container.menu.NDeliveryStationMenu;
import com.arcanc.nedaire.content.container.widget.Panel;
import com.arcanc.nedaire.content.container.widget.RadioButton;
import com.arcanc.nedaire.content.container.widget.RadioButton.CustomCheckbox;
import com.arcanc.nedaire.content.container.widget.icon.Icon;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NDeliveryStationScreen extends NContainerScreen<NDeliveryStationMenu> 
{

	public NDeliveryStationScreen(NDeliveryStationMenu slots, Inventory player, Component title) 
	{
		super(slots, player, title);

		this.imageHeight = 175;
		this.imageWidth =  185;	
	}
	
	@Override
	protected void init() 
	{
		super.init();
	
		addRedstoneSensitiveDropPanel(menu.be);
		
		
		RadioButton button = RadioButton.newRadioButton(3, 5).
				setPos(this.getGuiLeft() + 25, 47).
				setSize(70, 20).
				setCurrentButtonId(menu.be.getMode()).
				build();

		
		CustomCheckbox items = RadioButton.newButton(Icon.of(new ItemStack(Blocks.DIRT), false), 
				() -> Tooltip.create(Component.translatable(NDatabase.GUI.BlockEntities.DeliveryStation.Description.MODE_ITEMS))).
				pressAction(but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.MODE, 0);
					
					sendItemFilterUpdate(tag, menu.be);
				}).build();
		
		CustomCheckbox fluids = RadioButton.newButton(Icon.of(new ItemStack(Items.WATER_BUCKET), false), 
				() -> Tooltip.create(Component.translatable(NDatabase.GUI.BlockEntities.DeliveryStation.Description.MODE_FLUIDS))).
				pressAction(but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.MODE, 1);
					
					sendItemFilterUpdate(tag, menu.be);
				}).build();
		
		CustomCheckbox vim = RadioButton.newButton(Icon.of(StringHelper.getLocFStr("textures/misc/lightning.png"), 5, 0, 0, 16, 16, 16, 64), 
				() -> Tooltip.create(Component.translatable(NDatabase.GUI.BlockEntities.DeliveryStation.Description.MODE_VIM))).
				pressAction(but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.DeliveryStation.MODE, 2);
					
					sendItemFilterUpdate(tag, menu.be);
				}).build();

		
		
		button.addButton(items).addButton(fluids).addButton(vim);
		
		addPanel(new Panel(0, this.leftPos, this.topPos, this.imageWidth, this.imageHeight - (this.imageHeight / 2) - 10).
				addWidget(button));

		addItemFilterPanel(menu.be);
		addFluidFilterPanel(menu.be);
		addVimFilterPanel(menu.be);
		
		addPanelSwitcherDropPanel(menu.be);
	}
	
	protected void sendModeUpdate(CompoundTag message, BlockEntity be)
	{
		message.putInt("x", be.getBlockPos().getX());
		message.putInt("y", be.getBlockPos().getY());
		message.putInt("z", be.getBlockPos().getZ());
		
		sendUpdateToServer(message);
	}
	
	@Override
	protected void containerTick() 
	{
		super.containerTick();
	}

}

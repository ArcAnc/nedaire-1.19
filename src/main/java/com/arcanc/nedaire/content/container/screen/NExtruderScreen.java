/**
 * @author ArcAnc
 * Created at: 2023-03-09
 * Copyright (c) 2023
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.container.screen;

import com.arcanc.nedaire.content.container.menu.NExtruderMenu;
import com.arcanc.nedaire.content.container.widget.Panel;
import com.arcanc.nedaire.content.container.widget.RadioButton;
import com.arcanc.nedaire.content.container.widget.RadioButton.CustomCheckbox;
import com.arcanc.nedaire.content.container.widget.icon.Icon;
import com.arcanc.nedaire.content.container.widget.info.FluidInfoArea;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.FluidHelper;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class NExtruderScreen extends NContainerScreen<NExtruderMenu> 
{

	public NExtruderScreen(NExtruderMenu slots, Inventory player, Component title) 
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
				setPos(this.getGuiLeft() + 60, 47).
				setSize(70, 20).
				setCurrentButtonId(menu.be.getMode()).
				build();

		CustomCheckbox cobble = RadioButton.newButton(Icon.of(new ItemStack(Blocks.COBBLESTONE), false), 
				() -> Tooltip.create(Component.translatable(NDatabase.GUI.BlockEntities.Extruder.Description.MODE_COBBLE))).
				pressAction(but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Extruder.MODE, 0);
					
					sendModeUpdate(tag, menu.be);
				}).build();
		
		CustomCheckbox stone = RadioButton.newButton(Icon.of(new ItemStack(Blocks.STONE), false), 
				() -> Tooltip.create(Component.translatable(NDatabase.GUI.BlockEntities.Extruder.Description.MODE_STONE))).
				pressAction(but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Extruder.MODE, 1);
					
					sendModeUpdate(tag, menu.be);
				}).build();
		
		CustomCheckbox obsidian = RadioButton.newButton(Icon.of(new ItemStack(Blocks.OBSIDIAN), false), 
				() -> Tooltip.create(Component.translatable(NDatabase.GUI.BlockEntities.Extruder.Description.MODE_OBSIDIAN))).
				pressAction(but -> 
				{
					CompoundTag tag = new CompoundTag();
					
					tag.putInt(NDatabase.Blocks.BlockEntities.TagAddress.Machines.Extruder.MODE, 2);
					
					sendModeUpdate(tag, menu.be);
				}).build();

		button.addButton(cobble).addButton(stone).addButton(obsidian);
		
		
		addPanel(new Panel(0, this.getGuiLeft(), this.getGuiTop(), this.getXSize(), this.getYSize() - (this.getYSize() / 2) - 10 ).
				addWidget(new FluidInfoArea(new Rect2i(this.getGuiLeft() + 155, this.getGuiTop() + 15, 14, 42), FluidHelper.getFluidHandler(menu.be).
						orElseGet(() -> new FluidTank(1000)))).
				addWidget(new FluidInfoArea(new Rect2i(this.getGuiLeft() + 15, this.getGuiTop() + 15, 14, 42), FluidHelper.getFluidHandler(menu.be).
						orElseGet(() -> new FluidTank(1000)), 1)).
				addWidget(button));

	}
	
	protected void sendModeUpdate(CompoundTag message, BlockEntity be)
	{
		message.putInt("x", be.getBlockPos().getX());
		message.putInt("y", be.getBlockPos().getY());
		message.putInt("z", be.getBlockPos().getZ());
		
		sendUpdateToServer(message);
	}
}

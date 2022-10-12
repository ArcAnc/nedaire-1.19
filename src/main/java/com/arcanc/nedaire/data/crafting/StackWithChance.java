/**
 * @author ArcAnc
 * Created at: 2022-10-11
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting;

import com.arcanc.nedaire.util.database.NDatabase;
import com.google.common.base.Preconditions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

public record StackWithChance(Lazy<ItemStack> stack, float chance) 
{
	public StackWithChance
	{
		Preconditions.checkNotNull(stack);
	}

	public StackWithChance(ItemStack stack, float chance)
	{
		this(Lazy.of(() -> stack), chance);
	}

	public CompoundTag writeToNBT()
	{
		CompoundTag compoundNBT = new CompoundTag();
		compoundNBT.put(NDatabase.Recipes.StackWithChanceNBT.STACK, stack.get().save(new CompoundTag()));
		compoundNBT.putFloat(NDatabase.Recipes.StackWithChanceNBT.CHANCE, chance);
		return compoundNBT;
	}

	public static StackWithChance readFromNBT(CompoundTag compoundNBT)
	{
		Preconditions.checkNotNull(compoundNBT);
		Preconditions.checkArgument(compoundNBT.contains(NDatabase.Recipes.StackWithChanceNBT.CHANCE));
		Preconditions.checkArgument(compoundNBT.contains(NDatabase.Recipes.StackWithChanceNBT.STACK));
		final ItemStack stack = ItemStack.of(compoundNBT.getCompound(NDatabase.Recipes.StackWithChanceNBT.STACK));
		final float chance = compoundNBT.getFloat(NDatabase.Recipes.StackWithChanceNBT.CHANCE);
		return new StackWithChance(stack, chance);
	}

	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeItem(this.stack.get());
		buffer.writeFloat(this.chance);
	}

	public static StackWithChance read(FriendlyByteBuf buffer)
	{
		return new StackWithChance(buffer.readItem(), buffer.readFloat());
	}

	public StackWithChance recalculate(float totalChance)
	{
		return new StackWithChance(this.stack, this.chance/totalChance);
	}

}

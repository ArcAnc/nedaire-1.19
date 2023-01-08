/**
 * @author ArcAnc
 * Created at: 2022-11-04
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.content.network.messages;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public interface IMessage 
{
	void toBytes(FriendlyByteBuf buf);
	
	void process(Supplier<Context> context);
}

/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data;

import com.arcanc.nedaire.content.material.ModMaterial;
import com.arcanc.nedaire.content.registration.ModRegistration;
import com.arcanc.nedaire.util.database.ModDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStatesProvider extends BlockStateProvider 
{

	public ModBlockStatesProvider(DataGenerator gen, ExistingFileHelper exFileHelper) 
	{
		super(gen, ModDatabase.MOD_ID, exFileHelper);
	}

	
	@Override
	protected void registerStatesAndModels() 
	{
		ModMaterial mat = ModRegistration.RegisterMaterials.CORIUM;
		
		registerSimpleBlock (mat.getStorageBlock().get());
		
		if (mat.requiredOre())
		{
			registerOreBlock(mat.getOreBlock().get());	
			registerSimpleBlock (mat.getRawStorageBlock().get());
			registerDeepslateOreBlock(mat.getDeepSlateOre().get());
		}
		
		registerSimpleBlock(ModRegistration.RegisterBlocks.SKYSTONE.get());
		
		registerPedestal(ModRegistration.RegisterBlocks.PEDESTAL.get());
		registerHolder(ModRegistration.RegisterBlocks.HOLDER.get());
	}
	
	private void registerSimpleBlock (Block block)
	{
		ModelFile model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("cube_all"))).
				renderType("solid").
				texture("all", blockTexture(block)).
				texture("particle", blockTexture(block));
		
		getVariantBuilder(block).partialState().addModels(new ConfiguredModel(model));
		
		itemModels().getBuilder(itemPrefix(name(block))).
			parent(model);
	}

	private void registerOreBlock(Block block)
	{
		ModelFile model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("ore", blockTexture(block)).
				texture("back", mcLoc(blockPrefix("stone"))).
				texture("particle", mcLoc(blockPrefix("stone"))).
				element().
					cube("#back").
					end().
				element().
					cube("#ore").
					end();
		
		getVariantBuilder(block).partialState().addModels(new ConfiguredModel(model));

		itemModels().getBuilder(itemPrefix(name(block))).
			parent(model);
	}

	private void registerDeepslateOreBlock(Block block)
	{
		ModelFile model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("ore", blockTexture(block)).
				texture("back", mcLoc(blockPrefix("deepslate"))).
				texture("particle", mcLoc(blockPrefix("deepslate"))).
				element().
					cube("#back").
					end().
				element().
					cube("#ore").
					end();
		
		getVariantBuilder(block).partialState().addModels(new ConfiguredModel(model));

		itemModels().getBuilder(itemPrefix(name(block))).
			parent(model);
	}
	
	private void registerPedestal(Block block) 
	{
		ModelFile model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc("block")).
				renderType("cutout").
				texture("side", StringHelper.getLocFStr(blockPrefix(ModDatabase.Blocks.BlockEntities.Names.PEDESTAL + "/" + ModDatabase.Blocks.BlockEntities.Names.PEDESTAL + "_side"))).
				texture("top", StringHelper.getLocFStr(blockPrefix(ModDatabase.Blocks.BlockEntities.Names.PEDESTAL + "/" + ModDatabase.Blocks.BlockEntities.Names.PEDESTAL + "_top"))).
				texture("particle", StringHelper.getLocFStr(blockPrefix(ModDatabase.Blocks.BlockEntities.Names.PEDESTAL + "/" + ModDatabase.Blocks.BlockEntities.Names.PEDESTAL + "_side"))).
				element().
					from(0, 0, 0).
					to(16, 4, 16).
						face(Direction.DOWN).
							texture("#top").
							cullface(Direction.DOWN).
							end().
						face(Direction.UP).
							texture("#top").
							end().
						face(Direction.NORTH).
							texture("#side").
							cullface(Direction.NORTH).
							end().
						face(Direction.SOUTH).
							texture("#side").
							cullface(Direction.SOUTH).
							end().
						face(Direction.WEST).
							texture("#side").
							cullface(Direction.WEST).
							end().
						face(Direction.EAST).
							texture("#side").
							cullface(Direction.EAST).
							end().
					end().
				element().
					from(2, 12, 2).
					to(14, 16, 14).
						face(Direction.DOWN).
							texture("#top").
							end().
						face(Direction.UP).
							texture("#top").
							end().
						face(Direction.NORTH).
							texture("#side").
							end().
						face(Direction.SOUTH).
							texture("#side").
							end().
						face(Direction.WEST).
							texture("#side").
							end().
						face(Direction.EAST).
							texture("#side").
							end().
					end().
				element().
					from(4, 4, 4).
					to(12, 12, 12).
						face(Direction.DOWN).
							texture("#top").
							end().
						face(Direction.UP).
							texture("#top").
							end().
						face(Direction.NORTH).
							texture("#side").
							end().
						face(Direction.SOUTH).
							texture("#side").
							end().
						face(Direction.WEST).
							texture("#side").
							end().
						face(Direction.EAST).
							texture("#side").
							end().
					end();
	
		getVariantBuilder(block).partialState().addModels(new ConfiguredModel(model));
		
		itemModels().getBuilder(itemPrefix(name(block))).
			parent(model);
	}

	private void registerHolder(Block block) 
	{
		/*TODO: add uvs here!*/
		
		ModelFile model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc("block")).
				renderType("cutout").
				texture("main", StringHelper.getLocFStr(blockPrefix(ModDatabase.Blocks.Names.SKYSTONE))).
				texture("patricle", StringHelper.getLocFStr(blockPrefix(ModDatabase.Blocks.Names.SKYSTONE))).
				element().
					from(0,10,0).
					to(16,16,16).
						face(Direction.NORTH).
							texture("#main").
							cullface(Direction.NORTH).
							end().
						face(Direction.EAST).
							texture("#main").
							cullface(Direction.EAST).
							end().
						face(Direction.SOUTH).
							texture("#main").
							cullface(Direction.SOUTH).
							end().
						face(Direction.WEST).
							texture("#main").
							cullface(Direction.WEST).
							end().
						face(Direction.UP).
							texture("#main").
							cullface(Direction.UP).
							end().
						face(Direction.DOWN).
							texture("#main").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(7.5f, 10.75f, 7.5f).
					to(14.5f, 11.75f, 8.5f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.Z).
							origin(7, 9, 8).
							end().
						face(Direction.NORTH).
							texture("#main").
							cullface(Direction.NORTH).
							end().
						face(Direction.EAST).
							texture("#main").
							cullface(Direction.EAST).
							end().
						face(Direction.SOUTH).
							texture("#main").
							cullface(Direction.SOUTH).
							end().
						face(Direction.WEST).
							texture("#main").
							cullface(Direction.WEST).
							end().
						face(Direction.UP).
							texture("#main").
							cullface(Direction.UP).
							end().
						face(Direction.DOWN).
							texture("#main").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(2.5f, 8.25f, 7.5f).
					to(9.5f, 9.25f, 8.5f).
						rotation().
							angle(45f).
							axis(Direction.Axis.Z).
							origin(7, 9, 8).
							end().
						face(Direction.NORTH).
							texture("#main").
							cullface(Direction.NORTH).
							end().
						face(Direction.EAST).
							texture("#main").
							cullface(Direction.EAST).
							end().
						face(Direction.SOUTH).
							texture("#main").
							cullface(Direction.SOUTH).
							end().
						face(Direction.WEST).
							texture("#main").
							cullface(Direction.WEST).
							end().
						face(Direction.UP).
							texture("#main").
							cullface(Direction.UP).
							end().
						face(Direction.DOWN).
							texture("#main").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(8.5f, 9.75f, 1.75f).
					to(9.5f, 10.75f, 8.75f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.X).
							origin(7, 9, 8).
							end().
						face(Direction.NORTH).
							texture("#main").
							cullface(Direction.NORTH).
							end().
						face(Direction.EAST).
							texture("#main").
							cullface(Direction.EAST).
							end().
						face(Direction.SOUTH).
							texture("#main").
							cullface(Direction.SOUTH).
							end().
						face(Direction.WEST).
							texture("#main").
							cullface(Direction.WEST).
							end().
						face(Direction.UP).
							texture("#main").
							cullface(Direction.UP).
							end().
						face(Direction.DOWN).
							texture("#main").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(8.5f, 3f, 8.25f).
					to(9.5f, 10.0f, 9.25f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.X).
							origin(7, 9, 8).
							end().
						face(Direction.NORTH).
							texture("#main").
							cullface(Direction.NORTH).
							end().
						face(Direction.EAST).
							texture("#main").
							cullface(Direction.EAST).
							end().
						face(Direction.SOUTH).
							texture("#main").
							cullface(Direction.SOUTH).
							end().
						face(Direction.WEST).
							texture("#main").
							cullface(Direction.WEST).
							end().
						face(Direction.UP).
							texture("#main").
							cullface(Direction.UP).
							end().
						face(Direction.DOWN).
							texture("#main").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(-1.67f, 5.85f, 7.5f).
					to(1.33f, 6.85f, 8.5f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.Z).
							origin(0, 0, 0).
							end().
						face(Direction.NORTH).
							texture("#main").
							cullface(Direction.NORTH).
							end().
						face(Direction.EAST).
							texture("#main").
							cullface(Direction.EAST).
							end().
						face(Direction.SOUTH).
							texture("#main").
							cullface(Direction.SOUTH).
							end().
						face(Direction.WEST).
							texture("#main").
							cullface(Direction.WEST).
							end().
						face(Direction.UP).
							texture("#main").
							cullface(Direction.UP).
							end().
						face(Direction.DOWN).
							texture("#main").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(5.85f, 11.07f, 7.5f).
					to(6.85f, 14.07f, 8.5f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.Z).
							origin(0, 0, 0).
							end().
						face(Direction.NORTH).
							texture("#main").
							cullface(Direction.NORTH).
							end().
						face(Direction.EAST).
							texture("#main").
							cullface(Direction.EAST).
							end().
						face(Direction.SOUTH).
							texture("#main").
							cullface(Direction.SOUTH).
							end().
						face(Direction.WEST).
							texture("#main").
							cullface(Direction.WEST).
							end().
						face(Direction.UP).
							texture("#main").
							cullface(Direction.UP).
							end().
						face(Direction.DOWN).
							texture("#main").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(8.5f, 10.27f, 5.25f).
					to(9.5f, 13.27f, 6.25f).
						rotation().
							angle(45f).
							axis(Direction.Axis.X).
							origin(0, 0, 0).
							end().
						face(Direction.NORTH).
							texture("#main").
							cullface(Direction.NORTH).
							end().
						face(Direction.EAST).
							texture("#main").
							cullface(Direction.EAST).
							end().
						face(Direction.SOUTH).
							texture("#main").
							cullface(Direction.SOUTH).
							end().
						face(Direction.WEST).
							texture("#main").
							cullface(Direction.WEST).
							end().
						face(Direction.UP).
							texture("#main").
							cullface(Direction.UP).
							end().
						face(Direction.DOWN).
							texture("#main").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(8.5f, -0.54f, 4.77f).
					to(9.5f, 2.46f, 5.77f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.X).
							origin(0, 0, 0).
							end().
						face(Direction.NORTH).
							texture("#main").
							cullface(Direction.NORTH).
							end().
						face(Direction.EAST).
							texture("#main").
							cullface(Direction.EAST).
							end().
						face(Direction.SOUTH).
							texture("#main").
							cullface(Direction.SOUTH).
							end().
						face(Direction.WEST).
							texture("#main").
							cullface(Direction.WEST).
							end().
						face(Direction.UP).
							texture("#main").
							cullface(Direction.UP).
							end().
						face(Direction.DOWN).
							texture("#main").
							cullface(Direction.DOWN).
							end().
				end();

		getVariantBuilder(block).partialState().addModels(new ConfiguredModel(model));
		
		itemModels().getBuilder(itemPrefix(name(block))).
			parent(model);
	}
	
	private String itemPrefix(String str)
	{
		return ModelProvider.ITEM_FOLDER + "/" + str;
	}
	
	private String blockPrefix(String str)
	{
		return ModelProvider.BLOCK_FOLDER + "/" + str;
	}
	
    private String name(Block block) 
    {
        return BlockHelper.getRegistryName(block).getPath();
    }
	
	@Override
	public String getName() 
	{
		return "Nedaire Block States";
	}


}

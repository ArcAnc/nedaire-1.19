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
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
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
		super(gen, NDatabase.MOD_ID, exFileHelper);
	}

	
	@Override
	protected void registerStatesAndModels() 
	{
		ModMaterial mat = NRegistration.RegisterMaterials.CORIUM;
		
		registerSimpleBlock (mat.getStorageBlock().get());
		
		if (mat.requiredOre())
		{
			registerOreBlock(mat.getOreBlock().get());	
			registerSimpleBlock (mat.getRawStorageBlock().get());
			registerDeepslateOreBlock(mat.getDeepSlateOre().get());
		}
		
		registerSimpleBlock(NRegistration.RegisterBlocks.SKYSTONE.get());
		
		registerPedestal(NRegistration.RegisterBlocks.PEDESTAL.get());
		registerHolder(NRegistration.RegisterBlocks.HOLDER.get());
		registerManualChusher(NRegistration.RegisterBlocks.MANUAL_CRUSHER.get());
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
		
		registerModels(block, model);
	}
	
	private void registerPedestal(Block block) 
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.PEDESTAL + "/" + NDatabase.Blocks.BlockEntities.Names.PEDESTAL + "_side"));
		ResourceLocation texTop = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.PEDESTAL + "/" + NDatabase.Blocks.BlockEntities.Names.PEDESTAL + "_top"));
		
		ModelFile model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc("block")).
				renderType("cutout").
				texture("side", texSide).
				texture("top", texTop).
				texture("particle", texSide).
				element().
					from(0, 0, 0).
					to(16, 4, 16).
						allFaces((face, builder) -> 
						{
							if (face == Direction.DOWN || face == Direction.UP)
								builder.texture("#top");
							else
								builder.texture("#side");
							builder.cullface(face).end();
						}).
					end().
				element().
					from(2, 12, 2).
					to(14, 16, 14).
						allFaces((face, builder) -> 
						{
							if (face == Direction.DOWN || face == Direction.UP)
								builder.texture("#top");
							else
								builder.texture("#side");
							builder.cullface(face).end();
						}).
					end().
				element().
					from(4, 4, 4).
					to(12, 12, 12).
						allFaces((face, builder) -> 
						{
							if (face == Direction.DOWN || face == Direction.UP)
								builder.texture("#top");
							else
								builder.texture("#side");
							builder.cullface(face).end();
						}).
					end();
	
		registerModels(block, model);
	}

	private void registerHolder(Block block) 
	{
		/*TODO: add uvs here!*/
		
		ResourceLocation tex = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.Names.SKYSTONE));
		
		ModelFile model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc("block")).
				renderType("cutout").
				texture("main", tex).
				texture("patricle", tex).
				element().
					from(0,10,0).
					to(16,16,16).
						allFaces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					from(7.5f, 10.75f, 7.5f).
					to(14.5f, 11.75f, 8.5f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.Z).
							origin(7, 9, 8).
							end().
						allFaces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					from(2.5f, 8.25f, 7.5f).
					to(9.5f, 9.25f, 8.5f).
						rotation().
							angle(45f).
							axis(Direction.Axis.Z).
							origin(7, 9, 8).
							end().
						allFaces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					from(8.5f, 9.75f, 1.75f).
					to(9.5f, 10.75f, 8.75f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.X).
							origin(7, 9, 8).
							end().
						allFaces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					from(8.5f, 3f, 8.25f).
					to(9.5f, 10.0f, 9.25f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.X).
							origin(7, 9, 8).
							end().
						allFaces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					from(-1.67f, 5.85f, 7.5f).
					to(1.33f, 6.85f, 8.5f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.Z).
							origin(0, 0, 0).
							end().
						allFaces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					from(5.85f, 11.07f, 7.5f).
					to(6.85f, 14.07f, 8.5f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.Z).
							origin(0, 0, 0).
							end().
						allFaces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					from(8.5f, 10.27f, 5.25f).
					to(9.5f, 13.27f, 6.25f).
						rotation().
							angle(45f).
							axis(Direction.Axis.X).
							origin(0, 0, 0).
							end().
						allFaces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					from(8.5f, -0.54f, 4.77f).
					to(9.5f, 2.46f, 5.77f).
						rotation().
							angle(-45f).
							axis(Direction.Axis.X).
							origin(0, 0, 0).
							end().
						allFaces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end();

		registerModels(block, model);
	}
	
	private void registerManualChusher(Block block)
	{
		ResourceLocation tex = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.MANUAL_CRUSHER + "/" + NDatabase.Blocks.BlockEntities.Names.MANUAL_CRUSHER));
		
		ModelFile model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc("block")).
				renderType("cutout").
				texture("main", tex).
				texture("particle", tex).
				element().
					from(2, 0, 2).
					to(14, 6, 14).
						face(Direction.NORTH).
							uvs(9, 3, 12, 4.5f).
							end().
						face(Direction.SOUTH).
							uvs(3, 3, 6, 4.5f).
							end().
						face(Direction.EAST).
							uvs(0, 3, 3, 4.5f).
							end().
						face(Direction.WEST).
							uvs(6, 3, 9, 4.5f).
							end().
						face(Direction.UP).
							uvs(6, 3, 3, 0).
							end().
						face(Direction.DOWN).
							uvs(9, 0, 6, 3).
							end().
						faces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					from(3, 6, 3).
					to(13, 14, 13).
						face(Direction.NORTH).
							uvs(2.5f, 7, 5, 9).
							end().
						face(Direction.SOUTH).
							uvs(7.5f, 7, 10, 9).
							end().
						face(Direction.EAST).
							uvs(0, 7, 2.5f, 9).
							end().
						face(Direction.WEST).
							uvs(5, 7, 7.5f, 9).
							end().
						face(Direction.UP).
							uvs(5, 7, 2.5f, 4.5f).
							end().
						face(Direction.DOWN).
							uvs(7.5f, 4.5f, 5, 7).
							end().
						faces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					from(2, 14, 2).
					to(14, 16, 14).
						face(Direction.NORTH).
							uvs(3, 12, 6, 12.5f).
							end().
						face(Direction.SOUTH).
							uvs(9, 12, 12, 12.5f).
							end().
						face(Direction.EAST).
							uvs(0, 12, 3, 12.5f).
							end().
						face(Direction.WEST).
							uvs(6, 12, 3, 9).
							end().
						face(Direction.UP).
							uvs(6, 12, 3, 9).
							end().
						face(Direction.DOWN).
							uvs(9, 9, 6, 12).
							end().
						faces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end();
	
		horizontalBlock(block, model, 0);
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(model);
	}
	
	private void registerModels(Block block, ModelFile model)
	{
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

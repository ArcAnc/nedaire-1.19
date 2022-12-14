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
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder.FaceRotation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class NBlockStatesProvider extends BlockStateProvider 
{

	public NBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) 
	{
		super(output, NDatabase.MOD_ID, exFileHelper);
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
		registerVimStorage(NRegistration.RegisterBlocks.VIM_STORAGE.get());
		registerDeliveryStation(NRegistration.RegisterBlocks.DELIVERY_STATION.get());
		registerHoover(NRegistration.RegisterBlocks.HOOVER.get());
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
		
		BlockModelBuilder model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc("block")).
				renderType("cutout").
				texture("main", tex).
				texture("particle", tex).
				element().
					//base
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
					//glassN
					from(3, 6, 3).
					to(13, 14, 3.005f).
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
					//glassS
					from(3, 6, 13).
					to(13, 14, 13.005f).
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
					//glassE
					from(13, 6, 3).
					to(13.005f, 14, 13).
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
							rotation(FaceRotation.COUNTERCLOCKWISE_90).
							end().
						face(Direction.DOWN).
							uvs(7.5f, 4.5f, 5, 7).
							rotation(FaceRotation.CLOCKWISE_90).
							end().
						faces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					//glassW
					from(3, 6, 3).
					to(3.005f, 14, 13).
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
							rotation(FaceRotation.COUNTERCLOCKWISE_90).
							end().
						face(Direction.DOWN).
							uvs(7.5f, 4.5f, 5, 7).
							rotation(FaceRotation.CLOCKWISE_90).
							end().
						faces((face, builder) -> builder.texture("#main").cullface(face).end()).
				end().
				element().
					//up
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
							uvs(6, 12, 9, 12.5f).
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
		/*FIXME: take copy of block model and add part which is lower */
/*		//pressTop
		element().
			from(7.5f, 11, 7.5f).
			to(8.5f, 16, 8.5f).
				face(Direction.NORTH).
					uvs(15.25f, 0.25f, 15.5f, 2).
					end().
				face(Direction.SOUTH).
					uvs(15.75f, 0.25f, 16, 2).
					end().
				face(Direction.EAST).
					uvs(15, 0.25f, 15.25f, 2).
					end().
				face(Direction.WEST).
					uvs(15.5f, 0.25f, 15.75f, 2).
					end().
				face(Direction.UP).
					uvs(15.5f, 0.25f, 15.25f, 0).
					end().
				face(Direction.DOWN).
					uvs(15.75f, 0, 15.5f, 0.25f).
					end().
				faces((face, builder) -> builder.texture("#main").cullface(face).end()).
		end().
		//pressBot
		element().
			from(3.006f, 10, 3.006f).
			to(12.994f, 11, 12.994f).
				face(Direction.NORTH).
					uvs(8.5f, 15.75f, 11, 16).
					end().
				face(Direction.SOUTH).
					uvs(13.5f, 15.75f, 16, 16).
					end().
				face(Direction.EAST).
					uvs(6.25f, 15.75f, 8.5f, 16).
					end().
				face(Direction.WEST).
					uvs(11, 15.75f, 13.5f, 16).
					end().
				face(Direction.UP).
					uvs(11, 15.75f, 8.5f, 13.25f).
					end().
				face(Direction.DOWN).
					uvs(13.5f, 13.25f, 11, 15.75f).
					end().
				faces((face, builder) -> builder.texture("#main").cullface(face).end()).
		end().
		//shaftBot
		element().
			from(7.5f, 16, 7.5f).
			to(8.5f, 21, 8.5f).
				face(Direction.NORTH).
					uvs(15.25f, 9.5f, 15.5f, 10.75f).
					end().
				face(Direction.SOUTH).
					uvs(15.75f, 9.5f, 16, 10.75f).
					end().
				face(Direction.EAST).
					uvs(15, 9.5f, 15.25f, 10.75f).
					end().
				face(Direction.WEST).
					uvs(15.5f, 9.5f, 15.75f, 10.75f).
					end().
				face(Direction.UP).
					uvs(15.5f, 9.5f, 15.25f, 9.25f).
					end().
				face(Direction.DOWN).
					uvs(15.75f, 9.25f, 15.5f, 9.5f).
					end().
				faces((face, builder) -> builder.texture("#main").cullface(face).end()).
		end().
		//shaftMiddle
		element().
			from(7.5f, 21, 7.5f).
			to(13.5f, 22, 8.5f).
				face(Direction.NORTH).
					uvs(12.75f, 5.5f, 14.25f, 5.75f).
					end().
				face(Direction.SOUTH).
					uvs(14.5f, 5.5f, 16, 5.75f).
					end().
				face(Direction.EAST).
					uvs(12.5f, 5.5f, 12.75f, 5.75f).
					end().
				face(Direction.WEST).
					uvs(14.25f, 5.5f, 14.5f, 5.75f).
					end().
				face(Direction.UP).
					uvs(14.25f, 5.5f, 12.75f, 5.25f).
					end().
				face(Direction.DOWN).
					uvs(15.75f, 5.25f, 14.25f, 5.5f).
					end().
				faces((face, builder) -> builder.texture("#main").cullface(face).end()).
		end().
		//shaftTop
		element().
			from(12.5f, 22, 7.5f).
			to(13.5f, 24, 8.5f).
				face(Direction.NORTH).
					uvs(15.25f, 2.5f, 15.5f, 3).
					end().
				face(Direction.SOUTH).
					uvs(15.75f, 2.5f, 15.25f, 3).
					end().
				face(Direction.EAST).
					uvs(15, 2.5f, 15.25f, 3).
					end().
				face(Direction.WEST).
					uvs(15.5f, 2.5f, 15.75f, 3).
					end().
				face(Direction.UP).
					uvs(15.5f, 2.5f, 15.25f, 2.25f).
					end().
				face(Direction.DOWN).
					uvs(15.75f, 2.25f, 15.5f, 2.5f).
					end().
				faces((face, builder) -> builder.texture("#main").cullface(face).end()).
		end();
*/
	}
	
	private void registerVimStorage(Block block) 
	{
		ResourceLocation texFront = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE + "/" + NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE + "_front"));
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE + "/" + NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE + "_side"));
		ResourceLocation texTop = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE + "/" + NDatabase.Blocks.BlockEntities.Names.VIM_STORAGE + "_top"));
		
		ModelFile model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("front", texFront).
				texture("side", texSide).
				texture("top", texTop).
				texture("particle", texFront).
				element().
					from(0, 0, 2).
					to(4, 16, 14).
						face(Direction.NORTH).
							uvs(12, 0, 16, 16).
							end().
						face(Direction.EAST).
							uvs(2, 0, 14, 16).
							end().
						face(Direction.SOUTH).
							uvs(0, 0, 4, 16).
							end().
						face(Direction.WEST).
							uvs(2, 0, 14, 16).
							end().
						face(Direction.UP).
							uvs(4, 14, 0, 2).
							end().
						face(Direction.DOWN).
							uvs(16, 2, 12, 14).
							end().
						faces((face, builder) -> 
						{
							if(face.getAxis() == Direction.Axis.Z)
								builder.texture("#front");
							else if (face.getAxis() == Direction.Axis.X)
								builder.texture("#side");
							else if (face.getAxis() == Direction.Axis.Y)
								builder.texture("#top");
							if (face != Direction.EAST)
								builder.cullface(face);
						}).
				end().
				element().
					from(12, 0, 2).
					to(16, 16, 14).
						face(Direction.NORTH).
							uvs(0, 0, 4, 16).
							end().
						face(Direction.EAST).
							uvs(2, 0, 14, 16).
							end().
						face(Direction.SOUTH).
							uvs(12, 0, 16, 16).
							end().
						face(Direction.WEST).
							uvs(2, 0, 14, 16).
							end().
						face(Direction.UP).
							uvs(12, 2, 16, 14).
							end().
						face(Direction.DOWN).
							uvs(0, 2, 4, 14).
							end().
						faces((face, builder) -> 
						{
							if(face.getAxis() == Direction.Axis.Z)
								builder.texture("#front");
							else if (face.getAxis() == Direction.Axis.X)
								builder.texture("#side");
							else if (face.getAxis() == Direction.Axis.Y)
								builder.texture("#top");
							if (face != Direction.WEST)
								builder.cullface(face);
						}).
				end().
				element().
					from(6, 0, 2).
					to(10, 16, 14).
						face(Direction.NORTH).
							uvs(6, 0, 10, 16).
							end().
						face(Direction.EAST).
							uvs(2, 0, 14, 16).
							end().
						face(Direction.SOUTH).
							uvs(6, 0, 10, 16).
							end().
						face(Direction.WEST).
							uvs(2, 0, 14, 16).
							end().
						face(Direction.UP).
							uvs(6, 2, 10, 14).
							end().
						face(Direction.DOWN).
							uvs(6, 2, 10, 14).
							end().
						faces((face, builder) -> 
						{
							if(face.getAxis() == Direction.Axis.Z)
								builder.texture("#front");
							else if (face.getAxis() == Direction.Axis.X)
								builder.texture("#side");
							else if (face.getAxis() == Direction.Axis.Y)
								builder.texture("#top");
							if (face.getAxis() != Direction.Axis.X)
								builder.cullface(face);
						}).
				end().
				element().
					from(4, 10, 5).
					to(6, 15, 11).
						face(Direction.NORTH).
							uvs(10, 1, 12, 6).
							end().
						face(Direction.SOUTH).
							uvs(4, 1, 6, 6).
							end().
						face(Direction.UP).
							uvs(4, 5, 6, 11).
							end().
						face(Direction.DOWN).
							uvs(10, 5, 12, 11).
							end().
						faces((face, builder) -> 
						{
							if (face.getAxis() == Direction.Axis.Z)
								builder.texture("#front");
							else if(face.getAxis() == Direction.Axis.Y)
								builder.texture("#top");
							if (face.getAxis() != Direction.Axis.Y)
								builder.cullface(face);
						}).
				end().
				element().
					from(4, 1, 5).
					to(6, 6, 11).
						face(Direction.NORTH).
							uvs(10, 10, 12, 15).
							end().
						face(Direction.SOUTH).
							uvs(4, 10, 6, 15).
							end().
						face(Direction.UP).
							uvs(4, 5, 6, 11).
							end().
						face(Direction.DOWN).
							uvs(10, 5, 12, 11).
							end().
						faces((face, builder) -> 
						{
							if (face.getAxis() == Direction.Axis.Z)
								builder.texture("#front");
							else if(face.getAxis() == Direction.Axis.Y)
								builder.texture("#top");
							if (face.getAxis() != Direction.Axis.Y)
								builder.cullface(face);
						}).
				end().
				element().
					from(10, 10, 5).
					to(12, 15, 11).
						face(Direction.NORTH).
							uvs(4, 1, 6, 6).
							end().
						face(Direction.SOUTH).
							uvs(10, 1, 12, 6).
							end().
						face(Direction.UP).
							uvs(10, 5, 12, 11).
							end().
						face(Direction.DOWN).
							uvs(4, 5, 6, 11).
							end().
						faces((face, builder) -> 
						{
							if (face.getAxis() == Direction.Axis.Z)
								builder.texture("#front");
							else if(face.getAxis() == Direction.Axis.Y)
								builder.texture("#top");
							if (face.getAxis() != Direction.Axis.Y)
								builder.cullface(face);
						}).
				end().
				element().
					from(10, 1, 5).
					to(12, 6, 11).
						face(Direction.NORTH).
							uvs(4, 10, 6, 15).
							end().
						face(Direction.SOUTH).
							uvs(10, 10, 12, 15).
							end().
						face(Direction.UP).
							uvs(10, 5, 12, 11).
							end().
						face(Direction.DOWN).
							uvs(10, 5, 12, 11).
							end().
						faces((face, builder) -> 
						{
							if (face.getAxis() == Direction.Axis.Z)
								builder.texture("#front");
							else if(face.getAxis() == Direction.Axis.Y)
								builder.texture("#top");
							if (face.getAxis() != Direction.Axis.Y)
								builder.cullface(face);
						}).
				end();
	
		horizontalBlock(block, model, 0);
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(model);

	}

	private void registerDeliveryStation(Block block) 
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION + "/" + NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION + "_side"));
		ResourceLocation texTop = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION + "/" + NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION + "_top"));
		ResourceLocation texBot = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION + "/" + NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION + "_bot"));

		ModelFile mainModel = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("top", texTop).
				texture("bot", texBot).
				texture("particle", texTop).
				ao(false).
				element().
					//base
					from(1, 0, 1).
					to(15, 6, 15).
						allFaces((face, builder) -> 
						{
							if (face.getAxis() != Direction.Axis.Y)
								builder.uvs(1, 10, 15, 16).texture("#side");
						}).
						face(Direction.UP).
							uvs(15, 15, 1, 1).
							texture("#top").
							end().
						face(Direction.DOWN).
							uvs(15, 1, 1, 15).
							texture("#bot").
							end().
				end().
				element().
					//antennae_main
					from(7, 6, 7).
					to(9, 8, 9).
						allFaces((face, builder) -> 
						{
							if(face.getAxis() != Direction.Axis.Y)
								builder.uvs(7, 8, 9, 10).texture("#side");
							else
							{
								builder.uvs(7, 7, 9, 9);
								if (face == Direction.UP)
									builder.texture("#top");
								else
									builder.texture("#bot");
							}
						}).
				end().
				element().
					//antennae_middle
					from(7.5f, 8, 7.5f).
					to(8.5f, 13, 8.5f).
						allFaces((face, builder) -> 
						{
							if (face.getAxis() != Direction.Axis.Y)
								builder.uvs(7.5f, 3, 8.5f, 8).texture("#side");
							else
							{
								builder.uvs(7.5f, 7.5f, 8.5f, 8.5f);
								if(face == Direction.UP)
									builder.texture("#top");
								else
									builder.texture("#bot");
							}
						}).
				end().
				element().
					//antennae_top
					from(7.75f, 13, 7.75f).
					to(8.25f, 16, 8.25f).
						allFaces((face, builder) -> 
						{
							if (face.getAxis() != Direction.Axis.Y)
								builder.uvs(7.75f, 0, 8.25f, 3).texture("#side");
							else
							{
								builder.uvs(7.75f, 7.75f, 8.25f, 8.25f);
								if (face == Direction.UP)
									builder.texture("#top");
								else
									builder.texture("#bot");
							}	
						}).
				end().
				element().
					//platform_se_base
					from(11, 6, 11).
					to(14, 9, 14).
						allFaces((face, builder) -> 
						{
							if (face.getAxis() != Direction.Axis.Y)
							{
								if (face == Direction.EAST || face == Direction.SOUTH)
									builder.uvs(10, 7, 13, 10);
								else if (face == Direction.NORTH)
									builder.uvs(13, 7, 16, 10);
								else if (face == Direction.WEST)
									builder.uvs(16, 7, 13, 10);
								builder.texture("#side");
							}
						}).
				end().
				element().
					//platform_se_top
					from(11, 9, 11).
					to(16, 10, 16).
						allFaces((face, builder) -> 
						{
							if (face == Direction.EAST || face == Direction.SOUTH)
								builder.uvs(9, 5, 14, 6);
							else if (face == Direction.NORTH || face == Direction.WEST)
								{
									builder.uvs(9, 6, 14, 7);
									if (face == Direction.NORTH)
										builder.rotation(FaceRotation.UPSIDE_DOWN);
								}
							else 
							{
								builder.uvs(9, 0, 14, 5);
								if (face == Direction.UP)
									builder.rotation(FaceRotation.COUNTERCLOCKWISE_90);
								else
									builder.rotation(FaceRotation.UPSIDE_DOWN);
							}
							builder.texture("#side");
						}).
				end().
				element().
					//platfrom_ne_base
					from(11, 6, 2).
					to(14, 9, 5).
						allFaces((face, builder) -> 
						{
							if (face.getAxis() != Direction.Axis.Y)
							{
								if (face == Direction.EAST || face == Direction.NORTH)
									builder.uvs(10, 7, 13, 10);
								else if (face == Direction.WEST)
									builder.uvs(13, 7, 16, 10);
								else if (face == Direction.SOUTH)
									builder.uvs(16, 7, 13, 10);
								builder.texture("#side");
							}
							
						}).
				end().
				element().
					//platform_ne_top
					from(11, 9, 0).
					to(16, 10, 5).
						allFaces((face, builder) ->
						{
							if (face == Direction.NORTH || face == Direction.EAST)
								builder.uvs(9, 5, 14, 6);
							else if (face == Direction.SOUTH || face == Direction.WEST)
								{
									builder.uvs(9, 6, 14, 7);
									if (face == Direction.WEST)
										builder.rotation(FaceRotation.UPSIDE_DOWN);
								}
							else 
							{
								builder.uvs(9, 0, 14, 5);
								if (face == Direction.DOWN)
									builder.rotation(FaceRotation.COUNTERCLOCKWISE_90);
								else
									builder.rotation(FaceRotation.UPSIDE_DOWN);
							}
							builder.texture("#side");

						}).
				end().
				element().
					//platform_sw_base
					from(2, 6, 11).
					to(5, 9, 14).
						allFaces((face, builder) -> 
						{
							if (face.getAxis() != Direction.Axis.Y)
							{
								if (face == Direction.SOUTH || face == Direction.WEST)
									builder.uvs(10, 7, 13, 10);
								else if (face == Direction.EAST)
									builder.uvs(13, 7, 16, 10);
								else if (face == Direction.NORTH)
									builder.uvs(16, 7, 13, 10);
								builder.texture("#side");
							}
						
						}).
				end().
				element().
					//platfrom_sw_top
					from(0, 9, 11).
					to(5, 10, 16).
						allFaces((face, builder) ->
						{
							if (face == Direction.SOUTH || face == Direction.WEST)
								builder.uvs(9, 5, 14, 6);
							else if (face == Direction.NORTH || face == Direction.EAST)
								{
									builder.uvs(9, 6, 14, 7);
									if (face == Direction.EAST)
										builder.rotation(FaceRotation.UPSIDE_DOWN);
								}
							else 
							{
								builder.uvs(9, 0, 14, 5);
								if (face == Direction.DOWN)
									builder.rotation(FaceRotation.CLOCKWISE_90);
							}
							builder.texture("#side");

						}).
				end().
				element().
					//platform_nw_base
					from(2, 6, 2).
					to(5, 9, 5).
						allFaces((face, builder) -> 
						{
							if (face.getAxis() != Direction.Axis.Y)
							{
								if (face == Direction.NORTH || face == Direction.WEST)
									builder.uvs(10, 7, 13, 10);
								else if (face == Direction.SOUTH)
									builder.uvs(13, 7, 16, 10);
								else if (face == Direction.EAST)
									builder.uvs(16, 7, 13, 10);
								builder.texture("#side");
							}
					}).
				end().
				element().
					//platform_nw_top
					from(0, 9, 0).
					to(5, 10, 5).
						allFaces((face, builder) ->
						{
							if (face == Direction.NORTH || face == Direction.WEST)
								builder.uvs(9, 5, 14, 6);
							else if (face == Direction.SOUTH || face == Direction.EAST)
								{
									builder.uvs(9, 6, 14, 7);
									if (face == Direction.SOUTH)
										builder.rotation(FaceRotation.UPSIDE_DOWN);
								}
							else 
							{
								builder.uvs(9, 0, 14, 5);
								if (face == Direction.UP)
									builder.rotation(FaceRotation.CLOCKWISE_90);
							}
							builder.texture("#side");
					}).
				end(); 
		
		ModelFile connectorModel = models().getBuilder(blockPrefix(name(block) + "_connector")).
				renderType("solid").
				texture("texture", texSide).
				element().
					//base
					from(7, 6, 13).
					to(9, 8, 15).
						allFaces((face, builder) -> 
						{
							if (face != Direction.SOUTH && face != Direction.DOWN)
							{
								if (face.getAxis() == Direction.Axis.X)
									builder.uvs(14, 2, 16, 4);
								else 
									builder.uvs(14, 0, 16, 2);
								builder.texture("#texture");
							}
						}).
				end().
				element().
					//main
					from(6, 6, 15).
					to(10, 10, 16).
						allFaces((face, builder) -> 
						{
							if (face.getAxis() == Direction.Axis.Y || face.getAxis() == Direction.Axis.X)
							{
								builder.uvs(4, 0, 5, 4);
								if (face.getAxis() == Direction.Axis.Y)
									builder.rotation(FaceRotation.CLOCKWISE_90);
							}
							else if (face == Direction.NORTH)
								builder.uvs(0, 4, 4, 8);
							else 
								builder.uvs(0, 0, 4, 4);
							builder.texture("#texture");
						}).
				end();
		
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block).part().
			modelFile(mainModel).addModel().end();
		
        BlockHelper.BlockProperties.Pipe.PROPERTY_BY_DIRECTION.entrySet().forEach(e -> 
        {
            Direction dir = e.getKey();
            if (dir.getAxis().isHorizontal()) 
            {
                builder.part().modelFile(connectorModel).rotationY((((int) dir.toYRot())) % 360).addModel()
                    .condition(e.getValue(), true);
            }
        });
	
		itemModels().getBuilder(itemPrefix(name(block))).parent(mainModel);
	}

	private void registerHoover(Block block)
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.HOOVER + "/" + NDatabase.Blocks.BlockEntities.Names.HOOVER + "_side"));
		ResourceLocation texTop = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.HOOVER + "/" + NDatabase.Blocks.BlockEntities.Names.HOOVER + "_top"));
		ResourceLocation texBot = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.HOOVER + "/" + NDatabase.Blocks.BlockEntities.Names.HOOVER + "_bot"));
		ResourceLocation texInside = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.HOOVER + "/" + NDatabase.Blocks.BlockEntities.Names.HOOVER + "_inside"));
		
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("solid").
				texture("side", texSide).
				texture("top", texTop).
				texture("bot", texBot).
				texture("inside", texInside).
				texture("particle", texSide).
				element().
					//base
					from(0, 0, 0).
					to(16, 7, 16).
						allFaces((face, builder) -> 
						{
							if (face.getAxis() != Direction.Axis.Y)
								builder.uvs(0, 9, 16, 16).texture("#side").cullface(face);
							else
							{
								builder.uvs(0, 0, 16, 16).cullface(face);
								if(face == Direction.UP)
									builder.texture("#top");
								else
									builder.texture("#bot");
							}
						}).
				end().
				element().
					//frame_big_n
					from(3, 7, 0).
					to(13, 16, 3).
						face(Direction.NORTH).
							uvs(3, 0, 13, 9).
							texture("#side").
							cullface(Direction.NORTH).
							end().
						face(Direction.SOUTH).
							uvs(3, 0, 13, 9).
							texture("#inside").
							end().
						face(Direction.UP).
							uvs(3, 0, 13, 3).
							texture("#top").
							cullface(Direction.UP).
							end().
				end().
				element().
					//frame_big_s
					from(3, 7, 13).
					to(13, 16, 16).
						face(Direction.NORTH).
							uvs(3, 0, 13, 9).
							texture("#inside").
							end().
						face(Direction.SOUTH).
							uvs(3, 0, 13, 9).
							texture("#side").
							cullface(Direction.SOUTH).
							end().
						face(Direction.UP).
							uvs(3, 0, 13, 3).
							texture("#top").
							cullface(Direction.UP).
							rotation(FaceRotation.UPSIDE_DOWN).
							end().
				end().
				element().
					//frame_big_w
					from(0, 7, 0).
					to(3, 16, 16).
						face(Direction.NORTH).
							uvs(13, 0, 16, 9).
							texture("#side").
							cullface(Direction.NORTH).
							end().
						face(Direction.SOUTH).
							uvs(0, 0, 3, 9).
							texture("#side").
							cullface(Direction.SOUTH).
							end().
						face(Direction.EAST).
							uvs(0, 0, 16, 9).
							texture("#inside").
							end().
						face(Direction.WEST).
							uvs(0, 0, 16, 9).
							texture("#side").
							cullface(Direction.WEST).
							end().
						face(Direction.UP).
							uvs(16, 0, 0, 3).
							texture("#top").
							cullface(Direction.UP).
							rotation(FaceRotation.COUNTERCLOCKWISE_90).
							end().
				end().
				element().
					//frame_big_e
					from(13, 7, 0).
					to(16, 16, 16).
						face(Direction.NORTH).
							uvs(0, 0, 3, 9).
							texture("#side").
							cullface(Direction.NORTH).
							end().
						face(Direction.SOUTH).
							uvs(13, 0, 16, 9).
							texture("#side").
							cullface(Direction.SOUTH).
							end().
						face(Direction.EAST).
							uvs(0, 0, 16, 9).
							texture("#side").
							cullface(Direction.EAST).
							end().
						face(Direction.WEST).
							uvs(0, 0, 16, 9).
							texture("#inside").
							end().
						face(Direction.UP).
							uvs(16, 0, 0, 3).
							texture("#top").
							cullface(Direction.UP).
							rotation(FaceRotation.CLOCKWISE_90).
							end().

				end().
				element().
					//frame_small_s
					from(3, 7, 10).
					to(13, 11, 13).
						face(Direction.NORTH).
							uvs(3, 5, 13, 9).
							texture("#inside").
							rotation(FaceRotation.UPSIDE_DOWN).
							end().
						face(Direction.UP).
							uvs(3, 3, 13, 6).
							texture("#top").
							rotation(FaceRotation.UPSIDE_DOWN).
							end().
				end().
				element().
					//frame_small_n
					from(3, 7, 3).
					to(13, 11, 6).
						face(Direction.SOUTH).
							uvs(3, 5, 13, 9).
							texture("#inside").
							end().
						face(Direction.UP).
							uvs(3, 3, 13, 6).
							texture("#top").
							end().
				end().
				element().
					//frame_small_e
					from(10, 7, 6).
					to(13, 11, 10).
						face(Direction.WEST).
							uvs(6, 5, 10, 9).
							texture("#inside").
							end().
						face(Direction.UP).
							uvs(6, 3, 10, 6).
							texture("#top").
							rotation(FaceRotation.CLOCKWISE_90).
							end().
			end().
			element().
				//frame_small_w
				from(3, 7, 6).
				to(6, 11, 10).
					face(Direction.EAST).
						uvs(6, 5, 10, 9).
						texture("#inside").
						end().
					face(Direction.UP).
						uvs(6, 3, 10, 6).
						texture("#top").
						rotation(FaceRotation.COUNTERCLOCKWISE_90).
						end().
		end();
		
		registerModels(block, model);
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

/**
 * @author ArcAnc
 * Created at: 2022-03-31
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data;

import com.arcanc.nedaire.content.material.NMaterial;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.database.NDatabase;
import com.arcanc.nedaire.util.helpers.BlockHelper;
import com.arcanc.nedaire.util.helpers.StringHelper;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
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
		NMaterial mat = NRegistration.RegisterMaterials.CORIUM;
		
		registerSimpleBlock (mat.getStorageBlock().get());
		
		if (mat.requiredOre())
		{
			registerOreBlock(mat.getOreBlock().get());	
			registerSimpleBlock (mat.getRawStorageBlock().get());
			registerDeepslateOreBlock(mat.getDeepSlateOre().get());
		}
		
		registerSimpleBlock(NRegistration.RegisterBlocks.SKYSTONE.get());
		registerWall(NRegistration.RegisterBlocks.SKYSTONE_WALL.get());
		registerStairs(NRegistration.RegisterBlocks.SKYSTONE_STAIRS.get());
		registerSlab(NRegistration.RegisterBlocks.SKYSTONE_SLAB.get());
		
		registerFramework(NRegistration.RegisterBlocks.FRAMEWORK.get());
		
		registerPedestal(NRegistration.RegisterBlocks.PEDESTAL.get());
		registerHolder(NRegistration.RegisterBlocks.HOLDER.get());
		registerManualChusher(NRegistration.RegisterBlocks.MANUAL_CRUSHER.get());
		registerVimStorage(NRegistration.RegisterBlocks.VIM_STORAGE.get());
		registerFluidStorage(NRegistration.RegisterBlocks.FLUID_STORAGE.get());
		registerDeliveryStation(NRegistration.RegisterBlocks.DELIVERY_STATION.get());
		registerHoover(NRegistration.RegisterBlocks.HOOVER.get());
		registerTerramorfer(NRegistration.RegisterBlocks.TERRAMORFER.get());
		registerGeneratorSolar(NRegistration.RegisterBlocks.GENERATOR_SOLAR.get());
		registerMobCather(NRegistration.RegisterBlocks.MOB_CATCHER.get());
		
		registerMachine(NRegistration.RegisterBlocks.GENERATOR_FOOD.get());
		registerMachine(NRegistration.RegisterBlocks.GENERATOR_MOB.get());
		registerMachine(NRegistration.RegisterBlocks.FURNACE.get());
		registerMachine(NRegistration.RegisterBlocks.CRUSHER.get());
		registerExtruder(NRegistration.RegisterBlocks.EXTRUDER.get());

		registerFluidFiller(NRegistration.RegisterBlocks.FLUID_FILLER.get());
		
		registerCrossBlock(NRegistration.RegisterBlocks.CRYSTAL_GROWTH.get());
		registerDiffuser(NRegistration.RegisterBlocks.DIFFUSER.get());
		registerExpExtractor(NRegistration.RegisterBlocks.EXP_EXTRACTOR.get());

		registerJewelryTable(NRegistration.RegisterBlocks.JEWERLY_TABLE.get());
		
		//registerSimpleBlock(NRegistration.RegisterBlocks.CORE.get());
		
//		registerGeneratorFood(NRegistration.RegisterBlocks.GENERATOR_FOOD.get());
//		registerGeneratorMob(NRegistration.RegisterBlocks.GENERATOR_MOB.get());
//		registerFurnace(NRegistration.RegisterBlocks.FURNACE.get());
//		registerCrusher(NRegistration.RegisterBlocks.CRUSHER.get());
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
	
	private void registerWall(WallBlock block) 
	{
		
		String name = blockPrefix(name(block));
		ResourceLocation blockText = blockTexture(block);
		
		String blockString = blockText.getPath().substring(0, blockText.getPath().length() - 5);
		ResourceLocation texture = new ResourceLocation(blockText.getNamespace(), blockString);
		
		ModelFile post = models().wallPost(name + "_post", texture);
		ModelFile side = models().wallSide(name + "_side", texture);
		ModelFile sideTall = models().wallSideTall(name + "_side_tall", texture);
		ModelFile inv = models().wallInventory(name + "_inventory", texture);
		
		wallBlock(block, post, side, sideTall);
		
		itemModels().getBuilder(itemPrefix(name(block))).
			parent(inv);
	}
	
	private void registerStairs(StairBlock block) 
	{
        String name = blockPrefix(name(block));
		ResourceLocation blockText = blockTexture(block);
		
		String blockString = blockText.getPath().substring(0, blockText.getPath().length() - 7);
		ResourceLocation texture = new ResourceLocation(blockText.getNamespace(), blockString);
		
		ModelFile stairs = models().stairs(name, texture, texture, texture);
        ModelFile stairsInner = models().stairsInner(name + "_inner", texture, texture, texture);
        ModelFile stairsOuter = models().stairsOuter(name + "_outer", texture, texture, texture);
        stairsBlock(block, stairs, stairsInner, stairsOuter);
        
        itemModels().getBuilder(itemPrefix(name(block))).
        	parent(stairs);
	}
	
	private void registerSlab(SlabBlock block) 
	{
		String name = blockPrefix(name(block));
		ResourceLocation blockText = blockTexture(block);
		
		String blockString = blockText.getPath().substring(0, blockText.getPath().length() - 5);
		ResourceLocation texture = new ResourceLocation(blockText.getNamespace(), blockString);
		
		ModelFile slab = models().slab(name, texture, texture, texture);
		ModelFile top = models().slabTop(name + "_top", texture, texture, texture);
		ModelFile doubleSlab = models().getExistingFile(texture);
		
		slabBlock(block, slab, top, doubleSlab);
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(slab);
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
		
		registerModels(block, model);
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
		ResourceLocation tex = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.HOLDER + "/texture"));
		
		ModelFile model = models().
				withExistingParent(blockPrefix(name(block)), mcLoc("block")).
				renderType("cutout").
				texture("main", tex).
				texture("patricle", tex).
				element().
					from(0,10,0).
					to(16,16,16).
					allFaces((face, builder) -> 
					{
						builder.texture("#main");
						if (face != Direction.DOWN)
							builder.cullface(face);
						if (face.getAxis().isHorizontal())
							builder.uvs(4.25f, 1.75f * face.get2DDataValue(), 8.25f, 1.5f + 1.75f * face.get2DDataValue());
						else
							builder.uvs(4, 8.25f * face.get3DDataValue(), 0, 4 + 0.25f * face.get3DDataValue());
					}).
				end().
				element().
					from(4.001f, 9, 4.001f).
					to(11.999f, 10, 11.999f).
					face(Direction.DOWN).
						uvs(14.5f, 3, 12.5f, 5).
						texture("#main").
						end().
				end().
				element().
					from(5, 8, 12).
					to(11, 10, 13).
					face(Direction.NORTH).end().
					face(Direction.SOUTH).end().
					face(Direction.EAST).end().
					face(Direction.WEST).end().
					face(Direction.DOWN).end().
					faces((face, builder) -> 
					{
						builder.texture("#main");
						if (face.getAxis().isHorizontal())
						{
							builder.uvs(10.25f, 0.75f * face.get2DDataValue(), face.getAxis() == Direction.Axis.X ? 10.5f : 11.75f, 0.5f + 0.75f * face.get2DDataValue());
						}
						else 
							builder.uvs(10, 0, 8.5f, 0.25f);
					}).
				end().
				element().
					from(5, 8, 3).
					to(11, 10, 4).
					face(Direction.NORTH).end().
					face(Direction.SOUTH).end().
					face(Direction.EAST).end().
					face(Direction.WEST).end().
					face(Direction.DOWN).end().
					faces((face, builder) -> 
					{
						builder.texture("#main");
						if (face.getAxis().isHorizontal())
							builder.uvs(10.25f, 0.75f * face.get2DDataValue(), face.getAxis() == Direction.Axis.X ? 10.5f : 11.75f, 0.5f + 0.75f * face.get2DDataValue());
						else 
							builder.uvs(10, 0, 8.5f, 0.25f);
					}).
				end().
				element().
					from(12, 8, 5).
					to(13, 10, 11).
					face(Direction.NORTH).end().
					face(Direction.SOUTH).end().
					face(Direction.EAST).end().
					face(Direction.WEST).end().
					face(Direction.DOWN).end().
					faces((face, builder) -> 
					{
						builder.texture("#main");
						if (face.getAxis().isHorizontal())
							builder.uvs(10.25f, 3 + 0.75f * face.get2DDataValue(), face.getAxis() == Direction.Axis.Z ? 10.5f : 11.75f, 3.5f + 0.75f * face.get2DDataValue());
						else 
							builder.uvs(8.5f, 3.25f, 10, 3).rotation(FaceRotation.CLOCKWISE_90);
					}).
				end().
				element().
					from(3, 8, 5).
					to(4, 10, 11).
					face(Direction.NORTH).end().
					face(Direction.SOUTH).end().
					face(Direction.EAST).end().
					face(Direction.WEST).end().
					face(Direction.DOWN).end().
					faces((face, builder) -> 
					{
						builder.texture("#main");
						if (face.getAxis().isHorizontal())
							builder.uvs(10.25f, 3 + 0.75f * face.get2DDataValue(), face.getAxis() == Direction.Axis.Z ? 10.5f : 11.75f, 3.5f + 0.75f * face.get2DDataValue());
						else 
							builder.uvs(8.5f, 3.25f, 10, 3).rotation(FaceRotation.CLOCKWISE_90);
					}).
				end().
				element().
					from(11, 8, 4).
					to(12, 10, 5).
					face(Direction.NORTH).end().
					face(Direction.SOUTH).end().
					face(Direction.EAST).end().
					face(Direction.WEST).end().
					face(Direction.DOWN).end().
					faces((face, builder) -> 
					{
						builder.texture("#main");
						if (face.getAxis().isHorizontal())
							builder.uvs(12.5f, 0.75f * face.get2DDataValue(), 12.75f, 0.5f + 0.75f * face.get2DDataValue());
						else 
							builder.uvs(12.25f, 0, 12, 0.25f);
					}).
				end().
				element().
					from(11, 8, 11).
					to(12, 10, 12).
					face(Direction.NORTH).end().
					face(Direction.SOUTH).end().
					face(Direction.EAST).end().
					face(Direction.WEST).end().
					face(Direction.DOWN).end().
					faces((face, builder) -> 
					{
						builder.texture("#main");
						if (face.getAxis().isHorizontal())
							builder.uvs(12.5f, 0.75f * face.get2DDataValue(), 12.75f, 0.5f + 0.75f * face.get2DDataValue());
						else 
							builder.uvs(12.25f, 0, 12, 0.25f);
					}).
				end().
				element().
					from(4, 8, 11).
					to(5, 10, 12).
					face(Direction.NORTH).end().
					face(Direction.SOUTH).end().
					face(Direction.EAST).end().
					face(Direction.WEST).end().
					face(Direction.DOWN).end().
					faces((face, builder) -> 
					{
						builder.texture("#main");
						if (face.getAxis().isHorizontal())
							builder.uvs(12.5f, 0.75f * face.get2DDataValue(), 12.75f, 0.5f + 0.75f * face.get2DDataValue());
						else 
							builder.uvs(12.25f, 0, 12, 0.25f);
					}).
				end().
				element().
					from(4, 8, 4).
					to(5, 10, 5).
					face(Direction.NORTH).end().
					face(Direction.SOUTH).end().
					face(Direction.EAST).end().
					face(Direction.WEST).end().
					face(Direction.DOWN).end().
					faces((face, builder) -> 
					{
						builder.texture("#main");
						if (face.getAxis().isHorizontal())
							builder.uvs(12.5f, 0.75f * face.get2DDataValue(), 12.75f, 0.5f + 0.75f * face.get2DDataValue());
						else 
							builder.uvs(12.25f, 0, 12, 0.25f);
					}).
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
				texture("port", getPortTexture()).
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
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.DOWN).
						texture("#port").
						cullface(Direction.DOWN).
						tintindex(Direction.DOWN.get3DDataValue()).
						end().
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
				texture("port", getPortTexture()).
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
				end().
				element().
					from(-0.001f, -0.001f, -0.001f).
					to(16.001f, 16.001f, 16.001f).
						face(Direction.WEST).
							texture("#port").
							cullface(Direction.WEST).
							tintindex(Direction.WEST.get3DDataValue()).
							end().
						face(Direction.EAST).
							texture("#port").
							cullface(Direction.EAST).
							tintindex(Direction.EAST.get3DDataValue()).
							end().
				end();
	
		horizontalBlock(block, model, 0);
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(model);

	}

	private void registerFluidStorage(Block block) 
	{
		ResourceLocation texGlass = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.FLUID_STORAGE + "/" + NDatabase.Blocks.BlockEntities.Names.FLUID_STORAGE + "_glass"));
		ResourceLocation texTop = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.FLUID_STORAGE + "/" + NDatabase.Blocks.BlockEntities.Names.FLUID_STORAGE + "_updown"));
	
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("glass", texGlass).
				texture("updown", texTop).
				texture("particle", texGlass).
				texture("port", getPortTexture()).
				ao(false).
				element().
					from(3, 0, 3).
					to(13, 16, 13).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16);
						if (face.getAxis().isHorizontal())
							builder.texture("#glass");
						else
							builder.texture("#updown").cullface(face);
					}).
				end().
				element().
					from(13, 16, 13).
					to(3, 0, 3).
					face(Direction.NORTH).
						uvs(0, 16, 16, 0).
						texture("#glass").
						end().
					face(Direction.SOUTH).
						uvs(0, 16, 16, 0).
						texture("#glass").
						end().
					face(Direction.WEST).
						uvs(0, 16, 16, 0).
						texture("#glass").
						end().
					face(Direction.EAST).
						uvs(0, 16, 16, 0).
						texture("#glass").
						end().
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.UP).
						texture("#port").
						cullface(Direction.UP).
						tintindex(Direction.UP.get3DDataValue()).
						end().
					face(Direction.DOWN).
						texture("#port").
						cullface(Direction.DOWN).
						tintindex(Direction.DOWN.get3DDataValue()).
						end().
				end();

		
		registerModels(block, model);
	}

	private void registerDeliveryStation(Block block)
	{
		ResourceLocation texture = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION + "/" + NDatabase.Blocks.BlockEntities.Names.DELIVERY_STATION));
		
		ModelFile core = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("tex", texture).
				texture("particle", texture).
				ao(false).
				element().
					from(5, 5, 5).
					to(11, 11, 11).
					allFaces((face, builder) -> 
					{
						builder.uvs(5, 5, 11, 11).texture("#tex");
					}).
				end();
		
		ModelFile connectorModel = models().getBuilder(blockPrefix(name(block) + "_connector")).
				renderType("cutout").
				texture("tex", texture).
				texture("particle", texture).
				element().
					from(7.5f, 7.5f, 11).
					to(8.5f, 8.5f, 15).
					rotation().
						angle(0).
						axis(Axis.Y).
						origin(8f, 8f, 8f).
						end().
					allFaces((face, builder) -> 
					{
						if (face.getAxis() != Direction.Axis.Z)
						{
							builder.uvs(1, 0, 2, 4);
							if (face == Direction.EAST)
								builder.rotation(FaceRotation.COUNTERCLOCKWISE_90);
							else if(face == Direction.WEST)
								builder.rotation(FaceRotation.CLOCKWISE_90);
							else if (face == Direction.UP)
								builder.rotation(FaceRotation.UPSIDE_DOWN);
						}
						else 
						{
							builder.uvs(0, 0, 1, 1);
							if(face == Direction.NORTH)
							{
								builder.rotation(FaceRotation.UPSIDE_DOWN);
							}
						}
						builder.texture("#tex");
					}).
				end().
				element().
					from(6, 6, 15).
					to(10, 10, 16).
					rotation().
						angle(0f).
						axis(Axis.Y).
						origin(8f, 8f, 8f).
						end().
					allFaces((face, builder) -> 
					{
						if (face.getAxis() != Direction.Axis.Z)
						{
							builder.uvs(0, 8, 4, 9);
							if (face == Direction.EAST)
								builder.rotation(FaceRotation.COUNTERCLOCKWISE_90);
							else if (face == Direction.WEST)
								builder.rotation(FaceRotation.CLOCKWISE_90);
							else if (face == Direction.UP)
								builder.rotation(FaceRotation.UPSIDE_DOWN);
						}
						else
						{
							builder.uvs(0, 9, 4, 13);
							if (face == Direction.NORTH)
								builder.rotation(FaceRotation.UPSIDE_DOWN);
							else
								builder.cullface(Direction.SOUTH);
						}
						builder.texture("#tex");
					}).
				end();
				
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block).part().
				modelFile(core).addModel().end();
			
	        BlockHelper.BlockProperties.Pipe.PROPERTY_BY_DIRECTION.entrySet().forEach(e -> 
	        {
	            Direction dir = e.getKey();
	            if (dir.getAxis().isHorizontal()) 
	            {
	                builder.part().modelFile(connectorModel).rotationY((((int) dir.toYRot())) % 360).addModel()
	                    .condition(e.getValue(), true);
	            }
	            else
	            {
	            	builder.part().modelFile(connectorModel).rotationX(dir.getStepY() * 90).addModel().
	            	condition(e.getValue(), true);
	            }
	        });
		
			itemModels().getBuilder(itemPrefix(name(block))).parent(core);
	}
	
/*	private void registerDeliveryStation(Block block) 
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
*/
	private void registerHoover(Block block)
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.HOOVER + "/" + NDatabase.Blocks.BlockEntities.Names.HOOVER + "_side"));
		ResourceLocation texTop = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.HOOVER + "/" + NDatabase.Blocks.BlockEntities.Names.HOOVER + "_top"));
		ResourceLocation texBot = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.HOOVER + "/" + NDatabase.Blocks.BlockEntities.Names.HOOVER + "_bot"));
		ResourceLocation texInside = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.HOOVER + "/" + NDatabase.Blocks.BlockEntities.Names.HOOVER + "_inside"));
		
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("top", texTop).
				texture("bot", texBot).
				texture("inside", texInside).
				texture("port", getPortTexture()).
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
		end().
		element().
		from(-0.001f, -0.001f, -0.001f).
		to(16.001f, 16.001f, 16.001f).
			face(Direction.DOWN).
				end().
			face(Direction.SOUTH).
				end().
			face(Direction.NORTH).
				end().
			face(Direction.EAST).
				end().
			face(Direction.WEST).
				end().
			faces((dir, builder) -> 
			{
				builder.texture("#port").
				cullface(dir).
				tintindex(dir.get3DDataValue());
			}).
		end();
		
		registerModels(block, model);
	}
	
	private void registerTerramorfer(Block block)
	{
		ResourceLocation texture = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.TERRAMORFER + "/" + NDatabase.Blocks.BlockEntities.Names.TERRAMORFER));
		
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				ao(false).
				texture("texture", texture).
				texture("particle", texture).
				element().
					from(0, 2, 0).
					to(2, 14, 2).
					face(Direction.NORTH).end().
					face(Direction.EAST).end().
					face(Direction.SOUTH).end().
					face(Direction.WEST).end().
					faces((face, builder) -> 
					{
						builder.uvs(4.25f, 3, 4.75f, 6).texture("#texture");
						if (face == Direction.NORTH || face == Direction.WEST)
							builder.cullface(face);
					}).
				end().
				element().
					from(0, 2, 14).
					to(2, 14, 16).
					face(Direction.NORTH).end().
					face(Direction.EAST).end().
					face(Direction.SOUTH).end().
					face(Direction.WEST).end().
					faces((face, builder) -> 
					{
						builder.uvs(4.25f, 3, 4.75f, 6).texture("#texture");
						if (face == Direction.SOUTH || face == Direction.WEST)
							builder.cullface(face);
					}).
				end().
				element().
					from(14, 2, 14).
					to(16, 14, 16).
					face(Direction.NORTH).end().
					face(Direction.EAST).end().
					face(Direction.SOUTH).end().
					face(Direction.WEST).end().
					faces((face, builder) -> 
					{
						builder.uvs(4.25f, 3, 4.75f, 6).texture("#texture");
						if (face == Direction.SOUTH || face == Direction.EAST)
							builder.cullface(face);
					}).
				end().
				element().
					from(14, 2, 0).
					to(16, 14, 2).
					face(Direction.NORTH).end().
					face(Direction.EAST).end().
					face(Direction.SOUTH).end().
					face(Direction.WEST).end().
					faces((face, builder) -> 
					{
						builder.uvs(4.25f, 3, 4.75f, 6).texture("#texture");
						if (face == Direction.NORTH || face == Direction.EAST)
							builder.cullface(face);
					}).
				end().
				element().
					from(0, 0, 0).
					to(16, 2, 16).
					allFaces((face, builder) -> 
					{
						builder.texture("#texture");
						if (face != Direction.UP)
						{
							builder.cullface(face);
						}
						if (face.getAxis().isHorizontal())
						{
							builder.uvs(4.25f, face.get2DDataValue() * 0.75f, 8.25f, 0.5f + 0.75f * face.get2DDataValue());
						}
						else
						{
							builder.uvs(0, 4.25f * face.getOpposite().get3DDataValue(), 4, 4 + 4.25f * face.getOpposite().get3DDataValue());
						}
					}).
				end().
				element().
					from(0, 14, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.texture("#texture");
						if (face != Direction.DOWN)
						{
							builder.cullface(face);
						}
						if (face.getAxis().isHorizontal())
						{
							builder.uvs(4.25f, face.get2DDataValue() * 0.75f, 8.25f, 0.5f + 0.75f * face.get2DDataValue());
						}
						else
						{
							builder.uvs(4, 4 + 0.25f * face.getOpposite().get3DDataValue(), 0, 8.25f * face.getOpposite().get3DDataValue());
						}
					}).
				end().
				element().
					from(2, 2, 0).
					to(14, 14, 0).
					face(Direction.NORTH).end().
					face(Direction.SOUTH).end().
					faces((face, builder) -> 
					{
						builder.uvs(8.5f, 0, 11.5f, 3).texture("#texture");
						if (face == Direction.NORTH)
							builder.cullface(face);
					}).
				end().
				element().
					from(16, 2, 2).
					to(16, 14, 14).
					face(Direction.EAST).end().
					face(Direction.WEST).end().
					faces((face, builder) -> 
					{
						builder.uvs(8.5f, 0, 11.5f, 3).texture("#texture");
						if (face == Direction.EAST)
							builder.cullface(face);
					}).
				end().
				element().
					from(0, 2, 2).
					to(0, 14, 14).
					face(Direction.EAST).end().
					face(Direction.WEST).end().
					faces((face, builder) -> 
					{
						builder.uvs(8.5f, 0, 11.5f, 3).texture("#texture");
						if (face == Direction.WEST)
							builder.cullface(face);
					}).
				end().
				element().
					from(2, 2, 16).
					to(14, 14, 16).
					face(Direction.NORTH).end().
					face(Direction.SOUTH).end().
					faces((face, builder) -> 
					{
						builder.uvs(8.5f, 0, 11.5f, 3).texture("#texture");
						if (face == Direction.SOUTH)
							builder.cullface(face);
					}).
				end();






/*		ResourceLocation texGlass = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.TERRAMORFER + "/" + NDatabase.Blocks.BlockEntities.Names.TERRAMORFER + "_glass"));
		ResourceLocation texStone = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.TERRAMORFER + "/" + NDatabase.Blocks.BlockEntities.Names.TERRAMORFER + "_stone"));
		ResourceLocation texPedestal = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.TERRAMORFER + "/" + NDatabase.Blocks.BlockEntities.Names.TERRAMORFER + "_pedestal"));
	
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("glass", texGlass).
				texture("stone", texStone).
				texture("pedestal", texPedestal).
				texture("particle", texPedestal).
				//nw leg
				element().
					from(4, 0, 4).
					to(5, 4, 5).
					allFaces((face, builder) -> 
					{
						if (face.getAxis() != Direction.Axis.Y)
							builder.uvs(7, 5, 8, 9);
						else
							builder.uvs(6, 8, 5, 7).cullface(face);
						builder.texture("#stone");
					}).
				end().
				//sw leg
				element().
					from(4, 0, 11).
					to(5, 4, 12).
					allFaces((face, builder) -> 
					{
						if (face.getAxis() != Direction.Axis.Y)
							builder.uvs(7, 5, 8, 9);
						else
							builder.uvs(6, 8, 5, 7).cullface(face);
						builder.texture("#stone");
					}).
				end().
				//ne leg
				element().
					from(11, 0, 4).
					to(12, 4, 5).
					allFaces((face, builder) -> 
					{
						if (face.getAxis() != Direction.Axis.Y)
							builder.uvs(7, 5, 8, 9);
						else
							builder.uvs(6, 8, 5, 7).cullface(face);
						builder.texture("#stone");
					}).
				end().
				//se leg
				element().
					from(11, 0, 11).
					to(12, 4, 12).
					allFaces((face, builder) -> 
					{
						if (face.getAxis() != Direction.Axis.Y)
							builder.uvs(7, 5, 8, 9);
						else
							builder.uvs(6, 8, 5, 7).cullface(face);
						builder.texture("#stone");
					}).
				end().
				//platform
				element().
					from(3, 4, 3).
					to(13, 6, 13).
					allFaces((face, builder) -> 
					{
						if (face.getAxis() != Direction.Axis.Y)
							builder.uvs(0, 0, 10, 2).cullface(face);
						else
							builder.uvs(6, 6, 16, 16);
						builder.texture("#stone");
					}).
				end().
				//glass_block
				element().
					from(5, 6, 5).
					to(11, 13, 11).
					allFaces((face, builder) -> 
					{
						if (face == Direction.DOWN)
							builder.uvs(4, 4, 16, 16);
						else
							builder.uvs(0, 0, 16, 16);
						builder.texture("#glass");
					}).
				end().
				//glass_block_inverted
				element().
					from(11, 13, 11).
					to(5, 6, 5).
					allFaces((face, builder) -> 
					{
						if (face.getAxis() != Direction.Axis.Y)
							builder.uvs(0, 16, 16, 0);
						else
							if (face == Direction.DOWN)
								builder.uvs(16, 0, 0, 16);
							if (face == Direction.UP)
								builder.uvs(13, 4, 4, 13);
						builder.texture("#glass");
					}).
				end().
				//pedestal
				element().
					from(7, 6, 7).
					to(9, 8, 9).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).texture("#pedestal");
					}).
				end();

*/		registerModels(block, model);
	}
	
	private void registerGeneratorSolar(Block block) 
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.Generators.SOLAR +"/side"));
		ResourceLocation texTop = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.Generators.SOLAR + "/top"));
		ResourceLocation texBot = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.Generators.SOLAR + "/bot"));

		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("top", texTop).
				texture("bot", texBot).
				texture("port", getPortTexture()).
				texture("particle", texTop).
				element().
					from(0, 0, 0).
					to(16, 4, 16).
					allFaces((face, builder) -> 
					{
						if (face.getAxis() != Direction.Axis.Y)
							builder.uvs(0, 0, 16, 4).texture("#side").cullface(face);
						else
						{
							builder.uvs(0, 0, 16, 16);
							if(face == Direction.DOWN)
								builder.texture("#bot").cullface(face);
							else
								builder.texture("#top");
						}
					}).
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.DOWN).
						texture("#port").
						cullface(Direction.DOWN).
						tintindex(Direction.DOWN.get3DDataValue()).
						end().
				end();

		
		registerModels(block, model);
	}

	private void registerMachine(Block block)
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(name(block) + "/side"));
		ResourceLocation texFace = StringHelper.getLocFStr(blockPrefix(name(block) + "/face"));
		ResourceLocation texFaceOff = StringHelper.getLocFStr(blockPrefix(name(block) + "/face_off"));
	
		ModelFile modelOn = models().withExistingParent(blockPrefix(name(block) + "_on"), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("face", texFace).
				texture("port", getPortTexture()).
				texture("particle", texFace).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).texture("#side").cullface(face);
						if (face == Direction.SOUTH)
							builder.texture("#face").emissivity(15, 15);
					}).
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.WEST).
						end().
					face(Direction.EAST).
						end().
					face(Direction.DOWN).
						end().
					face(Direction.UP).
						end().
					face(Direction.NORTH).
						end().
					faces((dir, builder) -> 
					{
						builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
					}).
				end();
		
		ModelFile modelOff = models().withExistingParent(blockPrefix(name(block) + "_off"), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("face", texFaceOff).
				texture("port", getPortTexture()).
				texture("particle", texFace).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).texture("#side").cullface(face);
						if (face == Direction.SOUTH)
							builder.texture("#face").emissivity(0, 0);
					}).
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.WEST).
						end().
					face(Direction.EAST).
						end().
					face(Direction.DOWN).
						end().
					face(Direction.UP).
						end().
					face(Direction.NORTH).
						end().
					faces((dir, builder) -> 
					{
						builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
					}).
				end();

	
		horizontalBlock(block, (state) -> 
		{
			boolean lit = state.getValue(BlockHelper.BlockProperties.LIT);

			return lit ? modelOn : modelOff;
		}, 0);
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(modelOff).
		transforms().
			transform(ItemDisplayContext.GUI).
				rotation(30, 45, 0).
				scale(0.625f).
			end().
			transform(ItemDisplayContext.FIXED).
				rotation(0, 180, 0).
				scale(0.5f).
			end().
		end();		
	}
	
/*	private void registerGeneratorFood(Block block) 
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.Generators.FOOD + "/side"));
		ResourceLocation texFace = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.Generators.FOOD + "/face"));
		ResourceLocation texFaceOff = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.Generators.FOOD + "/face_off"));
	
		ModelFile modelOn = models().withExistingParent(blockPrefix(name(block) + "_on"), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("face", texFace).
				texture("port", getPortTexture()).
				texture("particle", texFace).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).texture("#side").cullface(face);
						if (face == Direction.SOUTH)
							builder.texture("#face").emissive();
					}).
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.WEST).
						end().
					face(Direction.EAST).
						end().
					face(Direction.DOWN).
						end().
					face(Direction.UP).
						end().
					face(Direction.NORTH).
						end().
					faces((dir, builder) -> 
					{
						builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
					}).
				end();
		
		ModelFile modelOff = models().withExistingParent(blockPrefix(name(block) + "_off"), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("face", texFaceOff).
				texture("port", getPortTexture()).
				texture("particle", texFace).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).texture("#side").cullface(face);
						if (face == Direction.SOUTH)
							builder.texture("#face").emissivity(0);
					}).
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.WEST).
						end().
					face(Direction.EAST).
						end().
					face(Direction.DOWN).
						end().
					face(Direction.UP).
						end().
					face(Direction.NORTH).
						end().
					faces((dir, builder) -> 
					{
						builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
					}).
				end();

	
		horizontalBlock(block, (state) -> 
		{
			boolean lit = state.getValue(BlockHelper.BlockProperties.LIT);

			return lit ? modelOn : modelOff;
		}, 0);
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(modelOff);
	}

	private void registerGeneratorMob(Block block) 
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.Generators.MOB + "/side"));
		ResourceLocation texFace = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.Generators.MOB + "/face"));
		ResourceLocation texFaceOff = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.Generators.MOB + "/face_off"));
	
		ModelFile modelOn = models().withExistingParent(blockPrefix(name(block) + "_on"), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("face", texFace).
				texture("port", getPortTexture()).
				texture("particle", texFace).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).texture("#side").cullface(face);
						if (face == Direction.SOUTH)
							builder.texture("#face").emissive();
					}).
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.WEST).
						end().
					face(Direction.EAST).
						end().
					face(Direction.DOWN).
						end().
					face(Direction.UP).
						end().
					face(Direction.NORTH).
						end().
					faces((dir, builder) -> 
					{
						builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
					}).
				end();
		
		ModelFile modelOff = models().withExistingParent(blockPrefix(name(block) + "_off"), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("face", texFaceOff).
				texture("port", getPortTexture()).
				texture("particle", texFace).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).texture("#side").cullface(face);
						if (face == Direction.SOUTH)
							builder.texture("#face").emissivity(0);
					}).
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.WEST).
						end().
					face(Direction.EAST).
						end().
					face(Direction.DOWN).
						end().
					face(Direction.UP).
						end().
					face(Direction.NORTH).
						end().
					faces((dir, builder) -> 
					{
						builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
					}).
				end();

	
		horizontalBlock(block, (state) -> 
		{
			boolean lit = state.getValue(BlockHelper.BlockProperties.LIT);

			return lit ? modelOn : modelOff;
		}, 0);
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(modelOff);
	}
*/
	
	private void registerMobCather(Block block) 
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.MOB_CATCHER + "/side"));
		ResourceLocation texTop = StringHelper.getLocFStr(blockPrefix(NDatabase.Blocks.BlockEntities.Names.MOB_CATCHER + "/top"));
	
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("top", texTop).
				texture("port", getPortTexture()).
				texture("particle", texTop).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).
						texture("#side").
						cullface(face);
						if (face == Direction.UP)
							builder.texture("#top");
					}).
				end().
				element().
					from(-0.001f, -0.001f, -0.001f).
					to(16.001f, 16.001f, 16.001f).
						face(Direction.WEST).
							end().
						face(Direction.EAST).
							end().
						face(Direction.DOWN).
							end().
						face(Direction.SOUTH).
							end().
						face(Direction.NORTH).
							end().
						faces((dir, builder) -> 
						{
							builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
						}).
				end();
		
		registerModels(block, model);
	}

	private void registerCrossBlock(Block block) 
	{
		ResourceLocation texture = StringHelper.getLocFStr(blockPrefix(name(block) + "/" + name(block)));
	
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("cross"))).
				renderType("cutout").
				ao(false).
				texture("cross", texture).
				texture("particle", texture);
		
		registerModels(block, model);
		
		itemModels().withExistingParent(itemPrefix(name(block)), mcLoc("item/generated")).
		texture("layer0", texture).
		transforms().
			transform(ItemDisplayContext.HEAD).
				translation(0, 14, -5).
			end().
		end();
	}
	
	private void registerJewelryTable(Block block) 
	{
		ResourceLocation side = StringHelper.getLocFStr(blockPrefix(name(block) + "/side"));
		ResourceLocation top = StringHelper.getLocFStr(blockPrefix(name(block) + "/top"));
		
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("solid").
				ao(false).
				texture("side", side).
				texture("top", top).
				texture("particle", top).
				element().
					from(2, 0, 2).
					to(6, 12, 6).
						face(Direction.NORTH).
							uvs(11, 4, 15, 16).
							texture("#side").
							end().
						face(Direction.EAST).
							uvs(11, 4, 15, 16).
							texture("#side").
							end().
						face(Direction.SOUTH).
							uvs(1, 4, 5, 16).
							texture("#side").
							end().
						face(Direction.WEST).
							uvs(1, 4, 5, 16).
							texture("#side").
							end().
						face(Direction.DOWN).
							uvs(10, 2, 14, 6).
							texture("#top").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(10, 0, 2).
					to(14, 12, 6).
						face(Direction.NORTH).
							uvs(1, 4, 5, 16).
							texture("#side").
							end().
						face(Direction.EAST).
							uvs(11, 4, 15, 16).
							texture("#side").
							end().
						face(Direction.SOUTH).
							uvs(11, 4, 15, 16).
							texture("#side").
							end().
						face(Direction.WEST).
							uvs(1, 4, 5, 16).
							texture("#side").
							end().
						face(Direction.DOWN).
							uvs(2, 2, 6, 6).
							texture("#top").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(10, 0, 10).
					to(14, 12, 14).
						face(Direction.NORTH).
							uvs(1, 4, 5, 16).
							texture("#side").
							end().
						face(Direction.EAST).
							uvs(1, 4, 5, 16).
							texture("#side").
							end().
						face(Direction.SOUTH).
							uvs(11, 4, 15, 16).
							texture("#side").
							end().
						face(Direction.WEST).
							uvs(11, 4, 15, 16).
							texture("#side").
							end().
						face(Direction.DOWN).
							uvs(2, 11, 5, 14).
							texture("#top").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(2, 0, 10).
					to(6, 12, 14).
						face(Direction.NORTH).
							uvs(11, 4, 15, 16).
							texture("#side").
							end().
						face(Direction.EAST).
							uvs(1, 4, 5, 16).
							texture("#side").
							end().
						face(Direction.SOUTH).
							uvs(1, 4, 5, 16).
							texture("#side").
							end().
						face(Direction.WEST).
							uvs(11, 4, 15, 16).
							texture("#side").
							end().
						face(Direction.DOWN).
							uvs(10, 10, 14, 14).
							texture("#top").
							cullface(Direction.DOWN).
							end().
				end().
				element().
					from(0, 12, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						if (face.getAxis() != Axis.Y)
						{
							builder.uvs(0, 0, 16, 4).texture("#side");
						}
						else
						{
							builder.uvs(0, 0, 16, 16).texture("#top");
							if (face == Direction.NORTH)
								builder.cullface(face);
						}
					}).
				end();
		
		registerModels(block, model);
	}

	private void registerExtruder(Block block)
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(name(block) + "/side"));
		ResourceLocation texFace = StringHelper.getLocFStr(blockPrefix(name(block) + "/face"));
		ResourceLocation texFaceOff = StringHelper.getLocFStr(blockPrefix(name(block) + "/face_off"));
		ResourceLocation texUnderlay = StringHelper.getLocFStr(blockPrefix(name(block) + "/underlay"));
		
		ModelFile modelOn = models().withExistingParent(blockPrefix(name(block) + "_on"), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("face", texFace).
				texture("port", getPortTexture()).
				texture("underlay", texUnderlay).
				texture("particle", texFace).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).texture("#side").cullface(face);
						if (face == Direction.SOUTH)
							builder.texture("#face").emissivity(15, 15);
					}).
				end().
				element().
					from(0.001f, 0.001f, 0.001f).
					to(15.999f, 15.999f, 15.999f).
						face(Direction.SOUTH).
							texture("#underlay").
							cullface(Direction.SOUTH).
							end().
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.WEST).
						end().
					face(Direction.EAST).
						end().
					face(Direction.DOWN).
						end().
					face(Direction.UP).
						end().
					face(Direction.NORTH).
						end().
					faces((dir, builder) -> 
					{
						builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
					}).
				end();
		
		ModelFile modelOff = models().withExistingParent(blockPrefix(name(block) + "_off"), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("face", texFaceOff).
				texture("port", getPortTexture()).
				texture("particle", texFace).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).texture("#side").cullface(face);
						if (face == Direction.SOUTH)
							builder.texture("#face").emissivity(0, 0);
					}).
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.WEST).
						end().
					face(Direction.EAST).
						end().
					face(Direction.DOWN).
						end().
					face(Direction.UP).
						end().
					face(Direction.NORTH).
						end().
					faces((dir, builder) -> 
					{
						builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
					}).
				end();

	
		horizontalBlock(block, (state) -> 
		{
			boolean lit = state.getValue(BlockHelper.BlockProperties.LIT);

			return lit ? modelOn : modelOff;
		}, 0);
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(modelOff).
		transforms().
			transform(ItemDisplayContext.GUI).
				rotation(30, 45, 0).
				scale(0.625f).
			end().
			transform(ItemDisplayContext.FIXED).
				rotation(0, 180, 0).
				scale(0.5f).
			end().
		end();
	}
	
	private void registerFluidFiller(Block block)
	{
		ResourceLocation texSide = StringHelper.getLocFStr(blockPrefix(name(block) + "/side"));
		ResourceLocation texFace = StringHelper.getLocFStr(blockPrefix(name(block) + "/face"));
		ResourceLocation texFaceOff = StringHelper.getLocFStr(blockPrefix(name(block) + "/face_off"));
		
		ModelFile modelOn = models().withExistingParent(blockPrefix(name(block) + "_on"), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("face", texFace).
				texture("port", getPortTexture()).
				texture("particle", texFace).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).cullface(face);
						if (face.getAxis().isVertical())
							builder.texture("#side");
						else 
							builder.texture("#face");
					}).
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.DOWN).
						end().
					face(Direction.UP).
						end().
					faces((dir, builder) -> 
					{
						builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
					}).
				end();
		
		ModelFile modelOff = models().withExistingParent(blockPrefix(name(block) + "_off"), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				texture("side", texSide).
				texture("face", texFaceOff).
				texture("port", getPortTexture()).
				texture("particle", texFace).
				element().
					from(0, 0, 0).
					to(16, 16, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 16).cullface(face);
						if (face.getAxis().isVertical())
							builder.texture("#side");
						else 
							builder.texture("#face");
					}).
				end().
				element().
				from(-0.001f, -0.001f, -0.001f).
				to(16.001f, 16.001f, 16.001f).
					face(Direction.DOWN).
						end().
					face(Direction.UP).
						end().
					faces((dir, builder) -> 
					{
						builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
					}).
				end();
		
		getVariantBuilder(block).forAllStates((state)-> ConfiguredModel.builder().
				modelFile(state.getValue(BlockHelper.BlockProperties.ENABLED) ? modelOn : modelOff).
				build());
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(modelOff);

	}
	
	private void registerDiffuser(Block block)
	{
		ResourceLocation text = StringHelper.getLocFStr(blockPrefix("skystone"));
		
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("cauldron"))).
				renderType("cutout").
				ao(false).
				texture("top", text).
				texture("bottom", text).
				texture("side", text).
				texture("inside", text).
				texture("particle", text).
				transforms().
					transform(ItemDisplayContext.GUI).
						rotation(30, 225, 0).
						scale(0.625f).
					end().
					transform(ItemDisplayContext.GROUND).
						translation(0, 3, 0).
						scale(0.25f).
					end().
					transform(ItemDisplayContext.FIXED).
						scale(0.5f).
					end().
					transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).
						rotation(75, 45, 0).
						translation(0, 2.5f, 0).
						scale(0.375f).
					end().
					transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).
						rotation(0, 45, 0).
						scale(0.4f).
					end().
					transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).
						rotation(0, 225, 0).
						scale(0.4f).
					end().
				end();
		
		registerModels(block, model);
	}
	
	private void registerFramework(Block block)
	{
		ResourceLocation text = StringHelper.getLocFStr(blockPrefix(name(block) + "/" + name(block)));
		
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("block"))).
				renderType("cutout").
				ao(false).
				texture("tex", text).
				texture("particle", text).
				element().
					from(0, 0, 0).
					to(16, 4, 4).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 12, 16, 16).texture("#tex");
						if (face == Direction.EAST)
							builder.uvs(12, 12, 16, 16).cullface(face);
						else if(face == Direction.WEST)
							builder.uvs(0, 12, 4, 16).cullface(face);
						else if(face == Direction.NORTH || face == Direction.DOWN)
							builder.cullface(face);
					}).
				end().
				element().
					from(0, 0, 12).
					to(16, 4, 16).
					allFaces((face, builder) -> 
					{
						builder.uvs(0, 0, 16, 4).texture("#tex");
						if (face == Direction.EAST)
							builder.uvs(0, 12, 4, 16).cullface(face);
						else if(face == Direction.SOUTH)
							builder.uvs(0, 12, 16, 16).cullface(face);
						else if (face == Direction.WEST) 
							builder.uvs(12, 12, 16, 16).cullface(face);
						else if (face == Direction.DOWN)
							builder.cullface(face);
					}).
				end().
				element().
					from (0, 0, 4).
					to(4, 4, 12).
						face(Direction.EAST).
							uvs(4, 12, 12, 16).
							texture("#tex").
						end().
						face(Direction.WEST).
							uvs(4, 12, 12, 16).
							texture("#tex").
							cullface(Direction.WEST).
						end().
						face(Direction.UP).
							uvs(0, 4, 4, 12).
							texture("#tex").
						end().
						face(Direction.DOWN).
							uvs(0, 4, 4, 12).
							texture("#tex").
							cullface(Direction.DOWN).
						end().
				end().
				element().
					from(12, 0, 4).
					to(16, 4, 12).
						face(Direction.EAST).
							uvs(4, 12, 12, 16).
							texture("#tex").
							cullface(Direction.EAST).
						end().
						face(Direction.WEST).
							uvs(4, 12, 12, 16).
							texture("#tex").
						end().
						face(Direction.UP).
							uvs(0, 4, 4, 12).
							texture("#tex").
						end().
						face(Direction.DOWN).
							uvs(12, 4, 16, 12).
							texture("#tex").
							cullface(Direction.DOWN).
						end().
				end().
				element().
					from(12, 12, 4).
					to(16, 16, 12).
						face(Direction.EAST).
							uvs(4, 0, 12, 4).
							texture("#tex").
							cullface(Direction.EAST).
						end().
						face(Direction.WEST).
							uvs(4, 12, 12, 16).
							texture("#tex").
						end().
						face(Direction.UP).
							uvs(0, 4, 4, 12).
							texture("#tex").
							cullface(Direction.UP).
						end().
						face(Direction.DOWN).
							uvs(0, 4, 4, 12).
							texture("#tex").
						end().
				end().
				element().
					from(0, 12, 4).
					to(4, 16, 12).
						face(Direction.EAST).
							uvs(4, 12, 12, 16).
							texture("#tex").
						end().
						face(Direction.WEST).
							uvs(4, 0, 12, 4).
							texture("#tex").
							cullface(Direction.WEST).
						end().
						face(Direction.UP).
							uvs(0, 4, 4, 12).
							texture("#tex").
							cullface(Direction.UP).
						end().
						face(Direction.DOWN).
							uvs(12, 4, 16, 12).
							texture("#tex").
						end().
				end().
				element().
					from(0, 4, 0).
					to(4, 12, 4).
						face(Direction.NORTH).
							uvs(12, 4, 16, 12).
							texture("#tex").
							cullface(Direction.NORTH).
						end().
						face(Direction.EAST).
							uvs(12, 4, 16, 12).
							texture("#tex").
						end().
						face(Direction.SOUTH).
							uvs(0, 4, 4, 12).
							texture("#tex").
						end().
						face(Direction.WEST).
							uvs(0, 4, 4, 12).
							texture("#tex").
							cullface(Direction.WEST).
						end().
				end().
				element().
					from(12, 4, 0).
					to(16, 12, 4).
						face(Direction.NORTH).
							uvs(0, 4, 4, 12).
							texture("#tex").
							cullface(Direction.NORTH).
						end().
						face(Direction.EAST).
							uvs(12, 4, 16, 12).
							texture("#tex").
							cullface(Direction.EAST).
						end().
						face(Direction.SOUTH).
							uvs(0, 4, 4, 12).
							texture("#tex").
						end().
						face(Direction.WEST).
							uvs(0, 4, 4, 12).
							texture("#tex").
						end().
				end().
				element().
					from(12, 4, 12).
					to(16, 12, 16).
						face(Direction.NORTH).
							uvs(12, 4, 16, 12).
							texture("#tex").
						end().
						face(Direction.EAST).
							uvs(0, 4, 4, 12).
							texture("#tex").
							cullface(Direction.EAST).
						end().
						face(Direction.SOUTH).
							uvs(12, 4, 16, 12).
							texture("#tex").
							cullface(Direction.SOUTH).
						end().
						face(Direction.WEST).
							uvs(0, 4, 4, 12).
							texture("#tex").
						end().
				end().
				element().
					from(0, 4, 12).
					to(4, 12, 16).
						face(Direction.NORTH).
							uvs(12, 4, 16, 12).
							texture("#tex").
						end().
						face(Direction.EAST).
							uvs(12, 4, 16, 12).
							texture("#tex").
						end().
						face(Direction.SOUTH).
							uvs(0, 4, 4, 12).
							texture("#tex").
							cullface(Direction.SOUTH).
						end().
						face(Direction.WEST).
							uvs(12, 4, 16, 12).
							texture("#tex").
							cullface(Direction.WEST).
						end().
				end().
				element().
					from(0, 12, 0).
					to(16, 16, 4).
						face(Direction.NORTH).
							uvs(0, 0, 16, 4).
							texture("#tex").
							cullface(Direction.NORTH).
						end().
						face(Direction.EAST).
							uvs(12, 0, 16, 4).
							texture("#tex").
							cullface(Direction.EAST).
						end().
						face(Direction.SOUTH).
							uvs(0, 12, 16, 16).
							texture("#tex").
						end().
						face(Direction.WEST).
							uvs(0, 0, 4, 4).
							texture("#tex").
							cullface(Direction.WEST).
						end().
						face(Direction.UP).
							uvs(0, 0, 16, 4).
							texture("#tex").
							cullface(Direction.UP).
						end().
						face(Direction.DOWN).
							uvs(0, 0, 16, 4).
							texture("#tex").
						end().
				end().
				element().
					from(0, 12, 12).
					to(16, 16, 16).
						face(Direction.NORTH).
							uvs(0, 0, 16, 4).
							texture("#tex").
						end().
						face(Direction.EAST).
							uvs(0, 0, 4, 4).
							texture("#tex").
							cullface(Direction.EAST).
						end().
						face(Direction.SOUTH).
							uvs(0, 0, 16, 4).
							texture("#tex").
							cullface(Direction.SOUTH).
						end().
						face(Direction.WEST).
							uvs(12, 0, 16, 4).
							texture("#tex").
							cullface(Direction.WEST).
						end().
						face(Direction.UP).
							uvs(0, 12, 16, 16).
							texture("#tex").
							cullface(Direction.UP).
						end().
						face(Direction.DOWN).
							uvs(0, 0, 16, 4).
							texture("#tex").
						end().					
				end();
		
		registerModels(block, model);
		
	}
	
	private void registerExpExtractor(Block block)
	{
		ResourceLocation plate = StringHelper.getLocFStr(blockPrefix(name(block) + "/plate"));
		ResourceLocation stance = StringHelper.getLocFStr(blockPrefix(name(block) + "/stance"));
		ModelFile model = models().withExistingParent(blockPrefix(name(block)), mcLoc(blockPrefix("thin_block"))).
				renderType("cutout").
				texture("plate", plate).
				texture("stance", stance).
				texture("port", getPortTexture()).
				texture("particle", plate).
				ao(false).
				element().
					from(0, 0, 1).
					to(16, 1, 15).
						allFaces((face, builder) -> 
						{
							builder.texture("#plate").uvs(0, 15, 16, 16);
							if (face.getAxis() == Axis.Y)
							{
								builder.uvs(0, 1, 16, 15);	
							}
							if (face.getAxis() == Axis.X || face == Direction.DOWN)
							{
									builder.cullface(face);
							}
						}).
				end().
				element().
					from(0, 1, 6).
					to(1, 16, 10).
						allFaces((face, builder) -> 
						{
							builder.texture("#stance");
							if (face.getAxis() == Axis.Y)
							{
								builder.uvs(0, 0, 1, 4);
								if (face == Direction.UP)
								{
									builder.cullface(face);
								}
							}
							else
							{
								builder.uvs(0, 0, 1, 15);
								if (face == Direction.EAST)
								{
									builder.uvs(5, 0, 9, 15);
								}
								else if (face == Direction.WEST)
								{
									builder.uvs(1, 0, 5, 15).cullface(face);
								}
							}
						}).
				end().
				element().
					from(15, 1, 6).
					to(16, 16, 10).
						allFaces((face, builder) -> 
						{
							builder.texture("#stance");
							if (face.getAxis() == Axis.Y)
							{
								builder.uvs(0, 0, 1, 4);
								if (face == Direction.UP)
								{
									builder.cullface(Direction.UP);
								}
							}
							else
							{
								builder.uvs(0, 0, 1, 15);
								if (face == Direction.EAST)
								{
									builder.uvs(1, 0, 5, 15).cullface(face);
								}
								else if (face == Direction.WEST)
								{
									builder.uvs(5, 0, 9, 15);
								}
							}
						}).
				end().
				element().
					from(-0.001f, -0.001f, -0.001f).
					to(16.001f, 16.001f, 16.001f).
						face(Direction.WEST).
							end().
						face(Direction.EAST).
							end().
						face(Direction.DOWN).
							end().
						faces((dir, builder) -> 
						{
							builder.texture("#port").
								cullface(dir).
								tintindex(dir.get3DDataValue());
						}).
				end();
		
		horizontalBlock(block, (state) -> model, 0);
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(model).
		transforms().
			transform(ItemDisplayContext.GUI).
				rotation(30, 45, 0).
				scale(0.625f).
			end().
			transform(ItemDisplayContext.FIXED).
				rotation(0, 180, 0).
				scale(0.5f).
			end().
		end();		
	}
	
	private void registerModels(Block block, ModelFile model)
	{
		getVariantBuilder(block).partialState().addModels(new ConfiguredModel(model));
		
		itemModels().getBuilder(itemPrefix(name(block))).
		parent(model);
	}
	
	private ResourceLocation getPortTexture()
	{
		return StringHelper.getLocFStr(blockPrefix("port"));
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

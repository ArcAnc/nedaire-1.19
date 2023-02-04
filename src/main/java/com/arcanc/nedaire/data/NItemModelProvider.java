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
import com.arcanc.nedaire.util.helpers.ItemHelper;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class NItemModelProvider extends ItemModelProvider 
{
	public NItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) 
	{
		super(output, NDatabase.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() 
	{
		ModMaterial mat = NRegistration.RegisterMaterials.CORIUM;
		
		simpleItem(mat.getIngot().getId());
		simpleItem(mat.getDust().getId());
		simpleItem(mat.getNugget().getId());
		if (mat.requiredOre())
		{
			simpleItem(mat.getRaw().getId());
		}
			
		handheld(mat.getPickaxe().getId());
		handheld(mat.getAxe().getId());
		handheld(mat.getShovel().getId());
		handheld(mat.getShears().getId());
		handheld(mat.getHoe().getId());
		handheld(mat.getSword().getId());
			
		fishingRod(mat.getFishingRod().getId());

		bow (mat.getBow().getId());
		shield(mat.getShield().getId());
		crossbow(mat.getCrossbow().getId());
			
		simpleItem(mat.getArmorHorse().getId());
		simpleItem(mat.getPlayerArmorHead().getId());
		simpleItem(mat.getPlayerArmorChest().getId());
		simpleItem(mat.getPlayerArmorLegs().getId());
		simpleItem(mat.getPlayerArmorFeet().getId());
		
		simpleItem(NRegistration.RegisterItems.NUGGET_SKYSTONE.getId());
		
		handheld(NRegistration.RegisterItems.HAMMER.getId());
		simpleItem(NRegistration.RegisterItems.CHALK.getId());
		handheld(NRegistration.RegisterItems.BOOK.getId());
		
		/*test*/
		
/*		getBuilder(name("test")).
		parent(getExistingFile(mcLoc("item/generated"))).
		texture("layer0", ITEM_FOLDER + "/test_0").
		texture("layer1", ITEM_FOLDER + "/test_1").
			element().
				face(Direction.UP).
					texture("#layer0").
				end().
			end().
			element().
				face(Direction.UP).
					texture("#layer1").
					emissive().
				end().
			end();
	*/}
	
	private void shield (ResourceLocation loc)
	{

		String[] splittedLoc = loc.getPath().split("/"); 
		String mat_name = splittedLoc[splittedLoc.length-1];
		
		ModelFile blocking = getBuilder(name(loc.getPath() + "_blocking")).
				parent(getExistingFile(mcLoc("item/shield_blocking"))).
				texture("particle", ModelProvider.BLOCK_FOLDER + "/storage_block/" + mat_name);
		
		getBuilder(name(loc.getPath())).
			parent(getExistingFile(mcLoc("item/shield"))).
			texture("particle", ModelProvider.BLOCK_FOLDER + "/storage_block/" + mat_name).
			override().
				predicate(mcLoc("blocking"), 1.0f).
				model(blocking).
				end();

	}
	
	private void crossbow (ResourceLocation loc)
	{
		ModelFile arrow = getBuilder(name(loc.getPath()) + "_arrow").
				parent(getExistingFile(mcLoc("item/crossbow_arrow"))).
				texture("layer0", name(loc.getPath()) + "_arrow");
		
		ModelFile firework = getBuilder(name(loc.getPath()) + "_firework").
				parent(getExistingFile(mcLoc("item/crossbow_firework"))).
				texture("layer0", name(loc.getPath()) + "_firework");
		
		ModelFile cast0 = getBuilder(name(loc.getPath()) + "_0").
				parent(getExistingFile(mcLoc("item/crossbow_pulling_0"))).
				texture("layer0", name(loc.getPath()) + "_0");
	
		ModelFile cast1 = getBuilder(name(loc.getPath()) + "_1").
				parent(getExistingFile(mcLoc("item/crossbow_pulling_1"))).
				texture("layer0", name(loc.getPath()) + "_1");
	
		ModelFile cast2 = getBuilder(name(loc.getPath()) + "_2").
				parent(getExistingFile(mcLoc("item/crossbow_pulling_2"))).
				texture("layer0", name(loc.getPath()) + "_2");
		
		getBuilder(name(loc.getPath())).
			parent(getExistingFile(mcLoc("item/crossbow"))).
			texture("layer0", name(loc.getPath())).
			override().
				predicate(mcLoc("pulling"), 1.0f).
				model(cast0).
				end().
			override().
				predicate(mcLoc("pulling"), 1.0f).
				predicate(mcLoc("pull"), 0.58f).
				model(cast1).
				end().
			override().
				predicate(mcLoc("pulling"), 1.0f).
				predicate(mcLoc("pull"), 1.0f).
				model(cast2).
				end().
			override().
				predicate(mcLoc("charged"), 1.0f).
				model(arrow).
				end().
			override().
				predicate(mcLoc("charged"), 1.0f).
				predicate(mcLoc("firework"), 1.0f).
				model(firework).
				end();
	}

	private void bow (ResourceLocation loc)
	{

		ModelFile cast0 = getBuilder(name(loc.getPath()) + "_0").
				parent(getExistingFile(mcLoc("item/bow_pulling_0"))).
				texture("layer0", name(loc.getPath() + "_0"));

		ModelFile cast1 = getBuilder(name(loc.getPath()) + "_1").
				parent(getExistingFile(mcLoc("item/bow_pulling_1"))).
				texture("layer0", name(loc.getPath() + "_1"));
		
		ModelFile cast2 = getBuilder(name(loc.getPath()) + "_2").
				parent(getExistingFile(mcLoc("item/bow_pulling_2"))).
				texture("layer0", name(loc.getPath() + "_2"));
		
		getBuilder(name(loc.getPath())).
			parent(getExistingFile(mcLoc("item/bow"))).
			texture("layer0", name(loc.getPath())).
			override().
				predicate(mcLoc("pulling"), 1.0f).
				model(cast0).
				end().
			override().
				predicate(mcLoc("pulling"), 1.0f).
				predicate(mcLoc("pull"), 0.65f).
				model(cast1).
				end().
			override().
				predicate(mcLoc("pulling"), 1.0f).
				predicate(mcLoc("pull"), 0.9f).
				model(cast2).
				end();
			
	}
	
	private void fishingRod(ResourceLocation loc)
	{
		
		ModelFile castModel = getBuilder(name(loc.getPath() + "_cast")).
				parent(getExistingFile(mcLoc("item/fishing_rod"))).
				texture("layer0", name(loc.getPath() +"_cast")); 
		
		getBuilder(name(loc.getPath())).
				parent(getExistingFile(mcLoc("item/handheld_rod"))).
				texture("layer0", name(loc.getPath())).
				override().predicate(mcLoc("cast"), 1.0f).model(castModel).end();
	}
	
	private void handheld(ResourceLocation loc)
	{
		simpleItem(loc, mcLoc("item/handheld"));
	}
	
	private void simpleItem(ResourceLocation loc, ResourceLocation parent)
	{
		withExistingParent(name(loc.getPath()), parent).
		texture("layer0", name(loc.getPath()));
	}
	
	private void simpleItem(ResourceLocation loc)
	{
		withExistingParent(name(loc.getPath()), mcLoc("item/generated")).
		texture("layer0", name(loc.getPath()));
	}
	
	private void simpleItem(Item item)
	{
		simpleItem(ItemHelper.getRegistryName(item));
	}
	
	private String name (String name)
	{
		return ModelProvider.ITEM_FOLDER + "/" + name;
	}

	@Override
	public String getName() 
	{
		return "Nedaire Item Models";
	}
}

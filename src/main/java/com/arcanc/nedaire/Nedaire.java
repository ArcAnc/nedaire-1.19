/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.content.capabilities.vim.CapabilityVim;
import com.arcanc.nedaire.content.container.ModSlot;
import com.arcanc.nedaire.content.item.ItemInterfaces.ICustomModelProperties;
import com.arcanc.nedaire.content.item.weapon.ModShieldBase;
import com.arcanc.nedaire.content.itemGroup.ModItemGroup;
import com.arcanc.nedaire.content.material.ModMaterial;
import com.arcanc.nedaire.content.module.jewelry.ModuleJewelry;
import com.arcanc.nedaire.content.module.runecarving.ModuleRunecarving;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.renderer.blockEntity.HolderRenderer;
import com.arcanc.nedaire.content.renderer.blockEntity.PedestalRenderer;
import com.arcanc.nedaire.data.ModBlockLootProvider;
import com.arcanc.nedaire.data.ModBlockStatesProvider;
import com.arcanc.nedaire.data.ModBlockTagsProvider;
import com.arcanc.nedaire.data.ModItemModelProvider;
import com.arcanc.nedaire.data.ModItemTagsProvider;
import com.arcanc.nedaire.data.crafting.NRecipeProvider;
import com.arcanc.nedaire.data.language.NEnUsLangProvider;
import com.arcanc.nedaire.util.database.NDatabase;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TagsUpdatedEvent.UpdateCause;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

@Mod(NDatabase.MOD_ID)
public class Nedaire 
{

    private static Nedaire instance;
	
	private static final Logger LOGGER = LogManager.getLogger(NDatabase.MOD_ID);

	public final CreativeModeTab TAB;
	
	public Nedaire ()
	{
		instance = this;
		
		TAB = new ModItemGroup(NDatabase.ItemGroups.Main.MAIN);
		
		ModuleJewelry.MUST_PRESENT = false;
		ModuleRunecarving.MUST_PRESENT = true;
		
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		NRegistration.RegisterBlocks.BLOCKS.register(modEventBus);
	    NRegistration.RegisterItems.ITEMS.register(modEventBus);
	    NRegistration.RegisterMaterials.init();
	    NRegistration.RegisterBlockEntities.BLOCK_ENTITIES.register(modEventBus);
	    NRegistration.RegisterRecipes.RECIPE_SERIALIZERS.register(modEventBus);
	    NRegistration.RegisterRecipes.Types.init(modEventBus);
	    NRegistration.RegisterWorldGen.FEATURES.register(modEventBus);
	    NRegistration.RegisterGemEffects.EFFECTS.register(modEventBus);
		
	    modEventBus.addListener(this :: serverSetup);
	    modEventBus.addListener(this :: clientSetup);
	    modEventBus.addListener(this :: clientTextureStitch);
//	    modEventBus.addListener(this :: registerItemColors);
	    modEventBus.addListener(ModShieldBase :: registerReloadListener);
	    modEventBus.addListener(this :: registerCapability);
	    
	    ModuleJewelry.init(modEventBus);
	    
	    modEventBus.addListener(this :: gatherData);
	    
	    MinecraftForge.EVENT_BUS.addListener(this :: updatedTags);
	}
	
	private void serverSetup(final FMLCommonSetupEvent event)
    {

    }

	private void registerCapability(final RegisterCapabilitiesEvent event )
	{
		CapabilityVim.register(event);
	}
	
	private void updatedTags(final TagsUpdatedEvent event)
	{
		if (event.getUpdateCause() == UpdateCause.CLIENT_PACKET_RECEIVED)
		{
			EnchiridionInstance.INSTANCE.fillContent();
		}
	}
	
	private void clientSetup (final FMLClientSetupEvent event)
	{
		event.enqueueWork(() ->
		{
//			registerBlocksRenderers();
			
			registerModelsProperties();

			registerBlockEntityRenderers();
		});
	}
	
	private void registerBlockEntityRenderers() 
	{
		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_PEDESTAL.get(), PedestalRenderer :: new);	
		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_HOLDER.get(), HolderRenderer :: new);	
	}
	
/*	private void registerBlocksRenderers()
	{
		ModRegistration.RegisterBlocks.BLOCKS.getEntries().
		stream().
		map(RegistryObject :: get).
		forEach(block ->
		{
			if (block instanceof IBlockRenderLayer b)
			{
				ItemBlockRenderTypes.setRenderLayer(block, b.getRenderLayer());
			}
		});		
	}
*/	
	private void registerModelsProperties() 
	{
		NRegistration.RegisterItems.ITEMS.getEntries().
		stream().
		map(RegistryObject :: get).
		forEach(item -> 
		{
			if (item instanceof ICustomModelProperties modProps)
			{
				modProps.registerModelProperties();
			}
		});
	}
	
	private void clientTextureStitch (final TextureStitchEvent.Pre event)
	{
		if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS))
		{
			ModMaterial mat = NRegistration.RegisterMaterials.CORIUM;
			event.addSprite(mat.getToolMat().getShieldBase().texture());
			event.addSprite(mat.getToolMat().getShieldNoPattern().texture());
		
			/**
			 * Slots
			 */
			
			event.addSprite(ModSlot.BACKGROUND_STANDART);
			event.addSprite(ModSlot.BACKGROUND_INPUT);
			event.addSprite(ModSlot.BACKGROUND_OUPUT);
			event.addSprite(ModSlot.BACKGROUND_BOTH);
		}
	}

    public void gatherData(GatherDataEvent event)
    {
    	
    	ExistingFileHelper ext = event.getExistingFileHelper();
    	DataGenerator gen = event.getGenerator();
        
        gen.addProvider(event.includeServer(), new ModBlockLootProvider(gen));
        ModBlockTagsProvider btp = new ModBlockTagsProvider(gen, ext);
    		
    	gen.addProvider(event.includeServer(), btp);
        gen.addProvider(event.includeServer(), new ModItemTagsProvider(gen, btp, ext));    	
        gen.addProvider(event.includeServer(), new NRecipeProvider(gen));
/*            gen.addProvider(new NedaireMultiblockProvider(gen));
 */   	
    	
    	
    	gen.addProvider(event.includeClient(), new NEnUsLangProvider(gen));
        gen.addProvider(event.includeClient(), new ModItemModelProvider(gen, ext));
        gen.addProvider(event.includeClient(), new ModBlockStatesProvider(gen, ext));

/*            
            gen.addProvider(new NedaireSoundsProvider(gen, ext));
            gen.addProvider(new NedaireParticleProvider(gen));
*/
    }
    
    
	public static Nedaire getInstance()
	{
		return instance;
	}
	
	/**
	 * @return the logger
	 */
	public static Logger getLogger() 
	{
		return LOGGER;
	}
}

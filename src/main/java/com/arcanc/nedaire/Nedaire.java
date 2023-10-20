/**
 * @author ArcAnc
 * Created at: 2022-03-30
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire;

import com.arcanc.nedaire.content.block.entities.NBESidedAccess;
import com.arcanc.nedaire.content.book.EnchiridionInstance;
import com.arcanc.nedaire.content.capabilities.filter.CapabilityFilter;
import com.arcanc.nedaire.content.capabilities.vim.CapabilityVim;
import com.arcanc.nedaire.content.container.menu.NContainerMenu;
import com.arcanc.nedaire.content.container.screen.*;
import com.arcanc.nedaire.content.entities.DeliveryDroneEntity;
import com.arcanc.nedaire.content.item.ItemInterfaces.ICustomModelProperties;
import com.arcanc.nedaire.content.item.weapon.NShieldBase;
import com.arcanc.nedaire.content.module.jewelry.ModuleJewelry;
import com.arcanc.nedaire.content.module.runecarving.ModuleRunecarving;
import com.arcanc.nedaire.content.network.NNetworkEngine;
import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.content.renderer.EssenceRender;
import com.arcanc.nedaire.content.renderer.blockEntity.*;
import com.arcanc.nedaire.content.renderer.entity.DeliveryDroneRenderer;
import com.arcanc.nedaire.content.renderer.particle.EssenceParticle;
import com.arcanc.nedaire.content.world.level.levelgen.village.NVillage;
import com.arcanc.nedaire.content.world.level.levelgen.village.NVillageAddition;
import com.arcanc.nedaire.data.*;
import com.arcanc.nedaire.data.language.NEnUsLangProvider;
import com.arcanc.nedaire.data.multiblocks.reloadListeners.MultiblockManager;
import com.arcanc.nedaire.data.tags.NBlockTagsProvider;
import com.arcanc.nedaire.data.tags.NFluidTagsProvider;
import com.arcanc.nedaire.data.tags.NItemTagsProvider;
import com.arcanc.nedaire.data.worldgen.NBiomeTags;
import com.arcanc.nedaire.util.database.NDatabase;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Mod(NDatabase.MOD_ID)
public class Nedaire 
{
    private static Nedaire instance;

	private static final Logger LOGGER = LogManager.getLogger(NDatabase.MOD_ID);

	public static CreativeModeTab TAB;

	public Nedaire ()
	{
		instance = this;

		ModuleJewelry.MUST_PRESENT = false;
		ModuleRunecarving.MUST_PRESENT = true;

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		NRegistration.RegisterBlocks.BLOCKS.register(modEventBus);
	    NRegistration.RegisterItems.ITEMS.register(modEventBus);
	    NRegistration.RegisterFluids.register(modEventBus);
	    NRegistration.RegisterEntities.ENTITIES.register(modEventBus);
	    NRegistration.RegisterBlockEntities.BLOCK_ENTITIES.register(modEventBus);
		NRegistration.RegisterMenuTypes.MENU_TYPES.register(modEventBus);
	    NRegistration.RegisterRecipes.Types.init(modEventBus);
	    NRegistration.RegisterRecipes.RECIPE_SERIALIZERS.register(modEventBus);
	    NRegistration.RegisterMaterials.init();
	    NRegistration.RegisterWorldGen.FEATURES.register(modEventBus);
	    NRegistration.RegisterGemEffects.EFFECTS.register(modEventBus);

	    NRegistration.RegisterVillage.POI_TYPES.register(modEventBus);
	    NRegistration.RegisterVillage.VILLAGER_PROFESSIONS.register(modEventBus);
	    NRegistration.RegisterParticleTypes.PARTICLE_TYPES.register(modEventBus);
	    NRegistration.RegisterFeatures.FEATURES.register(modEventBus);
		NRegistration.RegisterCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

		NRegistration.RegisterMultiblocks.init(modEventBus);

	    modEventBus.addListener(this :: serverSetup);
	    modEventBus.addListener(this :: clientSetup);
//	    modEventBus.addListener(this :: registerItemColors);
	    modEventBus.addListener(NShieldBase :: registerReloadListener);
	    modEventBus.addListener(this :: registerCapability);
	    modEventBus.addListener(this :: registerParticles);
	    modEventBus.addListener(this :: registerBlockColors);

//	    modEventBus.addListener(NRenderTypes :: registerShaders);

	    registerBECustomModels(modEventBus);
	    registerEntityCustomModels(modEventBus);
	    registerEntityAttributes(modEventBus);

	    ModuleJewelry.init(modEventBus);

	    modEventBus.addListener(this :: gatherData);

	    MinecraftForge.EVENT_BUS.addListener(this :: updatedTags);
	    MinecraftForge.EVENT_BUS.addListener(NContainerMenu :: onContainerClosed);
	    MinecraftForge.EVENT_BUS.addListener(NContainerMenu :: onContainerOpen);
	    MinecraftForge.EVENT_BUS.addListener(NVillage :: addCustomTrades);
	    MinecraftForge.EVENT_BUS.addListener(NVillageAddition :: addNewVillageBuilding);
//	    MinecraftForge.EVENT_BUS.addListener(EssenceRender :: worldRender);
	    MinecraftForge.EVENT_BUS.addListener(EssenceRender :: worldRenderPatricle);
		MinecraftForge.EVENT_BUS.addListener(MultiblockManager :: registerListener);
		MinecraftForge.EVENT_BUS.addListener(MultiblockManager ::syncDataToClient);

	}

	private void serverSetup(final FMLCommonSetupEvent event)
    {
		NNetworkEngine.init();
		event.enqueueWork(() ->
		{
			//NRegistration.RegisterVillage.registerPois();
		});
    }

	private void registerCapability(final RegisterCapabilitiesEvent event )
	{
		CapabilityVim.register(event);
		CapabilityFilter.register(event);
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
			registerMenuScreens();

			registerModelsProperties();

			registerBlockEntityRenderers();

			registerEntityRenderers();

			ItemBlockRenderTypes.setRenderLayer(NRegistration.RegisterFluids.EXPERIENCE.flowing().get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(NRegistration.RegisterFluids.EXPERIENCE.still().get(), RenderType.translucent());
		});
	}

	private void registerMenuScreens()
	{
		MenuScreens.register(NRegistration.RegisterMenuTypes.HOOVER.getType(), NHooverScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.MOB_CATCHER.getType(), NMobCatcherScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.GENERATOR_SOLAR.getType(), NGeneratorSolarScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.GENERATOR_FOOD.getType(), NGeneratorFoodScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.GENERATOR_MOB.getType(), NGeneratorMobScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.DELIVERY_STATION.getType(), NDeliveryStationScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.VIM_STORAGE.getType(), NVimStorageScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.FLUID_STORAGE.getType(), NFluidStorageScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.CRUSHER.getType(), NCrusherScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.FURNACE.getType(), NFurnaceScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.EXTRUDER.getType(), NExtruderScreen :: new);
		MenuScreens.register(NRegistration.RegisterMenuTypes.FLUID_FILLER.getType(), NFluidFillerScreen :: new);

		MenuScreens.register(NRegistration.RegisterMenuTypes.EXP_EXTRACTOR.getType(), NExpExtractorScreen :: new);
	}


	private void registerEntityRenderers()
	{
		EntityRenderers.register(NRegistration.RegisterEntities.DELIVERY_DRONE.get(), DeliveryDroneRenderer :: new);
		EntityRenderers.register(NRegistration.RegisterEntities.CRYSTAL_PRISON.get(), ThrownItemRenderer :: new);
	}


	private void registerBlockEntityRenderers()
	{
		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_PEDESTAL.get(), PedestalRenderer :: new);
		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_HOLDER.get(), HolderRenderer :: new);
		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_MANUAL_CRUSHER.get(), ManualCrusherRenderer :: new);
		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_TERRAMORFER.get(), TerramorferRenderer :: new);
		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_FLUID_STORAGE.get(), FluidStorageRenderer :: new);
		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_MOB_CATHER.get(), MobCatcherRenderer :: new);
		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_DIFFUSER.get(), DiffuserRenderer :: new);

		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_EXP_EXTRACTOR.get(), ExpExtractorRenderer :: new);

		BlockEntityRenderers.register(NRegistration.RegisterBlockEntities.BE_CORE.get(), CoreRenderer :: new);
	}

	private void registerParticles(final RegisterParticleProvidersEvent event)
	{
		event.registerSpriteSet(NRegistration.RegisterParticleTypes.ESSENCE.get(), EssenceParticle.Provider :: new);
	}

	private void registerBECustomModels(IEventBus bus)
	{
	    //ManualCrusher

		bus.addListener(ManualCrusherRenderer :: registerModelLocation);
		bus.addListener(MobCatcherRenderer :: registerModelLocation);
	}

	private void registerEntityCustomModels(IEventBus bus)
	{
		//DeliveryDrone
		bus.addListener(DeliveryDroneRenderer :: registerModelLocation);
	}

	private void registerEntityAttributes(IEventBus bus)
	{
		//Delivery Drone
		bus.addListener(DeliveryDroneEntity :: createAttributes);
	}

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

	@SuppressWarnings("deprecation")
	private void registerBlockColors(final RegisterColorHandlersEvent.Block event)
	{
		BlockColors colors = event.getBlockColors();

		colors.register((state, level, pos, tintIndex) ->
		{
			BlockEntity tile = level.getBlockEntity(pos);
			if (tile instanceof NBESidedAccess access && tintIndex != -1)
			{
				return access.getAccessType(Direction.values()[tintIndex]).getColor();
			}
			return 0;
		}, NRegistration.RegisterBlocks.BLOCKS.getEntries().stream().filter(block ->
		{
			return NRegistration.RegisterBlockEntities.BLOCK_ENTITIES.getEntries().
					stream().
					filter(tile -> tile.get().isValid(block.get().defaultBlockState())).
					anyMatch(tile ->
					{
						var type = tile.get();
						BlockEntity be = type.create(new BlockPos(0,0,0), block.get().defaultBlockState());
						if (be instanceof NBESidedAccess)
							return true;
						be.setRemoved();
						return false;
					});
		}).map(RegistryObject :: get).toArray(Block[] :: new));
	}

    public void gatherData(GatherDataEvent event)
    {
    	ExistingFileHelper ext = event.getExistingFileHelper();
    	DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
       
        gen.addProvider(event.includeServer(), new NBiomeTags(packOutput, lookupProvider, ext));
        gen.addProvider(event.includeServer(), new NVillagersTags(packOutput, lookupProvider, ext));
        gen.addProvider(event.includeServer(), new LootTableProvider(
        		packOutput,
        		Collections.emptySet(),
				List.of(
						new LootTableProvider.SubProviderEntry(NBlockLootSubProvider :: new, LootContextParamSets.BLOCK))
				));
        NBlockTagsProvider btp = new NBlockTagsProvider(packOutput, lookupProvider, ext);

    	gen.addProvider(event.includeServer(), btp);
        gen.addProvider(event.includeServer(), new NItemTagsProvider(packOutput, lookupProvider, btp, ext));
        gen.addProvider(event.includeServer(), new NFluidTagsProvider(packOutput, lookupProvider, ext));
        gen.addProvider(event.includeServer(), new NRecipeProvider(packOutput));
		gen.addProvider(event.includeServer(), new NMultiblockProvider(packOutput));
        gen.addProvider(event.includeServer(), new NWorldGenProvider(packOutput, lookupProvider));
		gen.addProvider(event.includeClient(), new NParticlesDescriptionProvider(packOutput, ext));
/*            gen.addProvider(new NedaireMultiblockProvider(gen));
 */


    	gen.addProvider(event.includeClient(), new NEnUsLangProvider(packOutput));
        gen.addProvider(event.includeClient(), new NItemModelProvider(packOutput, ext));
        gen.addProvider(event.includeClient(), new NBlockStatesProvider(packOutput, ext));
        gen.addProvider(event.includeClient(), new NSpriteSourceProvider(packOutput, ext));
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

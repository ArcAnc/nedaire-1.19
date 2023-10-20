/**
 * @author ArcAnc
 * Created at: 2022-10-12
 * Copyright (c) 2022
 * 
 * This code is licensed under "Ancient's License of Common Sense"	
 * Details can be found in the license file in the root folder of this project
 */
package com.arcanc.nedaire.data.crafting;

import com.arcanc.nedaire.content.registration.NRegistration.RegisterRecipes.Types.TypeWithClass;
import com.arcanc.nedaire.util.database.NDatabase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = NDatabase.MOD_ID, bus = Bus.FORGE)
public class CachedRecipeList <T extends Recipe<?>>
{
	public static final int INVALID_RELOAD_COUNT = -1;
	private static int reloadCount = 0;

	private final Supplier<RecipeType<T>> type;
	private final Class<T> recipeClass;
	private Map<ResourceLocation, T> recipes;
	private boolean cachedDataIsClient;
	private int cachedAtReloadCount = INVALID_RELOAD_COUNT;

	public CachedRecipeList(Supplier<RecipeType<T>> type, Class<T> recipeClass)
	{
		this.type = type;
		this.recipeClass = recipeClass;
	}

	public CachedRecipeList(TypeWithClass<T> type)
	{
		this(type.type(), type.recipeClass());
	}

	@SubscribeEvent
	public static void onTagsUpdated(final TagsUpdatedEvent event)
	{
		++reloadCount;
	}

	@SubscribeEvent
	public static void onRecipeUpdatedClient(final RecipesUpdatedEvent event)
	{
		++reloadCount;
	}

	public static int getReloadCount()
	{
		return reloadCount;
	}

	public Collection<T> getRecipes(@Nonnull Level level)
	{
		updateCache(level.getRecipeManager(), level.isClientSide());
		return Objects.requireNonNull(recipes).values();
	}

	public Collection<ResourceLocation> getRecipeNames(@Nonnull Level level)
	{
		updateCache(level.getRecipeManager(), level.isClientSide());
		return Objects.requireNonNull(recipes).keySet();
	}

	public T getById(@Nonnull Level level, ResourceLocation name)
	{
		updateCache(level.getRecipeManager(), level.isClientSide());
		return recipes.get(name);
	}

	private void updateCache(RecipeManager manager, boolean isClient)
	{
		if(recipes != null && cachedAtReloadCount == reloadCount && (!cachedDataIsClient || isClient))
			return;
		this.recipes = manager.getRecipes().stream().
				filter(iRecipe -> iRecipe.value().getType().toString().equals(type.get().toString())).
				collect(Collectors.toMap(RecipeHolder::id, holder -> (T)holder.value()));
		this.cachedDataIsClient = isClient;
		this.cachedAtReloadCount = reloadCount;
	}
}

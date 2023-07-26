/**
 * @author ArcAnc
 * Created at: 20.07.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.data;

import com.arcanc.nedaire.content.registration.NRegistration;
import com.arcanc.nedaire.util.helpers.StringHelper;
import com.google.common.base.Preconditions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ParticleDescriptionProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NParticlesDescriptionProvider extends ParticleDescriptionProvider
{
    /**
     * Creates an instance of the data provider.
     *
     * @param output     the expected root directory the data generator outputs to
     * @param fileHelper the helper used to validate a texture's existence
     */
    public NParticlesDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper)
    {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions()
    {
        this.sprite(NRegistration.RegisterParticleTypes.ESSENCE.get(), StringHelper.getLocFStr("essence"));
    }

    @Override
    protected void spriteSet(@NotNull ParticleType<?> type, Iterable<ResourceLocation> textures)
    {
        // Make sure particle type is registered
        var particle = Preconditions.checkNotNull(ForgeRegistries.PARTICLE_TYPES.getKey(type), "The particle type is not registered");

        // Validate textures
        List<String> desc = new ArrayList<>();
        for (var texture : textures)
        {
            Preconditions.checkArgument(this.fileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", "textures/misc"),
                    "Texture '%s' does not exist in any known resource pack", texture);
            desc.add(texture.toString());
        }
        Preconditions.checkArgument(desc.size() > 0, "The particle type '%s' must have one texture", particle);

        // Insert into map
        if (this.descriptions.putIfAbsent(particle, desc) != null) {
            throw new IllegalArgumentException(String.format("The particle type '%s' already has a description associated with it", particle));
        }
    }
}

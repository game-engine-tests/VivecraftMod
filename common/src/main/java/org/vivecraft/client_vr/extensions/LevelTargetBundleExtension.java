package org.vivecraft.client_vr.extensions;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public interface LevelTargetBundleExtension {
    ResourceLocation OCCLUDED_TARGET_ID = ResourceLocation.fromNamespaceAndPath("vivecraft", "vroccluded");
    ResourceLocation UNOCCLUDED_TARGET_ID = ResourceLocation.fromNamespaceAndPath("vivecraft", "vrunoccluded");
    ResourceLocation HANDS_TARGET_ID = ResourceLocation.fromNamespaceAndPath("vivecraft", "vrhands");

    Set<ResourceLocation> VR_TARGETS = Set.of(LevelTargetBundle.MAIN_TARGET_ID, LevelTargetBundle.TRANSLUCENT_TARGET_ID, LevelTargetBundle.ITEM_ENTITY_TARGET_ID, LevelTargetBundle.PARTICLES_TARGET_ID, LevelTargetBundle.WEATHER_TARGET_ID, LevelTargetBundle.CLOUDS_TARGET_ID, OCCLUDED_TARGET_ID, UNOCCLUDED_TARGET_ID, HANDS_TARGET_ID);

    ResourceHandle<RenderTarget> vivecraft$getOccluded();

    ResourceHandle<RenderTarget> vivecraft$getUnoccluded();

    ResourceHandle<RenderTarget> vivecraft$getHands();
}

package org.vivecraft.client_vr.render.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.vivecraft.client.utils.Utils;
import org.vivecraft.client_vr.ClientDataHolderVR;
import org.vivecraft.client_vr.MethodHolder;
import org.vivecraft.client_vr.extensions.GameRendererExtension;
import org.vivecraft.client_vr.gameplay.trackers.CameraTracker;
import org.vivecraft.client_vr.render.RenderPass;
import org.vivecraft.client_vr.settings.VRHotkeys;
import org.vivecraft.client_vr.settings.VRSettings;

import java.util.function.Function;

public class VRWidgetHelper {
    private static final RandomSource random = RandomSource.create();
    private static final ItemStackRenderState ITEM_STATE = new ItemStackRenderState();

    public static boolean debug = false;

    public static void renderVRThirdPersonCamWidget() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientDataHolderVR dataholder = ClientDataHolderVR.getInstance();

        if (dataholder.vrSettings.mixedRealityRenderCameraModel) {
            if ((dataholder.currentPass == RenderPass.LEFT || dataholder.currentPass == RenderPass.RIGHT) &&
                (dataholder.vrSettings.displayMirrorMode == VRSettings.MirrorMode.MIXED_REALITY || dataholder.vrSettings.displayMirrorMode == VRSettings.MirrorMode.THIRD_PERSON) &&
                (!ClientDataHolderVR.getInstance().vrSettings.displayMirrorUseScreenshotCamera || !ClientDataHolderVR.getInstance().cameraTracker.isVisible())) {
                float f = 0.35F;

                if (dataholder.interactTracker.isInCamera() && !VRHotkeys.isMovingThirdPersonCam()) {
                    f *= 1.03F;
                }

                renderVRCameraWidget(-0.748F, -0.438F, -0.06F, f, RenderPass.THIRD, ClientDataHolderVR.thirdPersonCameraModel, ClientDataHolderVR.thirdPersonCameraDisplayModel, () ->
                {
                    dataholder.vrRenderer.framebufferMR.bindRead();
                    RenderSystem.setShaderTexture(0, dataholder.vrRenderer.framebufferMR.getColorTextureId());
                }, (face) ->
                {
                    if (face == Direction.NORTH) {
                        return DisplayFace.MIRROR;
                    } else {
                        return face == Direction.SOUTH ? DisplayFace.NORMAL : DisplayFace.NONE;
                    }
                });
            }
        }
    }

    public static void renderVRHandheldCameraWidget() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientDataHolderVR dataholder = ClientDataHolderVR.getInstance();

        if (dataholder.currentPass != RenderPass.CAMERA && dataholder.cameraTracker.isVisible()) {
            float f = 0.25F;

            if (dataholder.interactTracker.isInHandheldCamera() && !dataholder.cameraTracker.isMoving()) {
                f *= 1.03F;
            }

            renderVRCameraWidget(-0.5F, -0.25F, -0.22F, f, RenderPass.CAMERA, CameraTracker.cameraModel, CameraTracker.cameraDisplayModel, () ->
            {
                if (VREffectsHelper.getNearOpaqueBlock(dataholder.vrPlayer.vrdata_world_render.getEye(RenderPass.CAMERA).getPosition(), ((GameRendererExtension) minecraft.gameRenderer).vivecraft$getMinClipDistance()) == null) {
                    dataholder.vrRenderer.cameraFramebuffer.bindRead();
                    RenderSystem.setShaderTexture(0, dataholder.vrRenderer.cameraFramebuffer.getColorTextureId());
                } else {
                    RenderSystem.setShaderTexture(0, ResourceLocation.parse("vivecraft:textures/black.png"));
                    RenderSystem.bindTexture(RenderSystem.getShaderTexture(0));
                }
            }, (face) ->
            {
                return face == Direction.SOUTH ? DisplayFace.NORMAL : DisplayFace.NONE;
            });
        }
    }

    public static void renderVRCameraWidget(float offsetX, float offsetY, float offsetZ, float scale, RenderPass renderPass, ResourceLocation model, ResourceLocation displayModel, Runnable displayBindFunc, Function<Direction, DisplayFace> displayFaceFunc) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientDataHolderVR dataholder = ClientDataHolderVR.getInstance();
        Matrix4fStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushMatrix();

        Vec3 widgetPosition = dataholder.vrPlayer.vrdata_world_render.getEye(renderPass).getPosition();
        Vec3 eye = RenderHelper.getSmoothCameraPosition(dataholder.currentPass, dataholder.vrPlayer.vrdata_world_render);
        Vec3 widgetOffset = widgetPosition.subtract(eye);

        poseStack.translate((float) widgetOffset.x, (float) widgetOffset.y, (float) widgetOffset.z);
        poseStack.mul(dataholder.vrPlayer.vrdata_world_render.getEye(renderPass).getMatrix().toMCMatrix());
        scale = scale * dataholder.vrPlayer.vrdata_world_render.worldScale;
        poseStack.scale(scale, scale, scale);

        if (debug) {
            MethodHolder.rotateDeg(poseStack, 180.0F, 0.0F, 1.0F, 0.0F);
            RenderHelper.renderDebugAxes(0, 0, 0, 0.08F);
            MethodHolder.rotateDeg(poseStack, 180.0F, 0.0F, 1.0F, 0.0F);
        }

        poseStack.translate(offsetX, offsetY, offsetZ);

        BlockPos blockpos = BlockPos.containing(dataholder.vrPlayer.vrdata_world_render.getEye(renderPass).getPosition());
        int i = Utils.getCombinedLightWithMin(minecraft.level, blockpos, 0);

        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        if (minecraft.level != null) {
            RenderSystem.setShader(CoreShaders.RENDERTYPE_ENTITY_CUTOUT_NO_CULL);
        } else {
            RenderSystem.setShader(CoreShaders.POSITION_TEX_COLOR);
        }
        minecraft.gameRenderer.lightTexture().turnOnLightLayer();

        BufferBuilder bufferbuilder;

        PoseStack poseStack2 = new PoseStack();
        RenderHelper.applyVRModelView(dataholder.currentPass, poseStack2);
        poseStack2.last().pose().identity();
        poseStack2.last().normal().mul(new Matrix3f(dataholder.vrPlayer.vrdata_world_render.getEye(renderPass).getMatrix().toMCMatrix()));

        ITEM_STATE.clear();
        minecraft.getModelManager().getItemModel(model).update(ITEM_STATE, ItemStack.EMPTY, minecraft.getItemModelResolver(), ItemDisplayContext.GROUND, null, null, 0);

        if (!ITEM_STATE.isEmpty() && ITEM_STATE.layers[0].model != null) {
            bufferbuilder = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);
            minecraft.getBlockRenderer().getModelRenderer().renderModel(poseStack2.last(), bufferbuilder, null, ITEM_STATE.layers[0].model, 1.0F, 1.0F, 1.0F, i, OverlayTexture.NO_OVERLAY);

            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        }

        RenderSystem.disableBlend();
        displayBindFunc.run();
        RenderSystem.setShader(CoreShaders.RENDERTYPE_ENTITY_SOLID);

        ITEM_STATE.clear();
        minecraft.getModelManager().getItemModel(displayModel).update(ITEM_STATE, ItemStack.EMPTY, minecraft.getItemModelResolver(), ItemDisplayContext.GROUND, null, null, 0);

        if (!ITEM_STATE.isEmpty() && ITEM_STATE.layers[0].model != null) {
            bufferbuilder = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);

            for (BakedQuad bakedquad : ITEM_STATE.layers[0].model.getQuads(null, null, random)) {
                if (displayFaceFunc.apply(bakedquad.getDirection()) != DisplayFace.NONE && bakedquad.getSprite().contents().name().equals(ResourceLocation.parse("vivecraft:transparent"))) {
                    int[] vertexList = bakedquad.getVertices();
                    boolean flag = displayFaceFunc.apply(bakedquad.getDirection()) == DisplayFace.MIRROR;
                    // make normals point up, so they are always bright
                    // TODO: might break with shaders?
                    Vector3f normal = new Matrix3f(poseStack).transform(new Vector3f(0.0F, 1.0F, 0.0F));
                    int j = LightTexture.pack(15, 15);
                    int step = vertexList.length / 4;
                    bufferbuilder.addVertex(
                            Float.intBitsToFloat(vertexList[0]),
                            Float.intBitsToFloat(vertexList[1]),
                            Float.intBitsToFloat(vertexList[2]))
                        .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                        .setUv(flag ? 1.0F : 0.0F, 1.0F)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(j)
                        .setNormal(normal.x, normal.y, normal.z);
                    bufferbuilder.addVertex(
                            Float.intBitsToFloat(vertexList[step]),
                            Float.intBitsToFloat(vertexList[step + 1]),
                            Float.intBitsToFloat(vertexList[step + 2]))
                        .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                        .setUv(flag ? 1.0F : 0.0F, 0.0F)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(j)
                        .setNormal(normal.x, normal.y, normal.z);
                    bufferbuilder.addVertex(
                            Float.intBitsToFloat(vertexList[step * 2]),
                            Float.intBitsToFloat(vertexList[step * 2 + 1]),
                            Float.intBitsToFloat(vertexList[step * 2 + 2]))
                        .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                        .setUv(flag ? 0.0F : 1.0F, 0.0F)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(j)
                        .setNormal(normal.x, normal.y, normal.z);
                    bufferbuilder.addVertex(
                            Float.intBitsToFloat(vertexList[step * 3]),
                            Float.intBitsToFloat(vertexList[step * 3 + 1]),
                            Float.intBitsToFloat(vertexList[step * 3 + 2]))
                        .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                        .setUv(flag ? 0.0F : 1.0F, 1.0F)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(j)
                        .setNormal(normal.x, normal.y, normal.z);
                }
            }
            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        }
        minecraft.gameRenderer.lightTexture().turnOffLightLayer();
        RenderSystem.enableBlend();
        poseStack.popMatrix();
    }

    public enum DisplayFace {
        NONE,
        NORMAL,
        MIRROR
    }
}

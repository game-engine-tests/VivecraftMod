package org.vivecraft.mixin.client_vr.renderer;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.client_vr.VRState;
import org.vivecraft.mixin.client.blaze3d.RenderSystemAccessor;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(LevelRenderer.class)
public class NoSodiumLevelRendererVRMixin {

    @Shadow
    private boolean needsUpdate;

    @Redirect(
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;needsUpdate:Z",
            opcode = Opcodes.PUTFIELD
        ),
        method = "*"
    )
    public void vivecraft$alwaysUpdateCull(LevelRenderer that, boolean old) {
        if (VRState.vrRunning) {
            this.needsUpdate = true;
        } else {
            this.needsUpdate = old;
        }
    }

    @ModifyConstant(method = "renderChunkLayer", constant = @Constant(intValue = 12))
    public int vivecraft$moreTextures(int constant) {
        return RenderSystemAccessor.getShaderTextures().length;
    }
}

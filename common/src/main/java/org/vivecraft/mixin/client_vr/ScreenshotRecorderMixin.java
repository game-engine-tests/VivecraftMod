package org.vivecraft.mixin.client_vr;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Screenshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.vivecraft.client_vr.ClientDataHolderVR;
import org.vivecraft.client_vr.VRState;

@Mixin(Screenshot.class)
public class ScreenshotRecorderMixin {
    @Redirect(method = "takeScreenshot(Lcom/mojang/blaze3d/pipeline/RenderTarget;)Lcom/mojang/blaze3d/platform/NativeImage;", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;bindTexture(I)V"))
    private static void setToLeftEyePlease(int i) {
        if (VRState.vrRunning) {
            RenderSystem.bindTexture(ClientDataHolderVR.getInstance().vrRenderer.framebufferEye0.getColorTextureId());
        } else {
            RenderSystem.bindTexture(i);
        }
    }
}

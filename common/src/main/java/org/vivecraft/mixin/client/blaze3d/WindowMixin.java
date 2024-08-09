package org.vivecraft.mixin.client.blaze3d;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.vivecraft.client.VivecraftVRMod;

@Mixin(Window.class)
public class WindowMixin {
    @Redirect(method = "<init>(Lcom/mojang/blaze3d/platform/WindowEventHandler;Lcom/mojang/blaze3d/platform/ScreenManager;Lcom/mojang/blaze3d/platform/DisplayData;Ljava/lang/String;Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowSizeLimits(JIIII)V"))
    private void redirectWindowSizeLimitCall(long window, int minwidth, int minheight, int maxwidth, int maxheight) {
        System.out.println("Bypassed GLFW.glfwSetWindowSizeLimits call!");
    }
}

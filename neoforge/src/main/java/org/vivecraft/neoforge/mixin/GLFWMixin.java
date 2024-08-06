package org.vivecraft.neoforge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.vivecraft.neoforge.Vivecraft;

@Mixin(targets = "org.lwjgl.glfw.GLFW")
@Pseudo
public class GLFWMixin {
    public void glfwSetWindowSizeLimits(long l, int i1, int i2, int i3, int i4) {
        System.out.println("glfwSetWindowSizeLimits was called, but I have mixined this to prevent errors! THERE IS NO WINDOW TO STOP ME HAHAHAHAHA");
    }
}

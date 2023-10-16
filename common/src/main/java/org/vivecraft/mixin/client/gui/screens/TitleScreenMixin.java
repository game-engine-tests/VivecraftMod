package org.vivecraft.mixin.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.client.gui.screens.UpdateScreen;
import org.vivecraft.client.utils.UpdateChecker;
import org.vivecraft.client_vr.ClientDataHolderVR;
import org.vivecraft.client_vr.VRState;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    //TODO Add config file
//    private final Properties vrConfig = new Properties();
//    private final Path vrConfigPath = Xplat.getConfigPath("vivecraft-config.properties");
    private boolean showError = false;

    private Button updateButton;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PanoramaRenderer;render(FF)V"), method = "render")
    public void vivecraft$maybeNoPanorama(PanoramaRenderer instance, float f, float g) {
        if (VRState.vrRunning && ClientDataHolderVR.getInstance().menuWorldRenderer.isReady()) {
            return;
        }
        instance.render(f, g);
    }
}

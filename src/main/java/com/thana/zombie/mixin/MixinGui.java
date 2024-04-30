package com.thana.zombie.mixin;

import com.thana.zombie.event.handler.MainEventHandler;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {

    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true)
    public void renderVignette(CallbackInfo info) {
        if (MainEventHandler.isZombie) {
            info.cancel();
        }
    }
}

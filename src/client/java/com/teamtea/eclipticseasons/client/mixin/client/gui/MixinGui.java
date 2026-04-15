package com.teamtea.eclipticseasons.client.mixin.client.gui;


import com.teamtea.eclipticseasons.api.misc.BasicWeather;
import com.teamtea.eclipticseasons.client.debug.OverlayEventHandler;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Gui.class})
public abstract class MixinGui implements BasicWeather {


    @Inject(at = {@At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;extractChat(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V")},
            method = {"extractRenderState"})
    private void eclipticseasons$extractRenderState_debug_render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        OverlayEventHandler.onEvent(graphics);
    }
}

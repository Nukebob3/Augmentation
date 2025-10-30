package net.nukebob.augment.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.nukebob.augment.Augmentation;
import net.nukebob.augment.Ench;
import net.nukebob.augment.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method = "drawSlot", at = @At("HEAD"))
    private void drawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        List<Ench> missingEnchantments = Util.getMissingEnchantments(slot.getStack());
        if (!missingEnchantments.isEmpty()) {
            context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, new Color(255, 255, 255, (int)(100*(Augmentation.opacity/(float)Augmentation.maxOpacity))).getRGB());
        }
    }
}

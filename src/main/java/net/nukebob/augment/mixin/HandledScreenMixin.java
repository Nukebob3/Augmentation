package net.nukebob.augment.mixin;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.slot.Slot;
import net.nukebob.augment.Augmentation;
import net.nukebob.augment.Ench;
import net.nukebob.augment.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.ArrayList;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method = "drawSlot", at = @At("HEAD"))
    private void drawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        ArrayList<Ench> missingEnchantments = Util.getMissingEnchantments(slot.getStack());
        if (!missingEnchantments.isEmpty()) {
            int color = new Color(255, (int) (Math.min(missingEnchantments.size()/5f, 1.0f)*150f), 0, (int)(128*Augmentation.opacity/(float)Augmentation.maxOpacity)).getRGB();
            int color2 = new Color(255, (int) (Math.min(missingEnchantments.size()/5f, 1.0f)*150f), 0, (int)(64*Augmentation.opacity/(float)Augmentation.maxOpacity)).getRGB();
            context.drawBorder(slot.x, slot.y, 16, 16, color);
            context.drawBorder(slot.x+1, slot.y+1, 14, 14, color2);
        }
    }
}

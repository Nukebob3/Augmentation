package net.nukebob.augment.mixin;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.nukebob.augment.Augmentation;
import net.nukebob.augment.Ench;
import net.nukebob.augment.Util;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(net.minecraft.item.ItemStack.class)
public abstract class ItemStackMixin {

	@Inject(method = "getTooltip", at=@At("RETURN"), cancellable = true)
	public void getTooltip(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
		if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), KeyBindingHelper.getBoundKeyOf(Augmentation.keyBindingCheck).getCode())) {
			List<Text> tooltip = cir.getReturnValue();

			if (tooltip==null) tooltip = new ArrayList<>();

			net.minecraft.item.ItemStack stack = (net.minecraft.item.ItemStack) (Object) this;
			ArrayList<Ench> missingEnchants = Util.getMissingEnchantments(stack);
			for (Ench ench : missingEnchants) {
				tooltip.add(Text.translatable("enchantment."+ench.id.replace(":",".")).withColor(ench.extra ? Colors.YELLOW : Colors.GREEN).append(Text.literal(getRomanNumerals(ench.level)).withColor(ench.extra ? Colors.YELLOW : Colors.GREEN)));
			}

			cir.setReturnValue(tooltip);
		}
	}

	@Unique
	private String getRomanNumerals(int num) {
		int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
		String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

		StringBuilder roman = new StringBuilder();

		for (int i = 0; i < values.length; i++) {
			while (num >= values[i]) {
				num -= values[i];
				roman.append(symbols[i]);
			}
		}
		return roman.toString().equals("I") ? "" : " " + roman;
	}
}
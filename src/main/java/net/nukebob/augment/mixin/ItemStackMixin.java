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
import net.nukebob.augment.Settings;
import net.nukebob.augment.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(net.minecraft.item.ItemStack.class)
public abstract class ItemStackMixin {
	@Unique
	private static final Map<Character, Character> smallCapsMap = new HashMap<>();

	@Inject(method = "getTooltip", at=@At("RETURN"), cancellable = true)
	public void getTooltip(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
		if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow(), KeyBindingHelper.getBoundKeyOf(Augmentation.keyBindingCheck).getCode())) {
			List<Text> tooltip = cir.getReturnValue();

			if (tooltip==null) tooltip = new ArrayList<>();

			net.minecraft.item.ItemStack stack = (net.minecraft.item.ItemStack) (Object) this;
			List<Ench> missingEnchants = Util.getMissingEnchantments(stack);
			for (Ench ench : missingEnchants) {
				tooltip.add(Text.literal(toSmallCaps(Text.translatable("enchantment."+ench.id.replace(":",".")).getString())).withColor(ench.extra ? Colors.YELLOW : Colors.GREEN).append(Text.literal(toSmallCaps(getRomanNumerals(ench.level))).withColor(ench.extra ? Colors.YELLOW : Colors.GREEN)));
			}
			ArrayList<Ench> maxedEnchantments = new ArrayList<>();
			for (Map.Entry<String, ArrayList<Ench>> entry : Settings.loadConfig().entrySet()) {
				if (stack.getItem().toString().equals(entry.getKey())) {
					maxedEnchantments = entry.getValue();
					break;
				}
			}
			if (missingEnchants.isEmpty()) {
				if (!maxedEnchantments.isEmpty()) {
					tooltip.add(Text.literal(toSmallCaps(Text.translatable("text.augment.maxed").getString())).withColor(2871807));
				} else {
					tooltip.add(Text.literal(toSmallCaps(Text.translatable("text.augment.unenchantable").getString())).withColor(16720979));
				}
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

	@Unique
	private static String toSmallCaps(String input) {
		StringBuilder result = new StringBuilder();
		for (char ch : input.toLowerCase().toCharArray()) {
			result.append(smallCapsMap.getOrDefault(ch, ch));
		}
		return result.toString();
	}

	static {
		smallCapsMap.put('a', 'ᴀ');
		smallCapsMap.put('b', 'ʙ');
		smallCapsMap.put('c', 'ᴄ');
		smallCapsMap.put('d', 'ᴅ');
		smallCapsMap.put('e', 'ᴇ');
		smallCapsMap.put('f', 'ꜰ');
		smallCapsMap.put('g', 'ɢ');
		smallCapsMap.put('h', 'ʜ');
		smallCapsMap.put('i', 'ɪ');
		smallCapsMap.put('j', 'ᴊ');
		smallCapsMap.put('k', 'ᴋ');
		smallCapsMap.put('l', 'ʟ');
		smallCapsMap.put('m', 'ᴍ');
		smallCapsMap.put('n', 'ɴ');
		smallCapsMap.put('o', 'ᴏ');
		smallCapsMap.put('p', 'ᴘ');
		smallCapsMap.put('q', 'ǫ');
		smallCapsMap.put('r', 'ʀ');
		smallCapsMap.put('s', 'ѕ');
		smallCapsMap.put('t', 'ᴛ');
		smallCapsMap.put('u', 'ᴜ');
		smallCapsMap.put('v', 'ᴠ');
		smallCapsMap.put('w', 'ᴡ');
		smallCapsMap.put('x', 'х');
		smallCapsMap.put('y', 'ʏ');
		smallCapsMap.put('z', 'ᴢ');
	}
}
package net.nukebob.augment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Util {
    public static ArrayList<Ench> getMissingEnchantments(ItemStack stack) {
        ArrayList<Ench> currentEnchantments = new ArrayList<>();
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries()) {
            currentEnchantments.add(new Ench(entry.getKey().getIdAsString(), entry.getIntValue()));
        }

        ArrayList<Ench> maxedEnchantments = new ArrayList<>();
        ArrayList<Ench> output = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Ench>> entry : Settings.loadConfig().entrySet()) {
            if (stack.getItem().toString().equals(entry.getKey())) {
                maxedEnchantments = entry.getValue();
                output = new ArrayList<>(entry.getValue());
                break;
            }
        }

        for (Ench ench : maxedEnchantments) {
            for (Ench cEnch : currentEnchantments) {
                if (Objects.equals(ench.id, cEnch.id)) {
                    if (ench.level <= cEnch.level) {
                        output.remove(ench);
                    } else {
                        output.add(output.indexOf(ench),new Ench(ench.id, ench.level, true));
                        output.remove(ench);
                    }
                    break;
                }
            }
        }
        return output;
    }
}
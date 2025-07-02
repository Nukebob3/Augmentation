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

        removeIncompatible(output, currentEnchantments);
        return output;
    }

    public static void removeIncompatible(ArrayList<Ench> maxedEnchants, ArrayList<Ench> currentEnchants) {
        for (Ench e : currentEnchants) {
            switch (e.id) {
                case "minecraft:fortune": {
                    if (hasEnchant(maxedEnchants, "minecraft:silk_touch") != null) {
                        Integer pos = hasEnchant(maxedEnchants, "minecraft:silk_touch");
                        if (pos != null) {
                            maxedEnchants.remove(pos.intValue());
                            if (e.level < 3) {
                                maxedEnchants.add(pos, new Ench(e.id, 3, true));
                            }
                        }
                    }
                } break;
                case "minecraft:silk_touch": {
                    Integer pos = hasEnchant(maxedEnchants, "minecraft:fortune");
                    if (pos != null) {
                        maxedEnchants.remove(pos.intValue());
                    }
                } break;
                case "minecraft:protection", "minecraft:fire_protection", "minecraft:blast_protection", "minecraft:projectile_protection": {
                    Integer pos = hasEnchant(maxedEnchants, new String[]{"minecraft:protection", "minecraft:fire_protection", "minecraft:blast_protection", "minecraft:projectile_protection"});
                    if (pos != null) {
                        maxedEnchants.remove(pos.intValue());
                        if (e.level < 4) {
                            maxedEnchants.add(pos, new Ench(e.id, 4, true));
                        }
                    }
                } break;
                case "minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods": {
                    Integer pos = hasEnchant(maxedEnchants, new String[]{"minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods", "minecraft:density", "minecraft:breach"});
                    if (pos != null) {
                        maxedEnchants.remove(pos.intValue());
                        if (e.level < 5) {
                            maxedEnchants.add(pos, new Ench(e.id, 5, true));
                        }
                    }
                } break;
                case "minecraft:frost_walker": {
                    if (hasEnchant(maxedEnchants, "minecraft:depth_strider") != null) {
                        Integer pos = hasEnchant(maxedEnchants, "minecraft:depth_strider");
                        if (pos != null) {
                            maxedEnchants.remove(pos.intValue());
                            if (e.level < 2) {
                                maxedEnchants.add(pos, new Ench(e.id, 2, true));
                            }
                        }
                    }
                } break;
                case "minecraft:depth_strider": {
                    if (hasEnchant(maxedEnchants, "minecraft:frost_walker") != null) {
                        Integer pos = hasEnchant(maxedEnchants, "minecraft:frost_walker");
                        if (pos != null) {
                            maxedEnchants.remove(pos.intValue());
                            if (e.level < 3) {
                                maxedEnchants.add(pos, new Ench(e.id, 3, true));
                            }
                        }
                    }
                } break;
                case "minecraft:infinity","minecraft:mending": {
                    Integer pos = hasEnchant(maxedEnchants, new String[]{"minecraft:infinity", "minecraft:mending"});
                    if (pos != null) {
                        maxedEnchants.remove(pos.intValue());
                    }
                } break;
                case "minecraft:multishot","minecraft:piercing": {
                    Integer pos = hasEnchant(maxedEnchants, new String[]{"minecraft:multishot", "minecraft:piercing"});
                    if (pos != null) {
                        maxedEnchants.remove(pos.intValue());
                    }
                } break;
                case "minecraft:loyalty": {
                    if (hasEnchant(maxedEnchants, "minecraft:riptide") != null) {
                        Integer pos = hasEnchant(maxedEnchants, "minecraft:riptide");
                        if (pos != null) {
                            maxedEnchants.remove(pos.intValue());
                            if (e.level < 3) {
                                maxedEnchants.add(pos, new Ench(e.id, 3, true));
                            }
                            if (hasEnchant(currentEnchants, "minecraft:channeling") == null) {
                                maxedEnchants.add(new Ench("minecraft:channeling", 1));
                            }
                        }
                    }
                } break;
                case "minecraft:channeling": {
                    if (hasEnchant(maxedEnchants, "minecraft:riptide") != null) {
                        Integer pos = hasEnchant(maxedEnchants, "minecraft:riptide");
                        if (pos != null) {
                            maxedEnchants.remove(pos.intValue());
                            if (hasEnchant(currentEnchants, "minecraft:loyalty") == null) {
                                maxedEnchants.add(new Ench("minecraft:loyalty", 3));
                            } else {
                                Integer pos2 = hasEnchant(currentEnchants, "minecraft:loyalty");
                                if (pos2 != null && maxedEnchants.get(pos2).level < 3) {
                                    maxedEnchants.add(new Ench("minecraft:loyalty", 3, true));
                                }
                            }
                        }
                    }
                } break;
                case "minecraft:riptide": {
                    boolean added = false;
                    if (hasEnchant(maxedEnchants, "minecraft:loyalty") != null) {
                        Integer pos = hasEnchant(maxedEnchants, "minecraft:loyalty");
                        if (pos != null) {
                            maxedEnchants.remove(pos.intValue());
                            if (e.level < 3) {
                                added = true;
                                maxedEnchants.add(pos, new Ench(e.id, 3, true));
                            }
                        }
                    }
                    if (hasEnchant(maxedEnchants, "minecraft:channeling") != null) {
                        Integer pos = hasEnchant(maxedEnchants, "minecraft:loyalty");
                        if (pos != null) {
                            maxedEnchants.remove(pos.intValue());
                            if (e.level < 3 && !added) {
                                maxedEnchants.add(pos, new Ench(e.id, 3, true));
                            }
                        }
                    }
                } break;
                case "minecraft:density": {
                    if (hasEnchant(maxedEnchants, new String[]{"minecraft:breach", "minecraft:smite", "minecraft:bane_of_arthropods"}) != null) {
                        Integer pos = hasEnchant(maxedEnchants, "minecraft:riptide");
                        if (pos != null) {
                            maxedEnchants.remove(pos.intValue());
                            if (e.level < 5) {
                                maxedEnchants.add(pos, new Ench(e.id, 5, true));
                            }
                        }
                    }
                } break;
                case "minecraft:breach": {
                    if (hasEnchant(maxedEnchants, new String[]{"minecraft:density", "minecraft:smite", "minecraft:bane_of_arthropods"}) != null) {
                        Integer pos = hasEnchant(maxedEnchants, "minecraft:riptide");
                        if (pos != null) {
                            maxedEnchants.remove(pos.intValue());
                            if (e.level < 4) {
                                maxedEnchants.add(pos, new Ench(e.id, 4, true));
                            }
                        }
                    }
                } break;
            }
        }
    }

    public static Integer hasEnchant(ArrayList<Ench> list, String ench) {
        for (Ench e : list) {
            if (Objects.equals(e.id, ench)) {
                return list.indexOf(e);
            }
        }
        return null;
    }

    public static Integer hasEnchant(ArrayList<Ench> list, String[] enches) {
        for (Ench e : list) {
            for (String e2 : enches) {
                if (Objects.equals(e.id, e2)) {
                    return list.indexOf(e);
                }
            }
        }
        return null;
    }
}
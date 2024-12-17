package net.nukebob.augment;

import java.util.ArrayList;

public class MaxedItem {
    public static final ArrayList<Ench> helmet = new ArrayList<>(){{
        add(new Ench("minecraft:protection", 4));
        add(new Ench("minecraft:unbreaking", 3));
        add(new Ench("minecraft:mending", 1));
        add(new Ench("minecraft:respiration", 3));
        add(new Ench("minecraft:aqua_affinity", 1));
    }};

    public static final ArrayList<Ench> chestplate = new ArrayList<>(){{
        add(new Ench("minecraft:protection", 4));
        add(new Ench("minecraft:unbeaking", 3));
        add(new Ench("minecraft:mending", 1));
    }};

    public static final ArrayList<Ench> leggings = new ArrayList<>(){{
        add(new Ench("minecraft:protection", 4));
        add(new Ench("minecraft:unbeaking", 3));
        add(new Ench("minecraft:mending", 1));
        add(new Ench("minecraft:swift_sneak", 3));
    }};

    public static final ArrayList<Ench> boots = new ArrayList<>(){{
        add(new Ench("minecraft:protection", 4));
        add(new Ench("minecraft:unbeaking", 3));
        add(new Ench("minecraft:mending", 1));
        add(new Ench("minecraft:feather_falling", 4));
        add(new Ench("minecraft:soul_speed", 3));
        add(new Ench("minecraft:depth_strider", 3));
    }};

    public static final ArrayList<Ench> sword = new ArrayList<>(){{
        add(new Ench("minecraft:sharpness", 5));
        add(new Ench("minecraft:unbreaking", 3));
        add(new Ench("minecraft:mending", 1));
        add(new Ench("minecraft:sweeping_edge", 3));
        add(new Ench("minecraft:fire_aspect", 2));
        add(new Ench("minecraft:looting", 3));
    }};
}

package net.nukebob.augment;

public class Ench {
    public final String id;
    public final int level;
    public final boolean extra;

    public Ench(String id, int level, boolean extra) {
        this.id = id;
        this.level = level;
        this.extra = extra;
    }

    public Ench(String id, int level) {
        this.id = id;
        this.level = level;
        this.extra = false;
    }

    @Override
    public String toString() {
        return "Ench("+id+"="+level+(extra?", extra)" : ")");
    }
}

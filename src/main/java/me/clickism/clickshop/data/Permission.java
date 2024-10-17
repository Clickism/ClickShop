package me.clickism.clickshop.data;

import org.bukkit.entity.Player;

public enum Permission {
    CREATE,
    BUY,
    CUSTOMIZE,
    STOCKPILE,
    EARNINGS_PILE,
    DISPLAY,
    DELETE,
    SALE_TEXT,
    UNLIMITED_STOCK,
    BYPASS_STOCKPILE;

    private static final String PLUGIN_PREFIX = "clickshop";
    private final String permission;

    Permission() {
        String name = name().replace('_', '-').toLowerCase();
        permission = PLUGIN_PREFIX + "." + name;
    }

    public boolean has(Player player) {
        return player.hasPermission(permission);
    }

    public boolean lacks(Player player) {
        return !has(player);
    }

    public boolean lacksAndNotify(Player player) {
        if (lacks(player)) {
            Message.NO_PERMISSION.send(player);
            return true;
        }
        return false;
    }
}

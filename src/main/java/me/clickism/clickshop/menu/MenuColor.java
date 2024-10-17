package me.clickism.clickshop.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum MenuColor {
    BLACK(ChatColor.DARK_GRAY, ChatColor.BLACK,
            Material.BLACK_DYE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE),
    BLUE(ChatColor.BLUE, ChatColor.DARK_BLUE,
            Material.BLUE_DYE,
            Material.BLUE_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE,
            Material.BLUE_STAINED_GLASS_PANE),
    PINK(ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE,
            Material.PINK_DYE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE),
    GREEN(ChatColor.GREEN, ChatColor.DARK_GREEN,
            Material.GREEN_DYE,
            Material.GREEN_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.GREEN_STAINED_GLASS_PANE),
    RED(ChatColor.RED, ChatColor.DARK_RED,
            Material.RED_DYE,
            Material.RED_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE),
    YELLOW(ChatColor.YELLOW, ChatColor.GOLD,
            Material.YELLOW_DYE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE),
    RAINBOW(ChatColor.DARK_AQUA, ChatColor.BLUE,
            Material.CYAN_DYE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE),
    MOSAIC(ChatColor.DARK_AQUA, ChatColor.DARK_GREEN,
            Material.LIME_DYE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE) {
        @Override
        public Button getBackgroundButton(int slot) {
            return new BlankButton(slot, rows[(slot + slot / 9) % 3]);
        }
    },
    CHECKERBOARD(ChatColor.DARK_GRAY, ChatColor.BLACK,
            Material.WHITE_DYE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.WHITE_STAINED_GLASS_PANE) {
        @Override
        public Button getBackgroundButton(int slot) {
            return new BlankButton(slot, rows[slot % 2]);
        }
    };

    public Button getBackgroundButton(int slot) {
        return new BlankButton(slot, rows[slot / 9]);
    }

    public Material getIcon() {
        return dye;
    }

    public ChatColor getPrimaryColor() {
        return primaryColor;
    }

    public ChatColor getSecondaryColor() {
        return secondaryColor;
    }

    protected final ChatColor primaryColor;
    protected final ChatColor secondaryColor;
    protected final Material dye;
    protected final Material[] rows;

    MenuColor(ChatColor primaryColor, ChatColor secondaryColor, Material dye, Material... rows) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.rows = rows;
        this.dye = dye;
    }
}

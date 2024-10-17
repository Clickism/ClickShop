package me.clickism.clickshop.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {

    private static final String DEFAULT_ITEM_FORMAT = "%dx %s";

    /**
     * Puts item amount and formatted item name into given format.
     *
     * @param item item to format
     * @return formatted string with name and amount.
     */
    public static String formatItemWithAmount(String format, ItemStack item) {
        return String.format(format, item.getAmount(), getFormattedName(item));
    }

    /**
     * Puts item amount and formatted item name into the default format.
     * i.E: 16x Diamond
     *
     * @param item item to format
     * @return formatted string with name and amount.
     */
    public static String formatItemWithAmount(ItemStack item) {
        return formatItemWithAmount(DEFAULT_ITEM_FORMAT, item);
    }

    public static String formatLocation(Location location) {
        return formatLocation(location, true);
    }

    public static String formatLocation(Location location, boolean dimension) {
        String suffix = "";
        World world = location.getWorld();
        if (world != null && dimension) {
            switch (world.getEnvironment()) {
                case NETHER:
                    suffix = " (Nether)";
                    break;
                case THE_END:
                    suffix = " (End)";
                    break;
            }
        }
        return location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + suffix;
    }

    public static String getFormattedName(ItemStack item) {
        return Utils.capitalize(
                item.hasItemMeta()
                        ? item.getItemMeta().getDisplayName()
                        : (item.getType().name().toLowerCase()).replace('_', ' '));
    }

    public static String capitalize(String string) {
        StringBuilder builder = new StringBuilder();
        String[] words = string.split(" ");
        for (int i = 0; i < words.length; i++) {
            builder.append(words[i].substring(0, 1).toUpperCase())
                    .append(words[i].substring(1));
            if (i < words.length - 1) builder.append(" ");
        }
        return builder.toString();
    }

    public static void addItem(Player player, ItemStack item) {
        player.getInventory().addItem(item.clone()).values().forEach(drop -> {
            player.getWorld().dropItem(player.getLocation(), drop);
        });
    }

    private static final char[] RAINBOW_CHARS = new char[]{'c', '6', 'e', 'a', 'b', '3', 'd'};

    public static String rainbow(String string, boolean bold) {
        int index = 0;
        StringBuilder text = new StringBuilder();
        char[] array = string.toCharArray();
        for (char c : array) {
            text.append("&").append(RAINBOW_CHARS[index]);
            if (bold) {
                text.append("&l");
            }
            text.append(c);
            if (c != ' ') index++;
            if (index >= RAINBOW_CHARS.length) {
                index = 0;
            }
        }
        return Utils.colorize(text.toString());
    }

    /**
     * a green |
     * b aqua |
     * c red |
     * d pink |
     * e yellow |
     * f white |
     * 0 black |
     * 1 dark blue |
     * 2 dark green |
     * 3 dark aqua |
     * 4 dark red |
     * 5 dark purple |
     * 6 gold |
     * 7 gray |
     * 8 dark gray |
     * 9 blue
     *
     * @return colorized string
     **/
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}

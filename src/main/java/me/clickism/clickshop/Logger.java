package me.clickism.clickshop;

public class Logger {

    public static void info(String message) {
        Main.getMain().getLogger().info(message);
    }

    public static void severe(String message) {
        Main.getMain().getLogger().severe(message);
    }

    public static void warning(String message) {
        Main.getMain().getLogger().warning(message);
    }
}

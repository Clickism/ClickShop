package me.clickism.clickshop;

import me.clickism.clickshop.events.BlockEvent;
import me.clickism.clickshop.events.InteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClickShop extends JavaPlugin {

    public static void main(String[] args) {

    }

    public static DataManager data;
    public static LocalizationManager localization;

    @Override
    public void onEnable() {
        data = new DataManager(this);
        localization = new LocalizationManager();

        Bukkit.getLogger().info("ClickShop activated.");
        Bukkit.getPluginManager().registerEvents(new BlockEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);

        ShopManager.setPlugin(this);
        ShopMenu.setPlugin(this);
        InteractEvent.setPlugin(this);
        Utils.setPlugin(this);

        ShopMenu.setupInventories();
        ShopManager.setupData(data);
        
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.resetTitle();
        });

    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("ClickShop deactivated.");
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.resetTitle();
            ShopManager.clearTethers(p);
        });
    }

    public static DataManager getData() {
        return data;
    }

    public static LocalizationManager getLocalization() {
        return localization;
    }
}
package me.clickism.clickshop.data;

import org.bukkit.configuration.file.FileConfiguration;

public interface DataManager {

    FileConfiguration getConfig();

    void reloadConfig();

    void saveConfig();
}

package me.clickism.clickshop.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Set;

public class SettingManager {

    private final static int VERSION = 1;

    private final static String FILE_NAME = "config";

    private final Plugin plugin;
    private final DataManager dataManager;

    public SettingManager(Plugin plugin) throws IOException {
        this.plugin = plugin;
        dataManager = new YAMLDataManager(plugin, plugin.getDataFolder(), FILE_NAME);
        Object version = get("config-version");
        if (version == null || (int) version != VERSION) {
            // Replace config and inject old values
            injectValues();
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    @Nullable
    public Object get(String path) {
        return dataManager.getConfig().get(path);
    }

    public void set(String path, Object object) {
        dataManager.getConfig().set(path, object);
    }

    public void injectValues() {
        plugin.saveResource(FILE_NAME + ".yml", true);
        FileConfiguration oldConfig = dataManager.getConfig();
        dataManager.reloadConfig();
        Set<String> keys = dataManager.getConfig().getKeys(true);
        keys.forEach(key -> {
            Object value = oldConfig.get(key);
            if (value != null) {
                dataManager.getConfig().set(key, value);
            }
        });
        // Override version
        dataManager.getConfig().set("config-version", VERSION);
        dataManager.saveConfig();
    }
}

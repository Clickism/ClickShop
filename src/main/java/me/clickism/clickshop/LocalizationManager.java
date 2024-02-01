package me.clickism.clickshop;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LocalizationManager {

    // Config for lang-specific msgs
    private FileConfiguration langConfig = null;
    // File for lang-specific msgs
    private File configFile = null;
    // Config for lang settings
    private FileConfiguration langFileConfig = null;
    // File for lang settings
    private File langFile = null;

    // Setup lang configs
    public void setup() {
        // Init lang settings file if null
        if (langFile == null) {
            langFile = new File(ClickShop.getPlugin(ClickShop.class).getDataFolder(), "config_lang.yml");
        }
        // Create lang settings file if not exist
        if (!langFile.exists()) {
            ClickShop.getPlugin(ClickShop.class).saveResource("config_lang.yml", false);
        }
        // Load lang settings config
        langFileConfig = YamlConfiguration.loadConfiguration(langFile);

        // Get lang from settings
        String lang = langFileConfig.getString("language", "ru"); // Балалайка

        // Init lang-specific msgs file if null
        if (configFile == null) {
            configFile = new File(ClickShop.getPlugin(ClickShop.class).getDataFolder(), "messages_" + lang + ".yml");
        }
        // Create lang-specific msgs file if not exist
        if (!configFile.exists()) {
            try (InputStream in = ClickShop.getPlugin(ClickShop.class).getResource("messages_" + lang + ".yml")) {
                // Load and save lang-specific msgs config
                langConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(in));
                langConfig.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Load lang-specific msgs config
            langConfig = YamlConfiguration.loadConfiguration(configFile);
        }
    }

    // Get msg for key
    public String getMessage(String key) {
        // Setup if lang config is null
        if (langConfig == null) {
            setup();
        }
        // Return msg for key or empty string
        return langConfig.getString(key, "");
    }

    // Get lang-specific msgs config
    public FileConfiguration getLangConfig() {
        // Setup if lang config is null
        if (langConfig == null) {
            setup();
        }
        return langConfig;
    }
}
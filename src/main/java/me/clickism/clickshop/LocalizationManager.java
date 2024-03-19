package me.clickism.clickshop;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LocalizationManager {

    private FileConfiguration langConfig = null;
    private File configFile = null;
    private FileConfiguration langFileConfig = null;
    private File langFile = null;
    public void setup() {
        if (langFile == null) {
            langFile = new File(ClickShop.getPlugin(ClickShop.class).getDataFolder(), "config_lang.yml");
        }
        if (!langFile.exists()) {
            ClickShop.getPlugin(ClickShop.class).saveResource("config_lang.yml", false);
        }
        langFileConfig = YamlConfiguration.loadConfiguration(langFile);
        String lang = langFileConfig.getString("language", "ru");
        if (configFile == null) {
            configFile = new File(ClickShop.getPlugin(ClickShop.class).getDataFolder(), "messages_" + lang + ".yml");
        }
        if (!configFile.exists()) {
            try (InputStream in = ClickShop.getPlugin(ClickShop.class).getResource("messages_" + lang + ".yml")) {
                langConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(in));
                langConfig.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            langConfig = YamlConfiguration.loadConfiguration(configFile);
        }
    }
    public String getMessage(String key) {
        if (langConfig == null) {
            setup();
        }
        return langConfig.getString(key, "");
    }
    public FileConfiguration getLangConfig() {
        if (langConfig == null) {
            setup();
        }
        return langConfig;
    }
}
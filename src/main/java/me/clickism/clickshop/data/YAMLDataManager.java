package me.clickism.clickshop.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class YAMLDataManager implements DataManager {

    private final Plugin plugin;

    private final File file;
    private FileConfiguration config;

    public YAMLDataManager(Plugin plugin, @NotNull File directory, String fileName) throws IOException {
        this.plugin = plugin;
        createDirectoryIfNotExists(directory);
        file = new File(directory, fileName + ".yml");

        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
            return;
        }

        saveDefaultConfig();
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe("\"" + file.getName() + "\" config couldn't be saved.");
        }
    }

    private void saveDefaultConfig() throws IOException {
        String path = getTrimmedPath();
        try {
            plugin.saveResource(path, false);
        } catch (IllegalArgumentException exception) {
            if (!file.createNewFile()) throw new IOException("Failed to create file: " + file.getPath());
        }

        config = YamlConfiguration.loadConfiguration(file);
        InputStream defaultStream = plugin.getResource(path);

        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            config.setDefaults(defaultConfig);
            saveConfig();
            defaultStream.close();
        }
    }

    private String getTrimmedPath() {
        String dataFolderPath = plugin.getDataFolder().getPath();
        String filePath = file.getPath();
        if (filePath.startsWith(dataFolderPath)) {
            return filePath.substring(dataFolderPath.length() + 1); //Trim plugin directory
        }
        return filePath;
    }

    private static void createDirectoryIfNotExists(File directory) throws IOException {
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory " + directory.getPath());
        }
    }
}

package me.clickism.clickshop.data;

import me.clickism.clickshop.Debug;
import me.clickism.clickshop.utils.Utils;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageManager {

    private static final int VERSION = 1;

    private static final String DIRECTORY_NAME = "messages";
    private static final String[] SUPPORTED_LANGUAGES = {
            "en_US", "de_DE"
    };

    private final Plugin plugin;
    private final DataManager dataManager;

    private void checkAndUpdateLanguageFiles() {
        int version = getVersion();
        boolean versionMismatch = version != VERSION || Debug.OVERRIDE_MESSAGES;
        for (String lang : SUPPORTED_LANGUAGES) {
            String fileName = DIRECTORY_NAME + File.separator + lang + ".yml";
            boolean fileExists = new File(plugin.getDataFolder(), fileName).exists();
            if (!fileExists || versionMismatch) {
                // Override messages if new version
                plugin.saveResource(DIRECTORY_NAME + File.separator + lang + ".yml", true);
            }
            overwriteVersion();
            dataManager.reloadConfig();
        }
    }

    public MessageManager(Plugin plugin, String languageCode) throws IOException {
        // Save default languages
        this.plugin = plugin;
        File directory = new File(plugin.getDataFolder(), DIRECTORY_NAME);
        dataManager = new YAMLDataManager(plugin, directory, languageCode);
        checkAndUpdateLanguageFiles();
    }

    /**
     * @param path key of message
     * @return colorized string
     */
    @Nullable
    public String get(String path) {
        String message = (String) dataManager.getConfig().get(path);
        if (message == null) {
            return null;
        }
        return Utils.colorize(message);
    }

    public List<String> getLore(String pathToButton) {
        List<String> lore = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            String line = get(pathToButton + "-lore-" + i);
            if (line == null) break;
            lore.add(line);
        }
        return lore;
    }

    private int getVersion() {
        return dataManager.getConfig().getInt("messages-version", 0);
    }

    private void overwriteVersion() {
        dataManager.getConfig().set("messages-version", MessageManager.VERSION);
    }
}

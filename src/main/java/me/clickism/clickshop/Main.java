package me.clickism.clickshop;

import me.clickism.clickshop.data.DataManager;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Setting;
import me.clickism.clickshop.data.YAMLDataManager;
import me.clickism.clickshop.events.*;
import me.clickism.clickshop.menu.MenuListener;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.Pile;
import me.clickism.clickshop.shop.PlayerShopManager;
import me.clickism.clickshop.shop.ShopManager;
import me.clickism.clickshop.shop.connector.ConnectorManager;
import me.clickism.clickshop.shop.display.FrameDisplay;
import me.clickism.clickshop.shop.display.GlassDisplay;
import me.clickism.clickshop.utils.MessageParametizer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Main extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(ItemShop.class, "ItemShop");
        ConfigurationSerialization.registerClass(Pile.class, "Pile");
        ConfigurationSerialization.registerClass(PlayerShopManager.class, "PlayerShopManager");
        ConfigurationSerialization.registerClass(ShopManager.class, "ShopManager");
        ConfigurationSerialization.registerClass(FrameDisplay.class, "FrameDisplay");
        ConfigurationSerialization.registerClass(GlassDisplay.class, "GlassDisplay");
    }

    private static final String SPIGOT_ID = "111190";

    private static Main plugin;

    public static Main getMain() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        loadData();
        initializeAllManagers();
        registerAllEvents();

        // Check for updates
        new UpdateChecker(this, SPIGOT_ID).checkVersion(version -> {
            Logger.info("New version available: " + version);
            MessageParametizer message = Message.UPDATE.parameterizer()
                    .put("version", version);
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.isOp()) {
                    message.send(player);
                }
            });
        });

        getLogger().info("ClickShop activated.");
    }

    @Override
    public void onDisable() {
        if (menuListener != null) menuListener.closeActiveMenus();
        if (connectorManager != null) connectorManager.cancelAllConnections();
        saveData();
        getLogger().info("ClickShop deactivated.");
    }

    private void initializeAllManagers() {
        menuListener = new MenuListener();
        connectorManager = new ConnectorManager();
        chatInputListener = new ChatInputListener(this);
    }

    private void registerAllEvents() {
        registerEvents(
                menuListener,
                connectorManager,
                chatInputListener,
                new ChatEvent(),
                new ShopInteractEvent(),
                new PileInteractEvent(),
                new PileBreakEvent(),
                new ShopBreakEvent(),
                new ShopCreateEvent(),
                new PlaceEvent(),
                new StockpileBreakEvent(),
                new JoinEvent(),
                new ExplodeEvent()
        );

        if (Setting.PROTECT_STOCKPILES.isEnabled()) {
            registerEvents(new StockpileGriefEvent());
        }

        if (Setting.BLOCK_PISTON.isEnabled()) {
            registerEvents(new PistonEvent());
        }
    }

    private void registerEvents(Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    private DataManager data;

    private void loadData() {
        // Initialize enums
        try {
            Setting.initialize();
        } catch (IOException exception) {
            getLogger().severe("Couldn't load config.yml! Default settings will be used.");
            getLogger().severe("Cause: " + exception.getMessage());
            exception.printStackTrace();
        }

        try {
            Message.initialize();
        } catch (IOException exception) {
            getLogger().severe("Couldn't load message.yml! Messages will be empty.");
            getLogger().severe("Cause: " + exception.getMessage());
            exception.printStackTrace();
        }

        try {
            data = new YAMLDataManager(this, getDataFolder(), "data");
        } catch (IOException exception) {
            getLogger().severe("Couldn't load data.yml! Disabling plugin...");
            getLogger().severe("Cause: " + exception.getMessage());
            exception.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (data.getConfig().contains("shop-manager")) {
            shopManager = (ShopManager) data.getConfig().get("shop-manager");
        } else {
            shopManager = new ShopManager();
        }
        if (Setting.RECOVER_SHOP_FILES.isEnabled()) {
            shopManager.recoverAllManagers();
            Logger.warning("Loaded and recovered all shop files. Restart server again to save memory!");
            Setting.RECOVER_SHOP_FILES.set(false);
            Setting.saveSettings();
        }
    }

    public void saveData() {
        data.getConfig().set("shop-manager", shopManager);
        data.saveConfig();
        shopManager.saveAll();
    }

    private ShopManager shopManager;

    public ShopManager getShopManager() {
        return shopManager;
    }

    private ConnectorManager connectorManager;

    public ConnectorManager getConnectorManager() {
        return connectorManager;
    }

    private MenuListener menuListener;

    public MenuListener getMenuListener() {
        return menuListener;
    }

    private ChatInputListener chatInputListener;

    public ChatInputListener getChatInputListener() {
        return chatInputListener;
    }
}

package me.clickism.clickshop.shop;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Setting;
import me.clickism.clickshop.menu.MenuColor;
import me.clickism.clickshop.serialization.ItemShopSerializer;
import me.clickism.clickshop.shop.display.ShopDisplay;
import me.clickism.clickshop.shop.display.ShopDisplayType;
import me.clickism.clickshop.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class ItemShop implements ConfigurationSerializable {

    public static final int VERSION = 1;

    @Nullable
    public static ItemShop get(Location location) {
        return Main.getMain().getShopManager().getShop(location);
    }

    private final Location location;
    private final UUID ownerUUID;
    private ItemStack price;
    private List<ItemStack> products;
    private int earnings;
    private final Set<Location> stockpiles;
    private final Map<Location, Inventory> stockpileInventoryMap;
    private final Inventory stockInventory;
    private MenuColor color;
    private ShopDisplay display;
    private boolean admin;
    private String customName;

    private BuySound buySound;

    private final static int MAX_NAME_LENGTH = 26;

    private boolean deleted = false;

    private boolean notifications;
    private boolean stockWarning;

    public ItemShop(
            Location location,
            Player owner,
            ItemStack price,
            List<ItemStack> products
    ) {
        this(
                location.getBlock().getLocation(),
                owner.getUniqueId(),
                price,
                products,
                0,
                new HashSet<>(),
                createStockInventory(),
                MenuColor.BLACK,
                null,
                false,
                null,
                BuySound.DEFAULT,
                true,
                true
        );
    }

    public ItemShop(
            Location location,
            UUID ownerUUID,
            ItemStack price,
            List<ItemStack> products,
            int earnings,
            Set<Location> stockpiles,
            Inventory stockInventory,
            MenuColor color,
            ShopDisplay display,
            boolean admin,
            String customName,
            BuySound buySound,
            boolean notifications,
            boolean stockWarning
    ) {
        this.location = location;
        this.ownerUUID = ownerUUID;
        this.price = price;
        this.products = products;
        this.earnings = earnings;
        this.admin = admin;
        this.stockpiles = stockpiles;
        this.stockpileInventoryMap = new HashMap<>();
        stockpiles.forEach(stockLocation -> {
            Inventory inventory = getInventoryFromLocation(stockLocation);
            if (inventory != null) stockpileInventoryMap.put(stockLocation, inventory);
        });
        this.stockInventory = stockInventory;
        this.color = color;
        this.display = display;
        this.customName = customName;
        this.buySound = buySound;
        this.notifications = notifications;
        this.stockWarning = stockWarning;
    }

    public static Inventory createStockInventory() {
        return Bukkit.createInventory(null, 27, Message.MENU_STOCK_CHEST.toString());
    }

    public void saveData() {
        Main.getMain().getShopManager().saveData(ownerUUID);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete(Player deleter) {
        // Collect earnings
        if (isOwner(deleter) || Setting.OPERATOR_DELETE_SHOP_COLLECT.isEnabled()) {
            collectEarnings(deleter, true);
        }
        clearDisplay();
        //Drop stock chest contents
        for (ItemStack item : stockInventory.getContents()) {
            if (item != null && location.getWorld() != null) {
                location.getWorld().dropItem(location, item);
            }
        }
        ShopManager shopManager = Main.getMain().getShopManager();
        stockpiles.forEach(location -> shopManager.unregisterStockpileAt(getStockpileSides(location)));
        shopManager.unregisterShop(this);
        deleted = true;
    }

    public void incrementEarnings() {
        if (admin) return;
        earnings++;
    }

    /**
     * Collects earnings from this shop to the player given.
     * Handles messages.
     *
     * @param message sends confirmation message
     * @return collected earnings
     */
    public int collectEarnings(Player player, boolean message) {
        if (earnings <= 0) return 0;
        int collected = earnings;
        ItemStack item = price.clone();
        item.setAmount(price.getAmount() * earnings);
        Utils.addItem(player, item);
        earnings = 0;
        saveData();
        if (!message) return collected;
        Message.COLLECT_EARNINGS.parameterizer()
                .put("earnings", collected * price.getAmount() + " " + Utils.getFormattedName(price))
                .send(player);
        return collected;
    }

    public boolean hasStock() {
        return isAdminShop() || getNextStockedInventory() != null;
    }

    private static boolean containsItems(Inventory inventory, List<ItemStack> items) {
        boolean result = true;
        for (ItemStack item : items) {
            if (!inventory.containsAtLeast(item, item.getAmount())) result = false;
        }
        return result;
    }

    @Nullable
    public Inventory getNextStockedInventory() {
        if (containsItems(stockInventory, products)) return stockInventory;
        for (Inventory inventory : stockpileInventoryMap.values()) {
            if (containsItems(inventory, products)) return inventory;
        }
        return null;
    }

    public void addStockpile(Location location) {
        Inventory inventory = getInventoryFromLocation(location);
        if (inventory == null) {
            return;
        }
        stockpiles.add(location);
        stockpileInventoryMap.put(location, getInventoryFromLocation(location));
        setChestName(location, Message.MENU_STOCKPILE.parameterizer()
                .put("location", Utils.formatLocation(this.location, false))
                .toString());
        Main.getMain().getShopManager().registerStockpileAt(ownerUUID, getStockpileSides(location));
    }

    private void setChestName(Location location, @Nullable String customName) {
        Block block = location.getBlock();
        BlockState state = block.getState();
        ((Nameable) state).setCustomName(customName);
        state.update();
    }

    public void removeStockpile(Location location) {
        stockpiles.remove(location);
        stockpileInventoryMap.remove(location);
        setChestName(location, null);
        Main.getMain().getShopManager().unregisterStockpileAt(getStockpileSides(location));
    }

    public static Location[] getStockpileSides(Location location) {
        BlockState state = location.getBlock().getState();
        if (!(state instanceof InventoryHolder)) {
            return new Location[0];
        }
        InventoryHolder holder = ((InventoryHolder) state).getInventory().getHolder();
        if (holder instanceof DoubleChest) {
            DoubleChest doubleChest = (DoubleChest) holder;

            Chest left = (Chest) doubleChest.getLeftSide();
            Chest right = (Chest) doubleChest.getRightSide();
            if (left != null && right != null) {
                return new Location[]{left.getLocation(), right.getLocation()};
            }
        }
        return new Location[]{location};
    }

    @Nullable
    private static Inventory getInventoryFromLocation(Location location) {
        Block block = location.getBlock();
        if (block.getType() != Material.CHEST && block.getType() != Material.BARREL) return null;
        return ((InventoryHolder) block.getState()).getInventory();
    }

    public void setPrice(ItemStack price) {
        this.price = price;
    }

    public void setProducts(List<ItemStack> products) {
        this.products = products;
        if (display != null) display.update();
    }

    public void setColor(MenuColor color) {
        this.color = color;
    }

    /**
     * Clears old display
     *
     * @param display new display, set null to clear.
     */
    public void setDisplay(@Nullable ShopDisplay display) {
        if (this.display != null) this.display.clear();
        this.display = display;
        updateDisplay();
    }

    /**
     * Updates the current display if there is one.
     */
    public void updateDisplay() {
        if (display != null) display.update();
    }

    /**
     * Clears the current display.
     */
    public void clearDisplay() {
        setDisplay(null);
    }

    public void setAdminShop(boolean admin) {
        this.admin = admin;
    }

    /**
     * @return new copy of shop location
     */
    public Location getLocation() {
        return location.clone();
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public boolean isOwner(Player player) {
        return player.getUniqueId().equals(ownerUUID);
    }

    public String getOwnerName() {
        return Bukkit.getOfflinePlayer(ownerUUID).getName();
    }

    public boolean isAdminShop() {
        return admin;
    }

    public ItemStack getPrice() {
        return price;
    }

    public List<ItemStack> getProducts() {
        return products;
    }

    public int getEarnings() {
        return earnings;
    }

    public MenuColor getColor() {
        return color;
    }

    @Nullable
    public ShopDisplay getDisplay() {
        return display;
    }

    @NotNull
    public ShopDisplayType getDisplayType() {
        return display == null ? ShopDisplayType.NONE : display.getType();
    }

    public Set<Location> getStockpileSet() {
        return stockpiles;
    }

    public Inventory getStockInventory() {
        return stockInventory;
    }

    public boolean hasCustomName() {
        return customName != null;
    }

    public void setCustomName(@Nullable String customName) {
        this.customName = customName;
    }

    @Nullable
    public String getCustomName() {
        return customName;
    }

    public static boolean isValidCustomName(String customName) {
        return customName.length() <= MAX_NAME_LENGTH;
    }

    public BuySound getBuySound() {
        return buySound;
    }

    public void setBuySound(BuySound buySound) {
        this.buySound = buySound;
    }

    public boolean isNotificationsEnabled() {
        return notifications;
    }

    public void setNotificationsEnabled(boolean notifications) {
        this.notifications = notifications;
    }

    public boolean isStockWarningEnabled() {
        return stockWarning;
    }

    public void setStockWarningEnabled(boolean stockWarning) {
        this.stockWarning = stockWarning;
    }

    public static ItemShop deserialize(Map<String, Object> map) {
        return new ItemShopSerializer(map).deserialize();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return ItemShopSerializer.serialize(this);
    }
}

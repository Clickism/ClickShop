package me.clickism.clickshop.serialization;

import me.clickism.clickshop.menu.MenuColor;
import me.clickism.clickshop.shop.BuySound;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.display.ShopDisplay;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemShopSerializer extends Serializer<ItemShop> {

    public ItemShopSerializer(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected @NotNull ItemShop deserializeMap() throws DeserializationException {
        Location location = getOrAbort("location");
        setLocation(location);
        UUID ownerUUID = UUID.fromString(getOrAbort("owner"));
        ItemStack price = getOrAbort("price");
        List<ItemStack> products = getOrAbort("products");

        int earnings = getOrDefault("earnings", 0);
        List<Location> stockpileList = getListOrDefault("stockpiles", new ArrayList<>());
        Set<Location> stockpiles = new HashSet<>(stockpileList);

        Inventory stockInventory = deserializeStockInventory();

        ShopDisplay display = getOrDefault("display", null);
        MenuColor color = getValueOfOrDefault("color", MenuColor.BLACK);

        boolean admin = getOrDefault("admin", false);
        String customName = getOrDefault("name", null);
        BuySound buySound = getValueOfOrDefault("sound", BuySound.DEFAULT);

        boolean notifications = getOrDefault("notifications", true);
        boolean stockWarning = getOrDefault("warning", true);

        ItemShop shop = new ItemShop(
                location, ownerUUID, price, products, earnings, stockpiles,
                stockInventory, color, display, admin, customName, buySound,
                notifications, stockWarning
        );
        if (display != null) display.setShop(shop);
        return shop;
    }

    private Inventory deserializeStockInventory() {
        Inventory stockInventory = ItemShop.createStockInventory();
        List<ItemStack> stockInventoryContents = getOrDefault("inventory", null);
        if (stockInventoryContents == null) return stockInventory;
        stockInventoryContents.forEach(item -> {
            if (item != null) stockInventory.addItem(item);
        });
        return stockInventory;
    }

    public static Map<String, Object> serialize(ItemShop shop) {
        Map<String, Object> map = new HashMap<>();
        map.put("v", ItemShop.VERSION);
        map.put("location", shop.getLocation());
        map.put("owner", shop.getOwnerUUID().toString());
        map.put("price", shop.getPrice());
        map.put("products", shop.getProducts());
        map.put("color", shop.getColor().name());
        map.put("stockpiles", new ArrayList<>(shop.getStockpileSet()));
        map.put("earnings", shop.getEarnings());
        map.put("admin", shop.isAdminShop());

        List<ItemStack> contents = new ArrayList<>();
        for (ItemStack item : shop.getStockInventory().getContents()) {
            if (item != null) contents.add(item);
        }
        map.put("inventory", contents);
        map.put("display", shop.getDisplay());
        map.put("sound", shop.getBuySound().toString());

        map.put("notifications", shop.isNotificationsEnabled());
        map.put("warning", shop.isStockWarningEnabled());

        if (shop.hasCustomName()) {
            map.put("name", shop.getCustomName());
        }
        return map;
    }
}

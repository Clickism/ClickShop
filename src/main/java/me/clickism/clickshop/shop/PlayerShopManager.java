package me.clickism.clickshop.shop;

import me.clickism.clickshop.serialization.PlayerShopManagerSerializer;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerShopManager implements ConfigurationSerializable {

    public static PlayerShopManager deserialize(Map<String, Object> map) {
        return new PlayerShopManagerSerializer(map).deserialize();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return PlayerShopManagerSerializer.serialize(this);
    }

    public static final int VERSION = 1;

    private final Map<Location, ItemShop> shopMap;
    private final Map<Location, Pile> pileMap;

    public PlayerShopManager() {
        this(new HashMap<>(), new HashMap<>());
    }

    public PlayerShopManager(Map<Location, ItemShop> shopMap, Map<Location, Pile> pileMap) {
        this.shopMap = shopMap;
        this.pileMap = pileMap;
    }

    public ItemShop getShop(Location location) {
        return shopMap.get(location);
    }

    public void registerShop(ItemShop shop) {
        shopMap.put(shop.getLocation(), shop);
    }

    public void removeShop(ItemShop shop) {
        shopMap.remove(shop.getLocation());
    }

    public Pile getPile(Location location) {
        return pileMap.get(location);
    }

    public void registerPile(Pile pile) {
        pileMap.put(pile.getLocation(), pile);
    }

    public void removePile(Pile pile) {
        shopMap.remove(pile.getLocation());
    }

    public Collection<ItemShop> getShops() {
        return shopMap.values();
    }

    public Collection<Pile> getPiles() {
        return pileMap.values();
    }
}

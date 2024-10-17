package me.clickism.clickshop.shop;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.MessageType;
import me.clickism.clickshop.data.Setting;
import me.clickism.clickshop.serialization.PileSerializer;
import me.clickism.clickshop.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class Pile implements ConfigurationSerializable {

    @Nullable
    public static Pile get(Location location) {
        return Main.getMain().getShopManager().getPile(location);
    }

    public static final int VERSION = 1;

    private final Location location;
    private final UUID ownerUUID;
    private final Set<Location> shops;

    public Pile(Location location, Player owner) {
        this(location, owner.getUniqueId(), new HashSet<>());
    }

    public Pile(Location location, UUID ownerUUID, Set<Location> shops) {
        this.location = location;
        this.ownerUUID = ownerUUID;
        this.shops = shops;
    }

    public void addShop(ItemShop shop) {
        shops.add(shop.getLocation());

        if (Main.getMain().getShopManager().getPile(location) == null) {
            Main.getMain().getShopManager().registerPile(this);
        }
    }

    public void removeShop(ItemShop shop) {
        shops.remove(shop.getLocation());
        if (shops.isEmpty()) {
            Main.getMain().getShopManager().unregisterPile(this);
        }
    }

    public boolean hasShop(ItemShop shop) {
        return shops.contains(shop.getLocation());
    }

    public Set<Location> getShops() {
        removeDeletedShops();
        return shops;
    }

    private void removeDeletedShops() {
        shops.removeIf(loc -> ItemShop.get(loc) == null);
    }

    public Location getLocation() {
        return location;
    }

    public boolean isOwner(Player player) {
        return player.getUniqueId().equals(ownerUUID);
    }

    public void delete() {
        Main.getMain().getShopManager().unregisterPile(this);
    }

    /**
     * Collects earnings from all connected shops to player given.
     * Handles messages.
     */
    public void collectEarnings(Player player) {
        int total = 0;
        Iterator<Location> iterator = getShops().iterator();
        while (iterator.hasNext()) {
            ItemShop shop = ItemShop.get(iterator.next());
            if (shop == null) continue;
            // Skip shop if player isn't owner and remove from pile
            if (!shop.isOwner(player)) {
                iterator.remove();
                continue;
            }

            int earnings = shop.collectEarnings(player, false);
            total += earnings;

            if (earnings <= 0) continue;

            // Send message
            ItemStack price = shop.getPrice();
            Location location = shop.getLocation();

            Message.COLLECT_PILE_EARNINGS.parameterizer()
                    .put("earnings", earnings * price.getAmount() + " " + Utils.getFormattedName(price))
                    .put("location", location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ())
                    .sendSilently(player);
        }

        if (total > 0) {
            MessageType.CONFIRM.playSound(player);
        } else {
            Message.COLLECT_PILE_NO_EARNINGS.send(player);
        }
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public String getOwnerName() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(ownerUUID);
        return player.getName();
    }

    public static boolean isPileLimitReachedForAndWarn(Player player) {
        int maxPileCount = Setting.EARNINGS_PILE_LIMIT_PER_PLAYER.getInt();
        int pileCount = Main.getMain().getShopManager().getPileCount(player.getUniqueId());
        if (pileCount >= maxPileCount) {
            Message.PILE_LIMIT.parameterizer()
                    .put("limit", maxPileCount)
                    .send(player);
            return true;
        }
        return false;
    }

    public boolean isShopLimitReachedAndWarn(Player player) {
        int maxShopCount = Setting.SHOP_LIMIT_PER_EARNINGS_PILE.getInt();
        if (shops.size() >= maxShopCount) {
            Message.PILE_SHOP_LIMIT.parameterizer()
                    .put("limit", maxShopCount)
                    .send(player);
            return true;
        }
        return false;
    }

    public static Pile deserialize(Map<String, Object> map) {
        return new PileSerializer(map).deserialize();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return PileSerializer.serialize(this);
    }
}

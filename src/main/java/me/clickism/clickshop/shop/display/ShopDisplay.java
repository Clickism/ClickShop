package me.clickism.clickshop.shop.display;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.shop.DisplayHandler;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class ShopDisplay extends DisplayHandler implements ConfigurationSerializable {

    private ItemShop shop;
    private Location location;
    private final ShopDisplayType type;

    protected ShopDisplay(@Nullable ItemShop shop, ShopDisplayType type) {
        this.type = type;
        setShop(shop);
    }

    public final void update() {
        if (shop == null) {
            Main.getMain().getLogger().warning("Shop not set: Can't update display.");
            return;
        }
        prepareDisplays();
        updateDisplay();
    }

    protected abstract void updateDisplay();

    /**
     * Ensures that all displays necessary in updateDisplay() exists and sets their UUID.
     */
    protected abstract void prepareDisplays();

    @NotNull
    public ItemShop getShop() {
        if (shop == null) throw new IllegalStateException("Shop not set!");
        return shop;
    }

    public void setShop(ItemShop shop) {
        this.shop = shop;
        if (shop != null) {
            this.location = shop.getLocation().add(.5, 1, .5);
        }
    }

    public ShopDisplayType getType() {
        return type;
    }

    @NotNull
    protected Location getLocation() {
        if (location == null) throw new IllegalStateException("Shop not set!");
        return location;
    }

    protected BlockDisplay createBlockDisplayIfNotExists(@Nullable UUID uuid) {
        return (BlockDisplay) createDisplayIfNotExists(uuid, EntityType.BLOCK_DISPLAY);
    }

    protected ItemDisplay createItemDisplayIfNotExists(@Nullable UUID uuid) {
        return (ItemDisplay) createDisplayIfNotExists(uuid, EntityType.ITEM_DISPLAY);
    }

    protected TextDisplay createTextDisplayIfNotExists(@Nullable UUID uuid) {
        return (TextDisplay) createDisplayIfNotExists(uuid, EntityType.TEXT_DISPLAY);
    }

    private Display createDisplayIfNotExists(@Nullable UUID uuid, EntityType type) {
        if (exists(uuid)) return (Display) Bukkit.getEntity(uuid);
        return spawnDisplay(getLocation(), type);
    }
}

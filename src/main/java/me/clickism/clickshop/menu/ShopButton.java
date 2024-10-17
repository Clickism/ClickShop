package me.clickism.clickshop.menu;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class ShopButton extends Button {

    private final ItemShop shop;

    public ShopButton(int slot, ItemShop shop) {
        super(slot);
        this.shop = shop;
    }

    @Override
    public boolean isValidClick(InventoryClickEvent event) {
        if (shop != null && shop.isDeleted()) {
            Message.SHOP_ERROR.send(event.getWhoClicked());
            return false;
        }
        return true;
    }

    protected ItemShop getShop() {
        return shop;
    }
}

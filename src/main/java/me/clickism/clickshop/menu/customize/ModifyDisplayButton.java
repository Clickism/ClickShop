package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.display.ShopDisplay;
import me.clickism.clickshop.shop.display.ShopDisplayType;
import org.bukkit.event.inventory.InventoryClickEvent;

abstract class ModifyDisplayButton extends DisplayButton {

    private final ShopDisplayType type;

    public ModifyDisplayButton(int slot, ItemShop shop, ShopDisplayType type) {
        super(slot, shop);
        this.type = type;
    }

    @Override
    public boolean isValidClick(InventoryClickEvent event) {
        if (!super.isValidClick(event)) {
            return false;
        }
        ShopDisplay display = getShop().getDisplay();
        if (display == null) {
            return false;
        }
        return display.getType() == type;
    }
}

package me.clickism.clickshop.menu.settings;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.MessageType;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StockWarningButton extends ShopButton {

    public StockWarningButton(int slot, ItemShop shop) {
        super(slot, shop);
        List<String> lore = Message.BUTTON_STOCK_WARNING_ENABLED.getLore();
        enabled = createItem(
                Message.BUTTON_STOCK_WARNING_ENABLED.toString(), lore, Material.BELL, false
        );
        disabled = createItem(
                Message.BUTTON_STOCK_WARNING_DISABLED.toString(), lore, Material.GRAY_DYE, false
        );
    }

    @Override
    public void clickSound(Player player) {
    }

    private final ItemStack enabled;
    private final ItemStack disabled;

    @Override
    public ItemStack getItem() {
        if (getShop().isStockWarningEnabled()) {
            return enabled;
        }
        return disabled;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemShop shop = getShop();
        shop.setStockWarningEnabled(!shop.isStockWarningEnabled());
        if (shop.isStockWarningEnabled()) {
            MessageType.CONFIRM.playSound(player);
        } else {
            MessageType.WARN.playSound(player);
        }
        // Update item
        event.getInventory().setItem(getSlot(), getItem());
    }
}

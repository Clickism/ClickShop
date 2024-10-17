package me.clickism.clickshop.menu.edit;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.MessageType;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.connector.StockpileConnector;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConnectStockpileButton extends ShopButton {

    public ConnectStockpileButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    private final ItemStack item =
            createItem(Message.BUTTON_CONNECT_STOCK, Material.HOPPER, false);

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        player.closeInventory();
        if (Permission.STOCKPILE.lacksAndNotify(player)) {
            return;
        }

        try {
            new StockpileConnector(getShop(), player);
        } catch (IllegalArgumentException exception) {
            MessageType.FAIL.playSound(player);
        }
    }
}

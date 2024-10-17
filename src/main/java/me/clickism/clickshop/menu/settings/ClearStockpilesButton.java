package me.clickism.clickshop.menu.settings;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ClearStockpilesButton extends ShopButton {

    public ClearStockpilesButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    @Override
    public void clickSound(Player player) {
    }

    private final ItemStack item = createItem(
            Message.BUTTON_CLEAR_STOCKPILES, Material.TNT_MINECART, false
    );

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemShop shop = getShop();
        new ArrayList<>(shop.getStockpileSet())
                .forEach(shop::removeStockpile);
        Message.CLEAR_STOCKPILES.send(player);
        player.closeInventory();
    }
}

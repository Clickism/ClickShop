package me.clickism.clickshop.menu.edit;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.menu.settings.SettingsMenu;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettingsButton extends ShopButton {

    public SettingsButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    private final ItemStack item = createItem(
            Message.BUTTON_SETTINGS, Material.COMPASS, false
    );

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        new SettingsMenu(player, getShop()).open();
    }
}

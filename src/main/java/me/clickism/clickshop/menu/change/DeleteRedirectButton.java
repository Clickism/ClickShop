package me.clickism.clickshop.menu.change;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.menu.delete.DeleteShopMenu;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DeleteRedirectButton extends ShopButton {

    public DeleteRedirectButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    private final ItemStack item = createItem(
            Message.BUTTON_DELETE_REDIRECT, Material.BARRIER, false
    );

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        new DeleteShopMenu(player, getShop()).open();
    }
}

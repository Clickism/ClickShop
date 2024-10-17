package me.clickism.clickshop.menu.delete;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DeleteShopButton extends ShopButton {

    public DeleteShopButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    private final ItemStack item =
            createItem(Message.BUTTON_DELETE_SHOP, Material.BARRIER, false);

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        player.closeInventory();

        ItemShop shop = getShop();
        if (shop.isOwner(player)) {
            Message.DELETE_SHOP.send(player);
            shop.delete(player);
            return;
        }

        if (Permission.DELETE.lacks(player)) {
            Message.SHOP_NO_BREAK.send(player);
            return;
        }

        shop.delete(player);
        Message.SHOP_BREAK_OPERATOR.parameterizer()
                .put("owner", shop.getOwnerName())
                .send(player);
    }
}

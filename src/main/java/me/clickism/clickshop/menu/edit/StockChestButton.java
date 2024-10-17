package me.clickism.clickshop.menu.edit;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class StockChestButton extends ShopButton {

    public StockChestButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    @Override
    public void clickSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, .5f, 1f);
    }

    private final ItemStack item =
            createItem(Message.BUTTON_STOCK_CHEST, Material.CHEST, false);

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        player.openInventory(getShop().getStockInventory());
    }
}

package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.MessageType;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClearDisplayButton extends DisplayButton {

    public ClearDisplayButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    private final ItemStack item = createItem(
            Message.BUTTON_CLEAR_DISPLAY, Material.BARRIER, false
    );

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void clickSound(Player player) {
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemShop shop = getShop();

        if (shop == null) return;
        if (shop.getDisplay() == null) {
            MessageType.FAIL.playSound(player);
            return;
        }

        try {
            shop.setDisplay(null);
            player.closeInventory();
            Message.DISPLAY_CLEAR.send(player);
            sendDisplayParticle(player);
        } catch (Exception ignored) {
            // Can't build display
            MessageType.FAIL.playSound(player);
        }
    }
}

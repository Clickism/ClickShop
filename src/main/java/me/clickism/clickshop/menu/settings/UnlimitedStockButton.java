package me.clickism.clickshop.menu.settings;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.MessageType;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UnlimitedStockButton extends ShopButton {

    public UnlimitedStockButton(int slot, ItemShop shop) {
        super(slot, shop);
        List<String> lore = Message.BUTTON_UNLIMITED_ENABLED.getLore();
        enabled = createItem(
                Message.BUTTON_UNLIMITED_ENABLED.toString(), lore, Material.TOTEM_OF_UNDYING, true
        );
        disabled = createItem(
                Message.BUTTON_UNLIMITED_DISABLED.toString(), lore, Material.TOTEM_OF_UNDYING, false
        );
    }

    @Override
    public void clickSound(Player player) {
    }

    private final ItemStack enabled;
    private final ItemStack disabled;

    @Override
    public ItemStack getItem() {
        if (getShop().isAdminShop()) {
            return enabled;
        }
        return disabled;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if (Permission.UNLIMITED_STOCK.lacksAndNotify(player)) {
            player.closeInventory();
            return;
        }

        ItemShop shop = getShop();
        shop.setAdminShop(!shop.isAdminShop());
        if (shop.isAdminShop()) {
            MessageType.CONFIRM.playSound(player);
        } else {
            MessageType.WARN.playSound(player);
        }
        // Update item
        event.getInventory().setItem(getSlot(), getItem());
    }
}

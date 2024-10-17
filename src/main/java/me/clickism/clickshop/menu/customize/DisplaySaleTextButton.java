package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.display.GlassDisplay;
import me.clickism.clickshop.shop.display.ShopDisplay;
import me.clickism.clickshop.shop.display.ShopDisplayType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DisplaySaleTextButton extends ModifyDisplayButton {

    public DisplaySaleTextButton(int slot, ItemShop shop) {
        super(slot, shop, ShopDisplayType.GLASS);
    }

    private final ItemStack addSale =
            createItem(Message.BUTTON_DISPLAY_ADD_SALE, Material.WRITABLE_BOOK, false);
    private final ItemStack removeSale =
            createItem(Message.BUTTON_DISPLAY_REMOVE_SALE, Material.BOOK, true);

    @Override
    public ItemStack getItem() {
        ShopDisplay display = getShop().getDisplay();
        if (display instanceof GlassDisplay) {
            boolean hasSaleText = ((GlassDisplay) display).hasSaleText();
            if (hasSaleText) {
                return removeSale;
            }
        }
        return addSale;
    }

    @Override
    public void clickSound(Player player) {
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemShop shop = getShop();

        if (Permission.SALE_TEXT.lacksAndNotify(player)) {
            return;
        }

        GlassDisplay display = (GlassDisplay) shop.getDisplay();
        if (display == null) return;
        if (display.hasSaleText()) {
            display.setSaleText(null);
            event.getInventory().setItem(getSlot(), getItem());
            Message.SALE_TEXT_REMOVE.send(player);
            return;
        }

        player.closeInventory();
        Message.SALE_TEXT_TYPE.send(player);
        Main.getMain().getChatInputListener().addChatCallback(player, (String message) -> {
            Bukkit.getScheduler().runTask(Main.getMain(), () -> {
                if (!GlassDisplay.isValidSaleText(message)) {
                    Message.SALE_TEXT_INVALID.send(player);
                    return;
                }
                display.setSaleText(message);
                Message.SALE_TEXT_SET.parameterizer()
                        .setColorizeParameters(false)
                        .put("name", message)
                        .send(player);
            });
        }, 200, Message.SALE_TEXT_CANCEL);
    }
}

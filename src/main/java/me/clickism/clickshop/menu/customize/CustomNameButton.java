package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CustomNameButton extends ShopButton {

    private boolean hasCustomName() {
        return getShop().getCustomName() != null;
    }

    public CustomNameButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    private final ItemStack addName =
            createItem(Message.BUTTON_ADD_CUSTOM_NAME, Material.NAME_TAG, false);
    private final ItemStack removeName =
            createItem(Message.BUTTON_REMOVE_CUSTOM_NAME, Material.NAME_TAG, true);

    @Override
    public ItemStack getItem() {
        return hasCustomName() ? removeName : addName;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemShop shop = getShop();

        if (hasCustomName()) {
            shop.setCustomName(null);
            event.getInventory().setItem(getSlot(), getItem());
            Message.CUSTOM_NAME_REMOVE.send(player);
            return;
        }

        player.closeInventory();
        Message.CUSTOM_NAME_TYPE.send(player);
        Main.getMain().getChatInputListener().addChatCallback(player, (String message) -> {
            if (shop != null && !shop.isDeleted()) {
                if (!ItemShop.isValidCustomName(message)) {
                    Message.CUSTOM_NAME_INVALID.send(player);
                    return;
                }
                shop.setCustomName(message);
                Message.CUSTOM_NAME_SET.parameterizer()
                        .setColorizeParameters(false)
                        .put("name", message)
                        .send(player);
            }
        }, 200, Message.CUSTOM_NAME_CANCEL);
    }
}

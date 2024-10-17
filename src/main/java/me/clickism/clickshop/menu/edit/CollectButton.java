package me.clickism.clickshop.menu.edit;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.utils.Parameterizer;
import me.clickism.clickshop.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CollectButton extends ShopButton {

    public CollectButton(int slot, ItemShop shop) {
        super(slot, shop);
        this.item = createItem(
                Message.BUTTON_COLLECT.toString(),
                getEarningsLore(), Material.DIAMOND, false);
    }

    private final ItemStack item;

    @Override
    public ItemStack getItem() {
        return item;
    }

    public void clickSound(Player player) {
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemShop shop = getShop();

        player.closeInventory();
        if (shop.getEarnings() <= 0) {
            Message.COLLECT_NO_EARNINGS.send(player);
            return;
        }

        shop.collectEarnings(player, true);
    }

    private List<String> getEarningsLore() {
        ItemShop shop = getShop();
        int total = shop.getEarnings() * shop.getPrice().getAmount();

        return Message.BUTTON_COLLECT.getParameterizedLore(new Parameterizer<>()
                .put("earnings", total > 0
                        ? total + " " + Utils.getFormattedName(shop.getPrice())
                        : Message.BUTTON_NONE));
    }
}

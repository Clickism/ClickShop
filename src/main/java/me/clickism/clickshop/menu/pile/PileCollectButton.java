package me.clickism.clickshop.menu.pile;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.PileButton;
import me.clickism.clickshop.shop.Pile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PileCollectButton extends PileButton {

    public PileCollectButton(int slot, Pile pile) {
        super(slot, pile);
    }

    private final ItemStack item =
            createItem(Message.BUTTON_PILE_COLLECT, Material.DIAMOND, false);

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

        Pile pile = getPile();
        if (pile != null) {
            pile.collectEarnings(player);
        }

        player.closeInventory();
    }
}

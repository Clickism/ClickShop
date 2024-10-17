package me.clickism.clickshop.menu;

import me.clickism.clickshop.data.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BlankButton extends Button {

    public BlankButton(int slot, Material material) {
        super(slot);
        this.item.setType(material);
    }

    private final ItemStack item =
            createItem(Message.BLANK, Material.GRAY_STAINED_GLASS_PANE, false);

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
    }
}

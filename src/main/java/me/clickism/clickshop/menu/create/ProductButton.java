package me.clickism.clickshop.menu.create;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ProductButton extends Button {

    public ProductButton(int slot) {
        super(slot);
    }

    private final ItemStack item = createItem(
            Message.BUTTON_PRODUCT, Material.LIGHT_GRAY_STAINED_GLASS_PANE, false
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
    }
}

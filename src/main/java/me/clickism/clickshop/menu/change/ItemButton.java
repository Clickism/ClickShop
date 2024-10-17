package me.clickism.clickshop.menu.change;

import me.clickism.clickshop.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemButton extends Button {

    private final ItemStack item;

    public ItemButton(int slot, Material mat) {
        super(slot);
        this.item = new ItemStack(mat);
    }

    @Override
    public void clickSound(Player player) {
    }

    public ItemButton(int slot, ItemStack item) {
        super(slot);
        this.item = item.clone();
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}

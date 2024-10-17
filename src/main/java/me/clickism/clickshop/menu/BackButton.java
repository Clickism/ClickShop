package me.clickism.clickshop.menu;

import me.clickism.clickshop.data.Message;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BackButton extends Button {

    private final Menu menu;

    public BackButton(int slot, Menu menu) {
        super(slot);
        this.menu = menu;
    }

    @Override
    public void clickSound(Player player) {
        player.playSound(player, Sound.UI_LOOM_SELECT_PATTERN, 1f, 1f);
    }

    private final ItemStack item = createItem(Message.BUTTON_BACK, Material.FLOWER_BANNER_PATTERN, false);

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        menu.open();
    }
}

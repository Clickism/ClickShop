package me.clickism.clickshop.menu.color;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.MenuColor;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ColorButton extends ShopButton {

    private final MenuColor color;

    public ColorButton(int slot, ItemShop shop, MenuColor color) {
        super(slot, shop);
        this.color = color;
        Material mat = color.getIcon();
        boolean selected = mat == shop.getColor().getIcon();
        String name = selected
                ? Message.BUTTON_COLOR_SELECTED.toString()
                : rainbow(Message.BUTTON_COLOR.toString());
        this.item = createItem(
                name, Message.BUTTON_COLOR.getLore(), mat, selected
        );
    }

    private final ItemStack item;

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemShop shop = getShop();
        shop.setColor(color);
        new ColorMenu(player, shop).open();
    }
}

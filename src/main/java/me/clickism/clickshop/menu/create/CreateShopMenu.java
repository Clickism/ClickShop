package me.clickism.clickshop.menu.create;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.EditableClickHandler;
import me.clickism.clickshop.menu.Menu;
import me.clickism.clickshop.menu.MenuColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CreateShopMenu extends Menu {

    public CreateShopMenu(Player player, Location loc) {
        super(player, loc, 45, new EditableClickHandler(), MenuColor.BLACK, Message.MENU_CREATE_SHOP);
    }

    @Override
    protected void setupButtons() {
        addButton(new CreateShopButton(21, getLocation()));
        addButton(new PriceButton(PRICE_SLOT));
        PRODUCT_SLOTS.forEach(i -> addButton(new ProductButton(i)));
    }
}

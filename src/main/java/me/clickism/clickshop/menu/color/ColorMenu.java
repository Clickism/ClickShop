package me.clickism.clickshop.menu.color;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.BackButton;
import me.clickism.clickshop.menu.ClickHandler;
import me.clickism.clickshop.menu.MenuColor;
import me.clickism.clickshop.menu.ShopMenu;
import me.clickism.clickshop.menu.customize.CustomizeShopMenu;
import me.clickism.clickshop.shop.BuySound;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.entity.Player;

public class ColorMenu extends ShopMenu {

    public ColorMenu(Player player, ItemShop shop) {
        super(player, shop, 45, new ClickHandler(),
                shop.getColor().getPrimaryColorFilteredGreen() + Message.MENU_COLOR.toString());
    }

    @Override
    protected void setupButtons() {
        ItemShop shop = getShop();
        MenuColor[] colors = MenuColor.values();
        for (int i = 0; i < colors.length; i++) {
            addButton(new ColorButton(i, shop, colors[i]));
        }
        BuySound[] sounds = BuySound.values();
        for (int i = 0; i < sounds.length; i++) {
            addButton(new BuySoundButton(i + 20, getShop(), sounds[i]));
        }
        addButton(new BackButton(36, new CustomizeShopMenu(getPlayer(), getShop())));
    }
}

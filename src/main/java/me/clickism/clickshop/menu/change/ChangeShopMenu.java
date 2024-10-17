package me.clickism.clickshop.menu.change;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.BackButton;
import me.clickism.clickshop.menu.EditableClickHandler;
import me.clickism.clickshop.menu.ShopMenu;
import me.clickism.clickshop.menu.edit.EditShopMenu;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.entity.Player;

public class ChangeShopMenu extends ShopMenu {

    public ChangeShopMenu(Player player, ItemShop shop) {
        super(player, shop, 45, new EditableClickHandler(), Message.MENU_CHANGE_SHOP);
    }

    @Override
    protected void setupButtons() {
        ItemShop shop = getShop();
        addButton(new SaveChangesButton(12, shop));
        addButton(new DeleteRedirectButton(30, shop));

        addButton(new BackButton(36, new EditShopMenu(getPlayer(), shop)));

        addPriceAndProductButtons(true);
    }
}

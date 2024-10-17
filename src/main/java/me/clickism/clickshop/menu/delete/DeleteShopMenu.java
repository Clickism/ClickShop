package me.clickism.clickshop.menu.delete;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.BackButton;
import me.clickism.clickshop.menu.ClickHandler;
import me.clickism.clickshop.menu.MenuColor;
import me.clickism.clickshop.menu.ShopMenu;
import me.clickism.clickshop.menu.edit.EditShopMenu;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.entity.Player;

public class DeleteShopMenu extends ShopMenu {
    public DeleteShopMenu(Player player, ItemShop shop) {
        super(player, shop, 27, new ClickHandler(), MenuColor.RED,
                Message.MENU_DELETE.parameterizer()
                        .put("owner", shop.getOwnerName())
                        .toString());
    }

    @Override
    protected void setupButtons() {
        addButton(new DeleteShopButton(13, getShop()));
        // If deleter is the owner
        if (getShop().isOwner(getPlayer())) {
            addButton(new BackButton(18, new EditShopMenu(getPlayer(), getShop())));
        }
    }

}

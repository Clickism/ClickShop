package me.clickism.clickshop.menu.settings;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.menu.BackButton;
import me.clickism.clickshop.menu.ClickHandler;
import me.clickism.clickshop.menu.ShopMenu;
import me.clickism.clickshop.menu.edit.EditShopMenu;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.entity.Player;

public class SettingsMenu extends ShopMenu {

    public SettingsMenu(Player player, ItemShop shop) {
        super(player, shop, 45, new ClickHandler(), Message.MENU_SETTINGS);
    }

    @Override
    protected void setupButtons() {
        ItemShop shop = getShop();

        addButton(new NotificationsButton(12, shop));
        addButton(new StockWarningButton(13, shop));
        addButton(new ClearStockpilesButton(14, shop));

        addButton(new BackButton(36, new EditShopMenu(getPlayer(), shop)));

        if (Permission.UNLIMITED_STOCK.has(getPlayer())) {
            addButton(new UnlimitedStockButton(16, shop));
        }
    }
}

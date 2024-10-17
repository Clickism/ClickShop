package me.clickism.clickshop.menu.edit;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.ClickHandler;
import me.clickism.clickshop.menu.ShopMenu;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.entity.Player;

public class EditShopMenu extends ShopMenu {

    public EditShopMenu(Player player, ItemShop shop) throws IllegalArgumentException {
        super(player, shop, 45, new ClickHandler(), shop.hasStock() ? Message.MENU_SHOP_OPEN : Message.MENU_SHOP_CLOSED);
        if (!shop.isOwner(player)) {
            throw new IllegalArgumentException("Player is not owner");
        }
    }

    @Override
    protected void setupButtons() {
        ItemShop shop = getShop();
        addButton(new ChangeShopButton(19, shop));
        addButton(new ConnectPileButton(12, shop));
        addButton(new CollectButton(14, shop));
        addButton(new CustomizeButton(25, shop));
        addButton(new ConnectStockpileButton(30, shop));
        addButton(new StockChestButton(32, shop));
        addButton(new SettingsButton(22, shop));
    }
}

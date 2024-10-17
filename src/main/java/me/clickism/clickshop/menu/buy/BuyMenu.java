package me.clickism.clickshop.menu.buy;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.ClickHandler;
import me.clickism.clickshop.menu.MenuColor;
import me.clickism.clickshop.menu.ShopMenu;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.utils.MessageParametizer;
import org.bukkit.entity.Player;

public class BuyMenu extends ShopMenu {

    public BuyMenu(Player player, ItemShop shop) {
        super(player, shop, 45, new ClickHandler(), getTitle(shop));
    }

    @Override
    protected void setupButtons() {
        ItemShop shop = getShop();
        addButton(new BuyButton(21, shop));

        addPriceAndProductButtons(false);
    }

    private static String getTitle(ItemShop shop) {
        MenuColor color = shop.getColor();
        String customName = shop.getCustomName();
        MessageParametizer parametizer = new MessageParametizer(Message.MENU_BUY)
                .setColorizeParameters(false)
                .put("1", color.getPrimaryColor())
                .put("2", color.getSecondaryColor());
        if (customName == null) {
            parametizer.put("player", shop.getOwnerName());
        } else {
            parametizer.setMessage(Message.MENU_BUY_CUSTOM)
                    .put("name", customName);
        }

        return parametizer.toString();
    }
}

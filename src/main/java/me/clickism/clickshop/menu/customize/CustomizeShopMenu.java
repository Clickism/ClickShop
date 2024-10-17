package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.BackButton;
import me.clickism.clickshop.menu.ClickHandler;
import me.clickism.clickshop.menu.ShopMenu;
import me.clickism.clickshop.menu.edit.EditShopMenu;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.entity.Player;

public class CustomizeShopMenu extends ShopMenu {

    public CustomizeShopMenu(Player player, ItemShop shop) {
        super(player, shop, 45, new ClickHandler(), Message.MENU_CUSTOMIZE);
    }

    @Override
    protected void setupButtons() {
        ItemShop shop = getShop();

        addButton(new ColorMenuButton(19 - 9, shop));
        addButton(new ChangeBlockButton(28 - 9, shop));
        addButton(new CustomNameButton(37 - 9, shop));

        addButton(new BuildGlassDisplayButton(25 - 9, shop));
        addButton(new BuildFrameDisplayButton(34 - 9, shop));
        addButton(new ClearDisplayButton(43 - 9, shop));

        addButton(new BackButton(36, new EditShopMenu(getPlayer(), shop)));

        switch (shop.getDisplayType()) {
            case GLASS:
                addButton(new DisplayChangeBaseButton(24 - 9, shop));
                addButton(new DisplayChangeGlassButton(23 - 9, shop));
                addButton(new DisplayLightButton(22 - 9, shop));
                addButton(new DisplaySaleTextButton(21 - 9, shop));
                break;
            case FRAME:
                addButton(new DisplayMoveToTopButton(33 - 9, shop));
                addButton(new DisplayAddFrameButton(32 - 9, shop));
                addButton(new DisplayRemoveFrameButton(31 - 9, shop));
                break;
        }
    }
}

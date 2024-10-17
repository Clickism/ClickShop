package me.clickism.clickshop.menu.pile;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.ClickHandler;
import me.clickism.clickshop.menu.PileMenu;
import me.clickism.clickshop.shop.Pile;
import org.bukkit.entity.Player;

public class EarningsMenu extends PileMenu {

    public EarningsMenu(Player player, Pile pile) {
        super(player, pile, 27, new ClickHandler(), Message.MENU_EARNINGS);
    }

    @Override
    protected void setupButtons() {
        addButton(new ConnectShopButton(12, getPile()));
        addButton(new PileCollectButton(14, getPile()));
    }
}

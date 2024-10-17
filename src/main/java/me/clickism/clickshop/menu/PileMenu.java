package me.clickism.clickshop.menu;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.shop.Pile;
import org.bukkit.entity.Player;

public abstract class PileMenu extends Menu {

    private final Pile pile;

    public PileMenu(Player player, Pile pile, int size, ClickHandler clickHandler, Message title) {
        super(player, pile.getLocation(), size, clickHandler, MenuColor.PINK, title);
        this.pile = pile;
    }

    protected Pile getPile() {
        return pile;
    }
}

package me.clickism.clickshop.menu;

import me.clickism.clickshop.shop.Pile;

public abstract class PileButton extends Button {

    private final Pile pile;

    public PileButton(int slot, Pile pile) {
        super(slot);
        this.pile = pile;
    }

    protected Pile getPile() {
        return pile;
    }

}

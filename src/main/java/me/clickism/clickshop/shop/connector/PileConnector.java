package me.clickism.clickshop.shop.connector;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.Pile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PileConnector extends Connector {

    private final ItemShop shop;

    public PileConnector(ItemShop shop, Player player) throws IllegalArgumentException {
        super(Message.CONNECT_EARNINGS, shop.getLocation(), player);
        this.shop = shop;
        addCovers();
    }

    @Override
    public void handleConnection(Location target) {
        Player player = getPlayer();
        Pile pile = Pile.get(target);
        // Create earnings pile if it doesn't exist
        if (pile == null && target.getBlock().getType() == Material.ENDER_CHEST) {
            if (Pile.isPileLimitReachedForAndWarn(player)) return;
            pile = new Pile(target, player);
        }
        if (shop == null || pile == null) {
            Message.CONNECTOR_INVALID.send(player);
            return;
        }
        if (pile.hasShop(shop)) {
            pile.removeShop(shop);
            Message.CONNECTOR_REMOVE_EARNINGS.sendSilently(player);
            disconnectEffect(player);
        } else {
            if (pile.isShopLimitReachedAndWarn(player)) return;
            pile.addShop(shop);
            Message.CONNECTOR_EARNINGS.sendSilently(player);
            connectEffect(player, target);
        }
    }

    @Override
    public void addCovers() {
    }
}

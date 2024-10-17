package me.clickism.clickshop.shop.connector;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.Pile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShopConnector extends Connector {

    private final Pile pile;

    public ShopConnector(Pile pile, Player player) throws IllegalArgumentException {
        super(Message.CONNECT_SHOP, pile.getLocation(), player);
        this.pile = pile;
        addCovers();
    }

    @Override
    public void handleConnection(Location target) {
        ItemShop shop = ItemShop.get(target);
        Player player = getPlayer();
        if (pile == null || shop == null) {
            Message.CONNECTOR_INVALID.send(player);
            return;
        }
        if (pile.hasShop(shop)) {
            pile.removeShop(shop);
            Message.CONNECTOR_REMOVE_SHOP.send(player);
        } else {
            if (pile.isShopLimitReachedAndWarn(player)) return;
            pile.addShop(shop);
            Message.CONNECTOR_SHOP.send(player);
            connectEffect(player, target);
        }
    }

    @Override
    public void addCovers() {
        if (pile == null) return;

        for (Location location : pile.getShops()) {
            addCover(new Cover(location, Material.LIGHT_BLUE_STAINED_GLASS));
        }
    }
}

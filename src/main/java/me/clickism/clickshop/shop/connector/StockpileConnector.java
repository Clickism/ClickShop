package me.clickism.clickshop.shop.connector;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Setting;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public class StockpileConnector extends Connector {

    private final ItemShop shop;

    public StockpileConnector(ItemShop shop, Player player) throws IllegalArgumentException {
        super(Message.CONNECT_STOCKPILE, shop.getLocation(), player);
        this.shop = shop;
        addCovers();
    }

    @Override
    public void handleConnection(Location target) {
        Player player = getPlayer();
        Material mat = target.getBlock().getType();
        if (shop == null || ItemShop.get(target) != null || (mat != Material.CHEST && mat != Material.BARREL)) {
            Message.CONNECTOR_INVALID.send(player);
            return;
        }
        Location center = getCenter(target);
        if (shop.getStockpileSet().contains(center)) {
            shop.removeStockpile(center);
            Message.CONNECTOR_REMOVE_STOCKPILE.sendSilently(player);
            disconnectEffect(player);
        } else {
            int maxStockPileCount = Setting.STOCKPILE_LIMIT_PER_SHOP.getInt();
            int stockPileCount = shop.getStockpileSet().size();
            if (stockPileCount >= maxStockPileCount) {
                Message.STOCKPILE_LIMIT.parameterizer()
                        .put("limit", maxStockPileCount)
                        .send(player);
                return;
            }
            shop.addStockpile(center);
            Message.CONNECTOR_STOCKPILE.sendSilently(player);
            connectEffect(player, center);
        }
    }

    private Location getCenter(Location location) {
        BlockState state = location.getBlock().getState();
        if (!(state instanceof InventoryHolder)) {
            return location;
        }
        InventoryHolder holder = ((InventoryHolder) state).getInventory().getHolder();
        if (holder == null) return location;
        return holder.getInventory().getLocation();

    }

    @Override
    public void addCovers() {
        if (shop == null) {
            return;
        }
        for (Location location : shop.getStockpileSet()) {
            for (Location side : ItemShop.getStockpileSides(location)) {
                addCover(new Cover(side, Material.RED_STAINED_GLASS));
            }
        }
    }
}

package me.clickism.clickshop.events;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.shop.ShopManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class StockpileBreakEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        ShopManager shopManager = Main.getMain().getShopManager();
        // Stockpile has owner
        UUID owner = shopManager.getStockpileOwner(location);
        if (owner == null) return;
        event.setCancelled(true);
        if (!owner.equals(player.getUniqueId())) {
            Message.STOCKPILE_NO_BREAK.send(player);
            return;
        }
        Message.STOCKPILE_REMOVE_FIRST.send(player);
    }
}

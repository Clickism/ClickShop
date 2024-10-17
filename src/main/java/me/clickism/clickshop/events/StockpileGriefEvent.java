package me.clickism.clickshop.events;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.shop.ShopManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class StockpileGriefEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.HOPPER) return;
        Location above = event.getBlock().getRelative(BlockFace.UP).getLocation();
        Player player = event.getPlayer();
        if (Permission.BYPASS_STOCKPILE.has(player)) return;
        if (isStockpileAndNotOwner(above, player)) {
            Message.STOCKPILE_NO_HOPPER.send(player);
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onOpen(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;
        Location location = block.getLocation();
        if (block.getType() != Material.CHEST && block.getType() != Material.BARREL) return;
        if (Permission.BYPASS_STOCKPILE.has(player)) return;
        if (isStockpileAndNotOwner(location, player)) {
            Message.STOCKPILE_NOT_OWNER.send(player);
            event.setCancelled(true);
        }
    }

    private boolean isStockpileAndNotOwner(Location location, Player player) {
        ShopManager shopManager = Main.getMain().getShopManager();
        UUID owner = shopManager.getStockpileOwner(location);
        return owner != null && !owner.equals(player.getUniqueId());
    }
}

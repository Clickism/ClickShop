package me.clickism.clickshop.shop.connector;

import me.clickism.clickshop.data.Setting;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConnectorManager implements Listener {

    private final Map<Player, Connector> activeConnectors = new HashMap<>();

    public void registerConnector(Connector connector) {
        activeConnectors.put(connector.getPlayer(), connector);
    }

    public void unregisterConnector(Connector connector) {
        activeConnectors.remove(connector.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onConnect(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        Connector connector = activeConnectors.get(player);
        if (connector == null) return;

        //Connect
        event.setCancelled(true);
        connector.connect(event.getClickedBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCancel(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_AIR) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        //Cancel connector
        Player player = event.getPlayer();
        Connector connector = activeConnectors.get(player);
        if (connector == null) return;

        event.setCancelled(true);
        connector.cancel();
    }

    private final Map<Player, Location> fromMap = new HashMap<>();

    @EventHandler(ignoreCancelled = true)
    public void onTether(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Connector connector = activeConnectors.get(player);
        if (connector == null) {
            fromMap.remove(player);
            return;
        }
        if (event.getTo() == null) return;
        // Check Tether
        Tether tether = connector.getTether();
        if (tether == null) {
            double distance = connector.getOrigin().distance(event.getTo());
            if (distance >= Setting.MAX_CONNECTION_DISTANCE.getInt()) {
                connector.cancel();
            }
            return;
        }
        // Manage map
        if (!fromMap.containsKey(player)) fromMap.put(player, event.getFrom());
        if (fromMap.get(player).distance(event.getTo()) < .5) return;
        fromMap.put(player, event.getTo());
        // Extend tether
        tether.getLastTether().extend(connector);
    }

    public void cancelAllConnections() {
        Iterator<Connector> iterator = activeConnectors.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel(false);
            iterator.remove();
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        cancelConnection(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        cancelConnection(event.getPlayer());
    }

    @EventHandler
    public void onLeash(PlayerUnleashEntityEvent event) {
        Player player = event.getPlayer();
        Connector connector = activeConnectors.get(player);
        if (connector == null) return;
        event.setCancelled(true);
        connector.cancel();
    }

    private void cancelConnection(Player player) {
        Connector connector = activeConnectors.get(player);
        if (connector == null) return;
        connector.cancel();
    }
}

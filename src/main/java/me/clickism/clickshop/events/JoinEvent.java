package me.clickism.clickshop.events;

import me.clickism.clickshop.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(Main.getMain(), task -> {
            Main.getMain().getShopManager().sendWarning(event.getPlayer());
        }, 20L);
    }
}

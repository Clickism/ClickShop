package me.clickism.clickshop;

import me.clickism.clickshop.data.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ChatInputListener implements Listener {

    private final JavaPlugin plugin;

    public ChatInputListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private final Map<Player, Consumer<String>> callbackMap = new ConcurrentHashMap<>();

    public void addChatCallback(Player player, Consumer<String> callback, long timeoutTicks, Message cancelMessage) {
        callbackMap.put(player, callback);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (callbackMap.get(player) == callback) {
                callbackMap.remove(player);
                cancelMessage.send(player);
            }
        }, timeoutTicks);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Consumer<String> callback = callbackMap.get(player);
        if (callback == null) return;
        callback.accept(event.getMessage());
        callbackMap.remove(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        callbackMap.remove(event.getPlayer());
    }
}

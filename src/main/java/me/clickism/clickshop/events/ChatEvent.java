package me.clickism.clickshop.events;

import me.clickism.clickshop.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setMessage(Utils.colorize(event.getMessage()));
    }

}

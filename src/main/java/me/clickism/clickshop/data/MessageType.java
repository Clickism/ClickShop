package me.clickism.clickshop.data;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Stream;

public enum MessageType {
    TRANSACTION("&2[&l$&2] &a") {
        @Override
        public void playSound(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
            Bukkit.getScheduler().runTaskLater(Main.getMain(), task -> {
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 2f);
            }, 2L);
        }
    },
    STOCK_WARNING("&6[&l📢&6] &c") {
        @Override
        public void playSound(Player player) {
            player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1f);
        }
    },
    CONFIRM("&a[✔] ") {
        @Override
        public void playSound(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
            Bukkit.getScheduler().runTaskLater(Main.getMain(), task -> {
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 2f);
            }, 2L);
        }
    },
    FAIL("&c[❌] ") {
        @Override
        public void playSound(Player player) {
            player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, .5f);
        }
    },
    WARN("&e[⚠] ") {
        @Override
        public void playSound(Player player) {
            player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1f);
        }
    };

    private final String prefix;
    private final BaseComponent[] spigotPrefix;
    private final ChatColor spigotPrefixColor;

    MessageType(String prefix) {
        this.prefix = Utils.colorize(prefix);
        this.spigotPrefix = TextComponent.fromLegacyText(this.prefix);
        this.spigotPrefixColor = spigotPrefix[spigotPrefix.length - 1].getColor();
    }

    public abstract void playSound(Player player);

    public void send(CommandSender sender, String message, boolean sound) {
        sender.sendMessage(prefix + message);
        if (sound && sender instanceof Player) playSound((Player) sender);
    }

    public void sendSpigot(CommandSender sender, BaseComponent[] text, boolean sound) {
        if (text.length < 1) return;
        text[0].setColor(spigotPrefixColor);
        BaseComponent[] message = Stream.concat(Arrays.stream(spigotPrefix), Arrays.stream(text))
                .toArray(BaseComponent[]::new);
        sender.spigot().sendMessage(message);
        if (sound && sender instanceof Player) playSound((Player) sender);
    }
}

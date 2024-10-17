package me.clickism.clickshop.shop;

import me.clickism.clickshop.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum BuySound {
    DEFAULT(Material.LIME_DYE, Sound.BLOCK_NOTE_BLOCK_CHIME),
    AMETHYST(Material.AMETHYST_CLUSTER, Sound.BLOCK_AMETHYST_BLOCK_BREAK),
    BIT(Material.EXPERIENCE_BOTTLE, Sound.BLOCK_NOTE_BLOCK_BIT),
    BELL(Material.BELL, Sound.BLOCK_NOTE_BLOCK_BELL),
    PLING(Material.PUFFERFISH, Sound.BLOCK_NOTE_BLOCK_PLING);

    private final Material icon;
    private final Sound sound;

    BuySound(Material icon, Sound sound) {
        this.icon = icon;
        this.sound = sound;
    }

    public Material getIcon() {
        return icon;
    }

    public void playSound(Player player) {
        if (sound == null) return;
        player.playSound(player, sound, 1f, 1f);
        Bukkit.getScheduler().runTaskLater(Main.getMain(), task -> {
            player.playSound(player, sound, 1f, 2f);
        }, 2L);
    }
}

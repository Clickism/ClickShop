package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public abstract class DisplayButton extends ShopButton {

    public DisplayButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    public void sendDisplayParticle(Player player) {
        player.spawnParticle(Particle.GLOW, getShop().getLocation().add(.5, 1, .5), 10, 0, 0, 0);
    }

    protected void playBuildDisplaySound(Player player) {
        player.playSound(player, Sound.ENTITY_ARMOR_STAND_BREAK, SoundCategory.BLOCKS, 1, 1);
    }
}

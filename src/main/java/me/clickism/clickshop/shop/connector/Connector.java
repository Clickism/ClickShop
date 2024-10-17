package me.clickism.clickshop.shop.connector;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Setting;
import org.bukkit.*;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public abstract class Connector {

    private final Player player;
    private final Location origin;
    private final Tether tether;

    private final Message message;

    /**
     * @throws IllegalArgumentException if player is too far away from origin
     */
    public Connector(Message message, Location origin, Player player) throws IllegalArgumentException {
        this.player = player;
        this.origin = origin;
        this.message = message;
        if (Setting.VISUALIZE_TETHERS.isEnabled()) {
            this.tether = new Tether(centerToChest(origin), player);
        } else {
            this.tether = null;
        }
        sendTitle(player);
        Main.getMain().getConnectorManager().registerConnector(this);
    }

    /**
     * Handles messages.
     *
     * @param target location to connect
     */
    void connect(Location target) {
        clear();
        handleConnection(target);
        Main.getMain().getConnectorManager().unregisterConnector(this);
    }

    protected abstract void handleConnection(Location target);

    private final Set<Cover> covers = new HashSet<>();

    protected abstract void addCovers();

    protected void addCover(Cover cover) {
        covers.add(cover);
    }

    /**
     * Cancels connection and clears tethers, covers & title.
     * Handles messages.
     */
    public void cancel() {
        cancel(true);
    }

    void cancel(boolean unregister) {
        clear();
        Message.CONNECTOR_CANCEL.send(player);
        player.playSound(player, Sound.ENTITY_ITEM_FRAME_BREAK, SoundCategory.BLOCKS, 1, 1);
        if (unregister) Main.getMain().getConnectorManager().unregisterConnector(this);
    }

    void clear() {
        if (tether != null) tether.remove();
        covers.forEach(Cover::clear);
        player.resetTitle();
    }

    public void sendTitle(Player player) {
        player.sendTitle("", message.toString(), 0, 600, 0);
    }

    protected void connectEffect(Player player, Location target) {
        player.spawnParticle(Particle.SONIC_BOOM, target.clone().add(.5, .5, .5), 1, 0, 0, 0, 2);
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_FALL, 1f, 1f);
        Bukkit.getScheduler().runTaskLater(Main.getMain(), task -> {
            player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_FALL, 1f, 1.5f);
        }, 4L);
        Bukkit.getScheduler().runTaskLater(Main.getMain(), task -> {
            player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_FALL, 1f, 2f);
        }, 6L);
    }

    protected void disconnectEffect(Player player) {
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_FALL, 1f, .5f);
    }

    public Player getPlayer() {
        return player;
    }

    public Location getOrigin() {
        return origin;
    }

    @Nullable
    public Tether getTether() {
        return tether;
    }

    private static Location centerToChest(Location location) {
        return location.clone().add(.5, .6, .5);
    }
}

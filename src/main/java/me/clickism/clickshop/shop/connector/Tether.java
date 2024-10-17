package me.clickism.clickshop.shop.connector;

import me.clickism.clickshop.data.Setting;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Cod;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Tether {

    public LivingEntity getFrom() {
        return from;
    }

    public LivingEntity getTo() {
        return to;
    }

    private final LivingEntity from;
    private LivingEntity to;
    private Knot knot;
    private Tether subtether;

    static final double MAX_TETHER_LENGTH = 8;

    /**
     * @param fromLocation (absolute) starting location
     * @param player       target player
     * @throws IllegalArgumentException if tether distance is higher than 9.
     */
    Tether(Location fromLocation, Player player) {
        from = spawnCod(fromLocation);
        to = player;
        if (getLength() < MAX_TETHER_LENGTH) {
            from.setLeashHolder(player);
        } else {
            throw new IllegalArgumentException("Tether distance cannot be higher than " + MAX_TETHER_LENGTH + ".");
        }
    }

    public void remove() {
        if (subtether != null) subtether.remove();
        Location voidLocation = from.getLocation();
        voidLocation.setY(-100);
        from.setLeashHolder(null);
        from.teleport(voidLocation);
        from.setHealth(0d);

        if (knot != null) {
            knot.clear();
        }

        if (to instanceof Player) return;

        to.setLeashHolder(null);
        to.teleport(voidLocation);
        to.setHealth(0d);
    }

    public void extend(Connector connector) {
        if (!(to instanceof Player)) return;
        Player player = (Player) to;
        Tether firstTether = connector.getTether();
        if (firstTether == null) return;
        double length = getLength();
        double totalLength = firstTether.getSubtetherCount() * Tether.MAX_TETHER_LENGTH + length;
        boolean subtetherLimitReached = totalLength >= Setting.MAX_CONNECTION_DISTANCE.getInt();

        // Play color
        boolean alert = subtetherLimitReached && length > MIN_ALERT_DISTANCE;
        playExtensionSound(player, (float) length, alert);

        // Check tether distance and create sub tethers
        if (length <= Tether.MAX_TETHER_LENGTH) return;
        if (subtetherLimitReached) {
            // Cancel selection if limit is reached
            connector.cancel();
            return;
        }

        createSubtether();
        connector.sendTitle(player); // Refresh title
        player.playSound(player, Sound.ENTITY_LEASH_KNOT_PLACE, 1f, 1f);
    }

    private static final int MIN_ALERT_DISTANCE = 2;

    private void playExtensionSound(Player player, float distance, boolean alert) {
        float pitch = calculatePitch(distance, alert);
        Sound sound = alert ? Sound.BLOCK_BELL_USE : Sound.ENTITY_ITEM_FRAME_ADD_ITEM;
        player.playSound(player, sound, 1f, pitch);
    }

    private float calculatePitch(float distance, boolean alert) {
        return alert
                ? 1f + (distance - MIN_ALERT_DISTANCE) / ((float) MAX_TETHER_LENGTH - MIN_ALERT_DISTANCE)
                : distance / 15 + .5f;
    }

    public void createSubtether() {
        knot = new Knot(to);
        if (to instanceof Player)
            subtether = new Tether(getOffsetLocation().clone().add(0, 0, -.15), (Player) to);
        convertToStatic();
    }

    /**
     * Converts the current tether to static.
     * Adds a knot.
     * WARNING: Set "from" leash holder to null before executing.
     */
    private void convertToStatic() {
        if (!(to instanceof Player)) return;
        // Convert to static
        from.setLeashHolder(null);
        // Add knot
        Location location = getOffsetLocation();
        // Offset to align
        LivingEntity cod = spawnCod(location);
        from.setLeashHolder(cod);
        to = cod;
    }

    private Location getOffsetLocation() {
        if (knot == null) return to.getLocation();
        return knot.getAnchor().clone().add(.5, .65, .5);
    }

    public double getLength() {
        return getFrom().getLocation().distance(getTo().getLocation());
    }

    public int getSubtetherCount() {
        int count = 0;
        Tether current = subtether;
        while (current != null) {
            current = current.subtether;
            count++;
        }
        return count;
    }

    public Tether getLastTether() {
        Tether current = this;
        while (current.subtether != null) {
            current = current.subtether;
        }
        return current;
    }

    private static Cod spawnCod(Location location) {
        Cod cod = (Cod) location.getWorld().spawnEntity(location, EntityType.COD);
        cod.setAI(false);
        cod.setRemoveWhenFarAway(false);
        cod.setInvisible(true);
        cod.setInvulnerable(true);
        cod.setSilent(true);
        cod.setGravity(false);
        cod.setCollidable(false);
        return cod;
    }
}

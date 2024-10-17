package me.clickism.clickshop.shop;

import me.clickism.clickshop.data.Setting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.joml.AxisAngle4f;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class DisplayHandler {

    protected static final float PI = (float) Math.PI;
    protected static final float HALF_PI = (float) (Math.PI / 2f);
    protected static final AxisAngle4f ZERO_AXIS_4F = new AxisAngle4f(0f, 0f, 0f, 0f);
    private static final float DEFAULT_VIEW_RANGE_MULTIPLIER = .1f;

    public abstract void clear();

    protected static ItemDisplay getItemDisplay(UUID uuid) {
        return (ItemDisplay) Bukkit.getEntity(uuid);
    }

    protected static BlockDisplay getBlockDisplay(UUID uuid) {
        return (BlockDisplay) Bukkit.getEntity(uuid);
    }

    protected static TextDisplay getTextDisplay(UUID uuid) {
        return (TextDisplay) Bukkit.getEntity(uuid);
    }

    /**
     * Spawns a display of a specified type with optimized settings.
     *
     * @param type display entity type
     * @return optimized display entity
     */
    protected static Display spawnDisplay(Location location, EntityType type, float viewRangeMultiplier) {
        World world = location.getWorld();
        if (world == null) throw new IllegalArgumentException("World is null.");
        Display display = (Display) world.spawnEntity(location, type);

        display.setViewRange(viewRangeMultiplier * Setting.DISPLAY_VIEW_RANGE.getInt());
        display.setShadowRadius(0f);
        return display;
    }

    protected static Display spawnDisplay(Location location, EntityType type) {
        return spawnDisplay(location, type, DEFAULT_VIEW_RANGE_MULTIPLIER);
    }

    protected static void removeDisplayIfExists(@Nullable UUID uuid) {
        if (uuid == null) return;
        Entity entity = Bukkit.getEntity(uuid);
        if (entity != null) entity.remove();
    }

    protected static boolean exists(UUID uuid) {
        return uuid != null && Bukkit.getEntity(uuid) != null;
    }
}
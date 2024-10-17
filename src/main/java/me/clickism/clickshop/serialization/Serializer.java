package me.clickism.clickshop.serialization;

import me.clickism.clickshop.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public abstract class Serializer<E> {

    public static class DeserializationException extends Exception {
        public DeserializationException(String message, Location location) {
            super(getMessage(message, location));
        }

        private static String getMessage(String message, Location location) {
            if (location != null) {
                return message + " At: " + location;
            } else {
                return message;
            }
        }
    }

    private Location location = null;
    protected final Map<String, Object> map;

    public Serializer(Map<String, Object> map) {
        this.map = map;
    }

    @Nullable
    public E deserialize() {
        try {
            return deserializeMap();
        } catch (DeserializationException exception) {
            Logger.severe("Error during deserialization: " + exception.getMessage());
        } catch (Exception exception) {
            Logger.severe("An unexpected error occurred during deserialization: " + exception.getMessage());
        }
        return null;
    }

    @NotNull
    protected abstract E deserializeMap() throws DeserializationException;

    @SuppressWarnings("unchecked")
    private <T> T get(@NotNull String key) {
        return (T) map.get(key);
    }

    protected <T> T getOrDefault(String key, T defaultValue) {
        T object = get(key);
        if (object != null) {
            return object;
        }
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Enum<T>> T getValueOfOrDefault(String key, Enum<T> defaultValue) {
        String type = getOrDefault(key, null);
        if (type == null) {
            return (T) defaultValue;
        }
        try {
            return (T) Enum.valueOf(defaultValue.getClass(), type);
        } catch (IllegalArgumentException exception) {
            return (T) defaultValue;
        }
    }

    @NotNull
    protected <T> List<T> getListOrDefault(String key, List<T> defaultValue) {
        return getOrDefault(key, defaultValue);
    }

    @NotNull
    protected <T> T getOrAbort(String key) throws DeserializationException {
        T object = get(key);
        if (object != null) {
            return object;
        }
        throw new DeserializationException("Key \"" + key + "\" couldn't be deserialized.", location);
    }

    protected void setLocation(Location location) {
        this.location = location;
    }

    protected static String encodeLocation(Location location) {
        String worldName = location.getWorld() == null ? "null" : location.getWorld().getName();
        return worldName + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    @Nullable
    protected static Location decodeLocation(String string) {
        String[] arr = string.split(",");
        if (arr.length < 4) return null;
        World world = Bukkit.getWorld(arr[0]);
        if (world == null) return null;
        try {
            int x = Integer.parseInt(arr[1]);
            int y = Integer.parseInt(arr[2]);
            int z = Integer.parseInt(arr[3]);
            return new Location(world, x, y, z);
        } catch (NumberFormatException exception) {
            Logger.severe("Location string couldn't be decoded: " + string);
            return null;
        }
    }
}

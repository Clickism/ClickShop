package me.clickism.clickshop.serialization;

import me.clickism.clickshop.shop.ShopManager;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ShopManagerSerializer extends Serializer<ShopManager> {

    public ShopManagerSerializer(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected @NotNull ShopManager deserializeMap() {
        Map<Location, UUID> uuidMap = new HashMap<>();
        // Maps from uuid -> list<location>
        Map<String, Object> uuids = getOrDefault("uuids", new HashMap<>());
        deserializeLocationMapInto(uuidMap, uuids);
        Map<Location, UUID> stockpileMap = new HashMap<>();
        Map<String, Object> stockpiles = getOrDefault("stockpiles", new HashMap<>());
        deserializeLocationMapInto(stockpileMap, stockpiles);
        Map<UUID, Set<Location>> warningsMap = new HashMap<>();
        Map<String, Object> warnings = getOrDefault("warnings", new HashMap<>());
        deserializeWarningMapInto(warningsMap, warnings);
        return new ShopManager(uuidMap, stockpileMap, warningsMap);
    }

    private void deserializeLocationMapInto(Map<Location, UUID> locationUUIDMap, Map<String, Object> map) {
        map.forEach((uuid, list) -> {
            @SuppressWarnings("unchecked")
            List<Location> locations = ((List<String>) list).stream()
                    .map(Serializer::decodeLocation)
                    .collect(Collectors.toList());
            locations.forEach(loc -> locationUUIDMap.put(loc, UUID.fromString(uuid)));
        });
    }

    private void deserializeWarningMapInto(Map<UUID, Set<Location>> UUIDLocationMap, Map<String, Object> map) {
        map.forEach((uuid, list) -> {
            @SuppressWarnings("unchecked")
            Set<Location> set = ((List<String>) list).stream()
                    .map(Serializer::decodeLocation)
                    .collect(Collectors.toSet());
            UUIDLocationMap.put(UUID.fromString(uuid), set);
        });
    }

    public static Map<String, Object> serialize(ShopManager manager) {
        Map<String, Object> map = new HashMap<>();
        Map<String, List<String>> uuidMap = new HashMap<>();
        manager.getUUIDMap().forEach((location, uuid) -> {
            String uuidString = uuid.toString();
            uuidMap.computeIfAbsent(uuidString, k -> new ArrayList<>());
            uuidMap.get(uuidString).add(encodeLocation(location));
        });
        Map<String, List<String>> stockpileMap = new HashMap<>();
        manager.getStockpileMap().forEach((location, uuid) -> {
            String uuidString = uuid.toString();
            stockpileMap.computeIfAbsent(uuidString, k -> new ArrayList<>());
            stockpileMap.get(uuidString).add(encodeLocation(location));
        });
        Map<String, List<String>> warningMap = new HashMap<>();
        manager.getWarningMap().forEach((uuid, locations) -> {
            String uuidString = uuid.toString();
            List<String> list = new ArrayList<>();
            locations.forEach(location -> list.add(encodeLocation(location)));
            warningMap.put(uuidString, list);
        });
        map.put("v", ShopManager.VERSION);
        map.put("uuids", uuidMap);
        map.put("stockpiles", stockpileMap);
        map.put("warnings", warningMap);
        return map;
    }
}

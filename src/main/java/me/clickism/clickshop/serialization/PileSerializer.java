package me.clickism.clickshop.serialization;

import me.clickism.clickshop.shop.Pile;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PileSerializer extends Serializer<Pile> {

    public PileSerializer(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected @NotNull Pile deserializeMap() throws DeserializationException {
        Location location = getOrAbort("location");
        UUID ownerUUID = UUID.fromString(getOrAbort("owner"));
        List<Location> shopList = getListOrDefault("shops", new ArrayList<>());
        Set<Location> shops = new HashSet<>(shopList);

        return new Pile(location, ownerUUID, shops);
    }

    public static Map<String, Object> serialize(Pile pile) {
        Map<String, Object> map = new HashMap<>();
        map.put("v", Pile.VERSION);
        map.put("location", pile.getLocation());
        map.put("owner", pile.getOwnerUUID().toString());
        map.put("shops", new ArrayList<>(pile.getShops()));
        return map;
    }
}

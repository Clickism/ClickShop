package me.clickism.clickshop.shop.connector;

import me.clickism.clickshop.shop.DisplayHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.UUID;

public class Cover extends DisplayHandler {

    private static final Transformation COVER_TRANSFORMATION = new Transformation(
            new Vector3f(-.505f, -.005f, -.505f), new AxisAngle4f(0f, 0f, 0f, 0f),
            new Vector3f(1.01f, 1.01f, 1.01f), new AxisAngle4f(0f, 0f, 0f, 0f));

    private final UUID displayUUID;

    public Cover(Location location, Material material) {
        location = location.getBlock().getLocation().add(.5, 0, .5);
        this.displayUUID = buildDisplay(location, material).getUniqueId();
    }

    private static final float VIEW_RANGE_MULTIPLIER = .5f;

    private static Display buildDisplay(Location location, Material material) {
        BlockDisplay display = (BlockDisplay) spawnDisplay(location, EntityType.BLOCK_DISPLAY, VIEW_RANGE_MULTIPLIER);
        display.setTransformation(COVER_TRANSFORMATION);
        display.setBlock(material.createBlockData());
        display.setBrightness(new Display.Brightness(15, 15));
        return display;
    }

    @Override
    public void clear() {
        removeDisplayIfExists(displayUUID);
    }

}

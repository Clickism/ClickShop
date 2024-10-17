package me.clickism.clickshop.shop.connector;

import me.clickism.clickshop.shop.DisplayHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.*;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.UUID;

public class Knot extends DisplayHandler {

    private final LivingEntity to;
    private final UUID displayUUID;

    public Knot(LivingEntity to) {
        this.to = to;
        this.displayUUID = buildDisplay(getAnchor()).getUniqueId();
    }

    private static final Transformation BASE_TRANSFORMATION = new Transformation(
            new Vector3f(0, -.0625f, 0), ZERO_AXIS_4F,
            new Vector3f(1, 1.0625f, 1), ZERO_AXIS_4F);

    private static final Transformation SLAB_TRANSFORMATION = new Transformation(
            new Vector3f(0, -.5625f, 0), ZERO_AXIS_4F,
            new Vector3f(1, 1, 1), ZERO_AXIS_4F);

    private static Display buildDisplay(Location location) {
        BlockDisplay display = (BlockDisplay) spawnDisplay(location, EntityType.BLOCK_DISPLAY);
        display.setBlock(Material.OAK_FENCE.createBlockData());

        boolean isBottomSlab = isBottomSlab(location.getBlock().getRelative(BlockFace.DOWN).getLocation());
        display.setTransformation(isBottomSlab ? SLAB_TRANSFORMATION : BASE_TRANSFORMATION);

        return display;
    }

    @Override
    public void clear() {
        removeDisplayIfExists(displayUUID);
    }

    private Location anchor;

    @NotNull
    public Location getAnchor() {
        if (anchor == null) anchor = calculateAnchor();
        return anchor;
    }

    private Location calculateAnchor() {
        Location block = to.getLocation().getBlock().getLocation();
        anchor = findGroundAnchor(block);
        return adjustHeight(anchor, to);
    }

    private static final int MAX_DOWNWARDS_CHECK = 6;

    private static Location findGroundAnchor(Location anchor) {
        for (int i = 0; i < MAX_DOWNWARDS_CHECK; i++) {
            Block block = anchor.getBlock().getRelative(BlockFace.DOWN, i);
            Block under = anchor.getBlock().getRelative(BlockFace.DOWN, i + 1);
            if (!block.getType().isSolid() && under.getType().isSolid()) {
                return block.getLocation();
            }
        }
        return anchor;
    }

    private static Location adjustHeight(Location location, Entity entity) {
        if (entity.getLocation().getY() % 1 > .8) {
            return location.clone().add(0, 1, 0);
        }
        if (isBottomSlab(location)) {
            return location.clone().add(0, .5, 0);
        }
        return location;
    }

    private static boolean isBottomSlab(Location location) {
        return location.getBlock().getBlockData() instanceof Slab &&
                ((Slab) location.getBlock().getBlockData()).getType() == Slab.Type.BOTTOM;
    }
}

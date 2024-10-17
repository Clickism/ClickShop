package me.clickism.clickshop.shop.display;

import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FrameDisplay extends ShopDisplay {

    private UUID topUUID;
    private UUID bottomUUID;
    private UUID itemUUID;

    /**
     * Creates a new frame display.
     *
     * @param shop shop to build frame display on
     * @throws IllegalArgumentException if shop is a chest block
     */
    public FrameDisplay(ItemShop shop) throws IllegalArgumentException {
        super(shop, ShopDisplayType.FRAME);
        if (shop.getLocation().getBlock().getType() == Material.CHEST) {
            throw new IllegalArgumentException("Shop block can't be a chest for frame displays!");
        }
    }

    private FrameDisplay(UUID topUUID, UUID bottomUUID, UUID itemUUID) {
        super(null, ShopDisplayType.FRAME);
        this.topUUID = topUUID;
        this.bottomUUID = bottomUUID;
        this.itemUUID = itemUUID;
    }

    @Override
    public void updateDisplay() {
        ItemShop shop = getShop();
        Player player = Bukkit.getPlayer(shop.getOwnerUUID());
        if (player == null) return;

        ItemDisplay item = getItemDisplay(itemUUID);
        ItemStack product = shop.getProducts().get(0).clone();
        item.setItemStack(product);

        float offset = product.getType().isBlock() ? .3f : .5f;
        item.setTransformation(getItemTransformation(player, offset));
    }

    public void moveToTop() {
        update();
        ItemDisplay item = getItemDisplay(itemUUID);
        boolean block = item.getItemStack() != null && item.getItemStack().getType().isBlock();
        item.setTransformation(getTopTransformationOf(item.getTransformation(), block));
    }

    @Override
    public void clear() {
        removeDisplayIfExists(topUUID);
        removeDisplayIfExists(bottomUUID);
        removeDisplayIfExists(itemUUID);
    }

    @Override
    protected void prepareDisplays() {
        itemUUID = createItemDisplayIfNotExists(itemUUID).getUniqueId();
    }

    /**
     * Sets the frame material for the frame display.
     *
     * @param material material, set air or null to remove frame.
     */
    public void setFrame(Material material) {
        if (material == Material.AIR || material == null) {
            clearFrame();
            return;
        }

        prepareFrameDisplays();
        // Set up frame displays
        BlockDisplay top = getBlockDisplay(topUUID);
        BlockDisplay bottom = getBlockDisplay(bottomUUID);

        top.setBlock(material.createBlockData());
        bottom.setBlock(material.createBlockData());

        top.setTransformation(TOP_FRAME_TRANSFORMATION);
        bottom.setTransformation(BOTTOM_FRAME_TRANSFORMATION);
    }

    private void prepareFrameDisplays() {
        topUUID = createBlockDisplayIfNotExists(topUUID).getUniqueId();
        bottomUUID = createBlockDisplayIfNotExists(bottomUUID).getUniqueId();
    }

    @Nullable
    public Material getFrame() {
        BlockDisplay frame;
        if (topUUID == null || (frame = (BlockDisplay) Bukkit.getEntity(topUUID)) == null) {
            return null;
        }
        return frame.getBlock().getMaterial();
    }

    private void clearFrame() {
        removeDisplayIfExists(topUUID);
        removeDisplayIfExists(bottomUUID);
    }

    private static final Vector3f ITEM_SCALE = new Vector3f(.5f, .5f, .5f);
    private static final Vector3f FRAME_SCALE = new Vector3f(1.05f, .075f, 1.05f);

    private static final Transformation TOP_FRAME_TRANSFORMATION = new Transformation(
            new Vector3f(-.525f, -.135f, -.525f), ZERO_AXIS_4F, FRAME_SCALE, ZERO_AXIS_4F);

    private static final Transformation BOTTOM_FRAME_TRANSFORMATION = new Transformation(
            new Vector3f(-.525f, -.925f, -.525f), ZERO_AXIS_4F, FRAME_SCALE, ZERO_AXIS_4F);

    private static final Vector3f TOP_FACING_TRANSLATION = new Vector3f(0f, .01f, 0f);

    private Transformation getItemTransformation(Player player, float offset) {
        return new Transformation(
                getTranslation(player, offset),
                getRotation(player),
                ITEM_SCALE,
                ZERO_AXIS_4F);
    }

    private Vector3f getTranslation(Player player, float offset) {
        switch (player.getFacing()) {
            case NORTH:
                return new Vector3f(0f, -.49f, offset);
            case SOUTH:
                return new Vector3f(0f, -.49f, -offset);
            case WEST:
                return new Vector3f(offset, -.49f, 0f);
            default:
                return new Vector3f(-offset, -.49f, 0f);
        }
    }

    private AxisAngle4f getRotation(Player player) {
        switch (player.getFacing()) {
            case SOUTH:
                return ZERO_AXIS_4F;
            case NORTH:
                return new AxisAngle4f(PI, 0f, 1f, 0f);
            case EAST:
                return new AxisAngle4f(HALF_PI, 0f, 1f, 0f);
            default:
                return new AxisAngle4f(HALF_PI, 0f, -1f, 0f);
        }
    }

    private Transformation getTopTransformationOf(Transformation transformation, boolean block) {
        return new Transformation(
                TOP_FACING_TRANSLATION,
                new AxisAngle4f(transformation.getLeftRotation()),
                ITEM_SCALE,
                block ? ZERO_AXIS_4F : new AxisAngle4f(HALF_PI, 1f, 0f, 0f));
    }

    public static FrameDisplay deserialize(Map<String, Object> map) {
        String top = (String) map.get("top");
        String bottom = (String) map.get("bottom");
        String item = (String) map.get("item");
        UUID topUUID = top != null ? UUID.fromString(top) : null;
        UUID bottomUUID = bottom != null ? UUID.fromString(bottom) : null;
        UUID itemUUID = item != null ? UUID.fromString(item) : null;
        return new FrameDisplay(topUUID, bottomUUID, itemUUID);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        if (topUUID != null) map.put("top", topUUID.toString());
        if (bottomUUID != null) map.put("bottom", bottomUUID.toString());
        if (itemUUID != null) map.put("item", itemUUID.toString());
        return map;
    }
}

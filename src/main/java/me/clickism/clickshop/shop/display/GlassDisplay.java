package me.clickism.clickshop.shop.display;

import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

public class GlassDisplay extends ShopDisplay {

    private static final int MAX_SALE_TEXT_LENGTH = 26;

    public static final Set<Material> GLASS_OPTIONS = new HashSet<>(Arrays.asList(Material.GLASS, Material.TINTED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS, Material.BLACK_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.ORANGE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.WHITE_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS, Material.BROWN_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.PINK_STAINED_GLASS));

    private static final Material DEFAULT_GLASS = Material.GLASS;
    private static final Material DEFAULT_BASE = Material.STRIPPED_OAK_LOG;

    private static final Transformation TEXT_TRANSFORMATION = new Transformation(
            new Vector3f(0f, .7f, 0f),
            ZERO_AXIS_4F,
            new Vector3f(1f, 1f, 1f),
            ZERO_AXIS_4F
    );

    private UUID glassUUID;
    private UUID baseUUID;
    private UUID itemUUID;
    private UUID textUUID;

    public GlassDisplay(ItemShop shop) throws IllegalArgumentException {
        super(shop, ShopDisplayType.GLASS);
        Location location = shop.getLocation();

        Material blockAbove = location.getBlock().getRelative(BlockFace.UP).getType();
        if (blockAbove != Material.AIR && blockAbove != Material.LIGHT) {
            throw new IllegalArgumentException("Block above must be air or light for glass displays!");
        }
    }

    private GlassDisplay(UUID glassUUID, UUID baseUUID, UUID itemUUID, UUID textUUID) {
        super(null, ShopDisplayType.GLASS);
        this.glassUUID = glassUUID;
        this.baseUUID = baseUUID;
        this.itemUUID = itemUUID;
        this.textUUID = textUUID;
    }

    @Override
    protected void updateDisplay() {
        boolean isChest = getShop().getLocation().getBlock().getType() == Material.CHEST;

        // Update transformations
        BlockDisplay glass = getBlockDisplay(glassUUID);
        glass.setTransformation(getGlassTransformation(isChest));
        if (glass.getBlock().getMaterial() == Material.AIR) {
            glass.setBlock(DEFAULT_GLASS.createBlockData());
        }
        BlockDisplay base = getBlockDisplay(baseUUID);
        base.setTransformation(getGlassTransformation(isChest));
        if (base.getBlock().getMaterial() == Material.AIR) {
            base.setBlock(DEFAULT_BASE.createBlockData());
        }
        base.setTransformation(getBaseTransformation(isChest));

        ItemDisplay item = getItemDisplay(itemUUID);
        ItemStack product = getShop().getProducts().get(0).clone();
        item.setTransformation(getItemTransformation(product.getType(), isChest));

        item.setItemStack(product);
        item.setBillboard(Display.Billboard.VERTICAL);
    }

    @Override
    protected void prepareDisplays() {
        glassUUID = createBlockDisplayIfNotExists(glassUUID).getUniqueId();
        baseUUID = createBlockDisplayIfNotExists(baseUUID).getUniqueId();
        itemUUID = createItemDisplayIfNotExists(itemUUID).getUniqueId();
    }

    public boolean hasSaleText() {
        return textUUID != null;
    }

    public void setSaleText(@Nullable String saleText) {
        if (saleText == null) {
            removeDisplayIfExists(textUUID);
            textUUID = null;
            return;
        }
        textUUID = createTextDisplayIfNotExists(textUUID).getUniqueId();
        TextDisplay text = getTextDisplay(textUUID);
        text.setTransformation(TEXT_TRANSFORMATION);
        text.setText(Utils.rainbow(saleText, true));
        text.setBillboard(Display.Billboard.VERTICAL);
    }

    @Override
    public void clear() {
        Block above = getShop().getLocation().getBlock().getRelative(BlockFace.UP);
        if (above.getType() == Material.LIGHT) {
            above.setType(Material.AIR);
        }
        removeDisplayIfExists(glassUUID);
        removeDisplayIfExists(baseUUID);
        removeDisplayIfExists(itemUUID);
        removeDisplayIfExists(textUUID);
    }

    /**
     * @param material valid glass variant
     * @return if material was valid
     */
    public boolean setGlassMaterial(Material material) {
        if (!GLASS_OPTIONS.contains(material)) return false;
        setMaterial(glassUUID, material);
        return true;
    }

    public void setBaseMaterial(Material material) {
        setMaterial(baseUUID, material);
    }

    public Material getGlassMaterial() {
        if (!exists(glassUUID)) return DEFAULT_GLASS;
        return getMaterial(glassUUID);
    }

    public Material getBaseMaterial() {
        if (!exists(baseUUID)) return DEFAULT_BASE;
        return getMaterial(baseUUID);
    }

    private Material getMaterial(UUID uuid) {
        return getBlockDisplay(uuid).getBlock().getMaterial();
    }

    private void setMaterial(UUID uuid, Material material) {
        if (!exists(uuid)) return;
        getBlockDisplay(uuid).setBlock(material.createBlockData());
    }

    private Transformation getGlassTransformation(boolean isChest) {
        return isChest
                ? new Transformation(
                new Vector3f(-.3f, -0.05f, -.3f), ZERO_AXIS_4F,
                new Vector3f(0.6f, 0.6f, 0.6f), ZERO_AXIS_4F)
                : new Transformation(
                new Vector3f(-.3f, .075f, -.3f), ZERO_AXIS_4F,
                new Vector3f(0.6f, 0.6f, 0.6f), ZERO_AXIS_4F);
    }

    private Transformation getBaseTransformation(boolean isChest) {
        return isChest
                ? new Transformation(
                new Vector3f(-.35f, -.15f, -.35f), ZERO_AXIS_4F,
                new Vector3f(0.7f, 0.1f, 0.7f), ZERO_AXIS_4F)
                : new Transformation(
                new Vector3f(-.4f, -.025f, -.4f), ZERO_AXIS_4F,
                new Vector3f(.8f, .1f, .8f), ZERO_AXIS_4F);
    }

    private Vector3f getItemScale(Material material) {
        if (material == Material.WITHER_SKELETON_SKULL) {
            return new Vector3f(.5f, .5f, .5f);
        }
        if (material.isBlock()) {
            return new Vector3f(.3f, .3f, .3f);
        }
        return new Vector3f(.4f, .4f, .4f);
    }

    private Transformation getItemTransformation(Material material, boolean isChest) {
        Vector3f scale = getItemScale(material);
        return isChest
                ? new Transformation(new Vector3f(0f, .25f, 0f), ZERO_AXIS_4F, scale, ZERO_AXIS_4F)
                : new Transformation(new Vector3f(0f, .375f, 0f), ZERO_AXIS_4F, scale, ZERO_AXIS_4F);
    }

    public static boolean isValidSaleText(String saleText) {
        return saleText.length() <= MAX_SALE_TEXT_LENGTH;
    }

    public static GlassDisplay deserialize(Map<String, Object> map) {
        String glass = (String) map.get("glass");
        String base = (String) map.get("base");
        String item = (String) map.get("item");
        String text = (String) map.get("text");
        UUID glassUUID = glass != null ? UUID.fromString(glass) : null;
        UUID baseUUID = base != null ? UUID.fromString(base) : null;
        UUID itemUUID = item != null ? UUID.fromString(item) : null;
        UUID textUUID = text != null ? UUID.fromString(text) : null;
        return new GlassDisplay(glassUUID, baseUUID, itemUUID, textUUID);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        if (glassUUID != null) map.put("glass", glassUUID.toString());
        if (baseUUID != null) map.put("base", baseUUID.toString());
        if (itemUUID != null) map.put("item", itemUUID.toString());
        if (textUUID != null) map.put("text", textUUID.toString());
        return map;
    }
}
